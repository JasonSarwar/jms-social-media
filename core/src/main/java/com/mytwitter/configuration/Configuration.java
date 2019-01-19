package com.mytwitter.configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytwitter.exception.ConfigurationException;

public class Configuration {

	private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
	private static final Properties properties = new Properties();
	
	private Configuration() {
		throw new IllegalStateException();
	}
	
	static {
		try {
			properties.load(Resources.getResourceAsStream("application.properties"));
		} catch (IOException e) {
			LOGGER.error("Could not load Properties File", e);
			System.exit(1);
		}
	}
	
	public static final Properties getProperties() {
		return properties;
	}
	
	public static <TYPE> TYPE get(Setting<TYPE> setting) throws ConfigurationException {

		TYPE property = setting.convertRawValue(properties.getProperty(setting.name()));
		if (property == null) {
			property = setting.defaultValue();
		}
		if (property == null && setting.isRequired()) {
			throw new ConfigurationException("Could not retrive required setting " + setting.name());
		}
		return property;
	}
}
