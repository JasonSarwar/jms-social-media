package com.mytwitter.routes;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataservice.DataService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Routes {
	
	private DataService dataService;
	private Set<RouteListener> routeListeners;
	
	public Routes(DataService dataService) {
		this.dataService = dataService;
		routeListeners = new HashSet<>();
	}
	
	public final void start() {
		
		RequestHandler requestHandler = new RequestHandler(dataService);
		ExceptionHandler exceptionHandler = new ExceptionHandler();
		
		Gson gson = new GsonBuilder().create();
		XmlMapper xmlMapper = new XmlMapper();
		
		Spark.path("/api", () -> {
			Spark.before("/*", this::informAllListenersOnRequest);
			Spark.after("/*", this::informAllListenersOnResponse);
			
//			Spark.get("/user/:username", userController::getUser);
//			
//			Spark.get("/users", userController::getUsers);
//			
//			Spark.get("/users/count", userController::getUsersCount);
//			
//			Spark.post("/adduser", userController::createUser);
			
			Spark.post("/edituser", (request, response) -> "Hi");

			Spark.get("/post/:id", "application/json", requestHandler::handleGetPost, gson::toJson);
			Spark.get("/post/:id", "application/xml", requestHandler::handleGetPost, xmlMapper::writeValueAsString);
			
			Spark.get("/posts", "application/json", requestHandler::handleGetPosts, gson::toJson);
			Spark.get("/posts", "application/xml", requestHandler::handleGetPosts, xmlMapper::writeValueAsString);
			
			Spark.post("/post/add", "application/json", requestHandler::handleAddPost, gson::toJson);
			Spark.post("/post/add", "application/xml", requestHandler::handleAddPost, xmlMapper::writeValueAsString);
			
			Spark.get("/user/:username/posts", (request, response) -> "Hi");
			
			Spark.get("/user/:username/post/:postno", (request, response) -> "Hi");
			
			Spark.post("/login", "application/json", requestHandler::handleLogin, gson::toJson);
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
