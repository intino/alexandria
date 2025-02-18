package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewPushRequesterTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.requesters;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("packageType")).output(literal("s."))).output(placeholder("name", "FirstUpperCase")).output(literal(";\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".*;\n")).output(placeholder("schemaImport")).output(literal("\n\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.services.push.UIClient;\nimport io.intino.alexandria.ui.services.push.UIMessage;\nimport io.intino.alexandria.ui.server.AlexandriaUiManager;\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("PushRequester extends io.intino.alexandria.ui.displays.requesters.")).output(expression().output(placeholder("type", "class", "FirstUpperCase"))).output(literal("PushRequester {\n\n\tpublic void execute(UIClient client, UIMessage message) {\n\t\t")).output(placeholder("name", "firstUpperCase")).output(literal(" display = display(client, message);\n\t\tif (display == null) return;\n\t\tString operation = operation(message);\n\t\tString data = data(message);\n\n\t\t")).output(expression().output(placeholder("request").multiple("\nelse "))).output(literal("\n\n\t\tsuper.execute(client, message);\n\t}\n\n}")));
		rules.add(rule().condition(all(attribute("","Display"), trigger("class"))).output(literal("Display")));
		rules.add(rule().condition(all(allTypes("request", "file"), trigger("request"))));
		rules.add(rule().condition(all(allTypes("request", "asset"), trigger("request"))));
		rules.add(rule().condition(all(allTypes("request"), trigger("request"))).output(literal("if (operation.equals(\"")).output(placeholder("name")).output(literal("\")) {\n\tdisplay.")).output(placeholder("name")).output(literal("(")).output(placeholder("parameter")).output(literal(");\n\treturn;\n}")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(literal("java.util.List.of(io.intino.alexandria.Json.fromString(data, ")).output(placeholder("value")).output(literal("[].class))")));
		rules.add(rule().condition(all(allTypes("parameter", "file"), trigger("parameter"))));
		rules.add(rule().condition(all(allTypes("parameter", "String"), trigger("parameter"))).output(literal("data")));
		rules.add(rule().condition(all(allTypes("parameter", "Double"), trigger("parameter"))).output(literal("Double.parseDouble(data)")));
		rules.add(rule().condition(all(allTypes("parameter", "Integer"), trigger("parameter"))).output(literal("Integer.parseInt(data)")));
		rules.add(rule().condition(all(allTypes("parameter", "Long"), trigger("parameter"))).output(literal("Long.parseLong(data)")));
		rules.add(rule().condition(all(allTypes("parameter", "DateTime"), trigger("parameter"))).output(literal("data != null ? java.time.Instant.ofEpochMilli(Long.parseLong(data)) : null")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("parameter"))).output(literal("io.intino.alexandria.Json.fromString(data, ")).output(placeholder("value")).output(literal(".class)")));
		rules.add(rule().condition(trigger("parameter")).output(literal("aaaaa")));
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