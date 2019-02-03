package com.mytwitter.password;

public class NonEncryptionPasswordService implements PasswordService {

	public NonEncryptionPasswordService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String encryptPassword(String password) {
		return password;
	}

	@Override
	public boolean checkPassword(String password, String hashedPassword) {
		return password.equals(hashedPassword);
	}

}
