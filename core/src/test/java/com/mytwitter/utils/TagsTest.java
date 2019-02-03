package com.mytwitter.utils;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.jms.socialmedia.utils.TagsUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class TagsTest {

	@Test
	public void testExtractTags() {
		String post = "#Hello Everyone! I am feeling #blessed today! #Yolo #DamnDaniel #This#Should#Not#Work #Neither! #Should? #The/se #LastOne";
		List<String> tags = TagsUtils.extractTags(post);
		tags.forEach(System.out::println);
		assertThat(tags.size(), is(equalTo(5)));
		assertThat(tags.get(0), is("Hello"));
		assertThat(tags.get(1), is("blessed"));
		assertThat(tags.get(2), is("Yolo"));
		assertThat(tags.get(3), is("DamnDaniel"));
		assertThat(tags.get(4), is("LastOne"));
	}

}
