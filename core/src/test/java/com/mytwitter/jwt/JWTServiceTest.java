package com.mytwitter.jwt;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

import com.mytwitter.jwt.JWTService;

public class JWTServiceTest {

	@Test
	public void testJWTUtils() throws IOException {
		JWTService jwtService = new JWTService();
		String jwt = jwtService.createJWT(5);
		System.out.println(jwt);
		Integer userId = jwtService.validateJWTAndRetrieveUserId(jwt);
		assertThat(userId, is(5));
	}

}
