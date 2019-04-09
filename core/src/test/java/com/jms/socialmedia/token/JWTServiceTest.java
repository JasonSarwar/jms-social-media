package com.jms.socialmedia.token;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

import com.jms.socialmedia.token.JWTService;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;

public class JWTServiceTest {

	@Test
	public void testJWTService() throws IOException {
		JWTService jwtService = new JWTService();
		Token token = Token.newBuilder().setUserId(5).addPermissions(Permission.ADD_POST, Permission.DELETE_COMMENT).build();
		String jwt = jwtService.createTokenString(token);
		Token token2 = jwtService.createTokenFromString(jwt);
		assertThat(token2.getUserId(), is(5));
		assertThat(token2.getPermissions().size(), is(2));
		assertThat(token2.getPermissions().contains(Permission.ADD_POST), is(true));
		assertThat(token2.getPermissions().contains(Permission.DELETE_COMMENT), is(true));
	}

}
