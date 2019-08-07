package io.intino.alexandria;

import com.google.gson.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;

public class Json {


	public static String toString(Object object) {
		return gsonWriter().toJson(object);
	}

	public static <T> T fromString(String text, Class<T> t) {
		return gsonReader().fromJson(text, t);
	}

	private static Gson gsonReader() {
		return new GsonBuilder().
				registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type1, context) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).
				registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type1, context) -> new Date(json.getAsJsonPrimitive().getAsLong())).
				registerTypeAdapter(InputStream.class, (JsonDeserializer<InputStream>) (json, type1, context) -> new ByteArrayInputStream(Base64.decode(json.getAsString()))).
				create();
	}

	private static Gson gsonWriter() {
		return new GsonBuilder().
				registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toEpochMilli())).
				registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, context) -> new JsonPrimitive(date.getTime())).
				registerTypeAdapter(InputStream.class, (JsonSerializer<InputStream>) (inputStream, type1, jsonDeserializationContext) -> new JsonPrimitive(Base64.encode(toByteArray(inputStream)))).
				create();
	}

	private static byte[] toByteArray(InputStream inputStream) {
		try {
			return inputStream.readAllBytes();
		} catch (IOException e) {
			return new byte[0];
		}
	}
}
