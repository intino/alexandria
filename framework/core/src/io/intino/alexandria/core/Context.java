package io.intino.alexandria.core;

import java.util.HashMap;
import java.util.Map;

public class Context extends HashMap<String, String> {
	private Map<String, String> userMap = new HashMap<>();

	public Context add(String name, String value) {
		userMap.put(name, value);
		return this;
	}

}
