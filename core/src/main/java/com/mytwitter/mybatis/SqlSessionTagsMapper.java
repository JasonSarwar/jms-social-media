package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlSessionTagsMapper implements TagsMapper {

	private final SqlSessionFactory sessionfactory;
	
	public SqlSessionTagsMapper(SqlSessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public Collection<String> getTags() {
		try(SqlSession session = sessionfactory.openSession(true)) {
			TagsMapper mapper = session.getMapper(TagsMapper.class);
			return mapper.getTags();
		}
	}

	@Override
	public Collection<String> getPostTags(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			TagsMapper mapper = session.getMapper(TagsMapper.class);
			return mapper.getPostTags(postId);
		}
	}

	@Override
	public int addTags(int postId, Collection<String> tags) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			TagsMapper mapper = session.getMapper(TagsMapper.class);
			return mapper.addTags(postId, tags);
		}
	}

	@Override
	public int removePostTags(int postId) {
		try(SqlSession session = sessionfactory.openSession(true)) {
			TagsMapper mapper = session.getMapper(TagsMapper.class);
			return mapper.removePostTags(postId);
		}
	}
}
