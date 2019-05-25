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
	private AbstractCachingService cachingService;

	private MetricRegistry metricRegistry;

	private CachingServiceWithMetrics cachingServiceWithMetrics;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		metricRegistry = new MetricRegistry();
		cachingServiceWithMetrics = new CachingServiceWithMetrics(cachingService, metricRegistry, "test");
	}

	@Test
	public void testConstructorWithoutMetricsName() {
		cachingServiceWithMetrics = new CachingServiceWithMetrics(new JavaMapCachingService(), metricRegistry);
		cachingServiceWithMetrics.getPostFromCache(1);
		Timer timer = metricRegistry.timer("JavaMapCachingService.getPostFromCache");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
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
	public void testEditPostInCache() {
		Timer timer = metricRegistry.timer("test.editPostInCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.editPostInCache(1, "Edit Text");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.editPostInCache(1, "Edit Text");
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
	public void testLikePostInCache() {
		Timer timer = metricRegistry.timer("test.likePostInCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.likePostInCache(1, 3);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.likePostInCache(1, 3);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testUnlikePostInCache() {
		Timer timer = metricRegistry.timer("test.unlikePostInCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.unlikePostInCache(1, 3);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.unlikePostInCache(1, 3);
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
	public void testEditCommentInCache() {
		Timer timer = metricRegistry.timer("test.editCommentInCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.editCommentInCache(1, "Edit Text");
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.editCommentInCache(1, "Edit Text");
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

	@Test
	public void testLikeCommentInCache() {
		Timer timer = metricRegistry.timer("test.likeCommentInCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.likeCommentInCache(1, 3);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.likeCommentInCache(1, 3);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testUnlikeCommentInCache() {
		Timer timer = metricRegistry.timer("test.unlikeCommentInCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.unlikeCommentInCache(1, 3);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.unlikeCommentInCache(1, 3);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testGetUserSessionFromCache() {
		Timer timer = metricRegistry.timer("test.getUserSessionFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.getUserSessionFromCache(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.getUserSessionFromCache(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testPutUserSessionIntoCache() {
		Timer timer = metricRegistry.timer("test.putUserSessionIntoCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.putUserSessionIntoCache(null, null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.putUserSessionIntoCache(null, null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}

	@Test
	public void testRemoveUserSessionFromCache() {
		Timer timer = metricRegistry.timer("test.removeUserSessionFromCache");
		assertThat(timer.getCount(), is(0L));
		assertThat(timer.getOneMinuteRate() == 0, is(true));
		cachingServiceWithMetrics.removeUserSessionFromCache(null);
		assertThat(timer.getCount(), is(1L));
		assertThat(timer.getMeanRate() > 0, is(true));
		cachingServiceWithMetrics.removeUserSessionFromCache(null);
		assertThat(timer.getCount(), is(2L));
		assertThat(timer.getMeanRate() > 0, is(true));
	}
}
