package com.jms.socialmedia.handlers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.token.TokenService;

public class RequestHandlerTest {

	@Before
	public void setup() throws Exception {
	}

	//@Test
	public void testAuthorizeRequest() {
		fail("Not yet implemented");
	}

	//@Test
	public void testExtractBodyContent() {
		fail("Not yet implemented");
	}

	private class MyRequestHandler extends RequestHandler {

		public MyRequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
			super(dataService, tokenService, gson);
		}
		
	}
}
