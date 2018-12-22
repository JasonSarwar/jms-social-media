package com.mytwitter.model;

import java.time.LocalDateTime;
import java.util.Collection;

import com.mytwitter.exception.BadRequestException;

public class Post {

	private Integer postId;
	private Integer userId;
	private Integer replyOfPostId;
	private String username;
	private String fullName;
	private String profilePictureLink;
	private String text;
	private String linkToVideo;
	private LocalDateTime postTimestamp;
	private Collection<String> tags;
	private Collection<Post> replies;
	
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
	public Integer getReplyOfPostId() {
		return replyOfPostId;
	}
	public void setReplyOfPostId(Integer replyToPostId) {
		this.replyOfPostId = replyToPostId;
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
	public final String getLinkToVideo() {
		return linkToVideo;
	}
	public final void setLinkToVideo(String linkToVideo) {
		this.linkToVideo = linkToVideo;
	}
	public final LocalDateTime getPostTimestamp() {
		return postTimestamp;
	}
	public final void setPostTimestamp(LocalDateTime postTimestamp) {
		this.postTimestamp = postTimestamp;
	}
	public final Collection<String> getTags() {
		return tags;
	}
	public final void setTags(Collection<String> tags) {
		this.tags = tags;
	}
	public final Collection<Post> getReplies() {
		return replies;
	}
	public final void setReplies(Collection<Post> replies) {
		this.replies = replies;
	}
	
	public void validate() {
		if(!hasUserId()) {
			throw new BadRequestException("Add Post Request requires a 'userId'");
		} else if(!hasText()) {
			throw new BadRequestException("Add Post Request requires 'text'");
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("Post ID: ").append(postId).append('\n')
				.append("User ID: ").append(userId).append('\n')
				.append("Username: ").append(username).append('\n')
				.append("Full Name: ").append(fullName).append('\n')
				.append("Text: ").append(text).append('\n')
				.append("Post Time: ").append(postTimestamp)
				.toString();
	}
	
}
