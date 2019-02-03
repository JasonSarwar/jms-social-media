package com.jms.socialmedia.model;

import java.time.LocalDateTime;

import com.google.common.base.MoreObjects;

public class Comment extends Entry {

	private Integer commentId;
	
	public Comment() {
	}
	
	public Comment(Integer commentId, Integer postId, String text, LocalDateTime timestamp) {
		this(commentId, postId, null, null, null, text, timestamp);
	}

	public Comment(Integer commentId, Integer postId, Integer userId, String username, String fullName, String text, LocalDateTime timestamp) {
		super(postId, userId, username, fullName, text, timestamp);
		this.commentId = commentId;
	}
	
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer replyToPostId) {
		this.commentId = replyToPostId;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("commentId", commentId)
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
