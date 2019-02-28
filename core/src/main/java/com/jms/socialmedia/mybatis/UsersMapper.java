package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.jms.socialmedia.model.AddUserDB;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

public interface UsersMapper {

	int isUsernamePresent(String username);

	int isEmailPresent(String email);

	int addUser(AddUserDB addUserDb);
	
	UserPage getUserPageInfoByName(String username);

	User getUserLoginInfoByName(String username);

	User getHashedPasswordByUserId(Integer userId);

	Collection<String> getUsernamesByIds(Collection<Integer> userIds);

	int editPassword(@Param(value="id") Integer userId, @Param(value="hashedPassword") String hashedPassword);

	int addUserSession(@Param(value="userId") int userId, @Param(value="sessionKey") String sessionKey);

	User getUserBySessionKey(String sessionKey);

	void removeSessionKey(String sessionKey);
}
