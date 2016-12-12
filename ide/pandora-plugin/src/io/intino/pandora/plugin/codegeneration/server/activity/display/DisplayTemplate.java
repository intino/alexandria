package io.intino.pandora.plugin.codegeneration.server.activity.display;

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
			rule().add((condition("type", "display"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.pandora.exceptions.*;\nimport ")).add(mark("package")).add(literal(".*;\nimport ")).add(mark("package", "validname")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n")).add(mark("schemaImport")).add(literal("\nimport ")).add(mark("package", "validname")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\nimport io.intino.pandora.server.activity.displays.Display;\nimport io.intino.pandora.server.activity.services.push.User;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Display extends Display<")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier> {\n    private ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n    public ")).add(mark("name", "firstUpperCase")).add(literal("Display(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n        super();\n        this.box = box;\n    }\n\n\t")).add(mark("request").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "request"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter")).add(literal(" value"))).add(literal(") {\n\n}")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}