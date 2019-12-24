package com.jms.socialmedia.dataservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jms.socialmedia.model.Post;
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

	@After
	public void tearDown() {
		verifyNoMoreInteractions(usersMapper, postsMapper, commentsMapper, tagsMapper, followersMapper);
	}

	@Test
	public void testGetUserIdByUsername() {
		// TODO
	}

	@Test
	public void testGetUserPageInfoByName() {
		// TODO
	}

	@Test
	public void testGetUserLoginInfoByString() {
		// TODO
	}

	@Test
	public void testGetHashedPasswordByUserId() {
		// TODO
	}

	@Test
	public void testGetUsernamesToFollow() {
		Collection<String> usernamesToFollow = Collections.singleton("A Username To Follow");
		String username = "My_Username";
		when(followersMapper.getUsernamesToFollow(username)).thenReturn(usernamesToFollow);
		assertThat(mybatisDataService.getUsernamesToFollow(username), is(usernamesToFollow));
		verify(followersMapper, times(1)).getUsernamesToFollow(username);
	}

	@Test
	public void testIsUsernameTaken() {
		// TODO
	}

	@Test
	public void testIsEmailTaken() {
		// TODO
	}

	@Test
	public void testAddUser() {
		// TODO
	}

	@Test
	public void testEditPassword() {
		// TODO
	}

	@Test
	public void testAddUserSession() {
		// TODO
	}

	@Test
	public void testGetUserBySessionId() {
		// TODO
	}

	@Test
	public void testRemoveSessionId() {
		// TODO
	}

	@Test
	public void testGetPosts() {

		Collection<Integer> userIds = Set.of(1, 2, 3, 4, 5);
		Collection<String> usernames = Set.of("Pete", "Joe");
		String tag = "tag";
		String onDate = "2019-12-23";
		String beforeDate = "2019-12-24";
		String afterDate = "2019-12-22";
		int sincePostId = 1;
		String sortBy = "postId";
		boolean sortOrderAsc = true;
		
		Post post1 = new Post(1);
		Post post2 = new Post(2);
		Post post3 = new Post(3);
		Collection<Post> posts = Arrays.asList(post1, post2, post3);
		
		when(postsMapper.getPosts(userIds, usernames, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc)).thenReturn(posts);
		Collection<Post> returnedPosts = mybatisDataService.getPosts(userIds, usernames, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc);
		
		assertThat(returnedPosts, is(posts));
		
		verify(postsMapper, times(1)).getPosts(userIds, usernames, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc);
		verify(postsMapper, times(1)).getPostLikes(1);
		verify(postsMapper, times(1)).getPostLikes(2);
		verify(postsMapper, times(1)).getPostLikes(3);
	}

	@Test
	public void testGetPost() {
		// TODO
	}

	@Test
	public void testGetUserIdFromPostId() {
		// TODO
	}

	@Test
	public void testAddPost() {
		// TODO
	}

	@Test
	public void testEditPost() {
		// TODO
	}

	@Test
	public void testDeletePost() {
		// TODO
	}

	@Test
	public void testGetCommentedPostsByUserId() {
		// TODO
	}

	@Test
	public void testGetLikedPostsByUserId() {
		// TODO
	}

	@Test
	public void testGetPostLikes() {
		Collection<String> postLikes = Collections.singleton("A Person Who Liked The Post");
		int postId = 5;
		when(postsMapper.getPostLikes(postId)).thenReturn(postLikes);
		assertThat(mybatisDataService.getPostLikes(postId), is(postLikes));
		verify(postsMapper, times(1)).getPostLikes(postId);
	}

	@Test
	public void testLikePost() {
		int postId = 5;
		int userId = 10;
		when(postsMapper.likePost(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.likePost(postId, userId), is(true));
		verify(postsMapper, times(1)).likePost(postId, userId, null);
	}

	@Test
	public void testLikePost2() {
		int postId = 5;
		String username = "Username";
		when(postsMapper.likePost(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.likePost(postId, username), is(true));
		verify(postsMapper, times(1)).likePost(postId, null, username);
	}

	@Test
	public void testLikePostUnsuccessful() {
		int postId = 5;
		int userId = 10;
		assertThat(mybatisDataService.likePost(postId, userId), is(false));
		verify(postsMapper, times(1)).likePost(postId, userId, null);
	}

	@Test
	public void testLikePostUnsuccessful2() {
		int postId = 5;
		String username = "Username";
		assertThat(mybatisDataService.likePost(postId, username), is(false));
		verify(postsMapper, times(1)).likePost(postId, null, username);
	}

	@Test
	public void testUnlikePost() {
		int postId = 5;
		int userId = 10;
		when(postsMapper.unlikePost(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.unlikePost(postId, userId), is(true));
		verify(postsMapper, times(1)).unlikePost(postId, userId, null);
	}

	@Test
	public void testUnlikePost2() {
		int postId = 5;
		String username = "Username";
		when(postsMapper.unlikePost(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.unlikePost(postId, username), is(true));
		verify(postsMapper, times(1)).unlikePost(postId, null, username);
	}

	@Test
	public void testUnlikePostUnsuccessful() {
		int postId = 5;
		int userId = 10;
		assertThat(mybatisDataService.unlikePost(postId, userId), is(false));
		verify(postsMapper, times(1)).unlikePost(postId, userId, null);
	}

	@Test
	public void testUnlikePostUnsuccessful2() {
		int postId = 5;
		String username = "Username";
		assertThat(mybatisDataService.unlikePost(postId, username), is(false));
		verify(postsMapper, times(1)).unlikePost(postId, null, username);
	}

	@Test
	public void testGetComments() {
		// TODO
	}

	@Test
	public void testGetCommentsByUserId() {
		// TODO
	}

	@Test
	public void testGetComment() {
		// TODO
	}

	@Test
	public void testGetUserIdFromCommentId() {
		// TODO
	}

	@Test
	public void testAddComment() {
		// TODO
	}

	@Test
	public void testEditComment() {
		// TODO
	}

	@Test
	public void testDeleteComment() {
		// TODO
	}

	@Test
	public void testGetCommentLikes() {
		int commentId = 4;
		Collection<String> commentLikes = Collections.singletonList("A Like");
		when(commentsMapper.getCommentLikes(commentId)).thenReturn(commentLikes);
		assertThat(mybatisDataService.getCommentLikes(commentId), is(commentLikes));
		verify(commentsMapper, times(1)).getCommentLikes(commentId);
	}

	@Test
	public void testLikeComment() {
		int postId = 5;
		int userId = 10;
		when(commentsMapper.likeComment(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.likeComment(postId, userId), is(true));
		verify(commentsMapper, times(1)).likeComment(postId, userId, null);
	}

	@Test
	public void testLikeComment2() {
		int postId = 5;
		String username = "Username";
		when(commentsMapper.likeComment(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.likeComment(postId, username), is(true));
		verify(commentsMapper, times(1)).likeComment(postId, null, username);
	}

	@Test
	public void testLikeCommentUnsuccessful() {
		int postId = 5;
		int userId = 10;
		assertThat(mybatisDataService.likeComment(postId, userId), is(false));
		verify(commentsMapper, times(1)).likeComment(postId, userId, null);
	}

	@Test
	public void testLikeCommentUnsuccessful2() {
		int postId = 5;
		String username = "Username";
		assertThat(mybatisDataService.likeComment(postId, username), is(false));
		verify(commentsMapper, times(1)).likeComment(postId, null, username);
	}

	@Test
	public void testUnlikeComment() {
		int postId = 5;
		int userId = 10;
		when(commentsMapper.unlikeComment(postId, userId, null)).thenReturn(1);
		assertThat(mybatisDataService.unlikeComment(postId, userId), is(true));
		verify(commentsMapper, times(1)).unlikeComment(postId, userId, null);
	}

	@Test
	public void testUnlikeComment2() {
		int postId = 5;
		String username = "Username";
		when(commentsMapper.unlikeComment(postId, null, username)).thenReturn(1);
		assertThat(mybatisDataService.unlikeComment(postId, username), is(true));
		verify(commentsMapper, times(1)).unlikeComment(postId, null, username);
	}

	@Test
	public void testUnlikeCommentUnsuccessful() {
		int postId = 5;
		int userId = 10;
		assertThat(mybatisDataService.unlikeComment(postId, userId), is(false));
		verify(commentsMapper, times(1)).unlikeComment(postId, userId, null);
	}

	@Test
	public void testUnlikeCommentUnsuccessful2() {
		int postId = 5;
		String username = "Username";
		assertThat(mybatisDataService.unlikeComment(postId, username), is(false));
		verify(commentsMapper, times(1)).unlikeComment(postId, null, username);
	}

	@Test
	public void testGetFollowerUsernames() {
		Collection<String> followerUsernames = Collections.singleton("A Follower");
		String username = "Username";
		when(followersMapper.getFollowerUsernames(username)).thenReturn(followerUsernames);
		assertThat(mybatisDataService.getFollowerUsernames(username), is(followerUsernames));
		verify(followersMapper, times(1)).getFollowerUsernames(username);
	}

	@Test
	public void testGetFollowingUsernames() {
		Collection<String> followingUsernames = Collections.singleton("A User I Follow");
		String username = "Username";
		when(followersMapper.getFollowingUsernames(username)).thenReturn(followingUsernames);
		assertThat(mybatisDataService.getFollowingUsernames(username), is(followingUsernames));
		verify(followersMapper, times(1)).getFollowingUsernames(username);
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
	}

	@Test
	public void testFollowUserUnsuccessful() {
		
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Follow";

		assertThat(mybatisDataService.followUser(followerUserId, followerUsername, followingUserId, followingUsername), is(false));
		verify(followersMapper, times(1)).followUser(followerUserId, followerUsername, followingUserId, followingUsername);
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
	}

	@Test
	public void testUnfollowUserUnsuccessful() {
		
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Unfollow";

		assertThat(mybatisDataService.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername), is(false));
		verify(followersMapper, times(1)).unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername);
	}
}
