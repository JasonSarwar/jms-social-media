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
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.exception.UnauthorizedException;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.routes.LocalDateTypeAdapter;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CommentRequestHandlerTest {

	private static final String NOT_FOUND_MESSAGE = "Comment Not Found";
	private static final String AUTHORIZATION = "Authorization";
	private static final String POST_ID_PARAM = "postId";
	private static final String COMMENT_ID_PARAM = "commentId";
	private static final String USER_ID_PARAM = "userId";
	private static final String ADD_COMMENT_REQUEST = "{\"userId\":1, \"postId\":10, \"text\":\"A Cool Comment!\"}";
	private static final String ADD_COMMENT_REQUEST_WITHOUT_POST_ID = "{\"userId\":1, \"text\":\"A Cool Comment!\"}";
	private static final String EDIT_COMMENT_REQUEST = "{\"text\":\"Editing this Comment!\"}";

	@Mock
	private DataService dataService;
	@Mock
	private TokenService tokenService;
	@Mock
	private Request request;
	@Mock
	private Response response;

	private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

	private CommentRequestHandler commentRequestHandler;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		commentRequestHandler = new CommentRequestHandler(dataService, tokenService, gson);
	}

	@Test
	public void testHandleGetComments() {

		Collection<Comment> comments = Set.of(new Comment(1, 10, "A Cool Comment", LocalDateTime.now()),
				new Comment(2, 10, "Another Cool Comment", LocalDateTime.now()));

		when(request.params(POST_ID_PARAM)).thenReturn("10");
		when(dataService.getComments(10)).thenReturn(comments);

		Collection<Comment> retrievedComments = commentRequestHandler.handleGetComments(request, response);
		assertThat(retrievedComments, is(comments));
		verify(request, times(1)).params(POST_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getComments(10);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
	}

	@Test
	public void testHandleGetCommentsByUserId() {
		Collection<Comment> comments = Set.of(
				new Comment(1, 10, 12, "Jason", "Jason Sarwar", "A Cool Comment", LocalDateTime.now()),
				new Comment(2, 10, 12, "Jason", "Jason Sarwar", "Another Cool Comment", LocalDateTime.now()));

		when(request.params(USER_ID_PARAM)).thenReturn("12");
		when(dataService.getCommentsByUserId(12)).thenReturn(comments);

		Collection<Comment> retrievedComments = commentRequestHandler.handleGetCommentsByUserId(request, response);
		assertThat(retrievedComments, is(comments));
		verify(request, times(1)).params(USER_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getCommentsByUserId(12);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
	}

	@Test
	public void testHandleGetComment() {
		Comment comment = new Comment(3, 10, "A Cool Comment", LocalDateTime.now());

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(dataService.getComment(3)).thenReturn(comment);

		Comment retrievedComment = commentRequestHandler.handleGetComment(request, response);
		assertThat(retrievedComment, is(comment));
		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verifyNoMoreInteractions(request);
		verify(dataService, times(1)).getComment(3);
		verifyNoMoreInteractions(dataService);
		verifyZeroInteractions(tokenService);
	}

	@Test
	public void testHandleGetCommentNotFound() {

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");

		try {
			commentRequestHandler.handleGetComment(request, response);
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is(NOT_FOUND_MESSAGE));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verifyNoMoreInteractions(request);
			verify(dataService, times(1)).getComment(3);
			verifyNoMoreInteractions(dataService);
			verifyZeroInteractions(tokenService);
		}
	}

	@Test
	public void testHandleAddComment() throws IOException {
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_COMMENT).build();
		Comment comment = new Comment(10, 1, "A Cool Comment!");
		when(request.body()).thenReturn(ADD_COMMENT_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.addComment(comment)).thenReturn(true);

		boolean commentAdded = commentRequestHandler.handleAddComment(request, response);
		assertThat(commentAdded, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).contentType();
		verify(request, times(1)).headers(AUTHORIZATION);
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).addComment(comment);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleAddCommentWithPostIdParam() throws IOException {
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_COMMENT).build();
		Comment comment = new Comment(10, 1, "A Cool Comment!");
		when(request.params(POST_ID_PARAM)).thenReturn("10");
		when(request.body()).thenReturn(ADD_COMMENT_REQUEST_WITHOUT_POST_ID);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.addComment(comment)).thenReturn(true);

		boolean commentAdded = commentRequestHandler.handleAddComment(request, response);
		assertThat(commentAdded, is(true));

		verify(request, times(1)).params(POST_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).contentType();
		verify(request, times(1)).headers(AUTHORIZATION);
		verifyNoMoreInteractions(request);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).addComment(comment);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleAddCommentWithoutPostId() throws IOException {

		when(request.body()).thenReturn(ADD_COMMENT_REQUEST_WITHOUT_POST_ID);

		try {
			commentRequestHandler.handleAddComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Add Comment Request requires a 'postId'"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).contentType();
			verify(request, times(1)).body();
			verifyNoMoreInteractions(request);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddCommentWithoutUserId() throws IOException {

		when(request.params(POST_ID_PARAM)).thenReturn("10");
		when(request.body()).thenReturn("{\"text\":\"A Cool Comment!\"}");

		try {
			commentRequestHandler.handleAddComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Add Comment Request requires a 'userId'"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).contentType();
			verify(request, times(1)).body();
			verifyNoMoreInteractions(request);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddCommentWithoutText() throws IOException {

		when(request.params(POST_ID_PARAM)).thenReturn("10");
		when(request.body()).thenReturn("{\"userId\":1}");

		try {
			commentRequestHandler.handleAddComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Add Comment Request requires 'text'"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).contentType();
			verify(request, times(1)).body();
			verifyNoMoreInteractions(request);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleAddCommentWithoutRequiredFields() throws IOException {

		when(request.body()).thenReturn("{}");

		try {
			commentRequestHandler.handleAddComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), containsString("Add Comment Request requires a 'userId'"));
			assertThat(e.getMessage(), containsString("Add Comment Request requires a 'postId'"));
			assertThat(e.getMessage(), containsString("Add Comment Request requires 'text'"));
			verify(request, times(1)).params(POST_ID_PARAM);
			verify(request, times(1)).contentType();
			verify(request, times(1)).body();
			verifyNoMoreInteractions(request);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditComment() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_COMMENT).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);
		when(dataService.editComment(3, "Editing this Comment!")).thenReturn(true);

		boolean commentEdited = commentRequestHandler.handleEditComment(request, response);
		assertThat(commentEdited, is(true));

		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromCommentId(3);
		verify(dataService, times(1)).editComment(3, "Editing this Comment!");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditCommentByAdmin() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.ADMIN).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);
		when(dataService.editComment(3, "Editing this Comment!")).thenReturn(true);

		boolean commentEdited = commentRequestHandler.handleEditComment(request, response);
		assertThat(commentEdited, is(true));

		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromCommentId(3);
		verify(dataService, times(1)).editComment(3, "Editing this Comment!");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditCommentWithJsonContentType() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_COMMENT).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		when(request.contentType()).thenReturn("application/json");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);
		when(dataService.editComment(3, "Editing this Comment!")).thenReturn(true);

		boolean commentEdited = commentRequestHandler.handleEditComment(request, response);
		assertThat(commentEdited, is(true));

		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).body();
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromCommentId(3);
		verify(dataService, times(1)).editComment(3, "Editing this Comment!");
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleEditCommentWithNoCommentId() throws IOException {

		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		try {
			commentRequestHandler.handleEditComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), is("null"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditCommentWithInvalidCommentId() throws IOException {

		when(request.params(COMMENT_ID_PARAM)).thenReturn("a");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		try {
			commentRequestHandler.handleEditComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), containsString("For input string"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditCommentWithNoText() throws IOException {

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn("{}");
		try {
			commentRequestHandler.handleEditComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(BadRequestException.class));
			assertThat(e.getMessage(), is("Edit Comment Request requires 'text'"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditCommentThatDoesNotExist() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.EDIT_COMMENT).build();
		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);
		when(dataService.editComment(3, "Editing this Comment!")).thenReturn(false);

		try {
			commentRequestHandler.handleEditComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Comment Not Found"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromCommentId(3);
			verify(dataService, times(1)).editComment(3, "Editing this Comment!");
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditCommentByUnauthorizedPermission() throws IOException {

		Token token = Token.newBuilder().setUserId(2).addPermissions(Permission.ADD_COMMENT).build();
		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);

		try {
			commentRequestHandler.handleEditComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnauthorizedException.class));
			assertThat(e.getMessage(), is("User not authorized to Edit Comment"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromCommentId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleEditCommentByUnauthorizedUserId() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.EDIT_COMMENT).build();
		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.body()).thenReturn(EDIT_COMMENT_REQUEST);
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);

		try {
			commentRequestHandler.handleEditComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnauthorizedException.class));
			assertThat(e.getMessage(), is("User not authorized to Edit Comment"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verify(request, times(1)).body();
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromCommentId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeleteComment() throws IOException {
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.DELETE_COMMENT).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(1);
		when(dataService.deleteComment(3)).thenReturn(true);

		boolean commentDeleted = commentRequestHandler.handleDeleteComment(request, response);
		assertThat(commentDeleted, is(true));

		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromCommentId(3);
		verify(dataService, times(1)).deleteComment(3);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleDeleteCommentByAdmin() throws IOException {
		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADMIN).build();

		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(1);
		when(dataService.deleteComment(3)).thenReturn(true);

		boolean commentDeleted = commentRequestHandler.handleDeleteComment(request, response);
		assertThat(commentDeleted, is(true));

		verify(request, times(1)).params(COMMENT_ID_PARAM);
		verify(request, times(1)).headers(AUTHORIZATION);
		verify(tokenService, times(1)).createTokenFromString("SecretToken");
		verifyNoMoreInteractions(tokenService);
		verify(dataService, times(1)).getUserIdFromCommentId(3);
		verify(dataService, times(1)).deleteComment(3);
		verifyNoMoreInteractions(dataService);
	}

	@Test
	public void testHandleDeleteCommentWithNoCommentId() throws IOException {

		try {
			commentRequestHandler.handleDeleteComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), is("null"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeleteCommentWithInvalidCommentId() throws IOException {

		when(request.params(COMMENT_ID_PARAM)).thenReturn("a");
		try {
			commentRequestHandler.handleDeleteComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NumberFormatException.class));
			assertThat(e.getMessage(), containsString("For input string"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verifyZeroInteractions(tokenService);
			verifyZeroInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeleteCommentThatDoesNotExist() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.DELETE_COMMENT).build();
		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(1);
		when(dataService.deleteComment(3)).thenReturn(false);

		try {
			commentRequestHandler.handleDeleteComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Comment Not Found"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromCommentId(3);
			verify(dataService, times(1)).deleteComment(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeleteCommentByUnauthorizedPermission() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.ADD_COMMENT).build();
		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(1);

		try {
			commentRequestHandler.handleDeleteComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnauthorizedException.class));
			assertThat(e.getMessage(), is("User not authorized to Delete Comment"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromCommentId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

	@Test
	public void testHandleDeleteCommentByUnauthorizedUserId() throws IOException {

		Token token = Token.newBuilder().setUserId(1).addPermissions(Permission.DELETE_COMMENT).build();
		when(request.params(COMMENT_ID_PARAM)).thenReturn("3");
		when(request.headers(AUTHORIZATION)).thenReturn("Bearer SecretToken");
		when(tokenService.createTokenFromString("SecretToken")).thenReturn(token);
		when(dataService.getUserIdFromCommentId(3)).thenReturn(2);

		try {
			commentRequestHandler.handleDeleteComment(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnauthorizedException.class));
			assertThat(e.getMessage(), is("User not authorized to Delete Comment"));
			verify(request, times(1)).params(COMMENT_ID_PARAM);
			verify(request, times(1)).headers(AUTHORIZATION);
			verify(tokenService, times(1)).createTokenFromString("SecretToken");
			verify(dataService, times(1)).getUserIdFromCommentId(3);
			verifyNoMoreInteractions(dataService);
		}
	}

}
