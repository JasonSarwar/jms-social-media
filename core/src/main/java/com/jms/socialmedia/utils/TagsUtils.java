package com.jms.socialmedia.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jms.socialmedia.model.Post;

public class TagsUtils {

	public TagsUtils() {
		// TODO Auto-generated constructor stub
	}

	public static List<String> extractTagsFromPost(Post post) {
		return extractTags(post.getText());
	}
	
	public static List<String> extractTags(String text) {
		List<String> tags = new ArrayList<>();
		//Pattern pattern = Pattern.compile("/\\B#\\w*[a-zA-Z]+\\w*/");
		//Pattern pattern = Pattern.compile("/\\#([a-zA-Z0-9\\.\\-\\&]+)/");
		Pattern pattern = Pattern.compile("#(\\S+)");
		//Pattern pattern = Pattern.compile("#([a-zA-Z0-9\\-_]+)");
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()) {
			tags.add(matcher.group(1));
		}

		//tags.removeIf(tag -> tag.contains("[!@#$%&*()_+=|<>?{}\\[\\]~-]"));
		tags.removeIf(tag -> tag.contains("#"));
		tags.removeIf(tag -> tag.contains("/"));
		tags.removeIf(tag -> tag.contains("&"));
		tags.removeIf(tag -> tag.contains("?"));
		tags.removeIf(tag -> tag.contains("!"));
		tags.removeIf(tag -> tag.contains("\\"));
		return tags;
	}
	
//	public static List<String> extractTags(String text) {
//		List<String> tags = new ArrayList<>();
//		
//		int length = text.length();
//		boolean extracting = false;
//		int start = 0;
//		int end = 0;
//		
//		for(int i = 0; i < length; i++) {
//			if(!extracting &&)
//		}
//		
//		return tags;
//	}
}
