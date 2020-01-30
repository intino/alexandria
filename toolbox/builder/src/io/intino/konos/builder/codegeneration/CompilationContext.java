package io.intino.konos.builder.codegeneration;

import com.google.gson.Gson;
import io.intino.alexandria.logger.Logger;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.compiler.shared.PostCompileActionMessage;

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
import static io.intino.tara.compiler.shared.Configuration.Artifact.Model.Level.Solution;

public class CompilationContext {
	private List<OutputItem> compiledFiles;
	private List<PostCompileActionMessage> postCompileActionMessages;
	private CompilerConfiguration configuration;
	private DataHubManifest dataHubManifest;
	private File webModuleDirectory;
	private String parent;
	private ElementCache cache;
	private String boxName = null;
	private Map<String, String> classes = new HashMap<>();

	public CompilationContext() {
	}

	public CompilationContext(CompilerConfiguration configuration, List<PostCompileActionMessage> postCompileActionMessages, ElementCache cache, List<OutputItem> compiledFiles) {
		configuration(configuration);
		cache(cache);
		loadManifest();
		this.postCompileActionMessages = postCompileActionMessages;
		this.compiledFiles = compiledFiles;
	}

	public String project() {
		return configuration.project();
	}

	public String module() {
		return this.configuration.module();
	}

	public File webModuleDirectory() {
		return configuration.webModuleDirectory();
	}

	public String parent() {
		return configuration.parentInterface();
	}

	public CompilationContext parent(String parent) {
		this.parent = parent;
		return this;
	}

	public File root(Target target) {
		String rootDir = target == Target.Owner ? configuration.moduleDirectory().getAbsolutePath() : configuration.webModuleDirectory().getAbsolutePath();
		return createIfNotExists(new File(rootDir).getParentFile());
	}

	public File res(Target target) {
		return createIfNotExists(target == Target.Owner ? configuration.resDirectory() : accessorRes());
	}

	public File src(Target target) {
		return createIfNotExists(target == Target.Owner ? new File(configuration.srcDirectory(), packageName().replace(".", File.separator)) : accessorSrc());
	}

	public File gen(Target target) {
		return createIfNotExists(target == Target.Owner ? new File(configuration.genDirectory(), packageName().replace(".", File.separator)) : accessorGen());
	}

	public ElementCache cache() {
		return cache;
	}

	public CompilationContext cache(ElementCache cache) {
		this.cache = cache;
		return this;
	}

	public String packageName() {
		return configuration.generationPackage();
	}

	public String boxName() {
		if (boxName == null)
			boxName = snakeCaseToCamelCase(configuration != null ? configuration.artifactId() : Solution.name());
		return boxName;
	}

	public CompilerConfiguration configuration() {
		return configuration;
	}

	public DataHubManifest dataHubManifest() {
		return dataHubManifest;
	}

	public Map<String, String> classes() {
		return classes;
	}

	public CompilationContext classes(Map<String, String> classes) {
		this.classes = classes;
		return this;
	}

	private void configuration(CompilerConfiguration configuration) {
		this.configuration = configuration;
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
		dataHubManifest = configuration() != null ? loadManifest(configuration().datahubLibrary()) : null;
	}

	private DataHubManifest loadManifest(File jar) {
		if (jar == null) return null;
		try {
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

	public CompilationContext clone() {
		CompilationContext result = new CompilationContext(configuration, postCompileActionMessages, cache, compiledFiles);
		result.webModuleDirectory = webModuleDirectory;
		result.parent = parent;
		result.classes = classes;
		return result;
	}

	public String graphName() {
		String result = project() != null ? project() : "";
		result += module() != null ? module() : "";
		return !result.isEmpty() ? result : "test";
	}

	public List<PostCompileActionMessage> postCompileActionMessages() {
		return postCompileActionMessages;
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
