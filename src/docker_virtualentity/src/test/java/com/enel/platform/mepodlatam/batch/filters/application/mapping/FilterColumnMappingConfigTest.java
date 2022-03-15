package com.enel.platform.mepodlatam.batch.filters.application.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.exception.FilterColumnMappingException;

@ExtendWith(MockitoExtension.class)
class FilterColumnMappingConfigTest {

	@Test
	void getColumNameByEntityAndAliasWithEmptyMapping() {
		FilterColumnMappingConfig.removeAllColumnMapping();
		final String columnNameExpected = "columnName";
		final String fieldAlias = "fieldAlias";
		String columnName = FilterColumnMappingConfig.getColumNameByDBAndAlias(Tenant1FilterColumnMapping.DB_NAME,
				fieldAlias);
		assertEquals(columnNameExpected, columnName);
	}
	
	@Test
	void getColumNameByEntityAndAliasWithNoEmptyMapping() {
		FilterColumnMappingConfig.removeAllColumnMapping();
        final Map<String,String> columnMap = new HashMap<>();
        columnMap.put(Tenant1FilterColumnMapping.FIELD_NAME, Tenant1FilterColumnMapping.COLUMN_NAME);
        FilterColumnMappingConfig.mappingAll(columnMap);        
		final String columnNameExpected = "columnName";
		final String fieldAlias = "fieldAlias";
		String columnName = FilterColumnMappingConfig.getColumNameByDBAndAlias(Tenant1FilterColumnMapping.DB_NAME,
				fieldAlias);
		assertEquals(columnNameExpected, columnName);
	}
	
	@Test
	void getColumNameByEntityAndAliasThrowFilterColumnMappingException() {
		FilterColumnMappingConfig.removeAllColumnMapping();
        final String fieldAlias = "fieldAlias";
        final String dbName = "Tenant3DB";
		final String expectedMessage = String.format("Filter field %s no mapped with any db column", fieldAlias);
		Exception exception = assertThrows(FilterColumnMappingException.class, () -> {
			FilterColumnMappingConfig.getColumNameByDBAndAlias(dbName, fieldAlias);
		    });		
		assertEquals(expectedMessage, exception.getMessage());
	}
}
