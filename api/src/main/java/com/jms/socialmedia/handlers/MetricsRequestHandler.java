package com.jms.socialmedia.handlers;

import java.util.SortedMap;

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
	public SortedMap<String, Timer> handleGetTimers(Request request, Response response) {
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
		Timer timer = metricRegistry.getTimers().get(request.params("timer"));
		if (timer == null) {
			throw new NotFoundException("Timer not found");
		}
		return timer.getCount();
	}
}
