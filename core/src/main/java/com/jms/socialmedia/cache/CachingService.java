package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public abstract class CachingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachingService.class);

	public abstract Post getPostFromCache(int postId);

	public abstract void putPostIntoCache(Post post);

	public abstract void removePostFromCache(int postId);

	public abstract Collection<Comment> getCommentsFromCache(int postId);

	public abstract Comment getCommentFromCache(int commentId);

	public abstract void putCommentIntoCache(Comment comment);

	public abstract void putCommentsFromPostIntoCache(int postId, Collection<Comment> comments);

	public abstract void removeCommentFromCache(int commentId);

	public abstract User getUserSessionFromCache(String sessionKey);

	public abstract void putUserSessionIntoCache(String sessionKey, User user);

	public abstract void removeUserSessionFromCache(String sessionKey);

	public Post getPostFromCacheOrSupplier(int postId, Supplier<Post> supplier) {
		Post post = getPostFromCache(postId);
		if (post == null) {
			post = supplier.get();
			if (post != null) {
				putPostIntoCache(post);
				LOGGER.info("Retrieved Post #{} from DataService", postId);
			}
		} else {
			LOGGER.info("Retrieved Post #{} from CachingService", postId);
		}
		return post;
	}

	public Collection<Comment> getCommentsFromCacheOrSupplier(int postId, Supplier<Collection<Comment>> supplier) {
		Collection<Comment> comments = getCommentsFromCache(postId);
		if (comments == null) {
			comments = supplier.get();
			if (comments != null) {
				putCommentsFromPostIntoCache(postId, comments);
				LOGGER.info("Retrieved {} Comments for Post #{} from DataService", comments.size(), postId);
			}
		} else {
			LOGGER.info("Retrieved {} Comments for Post #{} from CachingService", comments.size(), postId);
		}
		return comments;
	}

	public User getUserSessionCacheOrSupplier(String sessionKey, Supplier<User> supplier) {
		User user = getUserSessionFromCache(sessionKey);
		if (user == null) {
			user = supplier.get();
			if (user != null) {
				putUserSessionIntoCache(sessionKey, user);
				LOGGER.info("Retrieved User Session for {} from DataService", user.getUsername());
			}
		} else {
			LOGGER.info("Retrieved User Session for {} from CachingService", user.getUsername());
		}
		return user;
	}
}
