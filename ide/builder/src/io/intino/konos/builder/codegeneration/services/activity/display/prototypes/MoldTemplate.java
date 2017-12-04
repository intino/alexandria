package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MoldTemplate extends Template {

	protected MoldTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MoldTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.net.URL;\nimport java.util.List;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Mold extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Mold {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Mold(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\tpublic static class Stamps {\n\n\t\t")).add(mark("block", "class").multiple("\n\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "class"))).add(mark("stamp", "class").multiple("\n")).add(literal("\n")).add(mark("block", "class").multiple("\n")),
			rule().add((condition("type", "stamp & operation & openDialog")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic static String path(")).add(mark("moldType")).add(literal(" object, String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & operation & download")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic static Execution execution(")).add(mark("moldType")).add(literal(" object, String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & location")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static java.net.URL icon(")).add(mark("moldType")).add(literal(" object, String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & catalogLink")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("moldType")).add(literal(" object, String username) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & highlight")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String color(")).add(mark("moldType")).add(literal(" object, String username) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & itemeLinks")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("moldType")).add(literal(" object, String username) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("property").multiple("\n")).add(literal("\n}")),
			rule().add((condition("trigger", "common"))).add(literal("\npublic static ")).add(mark("valueType")).add(literal(" value(")).add(mark("moldType")).add(literal(" object, String username) {\n\treturn null;//TODO\n}\n\npublic static String style(")).add(mark("moldType")).add(literal(" object, String username) {\n\treturn \"\";//TODO\n}")),
			rule().add((condition("trigger", "property"))).add(literal("public static ")).add(mark("returnType")).add(literal(" ")).add(mark("name")).add(literal("(")).add(mark("moldType")).add(literal(" object, String username) {\n\treturn null;//TODO\n}"))
		);
		return this;
	}
}