package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.jms.socialmedia.model.AddUserDB;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;

public class SqlSessionUsersMapper implements UsersMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionUsersMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public int isUsernamePresent(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.isUsernamePresent(username);
		}
	}
	
	@Override
	public int isEmailPresent(String email) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.isEmailPresent(email);
		}
	}
	
	@Override
	public int addUser(AddUserDB addUserDb) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.addUser(addUserDb);
		}
	}

	@Override
	public Integer getUserIdByUsername(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserIdByUsername(username);
		}
	}

	@Override
	public UserPage getUserPageInfoByName(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserPageInfoByName(username);
		}
	}

	@Override
	public User getUserLoginInfoByString(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserLoginInfoByString(username);
		}
	}

	@Override
	public User getHashedPasswordByUserId(Integer userId) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getHashedPasswordByUserId(userId);
		}
	}

	@Override
	public Collection<User> getUsernamesByIds(Collection<Integer> userIds) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUsernamesByIds(userIds);
		}
	}

	@Override
	public int editPassword(Integer userId, String hashedPassword) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.editPassword(userId, hashedPassword);
		}
	}

	@Override
	public int addUserSession(int userId, String sessionKey) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.addUserSession(userId, sessionKey);
		}
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserBySessionKey(sessionKey);
		}
	}

	@Override
	public void removeSessionKey(String sessionKey) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			mapper.removeSessionKey(sessionKey);
		}
	}
}
