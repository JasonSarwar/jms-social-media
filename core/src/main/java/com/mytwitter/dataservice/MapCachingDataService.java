package com.mytwitter.dataservice;

import java.util.HashMap;
import java.util.Map;

import com.mytwitter.model.Comment;
import com.mytwitter.model.FullPost;

public class MapCachingDataService extends CachingDataService {

	private final Map<Integer, FullPost> postsById;
	private final Map<Integer, Integer> commentIdToPostId;
	
	public MapCachingDataService(DataService dataService) {
		super(dataService);
		postsById = new HashMap<>();
		commentIdToPostId = new HashMap<>();
	}

	@Override
	protected FullPost getPostFromCache(int postId) {
		return postsById.get(postId);
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
		postsById.remove(postId);
	}

	@Override
	protected void removePostFromCacheUsingCommentId(int commentId) {
		Integer postId = commentIdToPostId.remove(commentId);
		removePostFromCache(postId);
	}
}
