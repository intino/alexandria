package io.intino.konos.builder.codegeneration.exception;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ExceptionTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("exception"))).output(literal("package ")).output(mark("package")).output(literal(".exceptions;\n\nimport io.intino.alexandria.exceptions.*;\nimport java.util.Map;\nimport java.util.LinkedHashMap;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends  io.intino.alexandria.exceptions.")).output(mark("code")).output(literal(" {\n\n    public ")).output(mark("name", "firstUpperCase")).output(literal("(String message) {\n        super(message);\n    }\n\n    public ")).output(mark("name", "firstUpperCase")).output(literal("(String message, Map<String, String> parameters) {\n\t\tsuper(message, parameters);\n\t}\n}"))
		);
	}
}