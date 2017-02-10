package io.intino.konos.builder.codegeneration.main;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class GraphProviderTemplate extends Template {

	protected GraphProviderTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new GraphProviderTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "graphprovider"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.tara.magritte.Graph;\n\n\tpublic class GraphProvider {\n\n\t\tpublic static void get(")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t\tGraph.use(store(configuration), ")).add(mark("wrapper").multiple(", ")).add(literal(").load();\n\t\t}\n\n\t\tprivate static Store store(")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t    // return new io.intino.tara.magritte.FileSystemStore(configuration.store());\n\t\t\treturn new io.intino.tara.magritte.stores.ResourcesStore();\n\t\t}\n\t}")),
			rule().add((condition("trigger", "wrapper"))).add(mark("value")).add(literal(".class"))
		);
		return this;
	}
}