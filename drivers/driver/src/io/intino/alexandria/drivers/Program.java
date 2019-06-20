package io.intino.alexandria.drivers;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Program {
	private String name;
	private List<Path> scripts;
	private List<Path> resources;
	private Map<String, String> parameters;

	public String name() {
		return name;
	}

	public Program name(String name) {
		this.name = name;
		return this;
	}

	public List<Path> scripts() {
		return scripts;
	}

	public Program scripts(List<Path> scripts) {
		this.scripts = scripts;
		return this;
	}

	public List<Path> resources() {
		return resources;
	}

	public Program resources(List<Path> resources) {
		this.resources = resources;
		return this;
	}

	public Map<String, String> parameters() {
		return parameters;
	}

	public Program parameters(Map<String, String> parameters) {
		this.parameters = parameters;
		return this;
	}
}
