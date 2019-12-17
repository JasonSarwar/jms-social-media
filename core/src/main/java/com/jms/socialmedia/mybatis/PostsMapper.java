package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.jms.socialmedia.model.Post;

public interface PostsMapper {

	int getNumberOfPosts();

	Collection<Post> getPosts(@Param(value="userIds") Collection<Integer> userIds, @Param(value="usernames") Collection<String> usernames, 
			@Param(value="tag") String tag, @Param(value="onDate") String onDate, 
			@Param(value="beforeDate") String beforeDate, @Param(value="afterDate") String afterDate, 
			@Param(value="sincePostId") Integer sincePostId,
			@Param(value="sortBy") String sortBy, @Param(value="sortOrderAsc") boolean sortOrderAsc);

	Post getPost(int postId);

	Integer getUserIdFromPostId(int postId);

	int addPost(Post post);

	int editPost(@Param(value="id") int postId, @Param(value="text") String postText);

	int deletePost(int postId);

	Collection<Post> getLikedPostsByUserId(int userId);

	Collection<String> getPostLikes(int postId);

	int likePost(@Param(value="postId") int postId, @Param(value="userId") Integer userId, @Param(value="username") String username);

	int unlikePost(@Param(value="postId") int postId, @Param(value="userId") Integer userId, @Param(value="username") String username);

	Collection<Post> getCommentedPostsByUserId(int userId);
}
