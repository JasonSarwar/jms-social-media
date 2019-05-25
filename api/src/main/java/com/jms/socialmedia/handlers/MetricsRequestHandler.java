package com.jms.socialmedia.handlers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

public class MetricsRequestHandler extends RequestHandler {

	private final MetricRegistry metricRegistry;

	public MetricsRequestHandler(DataService dataService, TokenService tokenService, MetricRegistry metricRegistry) {

		super(dataService, tokenService, null);
		this.metricRegistry = metricRegistry;
	}

	/**
	 * <h1> GET /metrics </h1>
	 * 
	 * @param request
	 * @param response
	 * @return	All Metrics
	 */
	public MetricRegistry handleGetMetrics(Request request, Response response) {
		return metricRegistry;
	}

	/**
	 * <h1>GET /metrics/timers</h1>
	 * 
	 * Query Parameters:
	 * <ul>
	 *   <li> query - Display timers that contain the query text </li>
	 *   <li> all - Display all Timers, even if count is 0 </li>
	 * </ul>
	 * 
	 * @param request
	 * @param response
	 * @return	Timers according to the query parameters
	 */
	public Map<String, Timer> handleGetTimers(Request request, Response response) {
		String query;
		MetricFilter metricFilter;
		if (request.queryParams("all") != null) {
			metricFilter = MetricFilter.ALL;
		} else if ((query = request.queryParams("query")) != null) {
			metricFilter = MetricFilter.contains(query);
		} else {
			metricFilter = (name, metric) -> ((Timer) metric).getCount() > 0;
		}

		return metricRegistry.getTimers(metricFilter);
	}

	/**
	 * <h1> GET /metrics/timer/:timer </h1>
	 * 
	 * @param request
	 * @param response
	 * @return a Timer if it exists
	 */
	public Timer handleGetTimer(Request request, Response response) {
		Timer timer = metricRegistry.getTimers().get(request.params("timer"));
		if (timer == null) {
			throw new NotFoundException("Timer not found");
		}
		return timer;
	}

	/**
	 * <h1> GET /metrics/timer/:timer/count </h1>
	 * 
	 * @param request
	 * @param response
	 * @return the number of times a Timer recorded a call
	 */
	public long handleGetTimerCount(Request request, Response response) {
		return handleGetTimer(request, response).getCount();
	}

	/**
	 * <h1>GET /metrics/gauges</h1>
	 * 
	 * Query Parameters:
	 * <ul>
	 *   <li> query - Display gauges that contain the query text </li>
	 *   <li> compact - un-nested response <li>
	 * </ul>
	 * 
	 * @param request
	 * @param response
	 * @return Gauge according to the query parameters
	 */
	public Map<String, ?> handleGetGauges(Request request, Response response) {
		String query;
		MetricFilter metricFilter;
		if ((query = request.queryParams("query")) != null) {
			metricFilter = MetricFilter.contains(query);
		} else {
			metricFilter = MetricFilter.ALL;
		}

		return compactResponseIfNecessary(metricRegistry.getGauges(metricFilter), request, e -> e.getValue().getValue());
	}

	/**
	 * <h1> GET /metrics/gauge/:gauge </h1>
	 * 
	 * @param request
	 * @param response
	 * @return a Gauge if it exists
	 */
	public Gauge<?> handleGetGauge(Request request, Response response) {
		Gauge<?> gauge = metricRegistry.getGauges().get(request.params("gauge"));
		if (gauge == null) {
			throw new NotFoundException("Gauge not found");
		}
		return gauge;
	}

	/**
	 * <h1> GET /metrics/jvm </h1>
	 * 
	 * @param request
	 * @param response
	 * @return Gauge according to the query parameters
	 */
	public Map<String, ?> handleGetJvmGauges(Request request, Response response) {
		return compactResponseIfNecessary(metricRegistry.getGauges(MetricFilter.startsWith("jvm.")), 
				request, e -> e.getValue().getValue());
	}

	/**
	 * <h1>GET /metrics/counters</h1>
	 * 
	 * Query Parameters:
	 * <ul>
	 *   <li> query - Display counters that contain the query text </li>
	 * </ul>
	 * 
	 * @param request
	 * @param response
	 * @return Counter according to the query parameters
	 */
	public Map<String, ?> handleGetCounters(Request request, Response response) {
		String query;
		MetricFilter metricFilter;
		if ((query = request.queryParams("query")) != null) {
			metricFilter = MetricFilter.contains(query);
		} else {
			metricFilter = MetricFilter.ALL;
		}

		return compactResponseIfNecessary(metricRegistry.getCounters(metricFilter), request, e -> e.getValue().getCount());
	}

	/**
	 * <h1> GET /metrics/counter/:counter </h1>
	 * 
	 * @param request
	 * @param response
	 * @return a Counter if it exists
	 */
	public Counter handleGetCounter(Request request, Response response) {
		Counter counter = metricRegistry.getCounters().get(request.params("counter"));
		if (counter == null) {
			throw new NotFoundException("Counter not found");
		}
		return counter;
	}

	private <T> Map<String, ?> compactResponseIfNecessary(Map<String, T> metricsMap, Request request, Function<Entry<String, T>, Object> valueMapper) {
		String compact = request.queryParams("compact");

		if (compact != null && !compact.equalsIgnoreCase("false")) {
			return metricsMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, valueMapper,
					throwingMerger(), TreeMap::new));
		} else {
			return metricsMap;
		}
	}

	private static <T> BinaryOperator<T> throwingMerger() {
		return (v1, v2) -> {
			throw new IllegalStateException(String.format("Duplicate key for values %s and %s", v1, v2));
		};
	}
}
