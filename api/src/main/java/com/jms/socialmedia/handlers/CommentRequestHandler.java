package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Entry;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

public class CommentRequestHandler extends RequestHandler {

	private static final String NOT_FOUND_MESSAGE = "Comment Not Found";
	private static final String POST_ID_PARAM = "postId";
	private static final String COMMENT_ID_PARAM = "commentId";
	private static final String USER_ID_PARAM = "userId";

	public CommentRequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
		super(dataService, tokenService, gson);
	}

	public Collection<Comment> handleGetComments(Request request, Response response) {

		int postId = Integer.parseInt(request.params(POST_ID_PARAM));
		return dataService.getComments(postId);
	}

	public Collection<Comment> handleGetCommentsByUserId(Request request, Response response) {

		int userId = Integer.parseInt(request.params(USER_ID_PARAM));
		return dataService.getCommentsByUserId(userId);
	}

	public Comment handleGetComment(Request request, Response response) {

		int commentId = Integer.parseInt(request.params(COMMENT_ID_PARAM));
		Comment comment = dataService.getComment(commentId);
		if (comment != null) {
			return comment;
		} else {
			throw new NotFoundException(NOT_FOUND_MESSAGE);
		}
	}

	public Boolean handleAddComment(Request request, Response response) throws IOException {

		Comment newComment = extractBodyContent(request, Comment.class);
		String strPostId = request.params(POST_ID_PARAM);
		if (strPostId != null) {
			int postId = Integer.parseInt(strPostId);
			newComment.setPostId(postId);
		}
		validateAddCommentRequest(newComment);
		authorizeRequest(request, newComment.getUserId(), Permission.ADD_COMMENT);
		return dataService.addComment(newComment);
	}

	public Boolean handleEditComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params(COMMENT_ID_PARAM));
		Entry body = extractBodyContent(request, Comment.class);
		validateEditCommentRequest(body);
		authorizeRequest(request, dataService.getUserIdFromCommentId(commentId), Permission.EDIT_COMMENT);
		if (!dataService.editComment(commentId, body.getText())) {
			throw new NotFoundException(NOT_FOUND_MESSAGE);
		}
		return true;
	}

	public Boolean handleDeleteComment(Request request, Response response) throws IOException {

		int commentId = Integer.parseInt(request.params(COMMENT_ID_PARAM));
		authorizeRequest(request, dataService.getUserIdFromCommentId(commentId), Permission.DELETE_COMMENT);
		if (!dataService.deleteComment(commentId)) {
			throw new NotFoundException(NOT_FOUND_MESSAGE);
		}
		return true;
	}

	/**
	 * 
	 * @param newComment
	 * @throws BadRequestException if request does not have a userId, postId or text
	 */
	private void validateAddCommentRequest(Comment newComment) {
		StringBuilder sb = new StringBuilder();
		checkParameter(newComment.getUserId(), "Add Comment Request requires a 'userId'", sb);
		checkParameter(newComment.getPostId(), "Add Comment Request requires a 'postId'", sb);
		checkParameter(newComment.getText(), "Add Comment Request requires 'text'", sb);
		throwExceptionIfNecessary(sb);
	}
	
	/**
	 * 
	 * @param body
	 * @throws BadRequestException	if request does not have a `text` field
	 */
	private void validateEditCommentRequest(Entry body) {
		StringBuilder sb = new StringBuilder();
		checkParameter(body.getText(), "Edit Comment Request requires 'text'", sb);
		throwExceptionIfNecessary(sb);
	}
}
