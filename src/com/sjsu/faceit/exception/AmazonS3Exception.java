package com.sjsu.faceit.exception;

public class AmazonS3Exception extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5337115654712167483L;

	public AmazonS3Exception() {
		super();
	}

	public AmazonS3Exception(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AmazonS3Exception(String arg0) {
		super(arg0);
	}

	public AmazonS3Exception(Throwable arg0) {
		super(arg0);
	}
	
}
