package com.jms.socialmedia.exception;

/**
 * Thrown when the Bearer Token does not have the proper Permissions for the action being taken
 * 
 * @author Jason Sarwar
 *
 */
public class ForbiddenException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722219434446589675L;

	public ForbiddenException() {
	}

	public ForbiddenException(String arg0) {
		super(arg0);
	}

	public ForbiddenException(Throwable arg0) {
		super(arg0);
	}

	public ForbiddenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ForbiddenException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
