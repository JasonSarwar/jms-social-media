package com.jms.socialmedia.dataservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.CoreSettings;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserPage;
import com.jms.socialmedia.mybatis.CommentsMapper;
import com.jms.socialmedia.mybatis.FollowersMapper;
import com.jms.socialmedia.mybatis.PostsMapper;
import com.jms.socialmedia.mybatis.SqlSessionCommentsMapper;
import com.jms.socialmedia.mybatis.SqlSessionFollowersMapper;
import com.jms.socialmedia.mybatis.SqlSessionPostsMapper;
import com.jms.socialmedia.mybatis.SqlSessionTagsMapper;
import com.jms.socialmedia.mybatis.SqlSessionUsersMapper;
import com.jms.socialmedia.mybatis.TagsMapper;
import com.jms.socialmedia.mybatis.UsersMapper;
import com.jms.socialmedia.utils.TagsUtils;

public class MybatisDataService implements DataService {

	private final UsersMapper usersMapper;
	private final PostsMapper postsMapper;
	private final CommentsMapper commentsMapper;
	private final TagsMapper tagsMapper;
	private final FollowersMapper followersMapper;

	public MybatisDataService(Configurations configuration) throws IOException {
		InputStream inputStream = Resources.getResourceAsStream(configuration.get(CoreSettings.MYBATIS_CONFIG_FILE_PATH));
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, configuration.getProperties());
		usersMapper = new SqlSessionUsersMapper(factory);
		postsMapper = new SqlSessionPostsMapper(factory);
		commentsMapper = new SqlSessionCommentsMapper(factory);
		tagsMapper = new SqlSessionTagsMapper(factory);
		followersMapper = new SqlSessionFollowersMapper(factory);
	}

	@Override
	public UserPage getUserPageInfoByName(String username) {
		return usersMapper.getUserPageInfoByName(username);
	}

	@Override
	public User getUserLoginInfoByString(String username) {
		return usersMapper.getUserLoginInfoByString(username);
	}

	@Override
	public User getHashedPasswordByUserId(Integer userId) {
		return usersMapper.getHashedPasswordByUserId(userId);
	}

	@Override
	public Collection<User> getUsernamesByIds(Collection<Integer> userIds) {
		return usersMapper.getUsernamesByIds(userIds);
	}

	@Override
	public boolean editPassword(Integer userId, String hashedPassword) {
		return usersMapper.editPassword(userId, hashedPassword) == 1;
	}

	@Override
	public String createUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addUserSession(int userId, String sessionKey) {
		return usersMapper.addUserSession(userId, sessionKey) == 1;
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		return usersMapper.getUserBySessionKey(sessionKey);
	}

	@Override
	public void removeSessionKey(String sessionKey) {
		usersMapper.removeSessionKey(sessionKey);
	}

	@Override
	public Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate, String beforeDate, String afterDate) {
		Collection<Post> posts = postsMapper.getPosts(userIds, username, tag, onDate, beforeDate, afterDate);
		posts.forEach(post -> post.setLikes(getPostLikes(post.getPostId())));
		return posts;
	}

	@Override
	public Post getPost(int postId) {
		Post post = postsMapper.getPost(postId);
		if (post != null) {
			post.setLikes(getPostLikes(postId));
		}
		return post;
	}
	
	@Override
	public FullPost getPostWithComments(int postId) {
		FullPost post = postsMapper.getPost(postId);
		if (post != null) {
			post.setLikes(getPostLikes(postId));
			post.setComments(getComments(postId));
		}
		return post;
	}

	@Override
	public Integer getUserIdFromPostId(int postId) {
		return postsMapper.getUserIdFromPostId(postId);
	}

	@Override
	public boolean addPost(Post post) {
		Collection<String> tags = TagsUtils.extractTagsFromPost(post);
		boolean addedPost = postsMapper.addPost(post) == 1;
		boolean addedTags = tags.isEmpty() ? true : 
			tagsMapper.addTags(post.getPostId(), tags) == tags.size();
		return addedPost && addedTags;
	}

	@Override
	public boolean editPost(int postId, String postText) {
		tagsMapper.removePostTags(postId);
		Collection<String> tags = TagsUtils.extractTags(postText);
		boolean addedTags = tags.isEmpty() ? true : 
			tagsMapper.addTags(postId, tags) == tags.size();
		return postsMapper.editPost(postId, postText) == 1 && addedTags;
	}

	@Override
	public boolean deletePost(int postId) {
		return postsMapper.deletePost(postId) == 1;
	}

	@Override
	public Collection<Post> getCommentedPostsByUserId(int userId) {
		Collection<Post> posts = postsMapper.getCommentedPostsByUserId(userId);
		posts.forEach(post -> post.setLikes(getPostLikes(post.getPostId())));
		return posts;
	}

	@Override
	public Collection<Post> getLikedPostsByUserId(int userId) {
		Collection<Post> posts = postsMapper.getLikedPostsByUserId(userId);
		posts.forEach(post -> post.setLikes(getPostLikes(post.getPostId())));
		return posts;
	}

	@Override
	public Collection<Integer> getPostLikes(int postId) {
		return postsMapper.getPostLikes(postId);
	}

	@Override
	public boolean likePost(int postId, int userId) {
		return postsMapper.likePost(postId, userId) == 1;
	}

	@Override
	public boolean unlikePost(int postId, int userId) {
		return postsMapper.unlikePost(postId, userId) == 1;
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		Collection<Comment> comments = commentsMapper.getComments(postId);
		comments.forEach(comment -> comment.setLikes(getCommentLikes(comment.getCommentId())));
		return comments;
	}

	@Override
	public Collection<Comment> getCommentsByUserId(int userId) {
		Collection<Comment> comments = commentsMapper.getCommentsByUserId(userId);
		comments.forEach(comment -> comment.setLikes(getCommentLikes(comment.getCommentId())));
		return comments;
	}

	@Override
	public Comment getComment(int commentId) {
		Comment comment = commentsMapper.getComment(commentId);
		if (comment != null) {
			comment.setLikes(getCommentLikes(commentId));
		}
		return comment;
	}

	@Override
	public Integer getUserIdFromCommentId(int commentId) {
		return commentsMapper.getUserIdFromCommentId(commentId);
	}

	@Override
	public boolean addComment(Comment comment) {
		return commentsMapper.addComment(comment) == 1;
	}

	@Override
	public boolean editComment(int commentId, String commentText) {
		return commentsMapper.editComment(commentId, commentText) == 1;
	}

	@Override
	public boolean deleteComment(int commentId) {
		return commentsMapper.deleteComment(commentId) == 1;
	}
	
	@Override
	public Collection<Integer> getCommentLikes(int commentId) {
		return commentsMapper.getCommentLikes(commentId);
	}

	@Override
	public boolean likeComment(int commentId, int userId) {
		return commentsMapper.likeComment(commentId, userId) == 1;
	}

	@Override
	public boolean unlikeComment(int commentId, int userId) {
		return commentsMapper.unlikeComment(commentId, userId) == 1;
	}

	@Override
	public Collection<Integer> getFollowerUserIds(int userId) {
		return followersMapper.getFollowerUserIds(userId);
	}

	@Override
	public Collection<Integer> getFollowingUserIds(int userId) {
		return followersMapper.getFollowingUserIds(userId);
	}

	@Override
	public boolean followUser(int followerUserId, int followingUserId) {
		return followersMapper.followUser(followerUserId, followingUserId) == 1;
	}

	@Override
	public boolean unfollowUser(int followerUserId, int followingUserId) {
		return followersMapper.unfollowUser(followerUserId, followingUserId) == 1;
	}
}
