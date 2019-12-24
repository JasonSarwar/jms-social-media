package com.jms.socialmedia.mybatis;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.Collections;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class SqlSessionPostsMapperTest {

	@Mock
	private SqlSession sqlSession;
	@Mock
	private SqlSessionFactory sqlSessionFactory;
	@Mock
	private PostsMapper postsMapper;

	private SqlSessionPostsMapper sqlSessionPostsMapper;

	@Before
	public void setUp() {
		initMocks(this);
		sqlSessionPostsMapper = new SqlSessionPostsMapper(sqlSessionFactory);
		when(sqlSessionFactory.openSession(true)).thenReturn(sqlSession);
		when(sqlSession.getMapper(PostsMapper.class)).thenReturn(postsMapper);
	}

	@After
	public void tearDown() {
		verify(sqlSessionFactory, times(1)).openSession(true);
		verify(sqlSession, times(1)).getMapper(PostsMapper.class);
		verify(sqlSession, times(1)).close();
		verifyNoMoreInteractions(postsMapper, sqlSessionFactory, sqlSession);
	}

	//@Test
	public void testGetNumberOfPosts() {
		// TODO
	}

	//@Test
	public void testGetPosts() {
		// TODO
	}

	//@Test
	public void testGetPost() {
		// TODO
	}

	//@Test
	public void testGetUserIdFromPostId() {
		// TODO
	}

	//@Test
	public void testAddPost() {
		// TODO
	}

	//@Test
	public void testEditPost() {
		// TODO
	}

	//@Test
	public void testDeletePost() {
		// TODO
	}

	//@Test
	public void testGetLikedPostsByUserId() {
		// TODO
	}

	@Test
	public void testGetPostLikes() {
		int postId = 4;
		Collection<String> postLikes = Collections.singletonList("A Like");
		when(postsMapper.getPostLikes(postId)).thenReturn(postLikes);
		assertThat(sqlSessionPostsMapper.getPostLikes(postId), is(postLikes));
		verify(postsMapper, times(1)).getPostLikes(postId);
	}

	@Test
	public void testLikePost() {
		int postId = 5;
		int userId = 10;
		String username = "Username";

		when(postsMapper.likePost(postId, userId, username)).thenReturn(1);
		assertThat(sqlSessionPostsMapper.likePost(postId, userId, username), is(1));
		verify(postsMapper, times(1)).likePost(postId, userId, username);
	}

	@Test
	public void testUnlikePost() {
		int postId = 5;
		int userId = 10;
		String username = "Username";

		when(postsMapper.unlikePost(postId, userId, username)).thenReturn(1);
		assertThat(sqlSessionPostsMapper.unlikePost(postId, userId, username), is(1));
		verify(postsMapper, times(1)).unlikePost(postId, userId, username);
	}

	//@Test
	public void testGetCommentedPostsByUserId() {
		// TODO
	}

}
