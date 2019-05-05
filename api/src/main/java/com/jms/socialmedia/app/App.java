package com.jms.socialmedia.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.jms.socialmedia.cache.CachingService;
import com.jms.socialmedia.cache.CachingServiceWithMetrics;
import com.jms.socialmedia.cache.GuavaCachingService;
import com.jms.socialmedia.cache.JavaMapCachingService;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.ConfigurationsFromFile;
import com.jms.socialmedia.configuration.CoreSettings;
import com.jms.socialmedia.dataservice.CachingDataService;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.dataservice.DataServiceWithMetrics;
import com.jms.socialmedia.dataservice.MockDataService;
import com.jms.socialmedia.dataservice.MybatisDataService;
import com.jms.socialmedia.password.BcryptPasswordService;
import com.jms.socialmedia.password.NonEncryptionPasswordService;
import com.jms.socialmedia.password.PasswordService;
import com.jms.socialmedia.routes.LogRouteAdapter;
import com.jms.socialmedia.routes.RouteMappings;
import com.jms.socialmedia.token.JWTService;

import spark.Spark;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws IOException {

		String configurationPath = args.length > 0 ? args[0] : "application-mock.properties";
		Configurations configurations = new ConfigurationsFromFile(configurationPath);
		startServices(configurations);
	}
	
	private static void startServices(Configurations configurations) throws IOException {
		Spark.port(configurations.get(CoreSettings.PORT));
		setupWebapp(configurations);

		MetricRegistry metricRegistry = createMetricRegistry(configurations);
		DataService dataService = createDataService(configurations, metricRegistry);
		PasswordService passwordService = createPasswordService(configurations);

		RouteMappings routes = new RouteMappings(dataService, passwordService, new JWTService(), metricRegistry,
				configurations.get(CoreSettings.ADMIN_USER_IDS));

		if (configurations.get(CoreSettings.LOG_REQUESTS_AND_RESPONSES)) {
			routes.addRouteListener(new LogRouteAdapter());
		}

		routes.start();
		LOGGER.info("Starting up at port:{}", Spark.port());
	}

	private static void setupWebapp(Configurations configurations) {
		if (configurations.get(CoreSettings.WEBAPP)) {
			String webappLocation = configurations.get(CoreSettings.WEBAPP_LOCATION);
			if (webappLocation != null) {
				Spark.staticFiles.externalLocation(webappLocation);
			} else {
				Spark.staticFiles.location("webapp");
			}
		}
	}

	private static DataService createDataService(Configurations configurations, MetricRegistry metricRegistry)
			throws IOException {

		DataService dataService;
		if (configurations.get(CoreSettings.MOCK_DATA_SERVICE)) {
			dataService = new MockDataService();
		} else if (configurations.get(CoreSettings.CACHING)) {

			CachingService cachingService;
			String cachingImplementation = configurations.get(CoreSettings.CACHING_IMPLEMENTATION);
			if (cachingImplementation.equalsIgnoreCase("javamap")) {
				cachingService = new JavaMapCachingService();
			} else {
				cachingService = new GuavaCachingService(configurations.get(CoreSettings.CACHING_MAX_NUMBER_OF_POSTS));
			}

			if (metricRegistry != null) {
				cachingService = new CachingServiceWithMetrics(cachingService, metricRegistry);
			}

			DataService innerDataService = new MybatisDataService(configurations);
			if (metricRegistry != null) {
				innerDataService = new DataServiceWithMetrics(innerDataService, metricRegistry);
			}

			dataService = new CachingDataService(innerDataService, cachingService);

		} else {
			dataService = new MybatisDataService(configurations);
		}

		if (metricRegistry != null) {
			dataService = new DataServiceWithMetrics(dataService, metricRegistry);
		}
		
		return dataService;
	}

	private static PasswordService createPasswordService(Configurations configurations) {
		if (configurations.get(CoreSettings.MOCK_DATA_SERVICE)) {
			return new NonEncryptionPasswordService();
		} else {
			return new BcryptPasswordService();
		}
	}

	private static MetricRegistry createMetricRegistry(Configurations configurations) {
		return configurations.get(CoreSettings.METRICS) ? new MetricRegistry() : null;
	}
}
