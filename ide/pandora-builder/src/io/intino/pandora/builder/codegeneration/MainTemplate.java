package io.intino.pandora.builder.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MainTemplate extends Template {

	protected MainTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MainTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.tara.magritte.Graph;\n\npublic class Main {\n\n\tpublic static void main(String[] args) {\n\t\tGraph graph = Graph.use(")).add(expression().add(mark("dslPackage")).add(literal("."))).add(mark("language")).add(literal("Application.class")).add(expression().add(literal(", ")).add(mark("metadslPackage")).add(literal("."))).add(expression().add(mark("metalanguage")).add(literal("Platform.class"))).add(literal(").load();\n\t\trun(graph, createConfigurationFromArgs(args));\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(exit()));\n\n\t}\n\n\tprivate static ")).add(mark("name", "firstUpperCase")).add(literal("Configuration createConfigurationFromArgs(String[] args) {\n\t\treturn new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();//TODO\n\t}\n\n\tprivate static void run(Graph graph, ")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Box box = new ")).add(mark("name", "firstUpperCase")).add(literal("Box(graph, configuration);\n\t\tbox.init();\n\t\tgraph.application().execute();\n\t}\n\n\tprivate static Runnable exit() {\n\t\treturn () -> {\n\t\t\t")).add(expression().add(mark("rest")).add(literal("Spark.stop();"))).add(literal("\n\t\t};\n\t}\n}"))
		);
		return this;
	}
}