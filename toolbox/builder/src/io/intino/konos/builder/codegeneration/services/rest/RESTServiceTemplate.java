package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RESTServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("server"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".rest.resources.*;\nimport io.intino.alexandria.core.Box;\n\npublic class ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Service {\n\n\t")).output(expression().output(mark("authenticator", "field"))).output(literal("\n\n\tpublic static AlexandriaSpark setup(AlexandriaSpark server, ")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(expression().output(mark("authenticator", "assign"))).output(literal("\n\t\t")).output(expression().output(mark("authenticationWithCertificate"))).output(literal("\n\t\t")).output(expression().output(mark("hasNotifications"))).output(literal("\n\t\t")).output(expression().output(mark("notification").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("resource").multiple("\n"))).output(literal("\n\n\t\treturn server;\n\t}\n}")),
			rule().condition((type("authenticator")), (trigger("assign"))).output(literal("authenticator = new ")).output(mark("service", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("ServiceAuthenticator(box);")),
			rule().condition((type("authenticator")), (trigger("field"))).output(literal("private static ")).output(mark("service", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("ServiceAuthenticator authenticator;")),
			rule().condition((type("authenticationWithCertificate"))).output(literal("server.secure(new io.intino.alexandria.http.security.DefaultSecurityManager(new java.io.File(\"")).output(mark("file")).output(literal("\"), \"")).output(mark("password")).output(literal("\"));")),
			rule().condition((trigger("notification"))).output(literal("server.route(\"")).output(mark("path", "format")).output(literal("\").post(manager -> new ")).output(mark("package")).output(literal(".rest.notifications.")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Notification(box, manager).execute());")),
			rule().condition((type("resource"))).output(literal("server.route(")).output(mark("path", "format")).output(literal(")")).output(expression().output(mark("authenticate"))).output(literal(".")).output(mark("method", "firstlowerCase")).output(literal("(manager -> new ")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Resource(box, manager).execute());")),
			rule().condition((type("bearer")), (trigger("authenticate"))).output(literal(".before(manager -> { if (!authenticator.isAuthenticated( manager.fromHeader(\"Authorization\").replace(\"Bearer \", \"\"))) throw new io.intino.alexandria.exceptions.Forbidden(\"Credential not found\");})")),
			rule().condition((type("basic")), (trigger("authenticate"))).output(literal(".before(manager -> { if (!authenticator.isAuthenticated( manager.fromHeader(\"Authorization\").replace(\"Basic \", \"\"))) throw new io.intino.alexandria.exceptions.Forbidden(\"Credential not found\");})")),
			rule().condition((type("path")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", box.configuration().get(\"")).output(mark("value")).output(literal("\"))")),
			rule().condition((trigger("hasnotifications"))).output(literal("if (!io.intino.alexandria.http.AlexandriaSparkBuilder.isUI()) server.route(\"/push\").push(new io.intino.alexandria.http.spark.SparkPushService());"))
		);
	}
}