package io.intino.konos.builder.codegeneration.server.activity.dialog;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DialogTemplate extends Template {

	protected DialogTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DialogTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "dialog"))).add(literal("package ")).add(mark("package")).add(literal(".dialogs;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.konos.server.activity.dialogs.Dialog;\nimport io.intino.konos.server.activity.dialogs.DialogValidator;\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Dialog extends ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("DialogDisplay {\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Dialog(")).add(mark("box")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Validators {\n\t\t")).add(mark("validator").multiple("\n")).add(literal("\n\t}\n\n\tpublic static class Sources {\n\t\t")).add(mark("sources").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "validator"))).add(literal("public static DialogValidator.Result ")).add(mark("name")).add(literal("(")).add(mark("field")).add(literal(" value) {\n\treturn null;\n}")),
			rule().add((condition("type", "sources"))).add(literal("public static java.util.List<String> ")).add(mark("name")).add(literal("(Dialog.Tab.OptionBox self) {\n\treturn java.util.Collections.emptyList();\n}"))
		);
		return this;
	}
}