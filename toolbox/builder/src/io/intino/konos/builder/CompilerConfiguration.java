package io.intino.konos.builder;

import io.intino.Configuration.Artifact.Model.Level;
import tara.dsl.Konos;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class CompilerConfiguration implements Cloneable {
	private static final Logger LOG = Logger.getGlobal();

	static {
		Logger.getGlobal().setLevel(java.util.logging.Level.INFO);
		LOG.setUseParentHandlers(false);
		for (Handler handler : LOG.getHandlers()) LOG.removeHandler(handler);
		final StreamHandler errorHandler = new StreamHandler(System.err, new SimpleFormatter());
		errorHandler.setLevel(java.util.logging.Level.WARNING);
		LOG.addHandler(errorHandler);
		final StreamHandler infoHandler = new StreamHandler(System.out, new SimpleFormatter());
		infoHandler.setLevel(java.util.logging.Level.INFO);
		LOG.addHandler(infoHandler);
	}

	private int warningLevel;
	private String sourceEncoding;
	private String project;
	private String module;
	private String parentInterface;
	private boolean debug;
	private List<File> sources = new ArrayList<>();
	private File srcDirectory;
	private File genDirectory;
	private File resDirectory;
	private File outDirectory;
	private String groupID;
	private String artifactID;
	private String version;
	private ModelConfiguration model;
	private boolean verbose;
	private File tempDirectory;
	private File datahubLibrary;
	private File intinoProjectDirectory;
	private String generationPackage;
	private PrintStream out = System.out;
	private File projectDirectory;
	private File webModuleDirectory;
	private File moduleDirectory;
	private List<String> parameters = new ArrayList<>();

	public CompilerConfiguration() {
		setWarningLevel(1);
		setDebug(false);
		String encoding;
		encoding = System.getProperty("file.encoding", "UTF8");
		encoding = System.getProperty("tara.source.encoding", encoding);
		sourceEncoding(encoding);
		this.model = new ModelConfiguration();
		try {
			tempDirectory = Files.createTempDirectory("_tara_").toFile();
		} catch (IOException e) {
			LOG.log(java.util.logging.Level.SEVERE, e.getMessage(), e);
		}
	}

	public int getWarningLevel() {
		return this.warningLevel;
	}

	public void setWarningLevel(int level) {
		if ((level < 0) || (level > 3)) {
			this.warningLevel = 1;
		} else
			this.warningLevel = level;
	}

	public String sourceEncoding() {
		return this.sourceEncoding;
	}

	public void sourceEncoding(String encoding) {
		if (encoding == null) sourceEncoding = "UTF8";
		this.sourceEncoding = encoding;
	}

	public boolean getDebug() {
		return this.debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public File getTempDirectory() {
		return tempDirectory;
	}

	public String project() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String groupId() {
		return groupID;
	}

	public void groupId(String groupID) {
		this.groupID = groupID;
	}

	public String artifactId() {
		return artifactID;
	}

	public void artifactId(String artifactID) {
		this.artifactID = artifactID;
	}

	public String version() {
		return version;
	}

	public void version(String version) {
		this.version = version;
	}

	public void generationPackage(String generationPackage) {
		this.generationPackage = generationPackage;
	}

	public String generationPackage() {
		return generationPackage;
	}

	public String module() {
		return module;
	}

	public void module(String module) {
		this.module = module;
	}

	public ModelConfiguration model() {
		return model;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void intinoProjectDirectory(File intinoPath) {
		this.intinoProjectDirectory = intinoPath;
	}

	public File intinoProjectDirectory() {
		return intinoProjectDirectory;
	}

	public List<File> sources() {
		return sources;
	}

	public File srcDirectory() {
		return srcDirectory;
	}

	public void srcDirectory(File srcDirectory) {
		this.srcDirectory = srcDirectory;
	}

	public void genDirectory(File directory) {
		if (directory != null) {
			this.genDirectory = directory;
			this.genDirectory.mkdirs();
		}
	}

	public File genDirectory() {
		return genDirectory;
	}

	public File webModuleDirectory() {
		return webModuleDirectory;
	}

	public File moduleDirectory() {
		return moduleDirectory;
	}

	public File projectDirectory() {
		return projectDirectory;
	}

	public void webModuleDirectory(File webModuleDirectory) {
		this.webModuleDirectory = webModuleDirectory;
	}

	public void moduleDirectory(File moduleDirectory) {
		this.moduleDirectory = moduleDirectory;
	}

	public void projectDirectory(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}

	public File resDirectory() {
		return resDirectory;
	}

	public void resDirectory(File resDirectory) {
		this.resDirectory = resDirectory;
	}

	public File outDirectory() {
		return this.outDirectory;
	}

	public PrintStream out() {
		return out;
	}

	public void out(PrintStream out) {
		this.out = out;
	}

	public String parentInterface() {
		return parentInterface;
	}

	public void parentInterface(String parentInterface) {
		this.parentInterface = parentInterface;
	}

	public File datahubLibrary() {
		return datahubLibrary;
	}

	public void datahubLibrary(File datahubLibrary) {
		this.datahubLibrary = datahubLibrary;
	}

	public void parameters(String[] parameters) {
		this.parameters = List.of(parameters);
	}

	public List<String> parameters() {
		return parameters;
	}

	public void outDirectory(File file) {
		this.outDirectory = file;
	}

	public static class ModelConfiguration {

		private String language;
		private Level level;
		private String outDsl;
		private String generationPackage;


		public String language() {
			return language;
		}

		public void outDsl(String outDsl) {
			this.outDsl = outDsl;
		}

		public String outDsl() {
			return outDsl;
		}

		public void language(String language) {
			this.language = language;
		}

		public void level(Level level) {
			this.level = level;
		}

		public Level level() {
			return level;
		}

		public String generationPackage() {
			return generationPackage;
		}

		public void generationPackage(String generationPackage) {
			this.generationPackage = generationPackage;
		}
	}

	public static class Language {
		Konos language;
		String name;
		String version;

		public Language(String name, String version) {
			this.name = name;
			this.version = version;
		}

		public Language(tara.dsl.Konos language) {
			this.language = language;
			this.name = this.language.languageName();
		}

		public Konos get() {
			return language == null ? (language = new Konos()) : language;
		}

		public String name() {
			return language == null ? name : language.languageName();
		}

		public String version() {
			return version;
		}

		public void version(String version) {
			this.version = version;
		}
	}
}
