package com.jms.socialmedia.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class PostTest {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsObjectAndHashCode() {
		Post post = new Post();
		Post post2 = new Post();
		assertThat(post.equals(post), is(true));
		assertThat(post.equals(null), is(false));
		assertThat(post.equals(new Comment()), is(false));
		assertThat(post.equals(post2), is(true));
		assertThat(post.hashCode(), is(post2.hashCode()));

		post.setUserId(1);
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setUserId(1);
		post.setUsername("username");
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setUsername("username");
		post.setFullName("fullName");
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setFullName("fullName");
		post.setPostId(4);
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setPostId(4);
		post.setText("text");
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setText("text");
		post.setProfilePictureLink("link");
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setProfilePictureLink("link");
		post.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1, 1));
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1, 1));
		post.addLike("Me");
		assertThat(post.equals(post2), is(false));
		assertThat(post.hashCode(), is(not(post2.hashCode())));

		post2.addLike("Me");
		assertThat(post.equals(post2), is(true));
		assertThat(post.hashCode(), is(post2.hashCode()));
	}

	@Test
	public void testToString() {
		Post post = new Post(4, 1, "username", "Full Name", "Post Text", LocalDateTime.of(2011, 1, 1, 1, 1, 1));
		post.addLike("Me 1");
		post.addLike("Me 2");

		assertThat(post.toString(), containsString("postId=4"));
		assertThat(post.toString(), containsString("userId=1"));
		assertThat(post.toString(), containsString("username=username"));
		assertThat(post.toString(), containsString("fullName=Full Name"));
		assertThat(post.toString(), containsString("text=Post Text"));
		assertThat(post.toString(), containsString("likes=[Me 1, Me 2]"));
		assertThat(post.toString(), containsString("timestamp=2011-01-01T01:01:01"));
	}
}
