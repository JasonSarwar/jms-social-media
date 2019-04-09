package com.jms.socialmedia.token;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class Token {

	private final Integer userId;
	private final Set<Permission> permissions;

	public Token(Builder b) {
		this.userId = b.userId;
		this.permissions = EnumSet.copyOf(b.permissions);
	}

	public final Integer getUserId() {
		return userId;
	}

	public final Set<Permission> getPermissions() {
		return permissions;
	}

	public final boolean hasPermission(Permission permission) {
		return permissions.contains(permission);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {
		private Integer userId;
		private Set<Permission> permissions = new HashSet<>();
		
		public Builder setUserId(Integer userId) {
			this.userId = userId;
			return this;
		}
		
		public Builder setPermission(Permission... permissions) {
			Collections.addAll(this.permissions, permissions);
			return this;
		}

		public Builder setPermission(Set<Permission> permissions) {
			this.permissions.addAll(permissions);
			return this;
		}

		public Token build() {
			return new Token(this);
		}
	}

}
