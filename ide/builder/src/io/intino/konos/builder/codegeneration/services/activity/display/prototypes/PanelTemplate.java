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
			rule().add((condition("type", "panel & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaPanel;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.activity.model.AbstractView;\nimport io.intino.konos.alexandria.activity.model.Panel;\nimport io.intino.konos.alexandria.activity.model.Toolbar;\nimport io.intino.konos.alexandria.activity.model.panel.View;\nimport io.intino.konos.alexandria.activity.model.renders.RenderDisplay;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.function.Consumer;\n\npublic abstract class Abstract")).add(mark("name", "firstUpperCase")).add(literal(" extends AlexandriaPanel<")).add(mark("name", "firstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildPanel(box));\n\t}\n\n\tprivate static Panel buildPanel(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tPanel panel = new Panel();\n\t\t")).add(expression().add(literal("panel.name(\"")).add(mark("name")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(literal("panel.label(\"")).add(mark("label")).add(literal("\");"))).add(literal("\n\t\t")).add(expression().add(mark("toolbar", "empty")).add(literal("panel.toolbar(buildToolbar(box));"))).add(literal("\n\t\tbuildViews(box).forEach(v -> panel.add(v));\n\t\treturn panel;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static List<AbstractView> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<AbstractView> result = new ArrayList<>();\n\t\t")).add(mark("view", "genview").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n\n}")),
			rule().add((condition("trigger", "toolbar"))).add(literal("private static Toolbar buildToolbar(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\tToolbar toolbar = new Toolbar();\n\ttoolbar.canSearch(")).add(mark("canSearch")).add(literal(");\n\t")).add(mark("operation").multiple("\n")).add(literal("\n\treturn toolbar;\n}")),
			rule().add((condition("type", "operation & download")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Download().execute((element, option, username) -> ")).add(mark("panel")).add(literal(".Toolbar.")).add(mark("name", "validName", "firstLowerCase")).add(literal("(box, element, option, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\""))).add(literal("));")),
			rule().add((condition("type", "operation & export")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Export().execute((element, from, to, username) -> ")).add(mark("panel")).add(literal(".Toolbar.")).add(mark("name", "validName", "firstLowerCase")).add(literal("(box, element, from, to, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & openDialog")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.OpenDialog().execute((element, from, to, username) -> ")).add(mark("panel")).add(literal(".Toolbar.")).add(mark("name", "validName", "firstLowerCase")).add(literal("(box, element, from, to, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & task")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Task().execute((element, from, to, username) -> ")).add(mark("panel")).add(literal(".Toolbar.")).add(mark("name", "validName", "firstLowerCase")).add(literal("(box, element, from, to, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "display")), (condition("trigger", "genview"))).add(literal("result.add(new View().render(new io.intino.konos.alexandria.activity.model.renders.RenderDisplay().displayLoader(new io.intino.konos.alexandria.activity.model.renders.RenderDisplay.DisplayLoader() {\n\t@Override\n\tpublic AlexandriaDisplay load(Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\t\treturn ")).add(mark("owner", "firstUpperCase")).add(literal(".Views.")).add(mark("display", "firstLowerCase")).add(literal("(box, loadingListener, instantListener);\n\t}\n})).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "mold")), (condition("trigger", "genview"))).add(literal("result.add(new View().render(new io.intino.konos.alexandria.activity.model.renders.RenderMold().mold((io.intino.konos.alexandria.activity.model.Mold) ElementDisplays.displayFor(box, \"")).add(mark("name")).add(literal("\").element())).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "catalogs")), (condition("trigger", "genview"))).add(literal("result.add(new View().render(new io.intino.konos.alexandria.activity.model.renders.RenderCatalogs().catalogs(ElementDisplays.elementsFor(box, io.intino.konos.alexandria.activity.model.Catalog.class, ")).add(mark("catalog", "quoted").multiple(", ")).add(literal("))).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "panel"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\n\nimport java.util.function.Consumer;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "firstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\tpublic static class Toolbar {\n\t\t")).add(mark("operation", "src").multiple("\n")).add(literal("\n\t}\n\n\tpublic static class Views {\n\t\t")).add(mark("view", "src").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "operation")), (condition("trigger", "src"))).add(literal("public static AlexandriaDisplay ")).add(mark("name")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\treturn null;//TODO\n}")),
			rule().add((condition("type", "view")), (condition("trigger", "src"))).add(literal("public static AlexandriaDisplay ")).add(mark("display")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener) {\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}