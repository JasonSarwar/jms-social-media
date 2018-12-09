package com.mytwitter.password;

public interface PasswordService {
	
	String encryptPassword(String password);
	
	String decryptPassword(String password);
	
	
}
