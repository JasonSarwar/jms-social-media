package com.jms.socialmedia.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jms.socialmedia.exception.ConfigurationException;

public class ConfigurationsFromFile implements Configurations {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsFromFile.class);
	private final Properties properties;
	
	public ConfigurationsFromFile(String filePath) throws IOException {
		properties = new Properties();
		try (InputStream inputStream = createInputStreamForFile(filePath)) {
			properties.load(inputStream);
			LOGGER.info("Loaded Configurations from {}", filePath);
		} catch (Exception e) {
			LOGGER.error("Could not load Configurations from " + filePath, e);
			System.exit(1);
		}
	}

	private InputStream createInputStreamForFile(String filePath) throws IOException {
		if (filePath.startsWith(".") || filePath.contains("/") || filePath.contains("\\")) {
			return Files.newInputStream(Paths.get(filePath));
		} else {
			return Resources.getResourceAsStream(filePath);
		}
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
