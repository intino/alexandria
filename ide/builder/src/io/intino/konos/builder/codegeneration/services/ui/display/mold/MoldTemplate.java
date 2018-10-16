package io.intino.konos.builder.codegeneration.services.ui.display.mold;

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
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.ui.services.push.UISession;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.net.URL;\nimport java.util.List;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Stamps {\n\t\t")).add(mark("block", "stamps").multiple("\n\n")).add(literal("\n\t}\n\n\tpublic static class Blocks {\n\t\t")).add(mark("block").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "stamps"))).add(mark("stamp").multiple("\n")).add(literal("\n")).add(mark("block", "stamps").multiple("\n")).add(expression().add(literal("\n")).add(mark("blockClassName"))),
			rule().add((condition("trigger", "block"))).add(mark("hidden")).add(literal("\n")).add(mark("block").multiple("\n")).add(expression().add(literal("\n")).add(mark("blockClassName"))),
			rule().add((condition("trigger", "hidden"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\tpublic static boolean hidden(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn false;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & openDialogOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & openExternalDialogOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String dialogPath(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn null;//TODO\n\t}\n\n\tpublic static String dialogTitle(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn null;//by default is set to operation label\n\t}\n}")),
			rule().add((condition("type", "stamp & openCatalogOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("catalogFilter"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("openCatalogOperationExecution"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & downloadOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.ui.Resource execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String option, UISession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & previewOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static java.net.URL preview(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & taskOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.ui.model.mold.StampResult execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String selfId, UISession session) {\n\t\treturn io.intino.konos.alexandria.ui.model.mold.StampResult.none();\n\t}\n}")),
			rule().add((condition("type", "stamp & exportOperation"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static io.intino.konos.alexandria.ui.Resource execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", java.time.Instant from, java.time.Instant to, String option, UISession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & location"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static java.net.URL icon(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn null;//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & catalogLink"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(expression().add(mark("filter"))).add(literal("\n\t")).add(expression().add(mark("itemLoader"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & picture"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("avatarProperties"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & highlight"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & embeddedDisplay"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(mark("displayBuilder")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTimeRange"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTimeRangeNavigator"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTime"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & catalogTimeNavigator"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("type", "stamp & embeddedCatalog"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\t")).add(expression().add(mark("catalogFilter"))).add(literal("\n}")),
			rule().add((condition("type", "stamp & icon"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp & itemLinks"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n\n\tpublic static String title(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\t\treturn \"\";//TODO\n\t}\n}")),
			rule().add((condition("type", "stamp"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("common")).add(literal("\n}")),
			rule().add((condition("trigger", "common"))).add(literal("\n")).add(mark("valueMethod")).add(expression().add(literal("\n")).add(literal("\n")).add(mark("editable"))).add(expression().add(literal("\n")).add(literal("\n")).add(mark("labelLoader"))).add(expression().add(literal("\n")).add(literal("\n")).add(mark("style"))).add(expression().add(literal("\n")).add(literal("\n")).add(mark("className"))).add(expression().add(literal("\n")).add(literal("\n")).add(mark("color"))),
			rule().add((condition("trigger", "labelLoader"))).add(literal("public static String label(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn \"\";//TODO\n}")),
			rule().add((condition("trigger", "style"))).add(literal("public static String style(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn \"\";//TODO\n}")),
			rule().add((condition("trigger", "className"))).add(literal("public static String className(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn \"\";//TODO\n}")),
			rule().add((condition("trigger", "catalogFilter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", Object target, UISession session) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "openCatalogOperationExecution"))).add(literal("public static io.intino.konos.alexandria.ui.model.mold.StampResult execute(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", List<Object> selection, UISession session) {\n\treturn io.intino.konos.alexandria.ui.model.mold.StampResult.none();\n}")),
			rule().add((condition("trigger", "filter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", Object target, UISession session) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "itemLoader"))).add(literal("public static String item(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn null;//TODO return item to open\n}")),
			rule().add((condition("trigger", "displayBuilder"))).add(literal("public static io.intino.konos.alexandria.ui.displays.AlexandriaStamp buildDisplay(")).add(mark("box")).add(literal("Box box, String name, UISession session) {\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "editable"))).add(literal("public static io.intino.konos.alexandria.ui.model.mold.StampResult onChange(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String value, UISession session) {\n\treturn StampResult.none();\n}\npublic static String onValidate(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", String value, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("trigger", "valueMethod"))).add(literal("public static ")).add(mark("valueType")).add(literal(" value(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "color"))).add(literal("public static io.intino.konos.alexandria.ui.model.mold.Stamp.Color color(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "avatarProperties"))).add(literal("public static io.intino.konos.alexandria.ui.model.mold.stamps.Picture.AvatarProperties avatarProperties(")).add(mark("box")).add(literal("Box box, ")).add(mark("moldClass")).add(literal(" ")).add(mark("moldClass", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn null;//TODO\n}")),
			rule().add((condition("attribute", "resource")), (condition("trigger", "valueType"))).add(literal("java.net.URL")),
			rule().add((condition("attribute", "Breadcrumbs")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.ui.model.mold.stamps.Tree")),
			rule().add((condition("attribute", "CardWallet")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.ui.model.mold.stamps.Wallet")),
			rule().add((condition("attribute", "Timeline")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.ui.model.mold.stamps.Timeline")),
			rule().add((condition("attribute", "Pie")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.ui.model.mold.stamps.Pie")),
			rule().add((condition("attribute", "Histogram")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.ui.model.mold.stamps.Histogram")),
			rule().add((condition("attribute", "ItemLinks")), (condition("trigger", "valueType"))).add(literal("io.intino.konos.alexandria.ui.model.mold.stamps.Links")),
			rule().add((condition("attribute", "Picture")), (condition("trigger", "valueType"))).add(literal("java.util.List<java.net.URL>")),
			rule().add((condition("attribute", "Rating")), (condition("trigger", "valueType"))).add(literal("java.lang.Double")),
			rule().add((condition("trigger", "valueType"))).add(literal("java.lang.String"))
		);
		return this;
	}
}