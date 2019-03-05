package com.jms.socialmedia.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

public class JavaMapCachingService implements CachingService {

	private final Map<Integer, Post> postsById;
	private final Map<Integer, Comment> commentsById;
	
	public JavaMapCachingService() {
		this.postsById = new HashMap<>();
		this.commentsById = new HashMap<>();
	}

	@Override
	public Post getPostFromCache(int postId) {
		return postsById.get(postId);
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
		postsById.remove(postId);
	}

	@Override
	public Comment getCommentFromCache(int commentId) {
		return commentsById.get(commentId);
	}

	@Override
	public void putCommentIntoCache(Comment comment) {
		commentsById.put(comment.getCommentId(), comment);
	}

	@Override
	public void removeCommentFromCache(int commentId) {
		commentsById.remove(commentId);
	}

}
