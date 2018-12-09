package com.mytwitter.mybatis;

import org.apache.ibatis.annotations.Param;

import com.mytwitter.model.AddUserDB;
import com.mytwitter.model.User;

public interface UsersMapper {

	int isUsernamePresent(String username);
	
	int isEmailPresent(String email);
	
	int addUser(AddUserDB addUserDb);
	
	User getUserLoginInfo(String username);
	
	int addUserSession(@Param(value="userId") int userId, @Param(value="sessionKey") String sessionKey);
	
	Integer getUserIdBySessionKey(String sessionKey);
}
