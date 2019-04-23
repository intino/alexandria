package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractDesktopSkeletonTemplate extends Template {

	protected AbstractDesktopSkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractDesktopSkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "desktop"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays.desktops;\n\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).add(mark("package")).add(literal(".ui.*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;")).add(expression().add(literal("\n")).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.desktops.")).add(mark("abstract")).add(mark("name", "firstUpperCase")).add(literal(";"))).add(literal("\n\n")).add(mark("templatesImport")).add(literal("\n")).add(mark("blocksImport")).add(literal("\n")).add(mark("itemsImport")).add(literal("\n")).add(mark("rowsImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\npublic class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(mark("abstractBox", "extension")).add(literal(" extends io.intino.alexandria.ui.displays.")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("abstractBox", "type")).add(literal("> {\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Header header;\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Tabs tabs;\n\n    public ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n        super(box);\n        id(\"")).add(mark("id")).add(literal("\");\n    }\n\n    @Override\n\tpublic void init() {\n\t\tsuper.init();\n\t\theader = register(new ")).add(mark("name", "firstUpperCase")).add(literal("Header<>(box()).id(\"")).add(mark("headerId")).add(literal("\"));\n\t\ttabs = register(new ")).add(mark("name", "firstUpperCase")).add(literal("Tabs<>(box()).id(\"")).add(mark("tabBarId")).add(literal("\"));\n\t}\n\n\tpublic class ")).add(mark("name", "firstUpperCase")).add(literal("Header")).add(mark("abstractBox", "extension")).add(literal(" extends io.intino.alexandria.ui.displays.components.Header<")).add(mark("abstractBox", "type")).add(literal("> {\n\t\t")).add(expression().add(mark("attribute").multiple("\n")).add(literal("\n")).add(literal("\t\t"))).add(expression().add(mark("component", "declarations").multiple("\n")).add(literal("\n")).add(literal("\n")).add(literal("\t\t"))).add(literal("public ")).add(mark("name", "firstUpperCase")).add(literal("Header(")).add(mark("abstractBox", "type")).add(literal(" box) {\n\t\t\tsuper(box);\n\t\t}\n\n\t\t@Override\n\t\tpublic void init() {\n\t\t\tsuper.init();\n\t\t\t")).add(mark("componentReferences")).add(expression().add(literal("\n")).add(literal("\t\t\t")).add(mark("component", "initializations").multiple("\n"))).add(literal("\n\t\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t\t")).add(mark("component", "class").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "method").multiple("\n"))).add(literal("\n\t}\n\n\tpublic class ")).add(mark("name", "firstUpperCase")).add(literal("Tabs")).add(mark("abstractBox", "extension")).add(literal(" extends io.intino.alexandria.ui.displays.components.Tabs<")).add(mark("abstractBox", "type")).add(literal("> {\n\t\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Tabs(")).add(mark("abstractBox", "type")).add(literal(" box) {\n    \t\tsuper(box);\n    \t\t")).add(mark("tabs").multiple("\n")).add(literal("\n    \t}\n\t}\n}")),
			rule().add((condition("type", "templatesImports"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.templates.*;")),
			rule().add((condition("type", "blocksImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.blocks.*;")),
			rule().add((condition("type", "itemsImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.items.*;")),
			rule().add((condition("type", "rowsImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.rows.*;")),
			rule().add((condition("type", "componentReferences & forRoot"))).add(mark("component", "rootReferences").multiple("\n")),
			rule().add((condition("type", "componentReferences"))).add(mark("component", "references").multiple("\n")),
			rule().add((condition("type", "attribute"))).add(literal("public ")).add(mark("clazz")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal(";"))
		);
		return this;
	}
}