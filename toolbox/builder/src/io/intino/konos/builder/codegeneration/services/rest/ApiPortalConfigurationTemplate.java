package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ApiPortalConfigurationTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("api"))).output(literal("{\n  \"urls\": [\n    ")).output(mark("url").multiple(",\n")).output(literal("\n  ],\n  \"title\": \"")).output(mark("title")).output(literal("\",\n  \"subtitle\": \"")).output(mark("subtitle")).output(literal("\",\n  \"background\": \"")).output(mark("background")).output(literal("\",\n  \"color\": \"")).output(mark("color")).output(literal("\",\n  \"selectorBorderColor\": \"\"\n}")),
				rule().condition((trigger("url"))).output(literal("{\n  \"url\": \"./data/")).output(mark("value")).output(literal(".json\",\n  \"name\": \"")).output(mark("value")).output(literal("\"\n}"))
		);
	}
}