package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class CatalogSrcTemplate extends Template {

	protected CatalogSrcTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new CatalogSrcTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "catalog"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.Resource;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.CatalogInstantBlock;\nimport io.intino.konos.alexandria.activity.model.Catalog;\nimport io.intino.konos.alexandria.activity.model.Element;\nimport io.intino.konos.alexandria.activity.model.catalog.Scope;\nimport io.intino.konos.alexandria.activity.model.catalog.arrangement.Group;\nimport io.intino.konos.alexandria.activity.model.toolbar.TaskSelection;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.io.ByteArrayInputStream;\nimport java.io.InputStream;\nimport java.time.Instant;\nimport java.util.List;\nimport java.util.function.Consumer;\n\nimport static java.util.Collections.emptyList;\nimport static java.util.stream.Collectors.toList;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Catalog extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Catalog {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Catalog(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("toolbar")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tpublic static class Source {\n\t\tpublic static java.util.List<")).add(mark("type")).add(literal("> objects(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String condition, String username) {\n\t\t\treturn java.util.Collections.emptyList();//TODO\n\t\t}\n\n\t\tpublic static ")).add(mark("type")).add(literal(" object(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String id, String username) {\n\t\t\treturn null;//TODO\n\t\t}\n\n\t\tpublic static ")).add(mark("type")).add(literal(" rootObject(")).add(mark("box", "firstUpperCase")).add(literal("Box box, java.util.List<")).add(mark("type")).add(literal("> objects, String username) {\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic static ")).add(mark("type")).add(literal(" defaultObject(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String id, String username) {\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic static String objectId(")).add(mark("box", "firstUpperCase")).add(literal("Box box, ")).add(mark("type")).add(literal(" object) {\n\t\t\treturn object.core$().id();\n\t\t}\n\n\t\tpublic static String objectName(")).add(mark("box", "firstUpperCase")).add(literal("Box box, ")).add(mark("type")).add(literal(" object) {\n\t\t\treturn object.name$();\n\t\t}\n\t}\n\n\tpublic static class Events {\n\t\tpublic static void scopeChange(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Scope scope, String username) {\n\t\t}\n\t}\n\n\tpublic static class Views {\n\t\t")).add(mark("view", "method").multiple("\n")).add(literal("\n\t}\n\n\tpublic static class Arrangements {\n\t\t")).add(mark("arrangement", "method").multiple("\n\n")).add(literal("\n\n\t\tpublic static void createGroup(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Catalog infrastructure, String grouping, io.intino.konos.alexandria.activity.model.catalog.arrangement.Group group, String username) {\n\n\t\t}\n\t}\n\n\tprivate static io.intino.konos.alexandria.activity.Resource emptyResource(String label) {\n\t\treturn new io.intino.konos.alexandria.activity.Resource() {\n\t\t\t@Override\n\t\t\tpublic String label() {\n\t\t\t\treturn label;\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic InputStream content() {\n\t\t\t\treturn new java.io.ByteArrayInputStream(new byte[0]);\n\t\t\t}\n\t\t};\n\t}\n}")),
			rule().add((condition("trigger", "toolbar"))).add(literal("public static class Toolbar {\n\tpublic static TaskSelection.Refresh removeElements(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Element element, String option, java.util.List<")).add(mark("type")).add(literal("> selection, String username) {\n\t\treturn null;\n\t}\n\n\t")).add(mark("operation").multiple("\n\n")).add(literal("\n}\n")),
			rule().add((condition("type", "view & displayView")), (condition("trigger", "method"))).add(literal("public static AlexandriaDisplay ")).add(mark("display", "firstLowerCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.tara.magritte.Concept context, Consumer<Boolean> loadingListener, Consumer<CatalogInstantBlock> instantListener, String username) {\n\treturn null;//TODO\n}")),
			rule().add((condition("type", "arrangement & grouping")), (condition("trigger", "method"))).add(literal("public static java.util.List<io.intino.konos.alexandria.activity.model.catalog.arrangement.Group> ")).add(mark("name", "firstLowerCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, java.util.List<")).add(mark("type")).add(literal("> items, String username) {\n\treturn emptyList(); //TODO\n}")),
			rule().add((condition("type", "arrangement & sorting")), (condition("trigger", "method"))).add(literal("public static int ")).add(mark("name", "firstLowerCase")).add(literal("Comparator(")).add(mark("type")).add(literal(" item1, ")).add(mark("type")).add(literal(" item2) {\n\treturn 0; //TODO\n}")),
			rule().add((condition("type", "operation & download")), (condition("trigger", "operation"))).add(literal("public static Resource download(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, String option, String username) {\n\treturn emptyResource(\"\");\n}")),
			rule().add((condition("type", "operation & export")), (condition("trigger", "operation"))).add(literal("public static Resource export(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, java.time.Instant from, java.time.Instant to, String username) {\n\treturn emptyResource(\"\");\n}")),
			rule().add((condition("type", "operation & task")), (condition("trigger", "operation"))).add(literal("public static Resource task(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, java.time.Instant from, java.time.Instant to, String username) {\n\treturn emptyResource(\"\");\n}")),
			rule().add((condition("type", "operation & downloadselection")), (condition("trigger", "operation"))).add(literal("public static Resource downloadSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, String option, java.util.List<")).add(mark("type")).add(literal("> selection, String username) {\n\treturn emptyResource(\"\");\n}")),
			rule().add((condition("type", "operation & exportselection")), (condition("trigger", "operation"))).add(literal("public static Resource exportSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, java.time.Instant from, java.time.Instant to, java.util.List<")).add(mark("type")).add(literal("> selection, String username) {\n\treturn emptyResource(\"\");\n}")),
			rule().add((condition("type", "operation & taskselection")), (condition("trigger", "operation"))).add(literal("public static Resource taskSelection(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, String option, java.util.List<")).add(mark("type")).add(literal("> selection, String username) {\n\treturn emptyResource(\"\");\n}"))
		);
		return this;
	}
}