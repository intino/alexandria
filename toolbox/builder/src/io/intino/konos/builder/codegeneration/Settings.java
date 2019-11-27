package io.intino.konos.builder.codegeneration;

import com.google.gson.Gson;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import io.intino.alexandria.logger.Logger;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.plugin.dependencyresolution.DependencyCatalog;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.tara.compiler.shared.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static io.intino.tara.compiler.shared.Configuration.Model.Level.Solution;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.configurationOf;

public class Settings {
	private Project project;
	private Module module;
	private LegioConfiguration moduleConfiguration;
	private DataHubManifest dataHubManifest;
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
		project(module == null ? null : module.getProject());
		module(module);
		moduleConfiguration(configurationOf(module));
		res(res);
		src(src);
		gen(gen);
		packageName(packageName);
		cache(cache);
		loadManifest();
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
		String rootDir = target == Target.Owner ? module().getModuleFilePath() : webModule().getModuleFilePath();
		return createIfNotExists(new File(rootDir).getParentFile());
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
		if (boxName == null)
			boxName = snakeCaseToCamelCase(moduleConfiguration != null ? moduleConfiguration.artifactId() : Solution.name());
		return boxName;
	}

	public LegioConfiguration moduleConfiguration() {
		return moduleConfiguration;
	}

	public DataHubManifest dataHubManifest() {
		return dataHubManifest;
	}

	public Map<String, String> classes() {
		return classes;
	}

	public Settings classes(Map<String, String> classes) {
		this.classes = classes;
		return this;
	}

	private void moduleConfiguration(Configuration configuration) {
		moduleConfiguration = configuration instanceof LegioConfiguration ? (LegioConfiguration) configuration : null;
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


	private void loadManifest() {
		dataHubManifest = loadManifest(moduleConfiguration().dataHub());
	}

	private DataHubManifest loadManifest(DependencyCatalog.Dependency dependency) {
		if (dependency == null) return null;
		try {
			File jar = dependency.jar();
			ZipFile zipFile = new ZipFile(jar);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals("terminal.mf"))
					return new Gson().fromJson(new String(zipFile.getInputStream(entry).readAllBytes()), DataHubManifest.class);
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
	}

	public Settings clone() {
		Settings result = new Settings(module, src, gen, res, packageName, cache);
		result.webModule = webModule;
		result.parent = parent;
		result.classes = classes;
		return result;
	}

	public static class DataHubManifest {
		public String terminal;
		public String qn;
		public List<String> parameters;
		public List<String> publish;
		public List<String> subscribe;
		public Map<String, String> tankClasses;
		public Map<String, List<String>> messageContexts;
	}

}
