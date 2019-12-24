package com.jms.socialmedia.mybatis;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Collections;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class SqlSessionFollowersMapperTest {

	@Mock
	private SqlSession sqlSession;
	@Mock
	private SqlSessionFactory sqlSessionFactory;
	@Mock
	private FollowersMapper followersMapper;

	private SqlSessionFollowersMapper sqlSessionFollowersMapper;

	@Before
	public void setUp() {
		initMocks(this);
		sqlSessionFollowersMapper = new SqlSessionFollowersMapper(sqlSessionFactory);
		when(sqlSessionFactory.openSession(true)).thenReturn(sqlSession);
		when(sqlSession.getMapper(FollowersMapper.class)).thenReturn(followersMapper);
	}

	@After
	public void tearDown() {
		verify(sqlSessionFactory, times(1)).openSession(true);
		verify(sqlSession, times(1)).getMapper(FollowersMapper.class);
		verify(sqlSession, times(1)).close();
		verifyNoMoreInteractions(followersMapper, sqlSessionFactory, sqlSession);
	}

	@Test
	public void testGetFollowerUsernames() {
		Collection<String> followerUsernames = Collections.singleton("A Follower");
		String username = "Username";
		
		when(followersMapper.getFollowerUsernames(username)).thenReturn(followerUsernames);
		assertThat(sqlSessionFollowersMapper.getFollowerUsernames(username), is(followerUsernames));
		verify(followersMapper, times(1)).getFollowerUsernames(username);
	}

	@Test
	public void testGetFollowingUsernames() {
		Collection<String> followingUsernames = Collections.singleton("A User I Follow");
		String username = "Username";
		
		when(followersMapper.getFollowerUsernames(username)).thenReturn(followingUsernames);
		assertThat(sqlSessionFollowersMapper.getFollowerUsernames(username), is(followingUsernames));
		verify(followersMapper, times(1)).getFollowerUsernames(username);
	}

	@Test
	public void testFollowUser() {
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Follow";

		when(followersMapper.followUser(followerUserId, followerUsername, followingUserId, followingUsername)).thenReturn(1);
		assertThat(sqlSessionFollowersMapper.followUser(followerUserId, followerUsername, followingUserId, followingUsername), is(1));
		verify(followersMapper, times(1)).followUser(followerUserId, followerUsername, followingUserId, followingUsername);
	}

	@Test
	public void testUnfollowUser() {
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Unfollow";

		when(followersMapper.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername)).thenReturn(1);
		assertThat(sqlSessionFollowersMapper.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername), is(1));
		verify(followersMapper, times(1)).unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername);
	}

	@Test
	public void testGetUsernamesToFollow() {
		Collection<String> usernamesToFollow = Collections.singletonList("A Username To Follow");
		String username = "Username";

		when(followersMapper.getUsernamesToFollow(username)).thenReturn(usernamesToFollow);
		assertThat(sqlSessionFollowersMapper.getUsernamesToFollow(username), is(usernamesToFollow));
		verify(followersMapper, times(1)).getUsernamesToFollow(username);
	}

}
