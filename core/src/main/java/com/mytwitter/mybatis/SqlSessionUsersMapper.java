package com.mytwitter.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.mytwitter.model.AddUserDB;
import com.mytwitter.model.User;

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
	public User getUserLoginInfoByName(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserLoginInfoByName(username);
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
