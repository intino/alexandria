package io.intino.pandora.plugin.codegeneration.action;

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
			rule().add((condition("type", "action"))).add(literal("package ")).add(mark("package", "validname")).add(literal(".actions;\n\nimport ")).add(mark("package", "validname")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.pandora.exceptions.*;\nimport java.time.*;\nimport java.util.*;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Action {\n\n\tpublic ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\t")).add(mark("parameter", "type").multiple("\n")).add(literal("\n\n\tpublic ")).add(expression().add(mark("returnType")).or(expression().add(literal("void")))).add(literal(" execute() ")).add(expression().add(literal("throws ")).add(mark("throws", "FirstUpperCase").multiple(", ")).add(literal(" "))).add(literal("{\n\t\t")).add(mark("returnType", "return")).add(literal("\n\t}\n\n\t")).add(mark("ui")).add(literal("\n}")),
			rule().add((condition("type", "parameter & fileData")), (condition("trigger", "type"))).add(literal("public java.io.InputStream ")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(";")),
			rule().add((condition("type", "parameter & list")), (condition("trigger", "type"))).add(literal("public java.util.List<")).add(mark("type")).add(literal("> ")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(";")),
			rule().add((condition("type", "parameter")), (condition("trigger", "type"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(";")),
			rule().add((condition("attribute", "void")), (condition("trigger", "return"))),
			rule().add((condition("trigger", "return"))).add(literal("return null;")),
			rule().add((condition("type", "schemaImport")), (condition("trigger", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("trigger", "ui"))).add(literal("public io.intino.pandora.server.activity.displays.Soul prepareSoul(io.intino.pandora.server.activity.services.push.ActivityClient client) {\n    return null;\n}"))
		);
		return this;
	}
}