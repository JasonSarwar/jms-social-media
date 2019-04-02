package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.jms.socialmedia.model.Post;

public class SqlSessionPostsMapper implements PostsMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionPostsMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public int getNumberOfPosts() {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getNumberOfPosts();
		}
	}

	@Override
	public Collection<Post> getPosts(Collection<Integer> userIds, String username, 
			String tag, String onDate, String beforeDate, String afterDate, Integer sincePostId,
			String sortBy, boolean sortOrderAsc) {
		
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getPosts(userIds, username, tag, onDate, beforeDate, afterDate, sincePostId, sortBy, sortOrderAsc);
		}
	}

	@Override
	public Post getPost(int postId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
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
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.addPost(post);
		}
	}

	@Override
	public int editPost(int postId, String postText) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.editPost(postId, postText);
		}
	}

	@Override
	public int deletePost(int postId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.deletePost(postId);
		}
	}

	@Override
	public Collection<Post> getLikedPostsByUserId(int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getLikedPostsByUserId(userId);
		}
	}

	@Override
	public Collection<Integer> getPostLikes(int postId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getPostLikes(postId);
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

	@Override
	public Collection<Post> getCommentedPostsByUserId(int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			PostsMapper mapper = session.getMapper(PostsMapper.class);
			return mapper.getCommentedPostsByUserId(userId);
		}
	}
}
