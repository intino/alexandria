package io.intino.alexandria.ollama.responses;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OllamaListResponse extends OllamaResponse {

	private final List<OllamaModel> models = new ArrayList<>();

	public List<OllamaModel> models() {
		return models;
	}

	public Set<String> modelNames() {
		return models.stream().map(OllamaModel::name).map(name -> name.replace(":latest", "")).collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return Json.toJson(this);
	}

	public static class OllamaModel {

		private String name;
		@SerializedName("modified_at")
		private String modifiedAt;
		private long size;
		private String digest;
		private Map<String, Object> details;

		public String name() {
			return name;
		}

		public String modifiedAt() {
			return modifiedAt;
		}

		public long size() {
			return size;
		}

		public String digest() {
			return digest;
		}

		public Map<String, Object> details() {
			return details;
		}
	}
}
