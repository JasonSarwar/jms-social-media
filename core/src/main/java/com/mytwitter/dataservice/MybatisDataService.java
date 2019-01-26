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
import com.mytwitter.mybatis.SqlSessionPostsMapper;
import com.mytwitter.mybatis.SqlSessionTagsMapper;
import com.mytwitter.mybatis.SqlSessionUsersMapper;
import com.mytwitter.utils.TagsUtils;

public class MybatisDataService implements DataService {

	private final SqlSessionUsersMapper usersMapper;
	private final SqlSessionPostsMapper postsMapper;
	private final SqlSessionTagsMapper tagsMapper;
	
	public MybatisDataService(Configurations configuration) throws IOException {
		InputStream inputStream = Resources.getResourceAsStream(configuration.get(CoreSettings.MYBATIS_CONFIG_FILE_PATH));
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, configuration.getProperties());
		usersMapper = new SqlSessionUsersMapper(factory);
		postsMapper = new SqlSessionPostsMapper(factory);
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
	public FullPost getPost(int postId) {
		FullPost post = postsMapper.getPost(postId);
		post.setComments(postsMapper.getComments(postId));
		return post;
	}

	@Override
	public Collection<Post> getPosts(Integer userId, String username, String tag, String onDate, String beforeDate, String afterDate) {
		return postsMapper.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		return postsMapper.getComments(postId);
	}

	@Override
	public boolean addPost(Post post) {
		List<String> tags = TagsUtils.extractTagsFromPost(post);
		boolean addedPost = postsMapper.addPost(post) != 0;
		boolean addedTags = tags.isEmpty() ? true : 
			tagsMapper.addTags(post.getPostId(), tags) == tags.size();
		return addedPost && addedTags;
	}
	
	@Override
	public boolean addComment(Comment comment) {
		return postsMapper.addComment(comment) != 0;
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
}
