package com.mytwitter.configuration;

import java.util.Properties;

public interface Configurations {
	Properties getProperties();
	<T> T get(Setting<T> setting);
}
