package com.mytwitter.routes;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.dataservice.DataService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class RouteMappings {
	
	private DataService dataService;
	private Set<RouteListener> routeListeners;
	
	public RouteMappings(DataService dataService) {
		this.dataService = dataService;
		routeListeners = new HashSet<>();
	}
	
	public final void start() {
		
		RequestHandler requestHandler = new RequestHandler(dataService);
		ExceptionHandler exceptionHandler = new ExceptionHandler();
		
		Gson gson = new GsonBuilder().create();
		ObjectWriter xmlWriter = new XmlMapper().registerModule(new AfterburnerModule()).writer();
		
		Spark.path("/api", () -> {
			Spark.before("/*", this::informAllListenersOnRequest);
			Spark.after("/*", this::informAllListenersOnResponse);

			//Spark.get("/post/:id", "text/plain", requestHandler::handleGetPost, e -> e.toString());
			Spark.get("/post/:id", "application/json", requestHandler::handleGetPost, gson::toJson);
			Spark.get("/post/:id", "application/xml", requestHandler::handleGetPost, xmlWriter::writeValueAsString);
			
			Spark.get("/posts", "application/json", requestHandler::handleGetPosts, gson::toJson);
			Spark.get("/posts", "application/xml", requestHandler::handleGetPosts, xmlWriter::writeValueAsString);
			
			Spark.post("/post/add", "application/json", requestHandler::handleAddPost, gson::toJson);
			Spark.post("/post/add", "application/xml", requestHandler::handleAddPost, xmlWriter::writeValueAsString);
			
			Spark.post("/comment/add", "application/json", requestHandler::handleAddComment, gson::toJson);
			Spark.post("/comment/add", "application/xml", requestHandler::handleAddComment, xmlWriter::writeValueAsString);
			
			Spark.post("/retrieveSession", "application/json", requestHandler::handleSessionRetrieval, gson::toJson);
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
