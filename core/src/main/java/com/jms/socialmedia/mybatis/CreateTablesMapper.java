package com.jms.socialmedia.mybatis;

public interface CreateTablesMapper {

	int createUsersTable();

	int createPostsTable();

	int createCommentsTable();

	int createUserSessionsTable();

	int createFollowersTable();

	int createPostTagsTable();

	int createPostLikesTable();

	int createCommentLikesTable();

	int createPostMentionsTable();

	int createCommentMentionsTable();

}
