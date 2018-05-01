package io.intino.konos.builder.codegeneration.services.ui.display.panel;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class PanelSrcTemplate extends Template {

	protected PanelSrcTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new PanelSrcTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "panel"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.ui.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.ui.services.push.UISession;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.util.function.Consumer;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Toolbar {\n\t\t")).add(mark("operation").multiple("\n")).add(literal("\n\t}\n\n\tpublic static class Views {\n\t\t")).add(mark("view").multiple("\n")).add(literal("\n\t}\n}"))
		);
		return this;
	}
}