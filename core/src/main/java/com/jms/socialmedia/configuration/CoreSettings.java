package com.jms.socialmedia.configuration;

import java.util.Collections;
import java.util.Set;

import static com.jms.socialmedia.configuration.Settings.booleanSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.integerSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.stringSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.integerSetSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.requiredStringSetting;

public class CoreSettings {

	public static final Setting<String> MYBATIS_CONFIG_FILE_PATH = requiredStringSetting("mybatis.config.path");
	public static final Setting<Integer> PORT = integerSettingWithDefault("port", 4567);
	public static final Setting<Boolean> USE_MOCK_DATA_SERVICE = booleanSettingWithDefault("mock_data_service.use", false);
	public static final Setting<Boolean> USE_CACHING = booleanSettingWithDefault("caching.use", true);
	public static final Setting<String> CACHING_IMPLEMENTATION = stringSettingWithDefault("caching.implementation", "guava");
	public static final Setting<Boolean> CREATE_TABLES = booleanSettingWithDefault("db.create_tables", false);
	public static final Setting<Set<Integer>> ADMIN_USER_IDS = integerSetSettingWithDefault("admin.user_ids", Collections.emptySet());

	private CoreSettings() {
		throw new IllegalStateException();
	}
}
