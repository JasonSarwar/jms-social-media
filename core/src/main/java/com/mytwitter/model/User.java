package com.mytwitter.model;

public class User {

	private Integer userId;
	private String username;
	private String fullName;
	private String hashedPassword;
	
	public User() {

	}

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

	public final String getFullName() {
		return fullName;
	}

	public final void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public final String getHashedPassword() {
		return hashedPassword;
	}

	public final void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	
}
