package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.jms.socialmedia.model.User;

public interface FollowersMapper {

	Collection<Integer> getFollowerUserIds(int userId);

	Collection<Integer> getFollowingUserIds(int userId);

	int followUser(@Param(value="followerUserId") Integer followerUserId, @Param(value="followerUsername") String followerUsername, 
			@Param(value="followingUserId") Integer followingUserId, @Param(value="followingUsername") String followingUsername);

	int unfollowUser(@Param(value="followerUserId") Integer followerUserId, @Param(value="followerUsername") String followerUsername, 
			@Param(value="followingUserId") Integer followingUserId, @Param(value="followingUsername") String followingUsername);
	
	Collection<User> getUsersToFollow(int userId);
}
