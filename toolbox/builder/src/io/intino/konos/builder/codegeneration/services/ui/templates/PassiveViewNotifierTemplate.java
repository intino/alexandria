package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewNotifierTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("display","accessible"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.displays.notifiers;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".*;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal("ProxyNotifier extends io.intino.alexandria.ui.displays.notifiers.ProxyDisplayNotifier  {\n    public ")).output(mark("name", "FirstUpperCase")).output(literal("ProxyNotifier(io.intino.alexandria.ui.displays.Display display, io.intino.alexandria.http.pushservice.MessageCarrier carrier) {\n        super(display, carrier);\n    }\n}")),
			rule().condition((type("display"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.displays.notifiers;\n\nimport io.intino.alexandria.exceptions.*;\nimport ")).output(mark("package", "validPackage")).output(literal(".*;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal("Notifier extends ")).output(mark("parentType")).output(literal(" {\n\n    public ")).output(mark("name", "FirstUpperCase")).output(literal("Notifier(io.intino.alexandria.ui.displays.Display display, io.intino.alexandria.http.pushservice.MessageCarrier carrier) {\n        super(display, carrier);\n    }\n\n\t")).output(expression().output(mark("notification").multiple("\n\n"))).output(literal("\n\n}")),
			rule().condition((attribute("extensionof")), (trigger("parenttype"))).output(mark("parent", "firstUpperCase")).output(literal("Notifier")),
			rule().condition((trigger("parenttype"))).output(literal("io.intino.alexandria.ui.displays.notifiers.")).output(mark("value", "FirstUpperCase")).output(literal("Notifier")),
			rule().condition((type("notification")), (trigger("notification"))).output(literal("public void ")).output(mark("name", "firstLowercase")).output(literal("(")).output(expression().output(mark("parameter")).output(literal(" value"))).output(literal(") {\n\tput")).output(expression().output(mark("target"))).output(literal("(\"")).output(mark("name", "firstLowercase")).output(literal("\"")).output(expression().output(literal(", \"v\", ")).output(mark("parameter", "call"))).output(literal(");\n}")),
			rule().condition((type("parameter")), (trigger("call"))).output(literal("value")),
			rule().condition((type("list")), (trigger("parameter"))).output(literal("java.util.List<")).output(mark("value")).output(literal(">")),
			rule().condition((trigger("parameter"))).output(mark("value")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((attribute("", "All")), (trigger("target"))).output(literal("ToAll")),
			rule().condition((attribute("", "Display")), (trigger("target"))).output(literal("ToDisplay")),
			rule().condition((attribute("", "Client")), (trigger("target")))
		);
	}
}