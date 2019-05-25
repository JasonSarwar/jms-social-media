package com.jms.socialmedia.cache;

import org.junit.Before;
import org.junit.Test;

import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;

import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class GuavaCachingServiceTest {

	private GuavaCachingService guavaCachingService;

	@Before
	public void setUp() throws Exception {
		guavaCachingService = new GuavaCachingService(2, 2);
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

	@Test
	public void testAddUserSessions() {
		User user1 = new User(1, "User1", "Full Name 1", "Hashed Password");
		User user2 = new User(2, "User2", "Full Name 2", "Hashed Password");
		User user3 = new User(3, "User3", "Full Name 3", "Hashed Password");

		String sessionKey1 = "sessionKey1";
		String sessionKey2 = "sessionKey2";
		String sessionKey3 = "sessionKey3";

		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey1), is(nullValue()));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey2), is(nullValue()));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey3), is(nullValue()));

		guavaCachingService.putUserSessionIntoCache(sessionKey1, user1);

		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey1), is(user1));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey2), is(nullValue()));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey3), is(nullValue()));

		guavaCachingService.putUserSessionIntoCache(sessionKey2, user2);

		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey1), is(user1));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey2), is(user2));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey3), is(nullValue()));

		guavaCachingService.putUserSessionIntoCache(sessionKey3, user3);

		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey1), is(nullValue()));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey2), is(user2));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey3), is(user3));

		guavaCachingService.removeUserSessionFromCache(sessionKey3);

		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey1), is(nullValue()));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey2), is(user2));
		assertThat(guavaCachingService.getUserSessionFromCache(sessionKey3), is(nullValue()));
	}

	@Test
	public void testEditPost() {
		Post post = new Post(1);
		post.setText("Old Text");
		guavaCachingService.putPostIntoCache(post);
		assertThat(guavaCachingService.getPostFromCache(1).getText(), is("Old Text"));

		guavaCachingService.editPostInCache(1, "New Text");
		assertThat(guavaCachingService.getPostFromCache(1).getText(), is("New Text"));
	}

	@Test
	public void testEditPostThatIsNotThere() {

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		guavaCachingService.editPostInCache(1, "New Text");
		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
	}

	@Test
	public void testLikePost() {
		Post post = new Post(1);
		guavaCachingService.putPostIntoCache(post);
		assertThat(guavaCachingService.getPostFromCache(1).getLikes(), is(Collections.emptySet()));

		guavaCachingService.likePostInCache(1, 5);
		assertThat(guavaCachingService.getPostFromCache(1).getLikes(), is(Collections.singleton(5)));
	}

	@Test
	public void testLikePostThatIsNotThere() {

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		guavaCachingService.likePostInCache(1, 5);
		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
	}

	@Test
	public void testUnlikePost() {
		Post post = new Post(1);
		post.addLike(5);
		guavaCachingService.putPostIntoCache(post);
		assertThat(guavaCachingService.getPostFromCache(1).getLikes(), is(Collections.singleton(5)));

		guavaCachingService.unlikePostInCache(1, 5);
		assertThat(guavaCachingService.getPostFromCache(1).getLikes(), is(Collections.emptySet()));
	}

	@Test
	public void testUnlikePostThatIsNotThere() {

		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
		guavaCachingService.unlikePostInCache(1, 5);
		assertThat(guavaCachingService.getPostFromCache(1), is(nullValue()));
	}

	@Test
	public void testEditComment() {
		Comment comment = new Comment(1, 3, "Old Text", null);
		guavaCachingService.putCommentIntoCache(comment);
		assertThat(guavaCachingService.getCommentFromCache(1).getText(), is("Old Text"));

		guavaCachingService.editCommentInCache(1, "New Text");
		assertThat(guavaCachingService.getCommentFromCache(1).getText(), is("New Text"));
	}

	@Test
	public void testEditCommentThatIsNotThere() {

		assertThat(guavaCachingService.getCommentFromCache(1), is(nullValue()));
		guavaCachingService.editCommentInCache(1, "New Text");
		assertThat(guavaCachingService.getCommentFromCache(1), is(nullValue()));
	}

	@Test
	public void testLikeComment() {
		Comment comment = new Comment(1, 3, "Old Text", null);
		guavaCachingService.putCommentIntoCache(comment);
		assertThat(guavaCachingService.getCommentFromCache(1).getLikes(), is(Collections.emptySet()));

		guavaCachingService.likeCommentInCache(1, 5);
		assertThat(guavaCachingService.getCommentFromCache(1).getLikes(), is(Collections.singleton(5)));
	}

	@Test
	public void testLikeCommentThatIsNotThere() {

		assertThat(guavaCachingService.getCommentFromCache(1), is(nullValue()));
		guavaCachingService.likeCommentInCache(1, 5);
		assertThat(guavaCachingService.getCommentFromCache(1), is(nullValue()));
	}

	@Test
	public void testUnlikeComment() {
		Comment comment = new Comment(1, 3, "Old Text", null);
		comment.addLike(5);
		guavaCachingService.putCommentIntoCache(comment);
		assertThat(guavaCachingService.getCommentFromCache(1).getLikes(), is(Collections.singleton(5)));

		guavaCachingService.unlikeCommentInCache(1, 5);
		assertThat(guavaCachingService.getCommentFromCache(1).getLikes(), is(Collections.emptySet()));
	}

	@Test
	public void testUnlikeCommentThatIsNotThere() {

		assertThat(guavaCachingService.getCommentFromCache(1), is(nullValue()));
		guavaCachingService.unlikeCommentInCache(1, 5);
		assertThat(guavaCachingService.getCommentFromCache(1), is(nullValue()));
	}
}
