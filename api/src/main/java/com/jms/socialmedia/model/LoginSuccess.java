package com.jms.socialmedia.model;

public class LoginSuccess {

	private Integer userId;
	private String username;
	private String firstname;
	private String token;

	public final Integer getUserId() {
		return userId;
	}

	public final void setUserId(Integer userId) {
		this.userId = userId;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getFirstname() {
		return firstname;
	}

	public final void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public final String getToken() {
		return token;
	}

	public final void setToken(String token) {
		this.token = token;
	}

}
