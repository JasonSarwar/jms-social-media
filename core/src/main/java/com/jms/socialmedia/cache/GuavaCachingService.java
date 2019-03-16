package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

import static java.util.stream.Collectors.toSet;

public class GuavaCachingService implements CachingService {

	private final Cache<Integer, Post> postsById;
	private final Cache<Integer, Comment> commentsById;
	private final Cache<Integer, Collection<Comment>> commentsByPostId;

	public GuavaCachingService() {
		this.commentsById = CacheBuilder.newBuilder().build();
		this.commentsByPostId = CacheBuilder.newBuilder().maximumSize(100)
				.<Integer, Collection<Comment>>removalListener(removal -> {
					commentsById.invalidateAll(removal.getValue().stream().map(Comment::getCommentId).collect(toSet()));
				}).build();
		this.postsById = CacheBuilder.newBuilder().maximumSize(100)
				.removalListener(removal -> {
					commentsByPostId.invalidate(removal.getKey());
				}).build();
	}

	@Override
	public Post getPostFromCache(int postId) {
		return postsById.getIfPresent(postId);
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
	public Collection<Comment> getCommentsFromCache(int postId) {
		return commentsByPostId.getIfPresent(postId);
	}

	@Override
	public Comment getCommentFromCache(int commentId) {
		return commentsById.getIfPresent(commentId);
	}

	@Override
	public void putCommentIntoCache(Comment comment) {
		commentsById.put(comment.getCommentId(), comment);
		Collection<Comment> comments = getCommentsFromCache(comment.getPostId());
		if (comments != null) {
			comments.add(comment);
		}
	}

	@Override
	public void putCommentsFromPostIntoCache(int postId, Collection<Comment> comments) {
		commentsByPostId.put(postId, new HashSet<>(comments));
		for (Comment comment : comments) {
			commentsById.put(comment.getCommentId(), comment);
		}
	}

	@Override
	public void removeCommentFromCache(int commentId) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			Collection<Comment> comments = getCommentsFromCache(comment.getPostId());
			if (comments != null) {
				comments.remove(comment);
			}
			commentsById.invalidate(commentId);
		}
	}
}
