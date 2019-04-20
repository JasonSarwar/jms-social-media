package com.jms.socialmedia.token;

import java.io.IOException;

public interface TokenService {

	String createTokenString(Token token) throws IOException;
	
	Token createTokenFromString(String string) throws IOException;
}
