package com.jms.socialmedia.cache;

import java.util.Collection;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public class CachingServiceWithMetrics extends AbstractCachingService {

	private final AbstractCachingService cachingService;
	private final Timer getPostFromCacheTimer;
	private final Timer editPostInCacheTimer;
	private final Timer putPostIntoCacheTimer;
	private final Timer removePostFromCacheTimer;
	private final Timer likePostInCacheTimer;
	private final Timer unlikePostInCacheTimer;
	private final Timer getCommentsFromCacheTimer;
	private final Timer getCommentFromCacheTimer;
	private final Timer editCommentInCacheTimer;
	private final Timer putCommentIntoCacheTimer;
	private final Timer putCommentsFromPostIntoCacheTimer;
	private final Timer removeCommentFromCacheTimer;
	private final Timer likeCommentInCacheTimer;
	private final Timer unlikeCommentInCacheTimer;
	private final Timer getUserSessionFromCacheTimer;
	private final Timer putUserSessionIntoCacheTimer;
	private final Timer removeUserSessionFromCacheTimer;
	private final Counter postCacheHitCounter;
	private final Counter postCacheMissCounter;
	private final Counter postCacheTotalCounter;
	private final Counter commentsCacheHitCounter;
	private final Counter commentsCacheMissCounter;
	private final Counter commentsCacheTotalCounter;
	private final Counter userSessionCacheHitCounter;
	private final Counter userSessionCacheMissCounter;
	private final Counter userSessionCacheTotalCounter;

	public CachingServiceWithMetrics(AbstractCachingService cachingService, MetricRegistry metricRegistry) {
		this(cachingService, metricRegistry, cachingService.getClass().getSimpleName());
	}

	public CachingServiceWithMetrics(AbstractCachingService cachingService, MetricRegistry metricRegistry, String metricsName) {
		this.cachingService = cachingService;
		this.getPostFromCacheTimer = metricRegistry.timer(metricsName + ".getPostFromCache");
		this.editPostInCacheTimer = metricRegistry.timer(metricsName + ".editPostInCache");
		this.putPostIntoCacheTimer = metricRegistry.timer(metricsName + ".putPostIntoCache");
		this.removePostFromCacheTimer = metricRegistry.timer(metricsName + ".removePostFromCache");
		this.likePostInCacheTimer = metricRegistry.timer(metricsName + ".likePostInCache");
		this.unlikePostInCacheTimer = metricRegistry.timer(metricsName + ".unlikePostInCache");
		this.getCommentsFromCacheTimer = metricRegistry.timer(metricsName + ".getCommentsFromCache");
		this.getCommentFromCacheTimer = metricRegistry.timer(metricsName + ".getCommentFromCache");
		this.editCommentInCacheTimer = metricRegistry.timer(metricsName + ".editCommentInCache");
		this.putCommentIntoCacheTimer = metricRegistry.timer(metricsName + ".putCommentIntoCache");
		this.putCommentsFromPostIntoCacheTimer = metricRegistry.timer(metricsName + ".putCommentsFromPostIntoCache");
		this.removeCommentFromCacheTimer = metricRegistry.timer(metricsName + ".removeCommentFromCache");
		this.likeCommentInCacheTimer = metricRegistry.timer(metricsName + ".likeCommentInCache");
		this.unlikeCommentInCacheTimer = metricRegistry.timer(metricsName + ".unlikeCommentInCache");
		this.getUserSessionFromCacheTimer = metricRegistry.timer(metricsName + ".getUserSessionFromCache");
		this.putUserSessionIntoCacheTimer = metricRegistry.timer(metricsName + ".putUserSessionIntoCache");
		this.removeUserSessionFromCacheTimer = metricRegistry.timer(metricsName + ".removeUserSessionFromCache");
		this.postCacheHitCounter = metricRegistry.counter(metricsName + ".postCacheHits");
		this.postCacheMissCounter = metricRegistry.counter(metricsName + ".postCacheMisses");
		this.postCacheTotalCounter = metricRegistry.counter(metricsName + ".postCacheTotal");
		this.commentsCacheHitCounter = metricRegistry.counter(metricsName + ".commentsCacheHits");
		this.commentsCacheMissCounter = metricRegistry.counter(metricsName + ".commentsCacheMisses");
		this.commentsCacheTotalCounter = metricRegistry.counter(metricsName + ".commentsCacheTotal");
		this.userSessionCacheHitCounter = metricRegistry.counter(metricsName + ".userSessionCacheHits");
		this.userSessionCacheMissCounter = metricRegistry.counter(metricsName + ".userSessionCacheMisses");
		this.userSessionCacheTotalCounter = metricRegistry.counter(metricsName + ".userSessionCacheTotal");
		metricRegistry.gauge(metricsName + ".postCacheHitRatio", () -> () -> (double) postCacheHitCounter.getCount() / postCacheTotalCounter.getCount() * 100);
		metricRegistry.gauge(metricsName + ".commentsCacheHitRatio", () -> () -> (double) commentsCacheHitCounter.getCount() / commentsCacheTotalCounter.getCount() * 100);
		metricRegistry.gauge(metricsName + ".userSessionCacheHitRatio", () -> () -> (double) userSessionCacheHitCounter.getCount() / userSessionCacheTotalCounter.getCount() * 100);
	}

	@Override
	public Post getPostFromCache(int postId) {
		try (Timer.Context context = getPostFromCacheTimer.time()) {
			return cachingService.getPostFromCache(postId);
		}
	}

	@Override
	public void editPostInCache(int postId, String text) {
		try (Timer.Context context = editPostInCacheTimer.time()) {
			cachingService.editPostInCache(postId, text);
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
	public void likePostInCache(int postId, int userId) {
		try (Timer.Context context = likePostInCacheTimer.time()) {
			cachingService.likePostInCache(postId, userId);
		}
	}

	@Override
	public void unlikePostInCache(int postId, int userId) {
		try (Timer.Context context = unlikePostInCacheTimer.time()) {
			cachingService.unlikePostInCache(postId, userId);
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
	public void editCommentInCache(int commentId, String text) {
		try (Timer.Context context = editCommentInCacheTimer.time()) {
			cachingService.editCommentInCache(commentId, text);
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
	public void likeCommentInCache(int commentId, int userId) {
		try (Timer.Context context = likeCommentInCacheTimer.time()) {
			cachingService.likeCommentInCache(commentId, userId);
		}
	}

	@Override
	public void unlikeCommentInCache(int commentId, int userId) {
		try (Timer.Context context = unlikeCommentInCacheTimer.time()) {
			cachingService.unlikeCommentInCache(commentId, userId);
		}
	}

	@Override
	public User getUserSessionFromCache(String sessionKey) {
		try (Timer.Context context = getUserSessionFromCacheTimer.time()) {
			return cachingService.getUserSessionFromCache(sessionKey);
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

	@Override
	protected void postCacheHit() {
		postCacheHitCounter.inc();
		postCacheTotalCounter.inc();
	}

	@Override
	protected void postCacheMiss() {
		postCacheMissCounter.inc();
		postCacheTotalCounter.inc();
	}

	@Override
	protected void commentsCacheHit() {
		commentsCacheHitCounter.inc();
		commentsCacheTotalCounter.inc();
	}

	@Override
	protected void commentsCacheMiss() {
		commentsCacheMissCounter.inc();
		commentsCacheTotalCounter.inc();
	}

	@Override
	protected void userSessionCacheHit() {
		userSessionCacheHitCounter.inc();
		userSessionCacheTotalCounter.inc();
	}

	@Override
	protected void userSessionCacheMiss() {
		userSessionCacheMissCounter.inc();
		userSessionCacheTotalCounter.inc();
	}
}
