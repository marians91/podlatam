package com.enel.platform.mepodlatam.batch.filters.application.edes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;

@ExtendWith(MockitoExtension.class)
class EDESDtWoaCreationIncrementalFilterTest {

	@Test
	void getCondition() {
		FilterColumnMappingConfig.removeAllColumnMapping();
		EDESDtWoaCreationIncrementalFilter dtWoaCreationFilter  = new EDESDtWoaCreationIncrementalFilter();
		ApplicationFilterParser applicationParser = new ApplicationFilterParser();
		applicationParser.doMatch("incremental#field=dtWoaCreation;value1=2021-01-01T00:00:00.000Z");
		final String whereConditionExptected = " tmwr.woa_dt_creation > '2021-01-01 01:00:00.000+0100'| tmworr.dt_creation > '2021-01-01 01:00:00.000+0100'";		
		String whereCondition = dtWoaCreationFilter.getCondition(applicationParser);		
		assertEquals(whereConditionExptected, whereCondition);
	}	
	
	@Test
	void getFilterName() {
		EDESDtWoaCreationIncrementalFilter dtWoaCreationFilter  = new EDESDtWoaCreationIncrementalFilter();		
		final String filterNameExptected = "DD01#incremental#dtWoaCreation";
		final String filterName =  dtWoaCreationFilter.getFilterName();
		assertEquals(filterNameExptected, filterName);
	}
	
}
