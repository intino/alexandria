package io.intino.konos.builder.codegeneration.main;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MainTemplate extends Template {

	protected MainTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MainTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\npublic class Main {\n\tpublic static void main(String[] args) {\n\t\t")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box box = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(args).open();\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(box::close));\n\t}\n}"))
		);
		return this;
	}
}