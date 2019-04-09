package com.jms.socialmedia.token;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JWTService implements TokenService {

	private static final String JWT_KEY = "fh3qHRGn787K8t8WERFIKLh3p57tRscsdcsQW9328JHION";
	private static final String SUBJECT = "User Authenication";
	private static final String ISSUER = "jms-social-media";
	private static final String CLAIM_USERID = "userId";
	private static final String CLAIM_PERMISSIONS = "permissions";

	/**
	 * Taken from <a href='https://stormpath.com/blog/jwt-java-create-verify'>
	 * 				https://stormpath.com/blog/jwt-java-create-verify</a>
	 * @param id
	 * @return JWT
	 * @throws IOException
	 */
	public String createTokenString(Token token) throws IOException {
		 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
	                                .setSubject(SUBJECT)
	                                .setIssuer(ISSUER)
	                                .setIssuedAt(new Date())
	                                .claim(CLAIM_USERID, token.getUserId())
	                                .claim(CLAIM_PERMISSIONS, token.getPermissions())
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
	@SuppressWarnings("unchecked")
	public Token createTokenFromString(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, IOException {
		 
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_KEY))
	       .parseClaimsJws(jwt).getBody();
			Set<Permission> permissions = (Set<Permission>) claims.get(CLAIM_PERMISSIONS, ArrayList.class).stream().map(e -> Permission.valueOf((String) e)).collect(Collectors.toSet());
	    	return Token.newBuilder().setUserId(claims.get(CLAIM_USERID, Integer.class)).setPermission(permissions).build();
	}

}
