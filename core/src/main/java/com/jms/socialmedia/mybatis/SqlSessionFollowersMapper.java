package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlSessionFollowersMapper implements FollowersMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionFollowersMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public Collection<String> getFollowerUsernames(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.getFollowerUsernames(username);
		}
	}

	@Override
	public Collection<String> getFollowingUsernames(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.getFollowingUsernames(username);
		}
	}

	@Override
	public int followUser(Integer followerUserId, String followerUsername, Integer followingUserId, String followingUsername) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.followUser(followerUserId, followerUsername, followingUserId, followingUsername);
		}
	}

	@Override
	public int unfollowUser(Integer followerUserId, String followerUsername, Integer followingUserId, String followingUsername) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.unfollowUser(followerUserId, followerUsername, followingUserId, followingUsername);
		}
	}

	@Override
	public Collection<String> getUsernamesToFollow(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.getUsernamesToFollow(username);
		}
	}
}
