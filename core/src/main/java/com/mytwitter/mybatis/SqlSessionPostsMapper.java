package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.mytwitter.model.Comment;
import com.mytwitter.model.FullPost;
import com.mytwitter.model.Post;

public class SqlSessionPostsMapper implements PostsMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionPostsMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public int getNumberOfPosts() {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getNumberOfPosts();
		}
	}
	
	@Override
	public FullPost getPost(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getPost(postId);
		}
	}

	@Override
	public Collection<Post> getPosts(Integer userId, String username, 
			String tag, String onDate, String beforeDate, String afterDate) {
		
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
		}
	}
	
	@Override
	public int addPost(Post post) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.addPost(post);
		}
	}
	
	@Override
	public int addComment(Comment comment) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.addComment(comment);
		}
	}
	
	@Override
	public Collection<Comment> getComments(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getComments(postId);
		}
	}

}
