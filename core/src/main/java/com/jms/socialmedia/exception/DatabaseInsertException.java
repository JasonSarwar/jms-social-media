package com.jms.socialmedia.exception;

/**
 * Thrown whenever the system is unable to insert new records into the database
 * 
 * @author jason
 *
 */
public class DatabaseInsertException extends MySocialMediaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722219434446589675L;

	public DatabaseInsertException() {
	}

	public DatabaseInsertException(String arg0) {
		super(arg0);
	}

	public DatabaseInsertException(Throwable arg0) {
		super(arg0);
	}

	public DatabaseInsertException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DatabaseInsertException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
