package io.intino.konos;

import java.io.File;
import java.util.Map;

public abstract class BoxConfiguration {

	protected Map<String, String> args;
	protected File store;

	public BoxConfiguration(String[] args) {
		this.args = argsToMap(args);
	}

	private Map<String, String> argsToMap(String[] args) {
		Map<String, String> map = new java.util.LinkedHashMap<>();
		for (String arg : args) {
			String[] keyValue = arg.split("=");
			map.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : keyValue[0]);
		}
		return map;
	}

	public java.util.Map<String, String> args() {
		return args;
	}

	protected Integer toInt(String value) {
		try {
			if (value == null) return null;
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	protected Boolean toBool(String value) {
		try {
			if (value == null) return null;
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			return null;
		}
	}
}
