package com.jms.socialmedia.exception;

public class MySocialMediaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3127010441542891315L;

	public MySocialMediaException() {
	}

	public MySocialMediaException(String arg0) {
		super(arg0);
	}

	public MySocialMediaException(Throwable arg0) {
		super(arg0);
	}

	public MySocialMediaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MySocialMediaException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
