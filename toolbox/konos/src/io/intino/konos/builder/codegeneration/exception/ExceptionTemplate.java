package io.intino.konos.builder.codegeneration.exception;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class ExceptionTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("exception")).output(literal("package ")).output(placeholder("package")).output(literal(".exceptions;\n\nimport io.intino.alexandria.exceptions.*;\nimport java.util.Map;\nimport java.util.LinkedHashMap;\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends  io.intino.alexandria.exceptions.")).output(placeholder("code")).output(literal(" {\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("(String message) {\n\t\tsuper(message);\n\t}\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("(String message, Map<String, String> parameters) {\n\t\tsuper(message, parameters);\n\t}\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}