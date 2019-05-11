package com.jms.socialmedia.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.ConfigurationsFromFile;
import com.jms.socialmedia.configuration.CoreSettings;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.dataservice.DataServiceFactory;
import com.jms.socialmedia.metrics.MetricRegistryFactory;
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

		MetricRegistry metricRegistry = MetricRegistryFactory.createMetricRegistry(configurations);
		DataService dataService = DataServiceFactory.createDataService(configurations, metricRegistry);
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

	private static PasswordService createPasswordService(Configurations configurations) {
		if (configurations.get(CoreSettings.MOCK_DATA_SERVICE)) {
			return new NonEncryptionPasswordService();
		} else {
			return new BcryptPasswordService();
		}
	}
}
