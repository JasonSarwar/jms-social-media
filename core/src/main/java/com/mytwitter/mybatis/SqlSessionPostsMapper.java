package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

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
	public Post getPost(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getPost(postId);
		}
	}

	@Override
	public Collection<Post> getPosts(int userId, String username, 
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
			int x = mapper.addPost(post);
			return x;
		}
	}
	
	@Override
	public Collection<Post> getReplies(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getReplies(postId);
		}
	}

}
