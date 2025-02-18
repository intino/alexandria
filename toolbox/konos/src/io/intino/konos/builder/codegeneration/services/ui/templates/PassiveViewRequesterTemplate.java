package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewRequesterTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display", "accessible")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.requesters;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("packageType")).output(literal("s."))).output(placeholder("name", "FirstUppercase")).output(literal(";\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("packageType")).output(literal("s."))).output(placeholder("name", "FirstUpperCase")).output(literal("Proxy;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\nimport io.intino.alexandria.ui.server.AlexandriaUiManager;\n\npublic class ")).output(placeholder("name", "FirstUpperCase")).output(literal("ProxyRequester extends io.intino.alexandria.ui.displays.requesters.DisplayProxyRequester {\n\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("ProxyRequester(AlexandriaUiManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tString operation = operation();\n\t\t")).output(placeholder("name", "FirstUppercase")).output(literal(" display = personifiedDisplay();\n\t\tif (display == null) return;\n\t\tif (operation.equals(\"refreshPersonifiedDisplay\")) {\n\t\t\t")).output(placeholder("parameter").multiple("\n")).output(literal("\n\t\t\tdisplay.refresh();\n\t\t\treturn;\n\t\t}\n\t\t")).output(expression().output(literal("else ")).output(placeholder("request").multiple("\nelse "))).output(literal("\n\t\tsuper.execute();\n\t}\n}")));
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.requesters;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("packageType")).output(literal("s."))).output(placeholder("name", "FirstUpperCase")).output(literal(";\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".*;\n")).output(placeholder("schemaImport")).output(literal("\n\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\nimport io.intino.alexandria.ui.server.AlexandriaUiManager;\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester extends ")).output(placeholder("parentType")).output(literal(" {\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Requester(AlexandriaUiManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\t")).output(placeholder("name", "firstUpperCase")).output(literal(" display = display();\n\t\tif (display == null) return;\n\t\tString operation = operation();\n\n\t\t")).output(expression().output(placeholder("request").multiple("\nelse "))).output(literal("\n\n\t\tsuper.execute();\n\t}\n}")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("parenttype"))).output(placeholder("parent", "firstUpperCase")).output(literal("Requester")));
		rules.add(rule().condition(trigger("parenttype")).output(literal("io.intino.alexandria.ui.displays.requesters.")).output(expression().output(placeholder("type", "class", "FirstUpperCase"))).output(literal("Requester")));
		rules.add(rule().condition(all(attribute("","Display"), trigger("class"))).output(literal("Display")));
		rules.add(rule().condition(all(allTypes("request", "asset"), trigger("request"))).output(literal("if (operation.equals(\"")).output(placeholder("name")).output(literal("\")) {\n\tio.intino.alexandria.ui.server.UIFile file = display.")).output(placeholder("name")).output(literal("(")).output(placeholder("parameter")).output(literal(");\n\tif (file == null) return;\n\tmanager.write(file.content(), file.label(), file.embedded());\n\treturn;\n}")));
		rules.add(rule().condition(all(allTypes("request"), trigger("request"))).output(literal("if (operation.equals(\"")).output(placeholder("name")).output(literal("\")) {\n\tdisplay.")).output(placeholder("name")).output(literal("(")).output(placeholder("parameter")).output(literal(");\n\treturn;\n}")));
		rules.add(rule().condition(all(allTypes("parameter", "accessible"), trigger("parameter"))).output(literal("display.")).output(placeholder("value", "firstLowercase")).output(literal("(manager.fromQuery(\"")).output(placeholder("value")).output(literal("\"));")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(literal("java.util.List.of(io.intino.alexandria.Json.fromString(manager.fromQuery(\"v\"), ")).output(placeholder("value")).output(literal("[].class))")));
		rules.add(rule().condition(all(allTypes("parameter", "file"), trigger("parameter"))).output(literal("manager.fromFormAsResource(\"v\")")));
		rules.add(rule().condition(all(allTypes("parameter", "String"), trigger("parameter"))).output(literal("manager.fromQuery(\"v\")")));
		rules.add(rule().condition(all(allTypes("parameter", "Double"), trigger("parameter"))).output(literal("Double.parseDouble(manager.fromQuery(\"v\"))")));
		rules.add(rule().condition(all(allTypes("parameter", "Integer"), trigger("parameter"))).output(literal("Integer.parseInt(manager.fromQuery(\"v\"))")));
		rules.add(rule().condition(all(allTypes("parameter", "Long"), trigger("parameter"))).output(literal("Long.parseLong(manager.fromQuery(\"v\"))")));
		rules.add(rule().condition(all(allTypes("parameter", "DateTime"), trigger("parameter"))).output(literal("manager.fromQuery(\"v\") != null ? java.time.Instant.ofEpochMilli(Long.parseLong(manager.fromQuery(\"v\"))) : null")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("parameter"))).output(literal("io.intino.alexandria.Json.fromString(manager.fromQuery(\"v\"), ")).output(placeholder("value")).output(literal(".class)")));
		rules.add(rule().condition(trigger("parameter")).output(literal("aaaaa ")).output(placeholder("value")));
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