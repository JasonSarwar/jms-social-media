package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.jms.socialmedia.cache.codec.CachingCodec;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public abstract class AbstractCodecCachingService<T> extends AbstractCachingService {

	private final CachingCodec<T> cachingServiceCodec;

	public AbstractCodecCachingService(CachingCodec<T> cachingServiceCodec) {
		this.cachingServiceCodec = cachingServiceCodec;
	}

	protected abstract T getEncodedPostFromCache(int postId);

	protected abstract void putEncodedPostIntoCache(int postId, T encodedPost);

	@Override
	public Post getPostFromCache(int postId) {
		return cachingServiceCodec.decodePost(getEncodedPostFromCache(postId));
	}

	@Override
	public void editPostInCache(int postId, String text) {
		invalidatePost(postId);
	}

	@Override
	public void putPostIntoCache(Post post) {
		putEncodedPostIntoCache(post.getPostId(), cachingServiceCodec.encodePost(post));
	}


	@Override
	public void likePostInCache(int postId, String username) {
		invalidatePost(postId);
	}

	@Override
	public void unlikePostInCache(int postId, String username) {
		invalidatePost(postId);
	}

	protected abstract Collection<T> getEncodedCommentsFromCache(int postId);

	protected abstract T getEncodedCommentFromCache(int commentId);

	protected abstract void putEncodedCommentsFromPostIntoCache(int postId, Map<T, Double> encodedCommentsWithId);

	protected abstract void putEncodedCommentIntoCache(int commentId, int postId, T encodedComment);

	/**
	 * Slightly different from {@link #removePostFromCache(int)}
	 * This one only removes the {@link Post}, not all the {@link Comment}s of the Post
	 * @param postId
	 */
	protected abstract void invalidatePost(int postId);

	@Override
	public Collection<Comment> getCommentsFromCache(int postId) {
		Collection<T> encodedComments = getEncodedCommentsFromCache(postId);
		if (encodedComments != null) {
			return encodedComments.stream().map(cachingServiceCodec::decodeComment)
				.collect(Collectors.toList());
		} else {
			return null;
		}
	}

	@Override
	public Comment getCommentFromCache(int commentId) {
		return cachingServiceCodec.decodeComment(getEncodedCommentFromCache(commentId));
	}

	@Override
	public void editCommentInCache(int commentId, String text) {
		invalidateComment(commentId);
	}

	@Override
	public void putCommentsFromPostIntoCache(int postId, Collection<Comment> comments) {
		if (!comments.isEmpty()) {
			putEncodedCommentsFromPostIntoCache(postId, comments.stream()
					.collect(Collectors.toMap(cachingServiceCodec::encodeComment, e -> e.getCommentId().doubleValue())));
		}
	}

	@Override
	public void putCommentIntoCache(Comment comment) {
		putEncodedCommentIntoCache(comment.getCommentId(), comment.getPostId(), cachingServiceCodec.encodeComment(comment));
	}

	@Override
	public void likeCommentInCache(int commentId, int userId) {
		invalidateComment(commentId);
	}

	@Override
	public void likeCommentInCache(int commentId, String username) {
		invalidateComment(commentId);
	}

	@Override
	public void unlikeCommentInCache(int commentId, int userId) {
		invalidateComment(commentId);
	}

	@Override
	public void unlikeCommentInCache(int commentId, String username) {
		invalidateComment(commentId);
	}

	protected abstract void invalidateComment(int commentId);

	protected abstract T getEncodedUserSessionFromCache(String sessionKey);

	protected abstract void putEncodedUserSessionIntoCache(String sessionKey, T encodedUserSession);

	@Override
	public User getUserSessionFromCache(String sessionKey) {
		return cachingServiceCodec.decodeUser(getEncodedUserSessionFromCache(sessionKey));
	}

	@Override
	public void putUserSessionIntoCache(String sessionKey, User user) {
		putEncodedUserSessionIntoCache(sessionKey, cachingServiceCodec.encodeUser(user));
	}
}
