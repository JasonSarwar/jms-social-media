package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.DatabaseInsertException;
import com.jms.socialmedia.exception.FailedLoginAttemptException;
import com.jms.socialmedia.exception.InvalidUserLoginStateException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.ChangePassword;
import com.jms.socialmedia.model.LoginRequest;
import com.jms.socialmedia.model.LoginSuccess;
import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;
import com.jms.socialmedia.password.PasswordService;

import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class UserRequestHandler extends RequestHandler {

	private static final String SESSION_COOKIE = "my-social-media-session";

	private final PasswordService passwordService;
	
	public UserRequestHandler(DataService dataService, PasswordService passwordService, Gson gson) {
		super(dataService, gson);
		this.passwordService = passwordService;
	}

	public UserPage handleGetUserPage(Request request, Response response) {
		String username = request.params("username");
		UserPage userPage = dataService.getUserPageInfoByName(username);
		if (userPage != null) {
			int userId = userPage.getUserId();
			userPage.addFollowersUserIds(dataService.getFollowerUserIds(userId));
			userPage.addFollowingUserIds(dataService.getFollowingUserIds(userId));
			return userPage;
		} else {
			throw new NotFoundException("User not found");
		}
	}
	
	public Collection<User> handleGetUsernamesAndIds(Request request, Response response) throws IOException {
		String queryParam = request.queryParams("ids");
		if (StringUtils.isBlank(queryParam)) {
			throw new BadRequestException("No User IDs included");
		}
		Collection<Integer> userIds = Arrays.stream(queryParam.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		return dataService.getUsernamesByIds(userIds);
	}

	public Boolean handleGetIsUsernameTaken(Request request, Response response) throws IOException {
		return dataService.isUsernameTaken(request.params(":username"));
	}

	public Boolean handleGetIsEmailTaken(Request request, Response response) throws IOException {
		return dataService.isEmailTaken(request.params(":email"));
	}

	public LoginSuccess handleAddUser(Request request, Response response) throws IOException {
		NewUser newUser = extractBodyContent(request, NewUser.class);

		if (!newUser.passwordsMatch()) {
			throw new BadRequestException("Passwords do not match");
		}

		if (dataService.isUsernameTaken(newUser.getUsername())) {
			throw new BadRequestException("Username is taken");
		}

		if (dataService.isEmailTaken(newUser.getEmail())) {
			throw new BadRequestException("Email is taken");
		}

		createHashedPassword(newUser);
		if (!dataService.addUser(newUser)) {
			throw new DatabaseInsertException("Cannot create new user");
		}

		createSession(response, newUser);
		return createLoginSuccess(newUser);
	}

	public Boolean handleEditUserPassword(Request request, Response response) throws IOException {
		ChangePassword changePassword = extractBodyContent(request, ChangePassword.class);
		
		authorizeRequest(request, changePassword.getUserId(), "Edit Password");
		
		User user = dataService.getHashedPasswordByUserId(changePassword.getUserId());
		if (!passwordService.checkPassword(changePassword, user)) {
			throw new BadRequestException("Incorrect Old Password");
		}

		int len = changePassword.getNewPassword().length();
		if (len < 5) {
			throw new BadRequestException("New Password Too Short");
		}
		if (len > 50) {
			throw new BadRequestException("New Password Too Long");
		}
		
		return dataService.editPassword(changePassword.getUserId(), 
				passwordService.encryptPassword(changePassword.getNewPassword()));
	}
	
	public LoginSuccess handleSessionRetrieval(Request request, Response response) throws IOException {
		
		if (StringUtils.isNotBlank(request.cookie(SESSION_COOKIE))) {
			
			String sessionKey = request.cookie(SESSION_COOKIE);
			User user = dataService.getUserBySessionKey(sessionKey);
			
			if (user != null) {
				return createLoginSuccess(user);
			} else {
				return null;
			}
			
		} else {
			return null;
		}
	}
	
	public LoginSuccess handleLogin(Request request, Response response) throws IOException {

		if (request.cookie(SESSION_COOKIE) != null) {
			throw new InvalidUserLoginStateException("A User is already logged in");
		}
		
		LoginRequest loginRequest = extractBodyContent(request, LoginRequest.class);

		User user = dataService.getUserLoginInfoByString(loginRequest.getUser());

		if (user == null) {
			throw new FailedLoginAttemptException("Incorrect Username or Password");
		}

		if (!passwordService.checkPassword(loginRequest, user)) {
			throw new FailedLoginAttemptException("Incorrect Username or Password");
		}

		createSession(response, user);
		return createLoginSuccess(user);

	}
	
	public Object handleLogout(Request request, Response response) throws IOException {

		dataService.removeSessionKey(request.cookie(SESSION_COOKIE));
		response.removeCookie(SESSION_COOKIE);
		return "ok";
	}

	private void createSession(Response response, User user) {
		String sessionKey = UUID.randomUUID().toString();
		if (!dataService.addUserSession(user.getUserId(), sessionKey)) {
			throw new DatabaseInsertException("Cannot create user session");
		}
		response.cookie(SESSION_COOKIE, sessionKey);
	}

	private LoginSuccess createLoginSuccess(User user) throws IOException {
		LoginSuccess loginSuccess = new LoginSuccess();
		loginSuccess.setUserId(user.getUserId());
		loginSuccess.setUsername(user.getUsername());
		loginSuccess.setToken(jwtService.createJWT(user.getUserId()));
		loginSuccess.setFirstname(user.getFullName().split(" ")[0]);
		return loginSuccess;
	}
	
	private void createHashedPassword(NewUser newUser) {
		String hashedPassword = passwordService.encryptPassword(newUser.getPassword1());
		newUser.setPassword1(null);
		newUser.setPassword2(null);
		newUser.setHashedPassword(hashedPassword);
	}
}
