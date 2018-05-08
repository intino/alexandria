package io.intino.konos.builder.codegeneration.action;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ActionTemplate extends Template {

	protected ActionTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ActionTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "action")), not(condition("type", "ui"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".actions;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.alexandria.exceptions.*;\nimport java.time.*;\nimport java.util.*;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Action {\n\n\tpublic ")).add(mark("box", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Box box;\n\tpublic io.intino.konos.alexandria.schema.Context context = new io.intino.konos.alexandria.schema.Context();")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("parameter", "type").multiple("\n"))).add(literal("\n\n\tpublic ")).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute() ")).add(expression().add(literal("throws ")).add(mark("throws", "FirstUpperCase").multiple(", ")).add(literal(" "))).add(literal("{\n\t\t")).add(mark("returnType", "return")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "ui & action & gen"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".actions;\n\nimport io.intino.konos.alexandria.exceptions.*;\nimport io.intino.konos.alexandria.ui.spark.actions.AlexandriaResourceAction;\nimport java.util.*;\n\npublic abstract class Abstract")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Action extends Alexandria")).add(mark("type")).add(literal("Action {\n\n\tpublic Abstract")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Action() { super(\"")).add(mark("uiService", "camelCaseToSnakeCase", "lowerCase")).add(literal("\"); }\n\n\tpublic String execute() ")).add(expression().add(literal("throws ")).add(mark("throws", "FirstUpperCase").multiple(", ")).add(literal(" "))).add(literal("{\n\t\treturn super.template(\"")).add(mark("name")).add(literal("\");\n\t}\n\n\t@Override\n\tprotected String title() {\n\t\treturn \"")).add(mark("title")).add(literal("\";\n\t}\n\n\t@Override\n\tprotected java.net.URL favicon() {\n\t\treturn ")).add(expression().add(mark("favicon")).or(expression().add(literal("null;")))).add(literal("\n\t}\n}")),
			rule().add((condition("trigger", "favicon"))).add(literal("this.getClass().getResource(\"")).add(mark("value")).add(literal("\");")),
			rule().add((condition("type", "ui & action")), not(condition("type", "gen"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".actions;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.alexandria.exceptions.*;\nimport io.intino.konos.alexandria.ui.spark.actions.AlexandriaResourceAction;\nimport java.time.*;\nimport java.util.*;\n")).add(expression().add(literal("import ")).add(mark("importDialogs", "validPackage")).add(literal(".dialogs.*;"))).add(literal("\n")).add(expression().add(literal("import ")).add(mark("importDisplays", "validPackage")).add(literal(".displays.*;"))).add(literal("\n\npublic class ")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Action extends Abstract")).add(mark("name", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Action {\n\n\tpublic ")).add(mark("box", "snakecaseToCamelCase", "firstUpperCase")).add(literal("Box box;\n\t")).add(mark("parameter", "type").multiple("\n")).add(literal("\n\n\t")).add(mark("component")).add(literal("\n}")),
			rule().add((condition("type", "parameter & fileData")), (condition("trigger", "type"))).add(literal("public java.io.InputStream ")).add(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).add(literal(";")),
			rule().add((condition("type", "parameter & list")), (condition("trigger", "type"))).add(literal("public java.util.List<")).add(mark("type")).add(literal("> ")).add(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).add(literal(";")),
			rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name", "snakecaseToCamelCase", "FirstLowerCase")).add(literal(";")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("return null;")),
			rule().add((condition("type", "schemaImport")), (condition("trigger", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("type", "component"))).add(literal("public io.intino.konos.alexandria.ui.displays.Soul prepareSoul(io.intino.konos.alexandria.ui.services.push.UIClient client) {\n    return new io.intino.konos.alexandria.ui.displays.Soul(session) {\n\t\t@Override\n\t\tpublic void personify() {\n\t\t\t")).add(mark("value", "snakecaseToCamelCase", "firstUpperCase")).add(literal(" display = new ")).add(mark("value", "snakecaseToCamelCase", "firstUpperCase")).add(literal("(box);\n\t\t\tregister(display);\n\t\t\t")).add(expression().add(mark("editor"))).add(literal("\n\t\t\tdisplay.personify();\n\t\t}\n\t};\n}")),
			rule().add((condition("trigger", "editor"))).add(literal("display.onSave((document, completed) -> save(document, completed));"))
		);
		return this;
	}
}