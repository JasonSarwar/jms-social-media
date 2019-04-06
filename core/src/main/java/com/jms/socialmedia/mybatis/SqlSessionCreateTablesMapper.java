package com.jms.socialmedia.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlSessionCreateTablesMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionCreateTablesMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	public int createTables() {
		try (SqlSession session = sessionfactory.openSession(true)) {
			CreateTablesMapper mapper = session.getMapper(CreateTablesMapper.class);
			return mapper.createUsersTable()
					+ mapper.createPostsTable()
					+ mapper.createCommentsTable()
					+ mapper.createUserSessionsTable()
					+ mapper.createFollowersTable()
					+ mapper.createPostTagsTable()
					+ mapper.createPostLikesTable()
					+ mapper.createCommentLikesTable()
					+ mapper.createPostMentionsTable()
					+ mapper.createCommentMentionsTable();
		}
	}
}
