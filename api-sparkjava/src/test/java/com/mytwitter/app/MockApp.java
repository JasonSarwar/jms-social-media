package com.mytwitter.app;

import com.mytwitter.routes.LogRouteAdapter;
import com.mytwitter.routes.Routes;

import database.MockDataService;
import dataservice.DataService;
import spark.Spark;

public class MockApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Spark.staticFiles.location("/public");
		Spark.staticFiles.location("/");
		DataService dataService = new MockDataService();
		Routes routes = new Routes(dataService);
		routes.addRouteListener(new LogRouteAdapter());
		routes.start();
	}

}
