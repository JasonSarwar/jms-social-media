package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

public class LikeRequestHandler extends RequestHandler {

	private static final String USER_ID_PARAM = "userId";
	private static final String POST_ID_PARAM = "postId";
	private static final String COMMENT_ID_PARAM = "commentId";

	public LikeRequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
		super(dataService, tokenService, gson);
	}

	public Collection<Post> handleGetLikedPosts(Request request, Response response) {

		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		return dataService.getLikedPostsByUserId(userId);
	}

	public Collection<String> handleGetPostLikes(Request request, Response response) {

		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		return dataService.getPostLikes(postId);
	}

	public Boolean handleLikePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		authorizeRequest(request, userId, Permission.LIKE_POST);
		return dataService.likePost(postId, userId);
	}

	public Boolean handleUnlikePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		authorizeRequest(request, userId, Permission.UNLIKE_POST);
		return dataService.unlikePost(postId, userId);
	}

	public Collection<String> handleGetCommentLikes(Request request, Response response) {

		int commentId = Integer.parseInt(request.params(COMMENT_ID_PARAM));
		return dataService.getCommentLikes(commentId);
	}

	public Boolean handleLikeComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params(COMMENT_ID_PARAM));
		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		authorizeRequest(request, userId, Permission.LIKE_COMMENT);
		return dataService.likeComment(commentId, userId);
	}

	public Boolean handleUnlikeComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params(COMMENT_ID_PARAM));
		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		authorizeRequest(request, userId, Permission.UNLIKE_COMMENT);
		return dataService.unlikeComment(commentId, userId);
	}
}
