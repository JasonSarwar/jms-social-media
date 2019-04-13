package com.jms.socialmedia.model;

import java.time.LocalDateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

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
	public int hashCode() {
		return Objects.hashCode(postId, userId, username, fullName, text, timestamp, profilePictureLink, likes);
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		Post that = (Post) object;
		return Objects.equal(this.postId, that.postId)
				&& Objects.equal(this.userId, that.userId)
				&& Objects.equal(this.username, that.username)
				&& Objects.equal(this.fullName, that.fullName)
				&& Objects.equal(this.text, that.text)
				&& Objects.equal(this.timestamp, that.timestamp)
				&& Objects.equal(this.profilePictureLink, that.profilePictureLink)
				&& Objects.equal(this.likes, that.likes);
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
