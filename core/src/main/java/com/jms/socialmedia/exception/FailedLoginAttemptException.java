package com.jms.socialmedia.exception;

public class FailedLoginAttemptException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4230591304004898543L;

	public FailedLoginAttemptException() {
	}

	public FailedLoginAttemptException(String arg0) {
		super(arg0);
	}

	public FailedLoginAttemptException(Throwable arg0) {
		super(arg0);
	}

	public FailedLoginAttemptException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FailedLoginAttemptException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
