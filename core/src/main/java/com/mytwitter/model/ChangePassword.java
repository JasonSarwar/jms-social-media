package com.mytwitter.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class ChangePassword {

	private Integer userId;
	private String oldPassword;
	private String newPassword;

	public final Integer getUserId() {
		return userId;
	}
	public final void setUserId(Integer userId) {
		this.userId = userId;
	}
	public final String getOldPassword() {
		return oldPassword;
	}
	public final void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public final String getNewPassword() {
		return newPassword;
	}
	public final void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	@Override
	public int hashCode() {
		return Objects.hash(userId, oldPassword, newPassword);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		ChangePassword other = (ChangePassword) obj;
		return Objects.equals(userId, other.userId)
				&& Objects.equals(oldPassword, other.oldPassword)
				&& Objects.equals(newPassword, other.newPassword);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("userId", userId)
				.add("oldPassword", "****")
				.add("newPassword", "****")
				.toString();
	}
	
}
