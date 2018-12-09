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
	public User getUserLoginInfo(String username) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserLoginInfo(username);
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
	public Integer getUserIdBySessionKey(String sessionKey) {
		try (SqlSession session = sessionfactory.openSession(true)) {
			UsersMapper mapper = session.getMapper(UsersMapper.class);
			return mapper.getUserIdBySessionKey(sessionKey);
		}
	}

}
