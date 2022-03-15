package com.enel.platform.mepodlatam.batch.filters.application.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;

@ExtendWith(MockitoExtension.class)
class BaseFilteringHelperTest {

	@Test
	void buildIncrementalFilter() {
				
		String fieldName = "dtLastModify";
		String fieldValue = "20220101";
		String incrementalFilterString = FilterColumnMappingConfig.INCREMENTAL + "field="+fieldName + ";value1=" + fieldValue;
		String result = BaseFilteringHelper.buildIncrementalFilter(fieldName, fieldValue);
		assertEquals(incrementalFilterString, result);
	}
	
	@Test
	void getDateTimeInDBFormat() {		
		BaseFilteringHelper filteringHelper = Mockito.spy(BaseFilteringHelper.class);
		filteringHelper.validateDateFilter("20220101", Optional.empty());		
	}
	
	@Test
	void getDateTimeInDBFormatThrowsApplicationFilterException() {		
		BaseFilteringHelper filteringHelper = Mockito.spy(BaseFilteringHelper.class);
		String date = "2022-01-01";
		String dateFormat ="yyyyMMdd";
		String expectedMessage = String.format("Date %s does not match format %s", date, dateFormat);
		Exception exception = assertThrows(ApplicationFilterException.class, () -> {
			 filteringHelper.validateDateFilter(date, Optional.empty());
		    });		
		assertEquals(expectedMessage, exception.getMessage());	
	}
	
	@Test
	void getDateTimeInDBFormatPassingDateFormat() {		
		BaseFilteringHelper filteringHelper = Mockito.spy(BaseFilteringHelper.class);
		filteringHelper.validateDateFilter("01-01-2022",Optional.of("dd-mm-yyyy"));		
	}
	
	@Test
	void getDateTimeInDBFormatPassingDateFormatThrowsApplicationFilterException() {		
		BaseFilteringHelper filteringHelper = Mockito.spy(BaseFilteringHelper.class);
		String date = "20220101";
		String dateFormat ="yyyy-MM-dd";
		String expectedMessage = String.format("Date %s does not match format %s", date, dateFormat);
		Exception exception = assertThrows(ApplicationFilterException.class, () -> {
			 filteringHelper.validateDateFilter(date, Optional.of(dateFormat));
		    });		
		assertEquals(expectedMessage, exception.getMessage());	
	}
}
