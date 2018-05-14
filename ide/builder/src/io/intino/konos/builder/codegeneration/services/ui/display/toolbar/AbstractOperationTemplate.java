package io.intino.konos.builder.codegeneration.services.ui.display.toolbar;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractOperationTemplate extends Template {

	protected AbstractOperationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractOperationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "operation & download")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.Download().execute((element, options, selfId, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.download(box, (Catalog) element, options, selfId, session)).mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\""))).add(literal("));")),
			rule().add((condition("type", "operation & downloadselection")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.DownloadSelection().execute((element, option, selection, selfId, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.downloadSelection(box, (Catalog) element, option, selfId, (java.util.List<")).add(mark("itemClass")).add(literal(">)(Object) selection, session)).mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & export")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.Export().execute((element, from, to, selfId, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.export(box, (Catalog) element, from, to, selfId, session)).mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & exportselection")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.ExportSelection().execute((element, from, to, selection, selfId, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.exportSelection(box, (Catalog) element, from, to, (java.util.List<")).add(mark("itemClass")).add(literal(">)(Object) selection, selfId, session)).mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & task")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.Task().execute((element, selfId, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.task(box, (Catalog) element, selfId, session))")).add(expression().add(literal(".confirmText(\"")).add(mark("confirmText")).add(literal("\")"))).add(literal(".mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & taskselection")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.TaskSelection().execute((element, option, selection, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.taskSelection(box, (Catalog) element, option, (java.util.List<")).add(mark("itemClass")).add(literal(">)(Object)selection, session))")).add(expression().add(literal(".confirmText(\"")).add(mark("confirmText")).add(literal("\")"))).add(literal(".mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & groupingselection")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.GroupingSelection().mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\"))"))).add(literal(";")),
			rule().add((condition("type", "operation & openDialog")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.OpenDialog().dialogType(\"")).add(mark("dialog")).add(literal("\").dialogBuilder((session) -> ")).add(mark("package", "validPackage")).add(literal(".dialogs.Dialogs.dialogFor(box, \"")).add(mark("dialog")).add(literal("\")).mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\")")).add(expression().add(literal(".title(\"")).add(mark("title")).add(literal("\")"))).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("icon")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "operation & openCatalog")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.OpenCatalog()")).add(expression().add(literal(".filter(")).add(mark("openCatalogOperationFilter")).add(literal(")"))).add(literal(".position(\"")).add(mark("position")).add(literal("\").catalogDisplayBuilder((session) -> (io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog) Displays.displayFor(box, \"")).add(mark("openCatalog")).add(literal("\")).catalog((io.intino.konos.alexandria.ui.model.Catalog) Displays.displayFor(box, \"")).add(mark("openCatalog")).add(literal("\").element()).views(java.util.Arrays.asList(")).add(mark("view", "quoted").multiple(", ")).add(literal(")).width(")).add(mark("width")).add(literal(").height(")).add(mark("height")).add(literal(").mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("type", "operation & opencatalogselection")), (condition("trigger", "operation"))).add(literal("result.add(new io.intino.konos.alexandria.ui.model.toolbar.OpenCatalogSelection().execution((element, selection, openCatalogSelection, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.openCatalogSelection(box, (Catalog) element, (java.util.List<")).add(mark("itemClass")).add(literal(">)(Object)selection, (java.util.List<Object>)openCatalogSelection, session))")).add(expression().add(literal(".filter(")).add(mark("openCatalogSelectionOperationFilter")).add(literal(")"))).add(literal(".position(\"")).add(mark("position")).add(literal("\").selection(\"")).add(mark("selection")).add(literal("\").catalogDisplayBuilder((session) -> (io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog) Displays.displayFor(box, \"")).add(mark("openCatalog")).add(literal("\")).catalog((io.intino.konos.alexandria.ui.model.Catalog) Displays.displayFor(box, \"")).add(mark("openCatalog")).add(literal("\").element()).views(java.util.Arrays.asList(")).add(mark("view", "quoted").multiple(", ")).add(literal(")).width(")).add(mark("width")).add(literal(").height(")).add(mark("height")).add(literal(").mode(\"")).add(mark("mode")).add(literal("\").name(\"")).add(mark("name")).add(literal("\").title(\"")).add(mark("title")).add(literal("\").alexandriaIcon(\"")).add(mark("icon")).add(literal("\"));")),
			rule().add((condition("trigger", "openCatalogOperationFilter"))).add(literal("(context, object, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.openCatalogFilter(box, (")).add(mark("ownerClass")).add(literal(") context, object, session)")),
			rule().add((condition("trigger", "openCatalogSelectionOperationFilter"))).add(literal("(context, selection, object, session) -> ")).add(mark("owner", "FirstUpperCase")).add(literal(".Toolbar.openCatalogSelectionFilter(box, (Catalog) context, (java.util.List<")).add(mark("itemClass")).add(literal(">)(Object)selection, object, session)"))
		);
		return this;
	}
}