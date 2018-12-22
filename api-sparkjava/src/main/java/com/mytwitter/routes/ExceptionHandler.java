package com.mytwitter.routes;

import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytwitter.exception.BadRequestException;
import com.mytwitter.exception.FailedLoginAttemptException;
import com.mytwitter.exception.PostNotFoundException;
import com.mytwitter.exception.UnauthorizedPostException;
import com.mytwitter.exception.UnsupportedContentTypeException;
import com.mytwitter.exception.InvalidUserLoginStateException;
import com.mytwitter.exception.UserNotFoundException;
import spark.Request;
import spark.Response;

public class ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public ExceptionHandler() {
		// TODO Auto-generated constructor stub
	}

	public void handleException(Exception exception, Request request, Response response) {

		LOGGER.error("", exception);
		
		response.type("text/plain");
		
		if(exception instanceof BadRequestException) {
			response.body(exception.getMessage());
			response.status(400);
			
		} else if(exception instanceof PostNotFoundException) {
			response.body("Post Not Found");
			response.status(404);
			
		} else if(exception instanceof UserNotFoundException) {
			response.body("User Not Found");
			response.status(404);
			
		} else if (exception instanceof FailedLoginAttemptException) {
			response.body(exception.getMessage());
			response.status(401);
		
		} else if (exception instanceof UnauthorizedPostException) {
			response.body(exception.getMessage());
			response.status(401);
			
		} else if (exception instanceof UnsupportedContentTypeException) {
			response.body(exception.getMessage());
			response.status(415);
			
		} else if (exception instanceof InvalidUserLoginStateException) {
			response.body(exception.getMessage());
			response.status(403);
			
		} else if(exception instanceof SQLIntegrityConstraintViolationException) {
			response.body(exception.getMessage());
			response.status(500);
			
		} else {
			response.body(exception.getMessage());
			response.status(500);
		}
	}

}
