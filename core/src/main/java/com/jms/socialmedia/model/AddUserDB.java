package com.jms.socialmedia.model;

import java.time.LocalDate;

public class AddUserDB {
	
	private String username;
	private String hashedPassword;
	private String fullName;
	private String email;
	private String bio;
	private LocalDate birthdate;
	private String profilePictureLink;
	
	public final String getUsername() {
		return username;
	}
	public final void setUsername(String username) {
		this.username = username;
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
