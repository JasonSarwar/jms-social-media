package com.jms.socialmedia.routes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.handlers.CommentRequestHandler;
import com.jms.socialmedia.handlers.FollowRequestHandler;
import com.jms.socialmedia.handlers.LikeRequestHandler;
import com.jms.socialmedia.handlers.PostRequestHandler;
import com.jms.socialmedia.handlers.UserRequestHandler;
import com.jms.socialmedia.password.PasswordService;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Spark;

public class RouteMappings {
	
	private final DataService dataService;
	private final PasswordService passwordService;
	private Set<RouteListener> routeListeners;

	public RouteMappings(DataService dataService, PasswordService passwordService) {
		this.dataService = dataService;
		this.passwordService = passwordService;
		routeListeners = new HashSet<>();
	}
	
	public final void start() {

		Gson gson = createGson();
		UserRequestHandler userRequestHandler = new UserRequestHandler(dataService, passwordService, gson);
		PostRequestHandler postRequestHandler = new PostRequestHandler(dataService, gson);
		CommentRequestHandler commentRequestHandler = new CommentRequestHandler(dataService, gson);
		LikeRequestHandler likeRequestHandler = new LikeRequestHandler(dataService, gson);
		FollowRequestHandler followRequestHandler = new FollowRequestHandler(dataService, gson);
		ExceptionHandler exceptionHandler = new ExceptionHandler();

		ObjectWriter xmlWriter = new XmlMapper().registerModule(new AfterburnerModule()).writer();
		Map<String, ResponseTransformer> contentWriters = Map.of("application/json", gson::toJson, 
																"application/xml", xmlWriter::writeValueAsString);
		
		Spark.path("/api", () -> {
			Spark.before("/*", this::informAllListenersOnRequest);
			Spark.after("/*", this::informAllListenersOnResponse);

			contentWriters.forEach((contentType, contentWriter) -> {

				/** Post Request Mappings **/

				Spark.get("/posts", contentType, postRequestHandler::handleGetPosts, contentWriter);

				Spark.get("/post/:id", contentType, postRequestHandler::handleGetPost, contentWriter);

				Spark.post("/post/add", contentType, postRequestHandler::handleAddPost, contentWriter);

				Spark.put("/post/:id", contentType, postRequestHandler::handleEditPost, contentWriter);

				Spark.delete("/post/:id", contentType, postRequestHandler::handleDeletePost, contentWriter);

				Spark.get("/user/:userid/posts", contentType, postRequestHandler::handleGetPostsByUserId, contentWriter);

				Spark.get("/user/:userid/commentedposts", contentType, postRequestHandler::handleGetCommentedPosts, contentWriter);

				Spark.get("/user/:userid/feed", contentType, postRequestHandler::handleGetFeedPosts, contentWriter);

				/** Comments Request Mappings **/

				Spark.get("/post/:id/comments", contentType, commentRequestHandler::handleGetComments, contentWriter);

				Spark.get("/comment/:id", contentType, commentRequestHandler::handleGetComment, contentWriter);
				
				Spark.post("/comment/add", contentType, commentRequestHandler::handleAddComment, contentWriter);
				Spark.post("/post/:id/comment/add", contentType, commentRequestHandler::handleAddComment, contentWriter);
				
				Spark.put("/comment/:id", contentType, commentRequestHandler::handleEditComment, contentWriter);

				Spark.delete("/comment/:id", contentType, commentRequestHandler::handleDeleteComment, contentWriter);
				
				/** Like Request Mappings **/
				
				Spark.get("/post/:id/likes", contentType, likeRequestHandler::handleGetPostLikes, contentWriter);

				Spark.post("/post/:postid/like/:userid", contentType, likeRequestHandler::handleLikePost, contentWriter);

				Spark.delete("/post/:postid/unlike/:userid", contentType, likeRequestHandler::handleUnlikePost, contentWriter);

				Spark.get("/user/:userid/likedposts", contentType, likeRequestHandler::handleGetLikedPosts, contentWriter);

				Spark.get("/comment/:id/likes", contentType, likeRequestHandler::handleGetCommentLikes, contentWriter);

				Spark.post("/comment/:commentid/like/:userid", contentType, likeRequestHandler::handleLikeComment, contentWriter);

				Spark.delete("/comment/:commentid/unlike/:userid", contentType, likeRequestHandler::handleUnlikeComment, contentWriter);
				
				Spark.get("/user/:userid/comments", contentType, commentRequestHandler::handleGetCommentsByUserId, contentWriter);
				
				/** Follow Request Mappings **/

				Spark.get("/user/:userid/following", contentType, followRequestHandler::handleGetFollowingUserIds, contentWriter);

				Spark.post("/user/follow", contentType, followRequestHandler::handleFollowUser, contentWriter);

				Spark.post("/user/unfollow", contentType, followRequestHandler::handleUnfollowUser, contentWriter);

				/** User Request Mappings **/

				Spark.get("/users", contentType, userRequestHandler::handleGetUsernamesAndIds, contentWriter);

				Spark.get("/user/:username/pageinfo", contentType, userRequestHandler::handleGetUserPage, contentWriter);

				Spark.get("/users/isUsernameTaken/:username", contentType, userRequestHandler::handleIsUsernameTaken, contentWriter);

				Spark.get("/users/isEmailTaken/:email", contentType, userRequestHandler::handleIsEmailTaken, contentWriter);

				Spark.post("/user/add", contentType, userRequestHandler::handleAddUser, contentWriter);

				Spark.put("/user/password", contentType, userRequestHandler::handleEditUserPassword, contentWriter);

				Spark.post("/retrieveSession", contentType, userRequestHandler::handleSessionRetrieval, contentWriter);

				Spark.post("/login", contentType, userRequestHandler::handleLogin, contentWriter);

				Spark.post("/logout", contentType, userRequestHandler::handleLogout);

			});
		});
	
		Spark.exception(Exception.class, exceptionHandler::handleException);
	}

	public boolean addRouteListener(RouteListener routeListener) {
		return routeListeners.add(routeListener);
	}

	private void informAllListenersOnRequest(Request request, Response response) {
		routeListeners.forEach(e -> e.onRequest(request));
	}

	private void informAllListenersOnResponse(Request request, Response response) {
		routeListeners.forEach(e -> e.onResponse(response));
	}
	
	private Gson createGson() {
		return new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
				.create();
	}
}
