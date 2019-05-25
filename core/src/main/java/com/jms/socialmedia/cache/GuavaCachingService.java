package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

import static java.util.stream.Collectors.toSet;

public class GuavaCachingService extends AbstractHeapCachingService {

	private static final int DEFAULT_MAX_NO_OF_POSTS = 50;
	private static final int DEFAULT_MAX_NO_OF_USER_SESSIONS = 20;

	private final Cache<Integer, Post> postsById;
	private final Cache<Integer, Comment> commentsById;
	private final Cache<Integer, Collection<Comment>> commentsByPostId;
	private final Cache<String, User> userSessionsByKey;

	public GuavaCachingService() {
		this(DEFAULT_MAX_NO_OF_POSTS, DEFAULT_MAX_NO_OF_USER_SESSIONS);
	}

	public GuavaCachingService(int maxNumberOfPosts, int maxNumberOfUserSessions) {
		this(maxNumberOfPosts, maxNumberOfUserSessions, Integer.MAX_VALUE);
	}

	public GuavaCachingService(int maxNumberOfPosts, int maxNumberOfUserSessions, int expireTimeInSeconds) {
		this.commentsById = CacheBuilder.newBuilder().build();
		this.commentsByPostId = CacheBuilder.newBuilder().maximumSize(maxNumberOfPosts)
				.expireAfterAccess(expireTimeInSeconds, TimeUnit.SECONDS)
				.<Integer, Collection<Comment>>removalListener(removal -> 
					commentsById.invalidateAll(removal.getValue().stream().map(Comment::getCommentId).collect(toSet()))
				).build();
		this.postsById = CacheBuilder.newBuilder().maximumSize(maxNumberOfPosts).expireAfterAccess(expireTimeInSeconds, TimeUnit.SECONDS)
				.removalListener(removal -> 
					commentsByPostId.invalidate(removal.getKey())
				).build();
		this.userSessionsByKey = CacheBuilder.newBuilder().maximumSize(maxNumberOfUserSessions)
				.expireAfterAccess(expireTimeInSeconds, TimeUnit.SECONDS).build();
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
		commentsByPostId.put(postId, new TreeSet<>(comments));
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

	@Override
	public User getUserSessionFromCache(String sessionKey) {
		return userSessionsByKey.getIfPresent(sessionKey);
	}

	@Override
	public void putUserSessionIntoCache(String sessionKey, User user) {
		userSessionsByKey.put(sessionKey, user);
	}

	@Override
	public void removeUserSessionFromCache(String sessionKey) {
		userSessionsByKey.invalidate(sessionKey);
	}
}
