package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.jms.socialmedia.cache.CachingService;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

public class CachingDataService implements DataService {

	private final DataService dataService;
	private final CachingService cachingService;

	public CachingDataService(DataService dataService, CachingService cachingService) {
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
	public boolean isUsernamePresent(String username) {
		return dataService.isUsernamePresent(username);
	}

	@Override
	public boolean isEmailPresent(String email) {
		return dataService.isEmailPresent(email);
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
	public Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate, String beforeDate,
			String afterDate) {
		Collection<Post> posts = dataService.getPosts(userIds, username, tag, onDate, beforeDate, afterDate);
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
		Post post = cachingService.getPostFromCache(postId);
		if (post != null) {
			post.setText(postText);
		}
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
		Post post = cachingService.getPostFromCache(postId);
		if (post != null) {
			post.addLike(userId);
		}
		return dataService.likePost(postId, userId);
	}

	@Override
	public boolean unlikePost(int postId, int userId) {
		Post post = cachingService.getPostFromCache(postId);
		if (post != null) {
			post.removeLike(userId);
		}
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
		Comment comment = cachingService.getCommentFromCache(commentId);
		if (comment != null) {
			comment.setText(commentText);
		}
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
		Comment comment = cachingService.getCommentFromCache(commentId);
		if (comment != null) {
			comment.addLike(userId);
		}
		return dataService.likeComment(commentId, userId);
	}

	@Override
	public boolean unlikeComment(int commentId, int userId) {
		Comment comment = cachingService.getCommentFromCache(commentId);
		if (comment != null) {
			comment.removeLike(userId);
		}
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
