package com.mytwitter.dataservice;

import java.util.Collection;

import com.mytwitter.model.Comment;
import com.mytwitter.model.FullPost;
import com.mytwitter.model.Post;
import com.mytwitter.model.User;
import com.mytwitter.model.UserObject;

abstract class CachingDataService implements DataService {

	private final DataService dataService;
	
	protected CachingDataService(DataService dataService) {
		this.dataService = dataService;
	}

	protected abstract FullPost getPostFromCache(int postId);
	
	protected abstract void putPostIntoCache(FullPost post);
	
	protected abstract void removePostFromCache(int postId);
	
	@Override
	public UserObject getUser(String username) {
		return dataService.getUser(username);
	}

	@Override
	public User getUserLoginInfo(String username) {
		return dataService.getUserLoginInfo(username);
	}

	@Override
	public String getUsers() {
		return dataService.getUsers();
	}

	@Override
	public String getUsersCount() {
		return dataService.getUsersCount();
	}

	@Override
	public String createUser() {
		return dataService.createUser();
	}

	@Override
	public int addUserSession(int userId, String sessionKey) {
		return dataService.addUserSession(userId, sessionKey);
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		return dataService.getUserBySessionKey(sessionKey);
	}

	@Override
	public void removeSessionKey(String sessionKey) {
		dataService.removeSessionKey(sessionKey);
	}

	@Override
	public FullPost getPost(int postId) {
		FullPost post = getPostFromCache(postId);
		if (post == null) {
			post = dataService.getPost(postId);
			putPostIntoCache(post);
		}
		return post;
	}

	@Override
	public Collection<Post> getPosts(int userId, String username, String tag, String onDate, String beforeDate,
			String afterDate) {
		return dataService.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
	}
	
	@Override
	public Collection<Comment> getComments(int postId) {
		return dataService.getComments(postId);
	}
	
	@Override
	public boolean addPost(Post post) {
		return dataService.addPost(post);
	}

	@Override
	public boolean addComment(Comment comment) {
		removePostFromCache(comment.getPostId());
		return dataService.addComment(comment);
	}
}
