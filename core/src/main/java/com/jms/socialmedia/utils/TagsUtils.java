package com.jms.socialmedia.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jms.socialmedia.model.Post;

public class TagsUtils {

	private TagsUtils() {
	}

	public static Collection<String> extractTagsFromPost(Post post) {
		return extractTags(post.getText());
	}
	
	public static Collection<String> extractTags(String text) {
		Collection<String> tags = new HashSet<>();
		Pattern pattern = Pattern.compile("(?<=\\s|^)#([\\w_-]*)(?=\\s|$)");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			tags.add(matcher.group(1).toLowerCase());
		}

		return tags;
	}
}
