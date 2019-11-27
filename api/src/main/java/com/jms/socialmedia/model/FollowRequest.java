package com.jms.socialmedia.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class FollowRequest {

	private Integer followerUserId;
	private Integer followingUserId;
	
	public FollowRequest() {
	}

	public FollowRequest(Integer followerUserId, Integer followingUserId) {
		this.followerUserId = followerUserId;
		this.followingUserId = followingUserId;
	}

	public final Integer getFollowerUserId() {
		return followerUserId;
	}

	public final void setFollowerUserId(Integer followerUserId) {
		this.followerUserId = followerUserId;
	}

	public final Integer getFollowingUserId() {
		return followingUserId;
	}

	public final void setFollowingUserId(Integer followingUserId) {
		this.followingUserId = followingUserId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(followerUserId, followingUserId);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || other.getClass() != getClass()) return false;
			
		FollowRequest followRequest = (FollowRequest) other;
		return Objects.equals(followerUserId, followRequest.followerUserId)
				&& Objects.equals(followingUserId, followRequest.followingUserId);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("followerUserId", followerUserId)
				.add("followingUserId", followingUserId)
				.toString();
	}
}
