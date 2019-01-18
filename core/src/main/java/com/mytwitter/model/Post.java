package com.mytwitter.model;

public class Post extends Entry {

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Post ID: ").append(postId).append('\n')
				.append("User ID: ").append(userId).append('\n')
				.append("Username: ").append(username).append('\n')
				.append("Full Name: ").append(fullName).append('\n')
				.append("Text: ").append(text).append('\n')
				.append("Post Time: ").append(timestamp)
				.toString();
	}
	
}
