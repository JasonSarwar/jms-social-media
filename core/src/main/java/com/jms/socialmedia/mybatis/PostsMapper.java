package com.jms.socialmedia.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;

public interface PostsMapper {

	int getNumberOfPosts();

	Collection<Post> getPosts(@Param(value="userId") Integer userId, @Param(value="username") String username, 
			@Param(value="tag") String tag, @Param(value="onDate") String onDate, 
			@Param(value="beforeDate") String beforeDate, @Param(value="afterDate")String afterDate);

	FullPost getPost(int postId);

	Integer getUserIdFromPostId(int postId);

	int addPost(Post post);

	int editPost(@Param(value="id") int postId, @Param(value="text") String postText);

	int deletePost(int postId);
}
