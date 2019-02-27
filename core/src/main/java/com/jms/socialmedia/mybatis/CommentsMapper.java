package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.jms.socialmedia.model.Comment;

public interface CommentsMapper {

	int getNumberOfCommentsInPost(int postId);

	Collection<Comment> getComments(int postId);

	Collection<Comment> getCommentsByUserId(int userId);

	Comment getComment(int commentId);

	Integer getUserIdFromCommentId(int commentId);

	int addComment(Comment comment);
	
	int editComment(@Param(value="id") int commentId, @Param(value="text") String commentText);
	
	int deleteComment(int commentId);

	Collection<Integer> getCommentLikes(int commentId);

	int likeComment(@Param(value="commentId") int commentId, @Param(value="userId") int userId);

	int unlikeComment(@Param(value="commentId") int commentId, @Param(value="userId") int userId);
}
