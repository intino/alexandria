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
			rule().add((condition("type", "main"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.alexandria.core.Box;\n\npublic class Main {\n\tpublic static void main(String[] args) {\n\t\tBox box = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(args);")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("model")).add(literal("\n")).add(literal("\t\tbox.open();"))).add(literal("\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(box::close));\n\t}\n}")),
			rule().add((condition("type", "model")), (condition("trigger", "model"))).add(literal("io.intino.tara.magritte.Graph graph = new io.intino.tara.magritte.Graph().loadStashes(\"")).add(mark("name")).add(literal("\");\nbox.put(graph);"))
		);
		return this;
	}
}