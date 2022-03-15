package com.enel.platform.mepodlatam.batch.controller;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.enel.platform.entity.exception.batch.PartitionFilterException;
import com.enel.platform.entity.model.batch.DataRequest;
import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequest;
import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequestDTO;
import com.enel.platform.entity.model.batch.ExtractionExecutionPlanResponseDTO;
import com.enel.platform.entity.model.batch.FlightInfo;
import com.enel.platform.entity.model.batch.FlightInfoRequest;
import com.enel.platform.entity.model.batch.OnDemandAccessRequest;
import com.enel.platform.entity.model.batch.OnDemandAccessRequestDTO;
import com.enel.platform.entity.model.batch.OnDemandAccessResponse;
import com.enel.platform.entity.model.batch.OnDemandAccessResponseDTO;
import com.enel.platform.entity.model.batch.WriteExecutionPlanRequest;
import com.enel.platform.entity.model.batch.WriteExecutionPlanRequestDTO;
import com.enel.platform.entity.model.batch.WriteExecutionPlanResponseDTO;
import com.enel.platform.entity.model.batch.WriteResultCommittedResponseDTO;
import com.enel.platform.entity.model.batch.WriteResultCompleteRequest;
import com.enel.platform.entity.model.batch.WriteResultCompleteRequestDTO;
import com.enel.platform.entity.service.batch.BatchService;
import com.enel.platform.mepodlatam.dto.RestErrorResponseDTO;
import com.enel.platform.mepodlatam.json.adapter.DateAdapter;
import com.enel.platform.mepodlatam.json.adapter.ZonedDateTimeAdapter;
import com.enel.platform.mepodlatam.openapi.schemas.PodSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PodController<T> {

	private final BatchService<T> batchService;

	@Tag(name = "batch read", description = "batch read-related APIs")
	@Operation(description = "Execution Plan, retrieves s3 path", responses = {
			@ApiResponse(responseCode = "200", description = "Information about location of data source"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = RestErrorResponseDTO.class))) })
	@PostMapping(value = "executionPlan")
	public ExtractionExecutionPlanResponseDTO executionPlan(HttpServletRequest request,
			@Valid @RequestBody ExtractionExecutionPlanRequestDTO req) {

		return batchService.getExecutionPlan(ExtractionExecutionPlanRequest.fromRequestDTO(req)).toResponseDTO();
	}

	@Tag(name = "batch read", description = "batch read-related APIs")
	@Operation(description = "Retrieve the partition info", responses = {
			@ApiResponse(responseCode = "200", description = "Information about partitions"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = RestErrorResponseDTO.class))) })
	@GetMapping(value = "flightInfo")
	public FlightInfo getFlightInfo(HttpServletRequest request,
			@RequestParam(name = "applicationFilter", defaultValue = "") String applicationFilter) {

		return this.batchService.createFlightInfo(FlightInfoRequest.toRequest(applicationFilter));
	}

	@Tag(name = "batch read", description = "batch read-related APIs")
	@Operation(description = "Retrieve the data for given application, partition and dataGovernance filters. In streaming mode.", responses = {
			@ApiResponse(responseCode = "200", description = "A data partition", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = PodSchema.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = RestErrorResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "The filter is not valid", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = RestErrorResponseDTO.class))) })
	@GetMapping(value = "data/stream")
	public ResponseEntity<StreamingResponseBody> getDataStream(HttpServletRequest request,
			@RequestParam(name = "partitionFilter") String partitionFilter,
			@RequestParam(name = "applicationFilter", defaultValue = "") String applicationFilter,
			@RequestParam(name = "dataGovernanceFilter", defaultValue = "") String dataGovernanceFilter)
			throws PartitionFilterException {

		DataRequest dataRequest = DataRequest.toDataRequest(partitionFilter, applicationFilter, dataGovernanceFilter);
		Iterator<T> i = batchService.scanByPartition(dataRequest);
		Gson gson = getGsonBuilder().create();

		StreamingResponseBody streamingResponseBody = outputStream -> {
			while (i.hasNext()) {
				outputStream.write((gson.toJson(i.next()) + "\n").getBytes());
			}
			outputStream.close();
		};

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_NDJSON).body(streamingResponseBody);
	}

	@Tag(name = "batch write", description = "batch write-related APIs")
	@Operation(description = " WriteExecution Plan, returns write type (Hot/Cold), in case of Cold write returns an s3 path", responses = {
			@ApiResponse(responseCode = "200", description = "Write type, if Cold body contains s3 path"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = RestErrorResponseDTO.class))) })
	@PostMapping(value = "writeExecutionPlan")
	public WriteExecutionPlanResponseDTO writeExecutionPlan(HttpServletRequest request,
			@RequestBody WriteExecutionPlanRequestDTO body) {

		return batchService.getWriteExecutionPlan(WriteExecutionPlanRequest.fromRequestDTO(body)).toResponseDTO();
	}

	@Tag(name = "batch write", description = "batch write-related APIs")
	@Operation(description = " OnDemand Access, returns tempoprary credentials and s3 path", responses = {
			@ApiResponse(responseCode = "200", description = "S3 path and Temporary credentials"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "Result", implementation = RestErrorResponseDTO.class))) })
	@PostMapping(value = "onDemandAccess")
	public OnDemandAccessResponseDTO getOnDemandAccess(HttpServletRequest request,
			@RequestBody OnDemandAccessRequestDTO body,
			@RequestHeader(value = "X-PLT-Solution-User") String solutionUser) {
		OnDemandAccessResponse response = batchService.getOnDemandAccess(OnDemandAccessRequest.fromRequestDTO(body),
				solutionUser);
		return response.toResponseDTO();
	}

	@PostMapping(value = "data/complete")
	public void completeWriteHOT(@RequestBody WriteResultCompleteRequestDTO req,
			@RequestHeader(value = "X-PLT-Correlation-Id", required = false) String correlationId) {
		batchService.completeWriteResult(correlationId, WriteResultCompleteRequest.fromRequestDTO(req));
	}

	@GetMapping(value = "data/committed")
	public WriteResultCommittedResponseDTO writeCommitStatus(
			@RequestHeader(value = "X-PLT-Correlation-Id") String correlationId) {
		return batchService.writeResultCommited(correlationId).toResponseDTO();
	}

	protected GsonBuilder getGsonBuilder() {
		return new GsonBuilder().disableHtmlEscaping().serializeNulls()
				.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
				.registerTypeAdapter(Date.class, new DateAdapter());
	}

}
