package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class ApiPortalConfigurationTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("api")).output(literal("{\n  \"urls\": [\n\t")).output(placeholder("url").multiple(",\n")).output(literal("\n  ],\n  \"title\": \"")).output(placeholder("title")).output(literal("\",\n  \"subtitle\": \"")).output(placeholder("subtitle")).output(literal("\",\n  \"background\": \"")).output(placeholder("background")).output(literal("\",\n  \"color\": \"")).output(placeholder("color")).output(literal("\",\n  \"selectorBorderColor\": \"\"\n}")));
		rules.add(rule().condition(trigger("url")).output(literal("{\n  \"url\": \"./data/")).output(placeholder("")).output(literal(".json\",\n  \"name\": \"")).output(placeholder("")).output(literal("\"\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}