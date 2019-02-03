package com.jms.socialmedia.configuration;

public interface Setting<TYPE> {
	
	String name();
	boolean isRequired();
	TYPE defaultValue();
	TYPE convertRawValue(String rawValue);
}
