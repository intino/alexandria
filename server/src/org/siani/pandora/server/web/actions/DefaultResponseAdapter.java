package org.siani.pandora.server.web.actions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.siani.pandora.Error;
import org.siani.pandora.server.actions.ResponseAdapter;

import java.util.List;
import java.util.Map;

public class DefaultResponseAdapter implements ResponseAdapter<Object> {

	@Override
	public String adapt(Object value) {

		if (value instanceof String) return (String) value;
		if (value instanceof Error) return adaptError((Error) value);
		if (value instanceof List) return adaptList((List) value);
		return adaptObject(value).toString();
	}

	@Override
	public String adaptList(List<Object> values) {
		JsonArray result = new JsonArray();

		for (Object value : values)
			result.add(adaptObject(value));

		return result.toString();
	}

	private JsonElement adaptObject(Object value) {
		return new Gson().toJsonTree(value);
	}

	private String adaptError(Error error) {
		JsonObject object = new JsonObject();

		object.addProperty("code", error.code());
		object.addProperty("label", error.label());
		adaptParameters(object, error.parameters());

		return object.toString();
	}

	private void adaptParameters(JsonObject object, Map<String, String> parameters) {
		for (Map.Entry<String, String> parameter : parameters.entrySet())
			object.addProperty(parameter.getKey(), parameter.getValue());
	}

}
