package com.mytwitter.routes;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.exception.BadRequestException;
import com.mytwitter.exception.FailedLoginAttemptException;
import com.mytwitter.exception.PostNotFoundException;
import com.mytwitter.exception.UnauthorizedException;
import com.mytwitter.exception.UnsupportedContentTypeException;
import com.mytwitter.jwt.JWTService;
import com.mytwitter.exception.InvalidUserLoginStateException;
import com.mytwitter.model.Comment;
import com.mytwitter.model.Entry;
import com.mytwitter.model.FullPost;
import com.mytwitter.model.LoginSuccess;
import com.mytwitter.model.Post;
import com.mytwitter.model.StandardResponse;
import com.mytwitter.model.User;
import com.mytwitter.model.UserLogin;
import com.mytwitter.password.NonEncryptionPasswordService;
import com.mytwitter.password.PasswordService;

import com.mytwitter.dataservice.DataService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public final class RequestHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
	private static final String SESSION_COOKIE = "my-social-media-session";
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	
	private final DataService dataService;
	private final PasswordService passwordService;
	private final JWTService jwtService;
	private final Gson gson;
	
	public RequestHandler(DataService dataService) {
		this.dataService = dataService;
		passwordService = new NonEncryptionPasswordService();
		jwtService = new JWTService();
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

	public Collection<Post> handleGetPosts(Request request, Response response) {
		response.type("application/json");
		String userIdStr = request.queryParams("userId");
		Integer userId = userIdStr == null ? null : Integer.parseInt(userIdStr);
		String username = request.queryParams("username");
		String tag = request.queryParams("tag");
		String onDate = request.queryParams("on");
		String beforeDate = request.queryParams("before");
		String afterDate = request.queryParams("after");
		return dataService.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
	}

	public FullPost handleGetPost(Request request, Response response) {
		
		String strPostId = request.params(":id");
		int postId = Integer.parseInt(strPostId);
		long start = System.currentTimeMillis();
		FullPost post = dataService.getPost(postId);
		long stop = System.currentTimeMillis();
		LOGGER.info("Post Retrieved in {} ms", stop - start);
		
		if(post != null) {
			response.type("application/json");
			return post;
		} else {
			throw new PostNotFoundException("Post Not Found");
		}
	}

	public Boolean handleAddPost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		Post newPost = extractBodyContent(request, Post.class);
		validateAddEntryRequest(newPost);
		authorizeRequest(request, newPost.getUserId());
		return dataService.addPost(newPost);
	}

	public Boolean handleEditPost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		String strPostId = request.params(":id");
		int postId = Integer.parseInt(strPostId);
		Post edittedPost = extractBodyContent(request, Post.class);
		authorizeRequest(request, edittedPost.getUserId());
		return dataService.editPost(postId, edittedPost.getText());
	}

	public Boolean handleDeletePost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		String strPostId = request.params(":id");
		int postId = Integer.parseInt(strPostId);
		Post deletedPost = extractBodyContent(request, Post.class);
		authorizeRequest(request, deletedPost.getUserId());
		return dataService.deletePost(postId);
	}

	public Boolean handleAddComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		Comment newComment = extractBodyContent(request, Comment.class);
		validateAddEntryRequest(newComment);
		authorizeRequest(request, newComment.getUserId());
		return dataService.addComment(newComment);
	}
	
	public LoginSuccess handleSessionRetrieval(Request request, Response response) throws IOException {
		
		if (StringUtils.isNotBlank(request.cookie(SESSION_COOKIE))) {
			
			String sessionKey = request.cookie(SESSION_COOKIE);
			User user = dataService.getUserBySessionKey(sessionKey);
			
			if (user != null) {
				LoginSuccess loginSuccess = new LoginSuccess();
				loginSuccess.setUserId(user.getUserId());
				loginSuccess.setJwt(jwtService.createJWT(user.getUserId()));
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
			//System.err.println("Cookie: " + request.cookie(SESSION_COOKIE));
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
		if (!dataService.addUserSession(user.getUserId(), sessionKey)) {
			throw new InvalidUserLoginStateException("Cannot create user session");
		}
		response.cookie(SESSION_COOKIE, sessionKey);
		LoginSuccess loginSuccess = new LoginSuccess();
		loginSuccess.setUserId(user.getUserId());
		loginSuccess.setJwt(jwtService.createJWT(user.getUserId()));
		loginSuccess.setFirstname(user.getFullName().split(" ")[0]);
		return loginSuccess;

	}
	
	public Object handleLogout(Request request, Response response) throws IOException {

		if (request.cookie(SESSION_COOKIE) == null) {
			throw new InvalidUserLoginStateException("A User is not logged in");
		}
		dataService.removeSessionKey(request.cookie(SESSION_COOKIE));
		response.removeCookie(SESSION_COOKIE);
		return "ok";
	}
	
	private void authorizeRequest(Request request, Integer userIdFromRequest) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		String auth = request.headers(AUTHORIZATION);
		if (StringUtils.isBlank(auth) || auth.length() < BEARER.length()) {
			throw new UnauthorizedException("Not authorized to Post");
		} else {
			String jwt = auth.substring(BEARER.length());
			Integer userIdFromJWT = jwtService.validateJWTAndRetrieveUserId(jwt);
			if (!userIdFromRequest.equals(userIdFromJWT)) {
				throw new UnauthorizedException("User ID Mismatch");
			}
		}
	}
	
	private <T> T extractBodyContent(Request request, Class<T> aClass) throws IOException {

		if (request.contentType().toLowerCase().startsWith("application/json") 
				|| StringUtils.isBlank(request.contentType())) {
			return gson.fromJson(request.body(), aClass);
		} else if (request.contentType().toLowerCase().startsWith("application/xml")) {
			return null;
		} else if (request.contentType().toLowerCase().startsWith("application/x-protobuf")) {
			return null;
		} else {
			throw new UnsupportedContentTypeException("Unsupported Content Type");
		}
	}
	
	private void validateAddEntryRequest(Entry entry) {
		StringBuilder sb = new StringBuilder();
		if (!entry.hasUserId()) {
			sb.append("Add ").append(entry.getClass().getSimpleName()).append(" Request requires a 'userId'\n");
		}
		if (entry instanceof Comment && !entry.hasPostId()) {
			sb.append("Add ").append(entry.getClass().getSimpleName()).append(" Request requires a 'postId'\n");
		}
		if (!entry.hasText()) {
			sb.append("Add ").append(entry.getClass().getSimpleName()).append(" Request requires 'text'");
		}
		if (sb.length() > 0) {
			throw new BadRequestException(sb.toString());
		}
	}
}
