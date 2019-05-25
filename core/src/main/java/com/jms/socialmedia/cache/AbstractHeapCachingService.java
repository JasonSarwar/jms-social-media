package com.jms.socialmedia.cache;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

public abstract class AbstractHeapCachingService extends AbstractCachingService {

	@Override
	public void editPostInCache(int postId, String text) {
		Post post = getPostFromCache(postId);
		if (post != null) {
			post.setText(text);
		}
	}

	@Override
	public void likePostInCache(int postId, int userId) {
		Post post = getPostFromCache(postId);
		if (post != null) {
			post.addLike(userId);
		}
	}

	@Override
	public void unlikePostInCache(int postId, int userId) {
		Post post = getPostFromCache(postId);
		if (post != null) {
			post.removeLike(userId);
		}
	}

	@Override
	public void editCommentInCache(int commentId, String text) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			comment.setText(text);
		}
	}

	@Override
	public void likeCommentInCache(int commentId, int userId) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			comment.addLike(userId);
		}
	}

	@Override
	public void unlikeCommentInCache(int commentId, int userId) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			comment.removeLike(userId);
		}
	}
}
