package com.sjsu.faceit.exception;

public class InvalidSecurityAnswerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5747195945658783410L;

	public InvalidSecurityAnswerException() {

	}

	public InvalidSecurityAnswerException(String arg0) {
		super(arg0);
	}

	public InvalidSecurityAnswerException(Throwable arg0) {
		super(arg0);
	}

	public InvalidSecurityAnswerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
