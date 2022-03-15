package com.enel.platform.mepodlatam.batch.filters.application.edes;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;

@ExtendWith(MockitoExtension.class)
class EDESDtWoaCreationFilterTest {

	@Test
	void getConditionWithTwoValues() {
		FilterColumnMappingConfig.removeAllColumnMapping();
		EDESDtWoaCreationFilter dtWoaCreationFilter  = new EDESDtWoaCreationFilter();
		ApplicationFilterParser applicationParser = new ApplicationFilterParser();
		applicationParser.doMatch("field=dtWoaCreation;value1=20210101;value2=20211231");
		final String whereConditionExptected = " tmwr.woa_dt_creation >= '2021-01-01 00:00:00.000+0100' AND tmwr.woa_dt_creation <= '2021-12-31 23:59:59.000+0100'| tmworr.dt_creation >= '2021-01-01 00:00:00.000+0100' AND tmworr.dt_creation <= '2021-12-31 23:59:59.000+0100'";		
		String whereCondition = dtWoaCreationFilter.getCondition(applicationParser);		
		assertEquals(whereConditionExptected, whereCondition);
	}
	
	@Test
	void getConditionWithOneValue() {
		FilterColumnMappingConfig.removeAllColumnMapping();
		EDESDtWoaCreationFilter dtWoaCreationFilter  = new EDESDtWoaCreationFilter();
		ApplicationFilterParser applicationParser = new ApplicationFilterParser();
		applicationParser.doMatch("field=dtWoaCreation;value1=20210101");
		final String whereConditionExptected = " tmwr.woa_dt_creation >= '2021-01-01 00:00:00.000+0100'| tmworr.dt_creation >= '2021-01-01 00:00:00.000+0100'";		
		String whereCondition = dtWoaCreationFilter.getCondition(applicationParser);		
		assertEquals(whereConditionExptected, whereCondition);
	}

	@Test
	void getFilterName() {
		EDESDtWoaCreationFilter dtWoaCreationFilter  = new EDESDtWoaCreationFilter();		
		final String filterNameExptected = "DD01#dtWoaCreation";
		final String filterName =  dtWoaCreationFilter.getFilterName();
		assertEquals(filterNameExptected, filterName);
	}
	
}
