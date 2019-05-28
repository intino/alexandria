package io.intino.alexandria.core;

import java.io.File;
import java.util.Map;

public abstract class BoxConfiguration {

	protected Map<String, String> args;
	protected File workspace;

	public BoxConfiguration(String[] args) {
		this.args = argsToMap(args);
		if (workspace == null) {
			if (this.args.get("workspace") != null)
				workspace = new java.io.File(this.args.get("workspace"));
			else workspace = new java.io.File("./workspace");
		}
	}

	public String get(String key) {
		return args.get(key);
	}

	public java.io.File workspace() {
		return this.workspace;
	}

	private Map<String, String> argsToMap(String[] args) {
		Map<String, String> map = new java.util.LinkedHashMap<>();
		for (String arg : args) {
			if (!arg.contains("=")) continue;
			String key = arg.substring(0, arg.indexOf("="));
			String value = arg.substring(arg.indexOf("=") + 1);
			map.put(key, value);
		}
		return map;
	}

	public Map<String, String> args() {
		return args;
	}

	protected Integer toInt(String value) {
		try {
			if (value == null) return 0;
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	protected Boolean toBool(String value) {
		try {
			if (value == null) return false;
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			return false;
		}
	}
}
