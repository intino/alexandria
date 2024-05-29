package io.intino.konos.builder.codegeneration.services.soap;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class SoapServiceTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("server")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".soap.operations.*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.http.security.DefaultSecurityManager;\nimport io.intino.alexandria.http.spark.SparkPushService;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("Service {\n\n\tpublic static void setup(AlexandriaSpark server, ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(expression().output(placeholder("secure"))).output(literal("\n\t\t")).output(expression().output(placeholder("operation").multiple("\n"))).output(literal("\n\t}\n}")));
		rules.add(rule().condition(allTypes("secure")).output(literal("server.secure(new DefaultSecurityManager(new java.io.File(\"")).output(placeholder("file")).output(literal("\"), \"")).output(placeholder("password")).output(literal("\"));")));
		rules.add(rule().condition(allTypes("operation")).output(literal("server.route(")).output(placeholder("path", "format")).output(literal(").post(manager -> new ")).output(placeholder("name", "PascalCase")).output(literal("Operation(box, manager).execute());")));
		rules.add(rule().condition(all(allTypes("path"), trigger("format"))).output(literal("\"")).output(placeholder("name")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))));
		rules.add(rule().condition(trigger("custom")).output(literal(".replace(\"{")).output(placeholder("value")).output(literal("}\", box.configuration().get(\"")).output(placeholder("value")).output(literal("\"))")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}