package com.jms.socialmedia.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

public class NewUserTest {

	@Test
	public void testEqualsObjectAndHashCode() {
		NewUser newUser = new NewUser();

		assertThat(newUser.equals(newUser), is(true));
		assertThat(newUser.equals(null), is(false));
		assertThat(newUser.equals(new User()), is(false));

		assertThat(newUser.toString(), is("NewUser{userId=null, username=null, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=null, email=null, "
				+ "birthDate=null, profilePictureLink=null}"));

		NewUser newUser2 = new NewUser();

		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		newUser.setUserId(1);
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setUserId(1);
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		assertThat(newUser.toString(), is("NewUser{userId=1, username=null, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=null, email=null, "
				+ "birthDate=null, profilePictureLink=null}"));

		newUser.setUsername("Username");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setUsername("Username");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		assertThat(newUser.toString(), is("NewUser{userId=1, username=Username, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=null, email=null, "
				+ "birthDate=null, profilePictureLink=null}"));

		newUser.setPassword1("password");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setPassword1("password");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		newUser.setPassword2("password");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setPassword2("password");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		newUser.setHashedPassword("password");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setHashedPassword("password");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		assertThat(newUser.toString(), is("NewUser{userId=1, username=Username, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=null, email=null, "
				+ "birthDate=null, profilePictureLink=null}"));

		newUser.setFullName("Full Name");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setFullName("Full Name");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		assertThat(newUser.toString(), is("NewUser{userId=1, username=Username, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=Full Name, email=null, "
				+ "birthDate=null, profilePictureLink=null}"));

		newUser.setEmail("email@web.com");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setEmail("email@web.com");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		assertThat(newUser.toString(), is("NewUser{userId=1, username=Username, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=Full Name, email=email@web.com, "
				+ "birthDate=null, profilePictureLink=null}"));

		newUser.setBirthDate(LocalDate.of(1990, 6, 23));
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setBirthDate(LocalDate.of(1990, 6, 23));
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));

		assertThat(newUser.toString(), is("NewUser{userId=1, username=Username, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=Full Name, email=email@web.com, "
				+ "birthDate=1990-06-23, profilePictureLink=null}"));

		newUser.setProfilePictureLink("link");
		assertThat(newUser2.equals(newUser), is(false));
		assertThat(newUser2.hashCode(), is(not(newUser.hashCode())));

		newUser2.setProfilePictureLink("link");
		assertThat(newUser2.equals(newUser), is(true));
		assertThat(newUser2.hashCode(), is(newUser.hashCode()));
		
		assertThat(newUser.toString(), is("NewUser{userId=1, username=Username, "
				+ "password1=*****, password2=*****, hashedPassword=*****, "
				+ "fullName=Full Name, email=email@web.com, "
				+ "birthDate=1990-06-23, profilePictureLink=link}"));
	}

}
