package com.mytwitter.configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import com.mytwitter.exception.ConfigurationException;

public class ConfigurationsFromFile implements Configurations {

	private final Properties properties;
	
	public ConfigurationsFromFile(String propertiesFilePath) throws IOException {
		properties = new Properties();
		properties.load(Resources.getResourceAsStream("application.properties"));
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
