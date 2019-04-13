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

	public LikeRequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
		super(dataService, tokenService, gson);
	}

	public Collection<Post> handleGetLikedPosts(Request request, Response response) {

		int userId = Integer.parseInt(request.params("userId"));
		return dataService.getLikedPostsByUserId(userId);
	}

	public Collection<Integer> handleGetPostLikes(Request request, Response response) {

		int postId = Integer.parseInt(request.params("postId"));
		return dataService.getPostLikes(postId);
	}

	public Boolean handleLikePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params("postId"));
		int userId = Integer.parseInt(request.params("userId"));
		authorizeRequest(request, userId, Permission.LIKE_POST);
		return dataService.likePost(postId, userId);
	}

	public Boolean handleUnlikePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params("postId"));
		int userId = Integer.parseInt(request.params("userId"));
		authorizeRequest(request, userId, Permission.UNLIKE_POST);
		return dataService.unlikePost(postId, userId);
	}

	public Collection<Integer> handleGetCommentLikes(Request request, Response response) {

		int commentId = Integer.parseInt(request.params("commentId"));
		return dataService.getCommentLikes(commentId);
	}

	public Boolean handleLikeComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params("commentId"));
		int userId = Integer.parseInt(request.params("userId"));
		authorizeRequest(request, userId, Permission.LIKE_COMMENT);
		return dataService.likeComment(commentId, userId);
	}

	public Boolean handleUnlikeComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params("commentId"));
		int userId = Integer.parseInt(request.params("userId"));
		authorizeRequest(request, userId, Permission.UNLIKE_COMMENT);
		return dataService.unlikeComment(commentId, userId);
	}
}
