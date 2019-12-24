package com.jms.socialmedia.dataservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.UserPage;

public class MockDataServiceTest {

	private MockDataService mockDataService;

	@Before
	public void setUp() {
		mockDataService = new MockDataService();

	}

	@Test
	public void testSetUp() {
		Post post1 = mockDataService.getPost(1);
		assertThat(post1, is(notNullValue()));
		assertThat(post1.getPostId(), is(1));
		assertThat(post1.getText(), is("These posts are from a Mock #Data Service and are not from a database"));
		assertThat(post1.getUserId(), is(2));

		assertThat(mockDataService.getPosts(2).size(), is(3));
	}

	@Test
	public void testGetUserIdByUsername() {
		// TODO
	}

	@Test
	public void testGetUserPageInfoByName() {
		UserPage jasonsUserPage = mockDataService.getUserPageInfoByName("Jason");

		assertThat(jasonsUserPage, is(notNullValue()));
		assertThat(jasonsUserPage.getUserId(), is(2));
		assertThat(jasonsUserPage.getUsername(), is("Jason"));
		assertThat(jasonsUserPage.getFullName(), is("Jason Sarwar"));
		assertThat(jasonsUserPage.getEmail(), is("jason_sarwar@jms.com"));
		assertThat(jasonsUserPage.getBio(), is("Trying to create this website."));

		assertThat(mockDataService.getUserPageInfoByName("Matt"), is(nullValue()));

		mockDataService.addUser(
				new NewUser(null, "Matt", "Mr. Matt", "matt@jms.com", "password", "password", LocalDate.now(), null));

		UserPage mattsUserPage = mockDataService.getUserPageInfoByName("Matt");

		assertThat(mattsUserPage, is(notNullValue()));
		assertThat(mattsUserPage.getUserId(), is(3));
		assertThat(mattsUserPage.getUsername(), is("Matt"));
		assertThat(mattsUserPage.getFullName(), is("Mr. Matt"));
		assertThat(mattsUserPage.getEmail(), is("matt@jms.com"));
		assertThat(mattsUserPage.getBio(), is(nullValue()));
	}

	@Test
	public void testGetUserLoginInfoByString() {
		// TODO
	}

	@Test
	public void testGetHashedPasswordByUserId() {
		// TODO
	}

	@Test
	public void testGetUsernamesToFollow() {

		assertThat(mockDataService.getUsernamesToFollow("user"), is(Collections.singletonList("Jason")));

		mockDataService.addUser(
				new NewUser(10, "Matt", "Mr. Matt", "matt@jms.com", "password", "password", LocalDate.now(), null));
		
		Collection<String> usernamesToFollow = mockDataService.getUsernamesToFollow("user");
		assertThat(usernamesToFollow.size(), is(2));
		assertThat(usernamesToFollow.contains("Jason"), is(true));
		assertThat(usernamesToFollow.contains("Matt"), is(true));
		
		mockDataService.followUser(null, "user", null, "Jason");
		
		assertThat(mockDataService.getUsernamesToFollow("user"), is(Collections.singletonList("Matt")));
	}

	@Test
	public void testIsUsernameTaken() {
		assertThat(mockDataService.isUsernameTaken("jason"), is(true));
		assertThat(mockDataService.isUsernameTaken("JASON"), is(true));
		assertThat(mockDataService.isUsernameTaken("matt"), is(false));
		assertThat(mockDataService.isUsernameTaken("MATT"), is(false));

		mockDataService.addUser(
				new NewUser(10, "Matt", "Mr. Matt", "matt@jms.com", "password", "password", LocalDate.now(), null));

		assertThat(mockDataService.isUsernameTaken("matt"), is(true));
		assertThat(mockDataService.isUsernameTaken("MATT"), is(true));
	}

	@Test
	public void testIsEmailTaken() {
		assertThat(mockDataService.isEmailTaken("jason_sarwar@jms.com"), is(true));
		assertThat(mockDataService.isEmailTaken("Jason_Sarwar@JMS.com"), is(true));
		assertThat(mockDataService.isEmailTaken("matt@jms.com"), is(false));
		assertThat(mockDataService.isEmailTaken("MATT@JMS.COM"), is(false));

		mockDataService.addUser(
				new NewUser(10, "Matt", "Mr. Matt", "matt@jms.com", "password", "password", LocalDate.now(), null));

		assertThat(mockDataService.isEmailTaken("matt@jms.com"), is(true));
		assertThat(mockDataService.isEmailTaken("MATT@JMS.COM"), is(true));
	}

	@Test
	public void testAddUser() {
		// TODO
	}

	@Test
	public void testEditPassword() {
		// TODO
	}

	@Test
	public void testAddUserSession() {
		// TODO
	}

	@Test
	public void testGetUserBySessionId() {
		// TODO
	}

	@Test
	public void testRemoveSessionId() {
		// TODO
	}

	@Test
	public void testGetPosts() {
		// TODO
	}

	@Test
	public void testGetPost() {
		// TODO
	}

	@Test
	public void testGetUserIdFromPostId() {
		// TODO
	}

	@Test
	public void testAddPost() {
		// TODO
	}

	@Test
	public void testEditPost() {
		// TODO
	}

	@Test
	public void testDeletePost() {
		// TODO
	}

	@Test
	public void testGetCommentedPostsByUserId() {
		// TODO
	}

	@Test
	public void testGetLikedPostsByUserId() {
		// TODO
	}

	@Test
	public void testLikeAndUnlikePostIntInt() {
		int postId = 1;

		assertThat(mockDataService.getPostLikes(postId), is(Collections.emptyList()));

		assertThat(mockDataService.likePost(postId, 1), is(true));

		assertThat(mockDataService.getPostLikes(postId), is(Collections.singletonList("user")));

		assertThat(mockDataService.unlikePost(postId, 1), is(true));

		assertThat(mockDataService.getPostLikes(postId), is(Collections.emptyList()));
	}

	@Test
	public void testLikeAndUnlikePostIntString() {
		int postId = 1;

		assertThat(mockDataService.getPostLikes(postId), is(Collections.emptyList()));

		assertThat(mockDataService.likePost(postId, "user"), is(true));

		assertThat(mockDataService.getPostLikes(postId), is(Collections.singletonList("user")));

		assertThat(mockDataService.unlikePost(postId, "user"), is(true));

		assertThat(mockDataService.getPostLikes(postId), is(Collections.emptyList()));
	}

	@Test
	public void testGetComments() {
		// TODO
	}

	@Test
	public void testGetCommentsByUserId() {
		// TODO
	}

	@Test
	public void testGetComment() {
		// TODO
	}

	@Test
	public void testGetUserIdFromCommentId() {
		// TODO
	}

	@Test
	public void testAddComment() {
		// TODO
	}

	@Test
	public void testEditComment() {
		// TODO
	}

	@Test
	public void testDeleteComment() {
		// TODO
	}

	@Test
	public void testLikeAndUnlikeCommentIntInt() {
		int commentId = 1;

		assertThat(mockDataService.getCommentLikes(commentId), is(Collections.emptyList()));

		assertThat(mockDataService.likeComment(commentId, 1), is(true));

		assertThat(mockDataService.getCommentLikes(commentId), is(Collections.singletonList("user")));

		assertThat(mockDataService.unlikeComment(commentId, 1), is(true));

		assertThat(mockDataService.getCommentLikes(commentId), is(Collections.emptyList()));
	}

	@Test
	public void testLikeAndUnlikeCommentIntString() {
		int commentId = 1;

		assertThat(mockDataService.getCommentLikes(commentId), is(Collections.emptyList()));

		assertThat(mockDataService.likeComment(commentId, "user"), is(true));

		assertThat(mockDataService.getCommentLikes(commentId), is(Collections.singletonList("user")));

		assertThat(mockDataService.unlikeComment(commentId, "user"), is(true));

		assertThat(mockDataService.getCommentLikes(commentId), is(Collections.emptyList()));
	}

	@Test
	public void testFollowAndUnfollowUserByUsername() {
		assertThat(mockDataService.getFollowerUsernames("Jason"), is(Collections.emptySet()));
		assertThat(mockDataService.getFollowingUsernames("user"), is(Collections.emptySet()));

		assertThat(mockDataService.followUser(null, "user", null, "Jason"), is(true));

		assertThat(mockDataService.getFollowerUsernames("Jason"), is(Collections.singleton("user")));
		assertThat(mockDataService.getFollowingUsernames("user"), is(Collections.singleton("Jason")));

		assertThat(mockDataService.unfollowUser(null, "user", null, "Jason"), is(true));

		assertThat(mockDataService.getFollowerUsernames("Jason"), is(Collections.emptySet()));
		assertThat(mockDataService.getFollowingUsernames("user"), is(Collections.emptySet()));
	}

	@Test
	public void testFollowAndUnfollowUserByUserId() {
		assertThat(mockDataService.getFollowerUsernames("Jason"), is(Collections.emptySet()));
		assertThat(mockDataService.getFollowingUsernames("user"), is(Collections.emptySet()));

		assertThat(mockDataService.followUser(1, null, 2, null), is(true));

		assertThat(mockDataService.getFollowerUsernames("Jason"), is(Collections.singleton("user")));
		assertThat(mockDataService.getFollowingUsernames("user"), is(Collections.singleton("Jason")));

		assertThat(mockDataService.unfollowUser(1, null, 2, null), is(true));

		assertThat(mockDataService.getFollowerUsernames("Jason"), is(Collections.emptySet()));
		assertThat(mockDataService.getFollowingUsernames("user"), is(Collections.emptySet()));
	}

}
