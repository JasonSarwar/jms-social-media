package com.mytwitter.routes;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.exception.BadRequestException;
import com.mytwitter.exception.FailedLoginAttemptException;
import com.mytwitter.exception.PostNotFoundException;
import com.mytwitter.exception.UnauthorizedPostException;
import com.mytwitter.exception.UnsupportedContentTypeException;
import com.mytwitter.exception.InvalidUserLoginStateException;
import com.mytwitter.model.LoginSuccess;
import com.mytwitter.model.Post;
import com.mytwitter.model.StandardResponse;
import com.mytwitter.model.StandardResponse.Status;
import com.mytwitter.model.User;
import com.mytwitter.model.UserLogin;
import com.mytwitter.password.NonEncryptionPasswordService;
import com.mytwitter.password.PasswordService;
import com.mytwitter.utils.JWTUtils;

import dataservice.DataService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class RequestHandler {

	private static final String SESSION_COOKIE = "my-twitter-session";
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer: ";
	
	private final DataService dataService;
	private final PasswordService passwordService;
	private final Gson gson;
	
	public RequestHandler(DataService dataService) {
		this.dataService = dataService;
		passwordService = new NonEncryptionPasswordService();
		gson = new GsonBuilder().create();
	}

	public String handlePing(Request request, Response response) {
		return "pong";
	}
	
	public StandardResponse handleGetUser(Request request, Response response) {
		StandardResponse standardResponse = new StandardResponse();
		
		return standardResponse;
	}
	
	public StandardResponse handleGetUsers(Request request, Response response) {
		StandardResponse standardResponse = new StandardResponse();
		
		return standardResponse;
	}

	public Post handleGetPost(Request request, Response response) {
		
		String strPostId = request.params(":id");
		try {
			int postId = Integer.parseInt(strPostId);
			Post post = dataService.getPost(postId);
			if(post != null) {
				response.type("application/json");
				return post;
			} else {
				throw new PostNotFoundException("Post Not Found");
			}
		} catch (NumberFormatException e) {
			throw new BadRequestException("Invalid Post ID");
		}
		
	}
	
	public Collection<Post> handleGetPosts(Request request, Response response) {
		response.type("application/json");
		String username = request.queryParams("username");
		String tag = request.queryParams("tag");
		String onDate = request.queryParams("on");
		String beforeDate = request.queryParams("before");
		String afterDate = request.queryParams("after");
		
		return dataService.getPosts(0, username, tag, onDate, beforeDate, afterDate);
	}
	
	public StandardResponse handleAddPost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		Post newPost = extractBodyContent(request, Post.class);
		newPost.validate();
		
		if (StringUtils.isNotBlank(request.cookie(SESSION_COOKIE))) {
			String sessionKey = request.cookie(SESSION_COOKIE);
			Integer userId = dataService.getUserIdBySessionKey(sessionKey);
			if (!newPost.getUserId().equals(userId)) {
				throw new UnauthorizedPostException("User ID Mismatch");
			}
		}

		String auth = request.headers(AUTHORIZATION);
		if (StringUtils.isBlank(auth) || auth.length() < BEARER.length()) {
			throw new UnauthorizedPostException("Not authorized to Post");
		} else {
			String jwt = auth.substring(BEARER.length());
			Integer userId = JWTUtils.validateJWTAndRetrieveUserId(jwt);
			if (!newPost.getUserId().equals(userId)) {
				throw new UnauthorizedPostException("User ID Mismatch");
			}
		}

		StandardResponse standardResponse = new StandardResponse();
		boolean success = dataService.addPost(newPost);
		standardResponse.setStatus(success ? Status.SUCCESS : Status.FAILURE);
		return standardResponse;
	}
	
	public LoginSuccess handleSessionRetrieval(Request request, Response response) throws IOException {
		
		if (StringUtils.isNotBlank(request.cookie(SESSION_COOKIE))) {
			
			String sessionKey = request.cookie(SESSION_COOKIE);
			Integer userId = dataService.getUserIdBySessionKey(sessionKey);
			
			if (userId != null) {
				LoginSuccess loginSuccess = new LoginSuccess();
				loginSuccess.setUserId(userId);
				loginSuccess.setJwt(JWTUtils.createJWT(userId));
				//loginSuccess.setFirstname(user.getFullName().split(" ")[0]);
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
			//throw new InvalidUserLoginStateException("A User is already logged in");
		}
		
		UserLogin userLogin = extractBodyContent(request, UserLogin.class);

		User user = dataService.getUserLoginInfo(userLogin.getUser());

		if (user == null) {
			throw new FailedLoginAttemptException("Incorrect Username or Password");
		}
		
		String hashedPasswordFromLogin = passwordService.encryptPassword(userLogin.getPassword());
		if (!user.getHashedPassword().equals(hashedPasswordFromLogin)) {
			throw new FailedLoginAttemptException("Incorrect Username or Password");
		}
		
		String sessionKey = UUID.randomUUID().toString();
		if (dataService.addUserSession(user.getUserId(), sessionKey) != 1) {
			throw new InvalidUserLoginStateException("Cannot create user session");
		}
		
		response.cookie(SESSION_COOKIE, sessionKey);
		
		LoginSuccess loginSuccess = new LoginSuccess();
		loginSuccess.setUserId(user.getUserId());
		loginSuccess.setJwt(JWTUtils.createJWT(user.getUserId()));
		loginSuccess.setFirstname(user.getFullName().split(" ")[0]);
		return loginSuccess;

	}
	
	public Object handleLogout(Request request, Response response) throws IOException {

		if (request.cookie(SESSION_COOKIE) == null) {
			throw new InvalidUserLoginStateException("A User is not logged in");
		}
		response.removeCookie(SESSION_COOKIE);
		return null;
	}
	
	private <T> T extractBodyContent(Request request, Class<T> aClass) {

		if (request.contentType().startsWith("application/json") 
				|| StringUtils.isBlank(request.contentType())) {
			return gson.fromJson(request.body(), aClass);
		} else if (request.contentType().startsWith("application/xml")) {
			return null;
		} else if (request.contentType().startsWith("application/x-protobuf")) {
			return null;
		} else {
			throw new UnsupportedContentTypeException("Unsupported Content Type");
		}
	}
}
