package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.*;

public class FutureTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("future")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".agenda;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(placeholder("name", "FirstUpperCase")).output(literal(" {\n\tprivate final ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("(")).output(placeholder("box", "PascalCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t")).output(placeholder("option").multiple("\n\n")).output(literal("\n\n\t@Override\n\tprotected void onTimeout(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\n\t}\n}")));
		rules.add(rule().condition(trigger("option")).output(literal("@Override\nprotected void on")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(expression().output(literal(", ")).output(placeholder("optionParameter", "signature").multiple(", "))).output(literal(") {\n\n}")));
		rules.add(rule().condition(trigger("signature")).output(placeholder("type")).output(literal(" ")).output(placeholder("name")));
		rules.add(rule().condition(trigger("names")).output(placeholder("name")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}