package com.jms.socialmedia.configuration;

import java.util.Collections;
import java.util.Set;

import static com.jms.socialmedia.configuration.Settings.booleanSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.integerSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.stringSetting;
import static com.jms.socialmedia.configuration.Settings.stringSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.integerSetSettingWithDefault;
import static com.jms.socialmedia.configuration.Settings.requiredStringSetting;

public class CoreSettings {

	public static final Setting<String> MYBATIS_CONFIG_FILE_PATH = requiredStringSetting("mybatis.config.path");
	public static final Setting<Integer> PORT = integerSettingWithDefault("port", 4567);
	public static final Setting<Boolean> MOCK_DATA_SERVICE = booleanSettingWithDefault("mock_data_service", false);

	public static final Setting<Boolean> CACHING = booleanSettingWithDefault("caching", true);
	public static final Setting<String> CACHING_IMPLEMENTATION = stringSettingWithDefault("caching.implementation",
			"guava");
	public static final Setting<Integer> CACHING_MAX_NUMBER_OF_POSTS = integerSettingWithDefault(
			"caching.max_number_of_posts", 50);

	public static final Setting<Boolean> CREATE_TABLES = booleanSettingWithDefault("db.create_tables", false);
	public static final Setting<Set<Integer>> ADMIN_USER_IDS = integerSetSettingWithDefault("admin.user_ids",
			Collections.emptySet());
	public static final Setting<Boolean> METRICS = booleanSettingWithDefault("metrics", true);
	public static final Setting<Boolean> LOG_REQUESTS_AND_RESPONSES = booleanSettingWithDefault(
			"log.requests_and_responses", true);

	public static final Setting<Boolean> WEBAPP = booleanSettingWithDefault("webapp", true);
	public static final Setting<String> WEBAPP_LOCATION = stringSetting("webapp.location");

	private CoreSettings() {
		throw new IllegalStateException();
	}
}
