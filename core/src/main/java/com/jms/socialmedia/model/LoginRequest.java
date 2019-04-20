package com.jms.socialmedia.model;

public class LoginRequest {

	private String user;
	private String password;

	public final String getUser() {
		return user;
	}

	public final void setUser(String username) {
		this.user = username;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

}
