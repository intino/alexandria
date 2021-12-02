package io.intino.alexandria.rest;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

public class RequestAdapter {

	private static final JsonDeserializer<Instant> instantJsonDeserializer;
	private static final JsonDeserializer<Date> dateJsonDeserializer;

	static {
		instantJsonDeserializer = (json, type1, jsonDeserializationContext) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
		dateJsonDeserializer = (json, type1, jsonDeserializationContext) -> new Date(json.getAsJsonPrimitive().getAsLong());
	}

	public static <T> T adapt(String object, Class<T> type) {
		if (object == null) return null;
		T result = adaptPrimitive(object, type);
		return result != null ? result : adaptFromJSON(object, type);
	}

	public static <T> T adaptFromJSON(String object, Class<T> type) {
		return object == null || object.isEmpty() ? null : gsonBuilder().create().fromJson(decode(object), type);
	}

	public static <T> T adapt(String object, Type type) {
		return object == null || object.isEmpty() ? null : gsonBuilder().create().fromJson(decode(object), type);
	}

	private static GsonBuilder gsonBuilder() {
		return new GsonBuilder()
				.registerTypeAdapter(Instant.class, instantJsonDeserializer)
				.registerTypeAdapter(Date.class, dateJsonDeserializer);
	}

	private static String decode(String object) {
		try {
			return URLDecoder.decode(object, StandardCharsets.UTF_8);
		} catch (IllegalArgumentException ex) {
			return object;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T adaptPrimitive(String object, Class<T> type) {
		if (type == String.class) return (T) object;
		if (type == Double.class) return (T) Double.valueOf(object);
		if (type == Integer.class) return (T) Integer.valueOf(object);
		if (type == Boolean.class) return (T) Boolean.valueOf(object);
		if (type == Instant.class) return (T) ofEpochMilli(Long.parseLong(object));
		if (type == LocalDate.class) return (T) LocalDate.ofEpochDay(Long.parseLong(object));
		if (type == LocalDateTime.class) return (T) ofInstant(ofEpochMilli(Long.parseLong(object)), ZoneId.of("UTC"));
		return null;
	}
}