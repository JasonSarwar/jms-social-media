package com.mytwitter.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

public class Settings {

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
	
	public static Setting<String> requiredStringSettingWithDefault(String name, String defaultValue) {
		return new StringSettingBuilder().setName(name).isRequired().setDefaultValue(defaultValue).build();
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
	
	public static Setting<Boolean> requiredBooleanSettingWithDefault(String name, Boolean defaultValue) {
		return new BooleanSettingBuilder().setName(name).isRequired().setDefaultValue(defaultValue).build();
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
	
	public static Setting<Integer> requiredIntegerSettingWithDefault(String name, Integer defaultValue) {
		return new IntegerSettingBuilder().setName(name).isRequired().setDefaultValue(defaultValue).build();
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
	
	public static Setting<Double> requiredDoubleSettingWithDefault(String name, Double defaultValue) {
		return new DoubleSettingBuilder().setName(name).isRequired().setDefaultValue(defaultValue).build();
	}
	
	private static abstract class AbstractSetting<TYPE> implements Setting<TYPE> {
		protected final String name;
		protected final boolean required;

		protected AbstractSetting(String name, boolean required) {
			this.name = name;
			this.required = required;
		}
		
		@Override
		public String name() {
			return name;
		}

		@Override
		public boolean isRequired() {
			return required;
		}
	}
	
	private static class StringSetting extends AbstractSetting<String> {
		private final String defaultValue;
		
		StringSetting(StringSettingBuilder builder) {
			super(checkNotNull(builder.name), builder.required);
			this.defaultValue = builder.defaultValue;
		}

		@Override
		public String defaultValue() {
			return defaultValue;
		}

		@Override
		public String convertRawValue(String rawValue) {
			return rawValue.trim();
		}
	}
	
	private static class StringSettingBuilder {
		String name;
		boolean required = false;
		String defaultValue;
		
		StringSettingBuilder setName(String name) {
			this.name = name;
			return this;
		}
		StringSettingBuilder isRequired() {
			required = true;
			return this;
		}
		StringSettingBuilder setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}
		StringSetting build() {
			return new StringSetting(this);
		}
	}
	
	private static class BooleanSetting extends AbstractSetting<Boolean> {
		private final Boolean defaultValue;
		
		BooleanSetting(BooleanSettingBuilder builder) {
			super(checkNotNull(builder.name), builder.required);
			this.defaultValue = builder.defaultValue;
		}

		@Override
		public Boolean defaultValue() {
			return defaultValue;
		}

		@Override
		public Boolean convertRawValue(String rawValue) {
			if (rawValue == null) {
				return null;
			} else {
				return Boolean.parseBoolean(rawValue.trim());
			}
		}
	}
	
	private static class BooleanSettingBuilder {
		String name;
		boolean required = false;
		Boolean defaultValue;
		
		BooleanSettingBuilder setName(String name) {
			this.name = name;
			return this;
		}
		BooleanSettingBuilder isRequired() {
			required = true;
			return this;
		}
		BooleanSettingBuilder setDefaultValue(Boolean defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}
		BooleanSetting build() {
			return new BooleanSetting(this);
		}
	}
	
	private static class IntegerSetting extends AbstractSetting<Integer> {
		private final Integer defaultValue;
		
		IntegerSetting(IntegerSettingBuilder builder) {
			super(checkNotNull(builder.name), builder.required);
			this.defaultValue = builder.defaultValue;
		}

		@Override
		public Integer defaultValue() {
			return defaultValue;
		}

		@Override
		public Integer convertRawValue(String rawValue) {
			if (rawValue == null) {
				return null;
			} else {
				return Integer.parseInt(rawValue.trim());
			}
		}
	}
	
	private static class IntegerSettingBuilder {
		String name;
		boolean required = false;
		Integer defaultValue;
		
		IntegerSettingBuilder setName(String name) {
			this.name = name;
			return this;
		}
		IntegerSettingBuilder isRequired() {
			required = true;
			return this;
		}
		IntegerSettingBuilder setDefaultValue(Integer defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}
		IntegerSetting build() {
			return new IntegerSetting(this);
		}
	}
	
	private static class DoubleSetting extends AbstractSetting<Double> {
		private final Double defaultValue;
		
		DoubleSetting(DoubleSettingBuilder builder) {
			super(checkNotNull(builder.name), builder.required);
			this.defaultValue = builder.defaultValue;
		}

		@Override
		public Double defaultValue() {
			return defaultValue;
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
	
	private static class DoubleSettingBuilder {
		String name;
		boolean required = false;
		Double defaultValue;
		
		DoubleSettingBuilder setName(String name) {
			this.name = name;
			return this;
		}
		DoubleSettingBuilder isRequired() {
			required = true;
			return this;
		}
		DoubleSettingBuilder setDefaultValue(Double defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}
		DoubleSetting build() {
			return new DoubleSetting(this);
		}
	}
}
