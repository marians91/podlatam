package com.enel.platform.mepodlatam.batch.controller.edes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enel.platform.entity.conf.S3Config;
import com.enel.platform.entity.model.batch.CommitStatus;
import com.enel.platform.entity.model.batch.Credentials;
import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequest;
import com.enel.platform.entity.model.batch.ExtractionExecutionPlanRequestDTO;
import com.enel.platform.entity.model.batch.ExtractionType;
import com.enel.platform.entity.model.batch.FlightInfo;
import com.enel.platform.entity.model.batch.OnDemandAccessRequestDTO;
import com.enel.platform.entity.model.batch.Source;
import com.enel.platform.entity.model.batch.WriteExecutionPlanRequestDTO;
import com.enel.platform.entity.model.batch.WriteResultCompleteRequest;
import com.enel.platform.entity.model.batch.WriteResultCompleteRequestDTO;
import com.enel.platform.entity.model.batch.WriteType;
import com.enel.platform.entity.repository.batch.BatchStagingArea;
import com.enel.platform.entity.utils.batch.ExecutionPlanHelper;
import com.enel.platform.entity.utils.batch.TemporaryCredentialsHelper;
import com.enel.platform.mepodlatam.batch.controller.PodRequestIncrementalControllerTest;
import com.enel.platform.mepodlatam.batch.controller.dto.ExtractionExecutionPlanHotResponseDTO;
import com.enel.platform.mepodlatam.batch.controller.dto.ExtractionExecutionPlanResponseDTO;
import com.enel.platform.mepodlatam.batch.controller.dto.OnDemandAccessResponseDTO;
import com.enel.platform.mepodlatam.batch.controller.dto.WriteExecutionPlanResponseColdDTO;
import com.enel.platform.mepodlatam.batch.controller.dto.WriteResultCommittedResponseDTO;
import com.enel.platform.mepodlatam.batch.controller.support.MapperJson;
import com.enel.platform.mepodlatam.batch.repository.PodBatchRepository;
import com.enel.platform.mepodlatam.batch.s3.PodS3Helper;
import com.enel.platform.mepodlatam.model.edes.EDESPod;

@WebMvcTest(controllers = EDESPodController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { EDESControllerTestConfiguration.class })
@ExtendWith(SpringExtension.class)
class EDESPodRequestControllerTest extends PodRequestIncrementalControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private S3Config s3Config;

	@MockBean
	@Qualifier("PodRequestTemporaryCredentialsHelper")
	private TemporaryCredentialsHelper temporaryCredentialsHelper;

	@MockBean
	@Qualifier("EDEDPodRequestPlanHelper")
	private ExecutionPlanHelper executionPlanHelper;

	@MockBean
	private BatchStagingArea<EDESPod> visitRequestBatchStagingAreaService;

	@MockBean
	private PodBatchRepository<EDESPod> visitRequestRepository;

	@MockBean
	@Qualifier("EDESPodRequestS3Helper")
	private PodS3Helper s3Helper;

	private static final String ENTITY_NAME = "edes_podrequest";
	private static final String ENTITY_VERSION = "v1";

	public EDESPodRequestControllerTest(@Autowired MockMvc mockMvc) {
		super(mockMvc, ENTITY_NAME, ENTITY_VERSION);
		this.mockMvc = mockMvc;
	}

	@Test
	void executionPlanExternalHot() throws Exception {
		when(this.temporaryCredentialsHelper.generateWriteTemporaryCredentials(anyString()))
				.thenReturn(new Credentials("mockAccessKey", "mockSecretKey", "mockSessionToken"));
		ExtractionExecutionPlanRequestDTO extractionExecutionPlanRequestDTO = new ExtractionExecutionPlanRequestDTO(
				Optional.empty(), Optional.empty(), Source.External);
		when(executionPlanHelper.getExtractionType(any(ExtractionExecutionPlanRequest.class)))
				.thenReturn(ExtractionType.Hot);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.post("/batch/{batch.api.name}/{version}/executionPlan", ENTITY_NAME, ENTITY_VERSION)
				.contentType(MediaType.APPLICATION_JSON)
				.content(MapperJson.mapToJson(extractionExecutionPlanRequestDTO)));
		res.andExpect(MockMvcResultMatchers.status().isOk());
		ExtractionExecutionPlanHotResponseDTO extractionExecutionPlanResponseDTO = MapperJson.mapFromJson(
				res.andReturn().getResponse().getContentAsString(), ExtractionExecutionPlanHotResponseDTO.class);
		assertEquals(ExtractionType.Hot, extractionExecutionPlanResponseDTO.getExtractionType());
	}

	@Test
	void executionPlanExternalCold() throws Exception {
		when(this.temporaryCredentialsHelper.generateWriteTemporaryCredentials(anyString()))
				.thenReturn(new Credentials("mockAccessKey", "mockSecretKey", "mockSessionToken"));
		ExtractionExecutionPlanRequestDTO extractionExecutionPlanRequestDTO = new ExtractionExecutionPlanRequestDTO(
				Optional.empty(), Optional.empty(), Source.External);
		when(executionPlanHelper.getExtractionType(any(ExtractionExecutionPlanRequest.class)))
				.thenReturn(ExtractionType.Cold);
		URIBuilder officialPath = new URIBuilder("s3://fakebucket/entity");
		when(s3Helper.getOfficialPath()).thenReturn(officialPath.build());
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.post("/batch/{batch.api.name}/{version}/executionPlan", ENTITY_NAME, ENTITY_VERSION)
				.contentType(MediaType.APPLICATION_JSON)
				.content(MapperJson.mapToJson(extractionExecutionPlanRequestDTO)));
		res.andExpect(MockMvcResultMatchers.status().isOk());
		ExtractionExecutionPlanResponseDTO extractionExecutionPlanResponseDTO = MapperJson.mapFromJson(
				res.andReturn().getResponse().getContentAsString(), ExtractionExecutionPlanResponseDTO.class);
		assertEquals(ExtractionType.Cold, extractionExecutionPlanResponseDTO.getExtractionType());
	}

	@Test
	void flightInfo() throws Exception {
		when(visitRequestRepository.tableSize(ENTITY_NAME)).thenReturn(10L);
		ResultActions res = mockMvc.perform(
				MockMvcRequestBuilders.get("/batch/{batch.api.name}/{version}/flightInfo", ENTITY_NAME, ENTITY_VERSION)
						.contentType(MediaType.APPLICATION_JSON));

		res.andExpect(MockMvcResultMatchers.status().isOk());
		FlightInfo flightInfo = MapperJson.mapFromJson(res.andReturn().getResponse().getContentAsString(),
				FlightInfo.class);
		assertEquals(1, flightInfo.getPartitions().length);
		assertEquals("partition_id:0,partition_size:1000", flightInfo.getPartitions()[0].getPartitionFilter());
	}

	@Test
	void flightInfoByApplicationFilter() throws Exception {
		String applicationFilter = "field=dtLastModify;value1=2021-05-01;value2=";
		when(visitRequestRepository.tableSize(ENTITY_NAME, Optional.of(applicationFilter).get())).thenReturn(10L);

		ResultActions res = mockMvc.perform(
				MockMvcRequestBuilders.get("/batch/{batch.api.name}/{version}/flightInfo", ENTITY_NAME, ENTITY_VERSION)
						.param("applicationFilter", applicationFilter).contentType(MediaType.APPLICATION_JSON));

		res.andExpect(MockMvcResultMatchers.status().isOk());
		FlightInfo flightInfo = MapperJson.mapFromJson(res.andReturn().getResponse().getContentAsString(),
				FlightInfo.class);
		assertEquals(1, flightInfo.getPartitions().length);
		assertEquals("partition_id:0,partition_size:1000", flightInfo.getPartitions()[0].getPartitionFilter());
	}

	@Test
	void getDataStream() throws Exception {
		String partitionFilter = "partition_id:0,partition_size:5000";
		String tenantId = "DD01";
		String pointCode = "14252379282";
		EDESPod visitRequest = new EDESPod();
		visitRequest.setTenant(tenantId);
		visitRequest.setPointCode(pointCode);
		List<EDESPod> visitRequests = Arrays.asList(visitRequest);
		when(visitRequestRepository.scanByPartition(ENTITY_NAME, 0, 5000)).thenReturn(visitRequests.iterator());

		ResultActions res = mockMvc.perform(
				MockMvcRequestBuilders.get("/batch/{batch.api.name}/{version}/data/stream", ENTITY_NAME, ENTITY_VERSION)
						.param("partitionFilter", partitionFilter).contentType(MediaType.APPLICATION_NDJSON));
		res.andExpect(request().asyncStarted()).andDo(MvcResult::getAsyncResult);
		res.andExpect(MockMvcResultMatchers.status().isOk());
		assertEquals(visitRequest.getTenant(), tenantId);
		assertEquals(visitRequest.getPointCode(), pointCode);

	}

	@Test
	void getDataStreamByFilter() throws Exception {
		String partitionFilter = "partition_id:0,partition_size:5000";
		String applicationFilter = "field=dtLastModify;value1=2021-05-01;value2=";
		String tenantId = "DD01";
		String pointCode = "14252379282";
		EDESPod visitRequest = new EDESPod();
		visitRequest.setTenant(tenantId);
		visitRequest.setPointCode(pointCode);
		List<EDESPod> visitRequests = Arrays.asList(visitRequest);
		when(visitRequestRepository.scanByPartition(ENTITY_NAME, 0, 5000, applicationFilter))
				.thenReturn(visitRequests.iterator());

		ResultActions res = mockMvc.perform(
				MockMvcRequestBuilders.get("/batch/{batch.api.name}/{version}/data/stream", ENTITY_NAME, ENTITY_VERSION)
						.param("partitionFilter", partitionFilter).param("applicationFilter", applicationFilter)
						.contentType(MediaType.APPLICATION_NDJSON));
		res.andExpect(request().asyncStarted()).andDo(MvcResult::getAsyncResult);
		res.andExpect(MockMvcResultMatchers.status().isOk());
		assertEquals(visitRequest.getTenant(), tenantId);
		assertEquals(visitRequest.getPointCode(), pointCode);

	}

	@Test
	void writeExecutionPlan() throws Exception {
		when(this.temporaryCredentialsHelper.generateWriteTemporaryCredentials(anyString()))
				.thenReturn(new Credentials("mockWriteAccessKey", "mockWriteSecretKey", "mockWriteSessionToken"));
		when(this.temporaryCredentialsHelper.generateReadTemporaryCredentials((anyString())))
				.thenReturn(new Credentials("mockReadAccessKey", "mockReadSecretKey", "mockReadSessionToken"));
		String uri = String.format("s3://bucketname/%s/ondemand/filter", ENTITY_NAME);
		Optional<String> filter = Optional.of("filter");
		when(this.s3Helper.getOfficialPath()).thenReturn(new URI(uri));
		WriteExecutionPlanRequestDTO writeExecutionPlanRequest = new WriteExecutionPlanRequestDTO(filter,
				Optional.empty(), Source.External);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.post("/batch/{batch.api.name}/{version}/writeExecutionPlan", ENTITY_NAME, ENTITY_VERSION)
				.contentType(MediaType.APPLICATION_JSON).content(MapperJson.mapToJson(writeExecutionPlanRequest)));
		res.andExpect(MockMvcResultMatchers.status().isOk());
		WriteExecutionPlanResponseColdDTO extractionExecutionPlanResponse = MapperJson.mapFromJson(
				res.andReturn().getResponse().getContentAsString(), WriteExecutionPlanResponseColdDTO.class);
		assertEquals(uri, extractionExecutionPlanResponse.getWriteUri());
		assertEquals(WriteType.Cold, extractionExecutionPlanResponse.getWriteType());
	}

	@Test
	void onDemandAccess() throws Exception {
		String solutionUser = "SOLUTION_USER_TEST";
		String extractionID = "12345";
		when(this.temporaryCredentialsHelper.generateWriteTemporaryCredentials(anyString()))
				.thenReturn(new Credentials("mockWriteAccessKey", "mockWriteSecretKey", "mockWriteSessionToken"));
		when(this.temporaryCredentialsHelper.generateReadTemporaryCredentials((anyString())))
				.thenReturn(new Credentials("mockReadAccessKey", "mockReadSecretKey", "mockReadSessionToken"));
		String uri = String.format("s3://bucketname/ondemand/%s/%s", solutionUser, extractionID);
		when(this.s3Helper.getOnDemandPath(solutionUser, extractionID)).thenReturn(new URI(uri));
		OnDemandAccessRequestDTO onDemandAccessRequest = new OnDemandAccessRequestDTO(extractionID);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.post("/batch/{batch.api.name}/{version}/onDemandAccess", ENTITY_NAME, ENTITY_VERSION)
				.header("X-PLT-Solution-User", solutionUser).contentType(MediaType.APPLICATION_JSON)
				.content(MapperJson.mapToJson(onDemandAccessRequest)));
		res.andExpect(MockMvcResultMatchers.status().isOk());
		OnDemandAccessResponseDTO onDemandAccessResponse = MapperJson
				.mapFromJson(res.andReturn().getResponse().getContentAsString(), OnDemandAccessResponseDTO.class);
		assertEquals(uri, onDemandAccessResponse.getFullPath());
	}

	@Test
	void completeDataFalse() throws Exception {
		String correlationId = "1";
		WriteResultCompleteRequestDTO writeResultCompleteRequestDTO = new WriteResultCompleteRequestDTO(false);
		WriteResultCompleteRequest writeResultCompleteRequest = WriteResultCompleteRequest
				.fromRequestDTO(writeResultCompleteRequestDTO);
		try (MockedStatic<WriteResultCompleteRequest> writeResultCompleteRequestMock = Mockito
				.mockStatic(WriteResultCompleteRequest.class)) {
			writeResultCompleteRequestMock
					.when(() -> WriteResultCompleteRequest.fromRequestDTO(writeResultCompleteRequestDTO))
					.thenReturn(writeResultCompleteRequest);
			ResultActions res = mockMvc.perform(MockMvcRequestBuilders
					.post("/batch/{batch.api.name}/{version}/data/complete", ENTITY_NAME, ENTITY_VERSION)
					.contentType(MediaType.APPLICATION_JSON).header("X-PLT-Correlation-Id", correlationId)
					.content(MapperJson.mapToJson(writeResultCompleteRequestDTO)));
			res.andExpect(MockMvcResultMatchers.status().isOk());
			verify(visitRequestBatchStagingAreaService, times(0)).commitAll(correlationId);
			verify(visitRequestBatchStagingAreaService, times(1)).unstageAll(correlationId);
		}
	}

	@Test
	void completeDataTrue() throws Exception {
		String correlationId = "1";
		WriteResultCompleteRequestDTO writeResultCompleteRequestDTO = new WriteResultCompleteRequestDTO(true);
		WriteResultCompleteRequest writeResultCompleteRequest = WriteResultCompleteRequest
				.fromRequestDTO(writeResultCompleteRequestDTO);
		try (MockedStatic<WriteResultCompleteRequest> writeResultCompleteRequestMock = Mockito
				.mockStatic(WriteResultCompleteRequest.class)) {
			writeResultCompleteRequestMock
					.when(() -> WriteResultCompleteRequest.fromRequestDTO(writeResultCompleteRequestDTO))
					.thenReturn(writeResultCompleteRequest);
			ResultActions res = mockMvc.perform(MockMvcRequestBuilders
					.post("/batch/{batch.api.name}/{version}/data/complete", ENTITY_NAME, ENTITY_VERSION)
					.contentType(MediaType.APPLICATION_JSON).header("X-PLT-Correlation-Id", correlationId)
					.content(MapperJson.mapToJson(writeResultCompleteRequestDTO)));
			res.andExpect(MockMvcResultMatchers.status().isOk());
			verify(visitRequestBatchStagingAreaService, times(1)).commitAll(correlationId);
			verify(visitRequestBatchStagingAreaService, times(0)).unstageAll(correlationId);
		}
	}

	@Test
	void dataCommitted() throws Exception {
		String correlationId = "1";
		when(visitRequestBatchStagingAreaService.getCommitStatus(correlationId)).thenReturn(CommitStatus.Success);
		ResultActions res = mockMvc.perform(MockMvcRequestBuilders
				.get("/batch/{batch.api.name}/{version}/data/committed", ENTITY_NAME, ENTITY_VERSION)
				.contentType(MediaType.APPLICATION_JSON).header("X-PLT-Correlation-Id", correlationId));
		res.andExpect(MockMvcResultMatchers.status().isOk());
		WriteResultCommittedResponseDTO writeResultCommittedResponse = MapperJson
				.mapFromJson(res.andReturn().getResponse().getContentAsString(), WriteResultCommittedResponseDTO.class);
		assertEquals(CommitStatus.Success, writeResultCommittedResponse.getCommitStatus());
	}
}