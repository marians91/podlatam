package com.enel.platform.mepodlatam.batch.service;

import com.enel.platform.mepodlatam.dto.IncrementalFilterDTO;

public interface PodIncrementalHandlerService {

	void updateOrCreateLastValue(String entityName, IncrementalFilterDTO incrementalFilter);	
	
	void deleteLastValue(String entityName);
	
	IncrementalFilterDTO findLastValue(String entityName);
			
}
