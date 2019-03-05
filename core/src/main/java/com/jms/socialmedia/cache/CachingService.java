package com.jms.socialmedia.cache;

import java.util.function.Supplier;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

public interface CachingService {

	Post getPostFromCache(int postId);
	
	Post getPostFromCacheOrSupplier(int postId, Supplier<Post> supplier);

	void putPostIntoCache(Post post);
	
	void removePostFromCache(int postId);
	
	Comment getCommentFromCache(int commentId);

	void putCommentIntoCache(Comment comment);
	
	void removeCommentFromCache(int commentId);

}
