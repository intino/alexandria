package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractDisplaySkeletonTemplate extends Template {

	protected AbstractDisplaySkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDisplaySkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(";\n\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).add(mark("package")).add(literal(".ui.*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\npublic class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(mark("abstractBox", "extension")).add(literal(" extends io.intino.alexandria.ui.displays.")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("abstractBox", "type")).add(literal("> {\n")).add(expression().add(literal("\t")).add(mark("component", "declarations").multiple("\n")).add(literal("\n")).add(literal("\n")).add(literal("    "))).add(literal("public ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n        super(box);\n    }\n\n    @Override\n\tprotected void init() {\n\t\tsuper.init();\n\t\t")).add(mark("component", "references").multiple("\n")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "initializations").multiple("\n"))).add(literal("\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("component", "class").multiple("\n"))).add(literal("\n}"))
		);
		return this;
	}
}