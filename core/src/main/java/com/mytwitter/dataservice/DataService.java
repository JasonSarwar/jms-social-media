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

	boolean addUserSession(int userId, String sessionKey);

	User getUserBySessionKey(String sessionKey);

	void removeSessionKey(String sessionKey);

	Collection<Post> getPosts(Integer userId, String username, String tag, String onDate, String beforeDate, String afterDate);

	Post getPost(int postId);

	FullPost getPostWithComments(int postId);

	boolean addPost(Post post);

	boolean editPost(int postId, String postText);

	boolean deletePost(int postId);

	Collection<Comment> getComments(int postId);

	Comment getComment(int commentId);

	boolean addComment(Comment comment);

	boolean editComment(int commentId, String commentText);

	boolean deleteComment(int commentId);

}
