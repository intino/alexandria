package io.intino.konos.builder.codegeneration;

import com.google.gson.Gson;
import io.intino.alexandria.logger.Logger;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.cache.CacheReader;
import io.intino.konos.builder.codegeneration.cache.CacheWriter;
import io.intino.konos.builder.codegeneration.cache.LayerCache;
import io.intino.konos.compiler.shared.PostCompileActionMessage;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.magritte.framework.Layer;
import io.intino.magritte.io.Stash;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.Configuration.Artifact.Model.Level.Solution;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;

public class CompilationContext {
	private final List<File> sources;
	private List<OutputItem> compiledFiles;
	private List<PostCompileActionMessage> postCompileActionMessages;
	private CompilerConfiguration configuration;
	private DataHubManifest dataHubManifest;
	private File webModuleDirectory;
	private String parent;
	private String boxName = null;
	private Map<String, String> classes = new HashMap<>();
	private LayerCache cache = null;

	public CompilationContext(CompilerConfiguration configuration, List<PostCompileActionMessage> postCompileActionMessages, List<File> sources, List<OutputItem> compiledFiles) {
		this.sources = sources;
		configuration(configuration);
		loadManifest();
		this.postCompileActionMessages = postCompileActionMessages;
		this.compiledFiles = compiledFiles;
	}

	public boolean onlyElements() {
		return configuration.onlyElements();
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

	public void webModuleDirectory(File file) {
		configuration.webModuleDirectory(file);
	}

	public String parent() {
		return configuration.parentInterface();
	}

	public CompilationContext parent(String parent) {
		this.parent = parent;
		return this;
	}

	public List<OutputItem> compiledFiles() {
		return compiledFiles;
	}

	public File root(Target target) {
		String rootDir = target == Target.Owner ? configuration.moduleDirectory().getAbsolutePath() : configuration.webModuleDirectory().getAbsolutePath();
		return createIfNotExists(new File(rootDir));
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

	public LayerCache cache() {
		return cache;
	}

	public void loadCache(KonosGraph graph, Stash[] stashes) {
		this.cache = new CacheReader(configuration.configurationDirectory()).load(graph, stashes);
	}

	public void saveCache() {
		new CacheWriter(configuration.configurationDirectory()).save(cache);
	}

	private void loadManifest() {
		dataHubManifest = configuration() != null ? loadManifest(configuration().datahubLibrary()) : null;
	}

	private DataHubManifest loadManifest(File jar) {
		if (jar == null || !jar.exists()) return null;
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
		CompilationContext result = new CompilationContext(configuration, postCompileActionMessages, sources, compiledFiles);
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

	public String sourceFileOf(Layer layer) {
		String defaultFile = sources.get(0).getAbsolutePath();
		if (layer == null) return defaultFile;
		String stash = layer.core$().stash();
		File file = sources.stream().filter(f -> f.getName().replace(".konos", "").equals(stash)).findFirst().orElse(null);
		return file == null ? defaultFile : file.getAbsolutePath();
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
