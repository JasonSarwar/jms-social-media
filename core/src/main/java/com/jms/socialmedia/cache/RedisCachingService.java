package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.Map;

import com.jms.socialmedia.cache.codec.CachingCodec;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCachingService extends AbstractCachingServiceUsingCodec<String> {

	private final JedisPool jedisPool;
	private final int expireTimeInSeconds;

	public RedisCachingService(CachingCodec<String> cachingServiceCodec, String host, int port, int expireTimeInSeconds) {
		this(cachingServiceCodec, new JedisPoolConfig(), host, port, expireTimeInSeconds);
	}

	public RedisCachingService(CachingCodec<String> cachingServiceCodec, String host, int port) {
		this(cachingServiceCodec, new JedisPoolConfig(), host, port);
	}

	public RedisCachingService(CachingCodec<String> cachingServiceCodec, JedisPoolConfig jedisPoolConfig, String host,
			int port) {
		this(cachingServiceCodec, new JedisPool(jedisPoolConfig, host, port));
	}

	public RedisCachingService(CachingCodec<String> cachingServiceCodec, JedisPoolConfig jedisPoolConfig, String host,
			int port, int expireTimeInSeconds) {
		this(cachingServiceCodec, new JedisPool(jedisPoolConfig, host, port), expireTimeInSeconds);
	}

	public RedisCachingService(CachingCodec<String> cachingServiceCodec, JedisPool jedisPool) {
		this(cachingServiceCodec, jedisPool, Integer.MAX_VALUE);
	}

	public RedisCachingService(CachingCodec<String> cachingServiceCodec, JedisPool jedisPool, int expireTimeInSeconds) {
		super(cachingServiceCodec);
		this.jedisPool = jedisPool;
		this.expireTimeInSeconds = expireTimeInSeconds;
	}

	@Override
	protected String getEncodedPostFromCache(int postId) {
		try (Jedis jedis = jedisPool.getResource()) {
			String result = jedis.get(getPostKey(postId));
			if (result != null) {
				setKeyExpiration(jedis, getPostKey(postId));
			}
			return result;
		}
	}

	@Override
	protected void putEncodedPostIntoCache(int postId, String encodedPost) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(getPostKey(postId), encodedPost);
			setKeyExpiration(jedis, getPostKey(postId));
		}
	}

	@Override
	public void removePostFromCache(int postId) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.del(getPostKey(postId));
			jedis.del(getCommentsInPostKey(postId));
		}
	}

	@Override
	protected Collection<String> getEncodedCommentsFromCache(int postId) {
		try (Jedis jedis = jedisPool.getResource()) {
			Collection<String> result = jedis.zrange(getCommentsInPostKey(postId), 0, -1);
			if (result != null && !result.isEmpty()) {
				setKeyExpiration(jedis, getCommentsInPostKey(postId));
			}
			return result;
		}
	}

	@Override
	protected String getEncodedCommentFromCache(int commentId) {
		try (Jedis jedis = jedisPool.getResource()) {
			String postId = jedis.get(getPostIdOfCommentKey(commentId));
			if (postId != null) {
				String result = jedis.zrangeByScore(getCommentsInPostKey(postId), commentId, commentId).stream().findAny().orElse(null);
				if (result != null) {
					setKeyExpiration(jedis, getCommentsInPostKey(postId));
				}
				return result;
			}
			return null;
		}
	}

	@Override
	protected void putEncodedCommentsFromPostIntoCache(int postId, Map<String, Double> encodedCommentsWithId) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.zadd(getCommentsInPostKey(postId), encodedCommentsWithId);
			setKeyExpiration(jedis, getCommentsInPostKey(postId));
			encodedCommentsWithId.values().forEach(d -> jedis.set(getPostIdOfCommentKey(d.intValue()), Integer.toString(postId)));
		}
	}

	@Override
	protected void putEncodedCommentIntoCache(int commentId, int postId, String encodedComment) {
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis.exists(getCommentsInPostKey(postId))) {
				jedis.zadd(getCommentsInPostKey(postId), commentId, encodedComment);
				setKeyExpiration(jedis, getCommentsInPostKey(postId));
				jedis.set(getPostIdOfCommentKey(commentId), Integer.toString(postId));
			}
		}
	}

	@Override
	public void removeCommentFromCache(int commentId) {
		try (Jedis jedis = jedisPool.getResource()) {
			String postId = jedis.get(getPostIdOfCommentKey(commentId));
			if (postId != null) {
				jedis.zremrangeByScore(getCommentsInPostKey(postId), commentId, commentId);
			}
		}
	}

	@Override
	protected String getEncodedUserSessionFromCache(String sessionKey) {
		try (Jedis jedis = jedisPool.getResource()) {
			String result = jedis.get(getUserSessionKey(sessionKey));
			if (result != null) {
				setKeyExpiration(jedis, getUserSessionKey(sessionKey));
			}
			return result;
		}
	}

	@Override
	protected void putEncodedUserSessionIntoCache(String sessionKey, String encodedUserSession) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(getUserSessionKey(sessionKey), encodedUserSession);
			setKeyExpiration(jedis, getUserSessionKey(sessionKey));
		}
	}

	@Override
	public void removeUserSessionFromCache(String sessionKey) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.del(getUserSessionKey(sessionKey));
		}
	}

	private void setKeyExpiration(Jedis jedis, String key) {
		jedis.expire(key, expireTimeInSeconds);
	}
	
	private static String getPostKey(int postId) {
		return "post/" + postId;
	}

	private static String getCommentsInPostKey(Object postId) {
		return "post/" + postId + "/comments";
	}

	private static String getUserSessionKey(String sessionKey) {
		return "user/session/" + sessionKey;
	}

	private static String getPostIdOfCommentKey(int commentId) {
		return "comment/" + commentId + "/postId";
	}
}
