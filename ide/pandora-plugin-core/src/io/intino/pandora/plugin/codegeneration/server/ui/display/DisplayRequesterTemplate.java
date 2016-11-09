package io.intino.pandora.plugin.codegeneration.server.ui.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayRequesterTemplate extends Template {

	protected DisplayRequesterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayRequesterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "notifier"))).add(literal("package ")).add(mark("package")).add(literal(".displays.notifiers;\n\nimport io.intino.pandora.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\nimport io.intino.pandora.server.activity.displays.Display;\nimport io.intino.pandora.server.activity.displays.DisplayNotifier;\nimport io.intino.pandora.server.activity.displays.MessageCarrier;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier extends DisplayNotifier {\n\n    public ")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier(Display display, MessageCarrier carrier) {\n        super(display, carrier);\n    }\n\n\n\t")).add(mark("notification").multiple("\n\n")).add(literal("\n}\n")),
			rule().add((condition("type", "notification")), (condition("trigger", "notification"))).add(literal("public void ")).add(mark("name", "firstLowercase")).add(literal("(")).add(mark("parameter", "signature")).add(literal(") {\n\tputToOwner(\"")).add(mark("name", "firstLowercase")).add(literal("\"")).add(expression().add(literal(", \"")).add(mark("parameter", "name")).add(literal("\", ")).add(mark("parameter", "name"))).add(literal(");\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("parameterType")).add(literal(" ")).add(mark("name", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = manager.from")).add(mark("in", "firstUpperCase")).add(literal("(\"")).add(mark("name")).add(literal("\", ")).add(mark("parameterType")).add(literal(".class);")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}