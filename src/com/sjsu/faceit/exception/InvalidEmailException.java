package com.sjsu.faceit.exception;

public class InvalidEmailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5830577741320761900L;

	public InvalidEmailException() {
		super();
	}

	public InvalidEmailException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidEmailException(String arg0) {
		super(arg0);
	}

	public InvalidEmailException(Throwable arg0) {
		super(arg0);
	}
	
	

}
