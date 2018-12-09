package com.mytwitter.utils;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

public class JWTUtilsTest {

	@Test
	public void testJWTUtils() throws IOException {

		String jwt = JWTUtils.createJWT(5);
		System.out.println(jwt);
		Integer userId = JWTUtils.validateJWTAndRetrieveUserId(jwt);
		assertThat(userId, is(5));
	}

}
