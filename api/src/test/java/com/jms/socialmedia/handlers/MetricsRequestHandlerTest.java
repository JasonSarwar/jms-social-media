package com.jms.socialmedia.handlers;

import java.io.IOException;
import java.util.SortedMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MetricsRequestHandlerTest {

	@Mock
	private DataService dataService;
	@Mock
	private TokenService tokenService;
	@Mock
	private Request request;
	@Mock
	private Response response;

	private MetricRegistry metricRegistry;

	private MetricsRequestHandler metricsRequestHandler;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		metricRegistry = new MetricRegistry();
		metricsRequestHandler = new MetricsRequestHandler(dataService, tokenService, metricRegistry);
	}

	@Test
	public void testHandleGetMetrics() throws IOException {
		assertThat(metricsRequestHandler.handleGetMetrics(request, response), is(metricRegistry));
	}

	@Test
	public void testHandleGetTimers() throws IOException {

		Timer timer = metricRegistry.timer("testTimer");
		timer.time().stop();
		metricRegistry.timer("doNotShowThisTimer");

		SortedMap<String, Timer> timersMap = metricsRequestHandler.handleGetTimers(request, response);
		assertThat(timersMap.size(), is(1));
		assertThat(timersMap.get("testTimer"), is(timer));
		assertThat(timersMap.get("doNotShowThisTimer"), is(nullValue()));

		verify(request, times(1)).queryParams("all");
		verify(request, times(1)).queryParams("query");
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testHandleGetTimersAll() throws IOException {

		Timer timer1 = metricRegistry.timer("testTimer1");
		timer1.time().stop();
		Timer timer2 = metricRegistry.timer("testTimer2");
		Timer timer3 = metricRegistry.timer("testTimer3");

		when(request.queryParams("all")).thenReturn("");

		SortedMap<String, Timer> timersMap = metricsRequestHandler.handleGetTimers(request, response);
		assertThat(timersMap.size(), is(3));
		assertThat(timersMap.get("testTimer1"), is(timer1));
		assertThat(timersMap.get("testTimer1").getCount(), is(1L));
		assertThat(timersMap.get("testTimer2"), is(timer2));
		assertThat(timersMap.get("testTimer2").getCount(), is(0L));
		assertThat(timersMap.get("testTimer3"), is(timer3));
		assertThat(timersMap.get("testTimer3").getCount(), is(0L));

		verify(request, times(1)).queryParams("all");
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testHandleGetTimersQuery() throws IOException {

		Timer timer1 = metricRegistry.timer("testTimer1");
		timer1.time().stop();
		Timer timer2 = metricRegistry.timer("testTimer2");
		metricRegistry.timer("timer3");

		when(request.queryParams("query")).thenReturn("test");

		SortedMap<String, Timer> timersMap = metricsRequestHandler.handleGetTimers(request, response);
		assertThat(timersMap.size(), is(2));
		assertThat(timersMap.get("testTimer1"), is(timer1));
		assertThat(timersMap.get("testTimer1").getCount(), is(1L));
		assertThat(timersMap.get("testTimer2"), is(timer2));
		assertThat(timersMap.get("testTimer2").getCount(), is(0L));
		assertThat(timersMap.get("timer3"), is(nullValue()));

		verify(request, times(1)).queryParams("all");
		verify(request, times(1)).queryParams("query");
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testHandleGetTimer() throws IOException {
		Timer timer = metricRegistry.timer("testTimer");
		when(request.params("timer")).thenReturn("testTimer");

		assertThat(metricsRequestHandler.handleGetTimer(request, response), is(timer));

		verify(request, times(1)).params("timer");
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testHandleGetTimerNotFound() throws IOException {

		when(request.params("timer")).thenReturn("testTimer");

		try {
			metricsRequestHandler.handleGetTimer(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Timer not found"));

			verify(request, times(1)).params("timer");
			verifyNoMoreInteractions(request);
		}
	}

	@Test
	public void testHandleGetTimerCount() throws IOException {
		Timer timer = metricRegistry.timer("testTimer");
		timer.time().stop();
		when(request.params("timer")).thenReturn("testTimer");

		assertThat(metricsRequestHandler.handleGetTimerCount(request, response), is(1L));

		verify(request, times(1)).params("timer");
		verifyNoMoreInteractions(request);
	}

	@Test
	public void testHandleGetTimerCountNotFound() throws IOException {

		when(request.params("timer")).thenReturn("testTimer");

		try {
			metricsRequestHandler.handleGetTimerCount(request, response);
			fail("Did not throw Exception");
		} catch (Exception e) {
			assertThat(e, instanceOf(NotFoundException.class));
			assertThat(e.getMessage(), is("Timer not found"));

			verify(request, times(1)).params("timer");
			verifyNoMoreInteractions(request);
		}
	}
}
