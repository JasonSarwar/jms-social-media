package com.mytwitter.routes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.dataservice.DataService;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
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
		Map<String, ResponseTransformer> contentWriters = Map.of("application/json", gson::toJson, 
																"application/xml", xmlWriter::writeValueAsString);
		
		Spark.path("/api", () -> {
			Spark.before("/*", this::informAllListenersOnRequest);
			Spark.after("/*", this::informAllListenersOnResponse);

			contentWriters.forEach((contentType, contentWriter) -> {

				Spark.get("/posts", contentType, requestHandler::handleGetPosts, contentWriter);

				Spark.get("/post/:id", contentType, requestHandler::handleGetPost, contentWriter);

				Spark.post("/post/add", contentType, requestHandler::handleAddPost, contentWriter);

				Spark.put("/post/:id", contentType, requestHandler::handleEditPost, contentWriter);

				Spark.delete("/post/:id", contentType, requestHandler::handleDeletePost, contentWriter);

				Spark.post("/comment/add", contentType, requestHandler::handleAddComment, contentWriter);

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
