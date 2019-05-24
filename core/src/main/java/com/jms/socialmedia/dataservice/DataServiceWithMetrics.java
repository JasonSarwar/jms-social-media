package com.jms.socialmedia.dataservice;

import java.util.Collection;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

public class DataServiceWithMetrics implements DataService {

	private final DataService dataService;
	private final Timer getUserIdByUsernameTimer;
	private final Timer getUserPageInfoByNameTimer;
	private final Timer getUserLoginInfoByStringTimer;
	private final Timer getHashedPasswordByUserIdTimer;
	private final Timer getUsernamesByIdsTimer;
	private final Timer getUsersToFollowTimer;
	private final Timer isUsernameTakenTimer;
	private final Timer isEmailTakenTimer;
	private final Timer addUserTimer;
	private final Timer editPasswordTimer;
	private final Timer addUserSessionTimer;
	private final Timer getUserBySessionIdTimer;
	private final Timer removeSessionIdTimer;
	private final Timer getPostsTimer;
	private final Timer getPostTimer;
	private final Timer getUserIdFromPostIdTimer;
	private final Timer addPostTimer;
	private final Timer editPostTimer;
	private final Timer deletePostTimer;
	private final Timer getCommentedPostsByUserIdTimer;
	private final Timer getLikedPostsByUserIdTimer;
	private final Timer getPostLikesTimer;
	private final Timer likePostTimer;
	private final Timer unlikePostTimer;
	private final Timer getCommentsTimer;
	private final Timer getCommentsByUserIdTimer;
	private final Timer getCommentTimer;
	private final Timer getUserIdFromCommentIdTimer;
	private final Timer addCommentTimer;
	private final Timer editCommentTimer;
	private final Timer deleteCommentTimer;
	private final Timer getCommentLikesTimer;
	private final Timer likeCommentTimer;
	private final Timer unlikeCommentTimer;
	private final Timer getFollowerUserIdsTimer;
	private final Timer getFollowingUserIdsTimer;
	private final Timer followUserTimer;
	private final Timer unfollowUserTimer;

	public DataServiceWithMetrics(DataService dataService, MetricRegistry metricRegistry) {
		this(dataService, metricRegistry, dataService.getClass().getSimpleName());
	}

	public DataServiceWithMetrics(DataService dataService, MetricRegistry metricRegistry, String metricsName) {
		this.dataService = dataService;
		this.getUserIdByUsernameTimer = metricRegistry.timer(metricsName + ".getUserIdByUsername");
		this.getUserPageInfoByNameTimer = metricRegistry.timer(metricsName + ".getUserPageInfoByName");
		this.getUserLoginInfoByStringTimer = metricRegistry.timer(metricsName + ".getUserLoginInfoByString");
		this.getHashedPasswordByUserIdTimer = metricRegistry.timer(metricsName + ".getHashedPasswordByUserId");
		this.getUsernamesByIdsTimer = metricRegistry.timer(metricsName + ".getUsernamesByIds");
		this.getUsersToFollowTimer = metricRegistry.timer(metricsName + ".getUsersToFollow");
		this.isUsernameTakenTimer = metricRegistry.timer(metricsName + ".isUsernameTaken");
		this.isEmailTakenTimer = metricRegistry.timer(metricsName + ".isEmailTaken");
		this.addUserTimer = metricRegistry.timer(metricsName + ".addUser");
		this.editPasswordTimer = metricRegistry.timer(metricsName + ".editPassword");
		this.addUserSessionTimer = metricRegistry.timer(metricsName + ".addUserSession");
		this.getUserBySessionIdTimer = metricRegistry.timer(metricsName + ".getUserBySessionId");
		this.removeSessionIdTimer = metricRegistry.timer(metricsName + ".removeSessionId");
		this.getPostsTimer = metricRegistry.timer(metricsName + ".getPosts");
		this.getPostTimer = metricRegistry.timer(metricsName + ".getPost");
		this.getUserIdFromPostIdTimer = metricRegistry.timer(metricsName + ".getUserIdFromPostId");
		this.addPostTimer = metricRegistry.timer(metricsName + ".addPost");
		this.editPostTimer = metricRegistry.timer(metricsName + ".editPost");
		this.deletePostTimer = metricRegistry.timer(metricsName + ".deletePost");
		this.getCommentedPostsByUserIdTimer = metricRegistry.timer(metricsName + ".getCommentedPostsByUserId");
		this.getLikedPostsByUserIdTimer = metricRegistry.timer(metricsName + ".getLikedPostsByUserId");
		this.getPostLikesTimer = metricRegistry.timer(metricsName + ".getPostLikes");
		this.likePostTimer = metricRegistry.timer(metricsName + ".likePost");
		this.unlikePostTimer = metricRegistry.timer(metricsName + ".unlikePost");
		this.getCommentsTimer = metricRegistry.timer(metricsName + ".getComments");
		this.getCommentsByUserIdTimer = metricRegistry.timer(metricsName + ".getCommentsByUserId");
		this.getCommentTimer = metricRegistry.timer(metricsName + ".getComment");
		this.getUserIdFromCommentIdTimer = metricRegistry.timer(metricsName + ".getUserIdFromCommentId");
		this.addCommentTimer = metricRegistry.timer(metricsName + ".addComment");
		this.editCommentTimer = metricRegistry.timer(metricsName + ".editComment");
		this.deleteCommentTimer = metricRegistry.timer(metricsName + ".deleteComment");
		this.getCommentLikesTimer = metricRegistry.timer(metricsName + ".getCommentLikes");
		this.likeCommentTimer = metricRegistry.timer(metricsName + ".likeComment");
		this.unlikeCommentTimer = metricRegistry.timer(metricsName + ".unlikeComment");
		this.getFollowerUserIdsTimer = metricRegistry.timer(metricsName + ".getFollowerUserIds");
		this.getFollowingUserIdsTimer = metricRegistry.timer(metricsName + ".getFollowingUserIds");
		this.followUserTimer = metricRegistry.timer(metricsName + ".followUser");
		this.unfollowUserTimer = metricRegistry.timer(metricsName + ".unfollowUser");
	}

	@Override
	public Integer getUserIdByUsername(String username) {
		try (Timer.Context context = getUserIdByUsernameTimer.time()) {
			return dataService.getUserIdByUsername(username);
		}
	}

	@Override
	public UserPage getUserPageInfoByName(String username) {
		try (Timer.Context context = getUserPageInfoByNameTimer.time()) {
			return dataService.getUserPageInfoByName(username);
		}
	}

	@Override
	public User getUserLoginInfoByString(String usernameOrEmail) {
		try (Timer.Context context = getUserLoginInfoByStringTimer.time()) {
			return dataService.getUserLoginInfoByString(usernameOrEmail);
		}
	}

	@Override
	public User getHashedPasswordByUserId(Integer userId) {
		try (Timer.Context context = getHashedPasswordByUserIdTimer.time()) {
			return dataService.getHashedPasswordByUserId(userId);
		}
	}

	@Override
	public Collection<User> getUsernamesByIds(Collection<Integer> userIds) {
		try (Timer.Context context = getUsernamesByIdsTimer.time()) {
			return dataService.getUsernamesByIds(userIds);
		}
	}

	@Override
	public Collection<User> getUsersToFollow(int userId) {
		try (Timer.Context context = getUsersToFollowTimer.time()) {
			return dataService.getUsersToFollow(userId);
		}
	}

	@Override
	public boolean isUsernameTaken(String username) {
		try (Timer.Context context = isUsernameTakenTimer.time()) {
			return dataService.isUsernameTaken(username);
		}
	}

	@Override
	public boolean isEmailTaken(String email) {
		try (Timer.Context context = isEmailTakenTimer.time()) {
			return dataService.isEmailTaken(email);
		}
	}

	@Override
	public boolean addUser(NewUser newUser) {
		try (Timer.Context context = addUserTimer.time()) {
			return dataService.addUser(newUser);
		}
	}

	@Override
	public boolean editPassword(Integer userId, String hashedPassword) {
		try (Timer.Context context = editPasswordTimer.time()) {
			return dataService.editPassword(userId, hashedPassword);
		}
	}

	@Override
	public User getUserBySessionId(String sessionId) {
		try (Timer.Context context = getUserBySessionIdTimer.time()) {
			return dataService.getUserBySessionId(sessionId);
		}
	}

	@Override
	public boolean addUserSession(int userId, String sessionId) {
		try (Timer.Context context = addUserSessionTimer.time()) {
			return dataService.addUserSession(userId, sessionId);
		}
	}

	@Override
	public void removeSessionId(String sessionId) {
		try (Timer.Context context = removeSessionIdTimer.time()) {
			dataService.removeSessionId(sessionId);
		}
	}

	@Override
	public Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate,
			String beforeDate, String afterDate, Integer sincePostId, String sortBy, boolean sortOrderAsc) {

		try (Timer.Context context = getPostsTimer.time()) {
			return dataService.getPosts(userIds, username, tag, onDate, beforeDate, afterDate, 
					sincePostId, sortBy, sortOrderAsc);
		}
	}

	@Override
	public Post getPost(int postId) {
		try (Timer.Context context = getPostTimer.time()) {
			return dataService.getPost(postId);
		}
	}

	@Override
	public Integer getUserIdFromPostId(int postId) {
		try (Timer.Context context = getUserIdFromPostIdTimer.time()) {
			return dataService.getUserIdFromPostId(postId);
		}
	}

	@Override
	public boolean addPost(Post post) {
		try (Timer.Context context = addPostTimer.time()) {
			return dataService.addPost(post);
		}
	}

	@Override
	public boolean editPost(int postId, String postText) {
		try (Timer.Context context = editPostTimer.time()) {
			return dataService.editPost(postId, postText);
		}
	}

	@Override
	public boolean deletePost(int postId) {
		try (Timer.Context context = deletePostTimer.time()) {
			return dataService.deletePost(postId);
		}
	}

	@Override
	public Collection<Post> getCommentedPostsByUserId(int userId) {
		try (Timer.Context context = getCommentedPostsByUserIdTimer.time()) {
			return dataService.getCommentedPostsByUserId(userId);
		}
	}

	@Override
	public Collection<Post> getLikedPostsByUserId(int userId) {
		try (Timer.Context context = getLikedPostsByUserIdTimer.time()) {
			return dataService.getLikedPostsByUserId(userId);
		}
	}

	@Override
	public Collection<Integer> getPostLikes(int postId) {
		try (Timer.Context context = getPostLikesTimer.time()) {
			return dataService.getPostLikes(postId);
		}
	}

	@Override
	public boolean likePost(int postId, int userId) {
		try (Timer.Context context = likePostTimer.time()) {
			return dataService.likePost(postId, userId);
		}
	}

	@Override
	public boolean unlikePost(int postId, int userId) {
		try (Timer.Context context = unlikePostTimer.time()) {
			return dataService.unlikePost(postId, userId);
		}
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		try (Timer.Context context = getCommentsTimer.time()) {
			return dataService.getComments(postId);
		}
	}

	@Override
	public Collection<Comment> getCommentsByUserId(int userId) {
		try (Timer.Context context = getCommentsByUserIdTimer.time()) {
			return dataService.getCommentsByUserId(userId);
		}
	}

	@Override
	public Comment getComment(int commentId) {
		try (Timer.Context context = getCommentTimer.time()) {
			return dataService.getComment(commentId);
		}
	}

	@Override
	public Integer getUserIdFromCommentId(int commentId) {
		try (Timer.Context context = getUserIdFromCommentIdTimer.time()) {
			return dataService.getUserIdFromCommentId(commentId);
		}
	}

	@Override
	public boolean addComment(Comment comment) {
		try (Timer.Context context = addCommentTimer.time()) {
			return dataService.addComment(comment);
		}
	}

	@Override
	public boolean editComment(int commentId, String commentText) {
		try (Timer.Context context = editCommentTimer.time()) {
			return dataService.editComment(commentId, commentText);
		}
	}

	@Override
	public boolean deleteComment(int commentId) {
		try (Timer.Context context = deleteCommentTimer.time()) {
			return dataService.deleteComment(commentId);
		}
	}

	@Override
	public Collection<Integer> getCommentLikes(int commentId) {
		try (Timer.Context context = getCommentLikesTimer.time()) {
			return dataService.getCommentLikes(commentId);
		}
	}

	@Override
	public boolean likeComment(int commentId, int userId) {
		try (Timer.Context context = likeCommentTimer.time()) {
			return dataService.likeComment(commentId, userId);
		}
	}

	@Override
	public boolean unlikeComment(int commentId, int userId) {
		try (Timer.Context context = unlikeCommentTimer.time()) {
			return dataService.unlikeComment(commentId, userId);
		}
	}

	@Override
	public Collection<Integer> getFollowerUserIds(int userId) {
		try (Timer.Context context = getFollowerUserIdsTimer.time()) {
			return dataService.getFollowerUserIds(userId);
		}
	}

	@Override
	public Collection<Integer> getFollowingUserIds(int userId) {
		try (Timer.Context context = getFollowingUserIdsTimer.time()) {
			return dataService.getFollowingUserIds(userId);
		}
	}

	@Override
	public boolean followUser(int followerUserId, int followingUserId) {
		try (Timer.Context context = followUserTimer.time()) {
			return dataService.followUser(followerUserId, followingUserId);
		}
	}

	@Override
	public boolean unfollowUser(int followerUserId, int followingUserId) {
		try (Timer.Context context = unfollowUserTimer.time()) {
			return dataService.unfollowUser(followerUserId, followingUserId);
		}
	}
}
