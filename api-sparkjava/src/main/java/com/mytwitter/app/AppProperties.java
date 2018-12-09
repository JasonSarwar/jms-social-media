package com.mytwitter.app;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppProperties.class);
	private static final Properties properties = new Properties();
	
	public static final String MYBATIS_CONFIG_FILE_PATH;
	public static final String JWT_KEY_PATH;
	
	private AppProperties() {
		throw new IllegalStateException();
	}
	
	static {
		try {
			properties.load(Resources.getResourceAsStream("application.properties"));
		} catch (IOException e) {
			LOGGER.error("Could not load Properties File", e);
		}
		MYBATIS_CONFIG_FILE_PATH = properties.getProperty("mybatis.config.path");
		JWT_KEY_PATH = properties.getProperty("jwt.key.path");
	}
	
	public static final Properties getProperties() {
		return properties;
	}
	
	public static final String getProperty(String property) {
		return properties.getProperty(property);
	}
	
	public static final String getProperty(String property, String defaultProperty) {
		return properties.getProperty(property, defaultProperty);
	}
	
	public static final Integer getProperty(String property, Integer defaultProperty) {
		if (properties.containsKey(property)) {
			return Integer.parseInt(properties.getProperty(property));
		} else {
			return defaultProperty;
		}
	}
}
