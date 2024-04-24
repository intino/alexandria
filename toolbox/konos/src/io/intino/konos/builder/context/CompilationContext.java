package io.intino.konos.builder.context;

import com.google.gson.Gson;
import io.intino.alexandria.logger.Logger;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.cache.CacheReader;
import io.intino.konos.builder.codegeneration.cache.CacheWriter;
import io.intino.konos.builder.codegeneration.cache.LayerCache;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.builder.BuildConstants;
import io.intino.builder.PostCompileActionMessage;
import io.intino.konos.dsl.KonosGraph;
import io.intino.magritte.framework.Layer;
import io.intino.magritte.framework.Node;
import io.intino.magritte.io.model.Stash;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static io.intino.tara.builder.core.CompilerConfiguration.Level.Model;

public class CompilationContext {
	private final List<File> sources;
	private final ArrayList<WarningMessage> warningMessages;
	private final List<OutputItem> compiledFiles;
	private final List<PostCompileActionMessage> postCompileActionMessages;
	private CompilerConfiguration configuration;
	private DataHubManifest dataHubManifest;
	private String archetypeQN;
	private File webModuleDirectory;
	private String parent;
	private String boxName = null;
	private Map<String, String> classes = new HashMap<>();
	private LayerCache cache = null;

	public CompilationContext(CompilerConfiguration configuration, List<PostCompileActionMessage> postCompileActionMessages, List<File> sources, List<OutputItem> compiledFiles) {
		this.sources = sources;
		this.postCompileActionMessages = postCompileActionMessages;
		this.compiledFiles = compiledFiles;
		configuration(configuration);
		loadManifest();
		findArchetypeQn();
		sources.sort(Comparator.comparing(File::getName));
		this.warningMessages = new ArrayList<>();
	}

	public BuildConstants.Mode mode() {
		return configuration.mode();
	}

	public String project() {
		return configuration.project();
	}

	public String module() {
		return this.configuration.module();
	}

	public File serviceDirectory() {
		return configuration.serviceDirectory();
	}

	public void serviceDirectory(File file) {
		configuration.serviceDirectory(file);
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

	public void addWarning(WarningMessage message) {
		warningMessages.add(message);
	}

	public List<WarningMessage> warningMessages() {
		return warningMessages;
	}

	public File root(Target target) {
		String rootDir;
		if (target == Target.Server) rootDir = configuration.moduleDirectory().getAbsolutePath();
		else if (target == Target.Accessor) rootDir = configuration.serviceDirectory().getAbsolutePath();
		else if (target == Target.Android || target == Target.AndroidResource || target == Target.MobileShared)
			rootDir = configuration.serviceDirectory().getAbsolutePath();
		else rootDir = configuration.moduleDirectory().getAbsolutePath();
		return createIfNotExists(new File(rootDir));
	}

	public File res(Target target) {
		File resDir;
		if (target == Target.Server) resDir = configuration.resDirectory();
		else if (target == Target.Android || target == Target.AndroidResource)
			resDir = new File(root(target) + androidRelativePath() + File.separator + "res");
		else resDir = accessorRes();
		return createIfNotExists(resDir);
	}

	public File src(Target target) {
		File srcDir;
		if (target == Target.Server)
			srcDir = new File(configuration.srcDirectory(), packageName().replace(".", File.separator));
		else if (target == Target.Android)
			srcDir = new File(root(target) + androidRelativePath() + File.separator + "java", packageName().replace(".", File.separator) + File.separator + "mobile" + File.separator + "android");
		else if (target == Target.MobileShared)
			srcDir = new File(root(target) + androidSharedRelativePath(), packageName().replace(".", File.separator) + File.separator + "mobile");
		else srcDir = accessorSrc();
		return createIfNotExists(srcDir);
	}

	public File gen(Target target) {
		File genDir;
		if (target == Target.Server)
			genDir = new File(configuration.genDirectory(), packageName().replace(".", File.separator));
		else if (target == Target.Android)
			genDir = new File(root(target) + androidRelativePath() + File.separator + "java", packageName().replace(".", File.separator) + File.separator + "mobile" + File.separator + "android");
		else if (target == Target.MobileShared)
			genDir = new File(root(target) + androidSharedRelativePath(), packageName().replace(".", File.separator) + File.separator + "mobile");
		else genDir = accessorGen();
		return createIfNotExists(genDir);
	}

	public String packageName() {
		return configuration.generationPackage();
	}

	public String boxName() {
		if (boxName == null)
			boxName = snakeCaseToCamelCase(configuration != null ? configuration.artifactId() : Model.name());
		return boxName;
	}

	public CompilerConfiguration configuration() {
		return configuration;
	}

	public DataHubManifest dataHubManifest() {
		return dataHubManifest;
	}

	public String archetypeQN() {
		return archetypeQN;
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

	public String androidRelativePath() {
		return File.separator + "android" + File.separator + "src" + File.separator + "main";
	}

	public String androidSharedRelativePath() {
		return File.separator + "shared" + File.separator + "src" + File.separator + "commonMain" + File.separator + "kotlin";
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

	private void findArchetypeQn() {
		archetypeQN = configuration() != null ? findArchetypeQn(configuration().archetypeLibrary()) : null;
	}

	private String findArchetypeQn(File jar) {
		if (jar == null || !jar.exists()) return null;
		try {
			ZipFile zipFile = new ZipFile(jar);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith("Archetype.class"))
					return entry.getName().replace("/", ".").replace(".class", "");
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return null;
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
		if (layer == null) return sources.get(0).getAbsolutePath();
		final Node node = layer.core$();
		return sourceFileOf(node);
	}

	public String sourceFileOf(Node node) {
		String stash = node.stash();
		File file = sources.stream().filter(f -> f.getName().replace(".konos", "").equals(stash)).findFirst().orElse(null);
		return file == null ? sources.get(0).getAbsolutePath() : file.getAbsolutePath();
	}

	public static class DataHubManifest {
		public String terminal;
		public String qn;
		public List<String> publish;
		public List<String> subscribe;
		public Map<String, String> tankClasses;
		public List<String> connectionParameters;
		public List<String> additionalParameters;
		public boolean datamartsAutoLoad;

	}

}
