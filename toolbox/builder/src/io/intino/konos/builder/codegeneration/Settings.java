package io.intino.konos.builder.codegeneration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.tara.compiler.shared.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.configurationOf;

public class Settings {
	private Project project;
	private Module module;
	private Module webModule;
	private String parent;
	private File res;
	private File src;
	private File gen;
	private ElementCache cache;
	private String packageName;
	private String boxName = null;
	private Map<String, String> classes = new HashMap<>();

	public Settings() {
	}

	public Settings(Module module, File src, File gen, File res, String packageName, ElementCache cache) {
		module(module);
		project(module == null ? null : module.getProject());
		res(res);
		src(src);
		gen(gen);
		packageName(packageName);
		cache(cache);
	}

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

	public Module webModule() {
		return this.webModule;
	}

	public Settings webModule(Module module) {
		this.webModule = module;
		return this;
	}

	public String parent() {
		return this.parent;
	}

	public Settings parent(String parent) {
		this.parent = parent;
		return this;
	}

	public File root(Target target) {
		return createIfNotExists(target == Target.Owner ? new File(module().getModuleFilePath()).getParentFile() : new File(webModule().getModuleFilePath()).getParentFile());
	}

	public File res(Target target) {
		return createIfNotExists(target == Target.Owner ? res : accessorRes());
	}

	public Settings res(File res) {
		this.res = res;
		return this;
	}

	public File src(Target target) {
		return createIfNotExists(target == Target.Owner ? src : accessorSrc());
	}

	public Settings src(File src) {
		this.src = src;
		return this;
	}

	public File gen(Target target) {
		return createIfNotExists(target == Target.Owner ? gen : accessorGen());
	}

	public Settings gen(File gen) {
		this.gen = gen;
		return this;
	}

	public ElementCache cache() {
		return cache;
	}

	public Settings cache(ElementCache cache) {
		this.cache = cache;
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
		if (boxName == null) boxName = snakeCaseToCamelCase(module() != null ? configurationOf(module()).artifactId() : Configuration.Level.Solution.name());
		return boxName;
	}

	public Map<String, String> classes() {
		return classes;
	}

	public Settings classes(Map<String, String> classes) {
		this.classes = classes;
		return this;
	}

	private File accessorRes() {
		return createIfNotExists(new File(root(Target.Accessor) + File.separator + "res"));
	}

	private File accessorSrc() {
		return createIfNotExists(new File(root(Target.Accessor) + File.separator + "src"));
	}

	private File accessorGen() {
		return createIfNotExists(new File(root(Target.Accessor) + File.separator + "gen"));
	}

	public Settings clone() {
		Settings result = new Settings();
		result.project = project;
		result.module = module;
		result.webModule = webModule;
		result.parent = parent;
		result.res = res;
		result.src = src;
		result.gen = gen;
		result.cache = cache;
		result.packageName = packageName;
		result.boxName = boxName;
		result.classes = classes;
		return result;
	}
}
