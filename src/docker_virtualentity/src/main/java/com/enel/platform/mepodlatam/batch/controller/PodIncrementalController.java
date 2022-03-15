package com.enel.platform.mepodlatam.batch.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.enel.platform.entity.model.batch.WriteExecutionPlanRequestDTO;
import com.enel.platform.entity.service.batch.BatchService;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.service.PodIncrementalHandlerService;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO;

public abstract class PodIncrementalController<T> extends PodController<T> {

	private final PodIncrementalHandlerService podIncrementalHandlerService;
	private final String entityName;

	protected PodIncrementalController(BatchService<T> batchService, String entityName,
			PodIncrementalHandlerService podIncrementalHandlerService) {
		super(batchService);
		this.entityName = entityName;
		this.podIncrementalHandlerService = podIncrementalHandlerService;
	}
		
	@GetMapping(value = "/process-state/{process-id}")
	protected ResponseEntity<IncrementalFilterDTO> getLastValueExtract(@PathVariable("process-id") String processId) {
		IncrementalFilterDTO incrementalFilter = podIncrementalHandlerService.findLastValue(entityName);
		return ResponseEntity.status(HttpStatus.OK).body(incrementalFilter);
	}

	@PutMapping(value = "/process-state/{process-id}")
	@ResponseStatus(value = HttpStatus.CREATED)
	protected void updateLastValueExtract(@PathVariable("process-id") String processId,
			@Valid @RequestBody IncrementalFilterDTO incrementalFilter) {
		podIncrementalHandlerService.updateOrCreateLastValue(entityName, incrementalFilter);		
	}

	@DeleteMapping(value = "/process-state")
	@ResponseStatus(value = HttpStatus.OK)
	protected void deleteLastValueExtract() {
		podIncrementalHandlerService.deleteLastValue(entityName);		
	}

	protected WriteExecutionPlanRequestDTO removeIncrementalByApplicationFilter(WriteExecutionPlanRequestDTO body) {
		if (body.getApplicationFilter().isPresent() && !body.getApplicationFilter().isEmpty()) {
			String filter = body.getApplicationFilter().get().replace(FilterColumnMappingConfig.INCREMENTAL, "");
			return new WriteExecutionPlanRequestDTO(Optional.of(filter), body.getDataGovernanceFilter(),
					body.getSource());
		} else {
			return body;
		}
	}

}
