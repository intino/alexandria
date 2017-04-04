package io.intino.konos;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Box {

	protected Map<String, Object> box = new LinkedHashMap<>();

	public abstract void init();

	public Map<String, Object> box() {
		return box;
	}

	public <T> T get(Class<T> tClass) {
		return (T) box.values().stream().filter(tClass::isInstance).findFirst().orElse(null);
	}


	public <T> T get(String object, Class<T> tClass) {
		return (T) box.get(object);
	}

	public String get(String key) {
		return box.containsKey(key) ? box.get(key).toString() : null;
	}


	public Box put(String name, Object object) {
		box.put(name, object);
		return this;
	}
}
