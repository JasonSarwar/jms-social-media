package com.mytwitter.model;

import java.time.LocalDateTime;

public abstract class Entry {
	
	protected Integer postId;
	protected Integer userId;
	protected String username;
	protected String fullName;
	protected String profilePictureLink;
	protected String text;
	protected LocalDateTime timestamp;
	
	public final boolean hasPostId() {
		return postId != null;
	}
	public final Integer getPostId() {
		return postId;
	}
	public final void setPostId(Integer postId) {
		this.postId = postId;
	}
	public final boolean hasUserId() {
		return userId != null;
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
	public final String getProfilePictureLink() {
		return profilePictureLink;
	}
	public final void setProfilePictureLink(String profilePictureLink) {
		this.profilePictureLink = profilePictureLink;
	}
	public final boolean hasText() {
		return text != null;
	}
	public final String getText() {
		return text;
	}
	public final void setText(String text) {
		this.text = text;
	}
	public final LocalDateTime getTimestamp() {
		return timestamp;
	}
	public final void setTimestamp(LocalDateTime postTimestamp) {
		this.timestamp = postTimestamp;
	}
}
