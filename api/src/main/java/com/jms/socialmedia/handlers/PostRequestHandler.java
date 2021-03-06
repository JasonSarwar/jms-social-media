package com.jms.socialmedia.handlers;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.eclipse.jetty.util.StringUtil;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.Entry;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

public class PostRequestHandler extends RequestHandler {

	private static final String NOT_FOUND_MESSAGE = "Post Not Found";
	private static final String USER_ID_PARAM = "userId";
	private static final String USERNAME_PARAM = "username";
	private static final String POST_ID_PARAM = "postId";
	private static final String SINCE_POST_ID_PARAM = "sincePostId";

	public PostRequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
		super(dataService, tokenService, gson);
	}

	/**
	 * <h1> GET /api/posts </h1>
	 * Query Parameters
	 * <ul>
	 * 	<li> userId - ID of User who made the Posts </li>
	 * 	<li> username - login name of the User who made the Posts </li>
	 * 	<li> tag - #tag in the Posts </li>
	 * 	<li> on - Date the Posts were created </li>
	 * 	<li> before - latest date of the Posts returned </li>
	 * 	<li> after - earliest date of the Posts returned </li>
	 * 	<li> sortBy - Field to Sort by </li>
	 * 	<li> order - asc or desc </li>
	 * 	<li> sincePostId - return posts after this Post ID </li>
	 * </ul>
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 				Collection of {@link Post}s according to the query parameters
	 */
	public Collection<Post> handleGetPosts(Request request, Response response) {

		String userIdsParam = request.queryParams(USER_ID_PARAM);
		Collection<Integer> userIds = userIdsParam == null ? null : Arrays.stream(userIdsParam.split("[,|]")).map(Integer::parseInt).collect(toSet());
		String usernamesParam = request.queryParams(USERNAME_PARAM);
		Collection<String> usernames = usernamesParam == null ? null : Arrays.stream(usernamesParam.split("[,|]")).collect(toSet());
		String tag = request.queryParams("tag");
		String onDate = request.queryParams("on");
		String beforeDate = request.queryParams("before");
		String afterDate = request.queryParams("after");
		String strSincePostId = request.queryParams(SINCE_POST_ID_PARAM);
		Integer sincePostId = strSincePostId == null ? null : Integer.parseInt(strSincePostId);
		String sortBy = Optional.ofNullable(request.queryParams("sortBy")).orElse(POST_ID_PARAM);
		String order = request.queryParams("order");
		boolean sortOrderAsc = StringUtil.isNotBlank(order) && order.equalsIgnoreCase("asc");

		return dataService.getPosts(userIds, usernames, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc);
	}

	/**
	 * <h1> GET /api/user/:username/feed </h1>
	 * 
	 * <ul>
	 * 	<li> username - Name of User </li>
	 * </ul>
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 				Collection of {@link Post}s
	 */
	public Collection<Post> handleGetFeedPosts(Request request, Response response) {

		String username = request.params(USERNAME_PARAM);
		String sincePostIdParam = request.queryParams(SINCE_POST_ID_PARAM);
		Integer sincePostId = sincePostIdParam == null ? null : Integer.parseInt(sincePostIdParam);
		
		Collection<String> usernames = new HashSet<>(dataService.getFollowingUsernames(username));
		usernames.add(username);
		return dataService.getPosts(null, usernames, null, null, null, null, sincePostId, POST_ID_PARAM, false);
	}

	/**
	 * <h1> GET /api/user/:userId/posts </h1>
	 * 
	 * :userId - ID of User who made the Posts
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 				Collection of {@link Post}s made by the User
	 * 
	 * @throws NumberFormatException	if given userId is not a number
	 */
	public Collection<Post> handleGetPostsByUserId(Request request, Response response) {

		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		return dataService.getPosts(userId);
	}

	/**
	 * <h1> GET /api/post/:id </h1>
	 * 
	 * :id - ID of Post
	 * 
	 * @param request				Spark Request
	 * @param response				Spark Response
	 * @return 						a single {@link Post}
	 * 
	 * @throws NotFoundException		if a Post with the given ID does not exist
	 * @throws NumberFormatException	if given post id is not a number
	 */
	public Post handleGetPost(Request request, Response response) {
		
		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		Post post = dataService.getPost(postId);

		if(post != null) {
			return post;
		} else {
			throw new NotFoundException(NOT_FOUND_MESSAGE);
		}
	}

	/**
	 * <h1> POST /api/post/add </h1>
	 * 
	 * <pre>
	 * {
	 *   "userId" [ID of User making the Post],
	 *   "text" [Post Text]
	 * }
	 * </pre>
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 				{@code true} if Add is successful
	 * 
	 * @throws IOException			if there is a JSON Parsing Issue or a JWT Validation Failure
	 * @throws BadRequestException 	if the request does not have a userId or text
	 */
	public Boolean handleAddPost(Request request, Response response) throws IOException {
		
		Post newPost = extractBodyContent(request, Post.class);
		validateAddPostRequest(newPost);
		authorizeRequest(request, newPost.getUserId(), Permission.ADD_POST);
		
		if (dataService.addPost(newPost)) {
			response.status(201);
			response.header("location", "/api/post/" + newPost.getPostId());
			return true;
		}
		return false;
	}

	/**
	 * <h1> PUT /api/post/:id </h1>
	 * 
	 * :id - ID of Post being edited
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return				{@code true} if Update is successful
	 * 
	 * @throws IOException			if there is a JSON Parsing Issue or a JWT Validation Failure
	 * @throws NotFoundException	if a Post with the given ID does not exist
	 */
	public Boolean handleEditPost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		Entry body = extractBodyContent(request, Post.class);
		validateEditPostRequest(body);
		
		authorizeRequest(request, dataService.getUserIdFromPostId(postId), Permission.EDIT_POST);
		if (!dataService.editPost(postId, body.getText())) {
			throw new NotFoundException(NOT_FOUND_MESSAGE);
		}
		return true;
	}

	/**
	 * <h1> DELETE /api/post/:id </h1>
	 * 
	 * :id - ID of Post being deleted
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return				{@code true} if Delete is successful
	 * 
	 * @throws IOException			if there is a JSON Parsing Issue or a JWT Validation Failure
	 * @throws NotFoundException	if a Post with the given ID does not exist
	 */
	public Boolean handleDeletePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		authorizeRequest(request, dataService.getUserIdFromPostId(postId), Permission.DELETE_POST);
		if (!dataService.deletePost(postId)) {
			throw new NotFoundException(NOT_FOUND_MESSAGE);
		}
		return true;
	}

	/**
	 * <h1> GET /api/user/:userId/commentedposts </h1>
	 * 
	 * :userId - ID of the User who commented on these Posts
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 							Collection of {@link Post}s that the User commented on
	 * @throws NumberFormatException	if the given userId is not a number
	 */
	public Collection<Post> handleGetCommentedPosts(Request request, Response response) {

		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		return dataService.getCommentedPostsByUserId(userId);
	}
	
	/**
	 * 
	 * @param newPost
	 * @throws BadRequestException	if request does not have a userId or text
	 */
	private void validateAddPostRequest(Post newPost) {
		StringBuilder sb = new StringBuilder();
		checkParameter(newPost.getUserId(), "Add Post Request requires a 'userId'", sb);
		checkParameter(newPost.getText(), "Add Post Request requires 'text'", sb);
		throwExceptionIfNecessary(sb);
	}
	
	/**
	 * 
	 * @param body
	 * @throws BadRequestException	if request does not have a `text` field
	 */
	private void validateEditPostRequest(Entry body) {
		StringBuilder sb = new StringBuilder();
		checkParameter(body.getText(), "Edit Post Request requires 'text'", sb);
		throwExceptionIfNecessary(sb);
	}
}
