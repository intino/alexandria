package io.intino.konos.builder.codegeneration;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class BoxTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("box"))).output(literal("package ")).output(mark("package")).output(literal(";\n\npublic class ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box extends AbstractBox {\n\n\tpublic ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box(String[] args) {\n\t\tthis(new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Configuration(args));\n\t}\n\n\tpublic ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box(")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Configuration configuration) {\n\t\tsuper(configuration);\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\tsuper.put(o);\n\t\treturn this;\n\t}\n\n\tpublic void beforeStart() {\n\n\t}\n\n\tpublic void afterStart() {\n\n\t}\n\n\tpublic void beforeStop() {\n\n\t}\n\n\tpublic void afterStop() {\n\n\t}\n\n\t")).output(expression().output(mark("hasUi", "authService"))).output(literal("\n\n\t")).output(expression().output(mark("terminal", "datamartSourceSelector"))).output(literal("\n\n\t")).output(expression().output(mark("rest"))).output(literal("\n\t")).output(expression().output(mark("authenticationValidator"))).output(literal("\n}")),
			rule().condition((trigger("hide"))),
			rule().condition((trigger("authservice"))).output(literal("protected io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}")),
			rule().condition((trigger("rest"))).output(literal("protected io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t")),
			rule().condition((type("datamartsLoad")), (trigger("datamartsourceselector"))).output(literal("protected String ")).output(mark("datamartsLoad")).output(literal("() {\n\treturn null;//TODO add ss selector using SQL syntax\n}")),
			rule().condition((trigger("authenticationvalidator"))).output(literal("public io.intino.alexandria.http.security.")).output(mark("type", "FirstUpperCase")).output(literal("AuthenticationValidator authenticationValidator() {\n\treturn token -> false;\n}"))
		);
	}
}