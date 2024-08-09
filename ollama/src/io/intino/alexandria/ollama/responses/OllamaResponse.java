package io.intino.alexandria.ollama.responses;

import io.intino.alexandria.Json;

import java.io.Serializable;

public class OllamaResponse implements Serializable {

	public static <T extends OllamaResponse> T fromJson(String json, Class<T> type) {
		return Json.fromJson(json, type);
	}

	public String toJson() {
		return Json.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}
}
