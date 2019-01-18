package com.mytwitter.model;

import java.util.Collection;

public class FullPost extends Entry {

	private Collection<Comment> comments;
	
	public final Collection<Comment> getComments() {
		return comments;
	}
	public final void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}
}
