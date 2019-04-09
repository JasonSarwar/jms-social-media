package com.jms.socialmedia.token;

public enum Permission {
	ADD_POST("Add Post"),
	EDIT_POST("Edit Post"),
	DELETE_POST("Delete Post"),
	LIKE_POST("Like Post"),
	UNLIKE_POST("Unlike Post"),
	ADD_COMMENT("Add Comment"),
	EDIT_COMMENT("Edit Comment"),
	DELETE_COMMENT("Delete Comment"),
	LIKE_COMMENT("Like Comment"),
	UNLIKE_COMMENT("Unlike Comment"),
	FOLLOW_USER("Follow User"),
	UNFOLLOW_USER("Unfollow User"),
	EDIT_PASSWORD("Edit Password"),
	ADMIN("Admin");

	private final String action;

	private Permission(String action) {
		this.action = action;
	}

	public final String getAction() {
		return action;
	}

	public static Permission[] getRegularPermissions() {
		return new Permission[] {ADD_POST, EDIT_POST, DELETE_POST, LIKE_POST, UNLIKE_POST, ADD_COMMENT, EDIT_COMMENT, DELETE_COMMENT, LIKE_COMMENT, UNLIKE_COMMENT, FOLLOW_USER, UNFOLLOW_USER, EDIT_PASSWORD};
	}
}