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
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.net.URL;\nimport java.util.List;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\tpublic static class Stamps {\n\n\t\t")).add(mark("block", "class").multiple("\n\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "class"))).add(mark("stamp", "class").multiple("\n")).add(literal("\n")).add(mark("block", "class").multiple("\n")),
			rule().add((condition("type", "stamp & openDialogOperation")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String path(")).add(mark("box")).add(literal("Box box, String ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal("Id, String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & downloadOperation")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.activity.Resource execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String option, String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & exportOperation")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.activity.Resource execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", java.time.Instant from, java.time.Instant to, String option, String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & taskOperation")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static void execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\n\t}\n}")),
			rule().add((condition("type", "stamp & location")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static java.net.URL icon(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & catalogLink")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(expression().add(mark("filter"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & highlight")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String color(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & embeddedDisplay")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(mark("displayBuilder")).add(literal("\n}")),
			rule().add((condition("type", "stamp & embeddedCatalog")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(expression().add(mark("embeddedCatalogFilter"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & icon")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & itemLinks")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("trigger", "common"))).add(literal("\n")).add(mark("valueMethod")).add(expression().add(literal("\n")).add(literal("\n")).add(mark("editable"))).add(expression().add(literal("\n")).add(literal("\n")).add(mark("style")).add(literal("\n"))),
			rule().add((condition("trigger", "style"))).add(literal("public static String style(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\treturn \"\";//TODO\n}")),
			rule().add((condition("trigger", "embeddedCatalogFilter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(" source, Object target, String username) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "filter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(" source, Object target, String username) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "displayBuilder"))).add(literal("public static io.intino.konos.alexandria.activity.displays.AlexandriaStamp buildDisplay(")).add(mark("box")).add(literal("Box box, String name, String username) {\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "editable"))).add(literal("public static io.intino.konos.alexandria.activity.model.mold.Stamp.Editable.Refresh onChange(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String value, String username) {\n\treturn null;\n}")),
			rule().add((condition("trigger", "valueMethod"))).add(literal("public static ")).add(mark("valueType")).add(literal(" value(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String username) {\n\treturn null;//TODO\n}")),
			rule().add((condition("attribute", "resource")), (condition("trigger", "valueType"))).add(literal("java.net.URL")),
			rule().add((condition("attribute", "breadcrumbs")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.activity.model.mold.stamps.Tree")),
			rule().add((condition("attribute", "itemlLinks")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.activity.model.mold.stamps.Links")),
			rule().add((condition("attribute", "Picture")), (condition("trigger", "valueType"))).add(literal("java.util.List<java.net.URL>")),
			rule().add((condition("attribute", "Rating")), (condition("trigger", "valueType"))).add(literal("java.lang.Double")),
			rule().add((condition("trigger", "valueType"))).add(literal("java.lang.String"))
		);
		return this;
	}
}