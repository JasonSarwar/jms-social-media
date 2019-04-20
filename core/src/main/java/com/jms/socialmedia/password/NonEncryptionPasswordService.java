package com.jms.socialmedia.password;

public class NonEncryptionPasswordService implements PasswordService {

	@Override
	public String encryptPassword(String password) {
		return password;
	}

	@Override
	public boolean checkPassword(String password, String hashedPassword) {
		return password.equals(hashedPassword);
	}

}
