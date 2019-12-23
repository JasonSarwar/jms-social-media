package com.jms.socialmedia.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class FollowRequestTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		FollowRequest followRequest1 = new FollowRequest();
		
		assertThat(followRequest1.equals(null), is(false));
		assertThat(followRequest1.equals(new LoginSuccess()), is(false));
		
		FollowRequest followRequest2 = new FollowRequest();

		assertThat(followRequest1.equals(followRequest2), is(true));
		assertThat(followRequest1.hashCode(), is(followRequest2.hashCode()));
		assertThat(followRequest1.toString(), is("FollowRequest{followerUsername=null, followingUsername=null}"));
		
		followRequest1.setFollowerUsername("A_Follower_Username");
		
		assertThat(followRequest1.equals(followRequest2), is(false));
		assertThat(followRequest1.hashCode(), is(not(followRequest2.hashCode())));
		assertThat(followRequest1.toString(), is("FollowRequest{followerUsername=A_Follower_Username, followingUsername=null}"));
		
		followRequest2.setFollowerUsername("A_Follower_Username");
		
		assertThat(followRequest1.equals(followRequest2), is(true));
		assertThat(followRequest1.hashCode(), is(followRequest2.hashCode()));
		
		followRequest1.setFollowingUsername("A_Following_Username");
		
		assertThat(followRequest1.equals(followRequest2), is(false));
		assertThat(followRequest1.hashCode(), is(not(followRequest2.hashCode())));
		assertThat(followRequest1.toString(), is("FollowRequest{followerUsername=A_Follower_Username, followingUsername=A_Following_Username}"));
		
		followRequest2.setFollowingUsername("A_Following_Username");
		
		assertThat(followRequest1.equals(followRequest2), is(true));
		assertThat(followRequest1.hashCode(), is(followRequest2.hashCode()));
		
		FollowRequest followRequest3 = new FollowRequest("A_Follower_Username", "A_Following_Username");
		
		assertThat(followRequest3.equals(followRequest2), is(true));
		assertThat(followRequest3.hashCode(), is(followRequest2.hashCode()));
		assertThat(followRequest3.toString(), is("FollowRequest{followerUsername=A_Follower_Username, followingUsername=A_Following_Username}"));
	}

}
