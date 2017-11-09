package io.intino.konos.builder.codegeneration.services.activity.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayNotifierTemplate extends Template {

	protected DisplayNotifierTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayNotifierTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers;\n\nimport io.intino.konos.alexandria.exceptions.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {\n\n    public ")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {\n        super(display, carrier);\n    }\n\n\t")).add(mark("notification").multiple("\n\n")).add(literal("\n}\n")),
			rule().add((condition("type", "notification")), (condition("trigger", "notification"))).add(literal("public void ")).add(mark("name", "firstLowercase")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n\tput")).add(expression().add(mark("target"))).add(literal("(\"")).add(mark("name", "firstLowercase")).add(literal("\"")).add(expression().add(literal(", \"value\", ")).add(mark("parameter", "empty")).add(literal("value"))).add(literal(");\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "empty"))),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(literal("java.util.List<")).add(mark("value")).add(literal(">")),
			rule().add((condition("trigger", "parameter"))).add(mark("value")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("attribute", "All")), (condition("trigger", "target"))).add(literal("ToAll")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "target"))).add(literal("ToDisplay")),
			rule().add((condition("attribute", "Client")), (condition("trigger", "target")))
		);
		return this;
	}
}