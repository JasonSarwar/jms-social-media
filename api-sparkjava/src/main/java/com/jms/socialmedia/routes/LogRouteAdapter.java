package com.jms.socialmedia.routes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;

public class LogRouteAdapter implements RouteListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogRouteAdapter.class);
	
	@Override
	public void onRequest(Request request) {
		StringBuilder stringBuilder = new StringBuilder('\n');
		stringBuilder.append("Received: " + request.protocol() + " " + request.requestMethod() + " " + request.uri());
		stringBuilder.append('\n');
		stringBuilder.append("Headers:\n");
		request.headers().stream().forEach(header -> stringBuilder.append(header).append(" - ").append(request.headers(header)).append('\n'));
		stringBuilder.append('\n');
		
		if(!request.params().isEmpty()) {
			stringBuilder.append("Parameters:\n");
			request.params().entrySet()
				.forEach(e -> stringBuilder.append(e.getKey() + ": " + e.getValue() + "\n"));
		}
		if(!request.queryParams().isEmpty()) {
			stringBuilder.append("Query Parameters:\n");
			request.queryParams().forEach(e -> stringBuilder.append(e).append(" - ").append(request.queryParams(e)).append('\n'));
		}

		if(request.body() != null && !request.body().isEmpty()) {
			stringBuilder.append("Body:\n");
			stringBuilder.append(request.body());
			stringBuilder.append('\n');
		}
		LOGGER.info(stringBuilder.toString());
	}

	@Override
	public void onResponse(Response response) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Response Status: " + response.status() + "\n");
		stringBuilder.append(response.body());
		stringBuilder.append("\n----------\n");
		LOGGER.info(stringBuilder.toString());
	}

}
