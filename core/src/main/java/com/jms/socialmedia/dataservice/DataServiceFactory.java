package com.jms.socialmedia.dataservice;

import java.io.IOException;

import com.codahale.metrics.MetricRegistry;
import com.jms.socialmedia.cache.AbstractCachingService;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.CoreSettings;

public class DataServiceFactory {

	private DataServiceFactory() {
		throw new IllegalStateException("Factory Class");
	}

	public static DataService createDataService(Configurations configurations, AbstractCachingService cachingService,
			MetricRegistry metricRegistry) throws IOException {

		DataService dataService;
		if (configurations.get(CoreSettings.MOCK_DATA_SERVICE)) {
			dataService = new MockDataService();
		} else {
			dataService = new MybatisDataService(configurations);
		}

		if (cachingService != null) {

			if (metricRegistry != null) {
				dataService = new DataServiceWithMetrics(dataService, metricRegistry);
			}

			dataService = new CachingDataService(dataService, cachingService);
		}

		if (metricRegistry != null) {
			dataService = new DataServiceWithMetrics(dataService, metricRegistry);
		}

		return dataService;
	}
}
