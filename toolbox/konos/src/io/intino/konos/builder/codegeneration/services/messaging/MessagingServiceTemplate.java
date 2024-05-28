package io.intino.konos.builder.codegeneration.services.messaging;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class MessagingServiceTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("messaging")).output(literal("package ")).output(placeholder("package")).output(literal(";\n\nimport io.intino.alexandria.terminal.Connector;\nimport io.intino.alexandria.logger.Logger;\n\nimport java.io.ByteArrayOutputStream;\nimport java.io.IOException;\nimport java.io.InputStream;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Service {\n\tprivate final ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box;\n\tprivate final ")).output(placeholder("box", "firstUpperCase")).output(literal("Configuration configuration;\n\tprivate final Connector connector;\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Service(Connector connector, ")).output(placeholder("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.configuration = box.configuration();\n\t\tthis.connector = connector;\n\t\t")).output(placeholder("request").multiple("\n")).output(literal("\n\t}\n}")));
		rules.add(rule().condition(allTypes("request")).output(literal("connector.attachListener(")).output(placeholder("path", "format")).output(literal(", (r, c) -> new ")).output(placeholder("package")).output(literal(".requests.")).output(placeholder("name", "firstUpperCase")).output(literal("Request(box, connector, c).accept(r));")));
		rules.add(rule().condition(all(allTypes("path"), trigger("format"))).output(literal("\"")).output(placeholder("name")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))));
		rules.add(rule().condition(trigger("custom")).output(literal(".replace(\"{")).output(placeholder("value")).output(literal("}\", configuration.get(\"")).output(placeholder("value")).output(literal("\"))")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}