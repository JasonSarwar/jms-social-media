package com.jms.socialmedia.configuration;

import static com.jms.socialmedia.configuration.Settings.booleanSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.integerSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.requiredStringSetting;

public class CoreSettings {

	public static final Setting<String> MYBATIS_CONFIG_FILE_PATH = requiredStringSetting("mybatis.config.path");
	public static final Setting<Integer> PORT = integerSettingWithDefault("port", 4567);
	public static final Setting<Boolean> USE_CACHING = booleanSettingWithDefault("caching", false);
	public static final Setting<Boolean> USE_MOCK_DATA_SERVICE = booleanSettingWithDefault("mock_data_service.use", false);
	
	private CoreSettings() {
		throw new IllegalStateException();
	}
}