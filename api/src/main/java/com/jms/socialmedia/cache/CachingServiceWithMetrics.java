package com.jms.socialmedia.cache;

import java.util.Collection;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public class CachingServiceWithMetrics extends CachingService {

	private final CachingService cachingService;
	private final Timer getPostFromCacheTimer;
	private final Timer putPostIntoCacheTimer;
	private final Timer removePostFromCacheTimer;
	private final Timer getCommentsFromCacheTimer;
	private final Timer getCommentFromCacheTimer;
	private final Timer putCommentIntoCacheTimer;
	private final Timer putCommentsFromPostIntoCacheTimer;
	private final Timer removeCommentFromCacheTimer;
	private final Timer getUserSessionCacheTimer;
	private final Timer putUserSessionIntoCacheTimer;
	private final Timer removeUserSessionFromCacheTimer;

	public CachingServiceWithMetrics(CachingService cachingService, MetricRegistry metricRegistry) {
		this(cachingService, metricRegistry, cachingService.getClass().getSimpleName());
	}

	public CachingServiceWithMetrics(CachingService cachingService, MetricRegistry metricRegistry, String metricsName) {
		this.cachingService = cachingService;
		this.getPostFromCacheTimer = metricRegistry.timer(metricsName + ".getPostFromCache");
		this.putPostIntoCacheTimer = metricRegistry.timer(metricsName + ".putPostIntoCache");
		this.removePostFromCacheTimer = metricRegistry.timer(metricsName + ".removePostFromCache");
		this.getCommentsFromCacheTimer = metricRegistry.timer(metricsName + ".getCommentsFromCache");
		this.getCommentFromCacheTimer = metricRegistry.timer(metricsName + ".getCommentFromCache");
		this.putCommentIntoCacheTimer = metricRegistry.timer(metricsName + ".putCommentIntoCache");
		this.putCommentsFromPostIntoCacheTimer = metricRegistry.timer(metricsName + ".putCommentsFromPostIntoCache");
		this.removeCommentFromCacheTimer = metricRegistry.timer(metricsName + ".removeCommentFromCache");
		this.getUserSessionCacheTimer = metricRegistry.timer(metricsName + ".getUserSessionCache");
		this.putUserSessionIntoCacheTimer = metricRegistry.timer(metricsName + ".putUserSessionIntoCache");
		this.removeUserSessionFromCacheTimer = metricRegistry.timer(metricsName + ".removeUserSessionFromCache");
	}

	@Override
	public Post getPostFromCache(int postId) {
		try (Timer.Context context = getPostFromCacheTimer.time()) {
			return cachingService.getPostFromCache(postId);
		}
	}

	@Override
	public void putPostIntoCache(Post post) {
		try (Timer.Context context = putPostIntoCacheTimer.time()) {
			cachingService.putPostIntoCache(post);
		}
	}

	@Override
	public void removePostFromCache(int postId) {
		try (Timer.Context context = removePostFromCacheTimer.time()) {
			cachingService.removePostFromCache(postId);
		}
	}

	@Override
	public Collection<Comment> getCommentsFromCache(int postId) {
		try (Timer.Context context = getCommentsFromCacheTimer.time()) {
			return cachingService.getCommentsFromCache(postId);
		}
	}

	@Override
	public Comment getCommentFromCache(int commentId) {
		try (Timer.Context context = getCommentFromCacheTimer.time()) {
			return cachingService.getCommentFromCache(commentId);
		}
	}

	@Override
	public void putCommentIntoCache(Comment comment) {
		try (Timer.Context context = putCommentIntoCacheTimer.time()) {
			cachingService.putCommentIntoCache(comment);
		}
	}

	@Override
	public void putCommentsFromPostIntoCache(int postId, Collection<Comment> comments) {
		try (Timer.Context context = putCommentsFromPostIntoCacheTimer.time()) {
			cachingService.putCommentsFromPostIntoCache(postId, comments);
		}
	}

	@Override
	public void removeCommentFromCache(int commentId) {
		try (Timer.Context context = removeCommentFromCacheTimer.time()) {
			cachingService.removeCommentFromCache(commentId);
		}
	}

	@Override
	public User getUserSessionCache(String sessionKey) {
		try (Timer.Context context = getUserSessionCacheTimer.time()) {
			return cachingService.getUserSessionCache(sessionKey);
		}
	}

	@Override
	public void putUserSessionIntoCache(String sessionKey, User user) {
		try (Timer.Context context = putUserSessionIntoCacheTimer.time()) {
			cachingService.putUserSessionIntoCache(sessionKey, user);
		}
	}

	@Override
	public void removeUserSessionFromCache(String sessionKey) {
		try (Timer.Context context = removeUserSessionFromCacheTimer.time()) {
			cachingService.removeUserSessionFromCache(sessionKey);
		}
	}
}
