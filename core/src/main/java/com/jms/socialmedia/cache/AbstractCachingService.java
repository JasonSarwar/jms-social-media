package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public abstract class AbstractCachingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCachingService.class);

	/**
	 * Retrieves a {@link Post} from cache if present
	 * @param postId	ID of Post
	 * @return			Post or {@code null}
	 */
	public abstract Post getPostFromCache(int postId);

	/**
	 * Either edits the text of the Post in the Cache, or invalidates the Post
	 * @param postId	ID of the Post being edited
	 * @param text		New text of the Post
	 */
	public abstract void editPostInCache(int postId, String text);

	public abstract void putPostIntoCache(Post post);

	/**
	 * Removes a {@link Post} and all of its {@link Comment}s from cache
	 * @param postId
	 */
	public abstract void removePostFromCache(int postId);

	/**
	 * Either adds a Like to the Post in the Cache, or invalidates the Post
	 * @param postId	ID of the Post being liked
	 * @param username	User that liked the Post
	 */
	public abstract void likePostInCache(int postId, String username);

	/**
	 * Either removes a Like in the Post in the Cache, or invalidates the Post
	 * @param postId	ID of the Post being unliked
	 * @param username	User that unliked the Post
	 */
	public abstract void unlikePostInCache(int postId, String username);

	public abstract Collection<Comment> getCommentsFromCache(int postId);

	public abstract Comment getCommentFromCache(int commentId);

	/**
	 * Either edits the text of the Comment in the Cache, or invalidates the Comment
	 * @param commentId		ID of the Comment being edited
	 * @param text			New text of the Comment
	 */
	public abstract void editCommentInCache(int commentId, String text);

	public abstract void putCommentIntoCache(Comment comment);

	public abstract void putCommentsFromPostIntoCache(int postId, Collection<Comment> comments);

	public abstract void removeCommentFromCache(int commentId);

	/**
	 * Either adds a Like to the Comment in the Cache, or invalidates the Comment
	 * @param commentId	ID of the Comment being liked
	 * @param userId	ID of the User that liked the Comment
	 */
	public abstract void likeCommentInCache(int commentId, int userId);

	/**
	 * Either adds a Like to the Comment in the Cache, or invalidates the Comment
	 * @param commentId	ID of the Comment being liked
	 * @param username	User that liked the Comment
	 */
	public abstract void likeCommentInCache(int commentId, String username);

	/**
	 * Either removes a Like in the Comment in the Cache, or invalidates the Comment
	 * @param commentId	ID of the Comment being unliked
	 * @param userId	ID of the User that unliked the Comment
	 */
	public abstract void unlikeCommentInCache(int commentId, int userId);

	/**
	 * Either removes a Like in the Comment in the Cache, or invalidates the Comment
	 * @param commentId	ID of the Comment being unliked
	 * @param username	User that unliked the Comment
	 */
	public abstract void unlikeCommentInCache(int commentId, String username);

	public abstract User getUserSessionFromCache(String sessionKey);

	public abstract void putUserSessionIntoCache(String sessionKey, User user);

	public abstract void removeUserSessionFromCache(String sessionKey);

	public Post getPostFromCacheOrSupplier(int postId, Supplier<Post> supplier) {
		Post post = getPostFromCache(postId);
		if (post == null) {
			post = supplier.get();
			postCacheMiss();
			if (post != null) {
				putPostIntoCache(post);
				LOGGER.info("Retrieved Post #{} from DataService", postId);
			}
		} else {
			postCacheHit();
			LOGGER.info("Retrieved Post #{} from CachingService", postId);
		}
		return post;
	}

	public Collection<Comment> getCommentsFromCacheOrSupplier(int postId, Supplier<Collection<Comment>> supplier) {
		Collection<Comment> comments = getCommentsFromCache(postId);
		if (comments == null) {
			comments = supplier.get();
			commentsCacheMiss();
			if (comments != null) {
				putCommentsFromPostIntoCache(postId, comments);
				LOGGER.info("Retrieved {} Comments for Post #{} from DataService", comments.size(), postId);
			}
		} else {
			commentsCacheHit();
			LOGGER.info("Retrieved {} Comments for Post #{} from CachingService", comments.size(), postId);
		}
		return comments;
	}

	public User getUserSessionCacheOrSupplier(String sessionKey, Supplier<User> supplier) {
		User user = getUserSessionFromCache(sessionKey);
		if (user == null) {
			user = supplier.get();
			userSessionCacheMiss();
			if (user != null) {
				putUserSessionIntoCache(sessionKey, user);
				LOGGER.info("Retrieved User Session for {} from DataService", user.getUsername());
			}
		} else {
			userSessionCacheHit();
			LOGGER.info("Retrieved User Session for {} from CachingService", user.getUsername());
		}
		return user;
	}

	protected void postCacheHit() {
	}

	protected void postCacheMiss() {
	}

	protected void commentsCacheHit() {
	}

	protected void commentsCacheMiss() {
	}

	protected void userSessionCacheHit() {
	}

	protected void userSessionCacheMiss() {
	}
}
