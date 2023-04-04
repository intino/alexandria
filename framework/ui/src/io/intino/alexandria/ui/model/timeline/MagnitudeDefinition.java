package io.intino.alexandria.ui.model.timeline;

import java.util.HashMap;
import java.util.Map;

public class MagnitudeDefinition {
	private String name;
	private String unit;
	private Integer decimalCount = 8;
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

	public Integer decimalCount() {
		return decimalCount;
	}

	public MagnitudeDefinition decimalCount(Integer decimalCount) {
		this.decimalCount = decimalCount;
		return this;
	}

	public String label(String language) {
		return labelMap.getOrDefault(language, name);
	}

	public MagnitudeDefinition add(String language, String label) {
		labelMap.put(language, label);
		return this;
	}

}
