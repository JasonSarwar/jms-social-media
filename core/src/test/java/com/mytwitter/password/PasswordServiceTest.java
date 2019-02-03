package com.mytwitter.password;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.password.BcryptPasswordService;
import com.jms.socialmedia.password.NonEncryptionPasswordService;
import com.jms.socialmedia.password.PasswordService;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class PasswordServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordServiceTest.class);
	private static final String SIMPLE_PASSWORD = "12345";
	private static final String COMPLEX_PASSWORD = "sfgEIOQWMcnioQPGnhjSNMKX./,'><?1#%#$23&$#45hdTESDFG2$@#,/<>?::";
	
	@Parameters(name = "{0}")
	public static Object[] data() {
		return new Object[] {
				new NonEncryptionPasswordService(),
				new BcryptPasswordService()};
	}
	
	@Parameter
	public PasswordService passwordService;
	
	@Test
	public void testSimplePassword() {
		String hashedPassword = passwordService.encryptPassword(SIMPLE_PASSWORD);
		LOGGER.info("{}: {} to {}", passwordService.getClass().getSimpleName(), SIMPLE_PASSWORD, hashedPassword);
		assertThat(passwordService.checkPassword(SIMPLE_PASSWORD, hashedPassword), is(true));
	}

	@Test
	public void testComplexPassword() {
		String hashedPassword = passwordService.encryptPassword(COMPLEX_PASSWORD);
		LOGGER.info("{}: {} to {}", passwordService.getClass().getSimpleName(), COMPLEX_PASSWORD, hashedPassword);
		assertThat(passwordService.checkPassword(COMPLEX_PASSWORD, hashedPassword), is(true));
	}
	
}
