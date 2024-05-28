package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AbstractDesktopSkeletonTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("desktop")).output(literal("package ")).output(placeholder("package")).output(literal(".ui.displays.desktops;\n\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.ui.displays.components.*;\nimport ")).output(placeholder("package")).output(literal(".ui.*;\n")).output(placeholder("schemaImport")).output(literal("\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "firstUpperCase")).output(literal("Box;")).output(expression().output(literal("\n")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.desktops.")).output(placeholder("abstract")).output(placeholder("name", "firstUpperCase")).output(literal(";"))).output(literal("\n\n")).output(placeholder("templatesImport")).output(literal("\n")).output(placeholder("blocksImport")).output(literal("\n")).output(placeholder("itemsImport")).output(literal("\n")).output(placeholder("rowsImport")).output(literal("\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.notifiers.")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier;\n\npublic class ")).output(expression().output(placeholder("abstract"))).output(placeholder("name", "firstUpperCase")).output(placeholder("parametrized")).output(literal(" extends io.intino.alexandria.ui.displays.")).output(placeholder("type", "firstUpperCase")).output(literal("<")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier, ")).output(placeholder("abstractBox", "type")).output(literal("> {\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Header header;\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Tabs tabs;\n\n\tpublic ")).output(expression().output(placeholder("abstract"))).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\tid(\"")).output(placeholder("id")).output(literal("\");\n\t}\n\n\t@Override\n\tpublic void init() {\n\t\tsuper.init();\n\t\theader = register(new ")).output(placeholder("name", "firstUpperCase")).output(literal("Header<>(box()).id(\"")).output(placeholder("headerId")).output(literal("\"));\n\t\ttabs = register(new ")).output(placeholder("name", "firstUpperCase")).output(literal("Tabs<>(box()).id(\"")).output(placeholder("tabBarId")).output(literal("\"));\n\t}\n\n\tpublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Header")).output(expression().output(literal("<")).output(placeholder("abstractBox", "extension")).output(literal(">"))).output(literal(" extends io.intino.alexandria.ui.displays.components.Header<")).output(placeholder("abstractBox", "type")).output(literal("> {\n\t\t")).output(expression().output(placeholder("attribute").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("component", "declarations").multiple("\n"))).output(literal("\n\n\t\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Header(")).output(placeholder("abstractBox", "type")).output(literal(" box) {\n\t\t\tsuper(box);\n\t\t}\n\n\t\t@Override\n\t\tpublic void init() {\n\t\t\tsuper.init();\n\t\t\t")).output(placeholder("componentReferences")).output(literal("\n\t\t\t")).output(expression().output(placeholder("component", "initializations").multiple("\n"))).output(literal("\n\t\t}\n\n\t\t@Override\n\t\tpublic void remove() {\n\t\t\tsuper.remove();\n\t\t\t")).output(expression().output(placeholder("component", "unregister").multiple("\n"))).output(literal("\n\t\t}\n\t\t")).output(expression().output(placeholder("component", "class").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("component", "method").multiple("\n"))).output(literal("\n\t}\n\n\tpublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Tabs")).output(expression().output(literal("<")).output(placeholder("abstractBox", "extension")).output(literal(">"))).output(literal(" extends io.intino.alexandria.ui.displays.components.Tabs<")).output(placeholder("abstractBox", "type")).output(literal("> {\n\t\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Tabs(")).output(placeholder("abstractBox", "type")).output(literal(" box) {\n\t\t\tsuper(box);\n\t\t\t")).output(placeholder("tabs").multiple("\n")).output(literal("\n\t\t}\n\t}\n}")));
		rules.add(rule().condition(allTypes("templatesImports")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.templates.*;")));
		rules.add(rule().condition(allTypes("blocksImport")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.blocks.*;")));
		rules.add(rule().condition(allTypes("itemsImport")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.items.*;")));
		rules.add(rule().condition(allTypes("rowsImport")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.rows.*;")));
		rules.add(rule().condition(allTypes("componentReferences", "forRoot")).output(placeholder("component", "rootReferences").multiple("\n")));
		rules.add(rule().condition(allTypes("componentReferences")).output(placeholder("component", "references").multiple("\n")));
		rules.add(rule().condition(allTypes("attribute")).output(literal("public ")).output(placeholder("clazz")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal(";")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}