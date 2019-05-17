package com.jms.socialmedia.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Post extends Entry {

	public Post() {
	}

	public Post(Integer postId) {
		this(postId, null, null);
	}

	public Post(Integer postId, String text, LocalDateTime timestamp) {
		super(postId, null, null, null, text, timestamp);
	}

	public Post(Integer postId, Integer userId, String username, String fullName, String text,
			LocalDateTime timestamp) {
		super(postId, userId, username, fullName, text, timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(postId, userId, username, fullName, text, timestamp, profilePictureLink, likes);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		Post that = (Post) object;
		return Objects.equals(this.postId, that.postId) && Objects.equals(this.userId, that.userId)
				&& Objects.equals(this.username, that.username) && Objects.equals(this.fullName, that.fullName)
				&& Objects.equals(this.text, that.text) && Objects.equals(this.timestamp, that.timestamp)
				&& Objects.equals(this.profilePictureLink, that.profilePictureLink)
				&& this.likes.size() == that.likes.size() && this.likes.containsAll(that.likes);
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
