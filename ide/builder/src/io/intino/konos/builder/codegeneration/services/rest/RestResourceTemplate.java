package io.intino.konos.builder.codegeneration.services.rest;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class RestResourceTemplate extends Template {

	protected RestResourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new RestResourceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "notification"))).add(literal("package ")).add(mark("package")).add(literal(".rest.notifications;\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.rest.Resource;\nimport io.intino.alexandria.rest.pushservice.Client;\nimport io.intino.alexandria.rest.pushservice.Session;\nimport io.intino.alexandria.rest.pushservice.MessageCarrier;\nimport io.intino.alexandria.rest.spark.SparkManager;\nimport io.intino.alexandria.rest.spark.SparkNotifier;\nimport io.intino.alexandria.rest.spark.SparkPushService;\n\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Notification implements Resource {\n\n\tprivate ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\tprivate SparkManager<SparkPushService> manager;")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("authenticationValidator", "field"))).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Notification(")).add(mark("box", "FirstUpperCase")).add(literal("Box box, SparkManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t\t")).add(mark("authenticationValidator", "assign")).add(literal("\n\t}\n\n\tpublic void execute()")).add(expression().add(literal(" throws ")).add(mark("throws").multiple(", "))).add(literal(" {\n\t\t")).add(expression().add(mark("authenticationValidator", "validate"))).add(literal("\n\t\tSession session = manager.currentSession();\n\t\tString clientId = java.util.UUID.randomUUID().toString();\n\t\t")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = fill(new ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action());\n\t\tmanager.pushService().onOpen(client -> {\n\t\t\taction.onOpen(client, notifier(session, client));\n\t\t\treturn true;\n\t\t});\n\t\tmanager.pushService().onClose(clientId).execute((java.util.function.Consumer<Client>) action::onClose);\n\t\t")).add(expression().add(mark("returnType", "methodCall"))).add(literal("manager.baseUrl().replace(\"http\", \"ws\") + \"/push?id=\" + clientId")).add(expression().add(mark("returnType", "ending"))).add(literal(";\n\t}\n\n\tprivate ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action fill(")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parameter", "assign").multiple("\n"))).add(literal("\n\t\treturn action;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("returnType", "method"))).add(literal("\n\n\tprivate SparkNotifier notifier(Session session, Client client) {\n\t\treturn new SparkNotifier(new MessageCarrier(manager.pushService(), session, client));\n\t}\n\n\tprivate io.intino.alexandria.core.Context context() {\n\t\tio.intino.alexandria.core.Context context = new io.intino.alexandria.core.Context();\n\t\tcontext.put(\"domain\", manager.domain());\n\t\tcontext.put(\"baseUrl\", manager.baseUrl());\n\t\tcontext.put(\"requestUrl\", manager.baseUrl() + manager.request().pathInfo());\n\t\t")).add(expression().add(mark("authenticationValidator", "put"))).add(literal("\n\t\treturn context;\n\t}\n}\n\n")),
				rule().add((condition("type", "resource"))).add(literal("package ")).add(mark("package")).add(literal(".rest.resources;\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.rest.Resource;\nimport io.intino.alexandria.rest.spark.SparkManager;\nimport io.intino.alexandria.rest.spark.SparkPushService;\n\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Resource implements Resource {\n\n\tprivate ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\tprivate SparkManager<SparkPushService> manager;")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("authenticationValidator", "field"))).add(literal("\n\n\tpublic ")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Resource(")).add(mark("box", "FirstUpperCase")).add(literal("Box box, SparkManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t\t")).add(mark("authenticationValidator", "assign")).add(literal("\n\t}\n\n\tpublic void execute() throws ")).add(mark("throws").multiple(", ")).add(literal(" {")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("authenticationValidator", "validate"))).add(literal("\n\t\t")).add(expression().add(mark("returnType", "methodCall"))).add(literal("fill(new ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Action()).execute()")).add(expression().add(mark("returnType", "ending"))).add(literal(";\n\t}\n\n\tprivate ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Action fill(")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("operation", "firstUpperCase")).add(mark("name", "firstUpperCase")).add(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parameter", "assign").multiple("\n"))).add(literal("\n\t\treturn action;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("returnType", "method"))).add(literal("\n\n\tprivate io.intino.alexandria.core.Context context() {\n\t\tio.intino.alexandria.core.Context context = new io.intino.alexandria.core.Context();\n\t\tcontext.put(\"domain\", manager.domain());\n\t\tcontext.put(\"baseUrl\", manager.baseUrl());\n\t\tcontext.put(\"requestUrl\", manager.baseUrl() + manager.request().pathInfo());\n\t\t")).add(mark("authenticationValidator", "put")).add(literal("\n\t\treturn context;\n\t}\n}")),
			rule().add((condition("attribute", "void")), (condition("trigger", "methodCall"))),
			rule().add((condition("type", "redirect")), (condition("trigger", "methodCall"))).add(literal("redirect(")),
			rule().add((condition("type", "response")), (condition("trigger", "methodCall"))).add(literal("write(")),
			rule().add((condition("attribute", "void")), (condition("trigger", "ending"))),
			rule().add((condition("trigger", "ending"))).add(literal(")")),
			rule().add((condition("attribute", "void")), (condition("trigger", "write"))),
			rule().add((condition("type", "redirect")), (condition("trigger", "method"))).add(literal("private void redirect(String url) {\n\tmanager.redirect(url);\n}")),
			rule().add((condition("type", "response")), (condition("trigger", "method"))).add(literal("private void write(")).add(mark("value", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" object) {\n\tmanager.write(object")).add(expression().add(literal(", \"")).add(mark("format")).add(literal("\""))).add(literal(");\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(mark("parameterType")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = manager.from")).add(mark("in", "firstUpperCase")).add(literal("(\"")).add(mark("name")).add(literal("\", ")).add(mark("parameterType")).add(literal(");")),
			rule().add((condition("type", "list")), (condition("trigger", "parameterType"))).add(literal("com.google.gson.reflect.TypeToken.getParameterized(java.util.ArrayList.class, ")).add(mark("value")).add(literal(".class).getType()")),
				rule().add((condition("type", "authenticationValidator")), (condition("trigger", "field"))).add(literal("io.intino.alexandria.rest.security.")).add(mark("type", "FirstUpperCase")).add(literal("AuthenticationValidator validator;")),
			rule().add((condition("type", "authenticationValidator")), (condition("trigger", "assign"))).add(literal("this.validator = box.authenticationValidator();")),
				rule().add((condition("type", "authenticationValidator")), (condition("trigger", "validate"))).add(literal("String auth = manager.fromHeader(\"Authorization\", String.class);\nif (auth == null || !validator.validate(auth.replace(\"Basic \", \"\"))) throw new Unauthorized(\"Credential not found\");")),
				rule().add((condition("type", "authenticationValidator")), (condition("trigger", "put"))).add(literal("context.put(\"auth\", manager.fromHeader(\"Authorization\", String.class).replace(\"Basic \", \"\"));")),
			rule().add((condition("trigger", "parameterType"))).add(mark("value")).add(literal(".class")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}