package io.intino.konos.server.spark;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.intino.konos.Error;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

@SuppressWarnings("unchecked")
class SparkReader {

	static <T> T read(String object, Class<T> type) {
		if (type.isAssignableFrom(Error.class) || type.isAssignableFrom(Collection.class))
			return adaptFromJSON(object, type);
		else if (type.isAssignableFrom(byte[].class)) return (T) readBytes(object);
		return adapt(object, type);
	}

	static <T> T read(Object object, Class<T> type) {
		return type.isAssignableFrom(InputStream.class) ? (T) object : null;
	}

	private static byte[] readBytes(String object) {
		return object.getBytes(Charset.forName("UTF-8"));
	}

	private static <T> T adapt(String object, Class<T> type) {
		T result = adaptPrimitive(object, type);
		return result != null ? result : adaptFromJSON(object, type);
	}

	private static <T> T adaptFromJSON(String object, Class<T> type) {
		try {
			final GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type1, jsonDeserializationContext) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).
					registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type1, jsonDeserializationContext) -> new Date(json.getAsJsonPrimitive().getAsLong()));
			return object == null || object.isEmpty() ? null : builder.create().fromJson(URLDecoder.decode(object, "UTF-8"), type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static <T> T adaptPrimitive(String object, Class<T> type) {
		if (type == String.class) return (T) object;
		if (type == Double.class) return (T) Double.valueOf(object);
		if (type == Integer.class) return (T) Integer.valueOf(object);
		if (type == Boolean.class) return (T) Boolean.valueOf(object);
		if (type == Instant.class) return (T) ofEpochMilli(Long.valueOf(object));
		if (type == LocalDate.class) return (T) LocalDate.ofEpochDay(Long.valueOf(object));
		if (type == LocalDateTime.class) return (T) ofInstant(ofEpochMilli(Long.valueOf(object)), ZoneId.of("UTC"));
		return null;
	}
}
