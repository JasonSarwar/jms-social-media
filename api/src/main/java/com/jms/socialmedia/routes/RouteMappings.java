package com.jms.socialmedia.routes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.handlers.CommentRequestHandler;
import com.jms.socialmedia.handlers.FollowRequestHandler;
import com.jms.socialmedia.handlers.LikeRequestHandler;
import com.jms.socialmedia.handlers.MetricsRequestHandler;
import com.jms.socialmedia.handlers.PostRequestHandler;
import com.jms.socialmedia.handlers.UserRequestHandler;
import com.jms.socialmedia.password.PasswordService;
import com.jms.socialmedia.token.TokenService;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import static spark.Spark.path;
import static spark.Spark.before;
import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;
import static spark.Spark.exception;

public class RouteMappings {

	private static final String POST_ID_MAPPING = "/post/:postId";
	private static final String COMMENT_ID_MAPPING = "/comment/:commentId";
	private static final String APPLICATION_JSON = "application/json";
	private static final String APPLICATION_XML = "application/xml";
	private static final String TEXT_XML = "text/xml";

	private final DataService dataService;
	private final PasswordService passwordService;
	private final TokenService tokenService;
	private final MetricRegistry metricRegistry;
	private final Set<Integer> adminUserIds;
	private Set<RouteListener> routeListeners;

	public RouteMappings(DataService dataService, PasswordService passwordService, TokenService tokenService,
			MetricRegistry metricRegistry, Set<Integer> adminUserIds) {
		this.dataService = dataService;
		this.passwordService = passwordService;
		this.tokenService = tokenService;
		this.metricRegistry = metricRegistry;
		this.adminUserIds = adminUserIds;
		routeListeners = new HashSet<>();
	}

	public final void start() {

		Gson gson = createGson();
		UserRequestHandler userRequestHandler = new UserRequestHandler(dataService, passwordService, tokenService,
				adminUserIds, gson);
		PostRequestHandler postRequestHandler = new PostRequestHandler(dataService, tokenService, gson);
		CommentRequestHandler commentRequestHandler = new CommentRequestHandler(dataService, tokenService, gson);
		LikeRequestHandler likeRequestHandler = new LikeRequestHandler(dataService, tokenService, gson);
		FollowRequestHandler followRequestHandler = new FollowRequestHandler(dataService, tokenService, gson);
		ExceptionHandler exceptionHandler = new ExceptionHandler();

		ObjectWriter xmlWriter = new XmlMapper().registerModule(new AfterburnerModule()).writer();
		Map<String, ResponseTransformer> contentWriters = Map.of("*/*", gson::toJson, 
				"*/xml", xmlWriter::writeValueAsString);

		before("/*", this::informAllListenersOnRequest);

		path("/api", () ->

		contentWriters.forEach((contentType, contentWriter) -> {

			/** Post Request Mappings **/

			get("/posts", contentType, postRequestHandler::handleGetPosts, contentWriter);

			get(POST_ID_MAPPING, contentType, postRequestHandler::handleGetPost, contentWriter);

			post("/post/add", contentType, postRequestHandler::handleAddPost, contentWriter);

			put(POST_ID_MAPPING, contentType, postRequestHandler::handleEditPost, contentWriter);

			delete(POST_ID_MAPPING, contentType, postRequestHandler::handleDeletePost, contentWriter);

			get("/user/:userId/posts", contentType, postRequestHandler::handleGetPostsByUserId, contentWriter);

			get("/user/:userId/commentedposts", contentType, postRequestHandler::handleGetCommentedPosts,
					contentWriter);

			get("/user/:userId/feed", contentType, postRequestHandler::handleGetFeedPosts, contentWriter);

			/** Comments Request Mappings **/

			get("/post/:postId/comments", contentType, commentRequestHandler::handleGetComments, contentWriter);

			get(COMMENT_ID_MAPPING, contentType, commentRequestHandler::handleGetComment, contentWriter);

			post("/comment/add", contentType, commentRequestHandler::handleAddComment, contentWriter);
			post("/post/:postId/comment/add", contentType, commentRequestHandler::handleAddComment, contentWriter);

			put(COMMENT_ID_MAPPING, contentType, commentRequestHandler::handleEditComment, contentWriter);

			delete(COMMENT_ID_MAPPING, contentType, commentRequestHandler::handleDeleteComment, contentWriter);

			/** Like Request Mappings **/

			get("/post/:postId/likes", contentType, likeRequestHandler::handleGetPostLikes, contentWriter);

			post("/post/:postId/like/:userId", contentType, likeRequestHandler::handleLikePost, contentWriter);

			delete("/post/:postId/unlike/:userId", contentType, likeRequestHandler::handleUnlikePost, contentWriter);

			get("/user/:userId/likedposts", contentType, likeRequestHandler::handleGetLikedPosts, contentWriter);

			get("/comment/:commentId/likes", contentType, likeRequestHandler::handleGetCommentLikes, contentWriter);

			post("/comment/:commentId/like/:userId", contentType, likeRequestHandler::handleLikeComment, contentWriter);

			delete("/comment/:commentId/unlike/:userId", contentType, likeRequestHandler::handleUnlikeComment,
					contentWriter);

			get("/user/:userId/comments", contentType, commentRequestHandler::handleGetCommentsByUserId, contentWriter);

			/** Follow Request Mappings **/

			get("/user/:userId/following", contentType, followRequestHandler::handleGetFollowingUserIds, contentWriter);

			get("/user/:userId/userstofollow", contentType, followRequestHandler::handleGetUsersToFollow,
					contentWriter);

			post("/user/follow", contentType, followRequestHandler::handleFollowUser, contentWriter);

			post("/user/unfollow", contentType, followRequestHandler::handleUnfollowUser, contentWriter);

			/** User Request Mappings **/

			get("/users", contentType, userRequestHandler::handleGetUsernamesAndIds, contentWriter);

			get("/user/:username/pageinfo", contentType, userRequestHandler::handleGetUserPage, contentWriter);

			get("/users/isUsernameTaken/:username", contentType, userRequestHandler::handleIsUsernameTaken,
					contentWriter);

			get("/users/isEmailTaken/:email", contentType, userRequestHandler::handleIsEmailTaken, contentWriter);

			post("/user/add", contentType, userRequestHandler::handleAddUser, contentWriter);

			put("/user/password", contentType, userRequestHandler::handleEditUserPassword, contentWriter);

			post("/retrieveSession", contentType, userRequestHandler::handleSessionRetrieval, contentWriter);

			post("/login", contentType, userRequestHandler::handleLogin, contentWriter);

			post("/logout", contentType, userRequestHandler::handleLogout);
		}));

		if (metricRegistry != null) {
			startMetricsEndpoints();
		}

		after("/*", this::setContentType);
		after("/*", this::informAllListenersOnResponse);
		exception(Exception.class, exceptionHandler::handleException);
	}

	private void startMetricsEndpoints() {
		MetricsRequestHandler metricsRequestHandler = new MetricsRequestHandler(dataService, tokenService,
				metricRegistry);

		MetricsModule metricsModule = new MetricsModule(TimeUnit.SECONDS, TimeUnit.MILLISECONDS, true);
		ObjectWriter metricsJsonMapper = new ObjectMapper().registerModule(metricsModule).writer();

		ObjectWriter metricsXmlWriter = new XmlMapper().registerModule(metricsModule).writer();

		Map<String, ResponseTransformer> contentWritersForMetrics = Map.of("*/*", metricsJsonMapper::writeValueAsString,
				"*/xml", metricsXmlWriter::writeValueAsString);

		path("/metrics", () -> {

			before("*", metricsRequestHandler::handleAuthorizeRequest);

			contentWritersForMetrics.forEach((contentType, contentWriter) -> {

				get("", contentType, metricsRequestHandler::handleGetMetrics, contentWriter);

				get("/timers", contentType, metricsRequestHandler::handleGetTimers, contentWriter);

				get("/timer/:timer", contentType, metricsRequestHandler::handleGetTimer, contentWriter);

				get("/timer/:timer/count", contentType, metricsRequestHandler::handleGetTimer, contentWriter);

				get("/gauges", contentType, metricsRequestHandler::handleGetGauges, contentWriter);

				get("/gauge/:gauge", contentType, metricsRequestHandler::handleGetGauge, contentWriter);

				get("/jvm", contentType, metricsRequestHandler::handleGetJvmGauges, contentWriter);
			});
		});
	}

	public boolean addRouteListener(RouteListener routeListener) {
		return routeListeners.add(routeListener);
	}

	private void informAllListenersOnRequest(Request request, Response response) {
		routeListeners.forEach(e -> e.onRequest(request));
	}

	private void informAllListenersOnResponse(Request request, Response response) {
		routeListeners.forEach(e -> e.onResponse(response));
	}

	private void setContentType(Request request, Response response) {
		String acceptsHeader = request.headers("Accept");
		if (acceptsHeader.equalsIgnoreCase(APPLICATION_XML) || acceptsHeader.equalsIgnoreCase(TEXT_XML)) {
			response.type(acceptsHeader);
		} else {
			response.type(APPLICATION_JSON);
		}
	}

	private Gson createGson() {
		return new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
	}
}