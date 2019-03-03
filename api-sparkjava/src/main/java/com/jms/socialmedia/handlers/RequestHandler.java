package com.jms.socialmedia.handlers;

import java.io.IOException;
import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.UnauthorizedException;
import com.jms.socialmedia.exception.UnsupportedContentTypeException;
import com.jms.socialmedia.jwt.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import spark.Request;
import spark.utils.StringUtils;

public abstract class RequestHandler {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	
	protected final DataService dataService;
	protected final JWTService jwtService;
	private final Gson gson;
	
	public RequestHandler(DataService dataService, Gson gson) {
		this.dataService = dataService;
		this.gson = gson;
		jwtService = new JWTService();
	}

	protected void authorizeRequest(Request request, Integer userIdFromRequest, String action) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		
		String auth = request.headers(AUTHORIZATION);
		if (StringUtils.isBlank(auth) || auth.length() < BEARER.length()) {
			throw new UnauthorizedException("Not authorized to " + action);
		} else {
			String jwt = auth.substring(BEARER.length());
			Integer userIdFromJWT = jwtService.validateJWTAndRetrieveUserId(jwt);
			if (!userIdFromRequest.equals(userIdFromJWT)) {
				throw new UnauthorizedException("User not authorized to " + action);
			}
		}
	}
	
	protected <T> T extractBodyContent(Request request, Class<T> aClass) throws IOException {

		if (request.contentType().toLowerCase().startsWith("application/json") 
				|| StringUtils.isBlank(request.contentType())) {
			return gson.fromJson(request.body(), aClass);
		} else if (request.contentType().toLowerCase().startsWith("application/xml")) {
			return null;
		} else if (request.contentType().toLowerCase().startsWith("application/x-protobuf")) {
			return null;
		} else {
			throw new UnsupportedContentTypeException("Unsupported Content Type");
		}
	}

}
