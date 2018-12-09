package com.mytwitter.model;

public class LoginSuccess {

	private Integer userId;
	private String firstname;
	private String jwt;

	public LoginSuccess() {
		
	}

	public final Integer getUserId() {
		return userId;
	}

	public final void setUserId(Integer userId) {
		this.userId = userId;
	}

	public final String getFirstname() {
		return firstname;
	}

	public final void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public final String getJwt() {
		return jwt;
	}

	public final void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
