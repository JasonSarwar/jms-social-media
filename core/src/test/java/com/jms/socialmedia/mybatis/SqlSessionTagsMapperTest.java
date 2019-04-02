package com.jms.socialmedia.mybatis;

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

import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.ConfigurationsFromFile;
import com.jms.socialmedia.configuration.CoreSettings;

public class SqlSessionTagsMapperTest {

	private static SqlSessionFactory factory;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException {
		Configurations configurations = new ConfigurationsFromFile("application.properties");
		InputStream inputStream = Resources.getResourceAsStream(configurations.get(CoreSettings.MYBATIS_CONFIG_FILE_PATH));
		factory = new SqlSessionFactoryBuilder().build(inputStream, configurations.getProperties());
	}

//	@Test
//	public void testAddTags() {
//		try(SqlSession session = factory.openSession(true)) {
//			TagsMapper mapper = session.getMapper(TagsMapper.class);
//			mapper.addTags(1, List.of("tag1", "tag2"));
//		}
//	}

//	@Test
//	public void testGetPostTags() {
//		try(SqlSession session = factory.openSession(true)) {
//			TagsMapper mapper = session.getMapper(TagsMapper.class);
//			System.out.println(Arrays.toString(mapper.getPostTags(1).toArray()));
//		}
//	}

}
