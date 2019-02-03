package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserObject;

public interface DataService {

	UserObject getUser(String username);

	User getUserLoginInfoByName(String username);

	User getHashedPasswordByUserId(Integer userId);
	
	boolean editPassword(Integer userId, String hashedPassword);

	String createUser();

	boolean addUserSession(int userId, String sessionKey);

	User getUserBySessionKey(String sessionKey);

	void removeSessionKey(String sessionKey);

	Collection<Post> getPosts(Integer userId, String username, String tag, String onDate, String beforeDate, String afterDate);

	Post getPost(int postId);

	FullPost getPostWithComments(int postId);

	Integer getUserIdFromPostId(int postId);

	boolean addPost(Post post);

	boolean editPost(int postId, String postText);

	boolean deletePost(int postId);

	Collection<Comment> getComments(int postId);

	Comment getComment(int commentId);

	Integer getUserIdFromCommentId(int commentId);

	boolean addComment(Comment comment);

	boolean editComment(int commentId, String commentText);

	boolean deleteComment(int commentId);

}
