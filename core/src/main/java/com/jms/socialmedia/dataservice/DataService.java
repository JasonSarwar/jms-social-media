package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserObject;

import static java.util.Collections.singleton;

public interface DataService {

	UserObject getUser(String username);

	User getUserLoginInfoByName(String username);

	User getHashedPasswordByUserId(Integer userId);

	Collection<String> getUsernamesByIds(Collection<Integer> userIds);

	boolean editPassword(Integer userId, String hashedPassword);

	String createUser();

	boolean addUserSession(int userId, String sessionKey);

	User getUserBySessionKey(String sessionKey);

	void removeSessionKey(String sessionKey);

	Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate, String beforeDate, String afterDate);

	default Collection<Post> getPosts(Integer userId) {
		return getPosts(singleton(userId), null, null, null, null, null);
	}

	Post getPost(int postId);

	FullPost getPostWithComments(int postId);

	Integer getUserIdFromPostId(int postId);

	boolean addPost(Post post);

	boolean editPost(int postId, String postText);

	boolean deletePost(int postId);

	Collection<Post> getLikedPostsByUserId(int userId);

	Collection<Integer> getPostLikes(int postId);

	boolean likePost(int postId, int userId);

	boolean unlikePost(int postId, int userId);

	Collection<Comment> getComments(int postId);

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
