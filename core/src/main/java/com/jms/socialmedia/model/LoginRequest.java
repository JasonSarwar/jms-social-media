package com.jms.socialmedia.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class LoginRequest {

	private String usernameOrEmail;
	private String password;

	public LoginRequest() {
	}

	public LoginRequest(String usernameOrEmail, String password) {
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
	}

	public final String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public final void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(usernameOrEmail, password);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || this.getClass() != object.getClass())
			return false;

		LoginRequest that = (LoginRequest) object;

		return Objects.equals(this.usernameOrEmail, that.usernameOrEmail)
				&& Objects.equals(this.password, that.password);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("usernameOrEmail", usernameOrEmail).add("password", "*****")
				.toString();
	}
}
