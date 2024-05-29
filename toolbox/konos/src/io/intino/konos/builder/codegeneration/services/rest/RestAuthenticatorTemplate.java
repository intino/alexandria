package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class RestAuthenticatorTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("basic")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\npublic class ")).output(placeholder("service", "pascalCase")).output(literal("Authenticator {\n\tprivate ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("service", "pascalCase")).output(literal("Authenticator(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(String user, String password) {\n\t\treturn false;\n\t}\n}")));
		rules.add(rule().condition(allTypes("bearer")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\npublic class ")).output(placeholder("service", "pascalCase")).output(literal("Authenticator {\n \tprivate ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\n \tpublic ")).output(placeholder("service", "pascalCase")).output(literal("Authenticator(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(String token) {\n\t\treturn false;\n\t}\n}")));
		rules.add(rule().condition(allTypes("custom")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport java.util.Map;\n\npublic class ")).output(placeholder("service", "pascalCase")).output(literal("Authenticator {\n \tprivate ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\n \tpublic ")).output(placeholder("service", "pascalCase")).output(literal("Authenticator(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(Map<String, String> parameters) {\n\t\treturn false;\n\t}\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}