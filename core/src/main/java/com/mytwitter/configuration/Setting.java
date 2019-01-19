package com.mytwitter.configuration;

public interface Setting<TYPE> {
	
	String name();
	boolean isRequired();
	TYPE defaultValue();
	TYPE convertRawValue(String rawValue);
}
