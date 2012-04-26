package com.sjsu.faceit.exception;

public class InvalidZipException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8454202793448857664L;

	public InvalidZipException() {
		super();
	}

	public InvalidZipException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidZipException(String message) {
		super(message);
	}

	public InvalidZipException(Throwable cause) {
		super(cause);
	}
	
}
