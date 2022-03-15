package com.enel.platform.mepodlatam.batch.filters.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.filters.application.exception.ApplicationFilterException;


@ExtendWith(MockitoExtension.class)
class FilterFactoryTest {

	@Test
	void getFilterWithEmptyFilterMap() {
		FilterFactory.removeAllFilters();
		final String filterName = "Tenant1DB#dtWoaCreation";
		Filter filter = FilterFactory.getFilter(filterName);		
		assertEquals(filterName, filter.getFilterName());
	}

	@Test
	void getFilterWithNoEmptyFilterMap() {
		FilterFactory.removeAllFilters();
		final String filterName = "Tenant2DB#dtWoaCreation";
		Tenant2DtWoaCreationFilter filter = new Tenant2DtWoaCreationFilter();
		FilterFactory.registerFilter(filterName, filter);
		Filter filterExpected = FilterFactory.getFilter(filterName);
		assertEquals(filterName, filterExpected.getFilterName());

	}
	
	@Test
	void getFilterThrowApplicationException() {
		FilterFactory.removeAllFilters();
		final String filterName = "Tenant3DB#dtWoaCreation";
		final String expectedMessage = "Filter is not allowed";
		Exception exception = assertThrows(ApplicationFilterException.class, () -> {
			FilterFactory.getFilter(filterName);
		    });		
		assertEquals(expectedMessage, exception.getMessage());

	}
}
