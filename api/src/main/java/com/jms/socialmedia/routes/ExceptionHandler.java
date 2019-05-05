package com.jms.socialmedia.routes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.exception.BadRequestException;
import com.jms.socialmedia.exception.DatabaseInsertException;
import com.jms.socialmedia.exception.FailedLoginAttemptException;
import com.jms.socialmedia.exception.ForbiddenException;
import com.jms.socialmedia.exception.InvalidUserLoginStateException;
import com.jms.socialmedia.exception.NotFoundException;
import com.jms.socialmedia.exception.NoBearerTokenException;
import com.jms.socialmedia.exception.UnsupportedContentTypeException;

import spark.Request;
import spark.Response;

public class ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	public void handleException(Exception exception, Request request, Response response) {

		LOGGER.error(exception.getMessage());

		response.type("text/plain");

		if (exception instanceof BadRequestException || exception instanceof InvalidUserLoginStateException) {
			response.body(exception.getMessage());
			response.status(400);

		} else if (exception instanceof NumberFormatException) {
			response.body(exception.getMessage().replace("For input string:", "Invalid ID"));
			response.status(400);

		} else if (exception instanceof FailedLoginAttemptException) {
			response.body(exception.getMessage());
			response.status(401);

		} else if (exception instanceof NoBearerTokenException) {
			response.header("WWW-Authenticate", "Bearer");
			response.body(exception.getMessage());
			response.status(401);

		} else if (exception instanceof ForbiddenException) {
			response.body(exception.getMessage());
			response.status(403);

		} else if (exception instanceof NotFoundException) {
			response.body(exception.getMessage());
			response.status(404);

		} else if (exception instanceof UnsupportedContentTypeException) {
			response.body(exception.getMessage());
			response.status(415);

		} else if (exception instanceof DatabaseInsertException) {
			response.body("Database Error: " + exception.getMessage());
			response.status(500);

		} else {
			LOGGER.error(exception.getClass().getCanonicalName(), exception);
			response.body(exception.getClass().getCanonicalName());
			response.status(500);
		}
	}

}
