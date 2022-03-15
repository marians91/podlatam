package com.enel.platform.mepodlatam.constants;


public enum Errors {
	
	    GENERIC("MEPOD_ERROR000","Generic Error"),
	    UNSUPPORTED_OPERATION("MEPOD_ERROR001","Requested operation is not supported"),
	    VALIDATION("MEPOD_ERROR002", "Validation Errors: %s"),
	    FILTER_NOT_SPECIFIED("MEPOD_ERROR003","Application filter does not match: %s");

		private String code;
		private String message;

		private Errors(final String code, final String message) {
			this.code=code;
			this.message =message;
		}

		public String getCode() {
			return this.code;
		}
		
		public String getMessage() {
			return this.message;
		}

	

}
