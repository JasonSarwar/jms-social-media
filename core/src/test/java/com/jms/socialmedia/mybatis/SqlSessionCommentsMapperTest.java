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

public class SqlSessionCommentsMapperTest {

	@Mock
	private SqlSession sqlSession;
	@Mock
	private SqlSessionFactory sqlSessionFactory;
	@Mock
	private CommentsMapper commentsMapper;

	private SqlSessionCommentsMapper sqlSessionCommentsMapper;

	@Before
	public void setUp() {
		initMocks(this);
		sqlSessionCommentsMapper = new SqlSessionCommentsMapper(sqlSessionFactory);
		when(sqlSessionFactory.openSession(true)).thenReturn(sqlSession);
		when(sqlSession.getMapper(CommentsMapper.class)).thenReturn(commentsMapper);
	}

	@After
	public void tearDown() {
		verify(sqlSessionFactory, times(1)).openSession(true);
		verify(sqlSession, times(1)).getMapper(CommentsMapper.class);
		verify(sqlSession, times(1)).close();
		verifyNoMoreInteractions(commentsMapper, sqlSessionFactory, sqlSession);
	}

	//@Test
	public void testGetNumberOfCommentsInPost() {
		// TODO
	}

	//@Test
	public void testGetComments() {
		// TODO
	}

	//@Test
	public void testGetCommentsByUserId() {
		// TODO
	}

	//@Test
	public void testGetComment() {
		// TODO
	}

	//@Test
	public void testGetUserIdFromCommentId() {
		// TODO
	}

	//@Test
	public void testAddComment() {
		// TODO
	}

	//@Test
	public void testEditComment() {
		// TODO
	}

	//@Test
	public void testDeleteComment() {
		// TODO
	}

	@Test
	public void testGetCommentLikes() {
		int commentId = 4;
		Collection<String> commentLikes = Collections.singletonList("A Like");
		when(commentsMapper.getCommentLikes(commentId)).thenReturn(commentLikes);
		assertThat(sqlSessionCommentsMapper.getCommentLikes(commentId), is(commentLikes));
		verify(commentsMapper, times(1)).getCommentLikes(commentId);
	}

	@Test
	public void testLikeComment() {
		int postId = 5;
		int userId = 10;
		String username = "Username";

		when(commentsMapper.likeComment(postId, userId, username)).thenReturn(1);
		assertThat(sqlSessionCommentsMapper.likeComment(postId, userId, username), is(1));
		verify(commentsMapper, times(1)).likeComment(postId, userId, username);
	}

	@Test
	public void testUnlikeComment() {
		int postId = 5;
		int userId = 10;
		String username = "Username";

		when(commentsMapper.unlikeComment(postId, userId, username)).thenReturn(1);
		assertThat(sqlSessionCommentsMapper.unlikeComment(postId, userId, username), is(1));
		verify(commentsMapper, times(1)).unlikeComment(postId, userId, username);
	}

}
