package io.intino.konos.builder.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BoxTemplate extends Template {

	protected BoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\n")).add(expression().add(mark("tara", "empty")).add(literal("import io.intino.tara.magritte.Graph;"))).add(literal("\n\nclass Box extends AbstractBox {\n\n\tBox(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\tsuper(configuration);\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("tara"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("rest")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tvoid init() {\n\t\tsuper.init();\n\t}\n\n\tvoid start() {\n\t\tsuper.start();\n    }\n\n\tvoid terminate() {\n\t\tsuper.terminate();\n\t}\n\n\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "tara"))).add(literal("Graph initGraph() {\n\treturn Graph.use(store(), ")).add(mark("wrapper").multiple(", ")).add(literal(").load(")).add(mark("outDSL", "quoted")).add(literal(");\n}\n\nprivate io.intino.tara.magritte.Store store() {\n\t// return new io.intino.tara.magritte.stores.FileSystemStore(configuration.store());\n\treturn new io.intino.tara.magritte.stores.ResourcesStore();\n}")),
			rule().add((condition("trigger", "wrapper"))).add(mark("value")).add(literal(".class")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("trigger", "rest"))).add(literal("static io.intino.konos.server.activity.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t"))
		);
		return this;
	}
}