package com.jms.socialmedia.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.cache.CachingService;
import com.jms.socialmedia.cache.GuavaCachingService;
import com.jms.socialmedia.cache.JavaMapCachingService;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.ConfigurationsFromFile;
import com.jms.socialmedia.configuration.CoreSettings;
import com.jms.socialmedia.dataservice.CachingDataService;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.dataservice.MockDataService;
import com.jms.socialmedia.dataservice.MybatisDataService;
import com.jms.socialmedia.password.BcryptPasswordService;
import com.jms.socialmedia.password.NonEncryptionPasswordService;
import com.jms.socialmedia.password.PasswordService;
import com.jms.socialmedia.routes.LogRouteAdapter;
import com.jms.socialmedia.routes.RouteMappings;

import spark.Spark;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws IOException {

		Configurations configurations = new ConfigurationsFromFile("application.properties");
		Spark.staticFiles.location("webapp");
		Spark.port(configurations.get(CoreSettings.PORT));

		DataService dataService;
		PasswordService passwordService;
		if (configurations.get(CoreSettings.USE_MOCK_DATA_SERVICE)) {
			dataService = new MockDataService();
			passwordService = new NonEncryptionPasswordService();
		} else {
			passwordService = new BcryptPasswordService();
			if (configurations.get(CoreSettings.USE_CACHING)) {

				CachingService cachingService;
				String cachingImplementation = configurations.get(CoreSettings.CACHING_IMPLEMENTATION);
				if (cachingImplementation.toLowerCase().equals("javamap")) {
					cachingService = new JavaMapCachingService();
				} else {
					cachingService = new GuavaCachingService();
				}
				dataService = new CachingDataService(new MybatisDataService(configurations), cachingService);
				
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
