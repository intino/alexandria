package io.intino.konos.builder.codegeneration.services.soap;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SoapServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("server"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".soap.operations.*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.http.security.DefaultSecurityManager;\nimport io.intino.alexandria.http.spark.SparkPushService;\n\npublic class ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(mark("<missing ID>")).output(literal("Service {\n\n\tpublic static void setup(AlexandriaSpark server, ")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(expression().output(mark("secure"))).output(literal("\n\t\t")).output(expression().output(mark("operation").multiple("\n"))).output(literal("\n\t}\n}")),
			rule().condition((type("secure"))).output(literal("server.secure(new DefaultSecurityManager(new java.io.File(\"")).output(mark("file")).output(literal("\"), \"")).output(mark("password")).output(literal("\"));")),
			rule().condition((type("operation"))).output(literal("server.route(")).output(mark("path", "format")).output(literal(").post(manager -> new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Operation(box, manager).execute());")),
			rule().condition((type("path")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", box.configuration().get(\"")).output(mark("value")).output(literal("\"))"))
		);
	}
}