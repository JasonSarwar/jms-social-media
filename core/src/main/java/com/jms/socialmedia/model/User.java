package com.jms.socialmedia.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class User {

	protected Integer userId;
	protected String username;
	protected String fullName;
	protected String hashedPassword;

	public User() {
	}

	public User(Integer userId, String username) {
		this(userId, username, null, null);
	}

	public User(Integer userId, String username, String fullName) {
		this(userId, username, fullName, null);
	}

	public User(Integer userId, String username, String fullName, String hashedPassword) {
		this.userId = userId;
		this.username = username;
		this.fullName = fullName;
		this.hashedPassword = hashedPassword;
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

	@Override
	public int hashCode() {
		return Objects.hash(userId, username, fullName, hashedPassword);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || this.getClass() != object.getClass())
			return false;

		User that = (User) object;

		return Objects.equals(this.userId, that.userId) 
				&& Objects.equals(this.username, that.username)
				&& Objects.equals(this.fullName, that.fullName)
				&& Objects.equals(this.hashedPassword, that.hashedPassword);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("userId", userId)
				.add("username", username)
				.add("fullName", fullName)
				.add("hashedPassword", "hashedPassword")
				.toString();
	}
}
