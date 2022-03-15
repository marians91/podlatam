package com.enel.platform.mepodlatam.batch.filters.application;

import com.enel.platform.mepodlatam.batch.filters.application.util.ApplicationFilterParser;

public interface Filter {
	
	String getCondition(ApplicationFilterParser parser);
	
	String getFilterName();
	
	
}
