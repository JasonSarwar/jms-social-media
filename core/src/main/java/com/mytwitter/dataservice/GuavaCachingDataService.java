package com.mytwitter.dataservice;

import java.util.HashMap;
import java.util.Map;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mytwitter.model.Comment;
import com.mytwitter.model.FullPost;

public class GuavaCachingDataService extends CachingDataService {

	private final Cache<Integer, FullPost> postsById;
	private final Map<Integer, Integer> commentIdToPostId;

	public GuavaCachingDataService(DataService dataService) {
		super(dataService);
		postsById = CacheBuilder.newBuilder().maximumSize(100).build();
		commentIdToPostId = new HashMap<>();
	}

	@Override
	protected FullPost getPostFromCache(int postId) {
		return postsById.getIfPresent(postId);
	}

	@Override
	protected void putPostIntoCache(FullPost post) {
		postsById.put(post.getPostId(), post);
		for (Comment comment : post.getComments()) {
			commentIdToPostId.put(comment.getCommentId(), post.getPostId());
		}
	}

	@Override
	protected void removePostFromCache(int postId) {
		postsById.invalidate(postId);
	}

	@Override
	protected void removePostFromCacheUsingCommentId(int commentId) {
		Integer postId = commentIdToPostId.remove(commentId);
		removePostFromCache(postId);
	}
}
