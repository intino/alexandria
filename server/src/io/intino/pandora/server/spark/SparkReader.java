package io.intino.pandora.server.spark;

import com.google.gson.Gson;
import io.intino.pandora.Error;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

@SuppressWarnings("unchecked")
class SparkReader {

	static <T> T read(String object, Class<T> type) {
		if (type == Error.class || type == Collection.class) return adaptFromJSON(object, type);
		else if (type == byte[].class) readBytes(object);
		return adapt(object, type);
	}

	static <T> T read(Object object, Class<T> type) {
		if (type == InputStream.class && object instanceof InputStream) return (T) object;
		return null;
	}

	private static byte[] readBytes(String object) {
		return object.getBytes(Charset.forName("UTF-8"));
	}

	private static <T> T adapt(String object, Class<T> type) {
		T result = adaptPrimitive(object, type);
		return result != null ? result : adaptFromJSON(object, type);
	}

	private static <T> T adaptFromJSON(String object, Class<T> type) {
		return new Gson().fromJson(object, type);
	}

	private static <T> T adaptPrimitive(String object, Class<T> type) {
		if (type == String.class) return (T) object;
		if (type == Double.class) return (T) Double.valueOf(object);
		if (type == Integer.class) return (T) Integer.valueOf(object);
		if (type == Boolean.class) return (T) Boolean.valueOf(object);
		if (type == LocalDate.class) return (T) LocalDate.ofEpochDay(Long.valueOf(object));
		if (type == LocalDateTime.class) return (T) ofInstant(ofEpochMilli(Long.valueOf(object)), ZoneId.of("UTC"));
		return null;
	}
}
