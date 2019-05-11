package com.jms.socialmedia.cache;

import org.junit.Before;
import org.junit.Test;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;

import static org.junit.Assert.assertThat;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class GuavaCachingServiceTest {

	private GuavaCachingService guavaCachingService;

	@Before
	public void setUp() throws Exception {
		guavaCachingService = new GuavaCachingService(2);
	}

	@Test
	public void testPosts() {
		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(3), is(nullValue()));

		Post post1 = new Post(1);
		guavaCachingService.putPostIntoCache(post1);

		assertThat(guavaCachingService.getPostFromCache(1), is(post1));
		assertThat(guavaCachingService.getPostFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(3), is(nullValue()));

		Post post2 = new Post(2);
		guavaCachingService.putPostIntoCache(post2);

		assertThat(guavaCachingService.getPostFromCache(1), is(post1));
		assertThat(guavaCachingService.getPostFromCache(2), is(post2));
		assertThat(guavaCachingService.getPostFromCache(3), is(nullValue()));

		Post post3 = new Post(3);
		guavaCachingService.putPostIntoCache(post3);

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(2), is(post2));
		assertThat(guavaCachingService.getPostFromCache(3), is(post3));

		guavaCachingService.removePostFromCache(2);

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(3), is(post3));
	}

	@Test
	public void testPostsAndComments() {

		Comment comment1 = new Comment(31, 1, null, null); // Comment ID 31, Post ID 1
		Comment comment2 = new Comment(32, 1, null, null);
		Comment comment3 = new Comment(33, 2, null, null);
		Comment comment4 = new Comment(34, 2, null, null);

		Set<Comment> commentsSet1 = Set.of(comment1, comment2);
		Set<Comment> commentsSet2 = Set.of(comment3, comment4);

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(3), is(nullValue()));

		assertThat(guavaCachingService.getCommentsFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getCommentsFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getCommentsFromCache(3), is(nullValue()));

		assertThat(guavaCachingService.getCommentFromCache(31), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(32), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		Post post1 = new Post(1);
		guavaCachingService.putPostIntoCache(post1);

		assertThat(guavaCachingService.getPostFromCache(1), is(post1));
		assertThat(guavaCachingService.getPostFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(3), is(nullValue()));

		guavaCachingService.putCommentsFromPostIntoCache(1, commentsSet1);

		assertThat(guavaCachingService.getCommentsFromCache(1), is(commentsSet1));
		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(2));
		assertThat(guavaCachingService.getCommentsFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getCommentsFromCache(3), is(nullValue()));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		Post post2 = new Post(2);
		guavaCachingService.putPostIntoCache(post2);

		assertThat(guavaCachingService.getPostFromCache(1), is(post1));
		assertThat(guavaCachingService.getPostFromCache(2), is(post2));
		assertThat(guavaCachingService.getPostFromCache(3), is(nullValue()));

		guavaCachingService.putCommentsFromPostIntoCache(2, commentsSet2);

		assertThat(guavaCachingService.getCommentsFromCache(1), is(commentsSet1));
		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(2));
		assertThat(guavaCachingService.getCommentsFromCache(2), is(commentsSet2));
		assertThat(guavaCachingService.getCommentsFromCache(2).size(), is(2));
		assertThat(guavaCachingService.getCommentsFromCache(3), is(nullValue()));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(comment3));
		assertThat(guavaCachingService.getCommentFromCache(34), is(comment4));

		Post post3 = new Post(3);
		guavaCachingService.putPostIntoCache(post3);

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(2), is(post2));
		assertThat(guavaCachingService.getPostFromCache(3), is(post3));

		assertThat(guavaCachingService.getCommentsFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getCommentsFromCache(2), is(commentsSet2));
		assertThat(guavaCachingService.getCommentsFromCache(2).size(), is(2));
		assertThat(guavaCachingService.getCommentsFromCache(3), is(nullValue()));

		assertThat(guavaCachingService.getCommentFromCache(31), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(32), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(33), is(comment3));
		assertThat(guavaCachingService.getCommentFromCache(34), is(comment4));

		guavaCachingService.removePostFromCache(2);

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getPostFromCache(3), is(post3));

		assertThat(guavaCachingService.getCommentsFromCache(1), is(nullValue()));
		assertThat(guavaCachingService.getCommentsFromCache(2), is(nullValue()));
		assertThat(guavaCachingService.getCommentsFromCache(3), is(nullValue()));

		assertThat(guavaCachingService.getCommentFromCache(31), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(32), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		guavaCachingService.putCommentIntoCache(comment2);
		assertThat(guavaCachingService.getCommentFromCache(31), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		guavaCachingService.removeCommentFromCache(32);
		assertThat(guavaCachingService.getCommentFromCache(31), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(32), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));
	}

	@Test
	public void testAddComments() {
		Comment comment1 = new Comment(31, 1, null, null); // Comment ID 31, Post ID 1
		Comment comment2 = new Comment(32, 1, null, null);
		Comment comment3 = new Comment(33, 1, null, null);
		Comment comment4 = new Comment(34, 2, null, null);

		Set<Comment> commentsSet1 = Set.of(comment1, comment2);

		assertThat(guavaCachingService.getCommentsFromCache(1), is(nullValue()));

		assertThat(guavaCachingService.getCommentFromCache(31), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(32), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		guavaCachingService.putCommentsFromPostIntoCache(1, commentsSet1);

		assertThat(guavaCachingService.getCommentsFromCache(1), is(commentsSet1));
		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(2));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		guavaCachingService.putCommentIntoCache(comment3);

		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(3));
		assertThat(guavaCachingService.getCommentsFromCache(1).containsAll(commentsSet1), is(true));
		assertThat(guavaCachingService.getCommentsFromCache(1).contains(comment3), is(true));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(comment3));
		assertThat(guavaCachingService.getCommentFromCache(34), is(nullValue()));

		guavaCachingService.putCommentIntoCache(comment4);

		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(3));
		assertThat(guavaCachingService.getCommentsFromCache(1).containsAll(commentsSet1), is(true));
		assertThat(guavaCachingService.getCommentsFromCache(1).contains(comment3), is(true));
		assertThat(guavaCachingService.getCommentsFromCache(1).contains(comment4), is(false));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(comment3));
		assertThat(guavaCachingService.getCommentFromCache(34), is(comment4));

		guavaCachingService.removeCommentFromCache(33);

		assertThat(guavaCachingService.getCommentsFromCache(1), is(commentsSet1));
		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(2));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(comment4));
		assertThat(guavaCachingService.getCommentFromCache(37), is(nullValue()));

		guavaCachingService.removeCommentFromCache(37);

		assertThat(guavaCachingService.getCommentsFromCache(1), is(commentsSet1));
		assertThat(guavaCachingService.getCommentsFromCache(1).size(), is(2));

		assertThat(guavaCachingService.getCommentFromCache(31), is(comment1));
		assertThat(guavaCachingService.getCommentFromCache(32), is(comment2));
		assertThat(guavaCachingService.getCommentFromCache(33), is(nullValue()));
		assertThat(guavaCachingService.getCommentFromCache(34), is(comment4));
		assertThat(guavaCachingService.getCommentFromCache(37), is(nullValue()));
	}
}
