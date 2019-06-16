package io.intino.alexandria.driver;

import java.nio.file.Path;
import java.util.List;

public class Program {
	private String name;
	private List<Path> algorithms;
	private List<Path> resources;

	public String name() {
		return name;
	}

	public Program name(String name) {
		this.name = name;
		return this;
	}

	public List<Path> algorithms() {
		return algorithms;
	}

	public Program algorithms(List<Path> algorithms) {
		this.algorithms = algorithms;
		return this;
	}

	public List<Path> resources() {
		return resources;
	}

	public Program resources(List<Path> resources) {
		this.resources = resources;
		return this;
	}
}
