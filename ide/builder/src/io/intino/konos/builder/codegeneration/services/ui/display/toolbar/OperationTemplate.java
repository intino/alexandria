package io.intino.konos.builder.codegeneration.services.ui.display.toolbar;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class OperationTemplate extends Template {

	protected OperationTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new OperationTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "operation & download"))).add(literal("public static io.intino.konos.alexandria.ui.Resource download(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.")).add(mark("ownerClass")).add(literal(" ")).add(mark("ownerClass", "toLowerCase")).add(literal(", String option, String selfId, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("type", "operation & export"))).add(literal("public static io.intino.konos.alexandria.ui.Resource export(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.")).add(mark("ownerClass")).add(literal(" ")).add(mark("ownerClass", "toLowerCase")).add(literal(", java.time.Instant from, java.time.Instant to, String selfId, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("type", "operation & task"))).add(literal("public static io.intino.konos.alexandria.ui.model.toolbar.ToolbarResult task(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.")).add(mark("ownerClass")).add(literal(" ")).add(mark("ownerClass", "toLowerCase")).add(literal(", String selfId, UISession session) {\n\treturn io.intino.konos.alexandria.ui.model.toolbar.ToolbarResult.none();\n}")),
			rule().add((condition("type", "operation & downloadselection"))).add(literal("public static io.intino.konos.alexandria.ui.Resource downloadSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, String option, java.util.List<")).add(mark("itemClass")).add(literal("> selection, String selfId, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("type", "operation & exportselection"))).add(literal("public static io.intino.konos.alexandria.ui.Resource exportSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, java.time.Instant from, java.time.Instant to, java.util.List<")).add(mark("itemClass")).add(literal("> selection, String selfId, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("type", "operation & taskselection"))).add(literal("public io.intino.konos.alexandria.ui.model.toolbar.ToolbarSelectionResult taskSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, String option, java.util.List<")).add(mark("itemClass")).add(literal("> selection, UISession session) {\n\treturn io.intino.konos.alexandria.ui.model.toolbar.ToolbarSelectionResult.none();\n}")),
			rule().add((condition("type", "operation & openCatalog"))).add(expression().add(mark("openCatalogOperationFilter"))),
			rule().add((condition("type", "operation & openCatalogSelection"))).add(expression().add(mark("openCatalogSelectionOperationFilter"))).add(literal("\n")).add(mark("openCatalogSelectionOperation")),
			rule().add((condition("trigger", "openCatalogOperationFilter"))).add(literal("public static boolean openCatalogFilter(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.")).add(mark("ownerClass")).add(literal(" ")).add(mark("ownerClass", "toLowerCase")).add(literal(", Object object, UISession session) {\n\treturn true;\n}")),
			rule().add((condition("trigger", "openCatalogSelectionOperationFilter"))).add(literal("public static boolean openCatalogSelectionFilter(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.")).add(mark("ownerClass")).add(literal(" ")).add(mark("ownerClass", "toLowerCase")).add(literal(", java.util.List<")).add(mark("itemClass")).add(literal("> selection, Object object, UISession session) {\n\treturn true;\n}")),
			rule().add((condition("trigger", "openCatalogSelectionOperation"))).add(literal("public static io.intino.konos.alexandria.ui.model.toolbar.ToolbarSelectionResult openCatalogSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.")).add(mark("ownerClass")).add(literal(" ")).add(mark("ownerClass", "toLowerCase")).add(literal(", java.util.List<")).add(mark("itemClass")).add(literal("> selection, java.util.List<Object> openCatalogSelection, UISession session) {\n\treturn io.intino.konos.alexandria.ui.model.toolbar.ToolbarSelectionResult.none();\n\n}"))
		);
		return this;
	}
}