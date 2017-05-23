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
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nclass Box extends AbstractBox {\n\n\tBox(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\tsuper(configuration);\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("rest")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tvoid open() {\n\t\tsuper.open();\n\t}\n\n\tvoid close() {\n\t\tsuper.close();\n\t}\n\n\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("trigger", "rest"))).add(literal("static io.intino.konos.server.activity.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t"))
		);
		return this;
	}
}