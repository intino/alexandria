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
			rule().add((condition("type", "setup"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.tara.magritte.Graph;\n\nclass Setup {\n\n\tstatic Graph initGraph(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\treturn Graph.use(store(configuration), ")).add(mark("wrapper").multiple(", ")).add(literal(").load(")).add(mark("outDSL", "quoted")).add(literal(");\n\t}\n\n\tstatic void configureBox(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box) {\n\n\t}\n\n\tprivate static io.intino.tara.magritte.Store store(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t// return new io.intino.tara.magritte.stores.FileSystemStore(configuration.store());\n\t\treturn new io.intino.tara.magritte.stores.ResourcesStore();\n\t}\n\n\t")).add(mark("rest")).add(literal("\n}")),
			rule().add((condition("trigger", "wrapper"))).add(mark("value")).add(literal(".class")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("trigger", "rest"))).add(literal("static io.intino.konos.server.activity.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t"))
		);
		return this;
	}
}