package com.jms.socialmedia.dataservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
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

import com.jms.socialmedia.cache.AbstractCachingService;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

public class CachingDataServiceTest {
	
	@Mock
	private DataService dataService;
	@Mock
	private AbstractCachingService cachingService;
	
	private CachingDataService cachingDataService;

	@Before
	public void setUp() {
		initMocks(this);
		cachingDataService = new CachingDataService(dataService, cachingService);
	}

	@After
	public void tearDown() {
		verifyNoMoreInteractions(dataService, cachingService);
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
		Collection<String> usernamesToFollow = Collections.singletonList("A Username To Follow");
		String username = "Username";
		when(dataService.getUsernamesToFollow(username)).thenReturn(usernamesToFollow);
		assertThat(cachingDataService.getUsernamesToFollow(username), is(usernamesToFollow));
		verify(dataService, times(1)).getUsernamesToFollow(username);
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
		Post post4 = new Post(4);
		Post post5 = new Post(5);
		Post post6 = new Post(6);
		Collection<Post> posts = Arrays.asList(post1, post2, post3, post4, post5, post6);

		when(dataService.getPosts(userIds, usernames, tag, onDate, beforeDate, afterDate, sincePostId, sortBy,
				sortOrderAsc)).thenReturn(posts);
		Collection<Post> returnedPosts = cachingDataService.getPosts(userIds, usernames, tag, onDate, beforeDate,
				afterDate, sincePostId, sortBy, sortOrderAsc);

		assertThat(returnedPosts, is(posts));

		verify(dataService, times(1)).getPosts(userIds, usernames, tag, onDate, beforeDate, afterDate, sincePostId,
				sortBy, sortOrderAsc);
		verify(cachingService, times(1)).putPostIntoCache(post1);
		verify(cachingService, times(1)).putPostIntoCache(post2);
		verify(cachingService, times(1)).putPostIntoCache(post3);
		verify(cachingService, times(1)).putPostIntoCache(post4);
		verify(cachingService, times(1)).putPostIntoCache(post5);
		verify(cachingService, never()).putPostIntoCache(post6);
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
		int postId = 4;
		Collection<String> postLikes = Collections.singletonList("A Like");
		when(dataService.getPostLikes(postId)).thenReturn(postLikes);
		assertThat(cachingDataService.getPostLikes(postId), is(postLikes));
		verify(cachingService, times(1)).getPostFromCache(postId);
		verify(dataService, times(1)).getPostLikes(postId);
	}

	@Test
	public void testGetPostLikesPostInCache() {
		int postId = 4;
		Collection<String> postLikes = Collections.singleton("A Like");
		Post postInCache = new Post(postId);
		postInCache.addLike("A Like");
		when(cachingService.getPostFromCache(postId)).thenReturn(postInCache);
		assertThat(cachingDataService.getPostLikes(postId), is(postLikes));
		verify(cachingService, times(1)).getPostFromCache(postId);
	}

	@Test
	public void testLikePostIntInt() {
		int postId = 4;
		int userId = 6;
		when(dataService.likePost(postId, userId)).thenReturn(true);
		assertThat(cachingDataService.likePost(postId, userId), is(true));
		verify(dataService, times(1)).likePost(postId, userId);
		verify(cachingService, times(1)).removePostFromCache(postId);
	}

	@Test
	public void testLikePostIntString() {
		int postId = 4;
		String username = "Username";
		when(dataService.likePost(postId, username)).thenReturn(true);
		assertThat(cachingDataService.likePost(postId, username), is(true));
		verify(dataService, times(1)).likePost(postId, username);
		verify(cachingService, times(1)).likePostInCache(postId, username);
	}

	@Test
	public void testUnlikePostIntInt() {
		int postId = 4;
		int userId = 6;
		when(dataService.unlikePost(postId, userId)).thenReturn(true);
		assertThat(cachingDataService.unlikePost(postId, userId), is(true));
		verify(dataService, times(1)).unlikePost(postId, userId);
		verify(cachingService, times(1)).removePostFromCache(postId);
	}

	@Test
	public void testUnlikePostIntString() {
		int postId = 4;
		String username = "Username";
		when(dataService.unlikePost(postId, username)).thenReturn(true);
		assertThat(cachingDataService.unlikePost(postId, username), is(true));
		verify(dataService, times(1)).unlikePost(postId, username);
		verify(cachingService, times(1)).unlikePostInCache(postId, username);
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
		when(dataService.getCommentLikes(commentId)).thenReturn(commentLikes);
		assertThat(cachingDataService.getCommentLikes(commentId), is(commentLikes));
		verify(cachingService, times(1)).getCommentFromCache(commentId);
		verify(dataService, times(1)).getCommentLikes(commentId);
	}

	@Test
	public void testGetCommentLikesCommentInCache() {
		int commentId = 4;
		Collection<String> commentLikes = Collections.singleton("A Like");
		Comment commentInCache = new Comment(commentId);
		commentInCache.addLike("A Like");
		when(cachingService.getCommentFromCache(commentId)).thenReturn(commentInCache);
		assertThat(cachingDataService.getCommentLikes(commentId), is(commentLikes));
		verify(cachingService, times(1)).getCommentFromCache(commentId);
	}

	@Test
	public void testLikeCommentIntInt() {
		int commentId = 4;
		int userId = 6;
		when(dataService.likeComment(commentId, userId)).thenReturn(true);
		assertThat(cachingDataService.likeComment(commentId, userId), is(true));
		verify(dataService, times(1)).likeComment(commentId, userId);
		verify(cachingService, times(1)).likeCommentInCache(commentId, userId);
	}

	@Test
	public void testLikeCommentIntString() {
		int commentId = 4;
		String username = "Username";
		when(dataService.likeComment(commentId, username)).thenReturn(true);
		assertThat(cachingDataService.likeComment(commentId, username), is(true));
		verify(dataService, times(1)).likeComment(commentId, username);
		verify(cachingService, times(1)).likeCommentInCache(commentId, username);
	}

	@Test
	public void testUnlikeCommentIntInt() {
		int commentId = 4;
		int userId = 6;
		when(dataService.unlikeComment(commentId, userId)).thenReturn(true);
		assertThat(cachingDataService.unlikeComment(commentId, userId), is(true));
		verify(dataService, times(1)).unlikeComment(commentId, userId);
		verify(cachingService, times(1)).unlikeCommentInCache(commentId, userId);
	}

	@Test
	public void testUnlikeCommentIntString() {
		int commentId = 4;
		String username = "Username";
		when(dataService.unlikeComment(commentId, username)).thenReturn(true);
		assertThat(cachingDataService.unlikeComment(commentId, username), is(true));
		verify(dataService, times(1)).unlikeComment(commentId, username);
		verify(cachingService, times(1)).unlikeCommentInCache(commentId, username);
	}

	@Test
	public void testGetFollowerUsernames() {
		Collection<String> followerUsernames = Collections.singleton("A Follower");
		String username = "Username";
		when(dataService.getFollowerUsernames(username)).thenReturn(followerUsernames);
		assertThat(cachingDataService.getFollowerUsernames(username), is(followerUsernames));
		verify(dataService, times(1)).getFollowerUsernames(username);
	}

	@Test
	public void testGetFollowingUsernames() {
		Collection<String> followingUsernames = Collections.singleton("A User I Follow");
		String username = "Username";
		when(dataService.getFollowingUsernames(username)).thenReturn(followingUsernames);
		assertThat(cachingDataService.getFollowingUsernames(username), is(followingUsernames));
		verify(dataService, times(1)).getFollowingUsernames(username);
	}

	@Test
	public void testFollowUser() {
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Follow";

		when(dataService.followUser(followerUserId, followerUsername, followingUserId, followingUsername)).thenReturn(true);
		assertThat(cachingDataService.followUser(followerUserId, followerUsername, followingUserId, followingUsername), is(true));
		verify(dataService, times(1)).followUser(followerUserId, followerUsername, followingUserId, followingUsername);
	}

	@Test
	public void testUnfollowUser() {
		int followerUserId = 5;
		String followerUsername = "Me";
		int followingUserId = 11;
		String followingUsername = "A Person I Want To Unfollow";

		when(dataService.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername)).thenReturn(true);
		assertThat(cachingDataService.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername), is(true));
		verify(dataService, times(1)).unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername);
	}
}
