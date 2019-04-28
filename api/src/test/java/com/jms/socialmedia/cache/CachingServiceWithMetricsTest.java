package com.jms.socialmedia.cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class CachingServiceWithMetricsTest {

	@Mock
	private CachingService cachingService;

	private MetricRegistry metricRegistry;

	private CachingServiceWithMetrics cachingServiceWithMetrics;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		metricRegistry = new MetricRegistry();
		cachingServiceWithMetrics = new CachingServiceWithMetrics(cachingService, metricRegistry, "test");
	}

	@Test
	public void testGetPostFromCache() {
		Timer timer = metricRegistry.timer("test.getPostFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.getPostFromCache(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.getPostFromCache(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testPutPostIntoCache() {
		Timer timer = metricRegistry.timer("test.putPostIntoCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.putPostIntoCache(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.putPostIntoCache(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testRemovePostFromCache() {
		Timer timer = metricRegistry.timer("test.removePostFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.removePostFromCache(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.removePostFromCache(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetCommentsFromCache() {
		Timer timer = metricRegistry.timer("test.getCommentsFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.getCommentsFromCache(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.getCommentsFromCache(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetCommentFromCache() {
		Timer timer = metricRegistry.timer("test.getCommentFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.getCommentFromCache(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.getCommentFromCache(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testPutCommentIntoCache() {
		Timer timer = metricRegistry.timer("test.putCommentIntoCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.putCommentIntoCache(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.putCommentIntoCache(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testPutCommentsFromPostIntoCache() {
		Timer timer = metricRegistry.timer("test.putCommentsFromPostIntoCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.putCommentsFromPostIntoCache(1, null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.putCommentsFromPostIntoCache(1, null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testRemoveCommentFromCache() {
		Timer timer = metricRegistry.timer("test.removeCommentFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.removeCommentFromCache(1);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.removeCommentFromCache(1);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

}
