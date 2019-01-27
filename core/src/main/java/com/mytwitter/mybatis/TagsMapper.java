package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

public interface TagsMapper {

	Collection<String> getTags();

	Collection<String> getPostTags(int postId);

	int addTags(@Param(value="postId") int postId, @Param(value="tags") Collection<String> tags);
	
	int removePostTags(int postId);
}
