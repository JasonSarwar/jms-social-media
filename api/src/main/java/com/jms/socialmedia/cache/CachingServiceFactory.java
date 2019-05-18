package com.jms.socialmedia.cache;

import java.util.Locale;

import com.codahale.metrics.MetricRegistry;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.cache.codec.CachingCodec;
import com.jms.socialmedia.cache.codec.GsonCachingCodec;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.CoreSettings;

public class CachingServiceFactory {

	private CachingServiceFactory() {
		throw new IllegalStateException("Factory Class");
	}

	public static CachingService createCachingService(Configurations configurations, MetricRegistry metricRegistry) {

		CachingService cachingService = null;
		if (configurations.get(CoreSettings.USE_CACHE)) {
			switch (configurations.get(CoreSettings.CACHE_IMPLEMENTATION).toLowerCase(Locale.US)) {
				case "redis":
					CachingCodec<String> cachingCodec = new GsonCachingCodec(new GsonBuilder().create());
					cachingService = new RedisCachingService(cachingCodec,
							configurations.get(CoreSettings.CACHE_REDIS_HOST),
							configurations.get(CoreSettings.CACHE_REDIS_PORT),
							configurations.get(CoreSettings.CACHE_EXPIRE_TIME_SECONDS));
					break;
				case "guava":
					cachingService = new GuavaCachingService(
							configurations.get(CoreSettings.CACHE_GUAVA_MAX_NUMBER_OF_POSTS),
							configurations.get(CoreSettings.CACHE_GUAVA_MAX_NUMBER_OF_USER_SESSIONS),
							configurations.get(CoreSettings.CACHE_EXPIRE_TIME_SECONDS));
					break;
				default:
					cachingService = new JavaMapCachingService();
			}

			if (metricRegistry != null) {
				cachingService = new CachingServiceWithMetrics(cachingService, metricRegistry);
			}
		}

		return cachingService;
	}
}
