package com.mytwitter.model;

public class Comment extends Entry {

	private Integer commentId;
	
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer replyToPostId) {
		this.commentId = replyToPostId;
	}
}
