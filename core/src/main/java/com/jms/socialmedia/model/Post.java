package com.jms.socialmedia.model;

import java.time.LocalDateTime;

import com.google.common.base.MoreObjects;

public class Post extends Entry {

	public Post() {
	}

	public Post(Integer postId, String text, LocalDateTime timestamp) {
		super(postId, null, null, null, text, timestamp);
	}

	public Post(Integer postId, Integer userId, String username, String fullName, String text, LocalDateTime timestamp) {
		super(postId, userId, username, fullName, text, timestamp);
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
				.add("timestamp", timestamp)
				.toString();
	}
	
}
