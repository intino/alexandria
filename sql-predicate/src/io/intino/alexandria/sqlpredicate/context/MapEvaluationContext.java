package io.intino.alexandria.sqlpredicate.context;

import java.util.HashMap;
import java.util.Map;

public class MapEvaluationContext implements EvaluationContext {

	private final Map<String, Object> properties;

	public MapEvaluationContext(Map<String, Object> properties) {
		this.properties = properties;
	}

	public MapEvaluationContext() {
		properties = new HashMap<>();
	}

	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}
}
