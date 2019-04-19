package com.jms.socialmedia.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class CommentTest {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsObjectAndHashCode() {
		Comment comment = new Comment();
		Comment comment2 = new Comment();
		assertThat(comment.equals(comment), is(true));
		assertThat(comment.equals(null), is(false));
		assertThat(comment.equals(new Post()), is(false));
		assertThat(comment.equals(comment2), is(true));
		assertThat(comment.hashCode(), is(comment2.hashCode()));

		comment.setUserId(1);
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setUserId(1);
		comment.setUsername("username");
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setUsername("username");
		comment.setFullName("fullName");
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setFullName("fullName");
		comment.setPostId(4);
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setPostId(4);
		comment.setCommentId(9);
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setCommentId(9);
		comment.setText("text");
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setText("text");
		comment.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1, 1));
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.setTimestamp(LocalDateTime.of(1, 1, 1, 1, 1, 1));
		comment.addLike(56);
		assertThat(comment.equals(comment2), is(false));
		assertThat(comment.hashCode(), is(not(comment2.hashCode())));

		comment2.addLike(56);
		assertThat(comment.equals(comment2), is(true));
		assertThat(comment.hashCode(), is(comment2.hashCode()));
	}

	@Test
	public void testToString() {
		Comment comment = new Comment(4, 22, 1, "username", "Full Name", "Comment Text", LocalDateTime.of(2011, 1, 1, 1, 1, 1));
		comment.addLike(56);
		comment.addLike(57);

		assertThat(comment.toString(), containsString("commentId=4"));
		assertThat(comment.toString(), containsString("postId=22"));
		assertThat(comment.toString(), containsString("userId=1"));
		assertThat(comment.toString(), containsString("username=username"));
		assertThat(comment.toString(), containsString("fullName=Full Name"));
		assertThat(comment.toString(), containsString("text=Comment Text"));
		assertThat(comment.toString(), containsString("likes=[56, 57]"));
		assertThat(comment.toString(), containsString("timestamp=2011-01-01T01:01:01"));
	}
}
