package com.mytwitter.utils;

import static org.junit.Assert.assertThat;

import java.util.Collection;
import org.junit.Test;

import com.jms.socialmedia.utils.TagsUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class TagsTest {

	@Test
	public void testExtractTags() {
		String post = "#Hello Everyone! I am feeling #blessed today! #Yolo #DamnDaniel #Jason2012 #This#Should#Not#Work #This$Should@Not&Work #N$ #& #Neither! #Should? #The/se #LastOne";
		Collection<String> tags = TagsUtils.extractTags(post);
		assertThat(tags.size(), is(equalTo(6)));
		assertThat(tags.contains("hello"), is(true));
		assertThat(tags.contains("blessed"), is(true));
		assertThat(tags.contains("yolo"), is(true));
		assertThat(tags.contains("damndaniel"), is(true));
		assertThat(tags.contains("jason2012"), is(true));
		assertThat(tags.contains("lastone"), is(true));
	}

	@Test
	public void testDashesAndUnderscores() {
		String post = "#This_ShouldWork #This_Should_Work #This-ShouldWork #This-Should-Work";
		Collection<String> tags = TagsUtils.extractTags(post);
		assertThat(tags.size(), is(equalTo(4)));
		assertThat(tags.contains("this_shouldwork"), is(true));
		assertThat(tags.contains("this_should_work"), is(true));
		assertThat(tags.contains("this-shouldwork"), is(true));
		assertThat(tags.contains("this-should-work"), is(true));
	}
	
	@Test
	public void testNumbers() {
		String post = "#123 #T123 #123R #x123 #123w";
		Collection<String> tags = TagsUtils.extractTags(post);
		assertThat(tags.size(), is(equalTo(5)));
		assertThat(tags.contains("123"), is(true));
		assertThat(tags.contains("t123"), is(true));
		assertThat(tags.contains("123r"), is(true));
		assertThat(tags.contains("x123"), is(true));
		assertThat(tags.contains("123w"), is(true));
	}
	
	@Test
	public void testInvalidCharacters() {
		String post = "#! #T@ #$$ #4! #.7 #& #^ #$%^& #* #() #@$% #@";
		Collection<String> tags = TagsUtils.extractTags(post);
		assertThat(tags.size(), is(equalTo(0)));
	}
}
