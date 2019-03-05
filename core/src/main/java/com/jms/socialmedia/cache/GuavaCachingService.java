package com.jms.socialmedia.cache;

import java.util.function.Supplier;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

public class GuavaCachingService implements CachingService {

	private final Cache<Integer, Post> postsById;
	private final Cache<Integer, Comment> commentsById;
	
	public GuavaCachingService() {
		this.postsById = CacheBuilder.newBuilder().maximumSize(100).build();
		this.commentsById = CacheBuilder.newBuilder().maximumSize(100).build();
	}

	@Override
	public Post getPostFromCache(int postId) {
		return postsById.getIfPresent(postId);
	}
	
	@Override
	public Post getPostFromCacheOrSupplier(int postId, Supplier<Post> supplier) {
		Post post = getPostFromCache(postId);
		if (post == null) {
			post = supplier.get();
			putPostIntoCache(post);
		}
		return post;
	}

	@Override
	public void putPostIntoCache(Post post) {
		postsById.put(post.getPostId(), post);
	}

	@Override
	public void removePostFromCache(int postId) {
		postsById.invalidate(postId);
	}

	@Override
	public Comment getCommentFromCache(int commentId) {
		return commentsById.getIfPresent(commentId);
	}

	@Override
	public void putCommentIntoCache(Comment comment) {
		commentsById.put(comment.getCommentId(), comment);
	}

	@Override
	public void removeCommentFromCache(int commentId) {
		commentsById.invalidate(commentId);
	}

}
