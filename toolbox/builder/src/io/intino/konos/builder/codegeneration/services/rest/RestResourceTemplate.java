package io.intino.konos.builder.codegeneration.services.rest;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RestResourceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("notification"))).output(literal("package ")).output(mark("package")).output(literal(".rest.notifications;\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package")).output(literal(".*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.rest.Resource;\nimport io.intino.alexandria.rest.pushservice.Client;\nimport io.intino.alexandria.rest.pushservice.Session;\nimport io.intino.alexandria.rest.pushservice.MessageCarrier;\nimport io.intino.alexandria.rest.spark.SparkManager;\nimport io.intino.alexandria.rest.spark.SparkNotifier;\nimport io.intino.alexandria.rest.spark.SparkPushService;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Notification implements Resource {\n\n\tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\tprivate SparkManager<SparkPushService> manager;\n\t")).output(expression().output(mark("authenticationValidator", "field"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Notification(")).output(mark("box", "FirstUpperCase")).output(literal("Box box, SparkManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t\t")).output(mark("authenticationValidator", "assign")).output(literal("\n\t}\n\n\tpublic void execute() ")).output(expression().output(literal("throws ")).output(mark("throws").multiple(", "))).output(literal(" {\n\t\t")).output(expression().output(mark("authenticationValidator", "validate"))).output(literal("\n\t\tSession session = manager.currentSession();\n\t\tString clientId = java.util.UUID.randomUUID().toString();\n\t\t")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action action = fill(new ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action());\n\t\tmanager.pushService().onOpen(client -> {\n\t\t\taction.onOpen(client, notifier(session, client));\n\t\t\treturn true;\n\t\t});\n\t\tmanager.pushService().onClose(clientId).execute((java.util.function.Consumer<Client>) action::onClose);\n\t\t")).output(expression().output(mark("returnType", "methodCall"))).output(literal("manager.baseUrl().replace(\"http\", \"ws\") + \"/push?id=\" + clientId")).output(expression().output(mark("returnType", "ending"))).output(literal(";\n\t}\n\n\tprivate ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action fill(")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();\n\t\t")).output(expression().output(mark("parameter", "assign").multiple("\n"))).output(literal("\n\t\treturn action;\n\t}\n\n\t")).output(expression().output(mark("returnType", "method"))).output(literal("\n\n\tprivate SparkNotifier notifier(Session session, Client client) {\n\t\treturn new SparkNotifier(new MessageCarrier(manager.pushService(), session, client));\n\t}\n\n\tprivate io.intino.alexandria.core.Context context() {\n\t\tio.intino.alexandria.core.Context context = new io.intino.alexandria.core.Context();\n\t\tcontext.put(\"domain\", manager.domain());\n\t\tcontext.put(\"baseUrl\", manager.baseUrl());\n\t\tcontext.put(\"requestUrl\", manager.baseUrl() + manager.request().pathInfo());\n\t\t")).output(expression().output(mark("authenticationValidator", "put"))).output(literal("\n\t\treturn context;\n\t}\n}\n\n")),
			rule().condition((type("resource"))).output(literal("package ")).output(mark("package")).output(literal(".rest.resources;\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package")).output(literal(".*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.rest.Resource;\nimport io.intino.alexandria.rest.spark.SparkManager;\nimport io.intino.alexandria.rest.spark.SparkPushService;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Resource implements Resource {\n\n\tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\tprivate SparkManager<SparkPushService> manager;\n\t")).output(expression().output(mark("authenticationValidator", "field"))).output(literal("\n\n\tpublic ")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Resource(")).output(mark("box", "FirstUpperCase")).output(literal("Box box, SparkManager manager) {\n\t\tthis.box = box;\n\t\tthis.manager = manager;\n\t\t")).output(mark("authenticationValidator", "assign")).output(literal("\n\t}\n\n\tpublic void execute() throws ")).output(mark("throws").multiple(", ")).output(literal(" {\n\t\t")).output(expression().output(mark("authenticationValidator", "validate"))).output(literal("\n\t\t")).output(expression().output(mark("returnType", "methodCall"))).output(literal("fill(new ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Action()).execute()")).output(expression().output(mark("returnType", "ending"))).output(literal(";\n\t}\n\n\tprivate ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Action fill(")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("operation", "firstUpperCase")).output(mark("name", "firstUpperCase")).output(literal("Action action) {\n\t\taction.box = this.box;\n\t\taction.context = context();\n\t\t")).output(expression().output(mark("parameter", "assign").multiple("\n"))).output(literal("\n\t\treturn action;\n\t}\n\n\t")).output(expression().output(mark("returnType", "method"))).output(literal("\n\n\tprivate io.intino.alexandria.core.Context context() {\n\t\tio.intino.alexandria.core.Context context = new io.intino.alexandria.core.Context();\n\t\tcontext.put(\"domain\", manager.domain());\n\t\tcontext.put(\"baseUrl\", manager.baseUrl());\n\t\tcontext.put(\"requestUrl\", manager.baseUrl() + manager.request().pathInfo());\n\t\t")).output(mark("authenticationValidator", "put")).output(literal("\n\t\treturn context;\n\t}\n}")),
			rule().condition((attribute("void")), (trigger("methodcall"))),
			rule().condition((type("redirect")), (trigger("methodcall"))).output(literal("redirect(")),
			rule().condition((type("response")), (trigger("methodcall"))).output(literal("write(")),
			rule().condition((attribute("void")), (trigger("ending"))),
			rule().condition((trigger("ending"))).output(literal(")")),
			rule().condition((attribute("void")), (trigger("write"))),
			rule().condition((type("redirect")), (trigger("method"))).output(literal("private void redirect(String url) {\n\tmanager.redirect(url);\n}")),
			rule().condition((type("response")), (trigger("method"))).output(literal("private void write(")).output(mark("value", "firstUpperCase", "ReturnTypeFormatter")).output(literal(" object) {\n\tmanager.write(object")).output(expression().output(literal(", \"")).output(mark("format")).output(literal("\""))).output(literal(");\n}")),
			rule().condition((type("parameter")), (trigger("type"))).output(mark("parameterType")),
			rule().condition((allTypes("parameter","defaultvalue")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = manager.from")).output(mark("in", "firstUpperCase")).output(literal("OrDefault(\"")).output(mark("name")).output(literal("\", ")).output(mark("parameterType")).output(literal(", \"")).output(mark("defaultvalue")).output(literal("\");")),
			rule().condition((type("parameter")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = manager.from")).output(mark("in", "firstUpperCase")).output(literal("(\"")).output(mark("name")).output(literal("\", ")).output(mark("parameterType")).output(literal(");")),
			rule().condition((type("list")), (trigger("parametertype"))).output(literal("com.google.gson.reflect.TypeToken.getParameterized(java.util.ArrayList.class, ")).output(mark("value")).output(literal(".class).getType()")),
			rule().condition((type("authenticationvalidator")), (trigger("field"))).output(literal("io.intino.alexandria.rest.security.")).output(mark("type", "FirstUpperCase")).output(literal("AuthenticationValidator validator;")),
			rule().condition((type("authenticationvalidator")), (trigger("assign"))).output(literal("this.validator = box.authenticationValidator();")),
			rule().condition((type("authenticationvalidator")), (trigger("validate"))).output(literal("String auth = manager.fromHeader(\"Authorization\", String.class);\nif (auth == null || !validator.validate(auth.replace(\"Basic \", \"\"))) throw new Unauthorized(\"Credential not found\");")),
			rule().condition((type("authenticationvalidator")), (trigger("put"))).output(literal("context.put(\"auth\", manager.fromHeader(\"Authorization\", String.class).replace(\"Basic \", \"\"));")),
			rule().condition((trigger("parametertype"))).output(mark("value")).output(literal(".class")),
			rule().condition((type("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}