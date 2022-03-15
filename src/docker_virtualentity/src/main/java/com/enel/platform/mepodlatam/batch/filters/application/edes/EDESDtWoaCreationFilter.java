package com.enel.platform.mepodlatam.batch.filters.application.edes;

import com.enel.platform.mepodlatam.batch.filters.application.Filter;
import com.enel.platform.mepodlatam.batch.filters.application.annotation.WhereConditionFilter;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.FilterColumnMappingConfig;
import com.enel.platform.mepodlatam.batch.filters.application.mapping.edes.EDESFilterColumnMapping;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;
import com.enel.platform.mepodlatam.batch.filters.application.util.edes.EDESFilteringHelper;

@WhereConditionFilter
public class EDESDtWoaCreationFilter implements Filter {

	private final EDESFilteringHelper queryFilteringHelper;

	public EDESDtWoaCreationFilter() {
		this.queryFilteringHelper = new EDESFilteringHelper();
	}

	@Override
	public String getCondition(ApplicationFilterParser parser) {
		return queryFilteringHelper.getIntervalByDateIncludingExtremes(
				FilterColumnMappingConfig.getColumNameByDBAndAlias(EDESFilterColumnMapping.DB_NAME, parser.getFieldName()),
				parser.getFieldValue1(), parser.getFieldValue2().orElse(null));
	}

	@Override
	public String getFilterName() {
		return EDESFilterColumnMapping.DT_WOA_CREATION_FILTER;
	}

}
