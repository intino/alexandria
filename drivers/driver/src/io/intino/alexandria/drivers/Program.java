package io.intino.alexandria.drivers;

import java.nio.file.Path;
import java.util.List;

public class Program {
	private String name;
	private List<Path> scripts;
	private List<Path> data;

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

	public List<Path> data() {
		return data;
	}

	public Program data(List<Path> data) {
		this.data = data;
		return this;
	}
}
