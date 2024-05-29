package io.intino.konos.builder.codegeneration;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class BoxTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("box")).output(literal("package ")).output(placeholder("package")).output(literal(";\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Box extends AbstractBox {\n\n\tpublic ")).output(placeholder("name", "PascalCase")).output(literal("Box(String[] args) {\n\t\tthis(new ")).output(placeholder("name", "PascalCase")).output(literal("Configuration(args));\n\t}\n\n\tpublic ")).output(placeholder("name", "PascalCase")).output(literal("Box(")).output(placeholder("name", "PascalCase")).output(literal("Configuration configuration) {\n\t\tsuper(configuration);\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\tsuper.put(o);\n\t\treturn this;\n\t}\n\n\tpublic void beforeStart() {\n\n\t}\n\n\tpublic void afterStart() {\n\n\t}\n\n\tpublic void beforeStop() {\n\n\t}\n\n\tpublic void afterStop() {\n\n\t}\n\n\t")).output(expression().output(placeholder("hasUi", "authService"))).output(literal("\n\n\t")).output(expression().output(placeholder("terminal", "datamartSourceSelector"))).output(literal("\n\n\t")).output(expression().output(placeholder("rest"))).output(literal("\n\t")).output(expression().output(placeholder("authenticationValidator"))).output(literal("\n}")));
		rules.add(rule().condition(trigger("hide")));
		rules.add(rule().condition(trigger("authservice")).output(literal("protected io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}")));
		rules.add(rule().condition(trigger("rest")).output(literal("protected io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl) {\n\t//TODO add your authService\n\treturn null;\n}\t")));
		rules.add(rule().condition(all(allTypes("datamartsLoad"), trigger("datamartsourceselector"))).output(literal("protected String ")).output(placeholder("datamartsLoad")).output(literal("() {\n\treturn null;//TODO add ss selector using SQL syntax\n}")));
		rules.add(rule().condition(trigger("authenticationvalidator")).output(literal("public io.intino.alexandria.http.security.")).output(placeholder("type", "FirstUpperCase")).output(literal("AuthenticationValidator authenticationValidator() {\n\treturn token -> false;\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}