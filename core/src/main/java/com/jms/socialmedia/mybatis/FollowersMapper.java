package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

public interface FollowersMapper {

	Collection<String> getFollowerUsernames(String username);

	Collection<String> getFollowingUsernames(String username);

	int followUser(@Param(value="followerUserId") Integer followerUserId, @Param(value="followerUsername") String followerUsername, 
			@Param(value="followingUserId") Integer followingUserId, @Param(value="followingUsername") String followingUsername);

	int unfollowUser(@Param(value="followerUserId") Integer followerUserId, @Param(value="followerUsername") String followerUsername, 
			@Param(value="followingUserId") Integer followingUserId, @Param(value="followingUsername") String followingUsername);
	
	Collection<String> getUsernamesToFollow(String username);
}
