package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.jms.socialmedia.model.User;

public class SqlSessionFollowersMapper implements FollowersMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionFollowersMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public Collection<Integer> getFollowerUserIds(int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.getFollowerUserIds(userId);
		}
	}

	@Override
	public Collection<Integer> getFollowingUserIds(int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.getFollowingUserIds(userId);
		}
	}

	@Override
	public int followUser(int followerUserId, int followingUserId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.followUser(followerUserId, followingUserId);
		}
	}

	@Override
	public int unfollowUser(int followerUserId, int followingUserId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.unfollowUser(followerUserId, followingUserId);
		}
	}

	@Override
	public Collection<User> getUsersToFollow(int userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			FollowersMapper mapper = session.getMapper(FollowersMapper.class);
			return mapper.getUsersToFollow(userId);
		}
	}
}
