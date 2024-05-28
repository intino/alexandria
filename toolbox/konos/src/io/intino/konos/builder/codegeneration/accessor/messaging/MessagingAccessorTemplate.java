package io.intino.konos.builder.codegeneration.accessor.messaging;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class MessagingAccessorTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("accessor")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(";\n\nimport io.intino.alexandria.exceptions.*;\n\nimport java.util.function.Consumer;\nimport javax.jms.*;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Accessor {\n\tprivate final io.intino.alexandria.terminal.JmsConnector connector;\n\tprivate final String context;\n\n\tpublic ")).output(placeholder("name", "PascalCase")).output(literal("Accessor(io.intino.alexandria.terminal.JmsConnector connector, String context) {\n\t\tthis.connector = connector;\n\t\tthis.context = context;\n\t}\n\n\t")).output(placeholder("request", "registerCallback").multiple("\n\n")).output(literal("\n\n\t")).output(placeholder("request").multiple("\n\n")).output(literal("\n\n\tprivate String chainContext() {\n\t\treturn (context != null && !context.isEmpty() ? \".\" + context + \".\" : \".\");\n\t}\n\n}")));
		rules.add(rule().condition(all(allTypes("request", "reply"), trigger("registercallback"))).output(literal("public ")).output(placeholder("service", "PascalCase")).output(literal("Accessor register")).output(placeholder("name", "firstUpperCase")).output(literal("CallbackConsumer(Consumer<")).output(placeholder("response", "type")).output(literal("> consumer) {\n\tconnector.attachListener(\"response\" + chainContext() + \"")).output(placeholder("name")).output(literal("\", (r, c) -> consumer.accept(io.intino.alexandria.Json.fromString(r, ")).output(placeholder("response", "type")).output(literal(".class)));\n\treturn this;\n}")));
		rules.add(rule().condition(allTypes("request")).output(literal("public void ")).output(placeholder("name")).output(literal("(")).output(expression().output(placeholder("parameter", "signature"))).output(literal(") {\n\tconnector.requestResponse(\"")).output(placeholder("path")).output(literal("\", io.intino.alexandria.Json.toString(")).output(placeholder("parameter", "name")).output(literal("), \"response\" + chainContext() + \"")).output(placeholder("name")).output(literal("\");\n}")));
		rules.add(rule().condition(all(allTypes("value"), trigger("type"))).output(placeholder("type", "FirstUpperCase")));
		rules.add(rule().condition(all(allTypes("value"), trigger("signature"))).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "CamelCase")));
		rules.add(rule().condition(all(allTypes("value"), trigger("name"))).output(placeholder("name", "CamelCase")));
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