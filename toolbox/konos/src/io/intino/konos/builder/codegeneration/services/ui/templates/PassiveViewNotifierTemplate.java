package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewNotifierTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display", "accessible")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.notifiers;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".*;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "FirstUpperCase")).output(literal("ProxyNotifier extends io.intino.alexandria.ui.displays.notifiers.ProxyDisplayNotifier  {\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("ProxyNotifier(io.intino.alexandria.ui.displays.Display display, io.intino.alexandria.http.pushservice.MessageCarrier carrier) {\n\t\tsuper(display, carrier);\n\t}\n}")));
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.notifiers;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".*;\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "FirstUpperCase")).output(literal("Notifier extends ")).output(placeholder("parentType")).output(literal(" {\n\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("Notifier(io.intino.alexandria.ui.displays.Display display, io.intino.alexandria.http.pushservice.MessageCarrier carrier) {\n\t\tsuper(display, carrier);\n\t}\n\n\t")).output(expression().output(placeholder("notification").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(placeholder("event").multiple("\n\n"))).output(literal("\n}")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("parenttype"))).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier")));
		rules.add(rule().condition(trigger("parenttype")).output(literal("io.intino.alexandria.ui.displays.notifiers.")).output(placeholder("value", "FirstUpperCase")).output(literal("Notifier")));
		rules.add(rule().condition(all(allTypes("notification"), trigger("notification"))).output(literal("public void ")).output(placeholder("name", "firstLowercase")).output(literal("(")).output(expression().output(placeholder("parameter")).output(literal(" value"))).output(literal(") {\n\tput")).output(expression().output(placeholder("target"))).output(literal("(\"")).output(placeholder("name", "firstLowercase")).output(literal("\"")).output(expression().output(literal(", \"v\", ")).output(placeholder("parameter", "call"))).output(literal(");\n}")));
		rules.add(rule().condition(allTypes("event")).output(literal("public void ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\tputToDisplay(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\");\n}")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("call"))).output(literal("value")));
		rules.add(rule().condition(all(allTypes("list"), trigger("parameter"))).output(literal("java.util.List<")).output(placeholder("value")).output(literal(">")));
		rules.add(rule().condition(trigger("parameter")).output(placeholder("value")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		rules.add(rule().condition(all(attribute("", "All"), trigger("target"))).output(literal("ToAll")));
		rules.add(rule().condition(all(attribute("", "Display"), trigger("target"))).output(literal("ToDisplay")));
		rules.add(rule().condition(all(attribute("", "Client"), trigger("target"))));
		rules.add(rule().condition(allTypes("proxyMessage")).output(literal("public void notifyProxyMessage(String name) {\n\tputToDisplay(\"notifyProxyMessage\", \"v\", name);\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}