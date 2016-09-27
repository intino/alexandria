package org.siani.pandora.server.web.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.siani.pandora.server.actions.ResponseAdapter;

import java.util.List;

@FunctionalInterface
public interface JsonSerializer<T> extends ResponseAdapter<T> {

	@Override
	default String adapt(T element) {
		return toJson(element).toString();
	}

	@Override
	default String adaptList(List<T> list) {
		return toJsonArray(list).toString();
	}

	JsonElement toJson(T element);

	default JsonArray toJsonArray(List<T> elements) {
		if (elements.isEmpty()) {
			return new JsonArray();
		}
		return elements.stream().map(this::toJson).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
	}
}