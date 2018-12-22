package dataservice;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.model.Post;
import com.mytwitter.model.User;
import com.mytwitter.model.UserObject;

public class MockDataService implements DataService {

	private Gson gson;
	private Map<String, UserObject> usersByUsername;
	
	public MockDataService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
		usersByUsername = new HashMap<>();
		UserObject userObject = UserObject.newBuilder()
				.setUserId(1)
				.setUsername("jasonsarwar")
				.setFirstName("Jason")
				.setLastName("Sarwar")
				.setEmailAddress("jason_sarwar@yahoo.com")
				.setBirthdate("1993-11-20")
				.setPasswordHash("012345")
				.setPasswordkey("543210")
				.setTimeOfCreation(LocalDateTime.now())
				.build();
		usersByUsername.put(userObject.getUsername(), userObject);
		
		userObject = UserObject.newBuilder()
				.setUserId(2)
				.setUsername("rowansarwar")
				.setFirstName("Rowan")
				.setLastName("Sarwar")
				.setEmailAddress("rowan_sarwar@yahoo.com")
				.setBirthdate("1998-01-15")
				.setPasswordHash("012345")
				.setPasswordkey("543210")
				.setTimeOfCreation(LocalDateTime.now())
				.build();
		
		usersByUsername.put(userObject.getUsername(), userObject);
	}
	
	@Override
	public UserObject getUser(String username) {
		return usersByUsername.get(username);
	}

	@Override
	public String getUsers() {
		return gson.toJson(usersByUsername);
	}
	
	@Override
	public String getUsersCount() {
		return "{\"userCount\": " + Integer.toString(usersByUsername.size()) + "}";
	}
	
	@Override
	public String createUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Post getPost(int postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Post> getPosts(int userId, String username, String tag, String onDate, String beforeDate, String afterDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addPost(Post post) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUserLoginInfo(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addUserSession(int userId, String sessionKey) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSessionKey(String sessionKey) {
		// TODO Auto-generated method stub
		
	}

}
