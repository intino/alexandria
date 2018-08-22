package io.intino.konos.builder.codegeneration.services.ui.display.catalog;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class CatalogSkeletonTemplate extends Template {

	protected CatalogSkeletonTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new CatalogSkeletonTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "catalog"))).add(literal("package ")).add(mark("package")).add(literal(".ui.displays;\n\nimport io.intino.konos.alexandria.ui.Resource;\nimport io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.ui.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.ui.model.Catalog;\nimport io.intino.konos.alexandria.ui.model.Element;\nimport io.intino.konos.alexandria.ui.model.catalog.Scope;\nimport io.intino.konos.alexandria.ui.model.catalog.arrangement.Group;\nimport io.intino.konos.alexandria.ui.model.toolbar.TaskSelection;\nimport io.intino.konos.alexandria.ui.services.push.UISession;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.io.ByteArrayInputStream;\nimport java.io.InputStream;\nimport java.time.Instant;\nimport java.util.List;\nimport java.util.function.Consumer;\n\nimport static java.util.Collections.emptyList;\nimport static java.util.stream.Collectors.toList;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("range")).add(literal("\n")).add(literal("\t"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("temporalFilter")).add(literal("\n")).add(literal("\t"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tpublic static class Source {\n\t\tpublic static java.util.List<")).add(mark("type")).add(literal("> ")).add(mark("type", "shortType", "firstLowerCase")).add(literal("List(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.catalog.Scope scope, String condition")).add(expression().add(literal(", ")).add(mark("mode", "timerange")).add(literal(" range"))).add(literal(", UISession session) {\n\t\t\treturn java.util.Collections.emptyList();//TODO\n\t\t}\n\n\t\tpublic static ")).add(mark("type")).add(literal(" ")).add(mark("type", "shortType", "firstLowerCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String id, UISession session) {\n\t\t\treturn null;//TODO\n\t\t}\n\t\t")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("hasMagazineView")).add(literal("\n")).add(literal("\t\t"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("range", "created")).add(literal("\n")).add(literal("\t\t"))).add(expression().add(literal("\n")).add(literal("\t\t\t")).add(mark("hasCustomItemsArrivalMessage")).add(literal("\n")).add(literal("\t\t"))).add(literal("\n\t\tpublic static String ")).add(mark("type", "shortType", "firstLowerCase")).add(literal("Id(")).add(mark("box", "firstUpperCase")).add(literal("Box box, ")).add(mark("type")).add(literal(" ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(") {\n\t\t\t//return ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(".core$().id();\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic static String ")).add(mark("type", "shortType", "firstLowerCase")).add(literal("Name(")).add(mark("box", "firstUpperCase")).add(literal("Box box, ")).add(mark("type")).add(literal(" ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(") {\n\t\t\t//return ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(".name$();\n\t\t\treturn null;\n\t\t}\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("view")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tpublic static class Events {")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("event")).add(literal("\n")).add(literal("\t"))).add(literal("}\n\n\n\tpublic static class Arrangements {")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("arrangement", "method").multiple("\n\n"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t\t")).add(mark("groupingSelection"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t\t")).add(mark("hasArrangements")).add(literal("\n")).add(literal("\t"))).add(literal("}\n}")),
			rule().add((condition("trigger", "created"))).add(literal("public static java.time.Instant ")).add(mark("type", "shortType", "FirstLowerCase")).add(literal("Created(")).add(mark("box")).add(literal("Box box, ")).add(mark("type")).add(literal(" object) {\n\t//return ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(".created();\n\treturn null;//TODO\n}")),
			rule().add((condition("trigger", "range"))).add(literal("public static class Temporal {\n\tpublic static io.intino.konos.alexandria.ui.model.TimeRange range(")).add(mark("box", "firstUpperCase")).add(literal("Box box, UISession session) {\n\t\treturn null;\n\t}\n}")),
			rule().add((condition("trigger", "temporalFilter"))).add(literal("public static class TemporalFilter {\n\t")).add(mark("temporalFilterEnabled")).add(literal("\n\t")).add(mark("temporalFilterVisible")).add(literal("\n}")),
			rule().add((condition("attribute", "always")), (condition("trigger", "temporalFilterEnabled"))),
			rule().add((condition("attribute", "conditional")), (condition("trigger", "temporalFilterEnabled"))).add(literal("public static boolean enable(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.TemporalCatalog catalog, io.intino.konos.alexandria.ui.model.catalog.Scope scope, UISession session) {\n\treturn true;\n}")),
			rule().add((condition("attribute", "never")), (condition("trigger", "temporalFilterEnabled"))),
			rule().add((condition("attribute", "always")), (condition("trigger", "temporalFilterVisible"))),
			rule().add((condition("attribute", "conditional")), (condition("trigger", "temporalFilterVisible"))).add(literal("public static boolean visible(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.TemporalCatalog catalog, io.intino.konos.alexandria.ui.model.catalog.Scope scope, UISession session) {\n\treturn true;\n}")),
			rule().add((condition("attribute", "never")), (condition("trigger", "temporalFilterVisible"))),
			rule().add((condition("trigger", "timerange"))).add(literal("io.intino.konos.alexandria.ui.model.TimeRange")),
			rule().add((condition("trigger", "hasMagazineView"))).add(literal("public static ")).add(mark("type")).add(literal(" root")).add(mark("type", "shortType", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, java.util.List<")).add(mark("type")).add(literal("> objects")).add(expression().add(literal(", ")).add(mark("mode", "timerange")).add(literal(" range"))).add(literal(", UISession session) {\n\treturn null;\n}\n\npublic static ")).add(mark("type")).add(literal(" default")).add(mark("type", "shortType", "firstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String id")).add(expression().add(literal(", ")).add(mark("mode", "timerange")).add(literal(" range"))).add(literal(", UISession session) {\n\treturn null;\n}")),
			rule().add((condition("trigger", "hasCustomItemsArrivalMessage"))).add(literal("public static String itemsArrivalMessage(")).add(mark("box", "firstUpperCase")).add(literal("Box box, int count, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("trigger", "groupingSelection"))).add(literal("public static void createGroup(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, String grouping, io.intino.konos.alexandria.ui.model.catalog.arrangement.Group group, UISession session) {\n\n}")),
			rule().add((condition("trigger", "hasArrangements"))).add(literal("public static io.intino.konos.alexandria.ui.model.Catalog.ArrangementFilterer filterer(")).add(mark("box", "firstUpperCase")).add(literal("Box box, UISession session) {\n\treturn null;\n}")),
			rule().add((condition("type", "openPanel"))).add(mark("breadcrumbs")),
			rule().add((condition("trigger", "breadcrumbs"))).add(literal("public static io.intino.konos.alexandria.ui.model.mold.stamps.Tree onOpenPanelBreadcrumbs(")).add(mark("box", "firstUpperCase")).add(literal("Box box, ")).add(mark("type")).add(literal(" ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn null; //return breadcrumbs\n}")),
			rule().add((condition("type", "openCatalog"))).add(mark("openCatalogLoader")).add(literal("\n")).add(mark("openCatalogFilter")),
			rule().add((condition("trigger", "openCatalogLoader"))).add(literal("public static String onOpenCatalog(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, ")).add(mark("type")).add(literal(" ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(", UISession session) {\n\treturn null; //return item id to show when catalog is opened\n}")),
			rule().add((condition("trigger", "openCatalogFilter"))).add(literal("public static boolean onOpenCatalogFilter(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, ")).add(mark("type")).add(literal(" ")).add(mark("type", "shortType", "firstLowerCase")).add(literal(", Object item, UISession session) {\n\treturn true;\n}")),
			rule().add((condition("trigger", "toolbar"))).add(literal("public static class Toolbar {\n\t")).add(mark("operation").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "arrangement & grouping"))).add(literal("public static java.util.List<io.intino.konos.alexandria.ui.model.catalog.arrangement.Group> ")).add(mark("name", "firstLowerCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, java.util.List<")).add(mark("type")).add(literal("> items, UISession session) {\n\treturn emptyList(); //TODO\n}")),
			rule().add((condition("type", "arrangement & sorting"))).add(literal("public static int ")).add(mark("name", "firstLowerCase")).add(literal("Comparator(")).add(mark("type")).add(literal(" item1, ")).add(mark("type")).add(literal(" item2) {\n\treturn 0; //TODO\n}"))
		);
		return this;
	}
}