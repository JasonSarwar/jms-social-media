package com.jms.socialmedia.dataservice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.jms.socialmedia.model.Comment;
import com.jms.socialmedia.model.Entry;
import com.jms.socialmedia.model.FullPost;
import com.jms.socialmedia.model.Post;
import com.jms.socialmedia.model.User;
import com.jms.socialmedia.model.UserObject;

import static java.util.stream.Collectors.toList;

public class MockDataService implements DataService {

	private final Map<Integer, User> usersById;
	private final Map<String, Integer> userSessionKeys;
	private final Map<Integer, Post> postsById;
	private final Multimap<Integer, Comment> commentsByPostId;
	private final Multimap<Integer, Integer> followerUserIds;
	
	public MockDataService() {

		usersById = new HashMap<>();
		userSessionKeys = new HashMap<>();
		postsById = new TreeMap<>((a, b) -> b.compareTo(a));
		commentsByPostId = TreeMultimap.create(Ordering.natural(), (a, b) -> a.getCommentId().compareTo(b.getCommentId()));
		followerUserIds = HashMultimap.create();
		setupUsers();
		setupPosts();
		setupComments();
	}
	
	@Override
	public UserObject getUser(String username) {
		return null;//usersById.get(username);
	}

	@Override
	public String createUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserLoginInfoByName(String username) {
		return usersById.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
	}

	@Override
	public User getHashedPasswordByUserId(Integer userId) {
		return usersById.values().stream().filter(user -> user.getUserId().equals(userId)).findFirst().orElse(null);

	}

	@Override
	public Collection<String> getUsernamesByIds(Collection<Integer> userIds) {
		return usersById.values().stream().filter(user -> userIds.contains(user.getUserId())).map(User::getUsername).collect(toList());
	}

	@Override
	public boolean editPassword(Integer userId, String hashedPassword) {
		usersById.get(userId).setHashedPassword(hashedPassword);
		return true;
	}

	@Override
	public boolean addUserSession(int userId, String sessionKey) {
		userSessionKeys.put(sessionKey, userId);
		return true;
	}

	@Override
	public User getUserBySessionKey(String sessionKey) {
		return usersById.get(userSessionKeys.get(sessionKey));
	}

	@Override
	public void removeSessionKey(String sessionKey) {
		userSessionKeys.remove(sessionKey);
	}

	@Override
	public Collection<Post> getPosts(Collection<Integer> userIds, String username, String tag, String onDate, String beforeDate, String afterDate) {
		DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("MM-dd-yyyy");
		return postsById.values().stream().filter(post -> userIds == null || userIds.contains(post.getUserId()))
				.filter(post -> username == null || post.getUsername().equals(username))
				.filter(post -> tag == null || post.getText().contains(tag))
				.filter(post -> onDate == null || post.getTimestamp().toLocalDate().equals(LocalDate.parse(onDate,formatter)))
				.filter(post -> beforeDate == null || post.getTimestamp().toLocalDate().isAfter(LocalDate.parse(beforeDate, formatter)))
				.filter(post -> afterDate == null || post.getTimestamp().toLocalDate().isAfter(LocalDate.parse(afterDate, formatter)))
				.collect(toList());
	}

	@Override
	public Post getPost(int postId) {
		return postsById.get(postId);
	}
	
	@Override
	public FullPost getPostWithComments(int postId) {
		Post post = postsById.get(postId);
		FullPost fullPost = new FullPost(post.getPostId(), post.getUserId(), post.getUsername(), post.getFullName(), post.getText(), post.getTimestamp());
		fullPost.setComments(getComments(postId));
		fullPost.setLikes(post.getLikes());
		return fullPost;
	}

	@Override
	public Integer getUserIdFromPostId(int postId) {
		return getPost(postId).getUserId();
	}

	@Override
	public boolean addPost(Post post) {
		if (post.getPostId() == null) {
			post.setPostId(postsById.size() + 1);
		}
		if (post.getTimestamp() == null) {
			post.setTimestamp(LocalDateTime.now());
		}
		post.setLikes(new ArrayList<>());
		setCreatorOfEntry(post, usersById.get(post.getUserId()));
		return postsById.put(post.getPostId(), post) == null;
	}

	@Override
	public boolean editPost(int postId, String postText) {
		postsById.get(postId).setText(postText);
		return true;
	}

	@Override
	public boolean deletePost(int postId) {
		commentsByPostId.removeAll(postId);
		return postsById.remove(postId) != null;
	}

	@Override
	public Collection<Post> getLikedPostsByUserId(int userId) {
		return postsById.values().stream()
				.filter(post -> post.getLikes().contains(userId))
				.collect(toList());
	}

	@Override
	public Collection<Integer> getPostLikes(int postId) {
		return getPost(postId).getLikes();
	}

	@Override
	public boolean likePost(int postId, int userId) {
		return getPost(postId).addLike(userId);
	}

	@Override
	public boolean unlikePost(int postId, int userId) {
		return getPost(postId).removeLike(userId);
	}

	@Override
	public Collection<Comment> getComments(int postId) {
		return commentsByPostId.get(postId);
	}

	@Override
	public Comment getComment(int commentId) {
		return commentsByPostId.values().stream()
				.filter(comment -> comment.getCommentId().intValue() == commentId).findAny().orElse(null);
	}

	@Override
	public Integer getUserIdFromCommentId(int commentId) {
		return getComment(commentId).getUserId();
	}

	@Override
	public boolean addComment(Comment comment) {
		comment.setCommentId(commentsByPostId.size() + 1);
		if (comment.getTimestamp() == null) {
			comment.setTimestamp(LocalDateTime.now());
		}
		comment.setLikes(new ArrayList<>());
		setCreatorOfEntry(comment, usersById.get(comment.getUserId()));
		return commentsByPostId.put(comment.getPostId(), comment);
	}

	@Override
	public boolean editComment(int commentId, String commentText) {
		Comment comment = getComment(commentId);
		if (comment == null) {
			return false;
		}
		comment.setText(commentText);
		return true;
	}

	@Override
	public boolean deleteComment(int commentId) {
		Comment comment = getComment(commentId);
		return comment == null ? false : commentsByPostId.remove(comment.getPostId(), comment);
	}

	private void setupUsers() {
		User user = new User(1, "user", "Visitor", "password");
		User jason = new User(2, "Jason", "Jason Sarwar", "#$%@%$%");
		usersById.put(1, user);
		usersById.put(2, jason);
	}
	
	private void setupPosts() {
		
		User user = usersById.get(2);
		Post firstPost = new Post(3, "Hello!", LocalDateTime.of(2019, 1, 1, 0, 0, 10));
		Post secondPost = new Post(2, "Welcome to my website!", LocalDateTime.of(2019, 1, 1, 0, 0, 5));
		Post thirdPost = new Post(1, "These posts are from a Mock #Data Service and are not from a database", LocalDateTime.of(2019, 1, 1, 0, 0, 0));
		
		Collection<Post> posts = Arrays.asList(firstPost, secondPost, thirdPost);
		for (Post post: posts) {
			post.setLikes(new ArrayList<>());
			setCreatorOfEntry(post, user);
			addPost(post);
		}
	}

	private void setupComments() {
		
		User user = usersById.get(2);
		Comment firstComment = new Comment(1, 1, "Feel free to leave comments here!", LocalDateTime.of(2019, 1, 1, 0, 1, 10));
		Comment secondComment = new Comment(2, 2, "Feel free to leave comments here!", LocalDateTime.of(2019, 1, 1, 0, 1, 5));
		Comment thirdComment = new Comment(3, 3, "Feel free to leave comments here!", LocalDateTime.of(2019, 1, 1, 0, 1, 1));
		
		Collection<Comment> comments = Arrays.asList(firstComment, secondComment, thirdComment);
		for (Comment comment: comments) {
			comment.setLikes(new ArrayList<>());
			setCreatorOfEntry(comment, user);
			addComment(comment);
		}
	}

	private void setCreatorOfEntry(Entry entry, User user) {
		entry.setUserId(user.getUserId());
		entry.setUsername(user.getUsername());
		entry.setFullName(user.getFullName());
	}

	@Override
	public Collection<Integer> getCommentLikes(int commentId) {
		return getComment(commentId).getLikes();
	}

	@Override
	public boolean likeComment(int commentId, int userId) {
		return getComment(commentId).addLike(userId);
	}

	@Override
	public boolean unlikeComment(int commentId, int userId) {
		return getComment(commentId).removeLike(userId);
	}

	@Override
	public Collection<Integer> getFollowerUserIds(int userId) {
		return followerUserIds.entries().stream().filter(entry -> entry.getValue() == userId).map(Map.Entry::getKey).collect(toList());
	}

	@Override
	public Collection<Integer> getFollowingUserIds(int userId) {
		return followerUserIds.get(userId);
	}

	@Override
	public boolean followUser(int followerUserId, int followingUserId) {
		return followerUserIds.put(followerUserId, followingUserId);
	}

	@Override
	public boolean unfollowUser(int followerUserId, int followingUserId) {
		return followerUserIds.remove(followerUserId, followingUserId);
	}
}
