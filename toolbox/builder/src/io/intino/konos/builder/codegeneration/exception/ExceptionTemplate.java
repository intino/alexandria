package io.intino.konos.builder.codegeneration.exception;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ExceptionTemplate extends Template {

	protected ExceptionTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ExceptionTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "exception"))).add(literal("package ")).add(mark("package")).add(literal(".exceptions;\n\nimport io.intino.alexandria.exceptions.*;\nimport java.util.Map;\nimport java.util.LinkedHashMap;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" extends  io.intino.alexandria.exceptions.")).add(mark("code")).add(literal(" {\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(String message) {\n        super(message);\n    }\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(String message, Map<String, String> parameters) {\n\t\tsuper(message, parameters);\n\t}\n}"))
		);
		return this;
	}
}