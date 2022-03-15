package com.enel.platform.mepodlatam.exception;

public class ApplicationException extends RuntimeException {

	/**
	 * 
	 */

	private static final long serialVersionUID = -5098388476470339301L;

	public ApplicationException() {
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}
}