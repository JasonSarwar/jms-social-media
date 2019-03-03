package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.model.ChangePassword;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;
import com.jms.socialmedia.password.PasswordService;

import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class UserRequestHandler extends RequestHandler {

	private final PasswordService passwordService;
	
	public UserRequestHandler(DataService dataService, PasswordService passwordService, Gson gson) {
		super(dataService, gson);
		this.passwordService = passwordService;
	}

	public UserPage handleGetUserPage(Request request, Response response) {
		String username = request.params("username");
		UserPage userPage = dataService.getUserPageInfoByName(username);
		int userId = userPage.getUserId();
		userPage.addFollowersUserIds(dataService.getFollowerUserIds(userId));
		userPage.addFollowingUserIds(dataService.getFollowingUserIds(userId));
		return userPage;
	}
	
	public Collection<User> handleGetUsernamesAndIds(Request request, Response response) throws IOException {
		String queryParam = request.queryParams("ids");
		if (StringUtils.isBlank(queryParam)) {
			throw new BadRequestException("No User IDs included");
		}
		Collection<Integer> userIds = Arrays.stream(queryParam.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		return dataService.getUsernamesByIds(userIds);
	}
	
	public Boolean handleEditUserPassword(Request request, Response response) throws IOException {
		ChangePassword changePassword = extractBodyContent(request, ChangePassword.class);
		
		authorizeRequest(request, changePassword.getUserId(), "Edit Password");
		
		User user = dataService.getHashedPasswordByUserId(changePassword.getUserId());
		if (!passwordService.checkPassword(changePassword, user)) {
			throw new BadRequestException("Incorrect Old Password");
		}

		int len = changePassword.getNewPassword().length();
		if (len < 5) {
			throw new BadRequestException("New Password Too Short");
		}
		if (len > 50) {
			throw new BadRequestException("New Password Too Long");
		}
		
		return dataService.editPassword(changePassword.getUserId(), 
				passwordService.encryptPassword(changePassword.getNewPassword()));
	}
}
