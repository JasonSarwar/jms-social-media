package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.model.FollowRequest;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class FollowRequestHandler extends RequestHandler {

	private static final String USERNAME_PARAM = "username";
	private SecureRandom random = new SecureRandom();

	public FollowRequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
		super(dataService, tokenService, gson);
	}

	public Boolean handleFollowUser(Request request, Response response) throws IOException {
		FollowRequest followRequest = extractBodyContent(request, FollowRequest.class);
		authorizeRequest(request, followRequest.getFollowerUsername(), Permission.FOLLOW_USER);
		validateFollowRequest(followRequest, false);
		return dataService.followUser(null, followRequest.getFollowerUsername(), 
				null, followRequest.getFollowingUsername());
	}

	public Boolean handleUnfollowUser(Request request, Response response) throws IOException {
		FollowRequest followRequest = extractBodyContent(request, FollowRequest.class);
		authorizeRequest(request, followRequest.getFollowerUsername(), Permission.UNFOLLOW_USER);
		validateFollowRequest(followRequest, true);
		return dataService.unfollowUser(null, followRequest.getFollowerUsername(), 
				null, followRequest.getFollowingUsername());
	}

	public Collection<String> handleGetFollowingUsernames(Request request, Response response) {

		String username = request.params(USERNAME_PARAM);
		return dataService.getFollowingUsernames(username);
	}

	/**
	 * <h1> GET /api/user/:username/userstofollow </h1>
	 * Parameters
	 * <ul>
	 * 	<li> username - Username of the User who wants recommendations </li>
	 * </ul>
	 * 
	 * @param request
	 * @param response
	 * @return a collection of username recommendations
	 */
	public Collection<String> handleGetUsersToFollow(Request request, Response response) {

		String username = request.params(USERNAME_PARAM);
		List<String> usernames = new ArrayList<>(dataService.getUsernamesToFollow(username));

		int noOfUsers = usernames.size();

		String strMax = request.queryParams("max");
		int max = StringUtils.isBlank(strMax) ? noOfUsers : Integer.parseInt(strMax);
		max = max > noOfUsers ? noOfUsers : max;

		Collection<String> randomizedUsers = new HashSet<>(max);
		while (randomizedUsers.size() < max) {
			randomizedUsers.add(usernames.get(random.nextInt(noOfUsers)));
		}
		return randomizedUsers;
	}

	private void validateFollowRequest(FollowRequest followRequest, boolean isUnfollowRequest) {
		StringBuilder sb = new StringBuilder();
		checkParameter(followRequest.getFollowerUsername(), "Follow Request requires a 'followerUsername'", sb);
		checkParameter(followRequest.getFollowingUsername(), "Follow Request requires a 'followingUsername'", sb);
		throwExceptionIfNecessary(sb);
		checkParameter(followRequest.getFollowerUsername().trim().equalsIgnoreCase(followRequest.getFollowingUsername().trim()), 
				String.format("A User cannot %sfollow themself", isUnfollowRequest ? "un" : ""), sb);
		throwExceptionIfNecessary(sb);

	}
}
