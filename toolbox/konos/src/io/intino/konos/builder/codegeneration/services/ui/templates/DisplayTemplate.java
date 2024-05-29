package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class DisplayTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal(";\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package")).output(literal(".*;\n")).output(placeholder("schemaImport")).output(literal("\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "firstUpperCase")).output(literal("Box;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays")).output(expression().output(literal(".")).output(placeholder("packageType")).output(literal("s"))).output(literal(".Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(";\n")).output(expression().output(placeholder("accessibleNotifier", "import"))).output(literal("\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends Abstract")).output(placeholder("name", "firstUpperCase")).output(literal("<")).output(expression().output(placeholder("accessibleNotifier")).output(literal(", "))).output(placeholder("box", "firstUpperCase")).output(literal("Box> {\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("box", "firstUpperCase")).output(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t")).output(expression().output(placeholder("request").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(placeholder("parameter", "setter").multiple("\n\n"))).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("accessibleNotifier"), trigger("import"))).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.notifiers.")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier;")));
		rules.add(rule().condition(allTypes("accessibleNotifier")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier")));
		rules.add(rule().condition(allTypes("templatesImport")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.templates.*;")));
		rules.add(rule().condition(allTypes("blocksImport")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.blocks.*;")));
		rules.add(rule().condition(allTypes("itemsImport")).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.items.*;")));
		rules.add(rule().condition(allTypes("request", "asset")).output(literal("public io.intino.alexandria.ui.spark.UIFile ")).output(placeholder("name")).output(literal("(")).output(expression().output(placeholder("parameter")).output(literal(" value"))).output(literal(") {\n\treturn null;\n}")));
		rules.add(rule().condition(allTypes("request")).output(literal("public void ")).output(placeholder("name")).output(literal("(")).output(expression().output(placeholder("parameter")).output(literal(" value"))).output(literal(") {\n\n}")));
		rules.add(rule().condition(trigger("setter")).output(literal("public void ")).output(placeholder("value", "firstLowerCase")).output(literal("(String value) {\n\n}")));
		rules.add(rule().condition(all(all(any(allTypes("dateTime"), allTypes("date")), allTypes("list")), trigger("parameter"))).output(placeholder("value")));
		rules.add(rule().condition(all(any(allTypes("dateTime"), allTypes("date")), trigger("parameter"))).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(placeholder("value")).output(literal("[]")));
		rules.add(rule().condition(trigger("parameter")).output(placeholder("value")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		rules.add(rule().condition(trigger("import")).output(literal("import ")).output(placeholder("package")).output(literal(".ui.displays.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}