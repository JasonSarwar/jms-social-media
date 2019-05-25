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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.DatabaseInsertException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.ChangePassword;
import com.jms.socialmedia.model.LoginRequest;
import com.jms.socialmedia.model.LoginSuccess;
import com.jms.socialmedia.model.NewUser;
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
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class UserRequestHandlerTest {

	private static final String SESSION_COOKIE = "jms-social-media-session";
	private static final String SESSION_KEY = "sessionKey";
	private static final String AUTHORIZATION = "Authorization";
	private static final String LOGIN_REQUEST_JSON = "{\"usernameOrEmail\":\"Jason\", \"password\":\"Password\"}";

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
	@Captor
	private ArgumentCaptor<String> sessionKeyCaptor;

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
	public void testHandleAddUser() throws IOException {
		NewUser newUser = createNewUser();
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.body()).thenReturn(createNewUserJson("password", "password"));
		when(passwordService.encryptPassword("password")).thenReturn("hashedPassword");

		when(dataService.addUser(newUser)).then(invocation -> {
			NewUser newUserInvocation = invocation.getArgument(0);
			newUserInvocation.setUserId(1);
			return true;
		});

		when(request.queryParams("createSession")).thenReturn("true");
		when(tokenService.createTokenString(token)).thenReturn("TokenString");
		when(dataService.addUserSession(eq(1), anyString())).thenReturn(true);

		LoginSuccess loginSuccess = userRequestHandler.handleAddUser(request, response);
		assertThat(loginSuccess.getUserId(), is(1));
		assertThat(loginSuccess.getUsername(), is("Jason"));
		assertThat(loginSuccess.getFirstname(), is("Jason"));
		assertThat(loginSuccess.getToken(), is("TokenString"));

		verify(request, times(1)).body();
		verify(dataService, times(1)).isUsernameTaken("Jason");
		verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
		verify(passwordService, times(1)).encryptPassword("password");
		// verify(dataService, times(1)).addUser(newUser);
		verify(request, times(1)).queryParams("createSession");
		verify(tokenService, times(1)).createTokenString(token);
		verify(dataService, times(1)).addUserSession(eq(1), sessionKeyCaptor.capture());
		verify(response, times(1)).cookie("/", SESSION_COOKIE, sessionKeyCaptor.getValue(), 24 * 60 * 60 * 180, false);
	}

	@Test
	public void testHandleAddUserWithFalseSessionFlag() throws IOException {
		testHandleAddUserWithoutSessionFlag("false");
	}

	@Test
	public void testHandleAddUserWithNullSessionFlag() throws IOException {
		testHandleAddUserWithoutSessionFlag(null);
	}

	private void testHandleAddUserWithoutSessionFlag(String createSessionFlag) throws IOException {
		NewUser newUser = createNewUser();
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.body()).thenReturn(createNewUserJson("password", "password"));
		when(passwordService.encryptPassword("password")).thenReturn("hashedPassword");

		when(dataService.addUser(newUser)).then(invocation -> {
			NewUser newUserInvocation = invocation.getArgument(0);
			newUserInvocation.setUserId(1);
			return true;
		});

		when(request.queryParams("createSession")).thenReturn(createSessionFlag);
		when(tokenService.createTokenString(token)).thenReturn("TokenString");
		when(dataService.addUserSession(eq(1), anyString())).thenReturn(true);

		LoginSuccess loginSuccess = userRequestHandler.handleAddUser(request, response);
		assertThat(loginSuccess.getUserId(), is(1));
		assertThat(loginSuccess.getUsername(), is("Jason"));
		assertThat(loginSuccess.getFirstname(), is("Jason"));
		assertThat(loginSuccess.getToken(), is("TokenString"));

		verify(request, times(1)).body();
		verify(dataService, times(1)).isUsernameTaken("Jason");
		verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
		verify(passwordService, times(1)).encryptPassword("password");
		// verify(dataService, times(1)).addUser(newUser);
		verify(request, times(1)).queryParams("createSession");
		verify(tokenService, times(1)).createTokenString(token);
		verify(dataService, never()).addUserSession(eq(1), anyString());
		verify(response, times(1)).header("location", "/api/user/" + newUser.getUsername() + "/pageinfo");
		verify(response, times(1)).status(201);
		verifyNoMoreInteractions(response);
	}

	@Test
	public void testHandleAddUserFailToAddSession() throws IOException {
		NewUser newUser = createNewUser();

		when(request.body()).thenReturn(createNewUserJson("password", "password"));
		when(passwordService.encryptPassword("password")).thenReturn("hashedPassword");

		when(dataService.addUser(newUser)).then(invocation -> {
			NewUser newUserInvocation = invocation.getArgument(0);
			newUserInvocation.setUserId(1);
			return true;
		});

		when(request.queryParams("createSession")).thenReturn("true");
		when(dataService.addUserSession(eq(1), anyString())).thenReturn(false);

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(DatabaseInsertException.class));
			assertThat(e.getMessage(), is("Cannot create user session"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
			verify(passwordService, times(1)).encryptPassword("password");
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, times(1)).queryParams("createSession");
			verify(dataService, times(1)).addUserSession(eq(1), anyString());
			verify(response, times(1)).header("location", "/api/user/" + newUser.getUsername() + "/pageinfo");
			verify(response, times(1)).status(201);
			verifyNoMoreInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddUserFailToAddUser() throws IOException {
		NewUser newUser = createNewUser();

		when(request.body()).thenReturn(createNewUserJson("password", "password"));
		when(passwordService.encryptPassword("password")).thenReturn("hashedPassword");
		when(dataService.addUser(newUser)).thenReturn(false);

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(DatabaseInsertException.class));
			assertThat(e.getMessage(), is("Cannot create new user"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
			verify(passwordService, times(1)).encryptPassword("password");
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, never()).queryParams("createSession");
			verify(dataService, never()).addUserSession(eq(1), anyString());
			verifyZeroInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddUserUsernameTaken() throws IOException {

		when(request.body()).thenReturn(createNewUserJson("password", "password"));
		when(dataService.isUsernameTaken("Jason")).thenReturn(true);

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Username is taken"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, never()).isEmailTaken("jason_sarwar@jms.com");
			verifyZeroInteractions(passwordService);
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, never()).queryParams("createSession");
			verify(dataService, never()).addUserSession(eq(1), anyString());
			verifyZeroInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddUserEmailTaken() throws IOException {

		when(request.body()).thenReturn(createNewUserJson("password", "password"));
		when(dataService.isEmailTaken("jason_sarwar@jms.com")).thenReturn(true);

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Email is taken"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
			verifyZeroInteractions(passwordService);
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, never()).queryParams("createSession");
			verify(dataService, never()).addUserSession(eq(1), anyString());
			verifyZeroInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddUserPasswordTooShort() throws IOException {

		when(request.body()).thenReturn(createNewUserJson("pass", "pass"));

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Password Too Short"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
			verifyZeroInteractions(passwordService);
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, never()).queryParams("createSession");
			verify(dataService, never()).addUserSession(eq(1), anyString());
			verifyZeroInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddUserPasswordTooLong() throws IOException {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 65; i++) {
			sb.append("a");
		}

		when(request.body()).thenReturn(createNewUserJson(sb.toString(), sb.toString()));

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Password Too Long"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
			verifyZeroInteractions(passwordService);
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, never()).queryParams("createSession");
			verify(dataService, never()).addUserSession(eq(1), anyString());
			verifyZeroInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddUserPasswordsDoNotMatch() throws IOException {

		when(request.body()).thenReturn(createNewUserJson("password", "passwordDoNotMatch"));

		try {
			userRequestHandler.handleAddUser(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Passwords do not match"));

			verify(request, times(1)).body();
			verify(dataService, times(1)).isUsernameTaken("Jason");
			verify(dataService, times(1)).isEmailTaken("jason_sarwar@jms.com");
			verifyZeroInteractions(passwordService);
			// verify(dataService, times(1)).addUser(newUser);
			verify(request, never()).queryParams("createSession");
			verify(dataService, never()).addUserSession(eq(1), anyString());
			verifyZeroInteractions(response);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleEditUserPassword() throws IOException {
		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_PASSWORD).build();
		User user = new User();
		user.setHashedPassword("hashedPassword");

		ChangePassword changePassword = createChangePassword("newPassword", "newPassword");

		when(request.body()).thenReturn(createChangePasswordJson("newPassword", "newPassword"));
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getHashedPasswordByUserId(2)).thenReturn(user);
		when(passwordService.checkPassword(eq(changePassword), eq(user))).thenReturn(true);
		when(passwordService.encryptPassword("newPassword")).thenReturn("newHashedPassword");
		when(dataService.editPassword(2, "newHashedPassword")).thenReturn(true);

		assertThat(userRequestHandler.handleEditUserPassword(request, response), is(true));

		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verify(dataService, times(1)).getHashedPasswordByUserId(2);
		verify(passwordService, times(1)).checkPassword(eq(changePassword), eq(user));
		verify(passwordService, times(1)).encryptPassword("newPassword");
		verifyNoMoreInteractions(passwordService);
		verify(dataService, times(1)).editPassword(2, "newHashedPassword");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditUserPasswordUserDoesNotExist() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_PASSWORD).build();

		when(request.body()).thenReturn(createChangePasswordJson("pass", "pass"));
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);

		try {
			userRequestHandler.handleEditUserPassword(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Incorrect Old Password"));

			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getHashedPasswordByUserId(2);
			verify(passwordService, never()).checkPassword(any(ChangePassword.class), any(User.class));
			verifyNoMoreInteractions(passwordService);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditUserPasswordIncorrectOldPassword() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_PASSWORD).build();
		User user = new User();
		user.setHashedPassword("hashedPassword");

		ChangePassword changePassword = createChangePassword("pass", "pass");

		when(request.body()).thenReturn(createChangePasswordJson("pass", "pass"));
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getHashedPasswordByUserId(2)).thenReturn(user);

		try {
			userRequestHandler.handleEditUserPassword(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Incorrect Old Password"));

			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getHashedPasswordByUserId(2);
			verify(passwordService, times(1)).checkPassword(eq(changePassword), eq(user));
			verifyNoMoreInteractions(passwordService);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditUserPasswordTooShort() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_PASSWORD).build();
		User user = new User();
		user.setHashedPassword("hashedPassword");

		ChangePassword changePassword = createChangePassword("pass", "pass");

		when(request.body()).thenReturn(createChangePasswordJson("pass", "pass"));
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getHashedPasswordByUserId(2)).thenReturn(user);
		when(passwordService.checkPassword(eq(changePassword), eq(user))).thenReturn(true);

		try {
			userRequestHandler.handleEditUserPassword(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("New Password Too Short"));

			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getHashedPasswordByUserId(2);
			verify(passwordService, times(1)).checkPassword(eq(changePassword), eq(user));
			verifyNoMoreInteractions(passwordService);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditUserPasswordTooLong() throws IOException {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 65; i++) {
			sb.append("a");
		}

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_PASSWORD).build();
		User user = new User();
		user.setHashedPassword("hashedPassword");

		ChangePassword changePassword = createChangePassword(sb.toString(), sb.toString());

		when(request.body()).thenReturn(createChangePasswordJson(sb.toString(), sb.toString()));
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getHashedPasswordByUserId(2)).thenReturn(user);
		when(passwordService.checkPassword(eq(changePassword), eq(user))).thenReturn(true);

		try {
			userRequestHandler.handleEditUserPassword(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("New Password Too Long"));

			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getHashedPasswordByUserId(2);
			verify(passwordService, times(1)).checkPassword(eq(changePassword), eq(user));
			verifyNoMoreInteractions(passwordService);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditUserPasswordDoNotMatch() throws IOException {
		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_PASSWORD).build();
		User user = new User();
		user.setHashedPassword("hashedPassword");

		ChangePassword changePassword = createChangePassword("newPassword", "newPasswordDoesNotMatch");

		when(request.body()).thenReturn(createChangePasswordJson("newPassword", "newPasswordDoesNotMatch"));
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getHashedPasswordByUserId(2)).thenReturn(user);
		when(passwordService.checkPassword(eq(changePassword), eq(user))).thenReturn(true);

		try {
			userRequestHandler.handleEditUserPassword(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Passwords do not match"));

			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getHashedPasswordByUserId(2);
			verify(passwordService, times(1)).checkPassword(eq(changePassword), eq(user));
			verifyNoMoreInteractions(passwordService);
			verifyNoMoreInteractions(dataService);
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
		verify(dataService, times(1)).getUserBySessionId(SESSION_KEY);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleSessionRetrievalAdminRights() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.cookie(SESSION_COOKIE)).thenReturn(SESSION_KEY);
		when(dataService.getUserBySessionId(SESSION_KEY)).thenReturn(new User(1, "Username", "Full Name"));
		when(tokenService.createTokenString(token)).thenReturn("TokenString");

		LoginSuccess loginSuccess = userRequestHandler.handleSessionRetrieval(request, response);
		assertThat(loginSuccess.getUserId(), is(1));
		assertThat(loginSuccess.getUsername(), is("Username"));
		assertThat(loginSuccess.getFirstname(), is("Full"));
		assertThat(loginSuccess.getToken(), is("TokenString"));

		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).getUserBySessionId(SESSION_KEY);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleSessionRetrievalRegularRights() throws IOException {

		Token token = Token.newBuilder().setUserId(23).addPermissions(Permission.getRegularPermissions()).build();

		when(request.cookie(SESSION_COOKIE)).thenReturn(SESSION_KEY);
		when(dataService.getUserBySessionId(SESSION_KEY)).thenReturn(new User(23, "Username", "Full Name"));
		when(tokenService.createTokenString(token)).thenReturn("TokenString");

		LoginSuccess loginSuccess = userRequestHandler.handleSessionRetrieval(request, response);
		assertThat(loginSuccess.getUserId(), is(23));
		assertThat(loginSuccess.getUsername(), is("Username"));
		assertThat(loginSuccess.getFirstname(), is("Full"));
		assertThat(loginSuccess.getToken(), is("TokenString"));

		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).getUserBySessionId(SESSION_KEY);
		verifyZeroInteractions(passwordService);
	}

	@Test
	public void testHandleLogin() throws IOException {

		LoginRequest loginRequest = new LoginRequest("Jason", "Password");
		User user = new User(3, "Jason", "Jason Sarwar", "HashedPassword");
		Token token = Token.newBuilder().setUserId(3).addPermissions(Permission.getRegularPermissions()).build();

		when(request.body()).thenReturn(LOGIN_REQUEST_JSON);
		when(dataService.getUserLoginInfoByString("Jason")).thenReturn(user);
		when(passwordService.checkPassword(loginRequest, user)).thenReturn(true);
		when(tokenService.createTokenString(token)).thenReturn("SecretToken");

		LoginSuccess loginSuccess = userRequestHandler.handleLogin(request, response);
		assertThat(loginSuccess.getUserId(), is(3));
		assertThat(loginSuccess.getUsername(), is("Jason"));
		assertThat(loginSuccess.getFirstname(), is("Jason"));
		assertThat(loginSuccess.getToken(), is("SecretToken"));
	}

	@Test
	public void testHandleLogout() {
		when(request.cookie(SESSION_COOKIE)).thenReturn("cookie");
		userRequestHandler.handleLogout(request, response);
		verify(request, times(1)).cookie(SESSION_COOKIE);
		verify(dataService, times(1)).removeSessionId("cookie");
		verify(response, times(1)).removeCookie(SESSION_COOKIE);
	}

	private static Collection<User> createUsersToFollow(int no) {
		Collection<User> users = new HashSet<>(no);
		for (int i = 0; i < no; i++) {
			users.add(new User(no, "User" + no));
		}
		return users;
	}

	private NewUser createNewUser() {
		NewUser newUser = new NewUser();
		newUser.setUsername("Jason");
		newUser.setFullName("Jason M Sarwar");
		newUser.setEmail("jason_sarwar@jms.com");
		newUser.setHashedPassword("hashedPassword");
		newUser.setBirthDate(LocalDate.of(2018, 3, 19));
		return newUser;
	}

	private String createNewUserJson(String password1, String password2) {
		return "{\"username\":\"Jason\"," + "\"fullName\":\"Jason M Sarwar\"," + "\"email\":\"jason_sarwar@jms.com\","
				+ "\"password1\":\"" + password1 + "\"," + "\"password2\":\"" + password2 + "\",\"birthDate\":"
				+ "{\"month\":3, \"day\":19, \"year\":2018}}";
	}

	private ChangePassword createChangePassword(String newPassword1, String newPassword2) {
		ChangePassword changePassword = new ChangePassword();
		changePassword.setUserId(2);
		changePassword.setOldPassword("oldPassword");
		changePassword.setNewPassword1(newPassword1);
		changePassword.setNewPassword2(newPassword2);
		return changePassword;
	}

	private String createChangePasswordJson(String newPassword1, String newPassword2) {
		return "{\"userId\":2, \"oldPassword\":\"oldPassword\", \"newPassword1\":\"" + newPassword1
				+ "\", \"newPassword2\":\"" + newPassword2 + "\"}";
	}
}
