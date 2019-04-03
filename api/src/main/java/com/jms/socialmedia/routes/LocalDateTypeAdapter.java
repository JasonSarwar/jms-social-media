package com.jms.socialmedia.routes;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jms.socialmedia.exception.BadRequestException;

public class LocalDateTypeAdapter implements JsonDeserializer<LocalDate>{

	private Map<String, String> mappings = Map.of(
			"^\\w{4,} \\d{2}, \\d{4}$", "MMMM dd, yyyy",	// January 01, 1970
			"^\\w{4,} \\d{1}, \\d{4}$", "MMMM d, yyyy",		// January 1, 1970
			"^\\w{3} \\d{2}, \\d{4}$", "MMM dd, yyyy",		// Jan 01, 1970
			"^\\w{3} \\d{1}, \\d{4}$", "MMM d, yyyy",		// Jan 1, 1970
			"^\\d{4}-\\d{2}-\\d{2}$", "yyyy-MM-dd",			// 1970-01-01
			"^\\d{2}-\\d{2}-\\d{4}$", "MM-dd-yyyy",			// 01-01-1970
			"^\\d{2}/\\d{2}/\\d{4}$", "MM/dd/yyyy", 			// 01/01/1970
			"^\\d{2}/\\d{2}/\\d{2}$", "MM/dd/yy"			// 01/01/70
			);
	
	private Map<String, DateTimeFormatter> formatters = mappings.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> DateTimeFormatter.ofPattern(e.getValue())));
	
	@Override
	public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) 
			throws JsonParseException {
        
		try {
			if (json.isJsonObject()) {
				JsonObject jsonObject = json.getAsJsonObject();
				return LocalDate.of(jsonObject.get("year").getAsInt(), jsonObject.get("month").getAsInt(), 
						jsonObject.get("day").getAsInt());
			} else {
				
		        String dateString = json.getAsString();
		        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		        
		        for (Map.Entry<String, DateTimeFormatter> entry : formatters.entrySet()) {
		        	if (dateString.matches(entry.getKey())) {
			        	formatter = entry.getValue();
			        	break;
		        	}
		        }

		        return LocalDate.parse(dateString, formatter);
			}
		} catch (Exception e) {
			throw new BadRequestException("Invalid Date Format");
		}
    }

}
