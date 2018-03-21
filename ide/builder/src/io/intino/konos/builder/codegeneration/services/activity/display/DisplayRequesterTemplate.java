package io.intino.konos.builder.codegeneration.services.activity.display;

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
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".displays.requesters;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.")).add(mark("name", "FirstUpperCase")).add(literal(";\nimport io.intino.konos.alexandria.exceptions.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\n\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\nimport io.intino.konos.alexandria.activity.displays.requesters.Alexandria")).add(expression().add(mark("type", "class", "FirstUpperCase"))).add(literal("Requester;\nimport io.intino.konos.alexandria.exceptions.AlexandriaException;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;\nimport io.intino.konos.alexandria.activity.spark.ActivitySparkManager;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Requester extends ")).add(expression().add(literal("Alexandria")).add(mark("type", "class", "FirstUpperCase"))).add(literal("Requester {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Requester(ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal(" display = display();\n\t\tif (display == null) return;\n\t\tString operation = operation();\n\n\t\t")).add(mark("request").multiple("\nelse ")).add(literal("\n\t\tsuper.execute();\n\t}\n}")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "class"))).add(literal("Display")),
			rule().add((condition("type", "request & asset")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) {\n\tio.intino.konos.alexandria.activity.spark.ActivityFile file = display.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");\n\tmanager.write(file.content(), file.label(), file.embedded());\n\treturn;\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) display.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");")),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(literal("manager.fromQuery(\"value\", ")).add(mark("value")).add(literal("[].class)")),
			rule().add((condition("type", "parameter & file")), (condition("trigger", "parameter"))).add(literal("manager.fromForm(\"value\", ")).add(mark("value")).add(literal(".class)")),
			rule().add((condition("type", "parameter")), (condition("trigger", "parameter"))).add(literal("manager.fromQuery(\"value\", ")).add(mark("value")).add(literal(".class)")),
			rule().add((condition("trigger", "parameter"))),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}