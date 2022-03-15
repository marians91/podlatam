package com.enel.platform.mepodlatam.batch.filters.application;

import com.enel.platform.mepodlatam.batch.filters.application.annotation.WhereConditionFilter;
import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;

@WhereConditionFilter
public class Tenant1DtWoaCreationFilter implements Filter{

	public String getFilterName() {
		return "Tenant1DB#dtWoaCreation";
	}

	@Override
	public String getCondition(ApplicationFilterParser parser) {
		return null;
	}
}
