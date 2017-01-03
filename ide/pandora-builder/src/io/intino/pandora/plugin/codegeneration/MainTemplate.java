package io.intino.pandora.plugin.codegeneration;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class MainTemplate extends Template {

	protected MainTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MainTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.tara.magritte.Graph;\nimport io.intino.pandora.Box;\n\npublic class Main {\n\n\tpublic static void main(String[] args) {\n\t\tGraph graph = Graph.load(\"Model\").wrap(")).add(expression().add(mark("dslPackage")).add(literal("."))).add(mark("language")).add(literal("Application.class, null);//TODO\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = args.length != 0 ? createConfigurationFromArgs(): loadDevelopConfiguration();\n\t\trun(configuration);\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(exit()));\n\n\t}\n\n\tprivate static ")).add(mark("name", "firstUpperCase")).add(literal("Configuration createConfigurationFromArgs() {\n\t\treturn new ")).add(mark("name", "firstUpperCase")).add(literal("Configuration();//TODO\n\t}\n\n\tpublic static void run(")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Box box = new ")).add(mark("name", "firstUpperCase")).add(literal("Box(graph, configuration);\n\t\tbox.init();\n\t\tgraph.application().execute();\n\t}\n\n\tprivate static Runnable exit() {\n\t\treturn ()->{\n\t\t\t")).add(expression().add(mark("rest")).add(literal("Spark.stop();"))).add(literal("\n\t\t};\n\t}\n}"))
		);
		return this;
	}
}