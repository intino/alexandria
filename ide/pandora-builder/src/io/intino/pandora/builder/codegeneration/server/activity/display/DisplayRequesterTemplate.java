package io.intino.pandora.builder.codegeneration.server.activity.display;

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
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".displays.requesters;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.")).add(mark("name", "firstUpperCase")).add(literal("Display;\nimport io.intino.pandora.exceptions.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\n\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\nimport io.intino.pandora.exceptions.PandoraException;\nimport io.intino.pandora.server.activity.displays.DisplayNotifierProvider;\nimport io.intino.pandora.server.activity.spark.ActivitySparkManager;\nimport io.intino.pandora.server.activity.spark.resources.DisplayRequester;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("DisplayRequester extends DisplayRequester {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("DisplayRequester(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t}\n\n\t@Override\n\tpublic void execute() throws PandoraException {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Display display = display();\n\t\tString operation = operation();\n\n\t\t")).add(mark("request").multiple("\nelse ")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "request & asset")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) {\n\tio.intino.pandora.server.activity.spark.ActivityFile file = display.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");\n\tmanager.write(file.content(), file.label());\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) display.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");")),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(literal("manager.fromQuery(\"value\", ")).add(mark("value", "FirstUpperCase")).add(literal("[].class)")),
			rule().add((condition("type", "parameter")), (condition("trigger", "parameter"))).add(literal("manager.fromQuery(\"value\", ")).add(mark("value", "FirstUpperCase")).add(literal(".class)")),
			rule().add((condition("trigger", "parameter"))),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}