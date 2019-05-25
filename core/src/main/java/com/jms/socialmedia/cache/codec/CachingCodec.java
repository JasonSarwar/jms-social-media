package com.jms.socialmedia.cache.codec;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public interface CachingCodec<T> {

	T encodePost(Post post);
	
	Post decodePost(T encodedPost);
	
	T encodeComment(Comment comment);
	
	Comment decodeComment(T encodedComment);
	
	T encodeUser(User user);
	
	User decodeUser(T encodedUser);
}
