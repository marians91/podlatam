package com.enel.platform.mepodlatam.batch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.repository.PodIncrementalHandlerRepository;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO;
import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO.EntityFilterDTO;
import com.enel.platform.mepodlatam.model.PodIncrementalHandler;



@ExtendWith(MockitoExtension.class)
class PodRequestIncrementalHandlerServiceImplTest {
	
	@Mock
	private PodIncrementalHandlerRepository visitRequestIncrementalHandlerRepository;
	
	@Test
	void updateOrCreateLastValueForCreating() {
		
		String entityName = "entityTest";
		when(visitRequestIncrementalHandlerRepository.searchByEntityName(entityName)).thenReturn(null);
		IncrementalFilterDTO incrementalFilter = new IncrementalFilterDTO();
		EntityFilterDTO entityFilter = new EntityFilterDTO();
		entityFilter.setField("dtWoaCreation");
		entityFilter.setValue("2020-01-01T15:00:00");
		incrementalFilter.setEntityFilter(entityFilter);
		PodIncrementalHandler visitRequestIncrementalHandler = new PodIncrementalHandler();
		visitRequestIncrementalHandler.setEntityName(entityName);
		visitRequestIncrementalHandler.setField(incrementalFilter.getEntityFilter().getField());
		visitRequestIncrementalHandler.setValue(incrementalFilter.getEntityFilter().getValue());		
		doNothing().when(visitRequestIncrementalHandlerRepository).addEntityLastValue(visitRequestIncrementalHandler);		
		PodIncrementalHandlerServiceImpl visitRequestIncrementalHandlerService = new PodIncrementalHandlerServiceImpl(visitRequestIncrementalHandlerRepository);
		visitRequestIncrementalHandlerService.updateOrCreateLastValue(entityName, incrementalFilter);		
		verify(visitRequestIncrementalHandlerRepository, times(1)).addEntityLastValue(visitRequestIncrementalHandler);
		
		
	}
	
	@Test
	void updateOrCreateLastValueForUpdating() {
		
		String entityName = "entityTest";
		
		PodIncrementalHandler visitRequestIncrementalHandler = new PodIncrementalHandler();
		visitRequestIncrementalHandler.setEntityName(entityName);
		visitRequestIncrementalHandler.setField("dtWoaCreation");
		visitRequestIncrementalHandler.setValue("2021-07-01 12:00:12");	
	    when(visitRequestIncrementalHandlerRepository.searchByEntityName(entityName)).thenReturn(visitRequestIncrementalHandler);
		
		IncrementalFilterDTO incrementalFilter = new IncrementalFilterDTO();
		EntityFilterDTO entityFilter = new EntityFilterDTO();
		entityFilter.setField("dtWoaCreation");
		entityFilter.setValue("2020-01-01T15:00:00");
		incrementalFilter.setEntityFilter(entityFilter);
		doNothing().when(visitRequestIncrementalHandlerRepository).modifyEntityLastValue(entityName, incrementalFilter.getEntityFilter().getValue());		

		PodIncrementalHandlerServiceImpl visitRequestIncrementalHandlerService = new PodIncrementalHandlerServiceImpl(visitRequestIncrementalHandlerRepository);
		visitRequestIncrementalHandlerService.updateOrCreateLastValue(entityName, incrementalFilter);
		
		verify(visitRequestIncrementalHandlerRepository, times(1)).modifyEntityLastValue(entityName, incrementalFilter.getEntityFilter().getValue());
		
		
	}
	
	@Test
	void deleteLastValue() {
		String entityName = "entityTest";
		doNothing().when(visitRequestIncrementalHandlerRepository).deleteEntityLastValue(entityName);
		PodIncrementalHandlerServiceImpl visitRequestIncrementalHandlerService = new PodIncrementalHandlerServiceImpl(visitRequestIncrementalHandlerRepository);
		visitRequestIncrementalHandlerService.deleteLastValue(entityName);
		verify(visitRequestIncrementalHandlerRepository, times(1)).deleteEntityLastValue(entityName);
	}
	
	@Test
	void findLastValue() {		
		String entityName = "entityTest";
		PodIncrementalHandler visitRequestIncrementalHandler = new PodIncrementalHandler();
		visitRequestIncrementalHandler.setEntityName(entityName);
		visitRequestIncrementalHandler.setField("dtWoaCreation");
		visitRequestIncrementalHandler.setValue("2021-07-01T12:00:12");		
		when(visitRequestIncrementalHandlerRepository.searchByEntityName(entityName)).thenReturn(visitRequestIncrementalHandler);
		PodIncrementalHandlerServiceImpl visitRequestIncrementalHandlerService = new PodIncrementalHandlerServiceImpl(visitRequestIncrementalHandlerRepository);
		
		IncrementalFilterDTO incrementalFilter = visitRequestIncrementalHandlerService.findLastValue(entityName);
		
		assertEquals(visitRequestIncrementalHandler.getField(), incrementalFilter.getEntityFilter().getField());
		assertEquals(visitRequestIncrementalHandler.getValue(), incrementalFilter.getEntityFilter().getValue());
		
	}

}
