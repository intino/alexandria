package io.intino.pandora.plugin.codegeneration.exception;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.CRLF;

public class ExceptionTemplate extends Template {

	protected ExceptionTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ExceptionTemplate(Locale.ENGLISH, CRLF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "Exception"))).add(literal("package ")).add(mark("package")).add(literal(".exceptions;\n\nimport org.siani.pandora.exceptions.*;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(mark("<missing ID>")).add(literal("Exception extends ")).add(mark("code")).add(literal("Exception{\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("(String label, ")).add(mark("type")).add(literal(" value){\n        super(label); // TODO modify exceptions at pandora;\n    }\n}"))
		);
		return this;
	}
}