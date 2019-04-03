package com.jms.socialmedia.handlers;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.Entry;
import com.jms.socialmedia.model.Post;
import spark.Request;
import spark.Response;

public class PostRequestHandler extends RequestHandler {

	public PostRequestHandler(DataService dataService, Gson gson) {
		super(dataService, gson);
	}

	/**
	 * <h1> GET /api/posts?userId=[id]&username=[username]&tag=[tag]&on=[date]&before=[date]&after=[date] </h1>
	 * 
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

		String userIdStr = request.queryParams("userId");
		Collection<Integer> userIds = userIdStr == null ? null : Arrays.stream(userIdStr.split("[,|]")).map(Integer::parseInt).collect(toSet());
		String username = request.queryParams("username");
		String tag = request.queryParams("tag");
		String onDate = request.queryParams("on");
		String beforeDate = request.queryParams("before");
		String afterDate = request.queryParams("after");
		String strSincePostId = request.queryParams("sincePostId");
		Integer sincePostId = strSincePostId == null ? null : Integer.parseInt(strSincePostId);
		String sortBy = Optional.ofNullable(request.queryParams("sortBy")).orElse("postId");
		String order = request.queryParams("order");
		boolean sortOrderAsc = order == null ? false : order.equalsIgnoreCase("asc");

		return dataService.getPosts(userIds, username, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc);
	}

	/**
	 * <h1> GET /api/user/:userid/posts </h1>
	 * 
	 * :userId - ID of User who made the Posts
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 				Collection of {@link Post}s made by the User
	 * 
	 * @throws NumberFormatException	if given userid is not a number
	 */
	public Collection<Post> handleGetPostsByUserId(Request request, Response response) {

		int userId = Integer.parseInt(request.params(":userid"));
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
		
		int postId = Integer.parseInt(request.params(":id"));
		Post post = dataService.getPost(postId);

		if(post != null) {
			return post;
		} else {
			throw new NotFoundException("Post Not Found");
		}
	}

	/**
	 * <h1> POST /api/post/add </h1>
	 * 
	 * <pre>
	 * {
	 *   "userId": [ID of User making the Post],
	 *   "text": [Post Text]
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
		validateAddEntryRequest(newPost);
		authorizeRequest(request, newPost.getUserId(), "Add Post");
		return dataService.addPost(newPost);
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

		int postId = Integer.parseInt(request.params(":id"));
		Entry body = extractBodyContent(request, Post.class);
		authorizeRequest(request, dataService.getUserIdFromPostId(postId), "Edit Post");
		if (!dataService.editPost(postId, body.getText())) {
			throw new NotFoundException("Post Not Found");
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

		int postId = Integer.parseInt(request.params(":id"));
		authorizeRequest(request, dataService.getUserIdFromPostId(postId), "Delete Post");
		if (!dataService.deletePost(postId)) {
			throw new NotFoundException("Post Not Found");
		}
		return true;
	}

	/**
	 * <h1> GET /api/user/:userid/commentedposts </h1>
	 * 
	 * :userid - ID of the User who commented on these Posts
	 * 
	 * @param request		Spark Request
	 * @param response		Spark Response
	 * @return 							Collection of {@link Post}s that the User commented on
	 * @throws NumberFormatException	if the given userid is not a number
	 */
	public Collection<Post> handleGetCommentedPosts(Request request, Response response) {

		int userId = Integer.parseInt(request.params("userid"));
		return dataService.getCommentedPostsByUserId(userId);
	}
	
	/**
	 * 
	 * @param newPost
	 * @throws BadRequestException	if request does not have a userId or text
	 */
	private void validateAddEntryRequest(Post newPost) {
		StringBuilder sb = new StringBuilder();
		if (!newPost.hasUserId()) {
			sb.append("Add Post Request requires a 'userId'\n");
		}
		if (!newPost.hasText()) {
			sb.append("Add Post Request requires 'text'");
		}
		if (sb.length() > 0) {
			throw new BadRequestException(sb.toString());
		}
	}
}
