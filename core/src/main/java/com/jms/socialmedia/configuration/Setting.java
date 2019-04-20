package com.jms.socialmedia.configuration;

public interface Setting<T> {
	
	String name();
	boolean isRequired();
	T defaultValue();
	T convertRawValue(String rawValue);
}
