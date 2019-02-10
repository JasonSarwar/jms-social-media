package com.jms.socialmedia.routes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
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
		
		RequestHandler requestHandler = new RequestHandler(dataService, passwordService);
		ExceptionHandler exceptionHandler = new ExceptionHandler();
		
		Gson gson = new GsonBuilder().create();
		ObjectWriter xmlWriter = new XmlMapper().registerModule(new AfterburnerModule()).writer();
		Map<String, ResponseTransformer> contentWriters = Map.of("application/json", gson::toJson, 
																"application/xml", xmlWriter::writeValueAsString);
		
		Spark.path("/api", () -> {
			Spark.before("/*", this::informAllListenersOnRequest);
			Spark.after("/*", this::informAllListenersOnResponse);

			contentWriters.forEach((contentType, contentWriter) -> {

				Spark.get("/posts", contentType, requestHandler::handleGetPosts, contentWriter);

				Spark.get("/post/:id", contentType, requestHandler::handleGetPost, contentWriter);
				Spark.get("/post/:id/full", contentType, requestHandler::handleGetPostWithComments, contentWriter);
				
				Spark.post("/post/add", contentType, requestHandler::handleAddPost, contentWriter);

				Spark.put("/post/:id", contentType, requestHandler::handleEditPost, contentWriter);

				Spark.delete("/post/:id", contentType, requestHandler::handleDeletePost, contentWriter);

				Spark.get("/post/:id/likes", contentType, requestHandler::handleGetPostLikes, contentWriter);

				Spark.post("/post/:postid/like/:userid", contentType, requestHandler::handleLikePost, contentWriter);

				Spark.delete("/post/:postid/unlike/:userid", contentType, requestHandler::handleUnlikePost, contentWriter);

				Spark.get("/post/:id/comments", contentType, requestHandler::handleGetComments, contentWriter);

				Spark.get("/comment/:id", contentType, requestHandler::handleGetComment, contentWriter);
				
				Spark.post("/comment/add", contentType, requestHandler::handleAddComment, contentWriter);
				Spark.post("/post/:id/comment/add", contentType, requestHandler::handleAddComment, contentWriter);
				
				Spark.put("/comment/:id", contentType, requestHandler::handleEditComment, contentWriter);

				Spark.delete("/comment/:id", contentType, requestHandler::handleDeleteComment, contentWriter);

				Spark.put("/user/password", contentType, requestHandler::handleEditUserPassword, contentWriter);
				
				Spark.post("/retrieveSession", contentType, requestHandler::handleSessionRetrieval, contentWriter);

				Spark.post("/login", contentType, requestHandler::handleLogin, contentWriter);

			});
			
			
			Spark.post("/logout", requestHandler::handleLogout);
		
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
}
