package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.model.FollowRequest;
import com.jms.socialmedia.model.Post;
import spark.Request;
import spark.Response;

public class FollowRequestHandler extends RequestHandler {

	public FollowRequestHandler(DataService dataService, Gson gson) {
		super(dataService, gson);
	}

	public Boolean handleFollowUser(Request request, Response response) throws IOException {
		FollowRequest followRequest = extractBodyContent(request, FollowRequest.class);
		authorizeRequest(request, followRequest.getFollowerUserId(), "Follow User");
		if (followRequest.getFollowerUserId().equals(followRequest.getFollowingUserId())) {
			throw new BadRequestException("A User cannot follow themselves");
		}
		return dataService.followUser(followRequest.getFollowerUserId(), followRequest.getFollowingUserId());
	}

	public Boolean handleUnfollowUser(Request request, Response response) throws IOException {
		FollowRequest followRequest = extractBodyContent(request, FollowRequest.class);
		authorizeRequest(request, followRequest.getFollowerUserId(), "Unollow User");
		return dataService.unfollowUser(followRequest.getFollowerUserId(), followRequest.getFollowingUserId());
	}

	public Collection<Integer> handleGetFollowingUserIds(Request request, Response response) {

		Integer userId = Integer.parseInt(request.params("userid"));
		return dataService.getFollowingUserIds(userId);
	}

	public Collection<Post> handleGetFollowingPosts(Request request, Response response) {

		Integer userId = Integer.parseInt(request.params("userid"));
		Collection<Integer> followingUserIds = dataService.getFollowingUserIds(userId);
		if (followingUserIds == null || followingUserIds.isEmpty()) {
			return Collections.emptySet();
		}
		return dataService.getPosts(followingUserIds, null, null, null, null, null);
	}

}
