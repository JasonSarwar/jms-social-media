package com.mytwitter.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

public interface TagsMapper {

	int addTags(@Param(value="postId") int postId, @Param(value="tags") Collection<String> tags);
	
	Collection<String> getPostTags(int postId);

	Collection<String> getTags();
}
