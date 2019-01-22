package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.util.Map;

public class Settings {
	private Project project;
	private Module module;
	private String parent;
	private File src;
	private File gen;
	private String packageName;
	private String boxName;
	private Map<String, String> classes;

	public Project project() {
		return project;
	}

	public Settings project(Project project) {
		this.project = project;
		return this;
	}

	public Module module() {
		return this.module;
	}

	public Settings module(Module module) {
		this.module = module;
		return this;
	}

	public String parent() {
		return this.parent;
	}

	public Settings parent(String parent) {
		this.parent = parent;
		return this;
	}

	public File src() {
		return src;
	}

	public Settings src(File src) {
		this.src = src;
		return this;
	}

	public File gen() {
		return gen;
	}

	public Settings gen(File gen) {
		this.gen = gen;
		return this;
	}

	public String packageName() {
		return packageName;
	}

	public Settings packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public String boxName() {
		return boxName;
	}

	public Settings boxName(String boxName) {
		this.boxName = boxName;
		return this;
	}

	public Map<String, String> classes() {
		return classes;
	}

	public Settings classes(Map<String, String> classes) {
		this.classes = classes;
		return this;
	}

}
