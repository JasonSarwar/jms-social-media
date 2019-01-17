package dataservice;

import java.util.HashMap;
import java.util.Map;

import com.mytwitter.model.Post;

public class MapCachingDataService extends CachingDataService {

	private final Map<Integer, Post> postsById;
	
	public MapCachingDataService(DataService dataService) {
		super(dataService);
		postsById = new HashMap<>();
	}

	@Override
	protected Post getPostFromCache(int postId) {
		return postsById.get(postId);
	}

	@Override
	protected void putPostIntoCache(Post post) {
		postsById.put(post.getPostId(), post);
	}

	@Override
	protected void removePostFromCache(int postId) {
		postsById.remove(postId);
	}

}
