package com.mytwitter.jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.io.IOException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class JWTService {

	private static final String JWT_KEY = "fh3qHRGn787K8t8WERFIKLh3p57tRscsdcsQW9328JHION";
	private static final String SUBJECT = "User Authenication";
	private static final String ISSUER = "jason-social-media";
	private static final String CLAIM_USERID = "userId";
	private static final String CLAIM_ORIGIN = "origin";

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
	public String createJWT(Integer userId) throws IOException {
		 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    Date now = Date.from(ZonedDateTime.now().toInstant());
	    //Date expiresAt = Date.from(ZonedDateTime.now().plusDays(1).toInstant());
	 
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
	                                .setSubject(SUBJECT)
	                                .setIssuer(ISSUER)
	                                .setIssuedAt(now)
	                                //.setExpiration(expiresAt)
	                                .claim(CLAIM_USERID, userId)
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
	public Integer validateJWTAndRetrieveUserId(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		 
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_KEY))
	       .parseClaimsJws(jwt).getBody();
	    
	    return claims.get(CLAIM_USERID, Integer.class);
	}

}
