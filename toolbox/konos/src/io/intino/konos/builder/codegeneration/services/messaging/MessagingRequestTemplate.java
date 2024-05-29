package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class MessagingRequestTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("request")).output(literal("package ")).output(placeholder("package")).output(literal(".requests;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package")).output(literal(".*;\nimport com.google.gson.Gson;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.terminal.Connector;\n\nimport javax.jms.*;\nimport java.util.List;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Request {\n\tprivate final ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box;\n\tprivate final Connector connector;\n\tprivate final String responsePath;\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Request(")).output(placeholder("box", "firstUpperCase")).output(literal("Box box, Connector connector, String responsePath) {\n\t\tthis.box = box;\n\t\tthis.connector = connector;\n\t\tthis.responsePath = responsePath;\n\t}\n\n\tpublic void accept(String request) {\n\t\ttry {\n\t\t\t")).output(placeholder("call")).output(literal("\n\t\t} ")).output(expression().output(placeholder("exception", "catch"))).output(literal("\n\t\tcatch (Throwable e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);\n\t\t}\n\t}\n\n\tprivate ")).output(placeholder("package")).output(literal(".actions.")).output(placeholder("name", "firstUpperCase")).output(literal("Action actionFor(")).output(placeholder("parameter", "signature")).output(literal(") {\n\t\tfinal ")).output(placeholder("package")).output(literal(".actions.")).output(placeholder("name", "firstUpperCase")).output(literal("Action action = new ")).output(placeholder("package")).output(literal(".actions.")).output(placeholder("name", "firstUpperCase")).output(literal("Action();\n\t\taction.box = this.box;\n\t\t")).output(expression().output(placeholder("parameter", "assign"))).output(literal("\n\t\treturn action;\n\t}\n}")));
		rules.add(rule().condition(all(not(allTypes("void")), trigger("call"))).output(literal("connector.sendMessage(responsePath, io.intino.alexandria.Json.toString(actionFor(")).output(expression().output(literal("io.intino.alexandria.Json.fromString(request, ")).output(placeholder("parameter", "type")).output(literal(".class)"))).output(literal(").execute()));")));
		rules.add(rule().condition(trigger("call")).output(literal("actionFor(")).output(expression().output(literal("io.intino.alexandria.Json.fromString(request, ")).output(placeholder("parameter", "type")).output(literal(".class)"))).output(literal(").execute();")));
		rules.add(rule().condition(trigger("catch")).output(literal("catch (AlexandriaException e) {\n\tconnector.sendMessage(responsePath, e.getMessage());\n}")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("assign"))).output(literal("action.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("signature"))).output(placeholder("type", "FirstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("type"))).output(placeholder("type")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}