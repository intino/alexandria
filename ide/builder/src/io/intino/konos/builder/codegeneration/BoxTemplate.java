package io.intino.konos.builder.codegeneration;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class BoxTemplate extends Template {

	protected BoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box extends AbstractBox {\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(String[] args) {\n\t\tsuper(args);\n\t}\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\tsuper(configuration);\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\tsuper.put(o);\n\t\treturn this;\n\t}\n\n\tpublic io.intino.alexandria.core.Box open() {\n\t\treturn super.open();\n\t}\n\n\tpublic void close() {\n\t\tsuper.close();\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("rest"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("authenticationValidator"))).add(literal("\n}")),
			rule().add((condition("trigger", "hide"))),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
				rule().add((condition("trigger", "rest"))).add(literal("protected io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t\n\nprotected io.intino.alexandria.ui.services.EditorService editorService(java.net.URL editorServiceUrl) {\n\t//TODO add your editorService\n\treturn null;\n}")),
				rule().add((condition("trigger", "authenticationValidator"))).add(literal("public io.intino.alexandria.rest.security.")).add(mark("type", "FirstUpperCase")).add(literal("AuthenticationValidator authenticationValidator() {\n\treturn token -> false;\n}"))
		);
		return this;
	}
}