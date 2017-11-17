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
			rule().add((condition("type", "panel & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaPanelDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.activity.model.AbstractView;\nimport io.intino.konos.alexandria.activity.model.Panel;\nimport io.intino.konos.alexandria.activity.model.Toolbar;\nimport io.intino.konos.alexandria.activity.model.panel.View;\nimport io.intino.konos.alexandria.activity.model.renders.RenderDisplay;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.function.Consumer;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal("Panel extends AlexandriaPanelDisplay<")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("Panel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildPanel(box));\n\t}\n\n\tprivate static Panel buildPanel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tPanel panel = new Panel();\n\t\t")).add(expression().add(literal("panel.name(\"")).add(mark("name")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("panel.label(\"")).add(mark("label")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(mark("toolbar", "empty")).add(literal("panel.toolbar(buildToolbar(box));"))).add(literal("\n\t\tbuildViews(box).forEach(v -> panel.add(v));\n\t\treturn panel;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static List<AbstractView> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<AbstractView> result = new ArrayList<>();\n\t\tresult.add(")).add(mark("display")).add(literal(");\n\t\treturn result;\n\t}\n\n}")),
			rule().add((condition("trigger", "display & gen"))).add(literal("new View().render(new RenderDisplay().displayLoader(new RenderDisplay.DisplayLoader() {\n\t@Override\n\tpublic AlexandriaDisplay load(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\t\treturn ")).add(mark("owner", "firstUpperCase")).add(literal("Panel.Views.")).add(mark("name", "firstLowerCase")).add(literal("(box, loadingListener, instantListener);\n\t}\n})).name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\")")),
			rule().add((condition("type", "panel & src"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\n\nimport java.util.function.Consumer;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Panel extends Abstract")).add(mark("name", "firstUpperCase")).add(literal("Panel {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Panel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Views {\n\t\t")).add(mark("display").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("trigger", "display & src"))).add(literal("public static AlexandriaDisplay ")).add(mark("display")).add(literal("Display(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\treturn null;\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "toolbar"))).add(literal("private static Toolbar buildToolbar(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\tToolbar toolbar = new Toolbar();\n\t")).add(expression().add(literal("toolbar.canSearch(")).add(mark("canSearch")).add(literal(");"))).add(literal("\n\treturn toolbar;\n}"))
		);
		return this;
	}
}