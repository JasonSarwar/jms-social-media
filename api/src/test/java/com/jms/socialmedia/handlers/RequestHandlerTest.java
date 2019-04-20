package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.exception.UnauthorizedException;
import com.jms.socialmedia.exception.UnsupportedContentTypeException;
import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.routes.LocalDateTypeAdapter;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.TokenService;

import spark.Request;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RequestHandlerTest {

	private static final String AUTHORIZATION = "Authorization";
	private static final String NEW_USER_REQUEST = "{\"username\":\"JasonSarwar\"," + "\"fullName\":\"Jason M Sarwar\","
			+ "\"email\":\"jason_sarwar@jms-social-media.com\"," + "\"password1\":\"password\","
			+ "\"password2\":\"password\"," + "\"birthDate\":\"May 7, 1900\"}";

	@Mock
	private TokenService tokenService;
	@Mock
	private Request request;
	private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();

	private RequestHandler requestHandler;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		requestHandler = new MyRequestHandler(tokenService, gson);
	}

	@Test
	public void testAuthorizeRequestMissingHeader() {
		try {
			requestHandler.authorizeRequest(request, 1, Permission.ADD_COMMENT);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnauthorizedException.class));
			assertThat(e.getMessage(), is("Not authorized to Add Comment"));
			verify(request, times(1)).headers(AUTHORIZATION);
		}
	}

	@Test
	public void testAuthorizeRequestMissingBearer() {
		when(request.headers(AUTHORIZATION)).thenReturn("Bear er");
		try {
			requestHandler.authorizeRequest(request, 1, Permission.ADD_COMMENT);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnauthorizedException.class));
			assertThat(e.getMessage(), is("Not authorized to Add Comment"));
			verify(request, times(1)).headers(AUTHORIZATION);
		}
	}

	@Test
	public void testExtractBodyContent() throws IOException {

		when(request.body()).thenReturn(NEW_USER_REQUEST);
		NewUser newUser = requestHandler.extractBodyContent(request, NewUser.class);
		assertThat(newUser.getFullName(), is("Jason M Sarwar"));
		assertThat(newUser.getPassword1(), is("password"));
		assertThat(newUser.getPassword2(), is("password"));
		assertThat(newUser.getUsername(), is("JasonSarwar"));
		assertThat(newUser.getEmail(), is("jason_sarwar@jms-social-media.com"));
		assertThat(newUser.getBirthDate(), is(LocalDate.of(1900, 5, 7)));

		verify(request, times(1)).body();
		verify(request, times(1)).contentType();
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testExtractBodyContentJson() throws IOException {

		when(request.body()).thenReturn(NEW_USER_REQUEST);
		when(request.contentType()).thenReturn("application/json");
		NewUser newUser = requestHandler.extractBodyContent(request, NewUser.class);
		assertThat(newUser.getFullName(), is("Jason M Sarwar"));
		assertThat(newUser.getPassword1(), is("password"));
		assertThat(newUser.getPassword2(), is("password"));
		assertThat(newUser.getUsername(), is("JasonSarwar"));
		assertThat(newUser.getEmail(), is("jason_sarwar@jms-social-media.com"));
		assertThat(newUser.getBirthDate(), is(LocalDate.of(1900, 5, 7)));

		verify(request, times(1)).body();
		verify(request, atLeastOnce()).contentType();
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testExtractBodyContentApplicationXML() throws IOException {

		when(request.body()).thenReturn(NEW_USER_REQUEST);
		when(request.contentType()).thenReturn("application/xml");
		NewUser newUser = requestHandler.extractBodyContent(request, NewUser.class);
		assertThat(newUser, is(nullValue()));
		verify(request, atLeastOnce()).contentType();
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testExtractBodyContentTextXML() throws IOException {

		when(request.body()).thenReturn(NEW_USER_REQUEST);
		when(request.contentType()).thenReturn("text/xml");
		NewUser newUser = requestHandler.extractBodyContent(request, NewUser.class);
		assertThat(newUser, is(nullValue()));
		verify(request, atLeastOnce()).contentType();
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testExtractBodyContentApplicationProtobuf() throws IOException {

		when(request.body()).thenReturn(NEW_USER_REQUEST);
		when(request.contentType()).thenReturn("application/x-protobuf");
		NewUser newUser = requestHandler.extractBodyContent(request, NewUser.class);
		assertThat(newUser, is(nullValue()));
		verify(request, atLeastOnce()).contentType();
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testExtractBodyContentUnsupported() throws IOException {

		when(request.body()).thenReturn(NEW_USER_REQUEST);
		when(request.contentType()).thenReturn("application/unsupported");
		try {
			requestHandler.extractBodyContent(request, NewUser.class);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(UnsupportedContentTypeException.class));
			assertThat(e.getMessage(), is("Unsupported Content Type"));
			verify(request, atLeastOnce()).contentType();
			verifyNoMoreInteractions(request);
		}
	}

	private class MyRequestHandler extends RequestHandler {

		public MyRequestHandler(TokenService tokenService, Gson gson) {
			super(null, tokenService, gson);
		}

	}
}
