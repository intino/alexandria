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
			rule().add((condition("type", "catalog & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaCatalog;\nimport io.intino.konos.alexandria.activity.model.AbstractView;\nimport io.intino.konos.alexandria.activity.model.Catalog;\nimport io.intino.konos.alexandria.activity.model.TemporalCatalog;\nimport io.intino.konos.alexandria.activity.model.Mold;\nimport io.intino.konos.alexandria.activity.model.Toolbar;\nimport io.intino.konos.alexandria.activity.model.catalog.arrangement.Arrangement;\nimport io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping;\nimport io.intino.konos.alexandria.activity.model.catalog.views.DisplayView;\nimport io.intino.konos.alexandria.activity.model.catalog.events.OnClickRecord;\nimport io.intino.konos.alexandria.activity.model.catalog.views.GridView;\nimport io.intino.konos.alexandria.activity.model.catalog.views.ListView;\nimport io.intino.konos.alexandria.activity.model.catalog.views.MapView;\nimport io.intino.konos.alexandria.activity.model.TimeScale;\nimport io.intino.konos.alexandria.activity.model.toolbar.*;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" extends io.intino.konos.alexandria.activity.displays.Alexandria")).add(expression().add(literal("Temporal")).add(mark("mode"))).add(literal("Catalog<")).add(mark("name", "FirstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildCatalog(box));\n\t}\n\n\tprivate static ")).add(expression().add(literal("io.intino.konos.alexandria.activity.model.Temporal")).add(mark("mode", "empty"))).add(literal("Catalog buildCatalog(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tio.intino.konos.alexandria.activity.model.")).add(expression().add(literal("Temporal")).add(mark("mode", "empty"))).add(literal("Catalog catalog = new io.intino.konos.alexandria.activity.model.")).add(expression().add(literal("Temporal")).add(mark("mode", "empty"))).add(literal("Catalog();\n\t\tcatalog.objectsLoader((scope, condition")).add(expression().add(literal(", ")).add(mark("mode", "timerange"))).add(literal(", session) -> (java.util.List<Object>)(Object) ")).add(mark("name", "firstUpperCase")).add(literal(".Source.")).add(mark("type", "shortType", "firstLowerCase")).add(literal("List(box, scope, condition")).add(expression().add(literal(", ")).add(mark("mode", "timerange"))).add(literal(", session));")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("mode", "setMode"))).add(expression().add(literal("\n")).add(literal("        ")).add(mark("temporalFilter"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("range"))).add(expression().add(literal("\n")).add(literal("\t\tcatalog.scales(new ArrayList<TimeScale>() {{ ")).add(mark("scale", "scaleAdd").multiple("; ")).add(literal(" }});"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("hasMagazineView"))).add(expression().add(literal("\n")).add(literal("\t\tcatalog.events(new io.intino.konos.alexandria.activity.model.catalog.Events().onClickRecord(new OnClickRecord().")).add(mark("event")).add(literal("));"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("hasGroupings"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("groupingselection"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("toolbar", "empty")).add(literal("catalog.toolbar(buildToolbar(box));"))).add(expression().add(literal("\n")).add(literal("\t\tcatalog.name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(";\n\t\tcatalog.objectLoader((id, session) -> ")).add(mark("name", "firstUpperCase")).add(literal(".Source.")).add(mark("type", "shortType", "firstLowerCase")).add(literal("(box, id, session))\n\t\t\t.objectIdLoader(object -> ")).add(mark("name", "firstUpperCase")).add(literal(".Source.")).add(mark("type", "shortType", "firstLowerCase")).add(literal("Id(box, (")).add(mark("type")).add(literal(")object))\n\t\t\t.objectNameLoader(object -> ")).add(mark("name", "firstUpperCase")).add(literal(".Source.")).add(mark("type", "shortType", "firstLowerCase")).add(literal("Name(box, (")).add(mark("type")).add(literal(") object));\n\t\tbuildViews(box).forEach(v -> catalog.add(v));\n\t\tbuildArrangements(box).forEach(a -> catalog.add(a));\n\t\treturn catalog;\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate static java.util.List<AbstractView> buildViews(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<AbstractView> result = new ArrayList<>();\n\t\t")).add(mark("view", "add").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n\n\tprivate static java.util.List<Arrangement> buildArrangements(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<Arrangement> arrangements = new ArrayList<>();\n\t\t")).add(mark("arrangement", "add").multiple("\n")).add(literal("\n\t\treturn arrangements;\n\t}\n}")),
			rule().add((condition("trigger", "empty"))),
			rule().add((condition("trigger", "setMode"))).add(literal("catalog.type(TemporalCatalog.Type.")).add(mark("value", "firstUpperCase")).add(literal(");")),
			rule().add((condition("trigger", "temporalFilter"))).add(literal("catalog.temporalFilter(new io.intino.konos.alexandria.activity.model.catalog.TemporalFilter().enabledLoader(")).add(mark("temporalFilterEnabled")).add(literal(").visibilityLoader(")).add(mark("temporalFilterVisible")).add(literal(").layout(\"")).add(mark("temporalFilterLayout")).add(literal("\"));")),
			rule().add((condition("attribute", "conditional")), (condition("trigger", "temporalFilterEnabled"))).add(literal("(c, scope, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".TemporalFilter.enable(box, c, scope, session)")),
			rule().add((condition("attribute", "always")), (condition("trigger", "temporalFilterEnabled"))).add(literal("(c, scope, session) -> true")),
			rule().add((condition("attribute", "never")), (condition("trigger", "temporalFilterEnabled"))).add(literal("(c, scope, session) -> false")),
			rule().add((condition("attribute", "conditional")), (condition("trigger", "temporalFilterVisible"))).add(literal("(c, scope, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".TemporalFilter.visible(box, c, scope, session)")),
			rule().add((condition("attribute", "always")), (condition("trigger", "temporalFilterVisible"))).add(literal("(c, scope, session) -> true")),
			rule().add((condition("attribute", "never")), (condition("trigger", "temporalFilterVisible"))).add(literal("(c, scope, session) -> false")),
			rule().add((condition("trigger", "range"))).add(literal("catalog.rangeLoader(session -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Temporal.range(box, session));\ncatalog.objectCreatedLoader(object -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Source.")).add(mark("type", "shortType", "firstLowerCase")).add(literal("Created(box, (")).add(mark("type")).add(literal(") object));")),
			rule().add((condition("trigger", "timeRange"))).add(literal("range")),
			rule().add((condition("trigger", "scaleAdd"))).add(literal("add(TimeScale.")).add(mark("value", "FirstUpperCase")).add(literal(");")),
			rule().add((condition("trigger", "groupingSelection"))).add(literal("catalog.clusterManager((element, grouping, group, session) -> ")).add(mark("name", "firstUpperCase")).add(literal(".Arrangements.createGroup(box, element, grouping, group, session));")),
			rule().add((condition("trigger", "hasMagazineView"))).add(literal("catalog.rootObjectLoader((objectList, range, session) -> ")).add(mark("name", "firstUpperCase")).add(literal(".Source.root")).add(mark("type", "shortType", "firstUpperCase")).add(literal("(box, (java.util.List<")).add(mark("type")).add(literal(">)(Object)objectList")).add(expression().add(literal(", ")).add(mark("mode", "timerange"))).add(literal(", session));\ncatalog.defaultObjectLoader((id, range, session) -> ")).add(mark("name", "firstUpperCase")).add(literal(".Source.default")).add(mark("type", "shortType", "firstUpperCase")).add(literal("(box, id")).add(expression().add(literal(", ")).add(mark("mode", "timerange"))).add(literal(", session));")),
			rule().add((condition("trigger", "hasGroupings"))).add(literal("catalog.arrangementFiltererLoader((session) -> ")).add(mark("name", "firstUpperCase")).add(literal(".Arrangements.filterer(box, session));\ncatalog.arrangementHistogramsMode(\"")).add(mark("histogramsMode")).add(literal("\");")),
			rule().add((condition("trigger", "toolbar"))).add(literal("private static io.intino.konos.alexandria.activity.model.Toolbar buildToolbar(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\tio.intino.konos.alexandria.activity.model.Toolbar toolbar = new Toolbar();\n\ttoolbar.canSearch(")).add(mark("canSearch")).add(literal(");\n\t")).add(mark("operation").multiple("\n")).add(literal("\n\treturn toolbar;\n}")),
			rule().add((condition("type", "openDialog")), (condition("trigger", "event"))).add(literal("openDialog(new io.intino.konos.alexandria.activity.model.catalog.events.OpenDialog()")).add(expression().add(literal(".height(")).add(mark("height")).add(literal(")"))).add(expression().add(literal(".width(")).add(mark("width")).add(literal(")"))).add(literal(".dialogType(\"")).add(mark("dialog")).add(literal("\").dialogBuilder((item, session) -> {\n\tio.intino.konos.alexandria.activity.displays.AlexandriaDialog dialog = ")).add(mark("package", "validPackage")).add(literal(".dialogs.Dialogs.dialogFor(box, \"")).add(mark("dialog")).add(literal("\");\n\tdialog.target(item);\n\treturn dialog;\n}))")),
			rule().add((condition("type", "openPanel")), (condition("trigger", "event"))).add(literal("openPanel(new io.intino.konos.alexandria.activity.model.catalog.events.OpenPanel()\n\t")).add(expression().add(literal(".breadcrumbs(")).add(mark("breadCrumbs")).add(literal(")"))).add(literal("\n\t.panel((io.intino.konos.alexandria.activity.model.Panel) Displays.displayFor(box, \"")).add(mark("panel")).add(literal("\").element()))")),
			rule().add((condition("type", "openCatalog")), (condition("trigger", "event"))).add(literal("openCatalog(new io.intino.konos.alexandria.activity.model.catalog.events.OpenCatalog()\n\t")).add(expression().add(literal(".itemLoader(")).add(mark("openCatalogLoader")).add(literal(")"))).add(literal("\n\t")).add(expression().add(literal(".filter(")).add(mark("openCatalogFilter")).add(literal(")"))).add(literal("\n\t.catalog((io.intino.konos.alexandria.activity.model.Catalog) Displays.displayFor(box, \"")).add(mark("catalog")).add(literal("\").element()))")),
			rule().add((condition("trigger", "breadCrumbs"))).add(literal("(object, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Events.onOpenPanelBreadcrumbs(box, (")).add(mark("type")).add(literal(") object, session)")),
			rule().add((condition("trigger", "openCatalogLoader"))).add(literal("(element, sender, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Events.onOpenCatalog(box, element, (")).add(mark("type")).add(literal(") sender, session)")),
			rule().add((condition("trigger", "openCatalogFilter"))).add(literal("(element, sender, item, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Events.onOpenCatalogFilter(box, element, (")).add(mark("type")).add(literal(") sender, item, session)")),
			rule().add((condition("type", "operation & download")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Download().execute((element, option, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Toolbar.download(box, element, option, session)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\""))).add(literal("));")),
			rule().add((condition("type", "operation & export")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Export().execute((element, from, to, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Toolbar.export(box, element, from, to, session)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & openDialog")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.OpenDialog().dialogType(\"")).add(mark("dialog")).add(literal("\").dialogBuilder((session) -> ")).add(mark("package", "validPackage")).add(literal(".dialogs.Dialogs.dialogFor(box, \"")).add(mark("dialog")).add(literal("\")).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & task")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.Task().execute((element, from, to, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Toolbar.task(box, element, from, to, session)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & downloadselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.DownloadSelection().execute((element, option, selection, session) -> ")).add(mark("catalog", "firstUpperCase")).add(literal(".Toolbar.downloadSelection(box, element, option, (java.util.List<")).add(mark("type")).add(literal(">)(Object)selection, session)).name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & exportselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.ExportSelection().execute((element, from, to, selection, session) -> ")).add(mark("catalog", "firstUpperCase")).add(literal(".Toolbar.exportSelection(box, element, from, to, (java.util.List<")).add(mark("type")).add(literal(">)(Object)selection, session)).name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & groupingselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.GroupingSelection()")).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\"))"))).add(literal(";")),
			rule().add((condition("type", "operation & taskselection")), (condition("trigger", "operation"))).add(literal("toolbar.add(new io.intino.konos.alexandria.activity.model.toolbar.TaskSelection().execute((element, option, selection, session) -> ")).add(mark("catalog", "firstUpperCase")).add(literal(".Toolbar.taskSelection(box, element, option, (java.util.List<")).add(mark("type")).add(literal(">)(Object)selection, session)).name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "listview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.ListView()")).add(expression().add(literal(".noRecordsMessage(\"")).add(mark("noRecordMessage")).add(literal("\")"))).add(expression().add(literal(".width(")).add(mark("width")).add(literal(")"))).add(literal(".mold((io.intino.konos.alexandria.activity.model.Mold) ")).add(mark("package")).add(literal(".displays.Displays.displayFor(box, \"")).add(mark("mold")).add(literal("\").element()).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "gridview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.GridView()")).add(expression().add(literal(".noRecordsMessage(\"")).add(mark("noRecordMessage")).add(literal("\")"))).add(expression().add(literal(".width(")).add(mark("width")).add(literal(")"))).add(literal(".mold((io.intino.konos.alexandria.activity.model.Mold) ")).add(mark("package")).add(literal(".displays.Displays.displayFor(box, \"")).add(mark("mold")).add(literal("\").element()).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "mapview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.MapView()")).add(expression().add(literal(".center(new io.intino.konos.alexandria.activity.model.catalog.views.MapView.Center().latitude(")).add(mark("latitude")).add(literal(").longitude(")).add(mark("longitude")).add(literal("))"))).add(expression().add(literal(".")).add(mark("zoom"))).add(literal(".mold((Mold) Displays.displayFor(box, \"")).add(mark("mold")).add(literal("\").element()).name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\"));")),
			rule().add((condition("type", "magazineview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.MagazineView()")).add(expression().add(literal(".noRecordMessage(\"")).add(mark("noRecordMessage")).add(literal("\")"))).add(expression().add(literal(".width(")).add(mark("width")).add(literal(")"))).add(literal(".mold((Mold) Displays.displayFor(box, \"")).add(mark("mold")).add(literal("\").element()).name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\"));")),
			rule().add((condition("type", "displayview")), (condition("trigger", "add"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.catalog.views.DisplayView()\n\t.scopeManager((display, scope) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Views.")).add(mark("display", "firstLowerCase")).add(literal("Scope(box, display, scope))")).add(expression().add(literal("\n")).add(literal("\t.hideNavigator(")).add(mark("hideNavigator")).add(literal(")"))).add(literal("\n\t.displayLoader((context, loadingListener, instantListener, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Views.")).add(mark("display", "firstLowerCase")).add(literal("(box, (io.intino.konos.alexandria.activity.model.Element)context, loadingListener, instantListener, session))\n\t.name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\"));")),
			rule().add((condition("type", "sorting")), (condition("trigger", "add"))).add(literal("arrangements.add(new io.intino.konos.alexandria.activity.model.catalog.arrangement.Sorting().comparator((object1, object2) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Arrangements.")).add(mark("name", "FirstLowerCase")).add(literal("Comparator((")).add(mark("type")).add(literal(")object1, (")).add(mark("type")).add(literal(")object2)).visible(")).add(mark("visible")).add(literal(").name(\"")).add(mark("name")).add(literal("\").label(\"")).add(mark("label")).add(literal("\"));")),
			rule().add((condition("type", "grouping")), (condition("trigger", "add"))).add(literal("arrangements.add(new io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping()")).add(expression().add(literal(".histogram(io.intino.konos.alexandria.activity.model.catalog.arrangement.Grouping.Histogram.")).add(mark("histogram")).add(literal(")"))).add(literal(".groups((objects, session) -> ")).add(mark("catalog", "FirstUpperCase")).add(literal(".Arrangements.")).add(mark("name", "FirstLowerCase")).add(literal("(box, (List<")).add(mark("type")).add(literal(">)(Object)objects, session)).name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("trigger", "zoom"))).add(literal("zoom(new io.intino.konos.alexandria.activity.model.catalog.views.MapView.Zoom()")).add(expression().add(literal(".defaultZoom(")).add(mark("default")).add(literal(")"))).add(expression().add(literal(".max(")).add(mark("max")).add(literal(")"))).add(expression().add(literal(".min(")).add(mark("min")).add(literal(")"))).add(literal(")"))
		);
		return this;
	}
}