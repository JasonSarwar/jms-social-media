package com.jms.socialmedia.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class UserPage {

	private Integer userId;
	private String username;
	private String fullName;
	private String email;
	private String bio;
	private LocalDate birthDate;
	private LocalDateTime dateTimeJoined;
	private String profilePictureLink;
	private final Collection<String> followersUsernames;
	private final Collection<String> followingUsernames;
	
	public UserPage() {
		followersUsernames = new HashSet<>();
		followingUsernames = new HashSet<>();
	}

	public final int getUserId() {
		return userId;
	}

	public final void setUserId(int userId) {
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

	public final LocalDate getBirthDate() {
		return birthDate;
	}

	public final void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public final LocalDateTime getDateTimeJoined() {
		return dateTimeJoined;
	}

	public final void setDateTimeJoined(LocalDateTime dateTimeJoined) {
		this.dateTimeJoined = dateTimeJoined;
	}

	public final String getProfilePictureLink() {
		return profilePictureLink;
	}

	public final void setProfilePictureLink(String profilePictureLink) {
		this.profilePictureLink = profilePictureLink;
	}

	public final Collection<String> getFollowersUsernames() {
		return followersUsernames;
	}

	public final Collection<String> getFollowingUsernames() {
		return followingUsernames;
	}
	
	public final boolean addFollowersUsernames(String... usernames) {
		return addFollowersUsernames(Arrays.asList(usernames));
	}
	
	public final boolean addFollowersUsernames(Collection<String> usernames) {
		return followersUsernames.addAll(usernames);
	}

	public final boolean addFollowingUsernames(String... usernames) {
		return addFollowingUsernames(Arrays.asList(usernames));
	}

	public final boolean addFollowingUsernames(Collection<String> usernames) {
		return followingUsernames.addAll(usernames);
	}
}
