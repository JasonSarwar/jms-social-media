package com.jms.socialmedia.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import com.google.common.base.MoreObjects;

public abstract class Entry {
	
	protected Integer postId;
	protected Integer userId;
	protected String username;
	protected String fullName;
	protected String profilePictureLink;
	protected String text;
	protected LocalDateTime timestamp;
	protected Collection<String> likes;
	
	public Entry() {
		this.likes = new HashSet<>();
	}
	
	public Entry(Integer postId, Integer userId, String username, String fullName, String text, LocalDateTime timestamp) {
		this();
		this.postId = postId;
		this.userId = userId;
		this.username = username;
		this.fullName = fullName;
		this.text = text;
		this.timestamp = timestamp;
	}
	
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
	public final Collection<String> getLikes() {
		return likes;
	}
	public final void setLikes(Collection<String> likes) {
		this.likes = likes;
	}
	public final boolean addLike(String username) {
		return this.likes.add(username);
	}
	public final boolean removeLike(String username) {
		return this.likes.removeIf(e -> e.equalsIgnoreCase(username));
	}
	public final LocalDateTime getTimestamp() {
		return timestamp;
	}
	public final void setTimestamp(LocalDateTime postTimestamp) {
		this.timestamp = postTimestamp;
	}
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("postId", postId)
				.add("userId", userId)
				.add("username", username)
				.add("fullName", fullName)
				.add("profilePictureLink", profilePictureLink)
				.add("text", text)
				.add("likes", likes)
				.add("timestamp", timestamp)
				.toString();
	}
}
