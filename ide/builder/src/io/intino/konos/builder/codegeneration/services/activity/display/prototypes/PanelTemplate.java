package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PanelTemplate extends Template {

	protected PanelTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PanelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "panel"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.activity.services.push.User;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.util.function.Consumer;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Toolbar {\n\t\t")).add(mark("operation").multiple("\n")).add(literal("\n\t}\n\n\tpublic static class Views {\n\t\t")).add(mark("view").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "operation"))).add(literal("public static AlexandriaDisplay ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\treturn null;//TODO\n}")),
			rule().add((condition("type", "view & display"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\tpublic static AlexandriaDisplay ")).add(mark("display", "firstLowerCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Object target, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\t\treturn null;//TODO\n\t}\n\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "view & catalogs"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\t")).add(mark("filter")).add(literal("\n\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "filter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Catalog catalog, io.intino.konos.alexandria.activity.model.Element context, Object target, Object object, User user) {\n\treturn true;//TODO\n}")),
			rule().add((condition("trigger", "hidden"))).add(literal("public static boolean hidden(")).add(mark("box")).add(literal("Box box, Object object, User user) {\n\treturn false;//TODO\n}")),
			rule().add((condition("type", "view & mold"))),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}