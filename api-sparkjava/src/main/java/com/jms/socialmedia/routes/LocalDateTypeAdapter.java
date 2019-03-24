package com.jms.socialmedia.routes;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jms.socialmedia.exception.BadRequestException;

public class LocalDateTypeAdapter implements JsonDeserializer<LocalDate>{

	DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE; // 1970-01-01 yyyy-mm-dd
	String regex1 = "^\\d{4}-\\d{2}-\\d{2}$"; // January 01, 1970

	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy"); // January 01, 1970
	String regex2 = "^\\w{4,} \\d{2}, \\d{4}$"; // January 01, 1970

	DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("MMM dd, yyyy"); // Jan 01, 1970
	String regex3 = "^\\w{3} \\d{2}, \\d{4}$"; // Jan 01, 1970

	DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("MM-dd-yyyy"); // 01-01-1970
	String regex4 = "^\\d{2}-\\d{2}-\\d{4}$"; // 01-01-1970

	DateTimeFormatter formatter5 = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // 01/01/1970
	String regex5 = "^\\d{2}/\\d{2}/\\d{4}$"; // 01/01/1970

	DateTimeFormatter formatter6 = DateTimeFormatter.ofPattern("MM/dd/yy"); // 01/01/70
	String regex6 = "^\\d{2}/\\d{2}/\\d{2}$"; // 01/01/70

	private Map<String, DateTimeFormatter> formatters = Map.of(
			regex1, formatter1, regex2, formatter2, regex3, formatter3, 
			regex4, formatter4, regex5, formatter5, regex6, formatter6);
	
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
		        DateTimeFormatter formatter = formatter1;
		        
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
