package com.jms.socialmedia.cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Collections;

public class CachingServiceTest {

	@Mock
	private AbstractCachingService cachingService;
	@Mock
	private DataService dataService;

	private Post post;
	private Collection<Comment> comments;
	private User user;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		post = new Post(1);
		comments = Collections.singleton(new Comment(10, 1, "text", null));
		user = new User(1, "User", "Full Name", "Hashed Password");
		when(cachingService.getPostFromCacheOrSupplier(anyInt(), any())).thenCallRealMethod();
		when(cachingService.getCommentsFromCacheOrSupplier(anyInt(), any())).thenCallRealMethod();
		when(cachingService.getUserSessionCacheOrSupplier(anyString(), any())).thenCallRealMethod();
		when(dataService.getPost(1)).thenReturn(post);
		when(dataService.getComments(1)).thenReturn(comments);
		when(dataService.getUserBySessionId("sessionKey")).thenReturn(user);
	}

	@Test
	public void testGetPostFromCacheOrSupplier() {
		assertThat(cachingService.getPostFromCacheOrSupplier(1, () -> dataService.getPost(1)), is(post));
		verify(cachingService, times(1)).getPostFromCache(1);
		verify(dataService, times(1)).getPost(1);
		verify(cachingService, times(1)).putPostIntoCache(post);
	}

	@Test
	public void testGetPostFromCacheOrSupplierAlreadyInCache() {
		when(cachingService.getPostFromCache(1)).thenReturn(post);
		assertThat(cachingService.getPostFromCacheOrSupplier(1, () -> dataService.getPost(1)), is(post));
		verify(cachingService, times(1)).getPostFromCache(1);
		verifyZeroInteractions(dataService);
		verify(cachingService, never()).putPostIntoCache(any());
	}

	@Test
	public void testGetPostFromCacheOrSupplierNotInCacheOrSupplier() {
		assertThat(cachingService.getPostFromCacheOrSupplier(2, () -> dataService.getPost(2)), is(nullValue()));
		verify(cachingService, times(1)).getPostFromCache(2);
		verify(dataService, times(1)).getPost(2);
		verify(cachingService, never()).putPostIntoCache(any());
	}

	@Test
	public void testGetCommentsFromCacheOrSupplierNotInCache() {
		when(cachingService.getCommentsFromCache(1)).thenReturn(null);
		assertThat(cachingService.getCommentsFromCacheOrSupplier(1, () -> dataService.getComments(1)), is(comments));
		verify(cachingService, times(1)).getCommentsFromCache(1);
		verify(dataService, times(1)).getComments(1);
		verify(cachingService, times(1)).putCommentsFromPostIntoCache(1, comments);
	}

	@Test
	public void testGetCommentsFromCacheOrSupplierAlreadyInCache() {
		when(cachingService.getCommentsFromCache(1)).thenReturn(comments);
		assertThat(cachingService.getCommentsFromCacheOrSupplier(1, () -> dataService.getComments(1)), is(comments));
		verify(cachingService, times(1)).getCommentsFromCache(1);
		verifyZeroInteractions(dataService);
		verify(cachingService, never()).putCommentsFromPostIntoCache(anyInt(), any());
	}

	@Test
	public void testGetCommentsFromCacheOrSupplierNotInCacheOrSupplier() {
		when(cachingService.getCommentsFromCache(2)).thenReturn(null);
		when(dataService.getComments(2)).thenReturn(null);
		assertThat(cachingService.getCommentsFromCacheOrSupplier(2, () -> dataService.getComments(2)),
				is(nullValue()));
		verify(cachingService, times(1)).getCommentsFromCache(2);
		verify(dataService, times(1)).getComments(2);
		verify(cachingService, never()).putCommentsFromPostIntoCache(anyInt(), any());
	}

	@Test
	public void testGetUserSessionFromCacheOrSupplier() {
		assertThat(cachingService.getUserSessionCacheOrSupplier("sessionKey",
				() -> dataService.getUserBySessionId("sessionKey")), is(user));
		verify(cachingService, times(1)).getUserSessionFromCache("sessionKey");
		verify(dataService, times(1)).getUserBySessionId("sessionKey");
		verify(cachingService, times(1)).putUserSessionIntoCache("sessionKey", user);
	}

	@Test
	public void testGetUserSessionFromCacheOrSupplierAlreadyInCache() {
		when(cachingService.getUserSessionFromCache("sessionKey")).thenReturn(user);
		assertThat(cachingService.getUserSessionCacheOrSupplier("sessionKey",
				() -> dataService.getUserBySessionId("sessionKey")), is(user));
		verify(cachingService, times(1)).getUserSessionFromCache("sessionKey");
		verifyZeroInteractions(dataService);
		verify(cachingService, never()).putUserSessionIntoCache(any(), any());
	}

	@Test
	public void testGetUserSessionFromCacheOrSupplierNotInCacheOrSupplier() {
		assertThat(cachingService.getUserSessionCacheOrSupplier("sessionKey2",
				() -> dataService.getUserBySessionId("sessionKey2")), is(nullValue()));
		verify(cachingService, times(1)).getUserSessionFromCache("sessionKey2");
		verify(dataService, times(1)).getUserBySessionId("sessionKey2");
		verify(cachingService, never()).putUserSessionIntoCache(any(), any());
	}
}
