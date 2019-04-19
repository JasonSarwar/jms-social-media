package com.jms.socialmedia.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class EntryTest {

	@Test
	public void testToString() {
		Entry post = new Post();
		post.setPostId(4);
		post.setUserId(1);
		post.setUsername("username");
		post.setFullName("Full Name");
		post.setText("Post Text");
		post.setTimestamp(LocalDateTime.of(2011, 1, 1, 1, 1, 1));
		post.addLike(56);
		post.addLike(57);

		assertThat(post.toString(), containsString("postId=4"));
		assertThat(post.toString(), containsString("userId=1"));
		assertThat(post.toString(), containsString("username=username"));
		assertThat(post.toString(), containsString("fullName=Full Name"));
		assertThat(post.toString(), containsString("text=Post Text"));
		assertThat(post.toString(), containsString("likes=[56, 57]"));
		assertThat(post.toString(), containsString("timestamp=2011-01-01T01:01:01"));
	}

}
