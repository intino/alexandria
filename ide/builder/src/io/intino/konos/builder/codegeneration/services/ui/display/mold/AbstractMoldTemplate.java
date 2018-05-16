package io.intino.konos.builder.codegeneration.services.ui.display.mold;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractMoldTemplate extends Template {

	protected AbstractMoldTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractMoldTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "mold"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.ui.displays.AlexandriaMold;\nimport io.intino.konos.alexandria.ui.model.Mold;\nimport io.intino.konos.alexandria.ui.model.mold.Block;\nimport io.intino.konos.alexandria.ui.model.mold.Stamp;\nimport io.intino.konos.alexandria.ui.model.mold.stamps.*;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" extends AlexandriaMold<")).add(mark("name", "FirstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildMold(box));\n\t}\n\n\tpublic Mold buildMold(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tMold mold = new Mold()")).add(mark("block", "definition").multiple("\n")).add(literal(";\n\t\tmold.type(\"")).add(mark("name", "camelCaseToSnakeCase")).add(literal("\");\n\t\treturn mold;\n\t}\n}")),
			rule().add((condition("type", "block")), (condition("trigger", "definition"))).add(literal(".add(new Block()")).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(mark("expanded"))).add(expression().add(mark("layout").multiple(""))).add(expression().add(mark("height"))).add(expression().add(mark("width"))).add(expression().add(mark("hidden"))).add(expression().add(mark("hiddenIfMobile"))).add(expression().add(literal(".style(\"")).add(mark("blockStyle")).add(literal("\")"))).add(expression().add(mark("blockClassName"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("stamp").multiple("\n\t\t"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("block", "definition").multiple("\n"))).add(literal(")")),
			rule().add((condition("trigger", "height"))).add(literal(".height(")).add(mark("value")).add(literal(")")),
			rule().add((condition("trigger", "width"))).add(literal(".width(")).add(mark("value")).add(literal(")")),
			rule().add((condition("attribute", "HiddenEnabled")), (condition("trigger", "hidden"))).add(literal(".hidden((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Blocks.")).add(mark("name", "FirstUpperCase")).add(literal(".hidden(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "hidden"))),
			rule().add((condition("trigger", "hiddenIfMobile"))).add(literal(".hiddenIfMobile(")).add(mark("value")).add(literal(")")),
			rule().add((condition("trigger", "layout"))).add(literal(".add(Block.Layout.")).add(mark("value", "FirstUpperCase")).add(literal(")")),
			rule().add((condition("type", "stamp & location"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.Location()")).add(expression().add(mark("icon"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & breadcrumbs"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.Breadcrumbs()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & cardWallet"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.CardWallet()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & map"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.Map().zoom(")).add(mark("zoom")).add(literal(").latitude(")).add(mark("latitude")).add(literal(").longitude(")).add(mark("longitude")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & cataloglink"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.CatalogLink().catalog((io.intino.konos.alexandria.ui.model.Catalog) Displays.displayFor(box, \"")).add(mark("catalog")).add(literal("\").element())")).add(expression().add(literal(".filter(")).add(mark("filter")).add(literal(")"))).add(expression().add(literal(".itemLoader(")).add(mark("itemLoader")).add(literal(")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & embeddedCatalog"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.EmbeddedCatalog().catalogDisplayBuilder((session) -> (io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog) Displays.displayFor(box, \"")).add(mark("catalog")).add(literal("\")).catalog((io.intino.konos.alexandria.ui.model.Catalog) Displays.displayFor(box, \"")).add(mark("catalog")).add(literal("\").element()).views(java.util.Arrays.asList(")).add(mark("view", "quoted").multiple(", ")).add(literal("))")).add(expression().add(literal(".maxItems(")).add(mark("embeddedCatalogMaxItems")).add(literal(")"))).add(expression().add(literal(".filter(")).add(mark("catalogFilter")).add(literal(")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & embeddedDisplay"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.EmbeddedDisplay().displayType(\"")).add(mark("displayType")).add(literal("\").displayBuilder(")).add(mark("displayBuilder")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & embeddedDialog"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.EmbeddedDialog().dialogType(\"")).add(mark("dialogType")).add(literal("\").dialogBuilder(")).add(mark("embeddedDialogBuilder")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & temporalCatalogRange"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.TemporalCatalogRange()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & temporalCatalogRangeNavigator"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.TemporalCatalogRangeNavigator()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & temporalCatalogTime"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.TemporalCatalogTime()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & temporalCatalogTimeNavigator"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.TemporalCatalogTimeNavigator()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & highlight"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.Highlight()")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & itemLinks"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.ItemLinks().title(")).add(mark("title")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & polymerIcon"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.icons.AlexandriaIcon().title(")).add(mark("title")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & resourceIcon"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.icons.ResourceIcon().title(")).add(mark("title")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & base64Icon"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.icons.Base64Icon().title(")).add(mark("title")).add(literal(")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & rating"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.Rating().max(")).add(mark("ratingMax")).add(literal(")")).add(expression().add(literal(".ratingIcon(")).add(mark("ratingIcon", "quoted")).add(literal(")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & picture"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.Picture()")).add(expression().add(literal(".defaultPicture(\"")).add(mark("defaultPicture")).add(literal("\")"))).add(expression().add(literal(".avatarProperties(")).add(mark("avatarProperties")).add(literal(")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & openDialogOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.OpenDialogOperation()")).add(expression().add(mark("width"))).add(literal(".dialogType(\"")).add(mark("dialogType")).add(literal("\").dialogBuilder(")).add(mark("dialogBuilder")).add(literal(").mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & openExternalDialogOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.OpenExternalDialogOperation()")).add(expression().add(mark("width"))).add(literal(".dialogPathBuilder(")).add(mark("dialogPathBuilder")).add(literal(").dialogTitleBuilder(")).add(mark("dialogTitleBuilder")).add(literal(").mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & openCatalogOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.OpenCatalogOperation()")).add(expression().add(mark("width"))).add(literal(".catalogDisplayBuilder((session) -> (io.intino.konos.alexandria.ui.displays.AlexandriaAbstractCatalog) Displays.displayFor(box, \"")).add(mark("catalog")).add(literal("\")).catalog((io.intino.konos.alexandria.ui.model.Catalog) Displays.displayFor(box, \"")).add(mark("catalog")).add(literal("\").element()).views(java.util.Arrays.asList(")).add(mark("view", "quoted").multiple(", ")).add(literal("))")).add(expression().add(literal(".filter(")).add(mark("catalogFilter")).add(literal(")"))).add(literal(".position(\"")).add(mark("position")).add(literal("\").selection(\"")).add(mark("selection")).add(literal("\")")).add(expression().add(literal(".execution(")).add(mark("openCatalogOperationExecution")).add(literal(")"))).add(literal(".mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & downloadOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.DownloadOperation()")).add(expression().add(literal(".options(java.util.Arrays.asList(")).add(mark("options", "quoted").multiple(", ")).add(literal("))"))).add(literal(".execution(")).add(mark("downloadexecution")).add(literal(").mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & previewOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.PreviewOperation().preview(")).add(mark("previewexecution")).add(literal(").mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & taskOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.TaskOperation().execution(")).add(mark("taskexecution")).add(literal(")")).add(expression().add(literal(".confirmText(\"")).add(mark("confirmText")).add(literal("\")"))).add(literal(".mode(\"")).add(mark("mode")).add(literal("\")")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp & exportOperation"))).add(literal(".add(new io.intino.konos.alexandria.ui.model.mold.stamps.operations.ExportOperation()\n\t")).add(expression().add(literal(".from(java.time.Instant.ofEpochMilli(")).add(mark("from")).add(literal("L))"))).add(literal("\n\t")).add(expression().add(literal(".to(java.time.Instant.ofEpochMilli(")).add(mark("to")).add(literal("L))"))).add(literal("\n\t")).add(expression().add(literal(".options(java.util.Arrays.asList(")).add(mark("options", "quoted").multiple(", ")).add(literal("))"))).add(literal("\n\t.execution(")).add(mark("exportexecution")).add(literal(")\n\t")).add(expression().add(literal(".alexandriaIcon(\"")).add(mark("alexandriaIcon")).add(literal("\")"))).add(literal("\n\t.mode(\"")).add(mark("mode")).add(literal("\")")).add(mark("common")).add(literal(")")),
			rule().add((condition("type", "stamp"))).add(literal(".add(new ")).add(mark("type", "FirstUpperCase")).add(literal("()")).add(mark("common")).add(literal(")")),
			rule().add((condition("trigger", "common"))).add(expression().add(literal(".name(\"")).add(mark("name")).add(literal("\")"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(expression().add(mark("labelLoader"))).add(expression().add(mark("height"))).add(expression().add(literal("\n")).add(literal(".defaultStyle(\"")).add(mark("defaultStyle")).add(literal("\")"))).add(expression().add(literal(".suffix(\"")).add(mark("suffix")).add(literal("\")"))).add(expression().add(literal("\n")).add(literal(".layout(Stamp.Layout.")).add(mark("layout")).add(literal(")"))).add(expression().add(literal(".color(")).add(mark("color")).add(literal(")"))).add(expression().add(mark("editable"))).add(expression().add(literal("\n")).add(mark("style"))).add(expression().add(literal("\n")).add(mark("className"))).add(expression().add(literal("\n")).add(mark("valueMethod"))),
			rule().add((condition("trigger", "labelLoader"))).add(literal(".labelLoader((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "firstUpperCase")).add(literal(".label(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "style"))).add(literal(".style((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "firstUpperCase")).add(literal(".style(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "blockClassName"))).add(literal(".className((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Blocks.")).add(mark("name", "firstUpperCase")).add(literal(".className(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "className"))).add(literal(".className((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "firstUpperCase")).add(literal(".className(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "catalogFilter"))).add(literal("(element, source, target, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".filter(box, element, (")).add(mark("moldClass")).add(literal(") source, target, session)")),
			rule().add((condition("trigger", "displayBuilder"))).add(literal("(name, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".buildDisplay(box, name, session)")),
			rule().add((condition("trigger", "embeddedDialogBuilder"))).add(literal("(name, session) -> ")).add(mark("package", "validPackage")).add(literal(".dialogs.Dialogs.dialogFor(box, \"")).add(mark("dialog")).add(literal("\")")),
			rule().add((condition("trigger", "title"))).add(literal("(object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".title(box, (")).add(mark("moldClass")).add(literal(") object, session)")),
			rule().add((condition("trigger", "dialogBuilder"))).add(literal("(object, session) -> {\n\tio.intino.konos.alexandria.ui.displays.AlexandriaDialog dialog = ")).add(mark("package", "validPackage")).add(literal(".dialogs.Dialogs.dialogFor(box, \"")).add(mark("dialog")).add(literal("\");\n\tdialog.target(object);\n\treturn dialog;\n}")),
			rule().add((condition("trigger", "dialogPathBuilder"))).add(literal("(object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".dialogPath(box, (")).add(mark("moldClass")).add(literal(") object, session)")),
			rule().add((condition("trigger", "dialogTitleBuilder"))).add(literal("(object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".dialogTitle(box, (")).add(mark("moldClass")).add(literal(") object, session)")),
			rule().add((condition("trigger", "exportExecution"))).add(literal("(object, from, to, option, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".execute(box, (")).add(mark("moldClass")).add(literal(") object, from, to, option, session)")),
			rule().add((condition("trigger", "downloadExecution"))).add(literal("(object, option, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".execute(box, (")).add(mark("moldClass")).add(literal(") object, option, session)")),
			rule().add((condition("trigger", "previewExecution"))).add(literal("(object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".preview(box, (")).add(mark("moldClass")).add(literal(") object, session)")),
			rule().add((condition("trigger", "taskExecution"))).add(literal("(object, selfId, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".execute(box, (")).add(mark("moldClass")).add(literal(") object, selfId, session)")),
			rule().add((condition("trigger", "openCatalogOperationExecution"))).add(literal("(object, selection, selfId, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".execute(box, (")).add(mark("moldClass")).add(literal(") object, selection, selfId, session)")),
			rule().add((condition("trigger", "filter"))).add(literal("(source, target, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".filter(box, (")).add(mark("moldClass")).add(literal(") source, target, session)")),
			rule().add((condition("trigger", "itemLoader"))).add(literal("(source, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".item(box, (")).add(mark("moldClass")).add(literal(") source, session)")),
			rule().add((condition("trigger", "editable"))).add(literal(".changeEvent((object, value, selfId, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".onChange(box, (")).add(mark("moldClass")).add(literal(") object, value, selfId, session))\n.validateEvent((object, value, selfId, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".onValidate(box, (")).add(mark("moldClass")).add(literal(") object, value, selfId, session))")),
			rule().add((condition("trigger", "valueMethod"))).add(literal(".value((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".value(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "icon"))).add(literal(".icon((object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".icon(box, (")).add(mark("moldClass")).add(literal(") object, session))")),
			rule().add((condition("trigger", "color"))).add(literal("(object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".color(box, (")).add(mark("moldClass")).add(literal(") object, session)")),
			rule().add((condition("trigger", "avatarProperties"))).add(literal("(object, session) -> ")).add(mark("mold", "FirstUpperCase")).add(literal(".Stamps.")).add(mark("name", "FirstUpperCase")).add(literal(".avatarProperties(box, (")).add(mark("moldClass")).add(literal(") object, session)")),
			rule().add((condition("trigger", "expanded"))).add(literal(".expanded(")).add(mark("value")).add(literal(")")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}