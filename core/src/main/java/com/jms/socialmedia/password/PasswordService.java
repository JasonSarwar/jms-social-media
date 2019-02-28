package com.jms.socialmedia.password;

import com.jms.socialmedia.model.ChangePassword;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.LoginRequest;

public interface PasswordService {
	
	String encryptPassword(String password);
	
	boolean checkPassword(String password, String hashedPassword);
	
	default boolean checkPassword(LoginRequest loginRequest, User user) {
		return checkPassword(loginRequest.getPassword(), user.getHashedPassword());
	}

	default boolean checkPassword(ChangePassword changePassword, User user) {
		return checkPassword(changePassword.getOldPassword(), user.getHashedPassword());
	}
}
