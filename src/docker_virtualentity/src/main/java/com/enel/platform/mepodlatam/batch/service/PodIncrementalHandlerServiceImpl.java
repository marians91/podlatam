package com.enel.platform.mepodlatam.batch.service;

import org.springframework.stereotype.Service;

import com.enel.platform.mepodlatam.batch.repository.PodIncrementalHandlerRepository;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO.EntityFilterDTO;
import com.enel.platform.mepodlatam.model.PodIncrementalHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PodIncrementalHandlerServiceImpl implements PodIncrementalHandlerService {

	private final PodIncrementalHandlerRepository podIncrementalHandlerRepository;

	@Override
	public void updateOrCreateLastValue(String entityName, IncrementalFilterDTO incrementalFilter) {

		PodIncrementalHandler podIncrementalHandler = podIncrementalHandlerRepository
				.searchByEntityName(entityName);

		if (podIncrementalHandler != null) {
			podIncrementalHandlerRepository.modifyEntityLastValue(entityName,
					incrementalFilter.getEntityFilter().getValue());
		} else {
			podIncrementalHandler = new PodIncrementalHandler();
			podIncrementalHandler.setEntityName(entityName);
			podIncrementalHandler.setField(incrementalFilter.getEntityFilter().getField());
			podIncrementalHandler.setValue(incrementalFilter.getEntityFilter().getValue());
			podIncrementalHandlerRepository.addEntityLastValue(podIncrementalHandler);
		}
	}

	@Override
	public void deleteLastValue(String entityName) {
		podIncrementalHandlerRepository.deleteEntityLastValue(entityName);
	}

	@Override
	public IncrementalFilterDTO findLastValue(String entityName) {
		PodIncrementalHandler podIncrementalHandler = podIncrementalHandlerRepository
				.searchByEntityName(entityName);
		IncrementalFilterDTO incrementalFilter = new IncrementalFilterDTO();
		EntityFilterDTO entityFilter = new EntityFilterDTO();
		
		if (podIncrementalHandler == null) {
			return incrementalFilter;
		}
		
		entityFilter.setField(podIncrementalHandler.getField());
		entityFilter.setValue(podIncrementalHandler.getValue());
		entityFilter.setEntityName(podIncrementalHandler.getEntityName());
		incrementalFilter.setEntityFilter(entityFilter);
		incrementalFilter.setDtLastModify(podIncrementalHandler.getDtLastModify());
		
		return incrementalFilter;

	}

}
