package com.jms.socialmedia.cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
	private CachingService cachingService;
	@Mock
	private DataService dataService;
	
	private Post post;
	private Collection<Comment> comments;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		post = new Post(1);
		comments = Collections.singleton(new Comment(10, 1, "text", null));
		when(cachingService.getPostFromCacheOrSupplier(anyInt(), any())).thenCallRealMethod();
		when(cachingService.getCommentsFromCacheOrSupplier(anyInt(), any())).thenCallRealMethod();
		when(dataService.getPost(1)).thenReturn(post);
		when(dataService.getComments(1)).thenReturn(comments);
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
	public void testGetCommentsFromCacheOrSupplier() {
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
		assertThat(cachingService.getCommentsFromCacheOrSupplier(2, () -> dataService.getComments(2)), is(Collections.emptyList()));
		verify(cachingService, times(1)).getCommentsFromCache(2);
		verify(dataService, times(1)).getComments(2);
		verify(cachingService, never()).putCommentsFromPostIntoCache(2, Collections.emptySet());
	}
}
