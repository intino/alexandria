package io.intino.alexandria.drivers;

import io.intino.alexandria.drivers.program.Resource;
import io.intino.alexandria.drivers.program.Script;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
	private String name;
	private List<Script> scripts = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();
	private Map<String, Object> parameters = new HashMap<>();

	public String name() {
		return name;
	}

	public Program name(String name) {
		this.name = name;
		return this;
	}

	public List<Script> scripts() {
		return scripts;
	}

	public Program scripts(List<Script> scripts) {
		this.scripts = scripts;
		return this;
	}

	public Program add(String script, String content) {
		scripts.add(new Script().name(script).content(new ByteArrayInputStream(content.getBytes())));
		return this;
	}

	public List<Resource> resources() {
		return resources;
	}

	public Program resources(List<Resource> resources) {
		this.resources = resources;
		return this;
	}

	public Program add(String resource, InputStream content) {
		resources.add(new Resource().name(resource).content(content));
		return this;
	}

	public Map<String, Object> parameters() {
		return parameters;
	}

	public Program parameters(Map<String, Object> parameters) {
		this.parameters = parameters;
		return this;
	}

}
