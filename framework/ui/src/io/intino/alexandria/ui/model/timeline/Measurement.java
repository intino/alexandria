package io.intino.alexandria.ui.model.timeline;

import java.util.HashMap;
import java.util.Map;

public class Measurement {
	private String name;
	private String unit;
	private Integer decimalCount = 2;
	private final Map<String, String> labelMap = new HashMap<>();

	public String name() {
		return name;
	}

	public Measurement name(String name) {
		this.name = name;
		return this;
	}

	public String unit() {
		return unit;
	}

	public Measurement unit(String unit) {
		this.unit = unit;
		return this;
	}

	public Integer decimalCount() {
		return decimalCount;
	}

	public Measurement decimalCount(Integer decimalCount) {
		this.decimalCount = decimalCount;
		return this;
	}

	public String label(String language) {
		return labelMap.getOrDefault(language, name);
	}

	public Measurement add(String language, String label) {
		labelMap.put(language, label);
		return this;
	}

}
