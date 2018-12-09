package com.mytwitter.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

	public static final String MYBATIS_CONFIG_FILE_PATH;
	public static final String JWT_KEY_PATH;
	public static final Integer PORT;
	
	private Settings() {
		throw new IllegalStateException();
	}
	
	static {
		MYBATIS_CONFIG_FILE_PATH = AppProperties.getProperty("mybatis.config.path");
		JWT_KEY_PATH = AppProperties.getProperty("jwt.key.path");
		PORT = AppProperties.getProperty("port", 4567);
	}

}
