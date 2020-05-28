package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessagingServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("messaging"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport io.intino.alexandria.terminal.Connector;\nimport io.intino.alexandria.logger.Logger;\n\nimport java.io.ByteArrayOutputStream;\nimport java.io.IOException;\nimport java.io.InputStream;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Service {\n\tprivate final ")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\tprivate final ")).output(mark("box", "firstUpperCase")).output(literal("Configuration configuration;\n\tprivate final Connector connector;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Service(Connector connector, ")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.configuration = box.configuration();\n\t\tthis.connector = connector;\n\t\t")).output(mark("request").multiple("\n")).output(literal("\n\t}\n}")),
				rule().condition((type("request"))).output(literal("connector.attachListener(")).output(mark("path", "format")).output(literal(", (r, c) -> new ")).output(mark("package")).output(literal(".requests.")).output(mark("name", "firstUpperCase")).output(literal("Request(box, connector, c).accept(r));")),
				rule().condition((type("path")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
				rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", configuration.get(\"")).output(mark("value")).output(literal("\"))"))
		);
	}
}