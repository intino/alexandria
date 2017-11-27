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
			rule().add((condition("type", "mold & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaMoldDisplay;\nimport io.intino.konos.alexandria.activity.model.Mold;\nimport io.intino.konos.alexandria.activity.model.mold.Block;\nimport io.intino.konos.alexandria.activity.model.mold.Stamp;\nimport io.intino.konos.alexandria.activity.model.mold.stamps.Location;\nimport io.intino.konos.alexandria.activity.model.mold.stamps.Picture;\nimport io.intino.konos.alexandria.activity.model.mold.stamps.Snippet;\nimport io.intino.konos.alexandria.activity.model.mold.stamps.Title;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Mold extends AlexandriaMoldDisplay<")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Mold(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildMold(box));\n\t}\n\n\tpublic Mold buildMold(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tMold mold = new Mold()")).add(mark("block", "definition").multiple(";\n")).add(literal(";\n\t\t")).add(mark("block", "add").multiple("\n")).add(literal("\n\t\treturn mold;\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "definition"))).add(literal(".add(new Block()")).add(mark("expanded")).add(expression().add(mark("layout").multiple(""))).add(expression().add(literal("\n")).add(literal("\t")).add(literal(".")).add(mark("stamp").multiple("\n\t\t"))).add(literal("\n")).add(expression().add(mark("block").multiple("\n"))).add(literal(")\n")),
			rule().add((condition("type", "block")), (condition("trigger", "add"))).add(literal("mold.add(b")).add(mark("name")).add(literal(");")),
			rule().add((condition("trigger", "layout"))).add(literal(".add(Block.Layout.")).add(mark("value", "FirstUpperCase")).add(literal(");")),
			rule().add((condition("type", "stamp & location"))).add(literal("new Location().")).add(mark("common")).add(expression().add(literal(".")).add(mark("icon"))).add(literal(".name(\"s1\").label(\"coordinates\").value((object, username) -> TestInfrastructureMold.Stamps.coordinates((")).add(mark("nameType")).add(literal(") object, username)))")),
			rule().add((condition("trigger", "icon"))).add(literal("icon((object, username) -> ")).add(mark("mold")).add(literal("Mold.Stamps.")).add(mark("stamp", "FirstUpperCase")).add(literal(".icon((")).add(mark("moldType")).add(literal(") object, username))")),
			rule().add((condition("type", "stamp & title"))).add(literal("new Title().")).add(mark("common")).add(literal(".value((object, username) -> ")).add(mark("mold")).add(literal("Mold.Stamps.description((")).add(mark("moldType")).add(literal(") object, username)))")),
			rule().add((condition("type", "stamp & picture"))).add(literal("new Picture().")).add(mark("common")).add(literal(".defaultPicture(")).add(mark("defaultPicture")).add(literal(").value((object, username) -> ")).add(mark("mold")).add(literal("Mold.Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".value((")).add(mark("moldType")).add(literal(") object, username))")),
			rule().add((condition("type", "stamp & snippet"))).add(literal("new Snippet().")).add(mark("common")).add(literal(".value((object, username) -> ")).add(mark("mold")).add(literal("Mold.Stamps.")).add(mark("name", "firstUpperCase")).add(literal(".value(((")).add(mark("moldType")).add(literal(") object, username)))")),
			rule().add((condition("trigger", "common"))).add(expression().add(literal("name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".defaultStyle(\"")).add(mark("defaultStyle")).add(literal("\")"))).add(expression().add(literal(".layout(Stamp.Layout.")).add(mark("layout")).add(literal(")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal("(object, username) -> ")).add(mark("mold")).add(literal("Mold.Stamps.")).add(mark("name", "firstLoweCase")).add(literal(".style((")).add(mark("moldType")).add(literal(") object, username))))"))),
			rule().add((condition("trigger", "expanded"))).add(literal(".expanded(")).add(mark("value")).add(literal(")")),
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.net.URL;\nimport java.util.List;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Mold extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Mold {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Mold(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Stamps {\n\n\t\t")).add(mark("stamp", "class")).add(literal("\n\t\tpublic static URL coordinatesIcon(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn station.icon();\n\t\t}\n\n\t\tpublic static String coordinates(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn station.coordinates();\n\t\t}\n\n\t\tpublic static String label(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn station.label();\n\t\t}\n\n\t\tpublic static String description(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic static List<URL> chart(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic static String sequence(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic static String stats(")).add(mark("moldType")).add(literal(" station, String username) {\n\t\t\treturn null;\n\t\t}\n\t}\n\n}")),
			rule().add((condition("type", "stamp")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("property").multiple("\n")).add(literal("\n}")),
			rule().add((condition("trigger", "property")))
		);
		return this;
	}
}