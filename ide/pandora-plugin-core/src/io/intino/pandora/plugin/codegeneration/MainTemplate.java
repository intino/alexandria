package io.intino.pandora.plugin.codegeneration;

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
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport tara.magritte.Graph;\nimport io.intino.pandora.Box;\n\npublic class Main {\n\n\tpublic static void main(String[] args) {\n\t\tGraph graph = Graph.load(\"Model\").wrap(")).add(mark("language")).add(literal("Application.class);\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = args.length != 0 ? createConfigurationFromArgs(): loadDevelopConfiguration();\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Box box = new ")).add(mark("name", "firstUpperCase")).add(literal("Box(graph, configuration);\n\t\tbox.init();\n\t\tgraph.application().execute();\n\t}\n\n\tprivate static ")).add(mark("name", "firstUpperCase")).add(literal("Configuration createConfigurationFromArgs() {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\t}\n\n\tprivate static ")).add(mark("name", "firstUpperCase")).add(literal("Configuration loadDevelopConfiguration() {\n    \t")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();\n\t}\n}"))
		);
		return this;
	}
}