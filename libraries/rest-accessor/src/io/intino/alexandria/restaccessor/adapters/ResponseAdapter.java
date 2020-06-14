package io.intino.alexandria.restaccessor.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.intino.alexandria.Json;

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

public class ResponseAdapter {
	private static Gson gsonReader = Json.gsonReader();
	private static Gson gsonWriter = Json.gsonWriter();

	public static <T> T adapt(String object, Class<T> type) {
		T result = adaptPrimitive(object, type);
		return result != null ? result : adaptFromJSON(object, type);
	}

	public static <T> T adaptFromJSON(String object, Class<T> type) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type1, jsonDeserializationContext) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).
				registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type1, jsonDeserializationContext) -> new Date(json.getAsJsonPrimitive().getAsLong()));
		return object == null || object.isEmpty() ? null : builder.create().fromJson(decode(object), type);
	}

	public static <T> T adapt(String object, Type type) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type1, jsonDeserializationContext) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).
				registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type1, jsonDeserializationContext) -> new Date(json.getAsJsonPrimitive().getAsLong()));
		return object == null || object.isEmpty() ? null : builder.create().fromJson(decode(object), type);
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
