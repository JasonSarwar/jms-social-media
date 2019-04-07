package com.jms.socialmedia.mybatis;

import java.util.Collection;

import com.jms.socialmedia.model.User;

public interface FollowersMapper {

	Collection<Integer> getFollowerUserIds(int userId);

	Collection<Integer> getFollowingUserIds(int userId);

	int followUser(int followerUserId, int followingUserId);

	int unfollowUser(int followerUserId, int followingUserId);
	
	Collection<User> getUsersToFollow(int userId);
}
