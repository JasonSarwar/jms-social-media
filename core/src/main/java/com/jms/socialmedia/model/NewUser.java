package com.jms.socialmedia.model;

import java.time.LocalDate;

public class NewUser {

	private Integer userId;
	private String username;
	private String password1;
	private String password2;
	private String hashedPassword;
	private String fullName;
	private String email;
	private String bio;
	private LocalDate birthdate;
	private String profilePictureLink;
	
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
	public final String getHashedPassword() {
		return hashedPassword;
	}
	public final void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	public final String getFullName() {
		return fullName;
	}
	public final void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public final String getEmail() {
		return email;
	}
	public final void setEmail(String email) {
		this.email = email;
	}
	public final String getBio() {
		return bio;
	}
	public final void setBio(String bio) {
		this.bio = bio;
	}
	public final LocalDate getBirthdate() {
		return birthdate;
	}
	public final void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	public final String getProfilePictureLink() {
		return profilePictureLink;
	}
	public final void setProfilePictureLink(String profilePictureLink) {
		this.profilePictureLink = profilePictureLink;
	}
	
	
}
