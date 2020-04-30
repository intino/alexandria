package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RestAuthenticatorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("basic"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\npublic class ")).output(mark("service", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Authenticator {\n\tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("service", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Authenticator(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(String user, String password) {\n\t\treturn false;\n\t}\n}")),
			rule().condition((type("bearer"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\npublic class ")).output(mark("service", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Authenticator {\n \tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n \tpublic ")).output(mark("service", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Authenticator(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(String token) {\n\t\treturn false;\n\t}\n}"))
		);
	}
}