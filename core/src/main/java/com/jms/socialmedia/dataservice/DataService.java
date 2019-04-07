package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

import static java.util.Collections.singleton;

public interface DataService {

	Integer getUserIdByUsername(String username);

	UserPage getUserPageInfoByName(String username);

	User getUserLoginInfoByString(String usernameOrEmail);

	User getHashedPasswordByUserId(Integer userId);

	Collection<User> getUsernamesByIds(Collection<Integer> userIds);

	Collection<User> getUsersToFollow(int userId);

	boolean isUsernameTaken(String username);

	boolean isEmailTaken(String email);

	boolean addUser(NewUser newUser);

	boolean editPassword(Integer userId, String hashedPassword);

	User getUserBySessionKey(String sessionKey);

	boolean addUserSession(int userId, String sessionKey);

	void removeSessionKey(String sessionKey);

	Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate, String beforeDate, 
			String afterDate, Integer sincePostId, String sortBy, boolean sortOrderAsc);

	default Collection<Post> getPosts(Integer userId) {
		return getPosts(singleton(userId), null, null, null, null, null, null, "postId", false);
	}

	default Collection<Post> getPosts(Integer userId, Integer sincePostId) {
		return getPosts(singleton(userId), null, null, null, null, null, sincePostId, "postId", false);
	}

	Post getPost(int postId);

	Integer getUserIdFromPostId(int postId);

	boolean addPost(Post post);

	boolean editPost(int postId, String postText);

	boolean deletePost(int postId);

	Collection<Post> getCommentedPostsByUserId(int userId);

	Collection<Post> getLikedPostsByUserId(int userId);

	Collection<Integer> getPostLikes(int postId);

	boolean likePost(int postId, int userId);

	boolean unlikePost(int postId, int userId);

	Collection<Comment> getComments(int postId);

	Collection<Comment> getCommentsByUserId(int userId);

	Comment getComment(int commentId);

	Integer getUserIdFromCommentId(int commentId);

	boolean addComment(Comment comment);

	boolean editComment(int commentId, String commentText);

	boolean deleteComment(int commentId);

	Collection<Integer> getCommentLikes(int commentId);

	boolean likeComment(int commentId, int userId);

	boolean unlikeComment(int commentId, int userId);
	
	Collection<Integer> getFollowerUserIds(int userId);
	
	Collection<Integer> getFollowingUserIds(int userId);
	
	boolean followUser(int followerUserId, int followingUserId);

	boolean unfollowUser(int followerUserId, int followingUserId);
}
