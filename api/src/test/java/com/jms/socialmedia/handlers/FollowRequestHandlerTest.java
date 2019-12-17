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
import com.jms.socialmedia.exception.BadRequestException;
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
	private static final String USERNAME_PARAM = "username";
	private static final String TEST_USERNAME = "Jason";
	private static final String FOLLOW_REQUEST = "{\"followerUsername\": \"Jason\", \"followingUsername\": \"Sarwar\"}";
	private static final String FOLLOW_REQUEST_SAME_NAMES = "{\"followerUsername\": \"Jason\", \"followingUsername\": \"Jason\"}";
	private static final String FOLLOW_REQUEST_SAME_NAMES_IRREGULAR = "{\"followerUsername\": \" Jason \", \"followingUsername\": \"  jasOn\"}";

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
		Token token = Token.newBuilder().setUsername(TEST_USERNAME).addPermissions(Permission.FOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.followUser(null, TEST_USERNAME, null, "Sarwar")).thenReturn(true);

		boolean followUser = followRequestHandler.handleFollowUser(request, response);
		assertThat(followUser, is(true));
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(request, times(1)).contentType();
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).followUser(null, TEST_USERNAME, null, "Sarwar");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleFollowUserWithMatchingUsernames() throws IOException {
		Token token = Token.newBuilder().setUsername(TEST_USERNAME).addPermissions(Permission.FOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST_SAME_NAMES);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);

		try {
			followRequestHandler.handleFollowUser(request, response);
			fail("Did not throw exception");
		} catch (BadRequestException e) {
			
			assertThat(e.getMessage(), is("A User cannot follow themself"));
			verify(request, times(1)).body();
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleFollowUserWithMatchingIrregularUsernames() throws IOException {
		Token token = Token.newBuilder().setUsername(TEST_USERNAME).addPermissions(Permission.FOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST_SAME_NAMES_IRREGULAR);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);

		try {
			followRequestHandler.handleFollowUser(request, response);
			fail("Did not throw exception");
		} catch (BadRequestException e) {
			
			assertThat(e.getMessage(), is("A User cannot follow themself"));
			verify(request, times(1)).body();
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleUnfollowUser() throws IOException {
		Token token = Token.newBuilder().setUsername(TEST_USERNAME).addPermissions(Permission.UNFOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.unfollowUser(null, TEST_USERNAME, null, "Sarwar")).thenReturn(true);

		boolean unfollowUser = followRequestHandler.handleUnfollowUser(request, response);
		assertThat(unfollowUser, is(true));
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(request, times(1)).contentType();
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).unfollowUser(null, TEST_USERNAME, null, "Sarwar");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleUnfollowUserWithMatchingUsernames() throws IOException {
		Token token = Token.newBuilder().setUsername(TEST_USERNAME).addPermissions(Permission.UNFOLLOW_USER).build();

		when(request.body()).thenReturn(FOLLOW_REQUEST_SAME_NAMES);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);

		try {
			followRequestHandler.handleUnfollowUser(request, response);
			fail("Did not throw exception");
		} catch (BadRequestException e) {

			assertThat(e.getMessage(), is("A User cannot unfollow themself"));
			verify(request, times(1)).body();
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleGetFollowingUsernames() {
		Collection<String> followingUsernames = Set.of("Rob", "Mike", "Matt");
		when(request.params(USERNAME_PARAM)).thenReturn(TEST_USERNAME);
		when(dataService.getFollowingUsernames(TEST_USERNAME)).thenReturn(followingUsernames);

		Collection<String> retrievedUsernames = followRequestHandler.handleGetFollowingUsernames(request, response);
		assertThat(retrievedUsernames, is(followingUsernames));
		verify(request, times(1)).params(USERNAME_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getFollowingUsernames(TEST_USERNAME);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetUsersToFollow30UsersNoMax() {
		Collection<String> users = createUsernamesToFollow(30);
		when(request.params(USERNAME_PARAM)).thenReturn(TEST_USERNAME);
		when(dataService.getUsernamesToFollow(TEST_USERNAME)).thenReturn(users);

		Collection<String> retrievedUsers = followRequestHandler.handleGetUsersToFollow(request, response);
		assertThat(retrievedUsers.size(), is(30));
		assertThat(retrievedUsers, is(users));

		verify(request, times(1)).params(USERNAME_PARAM);
		verify(request, times(1)).queryParams("max");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsernamesToFollow(TEST_USERNAME);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetUsersToFollow30UsersMax10() {
		Collection<String> users = createUsernamesToFollow(30);
		when(request.params(USERNAME_PARAM)).thenReturn(TEST_USERNAME);
		when(request.queryParams("max")).thenReturn("10");
		when(dataService.getUsernamesToFollow(TEST_USERNAME)).thenReturn(users);

		Collection<String> retrievedUsers = followRequestHandler.handleGetUsersToFollow(request, response);
		assertThat(retrievedUsers.size(), is(10));
		assertThat(users.containsAll(retrievedUsers), is(true));

		verify(request, times(1)).params(USERNAME_PARAM);
		verify(request, times(1)).queryParams("max");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsernamesToFollow(TEST_USERNAME);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetUsersToFollow10UsersMax30() {
		Collection<String> users = createUsernamesToFollow(10);
		when(request.params(USERNAME_PARAM)).thenReturn(TEST_USERNAME);
		when(request.queryParams("max")).thenReturn("30");
		when(dataService.getUsernamesToFollow(TEST_USERNAME)).thenReturn(users);

		Collection<String> retrievedUsers = followRequestHandler.handleGetUsersToFollow(request, response);
		assertThat(retrievedUsers.size(), is(10));
		assertThat(retrievedUsers, is(users));

		verify(request, times(1)).params(USERNAME_PARAM);
		verify(request, times(1)).queryParams("max");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsernamesToFollow(TEST_USERNAME);
		verifyNoMoreInteractions(dataService);
	}

	private static Collection<String> createUsernamesToFollow(int no) {
		Collection<String> users = new HashSet<>(no);
		for (int i = 0; i < no; i++) {
			users.add("User" + i);
		}
		return users;
	}

}
