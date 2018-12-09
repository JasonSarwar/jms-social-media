package com.mytwitter.routes;

import spark.Request;
import spark.Response;

public interface RouteListener {
	void onRequest(Request request);
	
	void onResponse(Response response);
}
