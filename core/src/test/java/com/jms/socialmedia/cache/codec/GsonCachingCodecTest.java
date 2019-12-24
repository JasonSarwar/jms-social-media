package com.jms.socialmedia.cache.codec;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class GsonCachingCodecTest {

	private static final String ENCODED_POST = "{\"postId\":4,\"userId\":12,"
			+ "\"username\":\"Username\",\"fullName\":\"A Full Name\","
			+ "\"profilePictureLink\":\"link/to/picture\","
			+ "\"text\":\"Some Post Text\","
			+ "\"timestamp\":{\"date\":{\"year\":2019,\"month\":5,\"day\":17},"
			+ "\"time\":{\"hour\":6,\"minute\":16,\"second\":25,\"nano\":0}},"
			+ "\"likes\":[\"Joe\",\"Sam\"]}";

	private static final String ENCODED_COMMENT = "{\"commentId\":23,\"postId\":5,\"userId\":14,"
			+ "\"username\":\"SecondUsername\",\"fullName\":\"Another Full Name\","
			+ "\"profilePictureLink\":\"link/to/picture\",\"text\":\"Some Comment Text\","
			+ "\"timestamp\":{\"date\":{\"year\":2019,\"month\":5,\"day\":19},"
			+ "\"time\":{\"hour\":6,\"minute\":47,\"second\":47,\"nano\":0}},\"likes\":[\"Pete\",\"Ron\"]}";

	private static final String ENCODED_USER = "{\"userId\":10,\"username\":\"UserName\",\"fullName\":\"Full Name\"}";

	private Post post;
	private Comment comment;
	private User user;
	private GsonCachingCodec gsonCachingCodec;

	@Before
	public void setUp() throws Exception {
		post = new Post(4, 12, "Username", "A Full Name", "Some Post Text", LocalDateTime.of(2019, 5, 17, 6, 16, 25));
		post.setProfilePictureLink("link/to/picture");
		post.setLikes(Arrays.asList("Joe", "Sam"));

		comment = new Comment(23, 5, 14, "SecondUsername", "Another Full Name", "Some Comment Text", LocalDateTime.of(2019, 5, 19, 6, 47, 47));
		comment.setProfilePictureLink("link/to/picture");
		comment.setLikes(Arrays.asList("Pete", "Ron"));

		user = new User(10, "UserName", "Full Name");

		gsonCachingCodec = new GsonCachingCodec(new GsonBuilder().create());
	}

	@Test
	public void testEncodePost() {
		assertThat(gsonCachingCodec.encodePost(post), is(ENCODED_POST));
	}

	@Test
	public void testDecodePost() {
		assertThat(gsonCachingCodec.decodePost(ENCODED_POST), is(post));
		assertThat(gsonCachingCodec.decodePost(null), is(nullValue()));
	}

	@Test
	public void testEncodeComment() {
		assertThat(gsonCachingCodec.encodeComment(comment), is(ENCODED_COMMENT));
	}

	@Test
	public void testDecodeComment() {
		assertThat(gsonCachingCodec.decodeComment(ENCODED_COMMENT), is(comment));
		assertThat(gsonCachingCodec.decodeComment(null), is(nullValue()));
	}

	@Test
	public void testEncodeUser() {
		assertThat(gsonCachingCodec.encodeUser(user), is(ENCODED_USER));
	}

	@Test
	public void testDecodeUser() {
		assertThat(gsonCachingCodec.decodeUser(ENCODED_USER), is(user));
		assertThat(gsonCachingCodec.decodeUser(null), is(nullValue()));
	}

}
