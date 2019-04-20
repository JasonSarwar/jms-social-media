package com.jms.socialmedia.exception;

public class UnsupportedContentTypeException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3230595786387229611L;

	public UnsupportedContentTypeException() {
	}

	public UnsupportedContentTypeException(String arg0) {
		super(arg0);
	}

	public UnsupportedContentTypeException(Throwable arg0) {
		super(arg0);
	}

	public UnsupportedContentTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnsupportedContentTypeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
