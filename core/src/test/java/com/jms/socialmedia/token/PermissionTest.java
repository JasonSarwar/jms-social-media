package com.jms.socialmedia.token;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class PermissionTest {

	@Test
	public void testGetAction() {
		assertThat(Permission.ADMIN.getAction(), is("Admin"));
	}

	@Test
	public void testGetRegularPermissions() {
		List<Permission> permissions = new ArrayList<>(Arrays.asList(Permission.values()));
		permissions.remove(Permission.ADMIN);
		assertThat(Permission.getRegularPermissions(), is(permissions.toArray()));
	}

}
