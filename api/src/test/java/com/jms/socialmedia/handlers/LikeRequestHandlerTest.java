package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.routes.LocalDateTypeAdapter;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class LikeRequestHandlerTest {

	private static final String USER_ID_PARAM = "userId";
	private static final String POST_ID_PARAM = "postId";
	private static final String COMMENT_ID_PARAM = "commentId";
	private static final String AUTHORIZATION = "Authorization";

	@Mock
	private DataService dataService;
	@Mock
	private TokenService tokenService;
	@Mock
	private Request request;
	@Mock
	private Response response;

	private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

	private LikeRequestHandler likeRequestHandler;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		likeRequestHandler = new LikeRequestHandler(dataService, tokenService, gson);
	}

	@Test
	public void testHandleGetLikedPosts() {
		Post post1 = new Post(1, 4, "Jason", "Jason Sarwar", "A cool post!", LocalDateTime.now());
		post1.addLike(3);
		Post post2 = new Post(2, 4, "Jason", "Jason Sarwar", "Another cool post!", LocalDateTime.now());
		post2.addLike(3);
		Collection<Post> posts = Set.of(post1, post2);

		when(request.params(USER_ID_PARAM)).thenReturn("3");
		when(dataService.getLikedPostsByUserId(3)).thenReturn(posts);

		Collection<Post> retrievedPosts = likeRequestHandler.handleGetLikedPosts(request, response);
		assertThat(retrievedPosts, is(posts));
		verify(request, times(1)).params(USER_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getLikedPostsByUserId(3);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
	}

	@Test
	public void testHandleGetPostLikes() {
		Collection<Integer> postLikes = Set.of(1, 2, 3, 4, 5);
		when(request.params(POST_ID_PARAM)).thenReturn("27");
		when(dataService.getPostLikes(27)).thenReturn(postLikes);

		Collection<Integer> retrievedPostLikes = likeRequestHandler.handleGetPostLikes(request, response);
		assertThat(retrievedPostLikes, is(postLikes));
		verify(request, times(1)).params(POST_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getPostLikes(27);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
	}

	@Test
	public void testHandleLikePost() throws IOException {
		Token token = Token.newBuilder().setUserId(27).addPermissions(Permission.LIKE_POST).build();

		when(request.params(POST_ID_PARAM)).thenReturn("4");
		when(request.params(USER_ID_PARAM)).thenReturn("27");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.likePost(4, 27)).thenReturn(true);

		boolean likedPost = likeRequestHandler.handleLikePost(request, response);
		assertThat(likedPost, is(true));
		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).likePost(4, 27);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleUnlikePost() throws IOException {
		Token token = Token.newBuilder().setUserId(27).addPermissions(Permission.UNLIKE_POST).build();

		when(request.params(POST_ID_PARAM)).thenReturn("4");
		when(request.params(USER_ID_PARAM)).thenReturn("27");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.unlikePost(4, 27)).thenReturn(true);

		boolean unlikedPost = likeRequestHandler.handleUnlikePost(request, response);
		assertThat(unlikedPost, is(true));
		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).unlikePost(4, 27);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleGetCommentLikes() {
		Collection<Integer> commentLikes = Set.of(1, 2, 3, 4, 5);
		when(request.params(COMMENT_ID_PARAM)).thenReturn("6");
		when(dataService.getCommentLikes(6)).thenReturn(commentLikes);

		Collection<Integer> retrievedCommentLikes = likeRequestHandler.handleGetCommentLikes(request, response);
		assertThat(retrievedCommentLikes, is(commentLikes));
		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getCommentLikes(6);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
	}

	@Test
	public void testHandleLikeComment() throws IOException {
		Token token = Token.newBuilder().setUserId(27).addPermissions(Permission.LIKE_COMMENT).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("5");
		when(request.params(USER_ID_PARAM)).thenReturn("27");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.likeComment(5, 27)).thenReturn(true);

		boolean likedComment = likeRequestHandler.handleLikeComment(request, response);
		assertThat(likedComment, is(true));
		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).likeComment(5, 27);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleUnlikeComment() throws IOException {
		Token token = Token.newBuilder().setUserId(27).addPermissions(Permission.UNLIKE_COMMENT).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("5");
		when(request.params(USER_ID_PARAM)).thenReturn("27");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.unlikeComment(5, 27)).thenReturn(true);

		boolean unlikedComment = likeRequestHandler.handleUnlikeComment(request, response);
		assertThat(unlikedComment, is(true));
		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).params(USER_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).unlikeComment(5, 27);
		verifyNoMoreInteractions(dataService);
	}

}
