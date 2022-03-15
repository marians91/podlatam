package com.enel.platform.mepodlatam.batch.filters.application.mapping.edes;

import java.util.HashMap;
import java.util.Map;

import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMapping;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.annotation.FilterColumnMap;

@FilterColumnMap
public class EDESFilterColumnMapping implements FilterColumnMapping {

	public static final String DT_WOA_CREATION_FIELD = "dtWoaCreation";
	public static final String DB_NAME = "EDES#";
	public static final String DT_WOA_CREATION_FILTER = DB_NAME.concat(DT_WOA_CREATION_FIELD);
	public static final String DT_WOA_CREATION_INCREMENTAL_FILTER = DB_NAME.concat(FilterColumnMappingConfig.INCREMENTAL).concat(DT_WOA_CREATION_FIELD);
	public static final String DT_WOA_CREATION_COLUMN = "dt_creation";
	public static final String QUERIES_FILE_PATH = "com.enel.sql.EDES";

	@Override
	public Map<String, String> map() {
		Map<String, String> mapColumns = new HashMap<>();
		mapColumns.put(DT_WOA_CREATION_FILTER, DT_WOA_CREATION_COLUMN);
		mapColumns.put(DT_WOA_CREATION_INCREMENTAL_FILTER, DT_WOA_CREATION_COLUMN);
		return mapColumns;
	}

}
