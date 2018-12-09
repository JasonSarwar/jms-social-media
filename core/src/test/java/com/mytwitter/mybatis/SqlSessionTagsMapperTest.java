package com.mytwitter.mybatis;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mytwitter.app.AppProperties;

public class SqlSessionTagsMapperTest {

	private static SqlSessionFactory factory;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream(AppProperties.MYBATIS_CONFIG_FILE_PATH);
		factory = new SqlSessionFactoryBuilder().build(inputStream, AppProperties.getProperties());
	}

	@Test
	public void testAddTags() {
		try(SqlSession session = factory.openSession(true)) {
			TagsMapper mapper = session.getMapper(TagsMapper.class);
			mapper.addTags(1, List.of("tag1", "tag2"));
		}
	}

	@Test
	public void testGetPostTags() {
		try(SqlSession session = factory.openSession(true)) {
			TagsMapper mapper = session.getMapper(TagsMapper.class);
			System.out.println(Arrays.toString(mapper.getPostTags(1).toArray()));
		}
	}

}
