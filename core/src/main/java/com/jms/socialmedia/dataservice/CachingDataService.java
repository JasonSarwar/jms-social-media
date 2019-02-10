package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserObject;

abstract class CachingDataService implements DataService {

	private final DataService dataService;
	
	protected CachingDataService(DataService dataService) {
		this.dataService = dataService;
	}

	protected abstract FullPost getPostFromCache(int postId);
	
	protected abstract void putPostIntoCache(FullPost post);
	
	protected abstract void removePostFromCache(int postId);
	
	protected abstract void removePostFromCacheUsingCommentId(int commentId);
	
	@Override
	public UserObject getUser(String username) {
		return dataService.getUser(username);
	}

	@Override
	public User getUserLoginInfoByName(String username) {
		return dataService.getUserLoginInfoByName(username);
	}

	@Override
	public User getHashedPasswordByUserId(Integer userId) {
		return dataService.getHashedPasswordByUserId(userId);
	}

	@Override
	public Collection<String> getUsernamesByIds(Collection<Integer> userIds) {
		return dataService.getUsernamesByIds(userIds);
	}

	@Override
	public boolean editPassword(Integer userId, String hashedPassword) {
		return dataService.editPassword(userId, hashedPassword);
	}

	@Override
	public String createUser() {
		return dataService.createUser();
	}

	@Override
	public boolean addUserSession(int userId, String sessionKey) {
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
	public Collection<Post> getPosts(Integer userId, String username, String tag, String onDate, String beforeDate,
			String afterDate) {
		return dataService.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
	}

	@Override
	public Post getPost(int postId) {
		Post post = getPostFromCache(postId);
		return post != null ? post : dataService.getPost(postId);
	}

	@Override
	public FullPost getPostWithComments(int postId) {
		FullPost post = getPostFromCache(postId);
		if (post == null) {
			post = dataService.getPostWithComments(postId);
			putPostIntoCache(post);
		}
		return post;
	}

	@Override
	public Integer getUserIdFromPostId(int postId) {
		FullPost post = getPostFromCache(postId);
		if (post != null) {
			return post.getUserId();
		}
		return dataService.getUserIdFromPostId(postId);
	}

	@Override
	public boolean addPost(Post post) {
		return dataService.addPost(post);
	}
	
	@Override
	public boolean editPost(int postId, String postText) {
		removePostFromCache(postId);
		return dataService.editPost(postId, postText);
	}

	@Override
	public boolean deletePost(int postId) {
		removePostFromCache(postId);
		return dataService.deletePost(postId);
	}

	@Override
	public Collection<Integer> getLikesOfPost(int postId) {
		return dataService.getLikesOfPost(postId);
	}

	@Override
	public boolean likePost(int postId, int userId) {
		return dataService.likePost(postId, userId);
	}

	@Override
	public boolean unlikePost(int postId, int userId) {
		return dataService.unlikePost(postId, userId);
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		return dataService.getComments(postId);
	}

	@Override
	public Comment getComment(int commentId) {
		return dataService.getComment(commentId);
	}

	@Override
	public Integer getUserIdFromCommentId(int commentId) {
		return dataService.getUserIdFromCommentId(commentId);
	}

	@Override
	public boolean addComment(Comment comment) {
		removePostFromCache(comment.getPostId());
		return dataService.addComment(comment);
	}
	
	@Override
	public boolean editComment(int commentId, String commentText) {
		removePostFromCacheUsingCommentId(commentId);
		return dataService.editComment(commentId, commentText);
	}

	@Override
	public boolean deleteComment(int commentId) {
		removePostFromCacheUsingCommentId(commentId);
		return dataService.deleteComment(commentId);
	}
}
