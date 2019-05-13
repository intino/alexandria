package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PassiveViewRequesterTemplate extends Template {

	protected PassiveViewRequesterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PassiveViewRequesterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display & accessible"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ui.displays.requesters;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.")).add(expression().add(mark("packageType")).add(literal("s."))).add(mark("name", "FirstUppercase")).add(literal(";\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.")).add(expression().add(mark("packageType")).add(literal("s."))).add(mark("name", "FirstUpperCase")).add(literal("Proxy;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\nimport io.intino.alexandria.ui.spark.UISparkManager;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("ProxyRequester extends io.intino.alexandria.ui.displays.requesters.DisplayProxyRequester {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("ProxyRequester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tString operation = operation();\n\t\tif (operation.equals(\"registerPersonifiedDisplay\")) {\n\t\t\t")).add(mark("name", "FirstUpperCase")).add(literal("Proxy display = display();\n\t\t\tif (display != null) display.registerPersonifiedDisplay(manager.fromQuery(\"v\", String.class));\n\t\t}\n\t\telse {\n\t\t\t")).add(mark("name", "FirstUppercase")).add(literal(" display = personifiedDisplay();\n\t\t\tif (display == null) return;\n\t\t\tif (operation.equals(\"refreshPersonifiedDisplay\")) {\n\t\t\t\t")).add(mark("parameter").multiple("\n")).add(literal("\n\t\t\t\tdisplay.refresh();\n\t\t\t\treturn;\n\t\t\t}\n\t\t\t")).add(expression().add(literal("else ")).add(mark("request").multiple("\nelse "))).add(literal("\n\t\t\tsuper.execute();\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ui.displays.requesters;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.")).add(expression().add(mark("packageType")).add(literal("s."))).add(mark("name", "FirstUpperCase")).add(literal(";\nimport io.intino.alexandria.exceptions.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\n\nimport ")).add(mark("package", "validPackage")).add(literal(".ui.displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\nimport io.intino.alexandria.ui.spark.UISparkManager;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Requester extends ")).add(mark("parentType")).add(literal(" {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Requester(UISparkManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\t")).add(mark("name", "firstUpperCase")).add(literal(" display = display();\n\t\tif (display == null) return;\n\t\tString operation = operation();\n\n\t\t")).add(mark("request").multiple("\nelse ")).add(literal("\n\t\tsuper.execute();\n\t}\n}")),
			rule().add((condition("attribute", "extensionOf")), (condition("trigger", "parentType"))).add(mark("parent", "firstUpperCase")).add(literal("Requester")),
			rule().add((condition("trigger", "parentType"))).add(literal("io.intino.alexandria.ui.displays.requesters.")).add(expression().add(mark("type", "class", "FirstUpperCase"))).add(literal("Requester")),
			rule().add((condition("attribute", "Display")), (condition("trigger", "class"))).add(literal("Display")),
			rule().add((condition("type", "request & asset")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) {\n\tio.intino.alexandria.ui.spark.UIFile file = display.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");\n\tmanager.write(file.content(), file.label(), file.embedded());\n\treturn;\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "request"))).add(literal("if (operation.equals(\"")).add(mark("name")).add(literal("\")) {\n\tdisplay.")).add(mark("name")).add(literal("(")).add(mark("parameter")).add(literal(");\n\treturn;\n}")),
			rule().add((condition("type", "parameter & accessible")), (condition("trigger", "parameter"))).add(literal("display.")).add(mark("value", "firstLowercase")).add(literal("(manager.fromQuery(\"")).add(mark("value")).add(literal("\", String.class));")),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(literal("manager.fromQuery(\"v\", ")).add(mark("value")).add(literal("[].class)")),
			rule().add((condition("type", "parameter & file")), (condition("trigger", "parameter"))).add(literal("manager.fromForm(\"v\", ")).add(mark("value")).add(literal(".class)")),
			rule().add((condition("type", "parameter")), (condition("trigger", "parameter"))).add(literal("manager.fromQuery(\"v\", ")).add(mark("value")).add(literal(".class)")),
			rule().add((condition("trigger", "parameter"))),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}