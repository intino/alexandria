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
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.services.push.ActivitySession;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.net.URL;\nimport java.util.List;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Stamps {\n\t\t")).add(mark("block", "stamps").multiple("\n\n")).add(literal("\n\t}\n\n\tpublic static class Blocks {\n\t\t")).add(mark("block").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "stamps"))).add(mark("stamp").multiple("\n")).add(literal("\n")).add(mark("block", "stamps").multiple("\n")),
			rule().add((condition("trigger", "block"))).add(mark("hidden")).add(literal("\n")).add(mark("block").multiple("\n")),
			rule().add((condition("trigger", "hidden"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\tpublic static boolean hidden(")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn false;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & downloadOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.activity.Resource execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String option, ActivitySession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & previewOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static java.net.URL preview(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & exportOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.activity.Resource execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", java.time.Instant from, java.time.Instant to, String option, ActivitySession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & taskOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.activity.model.mold.stamps.operations.TaskOperation.Refresh execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String selfId, ActivitySession session) {\n\t\treturn io.intino.konos.alexandria.activity.model.mold.stamps.operations.TaskOperation.Refresh.None;\n\t}\n}")),
			rule().add((condition("type", "stamp & location"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static java.net.URL icon(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\t\treturn null;//TODO\n\t}\n\n\tpublic static String drawingColor(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & catalogLink"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(expression().add(mark("filter"))).add(literal("\n\t")).add(expression().add(mark("itemLoader"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & highlight"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String color(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & embeddedDisplay"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(mark("displayBuilder")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTimeRange"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTimeRangeNavigator"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTime"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTimeNavigator"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & embeddedCatalog"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(expression().add(mark("embeddedCatalogFilter"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & icon"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & itemLinks"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("trigger", "common"))).add(literal("\n")).add(mark("valueMethod")).add(expression().add(literal("\n")).add(literal("\n")).add(mark("editable"))).add(expression().add(literal("\n")).add(literal("\n")).add(mark("style")).add(literal("\n"))),
			rule().add((condition("trigger", "style"))).add(literal("public static String style(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\treturn \"\";//TODO\n}")),
			rule().add((condition("trigger", "embeddedCatalogFilter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", Object target, ActivitySession session) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "filter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", Object target, ActivitySession session) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "itemLoader"))).add(literal("public static String item(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\treturn null;//TODO return item to open\n}")),
			rule().add((condition("trigger", "displayBuilder"))).add(literal("public static io.intino.konos.alexandria.activity.displays.AlexandriaStamp buildDisplay(")).add(mark("box")).add(literal("Box box, String name, ActivitySession session) {\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "editable"))).add(literal("public static io.intino.konos.alexandria.activity.model.mold.Stamp.Editable.Refresh onChange(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String value, ActivitySession session) {\n\treturn null;\n}")),
			rule().add((condition("trigger", "valueMethod"))).add(literal("public static ")).add(mark("valueType")).add(literal(" value(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", ActivitySession session) {\n\treturn null;//TODO\n}")),
			rule().add((condition("attribute", "resource")), (condition("trigger", "valueType"))).add(literal("java.net.URL")),
			rule().add((condition("attribute", "Breadcrumbs")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.activity.model.mold.stamps.Tree")),
			rule().add((condition("attribute", "CardWallet")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.activity.model.mold.stamps.Wallet")),
			rule().add((condition("attribute", "ItemLinks")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.activity.model.mold.stamps.Links")),
			rule().add((condition("attribute", "Picture")), (condition("trigger", "valueType"))).add(literal("java.util.List<java.net.URL>")),
			rule().add((condition("attribute", "Rating")), (condition("trigger", "valueType"))).add(literal("java.lang.Double")),
			rule().add((condition("trigger", "valueType"))).add(literal("java.lang.String"))
		);
		return this;
	}
}