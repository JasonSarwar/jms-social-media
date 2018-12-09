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
	public String decryptPassword(String password) {
		return password;
	}

}
