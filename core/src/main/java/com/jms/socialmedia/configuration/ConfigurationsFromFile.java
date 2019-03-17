package com.jms.socialmedia.configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.exception.ConfigurationException;

public class ConfigurationsFromFile implements Configurations {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsFromFile.class);
	private final Properties properties;
	
	public ConfigurationsFromFile(String propertiesFilePath) throws IOException {
		properties = new Properties();
		properties.load(Resources.getResourceAsStream(propertiesFilePath));
		LOGGER.info("Loaded Configurations from {}", propertiesFilePath);
	}
	
	public final Properties getProperties() {
		return properties;
	}
	
	public final <T> T get(Setting<T> setting) throws ConfigurationException {

		T property = setting.convertRawValue(properties.getProperty(setting.name()));
		if (property == null) {
			property = setting.defaultValue();
		}
		if (property == null && setting.isRequired()) {
			throw new ConfigurationException("Could not retrive required setting " + setting.name());
		}
		return property;
	}
}
