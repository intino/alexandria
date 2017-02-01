package io.intino.konos.builder.codegeneration;

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
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.tara.magritte.Graph;\n\npublic class Main {\n\n\tpublic static void main(String[] args) {\n\t\tGraph graph = Graph.use(store(), ")).add(mark("wrapper").multiple(", ")).add(literal(").load();\n\t\trun(graph, createConfigurationFromArgs(args));\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(exit()));\n\t}\n\n\tprivate static ")).add(mark("name", "firstUpperCase")).add(literal("Configuration createConfigurationFromArgs(String[] args) {\n\t\treturn new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration(args);//TODO\n\t}\n\n\tprivate static void run(Graph graph, ")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Box box = new ")).add(mark("name", "firstUpperCase")).add(literal("Box(graph, configuration);\n\t\tbox.init();\n\t\tgraph.application().execute();\n\t}\n\n\tprivate static Runnable exit() {\n\t\treturn () -> {\n\t\t\t")).add(expression().add(mark("rest")).add(literal("Spark.stop();"))).add(literal("\n\t\t};\n\t}\n\n\tprivate static Store store() {\n        return new io.intino.tara.magritte.stores.ResourceStore();\n    }\n}")),
			rule().add((condition("trigger", "wrapper"))).add(mark("value")).add(literal(".class"))
		);
		return this;
	}
}