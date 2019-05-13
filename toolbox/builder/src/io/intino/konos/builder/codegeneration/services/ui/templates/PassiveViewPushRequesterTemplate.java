package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PassiveViewPushRequesterTemplate extends Template {

	protected PassiveViewPushRequesterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PassiveViewPushRequesterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ui.displays.requesters;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.")).add(expression().add(mark("packageType")).add(literal("s."))).add(mark("name", "FirstUpperCase")).add(literal(";\nimport io.intino.alexandria.exceptions.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\n\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.rest.spark.SparkReader;\nimport io.intino.alexandria.ui.services.push.UIClient;\nimport io.intino.alexandria.ui.services.push.UIMessage;\nimport io.intino.alexandria.ui.spark.UISparkManager;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("PushRequester extends io.intino.alexandria.ui.displays.requesters.")).add(expression().add(mark("type", "class", "FirstUpperCase"))).add(literal("PushRequester {\n\tpublic void execute(UIClient client, UIMessage message) {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal(" display = display(client, message);\n\t\tif (display == null) return;\n\t\tString operation = operation(message);\n\t\tString data = data(message);\n\n\t\t")).add(mark("request").multiple("\nelse ")).add(literal("\n\t\tsuper.execute(client, message);\n\t}\n}")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "class"))).add(literal("Display")),
			rule().add((condition("type", "request & file")), (condition("trigger", "request"))),
			rule().add((condition("type", "request & asset")), (condition("trigger", "request"))),
			rule().add((condition("type", "request")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) {\n\tdisplay.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");\n\treturn;\n}")),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(literal("SparkReader.read(data, ")).add(mark("value")).add(literal("[].class)")),
			rule().add((condition("type", "parameter & file")), (condition("trigger", "parameter"))),
			rule().add((condition("type", "parameter")), (condition("trigger", "parameter"))).add(literal("SparkReader.read(data, ")).add(mark("value")).add(literal(".class)")),
			rule().add((condition("trigger", "parameter"))),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}