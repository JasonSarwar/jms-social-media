package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.Post;
import spark.Request;
import spark.Response;

public class LikeRequestHandler extends RequestHandler {

	public LikeRequestHandler(DataService dataService, Gson gson) {
		super(dataService, gson);
	}

	public Collection<Post> handleGetLikedPosts(Request request, Response response) {

		int userId = Integer.parseInt(request.params("userid"));
		return dataService.getLikedPostsByUserId(userId);
	}

	public Collection<Integer> handleGetPostLikes(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(":id"));
		return dataService.getPostLikes(postId);
	}

	public Boolean handleLikePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(":postid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Like Post");
		return dataService.likePost(postId, userId);
	}

	public Boolean handleUnlikePost(Request request, Response response) throws IOException {

		int postId = Integer.parseInt(request.params(":postid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Unlike Post");
		return dataService.unlikePost(postId, userId);
	}

	public Collection<Integer> handleGetCommentLikes(Request request, Response response) {

		int commentId = Integer.parseInt(request.params(":id"));
		return dataService.getCommentLikes(commentId);
	}

	public Boolean handleLikeComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params(":commentid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Like Comment");
		return dataService.likeComment(commentId, userId);
	}

	public Boolean handleUnlikeComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params(":commentid"));
		int userId = Integer.parseInt(request.params(":userid"));
		authorizeRequest(request, userId, "Unlike Comment");
		return dataService.unlikeComment(commentId, userId);
	}
	

}
