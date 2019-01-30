package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.mytwitter.model.Comment;

public interface CommentsMapper {

	int getNumberOfCommentsInPost(int postId);

	Collection<Comment> getComments(int postId);

	Comment getComment(int commentId);

	Integer getUserIdFromCommentId(int commentId);

	int addComment(Comment comment);
	
	int editComment(@Param(value="id") int commentId, @Param(value="text") String commentText);
	
	int deleteComment(int commentId);
}
