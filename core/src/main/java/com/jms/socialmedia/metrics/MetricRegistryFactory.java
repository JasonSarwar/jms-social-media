package com.jms.socialmedia.metrics;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.CachedThreadStatesGaugeSet;
import com.codahale.metrics.jvm.JvmAttributeGaugeSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.jms.socialmedia.configuration.Configurations;
import com.jms.socialmedia.configuration.CoreSettings;

public class MetricRegistryFactory {

	private MetricRegistryFactory() {
	}

	public static MetricRegistry createMetricRegistry(Configurations configurations) {
		if (configurations.get(CoreSettings.METRICS)) {
			MetricRegistry metricRegistry = new MetricRegistry();
			if (configurations.get(CoreSettings.METRICS_JVM)) {
				metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
				metricRegistry.register("jvm.threads", new CachedThreadStatesGaugeSet(10, TimeUnit.SECONDS));
				metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
				metricRegistry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
				metricRegistry.register("jvm.fd_usage", new FileDescriptorRatioGauge());
				metricRegistry.register("jvm.attributes", new JvmAttributeGaugeSet());
			}
			return metricRegistry;
		}
		return null;
	}
}
