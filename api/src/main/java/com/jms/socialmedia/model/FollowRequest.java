package com.jms.socialmedia.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

public class FollowRequest {

	private String followerUsername;
	private String followingUsername;

	public FollowRequest() {
	}

	public FollowRequest(String followerUsername, String followingUsername) {
		this.followerUsername = followerUsername;
		this.followingUsername = followingUsername;
	}

	public final String getFollowerUsername() {
		return followerUsername;
	}

	public final void setFollowerUsername(String followerUsername) {
		this.followerUsername = followerUsername;
	}

	public final String getFollowingUsername() {
		return followingUsername;
	}

	public final void setFollowingUsername(String followingUsername) {
		this.followingUsername = followingUsername;
	}

	@Override
	public int hashCode() {
		return Objects.hash(followerUsername, followingUsername);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || other.getClass() != getClass())
			return false;

		FollowRequest followRequest = (FollowRequest) other;
		return Objects.equals(followerUsername, followRequest.followerUsername)
				&& Objects.equals(followingUsername, followRequest.followingUsername);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("followerUsername", followerUsername)
				.add("followingUsername", followingUsername)
				.toString();
	}
}
