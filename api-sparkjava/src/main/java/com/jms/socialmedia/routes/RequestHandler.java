package com.jms.socialmedia.routes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.FailedLoginAttemptException;
import com.jms.socialmedia.exception.InvalidUserLoginStateException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.exception.UnauthorizedException;
import com.jms.socialmedia.exception.UnsupportedContentTypeException;
import com.jms.socialmedia.jwt.JWTService;
import com.jms.socialmedia.model.ChangePassword;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Entry;
import com.jms.socialmedia.model.FollowRequest;
import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.LoginSuccess;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserLogin;
import com.jms.socialmedia.password.PasswordService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

import static java.util.stream.Collectors.toSet;

public final class RequestHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
	private static final String SESSION_COOKIE = "my-social-media-session";
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	
	private final DataService dataService;
	private final PasswordService passwordService;
	private final JWTService jwtService;
	private final Gson gson;
	
	public RequestHandler(DataService dataService, PasswordService passwordService) {
		this.dataService = dataService;
		this.passwordService = passwordService;
		jwtService = new JWTService();
		gson = new GsonBuilder().create();
	}

	public String handlePing(Request request, Response response) {
		return "pong";
	}
	
	public Object handleGetUser(Request request, Response response) {
		return null;
	}
	
	public Object handleGetUsers(Request request, Response response) {
		return null;
	}

	public Collection<Post> handleGetPosts(Request request, Response response) {

		String userIdStr = request.queryParams("userId");
		Collection<Integer> userIds = userIdStr == null ? null : Arrays.stream(userIdStr.split("[,|]")).map(Integer::parseInt).collect(toSet());
		String username = request.queryParams("username");
		String tag = request.queryParams("tag");
		String onDate = request.queryParams("on");
		String beforeDate = request.queryParams("before");
		String afterDate = request.queryParams("after");
		return dataService.getPosts(userIds, username, tag, onDate, beforeDate, afterDate);
	}

	public Post handleGetPost(Request request, Response response) {
		
		int postId = Integer.parseInt(request.params(":id"));
		Post post = dataService.getPost(postId);

		if(post != null) {
			return post;
		} else {
			throw new NotFoundException("Post Not Found");
		}
	}

	public FullPost handleGetPostWithComments(Request request, Response response) {
		
		int postId = Integer.parseInt(request.params(":id"));
		FullPost post = dataService.getPostWithComments(postId);
		
		if(post != null) {
			return post;
		} else {
			throw new NotFoundException("Post Not Found");
		}
	}

	public Boolean handleAddPost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		Post newPost = extractBodyContent(request, Post.class);
		validateAddEntryRequest(newPost);
		authorizeRequest(request, newPost.getUserId(), "Add Post");
		return dataService.addPost(newPost);
	}

	public Boolean handleEditPost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int postId = Integer.parseInt(request.params(":id"));
		Entry body = extractBodyContent(request, Post.class);
		authorizeRequest(request, dataService.getUserIdFromPostId(postId), "Edit Post");
		if (!dataService.editPost(postId, body.getText())) {
			throw new NotFoundException("Post Not Found");
		}
		return true;
	}

	public Boolean handleDeletePost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int postId = Integer.parseInt(request.params(":id"));
		authorizeRequest(request, dataService.getUserIdFromPostId(postId), "Delete Post");
		if (!dataService.deletePost(postId)) {
			throw new NotFoundException("Post Not Found");
		}
		return true;
	}

	public Collection<Integer> handleGetPostLikes(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int postId = Integer.parseInt(request.params(":id"));
		return dataService.getPostLikes(postId);
	}
	
	public Boolean handleLikePost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int postId = Integer.parseInt(request.params(":postid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Like Post");
		return dataService.likePost(postId, userId);
	}

	public Boolean handleUnlikePost(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int postId = Integer.parseInt(request.params(":postid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Unlike Post");
		return dataService.unlikePost(postId, userId);
	}

	public Collection<Comment> handleGetComments(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int postId = Integer.parseInt(request.params(":id"));
		return dataService.getComments(postId);
	}
	
	public Comment handleGetComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		String strCommentId = request.params(":id");
		int commentId = Integer.parseInt(strCommentId);
		Comment comment = dataService.getComment(commentId);
		if(comment != null) {
			return comment;
		} else {
			throw new NotFoundException("Comment Not Found");
		}
	}

	public Boolean handleAddComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		Comment newComment = extractBodyContent(request, Comment.class);
		String strPostId = request.params(":id");
		if (strPostId != null) {
			int postId = Integer.parseInt(strPostId);
			newComment.setPostId(postId);
		}
		validateAddEntryRequest(newComment);
		authorizeRequest(request, newComment.getUserId(), "Add Comment");
		return dataService.addComment(newComment);
	}

	public Boolean handleEditComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		String strCommentId = request.params(":id");
		int commentId = Integer.parseInt(strCommentId);
		Entry body = extractBodyContent(request, Comment.class);
		authorizeRequest(request, dataService.getUserIdFromCommentId(commentId), "Edit Comment");
		if (!dataService.editComment(commentId, body.getText())) {
			throw new NotFoundException("Comment Not Found");
		}
		return true;
	}

	public Boolean handleDeleteComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		String strCommentId = request.params(":id");
		int commentId = Integer.parseInt(strCommentId);
		authorizeRequest(request, dataService.getUserIdFromCommentId(commentId), "Edit Comment");
		if (!dataService.deleteComment(commentId)) {
			throw new NotFoundException("Comment Not Found");
		}
		return true;
	}

	public Collection<Integer> handleGetCommentLikes(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int commentId = Integer.parseInt(request.params(":id"));
		return dataService.getCommentLikes(commentId);
	}
	
	public Boolean handleLikeComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int commentId = Integer.parseInt(request.params(":commentid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Like Comment");
		return dataService.likeComment(commentId, userId);
	}

	public Boolean handleUnlikeComment(Request request, Response response) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {

		int commentId = Integer.parseInt(request.params(":commentid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Unlike Comment");
		return dataService.unlikeComment(commentId, userId);
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
		
		dataService.editPassword(changePassword.getUserId(), 
				passwordService.encryptPassword(changePassword.getNewPassword()));
		return true;
	}
	
	public Boolean handleFollowUser(Request request, Response response) throws IOException {
		FollowRequest followRequest = extractBodyContent(request, FollowRequest.class);
		authorizeRequest(request, followRequest.getFollowerUserId(), "Follow User");
		return dataService.followUser(followRequest.getFollowerUserId(), followRequest.getFollowingUserId());
	}
	
	public Boolean handleUnfollowUser(Request request, Response response) throws IOException {
		FollowRequest followRequest = extractBodyContent(request, FollowRequest.class);
		authorizeRequest(request, followRequest.getFollowerUserId(), "Unollow User");
		return dataService.unfollowUser(followRequest.getFollowerUserId(), followRequest.getFollowingUserId());
	}

	public Collection<Post> handleGetFollowersPosts(Request request, Response response) {

		Integer userId = Integer.parseInt(request.params("userid"));
		Collection<Integer> followingUserIds = dataService.getFollowingUserIds(userId);
		return dataService.getPosts(followingUserIds, null, null, null, null, null);
	}

	public LoginSuccess handleSessionRetrieval(Request request, Response response) throws IOException {
		
		if (StringUtils.isNotBlank(request.cookie(SESSION_COOKIE))) {
			
			String sessionKey = request.cookie(SESSION_COOKIE);
			User user = dataService.getUserBySessionKey(sessionKey);
			
			if (user != null) {
				LoginSuccess loginSuccess = new LoginSuccess();
				loginSuccess.setUserId(user.getUserId());
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
		
		UserLogin userLogin = extractBodyContent(request, UserLogin.class);

		User user = dataService.getUserLoginInfoByName(userLogin.getUser());

		if (user == null) {
			throw new FailedLoginAttemptException("Incorrect Username or Password");
		}

		if (!passwordService.checkPassword(userLogin, user)) {
			throw new FailedLoginAttemptException("Incorrect Username or Password");
		}

		String sessionKey = UUID.randomUUID().toString();
		if (!dataService.addUserSession(user.getUserId(), sessionKey)) {
			throw new InvalidUserLoginStateException("Cannot create user session");
		}
		response.cookie(SESSION_COOKIE, sessionKey);
		LoginSuccess loginSuccess = new LoginSuccess();
		loginSuccess.setUserId(user.getUserId());
		loginSuccess.setToken(jwtService.createJWT(user.getUserId()));
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
	
	private void authorizeRequest(Request request, Integer userIdFromRequest, String action) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		String auth = request.headers(AUTHORIZATION);
		if (StringUtils.isBlank(auth) || auth.length() < BEARER.length()) {
			throw new UnauthorizedException("Not authorized to " + action);
		} else {
			String jwt = auth.substring(BEARER.length());
			Integer userIdFromJWT = jwtService.validateJWTAndRetrieveUserId(jwt);
			if (!userIdFromRequest.equals(userIdFromJWT)) {
				throw new UnauthorizedException("User not authorized to " + action);
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
