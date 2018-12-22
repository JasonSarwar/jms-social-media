package dataservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.mytwitter.app.AppProperties;
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
	
	public MybatisDataService() throws IOException {
		InputStream inputStream = Resources.getResourceAsStream(AppProperties.MYBATIS_CONFIG_FILE_PATH);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, AppProperties.getProperties());
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
	public Post getPost(int postId) {
		Post post = postsMapper.getPost(postId);
		post.setTags(tagsMapper.getPostTags(postId));
		post.setReplies(postsMapper.getReplies(postId));
		return post;
	}

	@Override
	public Collection<Post> getPosts(int userId, String username, String tag, String onDate, String beforeDate, String afterDate) {
		Collection<Post> posts = postsMapper.getPosts(userId, username, tag, onDate, beforeDate, afterDate);
		posts.forEach(post -> post.setTags(tagsMapper.getPostTags(post.getPostId())));
		return posts;
	}

	@Override
	public boolean addPost(Post post) {
		List<String> tags = TagsUtils.extractTagsFromPost(post);
		boolean addedPost = postsMapper.addPost(post) != 0;
		boolean handledTags = tags.isEmpty() ? true : 
			tagsMapper.addTags(post.getPostId(), tags) == tags.size();
		return addedPost && handledTags;
	}

	@Override
	public int addUserSession(int userId, String sessionKey) {
		return usersMapper.addUserSession(userId, sessionKey);
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
