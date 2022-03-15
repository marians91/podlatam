package com.enel.platform.mepodlatam.model;

import lombok.Data;

@Data
public class PodIncrementalHandler {
	
	private String entityName;    
	private String field;  
	private String value;  
	private String dtLastModify;
}

