package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractCatalogTemplate extends Template {

	protected AbstractCatalogTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractCatalogTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "catalog & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaCatalogDisplay;\nimport io.intino.konos.alexandria.activity.model.AbstractView;\nimport io.intino.konos.alexandria.activity.model.Catalog;\nimport io.intino.konos.alexandria.activity.model.Mold;\nimport io.intino.konos.alexandria.activity.model.Toolbar;\nimport io.intino.konos.alexandria.activity.model.catalog.arrangement.Arrangement;\nimport io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping;\nimport io.intino.konos.alexandria.activity.model.catalog.views.DisplayView;\nimport io.intino.konos.alexandria.activity.model.catalog.views.GridView;\nimport io.intino.konos.alexandria.activity.model.catalog.views.ListView;\nimport io.intino.konos.alexandria.activity.model.catalog.views.MapView;\nimport io.intino.konos.alexandria.activity.model.toolbar.*;\nimport io.intino.tara.magritte.Concept;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Catalog extends AlexandriaCatalogDisplay<")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Catalog(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildCatalog(box));\n\t}\n\n\tprivate static Catalog buildCatalog(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tCatalog catalog = (Catalog) new Catalog()\n\t\t\t//.onScopeChange((scope, username) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Events.scopeChange(box, scope, username))\n\t\t\t.objectsLoader((scope, condition, username) -> (java.util.List<Object>)(Object) ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Source.objects(box, scope, condition, username))\n\t\t\t.rootObjectLoader((objectList, username) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Source.rootObject(box, (java.util.List<")).add(mark("type")).add(literal(">)(Object)objectList, username))\n\t\t\t.defaultObjectLoader((id, username) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Source.defaultObject(box, id, username))")).add(expression().add(literal("\n")).add(literal("\t\t\t")).add(mark("groupingselection", "empty")).add(literal(".clusterManager((element, grouping, group, username) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Arrangements.createGroup(box, element, grouping, group, username))"))).add(expression().add(literal("\n")).add(literal("\t\t\t")).add(mark("toolbar", "empty")).add(literal(".toolbar(buildToolbar(box))"))).add(expression().add(literal("\n")).add(literal("\t\t\t.name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal("\n")).add(literal("\t\t\t.label(\"")).add(mark("label")).add(literal("\")"))).add(literal("\n\t\t\t.objectLoader((id, username) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Source.object(box, id, username))\n\t\t\t.objectIdLoader((object) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Source.objectId(box, (")).add(mark("type")).add(literal(")object))\n\t\t\t.objectNameLoader((object) -> ")).add(mark("name", "firstUpperCase")).add(literal("Catalog.Source.objectName(box, (")).add(mark("type")).add(literal(") object));\n\t\tbuildViews(box).forEach(v -> catalog.add(v));\n\t\tbuildArrangements(box).forEach(a -> catalog.add(a));\n\t\treturn catalog;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static java.util.List<AbstractView> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<AbstractView> result = new ArrayList<>();\n\t\t")).add(mark("view", "add").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n\n\tprivate static java.util.List<Arrangement> buildArrangements(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<Arrangement> arrangements = new ArrayList<>();\n\t\t")).add(mark("arrangement", "add").multiple("\n")).add(literal("\n\t\treturn arrangements;\n\t}\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "toolbar"))).add(literal("private static io.intino.konos.alexandria.activity.model.Toolbar buildToolbar(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\tio.intino.konos.alexandria.activity.model.Toolbar toolbar = new Toolbar();\n\ttoolbar.canSearch(")).add(mark("canSearch")).add(literal(");\n\t")).add(mark("operation").multiple("\n")).add(literal("\n\treturn toolbar;\n}")),
			rule().add((condition("type", "operation & download")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Download().execute((element, option, username) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Toolbar.download(box, element, option, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\""))).add(literal("));")),
			rule().add((condition("type", "operation & export")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Export().execute((element, from, to, username) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Toolbar.export(box, element, from, to, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & openDialog")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.OpenDialog().execute((element, from, to, username) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Toolbar.openDialog(box, element, from, to, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & task")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Task().execute((element, from, to, username) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Toolbar.task(box, element, from, to, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & downloadselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.DownloadSelection().execute((element, option, selection, username) -> ")).add(mark("catalog", "firstUpperCase")).add(literal("Catalog.Toolbar.downloadSelection(box, element, option, (java.util.List<")).add(mark("type")).add(literal(">)(Object)selection, username)).name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & exportselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.ExportSelection().execute((element, from, to, selection, username) -> ")).add(mark("catalog", "firstUpperCase")).add(literal("Catalog.Toolbar.exportSelection(box, element, from, to, (java.util.List<")).add(mark("type")).add(literal(">)(Object)selection, username)).name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & groupingselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.GroupingSelection()")).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\"))"))).add(literal(";")),
			rule().add((condition("type", "operation & taskselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.TaskSelection().execute((element, option, selection, username) -> ")).add(mark("catalog", "firstUpperCase")).add(literal("Catalog.Toolbar.removeElements(box, element, option, (java.util.List<")).add(mark("type")).add(literal(">)(Object)selection, username)).name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "listview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.ListView()")).add(expression().add(literal(".width(")).add(mark("width")).add(literal(")"))).add(literal(".mold((io.intino.konos.alexandria.activity.model.Mold) ")).add(mark("package")).add(literal(".displays.ElementDisplays.displayFor(box, \"")).add(mark("mold")).add(literal("\").element()).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "gridview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.GridView()")).add(expression().add(literal(".width(")).add(mark("width")).add(literal(")"))).add(literal(".mold((io.intino.konos.alexandria.activity.model.Mold) ")).add(mark("package")).add(literal(".displays.ElementDisplays.displayFor(box, \"")).add(mark("mold")).add(literal("\").element()).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "displayview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.DisplayView()")).add(expression().add(literal(".hideNavigator(")).add(mark("hideNavigator")).add(literal(")"))).add(literal(".displayLoader((context, loadingListener, instantListener, username) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Views.")).add(mark("display", "firstLowerCase")).add(literal("(box, (io.intino.tara.magritte.Concept)context, loadingListener, instantListener, username)).name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\"));")),
			rule().add((condition("type", "mapview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.MapView().center(new io.intino.konos.alexandria.activity.model.catalog.views.MapView.Center()")).add(expression().add(literal(".latitude(")).add(mark("latitude")).add(literal(")"))).add(expression().add(literal(".longitude(")).add(mark("longitude")).add(literal(")"))).add(literal(")")).add(expression().add(literal(".")).add(mark("zoom"))).add(literal(".mold((Mold) ElementDisplays.displayFor(box, \"testInfrastructureMold\").element()).name(\"v4\").label(\"Mapa\"));")),
			rule().add((condition("type", "sorting")), (condition("trigger", "add"))).add(literal("arrangements.add(new io.intino.konos.alexandria.activity.model.catalog.arrangement.Sorting().comparator((object1, object2) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Arrangements.")).add(mark("name", "FirstLowerCase")).add(literal("Comparator((")).add(mark("type")).add(literal(")object1, (")).add(mark("type")).add(literal(")object2)).name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\"));")),
			rule().add((condition("type", "grouping")), (condition("trigger", "add"))).add(literal("arrangements.add(new io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping()")).add(expression().add(literal(".histogram(io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping.Histogram.")).add(mark("histogram")).add(literal(")"))).add(literal(".groups((objects, username) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal("Catalog.Arrangements.")).add(mark("name", "FirstLowerCase")).add(literal("(box, (List<")).add(mark("type")).add(literal(">)(Object)objects, username)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("trigger", "zoom"))).add(literal("zoom(new io.intino.konos.alexandria.activity.model.catalog.views.MapView.Zoom()")).add(expression().add(literal(".defaultZoom(")).add(mark("default")).add(literal(")"))).add(expression().add(literal(".min(")).add(mark("min")).add(literal(")"))).add(expression().add(literal(".max(")).add(mark("max")).add(literal(")"))).add(literal(")"))
		);
		return this;
	}
}