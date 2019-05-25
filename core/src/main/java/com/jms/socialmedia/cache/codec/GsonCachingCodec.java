package com.jms.socialmedia.cache.codec;

import com.google.gson.Gson;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public class GsonCachingCodec implements CachingCodec<String> {

	private final Gson gson;

	public GsonCachingCodec(final Gson gson) {
		this.gson = gson;
	}

	@Override
	public String encodePost(Post post) {
		return gson.toJson(post, Post.class);
	}

	@Override
	public Post decodePost(String encodedPost) {
		return gson.fromJson(encodedPost, Post.class);
	}

	@Override
	public String encodeComment(Comment comment) {
		return gson.toJson(comment, Comment.class);
	}

	@Override
	public Comment decodeComment(String encodedComment) {
		return gson.fromJson(encodedComment, Comment.class);
	}

	@Override
	public String encodeUser(User user) {
		return gson.toJson(user, User.class);
	}

	@Override
	public User decodeUser(String encodedUser) {
		return gson.fromJson(encodedUser, User.class);
	}
}