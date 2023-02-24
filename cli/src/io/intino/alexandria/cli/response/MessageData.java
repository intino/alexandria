package io.intino.alexandria.cli.response;

import java.util.HashMap;
import java.util.Map;

public class MessageData {
	private Map<String, String> info = new HashMap<>();

	public <T extends MessageData> T add(String variable, String value) {
		info.put(variable, value);
		return (T) this;
	}

	public String getOrDefault(String variable, String defaultValue) {
		return info.getOrDefault(variable, defaultValue);
	}

	public boolean isEmpty() {
		return info.isEmpty() || info.values().stream().allMatch(v -> v == null);
	}
}
