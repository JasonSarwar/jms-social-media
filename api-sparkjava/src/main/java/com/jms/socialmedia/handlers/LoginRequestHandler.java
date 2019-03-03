package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.UUID;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.FailedLoginAttemptException;
import com.jms.socialmedia.exception.InvalidUserLoginStateException;
import com.jms.socialmedia.model.LoginRequest;
import com.jms.socialmedia.model.LoginSuccess;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.password.PasswordService;

import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class LoginRequestHandler extends RequestHandler {

	private static final String SESSION_COOKIE = "my-social-media-session";

	private final PasswordService passwordService;
	
	public LoginRequestHandler(DataService dataService, PasswordService passwordService, Gson gson) {
		super(dataService, gson);
		this.passwordService = passwordService;
	}

	public LoginSuccess handleSessionRetrieval(Request request, Response response) throws IOException {
		
		if (StringUtils.isNotBlank(request.cookie(SESSION_COOKIE))) {
			
			String sessionKey = request.cookie(SESSION_COOKIE);
			User user = dataService.getUserBySessionKey(sessionKey);
			
			if (user != null) {
				LoginSuccess loginSuccess = new LoginSuccess();
				loginSuccess.setUserId(user.getUserId());
				loginSuccess.setUsername(user.getUsername());
				loginSuccess.setToken(jwtService.createJWT(user.getUserId()));
				loginSuccess.setFirstname(user.getFullName().split(" ")[0]);
				return loginSuccess;
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

		String sessionKey = UUID.randomUUID().toString();
		if (!dataService.addUserSession(user.getUserId(), sessionKey)) {
			throw new InvalidUserLoginStateException("Cannot create user session");
		}
		response.cookie(SESSION_COOKIE, sessionKey);
		LoginSuccess loginSuccess = new LoginSuccess();
		loginSuccess.setUserId(user.getUserId());
		loginSuccess.setUsername(user.getUsername());
		loginSuccess.setToken(jwtService.createJWT(user.getUserId()));
		loginSuccess.setFirstname(user.getFullName().split(" ")[0]);
		return loginSuccess;

	}
	
	public Object handleLogout(Request request, Response response) throws IOException {

		dataService.removeSessionKey(request.cookie(SESSION_COOKIE));
		response.removeCookie(SESSION_COOKIE);
		return "ok";
	}
}
