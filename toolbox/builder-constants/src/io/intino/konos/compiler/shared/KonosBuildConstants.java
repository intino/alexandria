package io.intino.konos.compiler.shared;

public class KonosBuildConstants {
	public static final String ENCODING = "encoding";
	public static final String OUTPUTPATH = "outputpath";
	public static final String PROJECT = "project";
	public static final String KONOSC = "Konosc";
	public static final String ACTION_MESSAGE = "%%postaction%%";
	public static final String START_ACTIONS_MESSAGE = "%%postaction%%";
	public static final String END_ACTIONS_MESSAGE = "/%postaction%%";
	public static final String REFRESH_MESSAGE = "%%refresh%%";
	public static final String REFRESH_BUILDER_MESSAGE_SEPARATOR = "#";
	public static final String SRC_PATH = "src.path";
	public static final String FINAL_OUTPUTPATH = "final_outputpath";
	public static final String RES_PATH = "res.path";
	public static final String SRC_FILE = "def.file";
	public static final String COMPILED_START = "%%c";
	public static final String COMPILED_END = "/%c";
	public static final String BUILD_END = "%end%";
	public static final String TO_RECOMPILE_START = "%%rc";
	public static final String MESSAGE_ACTION_START = "%%action";
	public static final String MESSAGE_ACTION_END = "/%action";
	public static final String TO_RECOMPILE_END = "/%rc";
	public static final String MESSAGES_START = "%%m";
	public static final String MESSAGES_END = "/%m";
	public static final String SEPARATOR = "#%%#%%%#%%%%%%%%%#";
	public static final String MESSAGE_ACTION_SEPARATOR = "#%%####%%%#%#####%#";
	public static final String PRESENTABLE_MESSAGE = "@#$%@# Presentable:";
	public static final String CLEAR_PRESENTABLE = "$@#$%^ CLEAR_PRESENTABLE";
	public static final String NO_KONOS = "Cannot compile Konos files: No Konos generator is defined";
	public static final String MODULE = "module";
	public static final String MODULE_PATH = "module.path";
	public static final String WEB_MODULE_PATH = "web.module.path";
	public static final String PROJECT_PATH = "project.path";
	public static final String PARAMETERS = "module.parameters";
	public static final String LANGUAGE = "language";
	public static final String LIBRARY = "library";
	public static final String LANGUAGE_GENERATION_PACKAGE = "language.generation.package";
	public static final String PARENT_INTERFACE = "parent.interface";
	public static final String INTINO_PROJECT_PATH = "intino.project.path";
	public static final String BOX_GENERATION_PACKAGE = "box.generation.package";
	public static final String COMPILATION_MODE = "compilation.mode";
	public static final String KONOSC_STUB_GENERATION_FAILED = "error generating stubs";
	public static final String LEVEL = "level";
	public static final String GROUP_ID = "groupId";
	public static final String ARTIFACT_ID = "artifactId";
	public static final String VERSION = "version";
	private KonosBuildConstants() {
	}


	public enum Mode {
		Normal, OnlyElements, Accessors
	}

}