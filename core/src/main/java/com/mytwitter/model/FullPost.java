package com.mytwitter.model;

import java.time.LocalDateTime;
import java.util.Collection;

import com.google.common.base.MoreObjects;

public class FullPost extends Entry {

	private Collection<Comment> comments;
	
	public FullPost() {
	}
	
	public FullPost(Integer postId, Integer userId, String username, String fullName, String text, LocalDateTime timestamp) {
		super(postId, userId, username, fullName, text, timestamp);
	}
	
	public final Collection<Comment> getComments() {
		return comments;
	}
	public final void setComments(Collection<Comment> comments) {
		this.comments = comments;
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
				.add("comments", comments)
				.toString();
	}
}
