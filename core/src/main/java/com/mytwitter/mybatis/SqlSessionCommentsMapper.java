package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.mytwitter.model.Comment;

public class SqlSessionCommentsMapper implements CommentsMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionCommentsMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public int getNumberOfCommentsInPost(int postId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CommentsMapper mapper = session.getMapper(CommentsMapper.class);
			return mapper.getNumberOfCommentsInPost(postId);
		}
	}
	
	@Override
	public Collection<Comment> getComments(int postId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CommentsMapper mapper = session.getMapper(CommentsMapper.class);
			return mapper.getComments(postId);
		}
	}

	@Override
	public Comment getComment(int commentId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CommentsMapper mapper = session.getMapper(CommentsMapper.class);
			return mapper.getComment(commentId);
		}
	}

	@Override
	public int addComment(Comment comment) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CommentsMapper mapper = session.getMapper(CommentsMapper.class);
			return mapper.addComment(comment);
		}
	}

	@Override
	public int editComment(int commentId, String commentText) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CommentsMapper mapper = session.getMapper(CommentsMapper.class);
			return mapper.editComment(commentId, commentText);
		}
	}

	@Override
	public int deleteComment(int commentId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CommentsMapper mapper = session.getMapper(CommentsMapper.class);
			return mapper.deleteComment(commentId);
		}
	}
}
