package com.enel.platform.mepodlatam.batch.filters.application.edes;

import com.enel.platform.mepodlatam.batch.filters.application.Filter;
import com.enel.platform.mepodlatam.batch.filters.application.annotation.WhereConditionFilter;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.edes.EDESFilterColumnMapping;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;
import com.enel.platform.mepodlatam.batch.filters.application.util.edes.EDESFilteringHelper;

@WhereConditionFilter
public class EDESDtWoaCreationIncrementalFilter implements Filter {

	private final EDESFilteringHelper queryFilteringHelper;

	public EDESDtWoaCreationIncrementalFilter() {
		this.queryFilteringHelper = new EDESFilteringHelper();
	}

	@Override
	public String getCondition(ApplicationFilterParser parser) {
		return queryFilteringHelper.getIntervalByDateExcludingExtremes(
				FilterColumnMappingConfig.getColumNameByDBAndAlias(EDESFilterColumnMapping.DB_NAME, parser.getFieldName()),
				parser.getFieldValue1());
	}

	@Override
	public String getFilterName() {
		return EDESFilterColumnMapping.DT_WOA_CREATION_INCREMENTAL_FILTER;
	}

}
