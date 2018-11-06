package io.intino.konos.builder.codegeneration.services.rest;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class RESTServiceTemplate extends Template {

	protected RESTServiceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RESTServiceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "server"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport io.intino.alexandria.rest.AlexandriaSpark;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".rest.resources.*;\nimport io.intino.alexandria.Box;\nimport io.intino.alexandria.rest.security.DefaultSecurityManager;\nimport io.intino.alexandria.rest.spark.SparkPushService;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(mark("<missing ID>")).add(literal("Service {\n\n\tpublic static AlexandriaSpark setup(AlexandriaSpark server, ")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\t")).add(mark("secure")).add(literal("\n\t\t")).add(mark("hasNotifications")).add(literal("\n\t\t")).add(mark("notification").multiple("\n")).add(literal("\n\t\t")).add(mark("resource").multiple("\n")).add(literal("\n\n\t\treturn server;\n\t}\n}")),
			rule().add((condition("type", "secure"))).add(literal("server.secure(new DefaultSecurityManager(new java.io.File(\"")).add(mark("file")).add(literal("\"), \"")).add(mark("password")).add(literal("\"));")),
			rule().add((condition("trigger", "notification"))).add(literal("server.route(\"")).add(mark("path", "format")).add(literal("\").post(manager -> new ")).add(mark("package")).add(literal(".rest.notifications.")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Notification(box, manager).execute());")),
			rule().add((condition("type", "resource"))).add(literal("server.route(")).add(mark("path", "format")).add(literal(").")).add(mark("method", "firstlowerCase")).add(literal("(manager -> new ")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Resource(box, manager).execute());")),
			rule().add((condition("type", "path")), (condition("trigger", "format"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", box.configuration().get(\"")).add(mark("value")).add(literal("\"))")),
			rule().add((condition("trigger", "hasNotifications"))).add(literal("if (!io.intino.alexandria.rest.AlexandriaSparkBuilder.isUI()) server.route(\"/push\").push(new SparkPushService());"))
		);
		return this;
	}
}