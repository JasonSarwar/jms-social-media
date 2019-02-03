package com.mytwitter.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytwitter.configuration.Configurations;
import com.mytwitter.configuration.ConfigurationsFromFile;
import com.mytwitter.configuration.CoreSettings;
import com.mytwitter.routes.LogRouteAdapter;
import com.mytwitter.routes.RouteMappings;

import com.mytwitter.dataservice.DataService;
import com.mytwitter.dataservice.GuavaCachingDataService;
import com.mytwitter.dataservice.MockDataService;
import com.mytwitter.dataservice.MybatisDataService;
import com.mytwitter.password.BcryptPasswordService;
import com.mytwitter.password.NonEncryptionPasswordService;
import com.mytwitter.password.PasswordService;

import spark.Spark;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws IOException {

		Configurations configurations = new ConfigurationsFromFile("application.properties");
		Spark.staticFiles.location("/webapp/");
		Spark.port(configurations.get(CoreSettings.PORT));

		DataService dataService;
		PasswordService passwordService;
		if (configurations.get(CoreSettings.USE_MOCK_DATA_SERVICE)) {
			dataService = new MockDataService();
			passwordService = new NonEncryptionPasswordService();
		} else {
			passwordService = new BcryptPasswordService();
			if (configurations.get(CoreSettings.USE_CACHING)) {
				dataService = new GuavaCachingDataService(new MybatisDataService(configurations));
				
			} else {
				dataService = new MybatisDataService(configurations);
			}
		}

		RouteMappings routes = new RouteMappings(dataService, passwordService);
		routes.addRouteListener(new LogRouteAdapter());
		routes.start();
		LOGGER.info("Starting up at localhost:{}/", Spark.port());
	}
}
