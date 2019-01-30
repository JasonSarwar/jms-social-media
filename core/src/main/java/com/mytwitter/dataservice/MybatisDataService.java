package com.mytwitter.dataservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mytwitter.configuration.Configurations;
import com.mytwitter.configuration.CoreSettings;
import com.mytwitter.model.Comment;
import com.mytwitter.model.FullPost;
import com.mytwitter.model.Post;
import com.mytwitter.model.User;
import com.mytwitter.model.UserObject;
import com.mytwitter.mybatis.CommentsMapper;
import com.mytwitter.mybatis.PostsMapper;
import com.mytwitter.mybatis.SqlSessionCommentsMapper;
import com.mytwitter.mybatis.SqlSessionPostsMapper;
import com.mytwitter.mybatis.SqlSessionTagsMapper;
import com.mytwitter.mybatis.SqlSessionUsersMapper;
import com.mytwitter.mybatis.TagsMapper;
import com.mytwitter.mybatis.UsersMapper;
import com.mytwitter.utils.TagsUtils;

public class MybatisDataService implements DataService {

	private final UsersMapper usersMapper;
	private final PostsMapper postsMapper;
	private final CommentsMapper commentsMapper;
	private final TagsMapper tagsMapper;
	
	public MybatisDataService(Configurations configuration) throws IOException {
		InputStream inputStream = Resources.getResourceAsStream(configuration.get(CoreSettings.MYBATIS_CONFIG_FILE_PATH));
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, configuration.getProperties());
		usersMapper = new SqlSessionUsersMapper(factory);
		postsMapper = new SqlSessionPostsMapper(factory);
		commentsMapper = new SqlSessionCommentsMapper(factory);
		tagsMapper = new SqlSessionTagsMapper(factory);
	}

	@Override
	public User getUserLoginInfo(String username) {
		return usersMapper.getUserLoginInfo(username);
	}
	
	@Override
	public UserObject getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsersCount() {
		// TODO Auto-generated method stub
		return null;
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
	public Collection<Post> getPosts(Integer userId, String username, String tag, String onDate, String beforeDate, String afterDate) {
		return postsMapper.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
	}

	@Override
	public Post getPost(int postId) {
		return postsMapper.getPost(postId);
	}
	
	@Override
	public FullPost getPostWithComments(int postId) {
		FullPost post = postsMapper.getPost(postId);
		if (post != null) {
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
		List<String> tags = TagsUtils.extractTagsFromPost(post);
		boolean addedPost = postsMapper.addPost(post) == 1;
		boolean addedTags = tags.isEmpty() ? true : 
			tagsMapper.addTags(post.getPostId(), tags) == tags.size();
		return addedPost && addedTags;
	}

	@Override
	public boolean editPost(int postId, String postText) {
		tagsMapper.removePostTags(postId);
		List<String> tags = TagsUtils.extractTags(postText);
		boolean addedTags = tags.isEmpty() ? true : 
			tagsMapper.addTags(postId, tags) == tags.size();
		return postsMapper.editPost(postId, postText) == 1 && addedTags;
	}

	@Override
	public boolean deletePost(int postId) {
		return postsMapper.deletePost(postId) == 1;
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		return commentsMapper.getComments(postId);
	}

	@Override
	public Comment getComment(int commentId) {
		return commentsMapper.getComment(commentId);
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
}
