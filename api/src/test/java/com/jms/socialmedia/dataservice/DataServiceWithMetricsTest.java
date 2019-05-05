package com.jms.socialmedia.dataservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataServiceWithMetricsTest {

	@Mock
	private DataService dataService;

	private MetricRegistry metricRegistry;

	private DataServiceWithMetrics dataServiceWithMetrics;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		metricRegistry = new MetricRegistry();
		dataServiceWithMetrics = new DataServiceWithMetrics(dataService, metricRegistry, "test");
	}

	@Test
	public void testGetUserIdByUsername() {
		Timer timer = metricRegistry.timer("test.getUserIdByUsername");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUserIdByUsername(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUserIdByUsername(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUserPageInfoByName() {
		Timer timer = metricRegistry.timer("test.getUserPageInfoByName");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUserPageInfoByName(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUserPageInfoByName(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUserLoginInfoByString() {
		Timer timer = metricRegistry.timer("test.getUserLoginInfoByString");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUserLoginInfoByString(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUserLoginInfoByString(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetHashedPasswordByUserId() {
		Timer timer = metricRegistry.timer("test.getHashedPasswordByUserId");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getHashedPasswordByUserId(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getHashedPasswordByUserId(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUsernamesByIds() {
		Timer timer = metricRegistry.timer("test.getUsernamesByIds");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUsernamesByIds(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUsernamesByIds(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUsersToFollow() {
		Timer timer = metricRegistry.timer("test.getUsersToFollow");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUsersToFollow(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUsersToFollow(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testIsUsernameTaken() {
		Timer timer = metricRegistry.timer("test.isUsernameTaken");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.isUsernameTaken("username");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.isUsernameTaken("username");
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testIsEmailTaken() {
		Timer timer = metricRegistry.timer("test.isEmailTaken");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.isEmailTaken("email@address.com");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.isEmailTaken("email@address.com");
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testAddUser() {
		Timer timer = metricRegistry.timer("test.addUser");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.addUser(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.addUser(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testEditPassword() {
		Timer timer = metricRegistry.timer("test.editPassword");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.editPassword(1, "hashedPassword");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.editPassword(1, "hashedPassword");
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUserBySessionKey() {
		Timer timer = metricRegistry.timer("test.getUserBySessionKey");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUserBySessionKey(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUserBySessionKey(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testAddUserSession() {
		Timer timer = metricRegistry.timer("test.addUserSession");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.addUserSession(1, null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.addUserSession(1, null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testRemoveSessionKey() {
		Timer timer = metricRegistry.timer("test.removeSessionKey");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.removeSessionKey(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.removeSessionKey(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetPosts() {
		Timer timer = metricRegistry.timer("test.getPosts");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getPosts(null, null, null, null, null, null, null, null, false);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getPosts(null, null, null, null, null, null, null, null, false);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetPost() {
		Timer timer = metricRegistry.timer("test.getPost");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getPost(5);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getPost(5);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUserIdFromPostId() {
		Timer timer = metricRegistry.timer("test.getUserIdFromPostId");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUserIdFromPostId(2);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUserIdFromPostId(2);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testAddPost() {
		Timer timer = metricRegistry.timer("test.addPost");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.addPost(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.addPost(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testEditPost() {
		Timer timer = metricRegistry.timer("test.editPost");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.editPost(1, "text");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.editPost(2, "text");
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testDeletePost() {
		Timer timer = metricRegistry.timer("test.deletePost");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.deletePost(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.deletePost(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetCommentedPostsByUserId() {
		Timer timer = metricRegistry.timer("test.getCommentedPostsByUserId");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getCommentedPostsByUserId(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getCommentedPostsByUserId(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetLikedPostsByUserId() {
		Timer timer = metricRegistry.timer("test.getLikedPostsByUserId");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getLikedPostsByUserId(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getLikedPostsByUserId(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetPostLikes() {
		Timer timer = metricRegistry.timer("test.getPostLikes");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getPostLikes(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getPostLikes(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testLikePost() {
		Timer timer = metricRegistry.timer("test.likePost");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.likePost(1, 2);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.likePost(1, 2);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testUnlikePost() {
		Timer timer = metricRegistry.timer("test.unlikePost");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.unlikePost(1, 2);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.unlikePost(1, 2);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetComments() {
		Timer timer = metricRegistry.timer("test.getComments");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getComments(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getComments(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetCommentsByUserId() {
		Timer timer = metricRegistry.timer("test.getCommentsByUserId");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getCommentsByUserId(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getCommentsByUserId(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetComment() {
		Timer timer = metricRegistry.timer("test.getComment");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getComment(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getComment(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUserIdFromCommentId() {
		Timer timer = metricRegistry.timer("test.getUserIdFromCommentId");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getUserIdFromCommentId(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getUserIdFromCommentId(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testAddComment() {
		Timer timer = metricRegistry.timer("test.addComment");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.addComment(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.addComment(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testEditComment() {
		Timer timer = metricRegistry.timer("test.editComment");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.editComment(1, "text");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.editComment(2, "text");
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testDeleteComment() {
		Timer timer = metricRegistry.timer("test.deleteComment");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.deleteComment(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.deleteComment(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetCommentLikes() {
		Timer timer = metricRegistry.timer("test.getCommentLikes");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getCommentLikes(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getCommentLikes(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testLikeComment() {
		Timer timer = metricRegistry.timer("test.likeComment");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.likeComment(1, 2);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.likeComment(1, 2);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testUnlikeComment() {
		Timer timer = metricRegistry.timer("test.unlikeComment");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.unlikeComment(1, 2);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.unlikeComment(1, 2);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetFollowerUserIds() {
		Timer timer = metricRegistry.timer("test.getFollowerUserIds");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getFollowerUserIds(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getFollowerUserIds(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetFollowingUserIds() {
		Timer timer = metricRegistry.timer("test.getFollowingUserIds");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.getFollowingUserIds(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.getFollowingUserIds(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testFollowUser() {
		Timer timer = metricRegistry.timer("test.followUser");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.followUser(1, 5);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.followUser(1, 5);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testUnfollowUser() {
		Timer timer = metricRegistry.timer("test.unfollowUser");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		dataServiceWithMetrics.unfollowUser(1, 5);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		dataServiceWithMetrics.unfollowUser(1, 5);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

}
