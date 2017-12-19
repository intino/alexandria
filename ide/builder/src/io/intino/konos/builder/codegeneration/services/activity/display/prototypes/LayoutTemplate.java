package io.intino.konos.builder.codegeneration.services.activity.display.prototypes;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class LayoutTemplate extends Template {

	protected LayoutTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new LayoutTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "layout & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaElementDisplay;\nimport io.intino.konos.alexandria.activity.displays.Alexandria")).add(mark("mode", "FisrtUpperCase")).add(literal("Layout;\nimport io.intino.konos.alexandria.activity.model.Catalog;\nimport io.intino.konos.alexandria.activity.model.Element;\nimport io.intino.konos.alexandria.activity.model.Layout;\nimport io.intino.konos.alexandria.activity.model.Panel;\nimport io.intino.konos.alexandria.activity.model.layout.ElementOption;\nimport io.intino.konos.alexandria.activity.model.layout.options.Group;\nimport io.intino.konos.alexandria.activity.model.layout.options.Option;\nimport io.intino.konos.alexandria.activity.model.renders.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("Notifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" extends Alexandria")).add(mark("mode", "FirstUpperCase")).add(literal("Layout<")).add(mark("name", "FirstUpperCase")).add(literal("Notifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildLayout(box));\n\t}\n\n\tprivate static Layout buildLayout(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tio.intino.konos.alexandria.activity.model.Layout layout = new io.intino.konos.alexandria.activity.model.Layout();\n\t\t")).add(expression().add(literal("layout.mode(Layout.Mode.")).add(mark("mode")).add(literal(");"))).add(literal("\n\t\tlayout.elementDisplayBuilder(new Layout.ElementDisplayBuilder() {\n\t\t\t@Override\n\t\t\tpublic AlexandriaElementDisplay displayFor(Element element, Object o) {\n\t\t\t\treturn ElementDisplays.displayFor(box, element);\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Object o) {\n\t\t\t\treturn ElementDisplays.displayTypeFor(box, element);\n\t\t\t}\n\t\t});\n\t\tbuildOptions(box).forEach(layout::add);\n\t\treturn layout;\n\t}\n\n\tprivate static List<io.intino.konos.alexandria.activity.model.layout.ElementOption> buildOptions(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<ElementOption> result = new ArrayList<>();\n\t\t")).add(mark("elementOption").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n}")),
			rule().add((condition("type", "elementOption & options")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic static String label(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, java.lang.Object object) {\n\t\treturn null;\n\t}\n\n\tpublic static String icon(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, java.lang.Object object) {\n\t\treturn null;\n\t}\n\n\tpublic static Integer bubble(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.model.Element element, java.lang.Object object) {\n\t\treturn null;\n\t}\n\n\t")).add(mark("render", "method")).add(literal("\n\n\t")).add(mark("elementOption", "class").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "elementOption & group")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("elementOption", "class").multiple("\n")).add(literal("\n\n\t")).add(mark("render", "method")).add(literal("\n}")),
			rule().add((condition("type", "elementOption & option")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("elementOption", "class").multiple("\n")).add(literal("\n\n\t")).add(mark("render", "method")).add(literal("\n}")),
			rule().add((condition("type", "catalogs")), (condition("trigger", "method"))).add(literal("public static boolean filter(io.intino.konos.alexandria.activity.model.Catalog catalog, io.intino.konos.alexandria.activity.model.Element context, Object target, Object object, String username) {\n\treturn true;\n}")),
			rule().add((condition("type", "panels")), (condition("trigger", "method"))).add(literal("public static void object(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Object element, Object object) {\n\n}")),
			rule().add((condition("type", "objects")), (condition("trigger", "method"))).add(literal("public static java.util.List<io.intino.konos.alexandria.activity.model.renders.RenderObjects.Source.Entry> objects(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\treturn java.util.Collections.emptyList();\n}")),
			rule().add((condition("type", "options")), (condition("trigger", "add"))).add(literal(".add(new io.intino.konos.alexandria.activity.model.layout.options.Options().\n\tlabel((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".label(box, element, object))\n\t")).add(expression().add(literal(".icon((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".icon(box, element, object))"))).add(literal("\n\t")).add(expression().add(literal(".bubble((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".bubble(box, element, object))"))).add(literal("\n\t.render(")).add(mark("render")).add(literal("))")),
			rule().add((condition("type", "option & elementOption")), (condition("trigger", "add"))).add(literal(".add(new io.intino.konos.alexandria.activity.model.layout.options.Option().label(\"")).add(mark("label")).add(literal("\").render(")).add(mark("render")).add(literal("))")),
			rule().add((condition("type", "options")), (condition("trigger", "elementOption"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.layout.options.Options().\n\tlabel((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".label(box, element, object))\n\t.icon((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".icon(box, element, object))\n\t.bubble((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".bubble(box, element, object))\n\t.render(")).add(mark("render")).add(literal("));")),
			rule().add((condition("type", "option")), (condition("trigger", "elementOption"))).add(literal("result.add(new io.intino.konos.alexandria.activity.model.layout.options.Option()")).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))).add(literal(");")),
			rule().add((condition("type", "group")), (condition("trigger", "elementOption"))).add(literal("result.add(new Group().")).add(expression().add(literal("label(\"")).add(mark("label")).add(literal("\")"))).add(literal(".mode(Group.Mode.")).add(mark("mode")).add(literal(")")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("elementOption", "add").multiple("\n"))).add(literal(");")),
			rule().add((condition("type", "catalogs")), (condition("trigger", "render"))).add(literal("new io.intino.konos.alexandria.activity.model.renders.RenderCatalogs().catalogs(ElementDisplays.elementsFor(box, Catalog.class, ")).add(mark("catalog", "quoted").multiple(", ")).add(literal("))")).add(expression().add(literal(".filter((catalog, context, target, object, username) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".filter(box, catalog, context, target, object, username))"))),
			rule().add((condition("type", "panels")), (condition("trigger", "render"))).add(literal("new io.intino.konos.alexandria.activity.model.renders.RenderPanels().panels(ElementDisplays.elementsFor(box, Panel.class, ")).add(mark("panel", "quoted").multiple(", ")).add(literal("))")).add(expression().add(literal(".object((element, object) -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".object(box, element, object)))"))),
			rule().add((condition("type", "objects")), (condition("trigger", "render"))).add(literal("new io.intino.konos.alexandria.activity.model.renders.RenderObjects().panel((io.intino.konos.alexandria.activity.model.Panel) ElementDisplays.displayFor(box, ")).add(mark("panel", "quoted")).add(literal(").element()).source(() -> ")).add(mark("layout", "FirstUpperCase")).add(literal(".")).add(mark("path", "FirstUppercase")).add(literal(".objects(box))")),
			rule().add((condition("type", "layout"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t")).add(mark("elementOption", "class").multiple("\n")).add(literal("\n}")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}