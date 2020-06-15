package io.intino.alexandria.restaccessor.adapters;

import com.google.gson.*;
import io.intino.alexandria.exceptions.AlexandriaError;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class RequestAdapter {
	public static String adapt(Object object) {
		if (object instanceof AlexandriaError) return adaptError((AlexandriaError) object);
		if (object instanceof Collection) return jsonArray((Collection<Object>) object);
		if (object instanceof String) return (String) object;
		return toJson(object).toString();
	}

	private static String jsonArray(Collection<Object> objects) {
		JsonArray result = new JsonArray();
		for (Object value : objects)
			result.add(toJson(value));
		return result.toString();
	}

	private static JsonElement toJson(Object value) {
		final GsonBuilder gsonBuilder = new GsonBuilder().
				registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toEpochMilli())).
				registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, context) -> new JsonPrimitive(date.getTime()));
		return gsonBuilder.create().toJsonTree(value);
	}

	private static String adaptError(AlexandriaError error) {
		JsonObject object = new JsonObject();
		object.addProperty("code", error.code());
		object.addProperty("label", error.message());
		adaptParameters(object, error.parameters());
		return object.toString();
	}

	private static void adaptParameters(JsonObject object, Map<String, String> parameters) {
		for (Map.Entry<String, String> parameter : parameters.entrySet())
			object.addProperty(parameter.getKey(), parameter.getValue());
	}

}
