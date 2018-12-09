package com.mytwitter.utils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.mytwitter.app.AppProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

public class JWTUtils {

	private static final String SUBJECT = "User Authenication";
	private static final String ISSUER = "my-twitter";
	private static final String JWT_ID = "JWT_ID";
	private static final String USERID = "userId";
	
	public JWTUtils() {
		// TODO Auto-generated constructor stub
	}
	
	private static String getKey() throws IOException {
		Path path = Paths.get(AppProperties.JWT_KEY_PATH);
		return Files.readAllLines(path).get(0);
	}
	
	/**
	 * Taken from <a href='https://stormpath.com/blog/jwt-java-create-verify'>
	 * 				https://stormpath.com/blog/jwt-java-create-verify</a>
	 * @param id
	 * @param issuer
	 * @param subject
	 * @param ttlMillis
	 * @return JWT
	 * @throws IOException
	 */
	public static String createJWT(Integer userId) throws IOException {
		 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    Date now = Date.from(ZonedDateTime.now().toInstant());
	    Date expiresAt = Date.from(ZonedDateTime.now().plusDays(1).toInstant());
	 
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(getKey());
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(JWT_ID)
	                                .setSubject(SUBJECT)
	                                .setIssuer(ISSUER)
	                                .setIssuedAt(now)
	                                //.setExpiration(expiresAt)
	                                .claim(USERID, userId)
	                                .signWith(signatureAlgorithm, signingKey);
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}


	/**
	 * Taken from <a href='https://stormpath.com/blog/jwt-java-create-verify'>
	 * 				https://stormpath.com/blog/jwt-java-create-verify</a>
	 * 
	 * @param jwt
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static Integer validateJWTAndRetrieveUserId(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		 
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(getKey()))
	       .parseClaimsJws(jwt).getBody();
	    
	    return claims.get(USERID, Integer.class);
	}

}
