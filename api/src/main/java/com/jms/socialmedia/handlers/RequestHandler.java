package com.jms.socialmedia.handlers;

import java.io.IOException;
import com.google.gson.Gson;
import com.jms.socialmedia.dataservice.DataService;
import com.jms.socialmedia.exception.UnauthorizedException;
import com.jms.socialmedia.exception.UnsupportedContentTypeException;
import com.jms.socialmedia.token.Permission;
import com.jms.socialmedia.token.Token;
import com.jms.socialmedia.token.TokenService;

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
	protected final TokenService tokenService;
	private final Gson gson;

	public RequestHandler(DataService dataService, TokenService tokenService, Gson gson) {
		this.dataService = dataService;
		this.tokenService = tokenService;
		this.gson = gson;
	}

	/**
	 * 
	 * @param request
	 * @param userIdFromRequest
	 * @param permission
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	protected void authorizeRequest(Request request, Integer userIdFromRequest, Permission permission)
			throws IOException {

		String auth = request.headers(AUTHORIZATION);
		if (StringUtils.isBlank(auth) || !auth.startsWith(BEARER)) {
			throw new UnauthorizedException("Not authorized to " + permission.getAction());
		} else {
			String tokenString = auth.substring(BEARER.length());
			Token token = tokenService.createTokenFromString(tokenString);
			if (!token.hasPermission(Permission.ADMIN)
					&& (!userIdFromRequest.equals(token.getUserId()) || !token.hasPermission(permission))) {
				throw new UnauthorizedException("User not authorized to " + permission.getAction());
			}
		}
	}

	protected <T> T extractBodyContent(Request request, Class<T> aClass) throws IOException {

		if (StringUtils.isBlank(request.contentType())
				|| request.contentType().toLowerCase().startsWith("application/json")) {
			return gson.fromJson(request.body(), aClass);
		} else if (request.contentType().toLowerCase().startsWith("application/xml") 
				|| request.contentType().toLowerCase().startsWith("text/xml")) {
			return null;
		} else if (request.contentType().toLowerCase().startsWith("application/x-protobuf")) {
			return null;
		} else {
			throw new UnsupportedContentTypeException("Unsupported Content Type");
		}
	}

}
