package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.jms.socialmedia.model.NewUser;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

public interface UsersMapper {

	int isUsernameTaken(String username);

	int isEmailTaken(String email);

	int addUser(NewUser newUser);

	Integer getUserIdByUsername(String username);

	UserPage getUserPageInfoByName(String username);

	User getUserLoginInfoByString(String usernameOrEmail);

	User getHashedPasswordByUserId(Integer userId);

	Collection<User> getUsernamesByIds(Collection<Integer> userIds);

	int editPassword(@Param(value="id") Integer userId, @Param(value="hashedPassword") String hashedPassword);

	int addUserSession(@Param(value="userId") int userId, @Param(value="sessionKey") String sessionKey);

	User getUserBySessionKey(String sessionKey);

	void removeSessionKey(String sessionKey);
}
