package com.enel.platform.mepodlatam.batch.filters.application.mapping.edes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EDESFilterColumnMappingTest {

	@Test
	void map() {
		EDESFilterColumnMapping filterColumnMapping = new EDESFilterColumnMapping();
		Map<String, String> mapColumns = filterColumnMapping.map();
		assertEquals(2, mapColumns.size());
		assertEquals(true, mapColumns.containsKey(EDESFilterColumnMapping.DT_WOA_CREATION_FILTER));
		assertEquals(EDESFilterColumnMapping.DT_WOA_CREATION_COLUMN,
				mapColumns.get(EDESFilterColumnMapping.DT_WOA_CREATION_FILTER));
		assertEquals(true, mapColumns.containsKey(EDESFilterColumnMapping.DT_WOA_CREATION_INCREMENTAL_FILTER));
		assertEquals(EDESFilterColumnMapping.DT_WOA_CREATION_COLUMN,
				mapColumns.get(EDESFilterColumnMapping.DT_WOA_CREATION_INCREMENTAL_FILTER));
	}

}
