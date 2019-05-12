package com.jms.socialmedia.dataservice;

import java.io.IOException;

import com.codahale.metrics.MetricRegistry;
import com.jms.socialmedia.cache.CachingService;
import com.jms.socialmedia.cache.CachingServiceWithMetrics;
import com.jms.socialmedia.cache.GuavaCachingService;
import com.jms.socialmedia.cache.JavaMapCachingService;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.CoreSettings;

public class DataServiceFactory {

	private DataServiceFactory() {
	}

	public static DataService createDataService(Configurations configurations, MetricRegistry metricRegistry)
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
				cachingService = new GuavaCachingService(configurations.get(CoreSettings.CACHING_MAX_NUMBER_OF_POSTS),
						configurations.get(CoreSettings.CACHING_MAX_NUMBER_OF_USER_SESSIONS));
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
}
