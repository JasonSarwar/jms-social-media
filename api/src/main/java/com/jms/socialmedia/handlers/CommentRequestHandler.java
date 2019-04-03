package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Entry;
import spark.Request;
import spark.Response;

public class CommentRequestHandler extends RequestHandler {

	public CommentRequestHandler(DataService dataService, Gson gson) {
		super(dataService, gson);
	}

	public Collection<Comment> handleGetComments(Request request, Response response) {

		int postId = Integer.parseInt(request.params(":id"));
		return dataService.getComments(postId);
	}

	public Collection<Comment> handleGetCommentsByUserId(Request request, Response response) {

		int userid = Integer.parseInt(request.params(":userid"));
		return dataService.getCommentsByUserId(userid);
	}

	public Comment handleGetComment(Request request, Response response) {

		String strCommentId = request.params(":id");
		int commentId = Integer.parseInt(strCommentId);
		Comment comment = dataService.getComment(commentId);
		if(comment != null) {
			return comment;
		} else {
			throw new NotFoundException("Comment Not Found");
		}
	}

	public Boolean handleAddComment(Request request, Response response) throws IOException {
		
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

	public Boolean handleEditComment(Request request, Response response) throws IOException {

		String strCommentId = request.params(":id");
		int commentId = Integer.parseInt(strCommentId);
		Entry body = extractBodyContent(request, Comment.class);
		authorizeRequest(request, dataService.getUserIdFromCommentId(commentId), "Edit Comment");
		if (!dataService.editComment(commentId, body.getText())) {
			throw new NotFoundException("Comment Not Found");
		}
		return true;
	}

	public Boolean handleDeleteComment(Request request, Response response) throws IOException {

		String strCommentId = request.params(":id");
		int commentId = Integer.parseInt(strCommentId);
		authorizeRequest(request, dataService.getUserIdFromCommentId(commentId), "Edit Comment");
		if (!dataService.deleteComment(commentId)) {
			throw new NotFoundException("Comment Not Found");
		}
		return true;
	}

	/**
	 * 
	 * @param newComment
	 * @throws BadRequestException	if request does not have a userId, postId or text
	 */
	private void validateAddEntryRequest(Comment newComment) {
		StringBuilder sb = new StringBuilder();
		if (!newComment.hasUserId()) {
			sb.append("Add Comment Request requires a 'userId'\n");
		}
		if (!newComment.hasPostId()) {
			sb.append("Add Comment Request requires a 'postId'\n");
		}
		if (!newComment.hasText()) {
			sb.append("Add Comment Request requires 'text'");
		}
		if (sb.length() > 0) {
			throw new BadRequestException(sb.toString());
		}
	}
}
