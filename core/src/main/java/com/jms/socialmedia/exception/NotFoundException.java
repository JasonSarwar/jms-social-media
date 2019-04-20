package com.jms.socialmedia.exception;

public class NotFoundException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3722244507931374253L;

	public NotFoundException() {
	}

	public NotFoundException(String arg0) {
		super(arg0);
	}

	public NotFoundException(Throwable arg0) {
		super(arg0);
	}

	public NotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
