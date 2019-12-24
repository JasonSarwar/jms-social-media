package com.jms.socialmedia.model;

import java.time.LocalDate;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class NewUser extends User {

	private String password1;
	private String password2;
	private String email;
	private LocalDate birthDate;
	private String profilePictureLink;

	public NewUser() {
	}

	public NewUser(Integer userId, String username, String fullName, String email, String password1, String password2,
			LocalDate birthDate, String profilePictureLink) {

		super(userId, username, fullName);
		this.email = email;
		this.password1 = password1;
		this.password2 = password2;
		this.birthDate = birthDate;
		this.profilePictureLink = profilePictureLink;

	}

	public final String getPassword1() {
		return password1;
	}

	public final void setPassword1(String password1) {
		this.password1 = password1;
	}

	public final String getPassword2() {
		return password2;
	}

	public final void setPassword2(String password2) {
		this.password2 = password2;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final LocalDate getBirthDate() {
		return birthDate;
	}

	public final void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public final String getProfilePictureLink() {
		return profilePictureLink;
	}

	public final void setProfilePictureLink(String profilePictureLink) {
		this.profilePictureLink = profilePictureLink;
	}

	public final boolean passwordsMatch() {
		return password1.equals(password2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, username, password1, password2, hashedPassword, fullName, email, birthDate,
				profilePictureLink);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || other.getClass() != getClass())
			return false;

		NewUser newUser = (NewUser) other;
		return Objects.equals(userId, newUser.userId)
				&& Objects.equals(username, newUser.username)
				&& Objects.equals(password1, newUser.password1)
				&& Objects.equals(password2, newUser.password2)
				&& Objects.equals(hashedPassword, newUser.hashedPassword)
				&& Objects.equals(fullName, newUser.fullName)
				&& Objects.equals(email, newUser.email)
				&& Objects.equals(birthDate, newUser.birthDate)
				&& Objects.equals(profilePictureLink, newUser.profilePictureLink);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("userId", userId)
				.add("username", username)
				.add("password1", "*****")
				.add("password2", "*****")
				.add("hashedPassword", "*****")
				.add("fullName", fullName)
				.add("email", email)
				.add("birthDate", birthDate)
				.add("profilePictureLink", profilePictureLink)
				.toString();
	}
}
