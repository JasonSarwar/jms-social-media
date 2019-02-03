package com.jms.socialmedia.password;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptPasswordService implements PasswordService {

	public BcryptPasswordService() {
	}

	@Override
	public String encryptPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(10));
	}

	@Override
	public boolean checkPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}

}
