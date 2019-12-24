package com.jms.socialmedia.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

public class UserPageTest {

	@Test
	public void testGetFollowers() {
		UserPage userPage = new UserPage();

		assertThat(userPage.getFollowersUsernames(), is(Collections.emptySet()));

		assertThat(userPage.addFollowersUsernames("Jason"), is(true));

		assertThat(userPage.getFollowersUsernames(), is(Collections.singleton("Jason")));

		assertThat(userPage.addFollowersUsernames(Set.of("User1", "User2")), is(true));
		
		assertThat(userPage.getFollowersUsernames(), is(Set.of("Jason", "User1", "User2")));
	}

	@Test
	public void testGetFollowing() {
		UserPage userPage = new UserPage();

		assertThat(userPage.getFollowingUsernames(), is(Collections.emptySet()));

		assertThat(userPage.addFollowingUsernames("Jason"), is(true));

		assertThat(userPage.getFollowingUsernames(), is(Collections.singleton("Jason")));

		assertThat(userPage.addFollowingUsernames(Set.of("User1", "User2")), is(true));
		
		assertThat(userPage.getFollowingUsernames(), is(Set.of("Jason", "User1", "User2")));
	}
}
