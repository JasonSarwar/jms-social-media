package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;

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
	public Collection<Post> getPosts(Integer userId, String username, 
			String tag, String onDate, String beforeDate, String afterDate) {
		
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
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
	public Integer getUserIdFromPostId(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getUserIdFromPostId(postId);
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
	public int editPost(int postId, String postText) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.editPost(postId, postText);
		}
	}

	@Override
	public int deletePost(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.deletePost(postId);
		}
	}

	@Override
	public Collection<Integer> getLikesOfPost(int postId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getLikesOfPost(postId);
		}
	}

	@Override
	public int likePost(int postId, int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.likePost(postId, userId);
		}
	}

	@Override
	public int unlikePost(int postId, int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.unlikePost(postId, userId);
		}
	}
}
