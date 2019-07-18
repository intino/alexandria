package io.intino.alexandria.ui.displays;

import java.util.HashMap;

public class PropertyList extends HashMap<String, String> {

	private static final String ClassName = "className";

	public PropertyList addClassName(String className) {
		if (!containsKey(ClassName)) put(ClassName, "");
		put(ClassName, get(ClassName) + " " + className);
		return this;
	}
}
