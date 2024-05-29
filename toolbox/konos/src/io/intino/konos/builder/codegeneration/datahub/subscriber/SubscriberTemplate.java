package io.intino.konos.builder.codegeneration.datahub.subscriber;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class SubscriberTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("subscriber")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".subscribers;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "firstUpperCase")).output(literal("Box;\n\npublic class ")).output(placeholder("name", "FirstUpperCase")).output(literal(" implements java.util.function.BiConsumer<")).output(placeholder("type")).output(literal(", String> {\n\tprivate final ")).output(placeholder("box", "validName", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("(")).output(placeholder("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void accept(")).output(placeholder("type")).output(literal(" event, String topic) {\n\n\t}\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}