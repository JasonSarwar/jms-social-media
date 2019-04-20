package com.jms.socialmedia.token;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
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

	@Override
	public int hashCode() {
		return Objects.hash(userId, permissions);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (this == null || this.getClass() != object.getClass())
			return false;

		Token that = (Token) object;
		return Objects.equals(this.userId, that.userId) && Objects.equals(this.permissions, that.permissions);
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

		public Builder addPermissions(Permission... permissions) {
			Collections.addAll(this.permissions, permissions);
			return this;
		}

		public Builder addPermissions(Set<Permission> permissions) {
			this.permissions.addAll(permissions);
			return this;
		}

		public Token build() {
			return new Token(this);
		}
	}

}
