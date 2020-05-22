package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessagingRequestTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("request"))).output(literal("package ")).output(mark("package")).output(literal(".requests;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package")).output(literal(".*;\nimport com.google.gson.Gson;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.terminal.Connector;\n\nimport javax.jms.*;\nimport java.util.List;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Request {\n\tprivate final ")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\tprivate final Connector connector;\n\tprivate final String responsePath;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Request(")).output(mark("box", "firstUpperCase")).output(literal("Box box, Connector connector, String responsePath) {\n\t\tthis.box = box;\n\t\tthis.connector = connector;\n\t\tthis.responsePath = responsePath;\n\t}\n\n\tpublic void accept(String request) {\n\t\ttry {\n\t\t\t")).output(mark("call")).output(literal("\n\t\t} ")).output(expression().output(mark("exception", "catch"))).output(literal("\n\t\tcatch (Throwable e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);\n\t\t}\n\t}\n\n\tprivate ")).output(mark("package")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action actionFor(")).output(mark("parameter", "signature")).output(literal(") {\n\t\tfinal ")).output(mark("package")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action action = new ")).output(mark("package")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action();\n\t\taction.box = this.box;\n\t\t")).output(expression().output(mark("parameter", "assign"))).output(literal("\n\t\treturn action;\n\t}\n}")),
				rule().condition(not(type("void")), (trigger("call"))).output(literal("connector.sendMessage(responsePath, io.intino.alexandria.Json.toString(actionFor(")).output(expression().output(literal("io.intino.alexandria.Json.fromString(request, ")).output(mark("parameter", "type")).output(literal(".class)"))).output(literal(").execute()));")),
				rule().condition((trigger("call"))).output(literal("actionFor(")).output(expression().output(literal("io.intino.alexandria.Json.fromString(request, ")).output(mark("parameter", "type")).output(literal(".class)"))).output(literal(").execute();")),
				rule().condition((trigger("catch"))).output(literal("catch (AlexandriaException e) {\n\tconnector.sendMessage(responsePath, e.getMessage());\n}")),
				rule().condition((type("parameter")), (trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
				rule().condition((type("parameter")), (trigger("signature"))).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
				rule().condition((type("parameter")), (trigger("type"))).output(mark("type")),
				rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}