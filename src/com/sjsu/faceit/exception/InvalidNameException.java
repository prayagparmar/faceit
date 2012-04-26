package com.sjsu.faceit.exception;

public class InvalidNameException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6360736473935047629L;

	public InvalidNameException() {
		super();
	}

	public InvalidNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidNameException(String message) {
		super(message);
	}

	public InvalidNameException(Throwable cause) {
		super(cause);
	}
	
	
	
}
