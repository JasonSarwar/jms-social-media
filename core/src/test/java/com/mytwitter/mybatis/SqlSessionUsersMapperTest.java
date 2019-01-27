package com.mytwitter.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mytwitter.configuration.Configurations;
import com.mytwitter.configuration.ConfigurationsFromFile;
import com.mytwitter.configuration.CoreSettings;
import com.mytwitter.model.AddUserDB;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class SqlSessionUsersMapperTest {

	private static SqlSessionFactory factory;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException {
		Configurations configurations = new ConfigurationsFromFile("application.properties");
		InputStream inputStream = Resources.getResourceAsStream(configurations.get(CoreSettings.MYBATIS_CONFIG_FILE_PATH));
		factory = new SqlSessionFactoryBuilder().build(inputStream, configurations.getProperties());
	}
	
//	@Test
//	public void testIsUsernamePresent() {
//		try(SqlSession session = factory.openSession(true)) {
//			UsersMapper mapper = session.getMapper(UsersMapper.class);
//			assertThat(mapper.isUsernamePresent("Jason1"), equalTo(0));
//			assertThat(mapper.isUsernamePresent("Jason2"), equalTo(1));
//		}	
//	}
//	
//	@Test
//	public void testIsEmailPresent() {
//		try(SqlSession session = factory.openSession(true)) {
//			UsersMapper mapper = session.getMapper(UsersMapper.class);
//			assertThat(mapper.isEmailPresent("jason.sarwar@gmail.com"), equalTo(1));
//			assertThat(mapper.isEmailPresent("Jason2"), equalTo(0));
//		}
//		
//	}
	
//	@Test
//	public void testAddUser() {
//		try(SqlSession session = factory.openSession(true)) {
//			UsersMapper mapper = session.getMapper(UsersMapper.class);
//			
//			AddUserDB user = new AddUserDB();
//			user.setUsername("Jason2");
//			user.setHashedPassword("12345");
//			user.setEmail("jason.sarwar@gmail.com");
//			user.setFullName("Jason Sarwar");
//			user.setBirthdate(LocalDate.of(1993, 11, 20));
//			user.setBio("HI");
//			mapper.addUser(user);
//		}
//		
//	}

}
