package com.enel.platform.mepodlatam.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.enel.platform.mepodlatam.constants.ErrorMessageConstants;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncrementalFilterDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private EntityFilterDTO entityFilter; 
	private String dtLastModify;
	
	@Data
	@NoArgsConstructor
	@NotBlank(message = ErrorMessageConstants.MANDATORY_FIELD_ERROR)
	public static class EntityFilterDTO implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String entityName;
		@NotBlank(message = ErrorMessageConstants.MANDATORY_FIELD_ERROR)
		private String field;  		  
		@NotBlank(message = ErrorMessageConstants.MANDATORY_FIELD_ERROR)
		private String value;
		
		
	}
}
