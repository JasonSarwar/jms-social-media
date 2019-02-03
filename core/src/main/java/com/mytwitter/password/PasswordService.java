package com.mytwitter.password;

import com.mytwitter.model.ChangePassword;
import com.mytwitter.model.User;
import com.mytwitter.model.UserLogin;

public interface PasswordService {
	
	String encryptPassword(String password);
	
	boolean checkPassword(String password, String hashedPassword);
	
	default boolean checkPassword(UserLogin userLogin, User user) {
		return checkPassword(userLogin.getPassword(), user.getHashedPassword());
	}

	default boolean checkPassword(ChangePassword changePassword, User user) {
		return checkPassword(changePassword.getOldPassword(), user.getHashedPassword());
	}
}
