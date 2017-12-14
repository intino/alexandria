package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractMoldTemplate extends Template {

	protected AbstractMoldTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractMoldTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaMoldDisplay;\nimport io.intino.konos.alexandria.activity.model.Mold;\nimport io.intino.konos.alexandria.activity.model.mold.Block;\nimport io.intino.konos.alexandria.activity.model.mold.Stamp;\nimport io.intino.konos.alexandria.activity.model.mold.stamps.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" extends AlexandriaMoldDisplay<")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildMold(box));\n\t}\n\n\tpublic Mold buildMold(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tMold mold = new Mold()")).add(mark("block", "definition").multiple("\n")).add(literal(";\n\t\treturn mold;\n\t}\n\n\tprivate java.net.URL urlOf(String url)  {\n\t\ttry {\n\t\t\treturn new java.net.URL(url);\n\t\t} catch (java.net.MalformedURLException e) {\n\t\t\treturn null;\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "definition"))).add(literal(".add(new Block()")).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(mark("expanded"))).add(expression().add(mark("layout").multiple(""))).add(expression().add(mark("height"))).add(expression().add(mark("width"))).add(expression().add(mark("hidden"))).add(expression().add(mark("hiddenIfMobile"))).add(expression().add(mark("style"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("stamp").multiple("\n\t\t"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("block", "definition").multiple("\n"))).add(literal(")\n")),
			rule().add((condition("trigger", "height"))).add(literal(".height(")).add(mark("value")).add(literal(")")),
			rule().add((condition("trigger", "width"))).add(literal(".width(")).add(mark("value")).add(literal(")")),
			rule().add((condition("attribute", "hiddenEnabled")), (condition("trigger", "hidden"))).add(literal(".hidden((object) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".hidden((")).add(mark("moldClass")).add(literal(") object))")),
			rule().add((condition("trigger", "hidden"))),
			rule().add((condition("trigger", "hiddenIfMobile"))).add(literal(".hiddenIfMobile(")).add(mark("value")).add(literal(")")),
			rule().add((condition("trigger", "layout"))).add(literal(".add(Block.Layout.")).add(mark("value", "FirstUpperCase")).add(literal(")")),
			rule().add((condition("type", "stamp & location"))).add(literal(".add(new Location()")).add(expression().add(mark("icon"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & highlight"))).add(literal(".add(new Highlight().color((object, username) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".color((")).add(mark("moldClass")).add(literal(") object, username))")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & icon"))).add(literal(".add(new io.intino.konos.alexandria.activity.model.mold.stamps.icons.AlexandriaIcon()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & customicon"))).add(literal(".add(new io.intino.konos.alexandria.activity.model.mold.stamps.icons.ResourceIcon()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & operation"))).add(literal(".add(new io.intino.konos.alexandria.activity.model.mold.stamps.operations.")).add(mark("operationType")).add(literal("Operation().path((object, username) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".path((")).add(mark("moldClass")).add(literal(") object, username)))")),
			rule().add((condition("type", "stamp & picture"))).add(literal(".add(new Picture().defaultPicture(urlOf(\"")).add(mark("defaultPicture")).add(literal("\"))")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp"))).add(literal(".add(new ")).add(mark("type", "FirstUpperCase")).add(literal("()")).add(mark("common")).add(literal(")")),
			rule().add((condition("trigger", "common"))).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(literal(".defaultStyle(\"")).add(mark("defaultStyle")).add(literal("\")"))).add(expression().add(literal(".layout(Stamp.Layout.")).add(mark("layout")).add(literal(")"))).add(literal(".style((object, username) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "firstUpperCase")).add(literal(".style((")).add(mark("moldClass")).add(literal(") object, username)).value((object, username) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".value((")).add(mark("moldClass")).add(literal(") object, username))")),
			rule().add((condition("trigger", "icon"))).add(literal(".icon((object, username) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".icon((")).add(mark("moldClass")).add(literal(") object, username))")),
			rule().add((condition("trigger", "expanded"))).add(literal(".expanded(")).add(mark("value")).add(literal(")"))
		);
		return this;
	}
}