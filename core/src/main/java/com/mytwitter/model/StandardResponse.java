package com.mytwitter.model;

public class StandardResponse {

	public enum Status {
		SUCCESS, FAILURE;
	}
	
	private Status status;
	private String message;
	private Object data;

	public final Status getStatus() {
		return status;
	}

	public final void setStatus(Status status) {
		this.status = status;
	}

	public final String getMessage() {
		return message;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

	public final Object getData() {
		return data;
	}

	public final void setData(Object data) {
		this.data = data;
	}
	
}
