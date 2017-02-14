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
			rule().add((condition("type", "graphprovider"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.tara.magritte.Graph;\n\nclass GraphProvider {\n\n    static Graph get(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n        return Graph.use(store(configuration), ")).add(mark("wrapper").multiple(", ")).add(literal(").load();\n    }\n\n    private static io.intino.tara.magritte.Store store(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n        // return new io.intino.tara.magritte.FileSystemStore(configuration.store());\n        return new io.intino.tara.magritte.stores.ResourcesStore();\n    }\n}")),
			rule().add((condition("trigger", "wrapper"))).add(mark("value")).add(literal(".class"))
		);
		return this;
	}
}