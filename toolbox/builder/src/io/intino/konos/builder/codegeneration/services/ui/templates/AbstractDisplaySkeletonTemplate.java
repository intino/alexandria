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
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays")).add(expression().add(literal(".")).add(mark("packageType")).add(literal("s"))).add(literal(";\n\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).add(mark("package")).add(literal(".ui.*;\n")).add(mark("schemaImport")).add(literal("\n")).add(mark("collectionItemsImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\n")).add(mark("templatesImport")).add(literal("\n")).add(mark("blocksImport")).add(literal("\n")).add(mark("itemsImport")).add(literal("\n")).add(mark("rowsImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\npublic")).add(expression().add(literal(" ")).add(mark("abstract", "lowerCase"))).add(literal(" class ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(mark("parametrized")).add(literal(" ")).add(mark("displayExtends")).add(expression().add(literal(" ")).add(mark("implements"))).add(literal(" {\n\t")).add(expression().add(mark("reference", "declaration").multiple("\n")).add(literal("\n")).add(literal("\t"))).add(expression().add(mark("component", "declarations").multiple("\n")).add(literal("\n")).add(literal("\n")).add(literal("    "))).add(literal("public ")).add(expression().add(mark("abstract"))).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n        super(box);\n        id(\"")).add(mark("id")).add(literal("\");\n    }\n\n    @Override\n\tpublic void init() {\n\t\tsuper.init();")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("reference").multiple("\n"))).add(literal("\n\t\t")).add(mark("componentReferences")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "initializations").multiple("\n"))).add(literal("\n\t}")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("methods")).add(literal("\n")).add(literal("\t"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "class").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "method").multiple("\n"))).add(literal("\n}")),
			rule().add((condition("type", "parametrized & extensionOf"))).add(literal("<DN extends ")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("abstractBox", "extension")).add(literal(">")),
			rule().add((condition("type", "parametrized"))).add(mark("abstractBox", "extensionTagged")),
			rule().add((condition("type", "displayExtends & extensionOf"))).add(literal("extends ")).add(mark("parent", "firstUpperCase")).add(literal("<DN, ")).add(mark("abstractBox", "type")).add(literal(">")),
			rule().add((condition("type", "displayExtends & template"))).add(literal("extends io.intino.alexandria.ui.displays.components.Template<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("modelClass")).add(literal(", ")).add(mark("abstractBox", "type")).add(literal(">")),
			rule().add((condition("type", "displayExtends & item"))).add(literal("extends io.intino.alexandria.ui.displays.components.Item<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("itemClass")).add(literal(", ")).add(mark("abstractBox", "type")).add(literal(">")),
			rule().add((condition("type", "displayExtends & row"))).add(literal("extends io.intino.alexandria.ui.displays.components.Row<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("itemClass")).add(literal(", ")).add(mark("abstractBox", "type")).add(literal(">")),
			rule().add((condition("type", "displayExtends"))).add(literal("extends io.intino.alexandria.ui.displays.")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("name", "firstUpperCase")).add(literal("Notifier, ")).add(mark("abstractBox", "type")).add(literal(">")),
			rule().add((condition("type", "templatesImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.templates.*;")),
			rule().add((condition("type", "blocksImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.blocks.*;")),
			rule().add((condition("type", "itemsImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.items.*;")),
			rule().add((condition("type", "rowsImport"))).add(literal("import ")).add(mark("package", "validPackage")).add(literal(".ui.displays.rows.*;")),
			rule().add((condition("type", "componentReferences & forRoot"))).add(mark("component", "rootReferences").multiple("\n")),
			rule().add((condition("type", "componentReferences"))).add(mark("component", "references").multiple("\n")),
			rule().add((condition("type", "attribute"))).add(literal("public ")).add(mark("clazz")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal(";")),
			rule().add((condition("type", "methods & dynamicloadedcomponent"))).add(literal("@Override\npublic io.intino.alexandria.ui.displays.components.DynamicLoaded.LoadTime loadTime() {\n\treturn io.intino.alexandria.ui.displays.components.DynamicLoaded.LoadTime.valueOf(\"")).add(mark("loadTime")).add(literal("\");\n}\n\n@Override\npublic void showLoading() {\n\tnotifier.refreshLoading(true);\n}\n\n@Override\npublic void hideLoading() {\n\tnotifier.refreshLoading(false);\n}"))
		);
		return this;
	}
}