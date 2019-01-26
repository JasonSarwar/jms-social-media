package com.mytwitter.dataservice;

import java.util.HashMap;
import java.util.Map;

import com.mytwitter.model.FullPost;

public class MapCachingDataService extends CachingDataService {

	private final Map<Integer, FullPost> postsById;
	
	public MapCachingDataService(DataService dataService) {
		super(dataService);
		postsById = new HashMap<>();
	}

	@Override
	protected FullPost getPostFromCache(int postId) {
		return postsById.get(postId);
	}

	@Override
	protected void putPostIntoCache(FullPost post) {
		postsById.put(post.getPostId(), post);
	}

	@Override
	protected void removePostFromCache(int postId) {
		postsById.remove(postId);
	}

}
