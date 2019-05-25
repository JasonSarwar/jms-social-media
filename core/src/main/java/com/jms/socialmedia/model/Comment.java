package com.jms.socialmedia.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class Comment extends Entry implements Comparable<Comment> {

	private Integer commentId;

	public Comment() {
	}

	public Comment(Integer postId, Integer userId, String text) {
		this(null, postId, userId, null, null, text, null);
	}

	public Comment(Integer commentId, Integer postId, String text, LocalDateTime timestamp) {
		this(commentId, postId, null, null, null, text, timestamp);
	}

	public Comment(Integer commentId, Integer postId, Integer userId, String username, String fullName, String text,
			LocalDateTime timestamp) {
		super(postId, userId, username, fullName, text, timestamp);
		this.commentId = commentId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(commentId, postId, userId, username, fullName, text, timestamp, profilePictureLink, likes);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object == null || this.getClass() != object.getClass()) {
			return false;
		}
		Comment that = (Comment) object;
		return Objects.equals(this.commentId, that.commentId) && Objects.equals(this.postId, that.postId) 
				&& Objects.equals(this.userId, that.userId) && Objects.equals(this.username, that.username) 
				&& Objects.equals(this.fullName, that.fullName) && Objects.equals(this.text, that.text) 
				&& Objects.equals(this.timestamp, that.timestamp)
				&& Objects.equals(this.profilePictureLink, that.profilePictureLink)
				&& this.likes.size() == that.likes.size() && this.likes.containsAll(that.likes);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("commentId", commentId)
				.add("postId", postId)
				.add("userId", userId)
				.add("username", username)
				.add("fullName", fullName)
				.add("profilePictureLink", profilePictureLink)
				.add("text", text)
				.add("likes", likes)
				.add("timestamp", timestamp).toString();
	}

	@Override
	public int compareTo(Comment other) {
		return commentId.compareTo(other.commentId);
	}
}
