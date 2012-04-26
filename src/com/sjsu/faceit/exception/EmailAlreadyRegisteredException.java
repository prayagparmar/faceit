package com.sjsu.faceit.exception;

public class EmailAlreadyRegisteredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5810930240343484447L;

	public EmailAlreadyRegisteredException() {
	}

	public EmailAlreadyRegisteredException(String arg0) {
		super(arg0);
	}

	public EmailAlreadyRegisteredException(Throwable arg0) {
		super(arg0);
	}

	public EmailAlreadyRegisteredException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
