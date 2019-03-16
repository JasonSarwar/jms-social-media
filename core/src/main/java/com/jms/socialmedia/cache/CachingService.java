package com.jms.socialmedia.cache;

import java.util.Collection;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

public interface CachingService {

	static final Logger LOGGER = LoggerFactory.getLogger(CachingService.class);
	
	Post getPostFromCache(int postId);

	default Post getPostFromCacheOrSupplier(int postId, Supplier<Post> supplier) {
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

	void putPostIntoCache(Post post);

	void removePostFromCache(int postId);

	Collection<Comment> getCommentsFromCache(int postId);

	default Collection<Comment> getCommentsFromCacheOrSupplier(int postId, Supplier<Collection<Comment>> supplier) {
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

	Comment getCommentFromCache(int commentId);

	void putCommentIntoCache(Comment comment);

	void putCommentsFromPostIntoCache(int postId, Collection<Comment> comments);

	void removeCommentFromCache(int commentId);

}
