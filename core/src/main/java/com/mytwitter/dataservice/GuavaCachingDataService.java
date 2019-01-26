package com.mytwitter.dataservice;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mytwitter.model.FullPost;

public class GuavaCachingDataService extends CachingDataService {

	private final Cache<Integer, FullPost> postsById;
	
	public GuavaCachingDataService(DataService dataService) {
		super(dataService);
		postsById = CacheBuilder.newBuilder().maximumSize(100).build();
	}

	@Override
	protected FullPost getPostFromCache(int postId) {
		return postsById.getIfPresent(postId);
	}

	@Override
	protected void putPostIntoCache(FullPost post) {
		postsById.put(post.getPostId(), post);
	}

	@Override
	protected void removePostFromCache(int postId) {
		postsById.invalidate(postId);
	}

}
