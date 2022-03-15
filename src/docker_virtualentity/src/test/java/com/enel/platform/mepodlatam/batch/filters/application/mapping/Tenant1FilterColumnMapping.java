package com.enel.platform.mepodlatam.batch.filters.application.mapping;

import java.util.HashMap;
import java.util.Map;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.annotation.FilterColumnMap;

@FilterColumnMap
public class Tenant1FilterColumnMapping implements FilterColumnMapping {

	public static final String DB_NAME = "Tenant1#";
	public static final String FIELD_NAME = DB_NAME.concat("fieldAlias");
	public static final String COLUMN_NAME = "columnName";

	@Override
	public Map<String, String> map() {
		Map<String, String> mapColumns = new HashMap<>();
		mapColumns.put(FIELD_NAME, COLUMN_NAME);
		return mapColumns;
	}

}
