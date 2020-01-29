package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class IMounterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("factory"))).output(literal("package ")).output(mark("package")).output(literal(".mounters;\n\nimport io.intino.alexandria.event.Event;\n\npublic interface Mounter {\n\tvoid handle(Event event);\n}"))
		);
	}
}