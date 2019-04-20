package com.jms.socialmedia.exception;

public class InvalidUserLoginStateException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1515590300230959129L;

	public InvalidUserLoginStateException() {
	}

	public InvalidUserLoginStateException(String arg0) {
		super(arg0);
	}

	public InvalidUserLoginStateException(Throwable arg0) {
		super(arg0);
	}

	public InvalidUserLoginStateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidUserLoginStateException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
