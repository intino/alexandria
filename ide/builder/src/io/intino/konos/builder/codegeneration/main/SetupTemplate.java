package io.intino.konos.builder.codegeneration.main;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class SetupTemplate extends Template {

	protected SetupTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SetupTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "setup"))).add(literal("package ")).add(mark("package")).add(literal(";\n\n")).add(expression().add(mark("tara", "empty")).add(literal("import io.intino.tara.magritte.Graph;"))).add(literal("\n\nimport java.util.logging.ConsoleHandler;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n\nclass Setup {")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("tara"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("rest")).add(literal("\n")).add(literal("\t"))).add(literal("\n\n\tstatic void configureBox(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box) {\n\t\tconfigureLogger(\"log\"); //TODO change path in case\n\t}\n\n\tstatic void execute(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box) {\n\n    }\n\n\tstatic void quit(ConsulBox box) {\n\n\t}\n\n\tprivate static void configureLogger(String path) {\n\t\tfinal Logger logger = Logger.getGlobal();\n\t\tfinal ConsoleHandler handler = new ConsoleHandler();\n\t\thandler.setLevel(Level.INFO);\n\t\thandler.setFormatter(new io.intino.konos.LogFormatter(path));\n\t\tlogger.addHandler(handler);\n\t}\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "tara"))).add(literal("static Graph initGraph(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\treturn Graph.use(store(configuration), ")).add(mark("wrapper").multiple(", ")).add(literal(").load(")).add(mark("outDSL", "quoted")).add(literal(");\n}\n\nprivate static io.intino.tara.magritte.Store store(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t// return new io.intino.tara.magritte.stores.FileSystemStore(configuration.store());\n\treturn new io.intino.tara.magritte.stores.ResourcesStore();\n}")),
			rule().add((condition("trigger", "wrapper"))).add(mark("value")).add(literal(".class")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("trigger", "rest"))).add(literal("static io.intino.konos.server.activity.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t"))
		);
		return this;
	}
}