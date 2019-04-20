package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.LoginSuccess;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;
import com.jms.socialmedia.password.PasswordService;
import com.jms.socialmedia.routes.LocalDateTypeAdapter;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class UserRequestHandlerTest {

	private static final String SESSION_COOKIE = "jms-social-media-session";
	private static final String SESSION_KEY = "sessionKey";

	@Mock
	private DataService dataService;
	@Mock
	private TokenService tokenService;
	@Mock
	private PasswordService passwordService;
	@Mock
	private Request request;
	@Mock
	private Response response;

	private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

	private UserRequestHandler userRequestHandler;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		userRequestHandler = new UserRequestHandler(dataService, passwordService, tokenService,
				Collections.singleton(1), gson);
	}

	@Test
	public void testHandleGetUserPage() {

		UserPage userPage = new UserPage();
		userPage.setUserId(1);
		userPage.setUsername("Jason");
		Collection<Integer> followerUserIds = Set.of(2, 3, 4, 5, 6);
		Collection<Integer> followingUserIds = Set.of(20, 30, 40, 50, 60);

		when(request.params("username")).thenReturn("Jason");
		when(dataService.getUserPageInfoByName("Jason")).thenReturn(userPage);
		when(dataService.getFollowerUserIds(1)).thenReturn(followerUserIds);
		when(dataService.getFollowingUserIds(1)).thenReturn(followingUserIds);

		UserPage retrievedUserPage = userRequestHandler.handleGetUserPage(request, response);
		assertThat(retrievedUserPage.getUserId(), is(1));
		assertThat(retrievedUserPage.getUsername(), is("Jason"));
		assertThat(retrievedUserPage.getFollowersUserIds(), is(followerUserIds));
		assertThat(retrievedUserPage.getFollowingUserIds(), is(followingUserIds));

		verify(request, times(1)).params("username");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUserPageInfoByName("Jason");
		verify(dataService, times(1)).getFollowerUserIds(1);
		verify(dataService, times(1)).getFollowingUserIds(1);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleGetUserPageNotFound() {

		when(request.params("username")).thenReturn("Jason");

		try {
			userRequestHandler.handleGetUserPage(request, response);
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("User not found"));
			verify(request, times(1)).params("username");
			verifyNoMoreInteractions(request);
			verify(dataService, times(1)).getUserPageInfoByName("Jason");
			verifyNoMoreInteractions(dataService);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(passwordService);
		}
	}

	@Test
	public void testHandleGetUsernamesAndIds() {
		Collection<User> users = createUsersToFollow(5);
		when(request.queryParams("ids")).thenReturn("0,1,2,3,4");
		when(dataService.getUsernamesByIds(List.of(0, 1, 2, 3, 4))).thenReturn(users);

		Collection<User> retrievedUsers = userRequestHandler.handleGetUsernamesAndIds(request, response);
		assertThat(retrievedUsers, is(users));

		verify(request, times(1)).queryParams("ids");
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getUsernamesByIds(List.of(0, 1, 2, 3, 4));
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleGetUsernamesAndIdsBadRequest() {
		when(request.queryParams("ids")).thenReturn("");

		try {
			userRequestHandler.handleGetUsernamesAndIds(request, response);
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("No User IDs included"));
			verify(request, times(1)).queryParams("ids");
			verifyNoMoreInteractions(request);
			verifyZeroInteractions(dataService);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(passwordService);
		}
	}

	@Test
	public void testHandleIsUsernameTaken() {
		testHandleIsUsernameTakenValidUsername("Username");
		testHandleIsUsernameTakenValidUsername("Username1");
		testHandleIsUsernameTakenValidUsername("User_name");
		testHandleIsUsernameTakenValidUsername("User_Name1");
		testHandleIsUsernameTakenValidUsername("User_name_3");
		testHandleIsUsernameTakenValidUsername("Us_er_na_me");
		testHandleIsUsernameTakenValidUsername("1234");
		testHandleIsUsernameTakenInvalidUsername("-");
		testHandleIsUsernameTakenInvalidUsername("!");
		testHandleIsUsernameTakenInvalidUsername("@");
		testHandleIsUsernameTakenInvalidUsername("#");
		testHandleIsUsernameTakenInvalidUsername("$");
		testHandleIsUsernameTakenInvalidUsername("%");
		testHandleIsUsernameTakenInvalidUsername("^");
		testHandleIsUsernameTakenInvalidUsername("&");
		testHandleIsUsernameTakenInvalidUsername("*");
		testHandleIsUsernameTakenInvalidUsername("(");
		testHandleIsUsernameTakenInvalidUsername(")");
		testHandleIsUsernameTakenInvalidUsername("+");
		testHandleIsUsernameTakenInvalidUsername("=");
	}

	private void testHandleIsUsernameTakenValidUsername(String username) {
		when(request.params("username")).thenReturn(username);
		when(dataService.isUsernameTaken(username)).thenReturn(true);

		assertThat(userRequestHandler.handleIsUsernameTaken(request, response), is(true));
		verify(request, atLeastOnce()).params("username");
		verify(dataService, atLeastOnce()).isUsernameTaken(username);
	}

	private void testHandleIsUsernameTakenInvalidUsername(String username) {
		when(request.params("username")).thenReturn(username);

		try {
			userRequestHandler.handleIsUsernameTaken(request, response);
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Invalid Username"));
			verify(request, atLeastOnce()).params("username");
		}
	}

	@Test
	public void testHandleIsEmailTaken() {
		testHandleIsEmailTakenValidEmail("jason@jms.com");
		testHandleIsEmailTakenValidEmail("jason-sarwar@jms.com");
		testHandleIsEmailTakenValidEmail("jason.sarwar@jms.com");
		testHandleIsEmailTakenValidEmail("jason_sarwar@jms.com");
		testHandleIsEmailTakenValidEmail("jason+sarwar@jms.com");
		testHandleIsEmailTakenValidEmail("jason@jms-social.com");
		testHandleIsEmailTakenValidEmail("jason@jms.social.com");
		testHandleIsEmailTakenValidEmail("jason@jms_social.com");
		testHandleIsEmailTakenValidEmail("jason@jms+social.com");
		testHandleIsEmailTakenInvalidEmail("jason");
		testHandleIsEmailTakenInvalidEmail("jason@jms");
		testHandleIsEmailTakenInvalidEmail("ja@son@jms.com");
		testHandleIsEmailTakenInvalidEmail("ja!son@jms.com");
		testHandleIsEmailTakenInvalidEmail("ja%son@jms.com");
		testHandleIsEmailTakenInvalidEmail("ja$son@jms.com");
		testHandleIsEmailTakenInvalidEmail("ja#son@jms.com");
		testHandleIsEmailTakenInvalidEmail("ja*son@jms.com");
	}

	private void testHandleIsEmailTakenValidEmail(String email) {
		when(request.params("email")).thenReturn(email);
		when(dataService.isEmailTaken(email)).thenReturn(true);

		assertThat(userRequestHandler.handleIsEmailTaken(request, response), is(true));
		verify(request, atLeastOnce()).params("email");
		verify(dataService, atLeastOnce()).isEmailTaken(email);
	}

	private void testHandleIsEmailTakenInvalidEmail(String email) {
		when(request.params("email")).thenReturn(email);

		try {
			userRequestHandler.handleIsEmailTaken(request, response);
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Invalid Email Address"));
			verify(request, atLeastOnce()).params("email");
		}
	}

	@Test
	public void testHandleSessionRetrievalNoCookie() throws IOException {

		assertThat(userRequestHandler.handleSessionRetrieval(request, response), is(nullValue()));
		verify(request, times(1)).cookie(SESSION_COOKIE);
		verifyZeroInteractions(dataService);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleSessionRetrievalCookieNoUserFound() throws IOException {

		when(request.cookie(SESSION_COOKIE)).thenReturn(SESSION_KEY);
		assertThat(userRequestHandler.handleSessionRetrieval(request, response), is(nullValue()));
		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).getUserBySessionKey(SESSION_KEY);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleSessionRetrievalAdminRights() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.cookie(SESSION_COOKIE)).thenReturn(SESSION_KEY);
		when(dataService.getUserBySessionKey(SESSION_KEY)).thenReturn(new User(1, "Username", "Full Name"));
		when(tokenService.createTokenString(token)).thenReturn("TokenString");

		LoginSuccess loginSuccess = userRequestHandler.handleSessionRetrieval(request, response);
		assertThat(loginSuccess.getUserId(), is(1));
		assertThat(loginSuccess.getUsername(), is("Username"));
		assertThat(loginSuccess.getFirstname(), is("Full"));
		assertThat(loginSuccess.getToken(), is("TokenString"));

		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).getUserBySessionKey(SESSION_KEY);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleSessionRetrievalRegularRights() throws IOException {

		Token token = Token.newBuilder().setUserId(23).addPermissions(Permission.getRegularPermissions()).build();

		when(request.cookie(SESSION_COOKIE)).thenReturn(SESSION_KEY);
		when(dataService.getUserBySessionKey(SESSION_KEY)).thenReturn(new User(23, "Username", "Full Name"));
		when(tokenService.createTokenString(token)).thenReturn("TokenString");

		LoginSuccess loginSuccess = userRequestHandler.handleSessionRetrieval(request, response);
		assertThat(loginSuccess.getUserId(), is(23));
		assertThat(loginSuccess.getUsername(), is("Username"));
		assertThat(loginSuccess.getFirstname(), is("Full"));
		assertThat(loginSuccess.getToken(), is("TokenString"));

		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).getUserBySessionKey(SESSION_KEY);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleLogout() {
		when(request.cookie(SESSION_COOKIE)).thenReturn("cookie");
		userRequestHandler.handleLogout(request, response);
		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).removeSessionKey("cookie");
		verify(response, times(1)).removeCookie(SESSION_COOKIE);
	}

	private static Collection<User> createUsersToFollow(int no) {
		Collection<User> users = new HashSet<>(no);
		for (int i = 0; i < no; i++) {
			users.add(new User(no, "User" + no));
		}
		return users;
	}

}
