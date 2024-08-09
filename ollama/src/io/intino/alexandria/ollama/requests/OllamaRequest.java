package io.intino.alexandria.ollama.requests;

import io.intino.alexandria.Json;
import io.intino.alexandria.ollama.OllamaParameters;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class OllamaRequest implements Serializable {

	public static <T extends OllamaRequest> T fromJson(String json, Class<T> type) {
		return Json.fromJson(json, type);
	}

	public String toJson() {
		return Json.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}

	public interface WithKeepAlive<Self extends WithKeepAlive<Self>> {

		String keepAlive();

		Self keepAlive(String keepAlive);

		default Self keepAlive(Duration keepAlive) {
			if(keepAlive == null) return keepAlive("5m");
			return keepAlive(keepAlive.getSeconds() + "s");
		}

		@SuppressWarnings("unchecked")
		default Self self() {
			return (Self) this;
		}
	}

	public static abstract class WithOptions<Self extends WithOptions<Self>> extends OllamaRequest implements OllamaParameters<Self> {

		private Map<String, Object> options = new HashMap<>();

		public Map<String, Object> options() {
			return options;
		}

		public Self options(Map<String, Object> options) {
			this.options = options == null ? new HashMap<>(0) : new HashMap<>(options);
			return self();
		}

		@Override
		public Map<String, Object> parametersMap() {
			return options;
		}
	}
}
