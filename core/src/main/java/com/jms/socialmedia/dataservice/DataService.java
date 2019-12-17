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

	Collection<String> getUsernamesToFollow(String username);

	boolean isUsernameTaken(String username);

	boolean isEmailTaken(String email);

	boolean addUser(NewUser newUser);

	boolean editPassword(Integer userId, String hashedPassword);

	User getUserBySessionId(String sessionId);

	boolean addUserSession(int userId, String sessionId);

	void removeSessionId(String sessionId);

	Collection<Post> getPosts(Collection<Integer> userIds, Collection<String> usernames, String tag, String onDate, String beforeDate, 
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

	Collection<String> getPostLikes(int postId);

	/**
	 * @deprecated 			in favor of {@link #likePost(int, String)}
	 * @param postId		ID of the Post being liked
	 * @param userId		ID of the user liking the Post
	 * @return 				true if the action was successful
	 */
	@Deprecated
	boolean likePost(int postId, int userId);

	/**
	 * @param postId		ID of the Post being liked
	 * @param username		username of the user liking the Post
	 * @return 				true if the action was successful
	 */
	boolean likePost(int postId, String username);

	/**
	 * @deprecated 			in favor of {@link #unlikePost(int, String)}
	 * @param postId		ID of the Post being unliked
	 * @param userId		ID of the user unliking the Post
	 * @return 				true if the action was successful
	 */
	@Deprecated
	boolean unlikePost(int postId, int userId);

	/**
	 * @param postId		ID of the Post being unliked
	 * @param username		username of the user unliking the Post
	 * @return 				true if the action was successful
	 */
	boolean unlikePost(int postId, String username);

	Collection<Comment> getComments(int postId);

	Collection<Comment> getCommentsByUserId(int userId);

	Comment getComment(int commentId);

	Integer getUserIdFromCommentId(int commentId);

	boolean addComment(Comment comment);

	boolean editComment(int commentId, String commentText);

	boolean deleteComment(int commentId);

	Collection<String> getCommentLikes(int commentId);

	/**
	 * @deprecated in favor of {@link #likePost(int, String)}
	 * @param commentId
	 * @param userId
	 * @return
	 */
	@Deprecated
	boolean likeComment(int commentId, int userId);

	boolean likeComment(int commentId, String username);

	/**
	 * @deprecated in favor of {@link #unlikePost(int, String)}
	 * @param commentId
	 * @param userId
	 * @return
	 */
	@Deprecated
	boolean unlikeComment(int commentId, int userId);

	boolean unlikeComment(int commentId, String username);
	
	Collection<String> getFollowerUsernames(String username);
	
	Collection<String> getFollowingUsernames(String username);
	
	boolean followUser(Integer followerUserId, String followerUsername, Integer followingUserId, String followingUsername);

	boolean unfollowUser(Integer followerUserId, String followerUsername, Integer followingUserId, String followingUsername);
}
