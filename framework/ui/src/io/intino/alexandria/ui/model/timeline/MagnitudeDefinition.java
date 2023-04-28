package io.intino.alexandria.ui.model.timeline;

import java.util.HashMap;
import java.util.Map;

public class MagnitudeDefinition {
	private String name;
	private String unit;
	private Formatter formatter = defaultFormatter();
	private final Map<String, String> labelMap = new HashMap<>();

	public String name() {
		return name;
	}

	public MagnitudeDefinition name(String name) {
		this.name = name;
		return this;
	}

	public String unit() {
		return unit;
	}

	public MagnitudeDefinition unit(String unit) {
		this.unit = unit;
		return this;
	}

	public Formatter formatter() {
		return formatter;
	}

	public MagnitudeDefinition formatter(Formatter formatter) {
		this.formatter = formatter;
		return this;
	}

	public String label(String language) {
		return labelMap.getOrDefault(language, name);
	}

	public MagnitudeDefinition add(String language, String label) {
		labelMap.put(language, label);
		return this;
	}

	private Formatter defaultFormatter() {
		return value -> String.format("%,.0f", value);
	}

}
