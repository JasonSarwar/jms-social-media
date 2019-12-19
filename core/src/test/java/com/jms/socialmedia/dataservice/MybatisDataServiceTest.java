package com.jms.socialmedia.dataservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jms.socialmedia.mybatis.CommentsMapper;
import com.jms.socialmedia.mybatis.FollowersMapper;
import com.jms.socialmedia.mybatis.PostsMapper;
import com.jms.socialmedia.mybatis.TagsMapper;
import com.jms.socialmedia.mybatis.UsersMapper;

public class MybatisDataServiceTest {

	@Mock
	private UsersMapper usersMapper;
	@Mock
	private PostsMapper postsMapper;
	@Mock
	private CommentsMapper commentsMapper;
	@Mock
	private TagsMapper tagsMapper;
	@Mock
	private FollowersMapper followersMapper;
	
	private MybatisDataService mybatisDataService;
	
	@Before
	public void setUp() {
		initMocks(this);
		mybatisDataService = new MybatisDataService(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testGetUsernamesToFollow() {
		Collection<String> usernamesToFollow = Collections.singleton("A Username To Follow");
		String username = "My_Username";
		when(followersMapper.getUsernamesToFollow(username)).thenReturn(usernamesToFollow);
		assertThat(mybatisDataService.getUsernamesToFollow(username), is(usernamesToFollow));
		verify(followersMapper, times(1)).getUsernamesToFollow(username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testGetPostLikes() {
		Collection<String> postLikes = Collections.singleton("A Person Who Liked The Post");
		int postId = 5;
		when(postsMapper.getPostLikes(postId)).thenReturn(postLikes);
		assertThat(mybatisDataService.getPostLikes(postId), is(postLikes));
		verify(postsMapper, times(1)).getPostLikes(postId);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testLikePost() {
		int postId = 5;
		int userId = 10;
		when(postsMapper.likePost(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.likePost(postId, userId), is(true));
		verify(postsMapper, times(1)).likePost(postId, userId, null);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testLikePost2() {
		int postId = 5;
		String username = "Username";
		when(postsMapper.likePost(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.likePost(postId, username), is(true));
		verify(postsMapper, times(1)).likePost(postId, null, username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testUnlikePost() {
		int postId = 5;
		int userId = 10;
		when(postsMapper.unlikePost(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.unlikePost(postId, userId), is(true));
		verify(postsMapper, times(1)).unlikePost(postId, userId, null);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testUnlikePost2() {
		int postId = 5;
		String username = "Username";
		when(postsMapper.unlikePost(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.unlikePost(postId, username), is(true));
		verify(postsMapper, times(1)).unlikePost(postId, null, username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testLikeComment() {
		int postId = 5;
		int userId = 10;
		when(commentsMapper.likeComment(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.likeComment(postId, userId), is(true));
		verify(commentsMapper, times(1)).likeComment(postId, userId, null);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testLikeComment2() {
		int postId = 5;
		String username = "Username";
		when(commentsMapper.likeComment(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.likeComment(postId, username), is(true));
		verify(commentsMapper, times(1)).likeComment(postId, null, username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testUnlikeComment() {
		int postId = 5;
		int userId = 10;
		when(commentsMapper.unlikeComment(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.unlikeComment(postId, userId), is(true));
		verify(commentsMapper, times(1)).unlikeComment(postId, userId, null);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testUnlikeComment2() {
		int postId = 5;
		String username = "Username";
		when(commentsMapper.unlikeComment(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.unlikeComment(postId, username), is(true));
		verify(commentsMapper, times(1)).unlikeComment(postId, null, username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testGetFollowerUsernames() {
		Collection<String> followerUsernames = Collections.singleton("A Follower");
		String username = "Username";
		when(followersMapper.getFollowerUsernames(username)).thenReturn(followerUsernames);
		assertThat(mybatisDataService.getFollowerUsernames(username), is(followerUsernames));
		verify(followersMapper, times(1)).getFollowerUsernames(username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testGetFollowingUsernames() {
		Collection<String> followingUsernames = Collections.singleton("A User I Follow");
		String username = "Username";
		when(followersMapper.getFollowingUsernames(username)).thenReturn(followingUsernames);
		assertThat(mybatisDataService.getFollowingUsernames(username), is(followingUsernames));
		verify(followersMapper, times(1)).getFollowingUsernames(username);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testFollowUser() {
		
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Follow";
		
		when(followersMapper.followUser(followerUserId, followerUsername, followingUserId, followingUsername)).thenReturn(1);
		assertThat(mybatisDataService.followUser(followerUserId, followerUsername, followingUserId, followingUsername), is(true));
		verify(followersMapper, times(1)).followUser(followerUserId, followerUsername, followingUserId, followingUsername);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testUnfollowUser() {
		
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Unfollow";
		
		when(followersMapper.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername)).thenReturn(1);
		assertThat(mybatisDataService.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername), is(true));
		verify(followersMapper, times(1)).unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername);
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

}
