package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.jms.socialmedia.cache.AbstractCachingService;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

public class CachingDataService implements DataService {

	private final DataService dataService;
	private final AbstractCachingService cachingService;

	public CachingDataService(DataService dataService, AbstractCachingService cachingService) {
		this.dataService = dataService;
		this.cachingService = cachingService;
	}

	@Override
	public Integer getUserIdByUsername(String username) {
		return dataService.getUserIdByUsername(username);
	}

	@Override
	public UserPage getUserPageInfoByName(String username) {
		return dataService.getUserPageInfoByName(username);
	}

	@Override
	public User getUserLoginInfoByString(String username) {
		return dataService.getUserLoginInfoByString(username);
	}

	@Override
	public User getHashedPasswordByUserId(Integer userId) {
		return dataService.getHashedPasswordByUserId(userId);
	}

	@Override
	public Collection<User> getUsernamesByIds(Collection<Integer> userIds) {
		return dataService.getUsernamesByIds(userIds);
	}

	@Override
	public Collection<User> getUsersToFollow(int userId) {
		return dataService.getUsersToFollow(userId);
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return dataService.isUsernameTaken(username);
	}

	@Override
	public boolean isEmailTaken(String email) {
		return dataService.isEmailTaken(email);
	}

	@Override
	public boolean addUser(NewUser newUser) {
		return dataService.addUser(newUser);
	}

	@Override
	public boolean editPassword(Integer userId, String hashedPassword) {
		return dataService.editPassword(userId, hashedPassword);
	}

	@Override
	public boolean addUserSession(int userId, String sessionId) {
		return dataService.addUserSession(userId, sessionId);
	}

	@Override
	public User getUserBySessionId(String sessionId) {
		return cachingService.getUserSessionCacheOrSupplier(sessionId, () -> dataService.getUserBySessionId(sessionId));
	}

	@Override
	public void removeSessionId(String sessionId) {
		cachingService.removeUserSessionFromCache(sessionId);
		dataService.removeSessionId(sessionId);
	}

	@Override
	public Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate, String beforeDate,
			String afterDate, Integer sincePostId, String sortBy, boolean sortOrderAsc) {
		Collection<Post> posts = dataService.getPosts(userIds, username, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc);
		// Put the first 5 posts into cache
		int i = 0;
		for (Post post : posts) {
			cachingService.putPostIntoCache(post);
			if (i++ > 5)
				break;
		}
		return posts;
	}

	@Override
	public Post getPost(int postId) {
		return cachingService.getPostFromCacheOrSupplier(postId, () -> dataService.getPost(postId));
	}

	@Override
	public Integer getUserIdFromPostId(int postId) {
		Post post = cachingService.getPostFromCache(postId);
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
		cachingService.editPostInCache(postId, postText);
		return dataService.editPost(postId, postText);
	}

	@Override
	public boolean deletePost(int postId) {
		cachingService.removePostFromCache(postId);
		return dataService.deletePost(postId);
	}

	@Override
	public Collection<Post> getCommentedPostsByUserId(int userId) {
		return dataService.getCommentedPostsByUserId(userId);
	}

	@Override
	public Collection<Post> getLikedPostsByUserId(int userId) {
		return dataService.getLikedPostsByUserId(userId);
	}

	@Override
	public Collection<Integer> getPostLikes(int postId) {
		Post post = cachingService.getPostFromCache(postId);
		if (post != null) {
			return post.getLikes();
		}
		return dataService.getPostLikes(postId);
	}

	@Override
	public boolean likePost(int postId, int userId) {
		cachingService.likePostInCache(postId, userId);
		return dataService.likePost(postId, userId);
	}

	@Override
	public boolean unlikePost(int postId, int userId) {
		cachingService.unlikePostInCache(postId, userId);
		return dataService.unlikePost(postId, userId);
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		return cachingService.getCommentsFromCacheOrSupplier(postId, () -> dataService.getComments(postId));
	}

	@Override
	public Collection<Comment> getCommentsByUserId(int userId) {
		return dataService.getCommentsByUserId(userId);
	}

	@Override
	public Comment getComment(int commentId) {
		Comment comment = cachingService.getCommentFromCache(commentId);
		if (comment == null) {
			comment = dataService.getComment(commentId);
			cachingService.putCommentIntoCache(comment);
		}
		return comment;
	}

	@Override
	public Integer getUserIdFromCommentId(int commentId) {
		Comment comment = cachingService.getCommentFromCache(commentId);
		if (comment == null) {
			comment = dataService.getComment(commentId);
			return comment.getUserId();
		}
		return dataService.getUserIdFromCommentId(commentId);
	}

	@Override
	public boolean addComment(Comment comment) {
		if (dataService.addComment(comment)) {
			cachingService.putCommentIntoCache(getComment(comment.getCommentId()));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean editComment(int commentId, String commentText) {
		cachingService.editCommentInCache(commentId, commentText);
		return dataService.editComment(commentId, commentText);
	}

	@Override
	public boolean deleteComment(int commentId) {
		cachingService.removeCommentFromCache(commentId);
		return dataService.deleteComment(commentId);
	}
	
	@Override
	public Collection<Integer> getCommentLikes(int commentId) {
		Comment comment = cachingService.getCommentFromCache(commentId);
		if (comment != null) {
			return comment.getLikes();
		}
		return dataService.getCommentLikes(commentId);
	}

	@Override
	public boolean likeComment(int commentId, int userId) {
		cachingService.likeCommentInCache(commentId, userId);
		return dataService.likeComment(commentId, userId);
	}

	@Override
	public boolean unlikeComment(int commentId, int userId) {
		cachingService.unlikeCommentInCache(commentId, userId);
		return dataService.unlikeComment(commentId, userId);
	}
	
	@Override
	public Collection<Integer> getFollowerUserIds(int userId) {
		return dataService.getFollowerUserIds(userId);
	}

	@Override
	public Collection<Integer> getFollowingUserIds(int userId) {
		return dataService.getFollowingUserIds(userId);
	}

	@Override
	public boolean followUser(int followerUserId, int followingUserId) {
		return dataService.followUser(followerUserId, followingUserId);
	}

	@Override
	public boolean unfollowUser(int followerUserId, int followingUserId) {
		return dataService.unfollowUser(followerUserId, followingUserId);
	}
}
