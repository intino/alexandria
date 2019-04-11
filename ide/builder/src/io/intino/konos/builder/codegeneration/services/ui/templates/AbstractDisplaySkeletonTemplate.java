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
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(";\n\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).add(mark("package")).add(literal(".ui.*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\n")).add(mark("moldsImport")).add(literal("\n")).add(mark("blocksImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\npublic class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(mark("abstractBox", "extension")).add(literal(" extends io.intino.alexandria.ui.displays.")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("abstractBox", "type")).add(literal(">")).add(expression().add(literal(" ")).add(mark("implements"))).add(literal(" {\n\t")).add(expression().add(mark("attribute").multiple("\n")).add(literal("\n")).add(literal("\t"))).add(expression().add(mark("component", "declarations").multiple("\n")).add(literal("\n")).add(literal("\n")).add(literal("    "))).add(literal("public ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n        super(box);\n        id(\"")).add(mark("id")).add(literal("\");\n    }\n\n    @Override\n\tpublic void init() {\n\t\tsuper.init();\n\t\t")).add(mark("componentReferences")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "initializations").multiple("\n"))).add(literal("\n\t}")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("methods")).add(literal("\n")).add(literal("\t"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "class").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "method").multiple("\n"))).add(literal("\n}")),
			rule().add((condition("type", "moldsImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.molds.*;")),
			rule().add((condition("type", "blocksImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.blocks.*;")),
			rule().add((condition("type", "componentReferences & forMold"))).add(mark("component", "moldReferences").multiple("\n")),
			rule().add((condition("type", "componentReferences & forBlock"))).add(mark("component", "blockReferences").multiple("\n")),
			rule().add((condition("type", "componentReferences"))).add(mark("component", "references").multiple("\n")),
			rule().add((condition("type", "attribute"))).add(literal("public ")).add(mark("clazz")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal(";")),
			rule().add((condition("type", "methods & timeconsumingmold"))).add(literal("@Override\npublic void showLoading() {\n\tnotifier.refreshLoading(true);\n}\n\n@Override\npublic void hideLoading() {\n\tnotifier.refreshLoading(false);\n}"))
		);
		return this;
	}
}