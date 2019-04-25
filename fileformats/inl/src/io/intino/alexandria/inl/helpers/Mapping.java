package io.intino.alexandria.inl.helpers;

import java.util.HashMap;
import java.util.Map;

public class Mapping {

	private Map<String, String> map = new HashMap<>();

	public void put(String from, String to) {
		map.put(from.toLowerCase(), to);
	}

	public String get(String text) {
		return map.getOrDefault(text.toLowerCase(), text);
	}
}
