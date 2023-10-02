package io.intino.alexandria;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Date;

public class Json {

	private static final Gson GsonInstance = gsonBuilder().create();
	private static final Gson GsonPrettyInstance = gsonBuilder().setPrettyPrinting().create();

	public static String toJson(Object object) {
		return GsonInstance.toJson(object);
	}

	public static String toJsonPretty(Object object) {
		return GsonPrettyInstance.toJson(object);
	}

	public static void toJson(Object object, Writer writer) throws IOException {
		try (JsonWriter jsonWriter = new JsonWriter(writer)) {
			GsonInstance.toJson(object, object.getClass(), jsonWriter);
		}
	}

	public static void toJsonPretty(Object object, Writer writer) throws IOException {
		try (JsonWriter jsonWriter = new JsonWriter(writer)) {
			GsonPrettyInstance.toJson(object, object.getClass(), jsonWriter);
		}
	}

	public static <T> T fromJson(String json, Class<T> type) {
		return GsonInstance.fromJson(json, type);
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		return GsonInstance.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(String json, TypeToken<T> typeOfT) {
		return GsonInstance.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(JsonElement json, Class<T> type) {
		return GsonInstance.fromJson(json, type);
	}

	public static <T> T fromJson(JsonElement json, Type typeOfT) {
		return GsonInstance.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(JsonElement json, TypeToken<T> typeOfT) {
		return GsonInstance.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(Reader reader, Class<T> type) {
		return GsonInstance.fromJson(reader, type);
	}

	public static <T> T fromJson(Reader json, Type typeOfT) {
		return GsonInstance.fromJson(json, typeOfT);
	}

	public static <T> T fromJson(Reader json, TypeToken<T> typeOfT) {
		return GsonInstance.fromJson(json, typeOfT);
	}

	public static String toString(Object object) {
		return toJson(object);
	}

	public static String toStringPretty(Object object) {
		return toJsonPretty(object);
	}

	public static <T> T fromString(String json, Class<T> type) {
		return fromJson(json, type);
	}

	private static GsonBuilder gsonBuilder() {
		return new GsonBuilder()
				.serializeSpecialFloatingPointValues()
				.registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toEpochMilli()))
				.registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type1, context) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong()))
				.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, context) -> new JsonPrimitive(date.getTime()))
				.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type1, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
				.registerTypeAdapter(InputStream.class, (JsonSerializer<InputStream>) (inputStream, type1, jsonDeserializationContext) -> new JsonPrimitive(Base64.encode(toByteArray(inputStream))))
				.registerTypeAdapter(InputStream.class, (JsonDeserializer<InputStream>) (json, type1, context) -> new ByteArrayInputStream(Base64.decode(json.getAsString())));
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
