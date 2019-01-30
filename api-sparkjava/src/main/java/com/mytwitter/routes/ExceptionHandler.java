package com.mytwitter.routes;

import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytwitter.exception.BadRequestException;
import com.mytwitter.exception.FailedLoginAttemptException;
import com.mytwitter.exception.NotFoundException;
import com.mytwitter.exception.UnauthorizedException;
import com.mytwitter.exception.UnsupportedContentTypeException;
import com.mytwitter.exception.InvalidUserLoginStateException;
import spark.Request;
import spark.Response;

public class ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
	
	public ExceptionHandler() {
		// TODO Auto-generated constructor stub
	}

	public void handleException(Exception exception, Request request, Response response) {

		LOGGER.error("", exception);
		
		response.type("text/plain");
		
		if (exception instanceof BadRequestException) {
			response.body(exception.getMessage());
			response.status(400);
			
		} else if (exception instanceof NotFoundException) {
			response.body(exception.getMessage());
			response.status(404);

		} else if (exception instanceof FailedLoginAttemptException) {
			response.body(exception.getMessage());
			response.status(401);
		
		} else if (exception instanceof UnauthorizedException) {
			response.body(exception.getMessage());
			response.status(401);
			
		} else if (exception instanceof UnsupportedContentTypeException) {
			response.body(exception.getMessage());
			response.status(415);
			
		} else if (exception instanceof InvalidUserLoginStateException) {
			response.body(exception.getMessage());
			response.status(400);

		} else if (exception instanceof NumberFormatException) {
			response.body(exception.getMessage().replace("For input string:", "Invalid ID"));
			response.status(500);

		} else if (exception instanceof SQLIntegrityConstraintViolationException) {
			response.body(exception.getMessage());
			response.status(500);

		} else {
			response.body(exception.getMessage());
			response.status(500);
		}
	}

}
