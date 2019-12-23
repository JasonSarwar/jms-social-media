package com.jms.socialmedia.dataservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
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
		verifyNoMoreInteractions(dataService, cachingService);
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
		// TODO
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
		verifyNoMoreInteractions(dataService, cachingService);
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
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testLikePostIntInt() {
		int postId = 4;
		int userId = 6;
		when(dataService.likePost(postId, userId)).thenReturn(true);
		assertThat(cachingDataService.likePost(postId, userId), is(true));
		verify(dataService, times(1)).likePost(postId, userId);
		verify(cachingService, times(1)).removePostFromCache(postId);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testLikePostIntString() {
		int postId = 4;
		String username = "Username";
		when(dataService.likePost(postId, username)).thenReturn(true);
		assertThat(cachingDataService.likePost(postId, username), is(true));
		verify(dataService, times(1)).likePost(postId, username);
		verify(cachingService, times(1)).likePostInCache(postId, username);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testUnlikePostIntInt() {
		int postId = 4;
		int userId = 6;
		when(dataService.unlikePost(postId, userId)).thenReturn(true);
		assertThat(cachingDataService.unlikePost(postId, userId), is(true));
		verify(dataService, times(1)).unlikePost(postId, userId);
		verify(cachingService, times(1)).removePostFromCache(postId);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testUnlikePostIntString() {
		int postId = 4;
		String username = "Username";
		when(dataService.unlikePost(postId, username)).thenReturn(true);
		assertThat(cachingDataService.unlikePost(postId, username), is(true));
		verify(dataService, times(1)).unlikePost(postId, username);
		verify(cachingService, times(1)).unlikePostInCache(postId, username);
		verifyNoMoreInteractions(dataService, cachingService);
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
		verifyNoMoreInteractions(dataService, cachingService);
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
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testLikeCommentIntInt() {
		int commentId = 4;
		int userId = 6;
		when(dataService.likeComment(commentId, userId)).thenReturn(true);
		assertThat(cachingDataService.likeComment(commentId, userId), is(true));
		verify(dataService, times(1)).likeComment(commentId, userId);
		verify(cachingService, times(1)).likeCommentInCache(commentId, userId);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testLikeCommentIntString() {
		int commentId = 4;
		String username = "Username";
		when(dataService.likeComment(commentId, username)).thenReturn(true);
		assertThat(cachingDataService.likeComment(commentId, username), is(true));
		verify(dataService, times(1)).likeComment(commentId, username);
		verify(cachingService, times(1)).likeCommentInCache(commentId, username);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testUnlikeCommentIntInt() {
		int commentId = 4;
		int userId = 6;
		when(dataService.unlikeComment(commentId, userId)).thenReturn(true);
		assertThat(cachingDataService.unlikeComment(commentId, userId), is(true));
		verify(dataService, times(1)).unlikeComment(commentId, userId);
		verify(cachingService, times(1)).unlikeCommentInCache(commentId, userId);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testUnlikeCommentIntString() {
		int commentId = 4;
		String username = "Username";
		when(dataService.unlikeComment(commentId, username)).thenReturn(true);
		assertThat(cachingDataService.unlikeComment(commentId, username), is(true));
		verify(dataService, times(1)).unlikeComment(commentId, username);
		verify(cachingService, times(1)).unlikeCommentInCache(commentId, username);
		verifyNoMoreInteractions(dataService, cachingService);
	}

	@Test
	public void testGetFollowerUsernames() {
		// TODO
	}

	@Test
	public void testGetFollowingUsernames() {
		// TODO
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
		verifyNoMoreInteractions(dataService, cachingService);
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
		verifyNoMoreInteractions(dataService, cachingService);
	}
}
