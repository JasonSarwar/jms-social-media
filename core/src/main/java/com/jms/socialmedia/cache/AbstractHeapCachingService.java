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
	public void likePostInCache(int postId, String username) {
		Post post = getPostFromCache(postId);
		if (post != null) {
			post.addLike(username);
		}
	}

	@Override
	public void unlikePostInCache(int postId, String username) {
		Post post = getPostFromCache(postId);
		if (post != null) {
			post.removeLike(username);
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
			invalidateCommentsByPostId(comment.getPostId());
		}
	}

	@Override
	public void likeCommentInCache(int commentId, String username) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			comment.addLike(username);
		}
	}

	@Override
	public void unlikeCommentInCache(int commentId, int userId) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			invalidateCommentsByPostId(comment.getPostId());
		}
	}

	@Override
	public void unlikeCommentInCache(int commentId, String username) {
		Comment comment = getCommentFromCache(commentId);
		if (comment != null) {
			comment.removeLike(username);
		}
	}
	
	protected abstract void invalidateCommentsByPostId(int postId);
}
