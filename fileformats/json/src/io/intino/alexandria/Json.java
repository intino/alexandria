package io.intino.alexandria;

import com.google.gson.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;

public class Json {

	public static String toString(Object object) {
		return toJson(object);
	}

	public static String toStringPretty(Object object) {
		return toJsonPretty(object);
	}

	public static String toJson(Object object) {
		return gsonWriter().toJson(object);
	}

	public static String toJsonPretty(Object object) {
		return gsonWriterPretty().toJson(object);
	}

	public static <T> T fromString(String text, Class<T> type) {
		return gsonReader().fromJson(text, type);
	}

	public static <T> T fromJson(String text, Class<T> type) {
		return gsonReader().fromJson(text, type);
	}

	public static Gson gsonReader() {
		return gsonBuilder().create();
	}

	public static Gson gsonWriter() {
		return gsonBuilder().create();
	}

	public static Gson gsonWriterPretty() {
		return gsonBuilder().setPrettyPrinting().create();
	}

	private static GsonBuilder gsonBuilder() {
		return new GsonBuilder().
				registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toEpochMilli())).
				registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, context) -> new JsonPrimitive(date.getTime())).
				registerTypeAdapter(InputStream.class, (JsonSerializer<InputStream>) (inputStream, type1, jsonDeserializationContext) -> new JsonPrimitive(Base64.encode(toByteArray(inputStream))));
	}

	private static byte[] toByteArray(InputStream is) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = is.read(data, 0, data.length)) != -1) buffer.write(data, 0, nRead);
			buffer.flush();
			return buffer.toByteArray();
		} catch (IOException e) {
			return new byte[0];
		}
	}
}
