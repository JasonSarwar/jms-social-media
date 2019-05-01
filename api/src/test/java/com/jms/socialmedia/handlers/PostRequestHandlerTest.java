package com.jms.socialmedia.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.ForbiddenException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.routes.LocalDateTypeAdapter;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PostRequestHandlerTest {

	private static final String AUTHORIZATION = "Authorization";
	private static final String USER_ID_PARAM = "userId";
	private static final String USERNAME_PARAM = "username";
	private static final String POST_ID_PARAM = "postId";
	private static final String TAG_PARAM = "tag";
	private static final String ON_PARAM = "on";
	private static final String BEFORE_PARAM = "before";
	private static final String AFTER_PARAM = "after";
	private static final String SINCE_POST_ID_PARAM = "sincePostId";
	private static final String SORT_BY_PARAM = "sortBy";
	private static final String ORDER_PARAM = "order";
	private static final String ADD_POST_REQUEST = "{\"userId\":1, \"text\":\"A Cool Post!\"}";
	private static final String EDIT_POST_REQUEST = "{\"text\":\"Editing this Post!\"}";

	@Mock
	private DataService dataService;
	@Mock
	private TokenService tokenService;
	@Mock
	private Request request;
	@Mock
	private Response response;
	@Captor
	private ArgumentCaptor<Collection<Integer>> userIdsCaptor;

	private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

	private PostRequestHandler postRequestHandler;

	@Before
	public void setup() {
		initMocks(this);
		postRequestHandler = new PostRequestHandler(dataService, tokenService, gson);
	}

	@Test
	public void testHandleGetPostsWithOneUserId() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 4, 17, 7, 34)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 2, 15, 6, 12)));

		when(request.queryParams(USER_ID_PARAM)).thenReturn("34");
		when(dataService.getPosts(anyCollection(), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(userIdsCaptor.capture(), eq(null), eq(null), eq(null), eq(null),
				eq(null), eq(null), eq(POST_ID_PARAM), eq(false));

		List<Collection<Integer>> capture = userIdsCaptor.getAllValues();
		assertThat(capture.size(), is(1));
		assertThat(capture.get(0).size(), is(1));
		assertThat(capture.get(0).contains(34), is(true));
	}

	@Test
	public void testHandleGetPostsMultipleUserIds() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 40, "Jason 2", "Jason Sarwar 2", "Another Cool Post", LocalDateTime.of(2019, 4, 17, 7, 34)),
				new Post(3, 56, "Jason 3", "Jason Sarwar 3", "One Last Cool Post",
						LocalDateTime.of(2019, 2, 15, 6, 12)));

		when(request.queryParams(USER_ID_PARAM)).thenReturn("34,40,56");
		when(dataService.getPosts(anyCollection(), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(userIdsCaptor.capture(), eq(null), eq(null), eq(null), eq(null),
				eq(null), eq(null), eq(POST_ID_PARAM), eq(false));

		List<Collection<Integer>> capture = userIdsCaptor.getAllValues();
		assertThat(capture.size(), is(1));
		assertThat(capture.get(0).size(), is(3));
		assertThat(capture.get(0).contains(34), is(true));
		assertThat(capture.get(0).contains(40), is(true));
		assertThat(capture.get(0).contains(56), is(true));
	}

	@Test
	public void testHandleGetPostsWithUsername() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 4, 17, 7, 34)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 2, 15, 6, 12)));

		when(request.queryParams(USERNAME_PARAM)).thenReturn("Jason");
		when(dataService.getPosts(eq(null), eq("Jason"), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(eq(null), eq("Jason"), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false));
	}

	@Test
	public void testHandleGetPostsWithTag() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool #Post", LocalDateTime.of(2019, 4, 17, 7, 34)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool #Post", LocalDateTime.of(2019, 2, 15, 6, 12)));

		when(request.queryParams(TAG_PARAM)).thenReturn("Post");
		when(dataService.getPosts(eq(null), eq(null), eq("Post"), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(eq(null), eq(null), eq("Post"), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false));
	}

	@Test
	public void testHandleGetPostsWithDate() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.queryParams(ON_PARAM)).thenReturn("06-15-2019");
		when(dataService.getPosts(eq(null), eq(null), eq(null), eq("06-15-2019"), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(eq(null), eq(null), eq(null), eq("06-15-2019"), eq(null), eq(null),
				eq(null), eq(POST_ID_PARAM), eq(false));
	}

	@Test
	public void testHandleGetPostsWithSortBy() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.queryParams(SORT_BY_PARAM)).thenReturn(USER_ID_PARAM);
		when(dataService.getPosts(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(USER_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(USER_ID_PARAM), eq(false));
	}

	@Test
	public void testHandleGetPostsWithOrderBy() {
		Collection<Post> posts = Set.of(
				new Post(3, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(5, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.queryParams(ORDER_PARAM)).thenReturn("asc");
		when(dataService.getPosts(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(true))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(true));
	}

	@Test
	public void testHandleGetPostsWithOrderByDesc() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.queryParams(ORDER_PARAM)).thenReturn("desc");
		when(dataService.getPosts(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false));
	}

	@Test
	public void testHandleGetPostsWithAllParameters() {
		Collection<Post> posts = Set.of(
				new Post(3, 33, "Jason1", "Jason Sarwar", "A Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason1", "Jason Sarwar", "Another Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(5, 35, "Jason1", "Jason Sarwar", "One Last Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.queryParams(USER_ID_PARAM)).thenReturn("33,34,35");
		when(request.queryParams(USERNAME_PARAM)).thenReturn("Jason1");
		when(request.queryParams(TAG_PARAM)).thenReturn("Post");
		when(request.queryParams(ON_PARAM)).thenReturn("06-15-2019");
		when(request.queryParams(BEFORE_PARAM)).thenReturn("06-16-2019");
		when(request.queryParams(AFTER_PARAM)).thenReturn("06-14-2019");
		when(request.queryParams(SINCE_POST_ID_PARAM)).thenReturn("2");
		when(request.queryParams(SORT_BY_PARAM)).thenReturn(USER_ID_PARAM);
		when(request.queryParams(ORDER_PARAM)).thenReturn("asc");

		when(dataService.getPosts(anyCollection(), eq("Jason1"), eq("Post"), eq("06-15-2019"), eq("06-16-2019"),
				eq("06-14-2019"), eq(2), eq(USER_ID_PARAM), eq(true))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).queryParams(USER_ID_PARAM);
		verify(request, times(1)).queryParams(USERNAME_PARAM);
		verify(request, times(1)).queryParams(TAG_PARAM);
		verify(request, times(1)).queryParams(ON_PARAM);
		verify(request, times(1)).queryParams(BEFORE_PARAM);
		verify(request, times(1)).queryParams(AFTER_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verify(request, times(1)).queryParams(SORT_BY_PARAM);
		verify(request, times(1)).queryParams(ORDER_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPosts(userIdsCaptor.capture(), eq("Jason1"), eq("Post"), eq("06-15-2019"),
				eq("06-16-2019"), eq("06-14-2019"), eq(2), eq(USER_ID_PARAM), eq(true));

		List<Collection<Integer>> capture = userIdsCaptor.getAllValues();
		assertThat(capture.size(), is(1));
		assertThat(capture.get(0).size(), is(3));
		assertThat(capture.get(0).containsAll(Set.of(33, 34, 35)), is(true));
	}

	@Test
	public void testHandleGetFeedPosts() {

		Collection<Post> posts = Set.of(
				new Post(8, 45, "Jason1", "Jason Sarwar", "A Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(7, 12, "Jason2", "Jason Sarwar 2", "Another Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(6, 78, "Jason3", "Jason Sarwar 3", "One Last Cool #Post",
						LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.params(USER_ID_PARAM)).thenReturn("45");
		when(dataService.getFollowingUserIds(45)).thenReturn(Set.of(12, 78, 23));
		when(dataService.getPosts(anyCollection(), eq(null), eq(null), eq(null), eq(null), eq(null), eq(null),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetFeedPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getFollowingUserIds(45);
		verify(dataService, times(1)).getPosts(userIdsCaptor.capture(), eq(null), eq(null), eq(null), eq(null),
				eq(null), eq(null), eq(POST_ID_PARAM), eq(false));
		verifyNoMoreInteractions(dataService);

		List<Collection<Integer>> capture = userIdsCaptor.getAllValues();
		assertThat(capture.size(), is(1));
		assertThat(capture.get(0).size(), is(4));
		assertThat(capture.get(0).containsAll(Set.of(12, 78, 23, 45)), is(true));
	}

	@Test
	public void testHandleGetFeedPostsWithSincePostId() {

		Collection<Post> posts = Set.of(
				new Post(8, 45, "Jason1", "Jason Sarwar", "A Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(7, 12, "Jason2", "Jason Sarwar 2", "Another Cool #Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(6, 78, "Jason3", "Jason Sarwar 3", "One Last Cool #Post",
						LocalDateTime.of(2019, 6, 15, 6, 23)));

		when(request.params(USER_ID_PARAM)).thenReturn("45");
		when(request.queryParams(SINCE_POST_ID_PARAM)).thenReturn("5");
		when(dataService.getFollowingUserIds(45)).thenReturn(Set.of(12, 78, 23));
		when(dataService.getPosts(anyCollection(), eq(null), eq(null), eq(null), eq(null), eq(null), eq(5),
				eq(POST_ID_PARAM), eq(false))).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetFeedPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).queryParams(SINCE_POST_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getFollowingUserIds(45);
		verify(dataService, times(1)).getPosts(userIdsCaptor.capture(), eq(null), eq(null), eq(null), eq(null),
				eq(null), eq(5), eq(POST_ID_PARAM), eq(false));
		verifyNoMoreInteractions(dataService);

		List<Collection<Integer>> capture = userIdsCaptor.getAllValues();
		assertThat(capture.size(), is(1));
		assertThat(capture.get(0).size(), is(4));
		assertThat(capture.get(0).containsAll(Set.of(12, 78, 23, 45)), is(true));
	}

	@Test
	public void testHandleGetPostsByUserId() {

		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 4, 17, 7, 34)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 2, 15, 6, 12)));

		when(request.params(USER_ID_PARAM)).thenReturn("5");
		when(dataService.getPosts(5)).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetPostsByUserId(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(dataService, times(1)).getPosts(5);
	}

	@Test
	public void testHandleGetPost() {
		Post post = new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 2, 15, 6, 23));

		when(request.params(POST_ID_PARAM)).thenReturn("5");
		when(dataService.getPost(5)).thenReturn(post);

		Post retrievedPost = postRequestHandler.handleGetPost(request, response);
		assertThat(retrievedPost, is(post));
		verify(request, times(1)).params(POST_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPost(5);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);

	}

	@Test
	public void testHandleGetPostWithNoPostId() {
		try {
			postRequestHandler.handleGetPost(request, response);
			fail("Did not throw exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), is("null"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(dataService);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleGetPostWithInvalidPostId() {
		when(request.params(POST_ID_PARAM)).thenReturn("5a");

		try {
			postRequestHandler.handleGetPost(request, response);
			fail("Did not throw exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), containsString("For input string"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(dataService);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleGetPostThatIsNotFound() {
		when(request.params(POST_ID_PARAM)).thenReturn("5");
		when(dataService.getPost(5)).thenReturn(null);
		try {
			postRequestHandler.handleGetPost(request, response);
			fail("Did not throw exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Post Not Found"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(dataService, times(1)).getPost(5);
			verifyNoMoreInteractions(dataService);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddPost() throws IOException {
		Post post = new Post();
		post.setUserId(1);
		post.setText("A Cool Post!");

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_POST).build();

		when(request.body()).thenReturn(ADD_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.addPost(post)).thenReturn(true);

		boolean postAdded = postRequestHandler.handleAddPost(request, response);
		assertThat(postAdded, is(true));

		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).addPost(post);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleAddPostByAdmin() throws IOException {
		Post post = new Post();
		post.setUserId(1);
		post.setText("A Cool Post!");

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.body()).thenReturn(ADD_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.addPost(post)).thenReturn(true);

		boolean postAdded = postRequestHandler.handleAddPost(request, response);
		assertThat(postAdded, is(true));

		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).addPost(post);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleAddPostWithJsonContentType() throws IOException {
		Post post = new Post();
		post.setUserId(1);
		post.setText("A Cool Post!");

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_POST).build();

		when(request.body()).thenReturn(ADD_POST_REQUEST);
		when(request.contentType()).thenReturn("application/json");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.addPost(post)).thenReturn(true);

		boolean postAdded = postRequestHandler.handleAddPost(request, response);
		assertThat(postAdded, is(true));

		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).addPost(post);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleAddPostWithNoText() throws IOException {

		when(request.body()).thenReturn("{\"userId\":1}");
		try {
			postRequestHandler.handleAddPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Add Post Request requires 'text'"));
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddPostWithNoUserId() throws IOException {

		when(request.body()).thenReturn("{\"text\":\"Post Text\"}");
		try {
			postRequestHandler.handleAddPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Add Post Request requires a 'userId'"));
			verify(request, times(1)).body();
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddPostWithNoUserIdOrText() throws IOException {

		when(request.body()).thenReturn("{}");
		try {
			postRequestHandler.handleAddPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Add Post Request requires a 'userId'\nAdd Post Request requires 'text'"));
			verify(request, times(1)).body();
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddPostByUnauthorizedPermission() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_COMMENT).build();

		when(request.body()).thenReturn(ADD_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);

		try {
			postRequestHandler.handleAddPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(ForbiddenException.class));
			assertThat(e.getMessage(), is("User not allowed to Add Post"));
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddPostByUnauthorizedUserId() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.ADD_POST).build();

		when(request.body()).thenReturn(ADD_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);

		try {
			postRequestHandler.handleAddPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(ForbiddenException.class));
			assertThat(e.getMessage(), is("User not allowed to Add Post"));
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditPost() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_POST).build();

		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);
		when(dataService.editPost(3, "Editing this Post!")).thenReturn(true);

		boolean postEdited = postRequestHandler.handleEditPost(request, response);
		assertThat(postEdited, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromPostId(3);
		verify(dataService, times(1)).editPost(3, "Editing this Post!");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditPostByAdmin() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.ADMIN).build();

		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);
		when(dataService.editPost(3, "Editing this Post!")).thenReturn(true);

		boolean postEdited = postRequestHandler.handleEditPost(request, response);
		assertThat(postEdited, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromPostId(3);
		verify(dataService, times(1)).editPost(3, "Editing this Post!");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditPostWithJsonContentType() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_POST).build();

		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		when(request.contentType()).thenReturn("application/json");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);
		when(dataService.editPost(3, "Editing this Post!")).thenReturn(true);

		boolean postEdited = postRequestHandler.handleEditPost(request, response);
		assertThat(postEdited, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromPostId(3);
		verify(dataService, times(1)).editPost(3, "Editing this Post!");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditPostWithNoPostId() throws IOException {

		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		try {
			postRequestHandler.handleEditPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), is("null"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditPostWithInvalidPostId() throws IOException {

		when(request.params(POST_ID_PARAM)).thenReturn("a");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		try {
			postRequestHandler.handleEditPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), containsString("For input string"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditPostWithNoText() throws IOException {

		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn("{}");
		try {
			postRequestHandler.handleEditPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Edit Post Request requires 'text'"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditPostThatDoesNotExist() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_POST).build();
		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);
		when(dataService.editPost(3, "Editing this Post!")).thenReturn(false);

		try {
			postRequestHandler.handleEditPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Post Not Found"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromPostId(3);
			verify(dataService, times(1)).editPost(3, "Editing this Post!");
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditPostByUnauthorizedPermission() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.ADD_COMMENT).build();
		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);

		try {
			postRequestHandler.handleEditPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(ForbiddenException.class));
			assertThat(e.getMessage(), is("User not allowed to Edit Post"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromPostId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditPostByUnauthorizedUserId() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.EDIT_POST).build();
		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_POST_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);

		try {
			postRequestHandler.handleEditPost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(ForbiddenException.class));
			assertThat(e.getMessage(), is("User not allowed to Edit Post"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromPostId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeletePost() throws IOException {
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.DELETE_POST).build();

		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(1);
		when(dataService.deletePost(3)).thenReturn(true);

		boolean postDeleted = postRequestHandler.handleDeletePost(request, response);
		assertThat(postDeleted, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromPostId(3);
		verify(dataService, times(1)).deletePost(3);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleDeletePostByAdmin() throws IOException {
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(1);
		when(dataService.deletePost(3)).thenReturn(true);

		boolean postDeleted = postRequestHandler.handleDeletePost(request, response);
		assertThat(postDeleted, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromPostId(3);
		verify(dataService, times(1)).deletePost(3);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleDeletePostWithNoPostId() throws IOException {

		try {
			postRequestHandler.handleDeletePost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), is("null"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeletePostWithInvalidPostId() throws IOException {

		when(request.params(POST_ID_PARAM)).thenReturn("a");
		try {
			postRequestHandler.handleDeletePost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), containsString("For input string"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeletePostThatDoesNotExist() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.DELETE_POST).build();
		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(1);
		when(dataService.deletePost(3)).thenReturn(false);

		try {
			postRequestHandler.handleDeletePost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Post Not Found"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromPostId(3);
			verify(dataService, times(1)).deletePost(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeletePostByUnauthorizedPermission() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_COMMENT).build();
		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(1);

		try {
			postRequestHandler.handleDeletePost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(ForbiddenException.class));
			assertThat(e.getMessage(), is("User not allowed to Delete Post"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromPostId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeletePostByUnauthorizedUserId() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.DELETE_POST).build();
		when(request.params(POST_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromPostId(3)).thenReturn(2);

		try {
			postRequestHandler.handleDeletePost(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(ForbiddenException.class));
			assertThat(e.getMessage(), is("User not allowed to Delete Post"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromPostId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleGetCommentedPosts() {
		Collection<Post> posts = Set.of(
				new Post(5, 34, "Jason", "Jason Sarwar", "A Cool Post", LocalDateTime.of(2019, 6, 15, 6, 23)),
				new Post(4, 34, "Jason", "Jason Sarwar", "Another Cool Post", LocalDateTime.of(2019, 4, 17, 7, 34)),
				new Post(3, 34, "Jason", "Jason Sarwar", "One Last Cool Post", LocalDateTime.of(2019, 2, 15, 6, 12)));

		when(request.params(USER_ID_PARAM)).thenReturn("8");
		when(dataService.getCommentedPostsByUserId(8)).thenReturn(posts);

		Collection<Post> retrievedPosts = postRequestHandler.handleGetCommentedPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).params(USER_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getCommentedPostsByUserId(8);
		verifyNoMoreInteractions(dataService);
	}

}
