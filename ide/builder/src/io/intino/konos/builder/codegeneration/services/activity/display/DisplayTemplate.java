package io.intino.konos.builder.codegeneration.services.activity.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayTemplate extends Template {

	protected DisplayTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.services.push.User;\n")).add(mark("parent", "import")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Display extends AlexandriaDisplay<")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier> {\n    private ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("Display(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n        super();\n        this.box = box;\n    }\n\t")).add(expression().add(literal("\n")).add(literal("    @Override")).add(literal("\n")).add(literal("\tprotected void init() {")).add(literal("\n")).add(literal("\t\tsuper.init();")).add(literal("\n")).add(literal("\t\t")).add(mark("parent")).add(literal("\n")).add(literal("\t\t")).add(mark("innerDisplay").multiple("\n")).add(literal("\n")).add(literal("\t}")).add(literal("\n"))).add(literal("\n\t")).add(mark("request").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "request & asset"))).add(literal("public io.intino.konos.alexandria.activity.spark.ActivityFile ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n    return null;\n}")),
			rule().add((condition("type", "request"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n\n}")),
			rule().add((condition("type", "dateTime | date")), (condition("type", "list")), (condition("trigger", "parameter"))).add(mark("value")),
			rule().add((condition("type", "dateTime | date")), (condition("trigger", "parameter"))).add(mark("value")),
			rule().add((condition("type", "list")), (condition("trigger", "parameter"))).add(mark("value", "firstUpperCase")).add(literal("[]")),
			rule().add((condition("trigger", "parameter"))).add(mark("value", "firstUpperCase")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("trigger", "import"))).add(literal("import ")).add(mark("package")).add(literal(".displays.*;")),
			rule().add((condition("trigger", "parent"))).add(literal("addAndPersonify(new ")).add(mark("value")).add(literal("Display((")).add(mark("dsl")).add(literal("Box) box.owner()));")),
			rule().add((condition("trigger", "innerDisplay"))).add(literal("addAndPersonify(new ")).add(mark("value", "firstUpperCase")).add(literal("Display((box)));"))
		);
		return this;
	}
}