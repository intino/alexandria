package io.intino.konos.builder.codegeneration.accessor.messaging;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessagingAccessorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("accessor"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\nimport io.intino.alexandria.exceptions.*;\n\nimport java.util.function.Consumer;\nimport javax.jms.*;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("Accessor {\n\tprivate final io.intino.alexandria.terminal.JmsConnector connector;\n\tprivate final String context;\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("Accessor(io.intino.alexandria.terminal.JmsConnector connector, String context) {\n\t\tthis.connector = connector;\n\t\tthis.context = context;\n\t}\n\n\t")).output(mark("request", "registerCallback").multiple("\n\n")).output(literal("\n\n\t")).output(mark("request").multiple("\n\n")).output(literal("\n\n\tprivate String chainContext() {\n\t\treturn (context != null && !context.isEmpty() ? \".\" + context + \".\" : \".\");\n\t}\n\n}")),
				rule().condition((allTypes("request", "reply")), (trigger("registercallback"))).output(literal("public ")).output(mark("service", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("Accessor register")).output(mark("name", "firstUpperCase")).output(literal("CallbackConsumer(Consumer<")).output(mark("response", "type")).output(literal("> consumer) {\n\tconnector.attachListener(\"response\" + chainContext() + \"")).output(mark("name")).output(literal("\", (r, c) -> consumer.accept(io.intino.alexandria.Json.fromString(r, ")).output(mark("response", "type")).output(literal(".class)));\n\treturn this;\n}")),
				rule().condition((type("request"))).output(literal("public void ")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameter", "signature"))).output(literal(") {\n\tconnector.requestResponse(\"")).output(mark("path")).output(literal("\", io.intino.alexandria.Json.toString(")).output(mark("parameter", "name")).output(literal("), \"response\" + chainContext() + \"")).output(mark("name")).output(literal("\");\n}")),
				rule().condition((type("value")), (trigger("type"))).output(mark("type", "FirstUpperCase")),
				rule().condition((type("value")), (trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
				rule().condition((type("value")), (trigger("name"))).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
				rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}