package com.jms.socialmedia.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jms.socialmedia.cache.codec.CachingCodec;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RedisCachingServiceTest {

	private static final String ENCODED_POST = "encodedPost";
	private static final String POST_KEY = "post/5";
	private static final String ENCODED_COMMENT1 = "encodedComment1";
	private static final String ENCODED_COMMENT2 = "encodedComment2";
	private static final String COMMENTS_KEY = "post/3/comments";
	private static final String POST_ID_OF_COMMENT_KEY1 = "comment/1/postId";
	private static final String POST_ID_OF_COMMENT_KEY2 = "comment/2/postId";
	private static final String ENCODED_USER_SESSION = "encodedUserSession";
	private static final String USER_SESSION_KEY = "user/session/sessionId";
	private static final int EXPIRATION_TIME = 500;

	@Mock
	private CachingCodec<String> cachingServiceCodec;
	@Mock
	private JedisPool jedisPool;
	@Mock
	private Jedis jedis;

	private Post post;

	private Comment comment1;
	private Comment comment2;

	private User userSession;

	private RedisCachingService redisCachingService;

	@Before
	public void setUp() {
		initMocks(this);
		post = new Post(5);
		comment1 = new Comment(1, 3, "comment 1", null);
		comment2 = new Comment(2, 3, "comment 2", null);
		userSession = new User(10, "User10", "User Number 10");
		redisCachingService = new RedisCachingService(cachingServiceCodec, jedisPool, EXPIRATION_TIME);

		when(jedisPool.getResource()).thenReturn(jedis);

		when(jedis.get(POST_KEY)).thenReturn(ENCODED_POST);
		when(jedis.get(POST_ID_OF_COMMENT_KEY2)).thenReturn("3");
		when(jedis.zrangeByScore(COMMENTS_KEY, 2, 2)).thenReturn(Set.of(ENCODED_COMMENT2));

		when(cachingServiceCodec.decodePost(ENCODED_POST)).thenReturn(post);
		when(cachingServiceCodec.decodeComment(ENCODED_COMMENT1)).thenReturn(comment1);
		when(cachingServiceCodec.decodeComment(ENCODED_COMMENT2)).thenReturn(comment2);
		when(cachingServiceCodec.encodeComment(comment1)).thenReturn(ENCODED_COMMENT1);
		when(cachingServiceCodec.encodeComment(comment2)).thenReturn(ENCODED_COMMENT2);

	}

	@After
	public void tearDown() {
		verify(jedisPool, times(1)).getResource();
		verify(jedis, times(1)).close();
		verifyNoMoreInteractions(jedis);
		verifyNoMoreInteractions(cachingServiceCodec);
	}

	@Test
	public void testGetPostFromCache() {

		assertThat(redisCachingService.getPostFromCache(5), is(post));

		verify(jedis, times(1)).get(POST_KEY);
		verify(jedis, times(1)).expire(POST_KEY, EXPIRATION_TIME);
		verify(cachingServiceCodec, times(1)).decodePost(ENCODED_POST);
	}

	@Test
	public void testGetEncodedPostFromCache() {

		assertThat(redisCachingService.getEncodedPostFromCache(5), is(ENCODED_POST));

		verify(jedis, times(1)).get(POST_KEY);
		verify(jedis, times(1)).expire(POST_KEY, EXPIRATION_TIME);
	}

	@Test
	public void testGetEncodedPostFromCacheDefaultExpirationTime() {
		redisCachingService = new RedisCachingService(cachingServiceCodec, jedisPool);

		assertThat(redisCachingService.getEncodedPostFromCache(5), is(ENCODED_POST));

		verify(jedis, times(1)).get(POST_KEY);
		verify(jedis, times(1)).expire(POST_KEY, Integer.MAX_VALUE);
	}

	@Test
	public void testGetEncodedPostFromCacheNotFound() {
		assertThat(redisCachingService.getEncodedPostFromCache(6), is(nullValue()));

		verify(jedis, times(1)).get("post/6");
		verify(jedis, never()).expire(anyString(), anyInt());
	}

	@Test
	public void testPutPostIntoCache() {
		when(cachingServiceCodec.encodePost(post)).thenReturn(ENCODED_POST);

		redisCachingService.putPostIntoCache(post);

		verify(jedis, times(1)).set(POST_KEY, ENCODED_POST);
		verify(jedis, times(1)).expire(POST_KEY, EXPIRATION_TIME);
		verify(cachingServiceCodec, times(1)).encodePost(post);
	}

	@Test
	public void testRemovePostFromCache() {
		redisCachingService.removePostFromCache(5);
		verify(jedis, times(1)).del(POST_KEY);
		verify(jedis, times(1)).del(POST_KEY + "/comments");
	}

	@Test
	public void testGetEncodedCommentsFromCache() {
		Set<String> encodedComments = Set.of(ENCODED_COMMENT1, ENCODED_COMMENT2);
		when(jedis.exists(COMMENTS_KEY)).thenReturn(true);
		when(jedis.zrange(COMMENTS_KEY, 0, -1)).thenReturn(encodedComments);

		assertThat(redisCachingService.getEncodedCommentsFromCache(3), is(encodedComments));

		verify(jedis, times(1)).exists(COMMENTS_KEY);
		verify(jedis, times(1)).zrange(COMMENTS_KEY, 0, -1);
		verify(jedis, times(1)).expire(COMMENTS_KEY, EXPIRATION_TIME);
	}

	@Test
	public void testGetEncodedCommentsFromCacheNotFound() {
		assertThat(redisCachingService.getEncodedCommentsFromCache(14), is(nullValue()));
		verify(jedis, times(1)).exists("post/14/comments");
	}

	@Test
	public void testGetCommentsFromCache() {
		Set<String> encodedComments = Set.of(ENCODED_COMMENT1, ENCODED_COMMENT2);
		Collection<Comment> comments = Set.of(comment1, comment2);

		when(jedis.exists(COMMENTS_KEY)).thenReturn(true);
		when(jedis.zrange(COMMENTS_KEY, 0, -1)).thenReturn(encodedComments);

		Collection<Comment> retrievedComments = redisCachingService.getCommentsFromCache(3);
		assertThat(retrievedComments.size(), is(comments.size()));
		assertThat(retrievedComments.containsAll(comments), is(true));

		verify(jedis, times(1)).exists(COMMENTS_KEY);
		verify(jedis, times(1)).zrange(COMMENTS_KEY, 0, -1);
		verify(jedis, times(1)).expire(COMMENTS_KEY, EXPIRATION_TIME);
		verify(cachingServiceCodec, times(1)).decodeComment(ENCODED_COMMENT1);
		verify(cachingServiceCodec, times(1)).decodeComment(ENCODED_COMMENT2);
	}

	@Test
	public void testGetEncodedCommentFromCache() {

		assertThat(redisCachingService.getEncodedCommentFromCache(2), is(ENCODED_COMMENT2));

		verify(jedis, times(1)).get(POST_ID_OF_COMMENT_KEY2);
		verify(jedis, times(1)).zrangeByScore(COMMENTS_KEY, 2, 2);
		verify(jedis, times(1)).expire(COMMENTS_KEY, EXPIRATION_TIME);
	}

	@Test
	public void testGetEncodedCommentFromCachePostIdNotFound() {

		assertThat(redisCachingService.getEncodedCommentFromCache(10), is(nullValue()));

		verify(jedis, times(1)).get("comment/10/postId");
	}

	@Test
	public void testGetEncodedCommentFromCacheNotFound() {

		when(jedis.get("comment/20/postId")).thenReturn("30");
		assertThat(redisCachingService.getEncodedCommentFromCache(20), is(nullValue()));

		verify(jedis, times(1)).get("comment/20/postId");
		verify(jedis, times(1)).zrangeByScore("post/30/comments", 20D, 20D);
	}

	@Test
	public void testGetCommentFromCache() {

		assertThat(redisCachingService.getCommentFromCache(2), is(comment2));

		verify(jedis, times(1)).get(POST_ID_OF_COMMENT_KEY2);
		verify(jedis, times(1)).zrangeByScore(COMMENTS_KEY, 2, 2);
		verify(jedis, times(1)).expire(COMMENTS_KEY, EXPIRATION_TIME);
		verify(cachingServiceCodec, times(1)).decodeComment(ENCODED_COMMENT2);
	}

	@Test
	public void testPutCommentsFromPostIntoCache() {
		Collection<Comment> comments = Set.of(comment1, comment2);

		redisCachingService.putCommentsFromPostIntoCache(3, comments);

		verify(jedis, times(1)).zadd(COMMENTS_KEY, Map.of(ENCODED_COMMENT1, 1.0D, ENCODED_COMMENT2, 2.0D));
		verify(jedis, times(1)).expire(COMMENTS_KEY, EXPIRATION_TIME);
		verify(jedis, times(1)).set(POST_ID_OF_COMMENT_KEY1, "3");
		verify(jedis, times(1)).set(POST_ID_OF_COMMENT_KEY2, "3");
		verify(cachingServiceCodec, times(1)).encodeComment(comment1);
		verify(cachingServiceCodec, times(1)).encodeComment(comment2);
	}

	@Test
	public void testPutCommentIntoCache() {

		when(jedis.exists(COMMENTS_KEY)).thenReturn(true);
		redisCachingService.putCommentIntoCache(comment1);

		verify(jedis, times(1)).exists(COMMENTS_KEY);
		verify(jedis, times(1)).zadd(COMMENTS_KEY, 1.0D, ENCODED_COMMENT1);
		verify(jedis, times(1)).expire(COMMENTS_KEY, EXPIRATION_TIME);
		verify(jedis, times(1)).set(POST_ID_OF_COMMENT_KEY1, "3");
		verify(cachingServiceCodec, times(1)).encodeComment(comment1);
	}

	@Test
	public void testPutCommentIntoCacheCommentsNotInCache() {

		redisCachingService.putCommentIntoCache(comment1);
		verify(jedis, times(1)).exists(COMMENTS_KEY);
		verify(cachingServiceCodec, times(1)).encodeComment(comment1);
	}

	@Test
	public void testRemoveCommentFromCache() {
		redisCachingService.removeCommentFromCache(2);
		verify(jedis, times(1)).get(POST_ID_OF_COMMENT_KEY2);
		verify(jedis, times(1)).zremrangeByScore(COMMENTS_KEY, 2, 2);
	}

	@Test
	public void testTryRemoveCommentFromCacheDoesNotExist() {
		redisCachingService.removeCommentFromCache(7);
		verify(jedis, times(1)).get("comment/7/postId");
		verify(jedis, never()).zremrangeByScore(anyString(), anyInt(), anyInt());
	}

	@Test
	public void testGetEncodedUserSessionFromCache() {
		when(jedis.get(USER_SESSION_KEY)).thenReturn(ENCODED_USER_SESSION);

		assertThat(redisCachingService.getEncodedUserSessionFromCache("sessionId"), is(ENCODED_USER_SESSION));

		verify(jedis, times(1)).get(USER_SESSION_KEY);
		verify(jedis, times(1)).expire(USER_SESSION_KEY, EXPIRATION_TIME);
	}

	@Test
	public void testGetEncodedUserSessionFromCacheNotFound() {
		assertThat(redisCachingService.getEncodedUserSessionFromCache("sessionId"), is(nullValue()));

		verify(jedis, times(1)).get(USER_SESSION_KEY);
		verify(jedis, never()).expire(USER_SESSION_KEY, EXPIRATION_TIME);
	}

	@Test
	public void testGetUserSessionFromCache() {
		when(jedis.get(USER_SESSION_KEY)).thenReturn(ENCODED_USER_SESSION);
		when(cachingServiceCodec.decodeUser(ENCODED_USER_SESSION)).thenReturn(userSession);

		assertThat(redisCachingService.getUserSessionFromCache("sessionId"), is(userSession));

		verify(jedis, times(1)).get(USER_SESSION_KEY);
		verify(jedis, times(1)).expire(USER_SESSION_KEY, EXPIRATION_TIME);
		verify(cachingServiceCodec, times(1)).decodeUser(ENCODED_USER_SESSION);
	}

	@Test
	public void testPutUserSessionIntoCache() {
		when(cachingServiceCodec.encodeUser(userSession)).thenReturn(ENCODED_USER_SESSION);

		redisCachingService.putUserSessionIntoCache("sessionId", userSession);

		verify(jedis, times(1)).set(USER_SESSION_KEY, ENCODED_USER_SESSION);
		verify(jedis, times(1)).expire(USER_SESSION_KEY, EXPIRATION_TIME);
		verify(cachingServiceCodec, times(1)).encodeUser(userSession);
	}

	@Test
	public void testRemoveUserSessionFromCache() {
		redisCachingService.removeUserSessionFromCache("aSessionKey");
		verify(jedis, times(1)).del("user/session/aSessionKey");
	}
}
