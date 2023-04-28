package io.intino.alexandria.ui.model.reel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignalDefinition {
	private String name;
	private String color;
	private Type type = Type.Normal;
	private final Map<String, String> labelMap = new HashMap<>();

	public enum Type { Normal, Empty }

	public String name() {
		return name;
	}

	public SignalDefinition name(String name) {
		this.name = name;
		return this;
	}

	public String color() {
		return color;
	}

	public SignalDefinition color(String color) {
		this.color = color;
		return this;
	}

	public String label(String language) {
		return labelMap.getOrDefault(language, name);
	}

	public SignalDefinition add(String language, String label) {
		labelMap.put(language, label);
		return this;
	}

	public Type type() {
		return type;
	}

	private SignalDefinition type(Type type) {
		this.type = type;
		return this;
	}

	public static SignalDefinition empty(String name) {
		return new SignalDefinition().name(name).type(Type.Empty);
	}
}
