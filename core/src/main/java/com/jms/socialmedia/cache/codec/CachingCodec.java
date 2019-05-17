package com.jms.socialmedia.cache.codec;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

public interface CachingCodec<T> {

	T encodePost(Post post);
	
	Post decodePost(T post);
	
	T encodeComment(Comment comment);
	
	Comment decodeComment(T comment);
	
	T encodeUser(User user);
	
	User decodeUser(T user);
}
