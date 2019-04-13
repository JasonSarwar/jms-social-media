package com.jms.socialmedia.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class ChangePassword {

	private Integer userId;
	private String oldPassword;
	private String newPassword1;
	private String newPassword2;

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
	public final String getNewPassword1() {
		return newPassword1;
	}
	public final void setNewPassword1(String newPassword) {
		this.newPassword1 = newPassword;
	}
	public final String getNewPassword2() {
		return newPassword2;
	}
	public final void setNewPassword2(String newPassword) {
		this.newPassword2 = newPassword;
	}
	public final boolean passwordsMatch() {
		return newPassword1.equals(newPassword2);
	}
	@Override
	public int hashCode() {
		return Objects.hash(userId, oldPassword, newPassword1, newPassword2);
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
				&& Objects.equals(newPassword1, other.newPassword1)
				&& Objects.equals(newPassword2, other.newPassword2);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("userId", userId)
				.add("oldPassword", "****")
				.add("newPassword1", "****")
				.add("newPassword2", "****")
				.toString();
	}
	
}
