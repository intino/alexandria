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
			rule().add((condition("type", "panel & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaPanelDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.activity.model.AbstractView;\nimport io.intino.konos.alexandria.activity.model.Panel;\nimport io.intino.konos.alexandria.activity.model.Toolbar;\nimport io.intino.konos.alexandria.activity.model.panel.View;\nimport io.intino.konos.alexandria.activity.model.renders.RenderDisplay;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.function.Consumer;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal("Panel extends AlexandriaPanelDisplay<")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("Panel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildPanel(box));\n\t}\n\n\tprivate static Panel buildPanel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tPanel panel = new Panel();\n\t\t")).add(expression().add(literal("panel.name(\"")).add(mark("name")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("panel.label(\"")).add(mark("label")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(mark("toolbar", "empty")).add(literal("panel.toolbar(buildToolbar(box));"))).add(literal("\n\t\tbuildViews(box).forEach(v -> panel.add(v));\n\t\treturn panel;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static List<AbstractView> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<AbstractView> result = new ArrayList<>();\n\t\tresult.add(new View().render(new RenderDisplay().displayLoader(new RenderDisplay.DisplayLoader() {\n\t\t\t@Override\n\t\t\tpublic AlexandriaDisplay load(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\t\t\t\treturn ")).add(mark("name", "firstUpperCase")).add(literal("Panel.Views.olapDisplay(box, loadingListener, instantListener);\n\t\t\t}\n\t\t})).name(\"v1\").label(\"Charts\"));\n\t\treturn result;\n\t}\n\n}")),
			rule().add((condition("type", "panel & src"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport io.intino.sumus.box.SumusBox;\nimport io.intino.sumus.box.displays.SumusOlapDisplay;\nimport io.intino.test.DisplayHelper;\nimport io.intino.test.konos.box.TestKonosBox;\n\nimport java.util.function.Consumer;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Panel extends Abstract")).add(mark("name", "firstUpperCase")).add(literal("Panel {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Panel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Views {\n\t\tpublic static AlexandriaDisplay olapDisplay(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\t\t\tSumusOlapDisplay display = new SumusOlapDisplay(sumusBox(box));\n\t\t\tdisplay.nameSpaceHandler(DisplayHelper.nameSpaceHandler(sumusBox(box)));\n\t\t\tdisplay.olap(box.graph().sampleOlap());\n\t\t\tdisplay.onLoading(loadingListener);\n\t\t\tdisplay.onSelect(instantListener::accept);\n\t\t\treturn display;\n\t\t}\n\t}\n\n\tprivate static SumusBox sumusBox(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\treturn (SumusBox) box.owner();\n\t}\n\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "toolbar"))).add(literal("private static Toolbar buildToolbar(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\tToolbar toolbar = new Toolbar();\n\t")).add(expression().add(literal("toolbar.canSearch(")).add(mark("canSearch")).add(literal(");"))).add(literal("\n\treturn toolbar;\n}"))
		);
		return this;
	}
}