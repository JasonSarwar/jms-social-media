package com.jms.socialmedia.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Settings {

	private static final String DELIMITER = ",";

	private Settings() {
		throw new IllegalStateException();
	}

	public static Setting<String> stringSetting(String name) {
		return new StringSettingBuilder().setName(name).build();
	}

	public static Setting<String> requiredStringSetting(String name) {
		return new StringSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<String> stringSettingWithDefault(String name, String defaultValue) {
		return new StringSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<Boolean> booleanSetting(String name) {
		return new BooleanSettingBuilder().setName(name).build();
	}

	public static Setting<Boolean> requiredBooleanSetting(String name) {
		return new BooleanSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<Boolean> booleanSettingWithDefault(String name, Boolean defaultValue) {
		return new BooleanSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<Integer> integerSetting(String name) {
		return new IntegerSettingBuilder().setName(name).build();
	}

	public static Setting<Integer> requiredIntegerSetting(String name) {
		return new IntegerSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<Integer> integerSettingWithDefault(String name, Integer defaultValue) {
		return new IntegerSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<Double> doubleSetting(String name) {
		return new DoubleSettingBuilder().setName(name).build();
	}

	public static Setting<Double> requiredDoubleSetting(String name) {
		return new DoubleSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<Double> doubleSettingWithDefault(String name, Double defaultValue) {
		return new DoubleSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<List<String>> stringListSetting(String name) {
		return new StringListSettingBuilder().setName(name).build();
	}

	public static Setting<List<String>> requiredStringListSetting(String name) {
		return new StringListSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<List<String>> stringListSettingWithDefault(String name, List<String> defaultValue) {
		return new StringListSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<List<Integer>> integerListSetting(String name) {
		return new IntegerListSettingBuilder().setName(name).build();
	}

	public static Setting<List<Integer>> requiredIntegerListSetting(String name) {
		return new IntegerListSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<List<Integer>> integerListSettingWithDefault(String name, List<Integer> defaultValue) {
		return new IntegerListSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<Set<String>> stringSetSetting(String name) {
		return new StringSetSettingBuilder().setName(name).build();
	}

	public static Setting<Set<String>> requiredStringSetSetting(String name) {
		return new StringSetSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<Set<String>> stringSetSettingWithDefault(String name, Set<String> defaultValue) {
		return new StringSetSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	public static Setting<Set<Integer>> integerSetSetting(String name) {
		return new IntegerSetSettingBuilder().setName(name).build();
	}

	public static Setting<Set<Integer>> requiredIntegerSetSetting(String name) {
		return new IntegerSetSettingBuilder().setName(name).isRequired().build();
	}

	public static Setting<Set<Integer>> integerSetSettingWithDefault(String name, Set<Integer> defaultValue) {
		return new IntegerSetSettingBuilder().setName(name).setDefaultValue(defaultValue).build();
	}

	private abstract static class AbstractSetting<T> implements Setting<T> {
		protected final String name;
		protected final boolean required;
		protected final T defaultValue;

		protected AbstractSetting(AbstractSettingBuilder<T> builder) {
			this.name = checkNotNull(builder.name);
			this.required = builder.required;
			this.defaultValue = builder.defaultValue;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public boolean isRequired() {
			return required;
		}

		@Override
		public T defaultValue() {
			return defaultValue;
		}
	}

	private abstract static class AbstractSettingBuilder<T> {
		String name;
		boolean required = false;
		T defaultValue;

		AbstractSettingBuilder<T> setName(String name) {
			this.name = name;
			return this;
		}

		AbstractSettingBuilder<T> isRequired() {
			required = true;
			return this;
		}

		AbstractSettingBuilder<T> setDefaultValue(T defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}

		abstract Setting<T> build();
	}

	private static class StringSetting extends AbstractSetting<String> {

		StringSetting(StringSettingBuilder builder) {
			super(builder);
		}

		@Override
		public String convertRawValue(String rawValue) {
			return rawValue != null ? rawValue.trim() : null;
		}
	}

	private static class StringSettingBuilder extends AbstractSettingBuilder<String> {
		@Override
		StringSetting build() {
			return new StringSetting(this);
		}
	}

	private static class BooleanSetting extends AbstractSetting<Boolean> {

		BooleanSetting(BooleanSettingBuilder builder) {
			super(builder);
		}

		@Override
		public Boolean convertRawValue(String rawValue) {
			return rawValue != null ? Boolean.parseBoolean(rawValue.trim()) : null;
		}
	}

	private static class BooleanSettingBuilder extends AbstractSettingBuilder<Boolean> {
		@Override
		BooleanSetting build() {
			return new BooleanSetting(this);
		}
	}

	private static class IntegerSetting extends AbstractSetting<Integer> {

		IntegerSetting(IntegerSettingBuilder builder) {
			super(builder);
		}

		@Override
		public Integer convertRawValue(String rawValue) {
			return rawValue != null ? Integer.parseInt(rawValue.trim()) : null;
		}
	}

	private static class IntegerSettingBuilder extends AbstractSettingBuilder<Integer> {
		@Override
		IntegerSetting build() {
			return new IntegerSetting(this);
		}
	}

	private static class DoubleSetting extends AbstractSetting<Double> {

		DoubleSetting(DoubleSettingBuilder builder) {
			super(builder);
		}

		@Override
		public Double convertRawValue(String rawValue) {
			if (rawValue == null) {
				return null;
			} else {
				return Double.parseDouble(rawValue.trim());
			}
		}
	}

	private static class DoubleSettingBuilder extends AbstractSettingBuilder<Double> {
		@Override
		DoubleSetting build() {
			return new DoubleSetting(this);
		}
	}

	private abstract static class AbstractListSetting<T> extends AbstractSetting<List<T>> {

		AbstractListSetting(AbstractSettingBuilder<List<T>> builder) {
			super(builder);
		}

		@Override
		public List<T> convertRawValue(String rawValue) {
			return rawValue == null ? null
					: Arrays.stream(rawValue.split(DELIMITER)).map(this::convertListItem).collect(Collectors.toList());
		}

		protected abstract T convertListItem(String item);
	}

	private static class StringListSetting extends AbstractListSetting<String> {

		StringListSetting(StringListSettingBuilder builder) {
			super(builder);
		}

		@Override
		protected String convertListItem(String item) {
			return item.trim();
		}

	}

	private static class StringListSettingBuilder extends AbstractSettingBuilder<List<String>> {
		@Override
		StringListSetting build() {
			return new StringListSetting(this);
		}
	}

	private static class IntegerListSetting extends AbstractListSetting<Integer> {

		IntegerListSetting(IntegerListSettingBuilder builder) {
			super(builder);
		}

		@Override
		protected Integer convertListItem(String item) {
			return Integer.parseInt(item.trim());
		}

	}

	private static class IntegerListSettingBuilder extends AbstractSettingBuilder<List<Integer>> {
		@Override
		IntegerListSetting build() {
			return new IntegerListSetting(this);
		}
	}

	private abstract static class AbstractSetSetting<T> extends AbstractSetting<Set<T>> {

		AbstractSetSetting(AbstractSettingBuilder<Set<T>> builder) {
			super(builder);
		}

		@Override
		public Set<T> convertRawValue(String rawValue) {
			return rawValue == null ? null
					: Arrays.stream(rawValue.split(DELIMITER)).map(this::convertSetItem).collect(Collectors.toSet());
		}

		protected abstract T convertSetItem(String item);
	}

	private static class StringSetSetting extends AbstractSetSetting<String> {

		StringSetSetting(StringSetSettingBuilder builder) {
			super(builder);
		}

		@Override
		protected String convertSetItem(String item) {
			return item.trim();
		}

	}

	private static class StringSetSettingBuilder extends AbstractSettingBuilder<Set<String>> {
		@Override
		StringSetSetting build() {
			return new StringSetSetting(this);
		}
	}

	private static class IntegerSetSetting extends AbstractSetSetting<Integer> {

		IntegerSetSetting(IntegerSetSettingBuilder builder) {
			super(builder);
		}

		@Override
		protected Integer convertSetItem(String item) {
			return Integer.parseInt(item.trim());
		}

	}

	private static class IntegerSetSettingBuilder extends AbstractSettingBuilder<Set<Integer>> {
		@Override
		IntegerSetSetting build() {
			return new IntegerSetSetting(this);
		}
	}
}
