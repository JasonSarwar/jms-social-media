package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.routes.LocalDateTypeAdapter;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FollowRequestHandlerTest {

	private static final String AUTHORIZATION = "Authorization";
	private static final String USER_ID_PARAM = "userId";
	private static final String FOLLOW_REQUEST = "{\"followerUserId\":5, \"followingUserId\":11}";
	private static final String FOLLOW_REQUEST_SAME_IDS = "{\"followerUserId\":6, \"followingUserId\":6}";

	@Mock
	private DataService dataService;
	@Mock
	private TokenService tokenService;
	@Mock
	private Request request;
	@Mock
	private Response response;

	private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

	private FollowRequestHandler followRequestHandler;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		followRequestHandler = new FollowRequestHandler(dataService, tokenService, gson);
	}

	@Test
	public void testHandleFollowUser() throws IOException {
		Token token = Token.newBuilder().setUserId(5).addPermissions(Permission.FOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.followUser(5, 11)).thenReturn(true);

		boolean followUser = followRequestHandler.handleFollowUser(request, response);
		assertThat(followUser, is(true));
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(request, times(1)).contentType();
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).followUser(5, 11);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleFollowUserWithMatchingUserIds() throws IOException {
		Token token = Token.newBuilder().setUserId(6).addPermissions(Permission.FOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST_SAME_IDS);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.followUser(6, 6)).thenReturn(true);

		try {
			followRequestHandler.handleFollowUser(request, response);
			fail("Did not throw exception");
		} catch (Exception e) {
			verify(request, times(1)).body();
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleUnfollowUser() throws IOException {
		Token token = Token.newBuilder().setUserId(5).addPermissions(Permission.UNFOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.unfollowUser(5, 11)).thenReturn(true);

		boolean unfollowUser = followRequestHandler.handleUnfollowUser(request, response);
		assertThat(unfollowUser, is(true));
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(request, times(1)).contentType();
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).unfollowUser(5, 11);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleUnfollowUserWithMatchingUserIds() throws IOException {
		Token token = Token.newBuilder().setUserId(6).addPermissions(Permission.FOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST_SAME_IDS);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.unfollowUser(6, 6)).thenReturn(true);

		try {
			followRequestHandler.handleUnfollowUser(request, response);
			fail("Did not throw exception");
		} catch (Exception e) {
			verify(request, times(1)).body();
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleGetFollowingUserIds() {
		Collection<Integer> userIds = Set.of(6, 7, 8, 9);
		when(request.params(USER_ID_PARAM)).thenReturn("5");
		when(dataService.getFollowingUserIds(5)).thenReturn(userIds);

		Collection<Integer> retrievedUserIds = followRequestHandler.handleGetFollowingUserIds(request, response);
		assertThat(retrievedUserIds, is(userIds));
		verify(request, times(1)).params(USER_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getFollowingUserIds(5);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetUsersToFollow30UsersNoMax() {
		Collection<User> users = createUsersToFollow(30);
		when(request.params(USER_ID_PARAM)).thenReturn("100");
		when(dataService.getUsersToFollow(100)).thenReturn(users);

		Collection<User> retrievedUsers = followRequestHandler.handleGetUsersToFollow(request, response);
		assertThat(retrievedUsers, is(users));

		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).queryParams("max");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsersToFollow(100);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetUsersToFollow30UsersMax10() {
		Collection<User> users = createUsersToFollow(30);
		when(request.params(USER_ID_PARAM)).thenReturn("100");
		when(request.queryParams("max")).thenReturn("10");
		when(dataService.getUsersToFollow(100)).thenReturn(users);

		Collection<User> retrievedUsers = followRequestHandler.handleGetUsersToFollow(request, response);
		assertThat(retrievedUsers.size(), is(10));
		assertThat(users.containsAll(retrievedUsers), is(true));

		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).queryParams("max");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsersToFollow(100);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetUsersToFollow10UsersMax30() {
		Collection<User> users = createUsersToFollow(10);
		when(request.params(USER_ID_PARAM)).thenReturn("100");
		when(request.queryParams("max")).thenReturn("30");
		when(dataService.getUsersToFollow(100)).thenReturn(users);

		Collection<User> retrievedUsers = followRequestHandler.handleGetUsersToFollow(request, response);
		assertThat(retrievedUsers, is(users));

		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).queryParams("max");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsersToFollow(100);
		verifyNoMoreInteractions(dataService);
	}

	private static Collection<User> createUsersToFollow(int no) {
		Collection<User> users = new HashSet<>(no);
		for (int i = 0; i < no; i++) {
			users.add(new User(no, "User" + no));
		}
		return users;
	}

}
