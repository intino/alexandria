package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RESTServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("server"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.rest.AlexandriaSpark;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".rest.resources.*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.rest.security.DefaultSecurityManager;\nimport io.intino.alexandria.rest.spark.SparkPushService;\n\npublic class ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(mark("<missing ID>")).output(literal("Service {\n\n\tpublic static AlexandriaSpark setup(AlexandriaSpark server, ")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(expression().output(mark("secure"))).output(literal("\n\t\t")).output(expression().output(mark("hasNotifications"))).output(literal("\n\t\t")).output(expression().output(mark("notification").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("resource").multiple("\n"))).output(literal("\n\n\t\treturn server;\n\t}\n}")),
			rule().condition((type("secure"))).output(literal("server.secure(new DefaultSecurityManager(new java.io.File(\"")).output(mark("file")).output(literal("\"), \"")).output(mark("password")).output(literal("\"));")),
			rule().condition((trigger("notification"))).output(literal("server.route(\"")).output(mark("path", "format")).output(literal("\").post(manager -> new ")).output(mark("package")).output(literal(".rest.notifications.")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Notification(box, manager).execute());")),
			rule().condition((type("resource"))).output(literal("server.route(")).output(mark("path", "format")).output(literal(").")).output(mark("method", "firstlowerCase")).output(literal("(manager -> new ")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Resource(box, manager).execute());")),
			rule().condition((type("path")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", box.configuration().get(\"")).output(mark("value")).output(literal("\"))")),
			rule().condition((trigger("hasnotifications"))).output(literal("if (!io.intino.alexandria.rest.AlexandriaSparkBuilder.isUI()) server.route(\"/push\").push(new SparkPushService());"))
		);
	}
}