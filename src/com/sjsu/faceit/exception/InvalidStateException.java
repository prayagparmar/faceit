package com.sjsu.faceit.exception;

public class InvalidStateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2724448280195961831L;

	public InvalidStateException() {
		super();
	}

	public InvalidStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStateException(String message) {
		super(message);
	}

	public InvalidStateException(Throwable cause) {
		super(cause);
	}
	
	

}
