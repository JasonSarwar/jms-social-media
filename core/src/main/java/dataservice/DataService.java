package dataservice;

import java.util.Collection;

import com.mytwitter.model.Post;
import com.mytwitter.model.User;
import com.mytwitter.model.UserObject;

public interface DataService {

	UserObject getUser(String username);
	
	User getUserLoginInfo(String username);
	
	String getUsers();
	
	String getUsersCount();
	
	String createUser();
	
	int addUserSession(int userId, String sessionKey);
	
	User getUserBySessionKey(String sessionKey);
	
	void removeSessionKey(String sessionKey);
	
	Post getPost(int postId);
	
	Collection<Post> getPosts(int userId, String username, String tag, String onDate, String beforeDate, String afterDate);
	
	boolean addPost(Post post);
}
