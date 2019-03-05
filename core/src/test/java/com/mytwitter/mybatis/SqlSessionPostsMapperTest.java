package com.mytwitter.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.ConfigurationsFromFile;
import com.jms.socialmedia.configuration.CoreSettings;
import com.jms.socialmedia.model.Post;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SqlSessionPostsMapperTest {

	private static SqlSessionFactory factory;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException {
		Configurations configurations = new ConfigurationsFromFile("application.properties");
		InputStream inputStream = Resources.getResourceAsStream(configurations.get(CoreSettings.MYBATIS_CONFIG_FILE_PATH));
		factory = new SqlSessionFactoryBuilder().build(inputStream, configurations.getProperties());
	}

	@Before
	public void setUp() throws Exception {
	}

//	@Test
//	public void testNumberOfPosts() {
//		try(SqlSession session = factory.openSession(true)) {
//			PostsMapper mapper = session.getMapper(PostsMapper.class);
//			System.out.println(mapper.getNumberOfPosts());
//		}
//	}
//	
//	@Test
//	public void testGetPost() {
//		try(SqlSession session = factory.openSession(true)) {
//			PostsMapper mapper = session.getMapper(PostsMapper.class);
//			FullPost post = mapper.getPost(1);
//			System.out.println(post.getTimestamp());
//			System.out.println(post.getFullName());
//			assertThat(post.getPostId(), equalTo(1));
//			assertThat(post.getUserId(), equalTo(1));
//			assertThat(post.getUsername(), equalTo("Jason2"));
//			assertThat(post.getFullName(), equalTo("Jason Sarwar"));
//			assertThat(post.getText(), equalTo("First post!!"));
//			assertThat(post.getPostId(), equalTo(1));
//			System.out.println(post.getTimestamp());
//		}
//	}

//	@Test
//	public void testAddPost() {
//		try(SqlSession session = factory.openSession(true)) {
//			PostsMapper mapper = session.getMapper(PostsMapper.class);
//			Post post = new Post();
//			post.setUserId(1);
//			//post.setPostId(3);
//			post.setText("Third post!!");
//			mapper.addPost(post);
//			System.out.println(post.getPostId());
//		}
//	}

}
