package io.intino.konos.builder.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BoxSrcTemplate extends Template {

	protected BoxSrcTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxSrcTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box extends AbstractBox {\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(String[] args) {\n\t\tsuper(args);\n\t}\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\tsuper(configuration);\n\t}\n\n\t@Override\n\tpublic io.intino.konos.alexandria.Box put(Object o) {\n\t\tsuper.put(o);\n\t\treturn this;\n\t}\n\n\tpublic io.intino.konos.alexandria.Box open() {\n\t\treturn super.open();\n\t}\n\n\tpublic void close() {\n\t\tsuper.close();\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("rest"))).add(literal("\n}")),
			rule().add((condition("trigger", "hide"))),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("trigger", "rest"))).add(literal("static io.intino.konos.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t"))
		);
		return this;
	}
}