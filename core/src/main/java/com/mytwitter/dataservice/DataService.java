package com.mytwitter.dataservice;

import java.util.Collection;

import com.mytwitter.model.Comment;
import com.mytwitter.model.FullPost;
import com.mytwitter.model.Post;
import com.mytwitter.model.User;
import com.mytwitter.model.UserObject;

public interface DataService {

	UserObject getUser(String username);
	
	User getUserLoginInfo(String username);
	
	String getUsers();
	
	String getUsersCount();
	
	String createUser();
	
	int addUserSession(int userId, String sessionKey);
	
	User getUserBySessionKey(String sessionKey);
	
	void removeSessionKey(String sessionKey);
	
	FullPost getPost(int postId);
	
	Collection<Post> getPosts(int userId, String username, String tag, String onDate, String beforeDate, String afterDate);
	
	Collection<Comment> getComments(int postId);
	
	boolean addPost(Post post);
	
	boolean addComment(Comment comment);
}
