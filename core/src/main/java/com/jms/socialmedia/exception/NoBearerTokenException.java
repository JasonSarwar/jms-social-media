package com.jms.socialmedia.exception;

/**
 * Thrown when the Authorization Header is not included in the HTTP Header or 
 * when the Bearer Token is not included in the Authorization Header
 * 
 * @author Jason Sarwar
 *
 */
public class NoBearerTokenException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722219434446589675L;

	public NoBearerTokenException() {
	}

	public NoBearerTokenException(String arg0) {
		super(arg0);
	}

	public NoBearerTokenException(Throwable arg0) {
		super(arg0);
	}

	public NoBearerTokenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NoBearerTokenException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
