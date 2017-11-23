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
			rule().add((condition("type", "layout & gen"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaElementDisplay;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaMenuLayoutDisplay;\nimport io.intino.konos.alexandria.activity.model.Catalog;\nimport io.intino.konos.alexandria.activity.model.Element;\nimport io.intino.konos.alexandria.activity.model.Layout;\nimport io.intino.konos.alexandria.activity.model.Panel;\nimport io.intino.konos.alexandria.activity.model.layout.ElementOption;\nimport io.intino.konos.alexandria.activity.model.layout.options.Group;\nimport io.intino.konos.alexandria.activity.model.layout.options.Option;\nimport io.intino.konos.alexandria.activity.model.renders.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".displays.notifiers.")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier;\n\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic abstract class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Layout extends AlexandriaMenuLayoutDisplay<")).add(mark("name", "FirstUpperCase")).add(literal("DisplayNotifier> {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Layout(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t\telement(buildLayout(box));\n\t}\n\n\tprivate static Layout buildLayout(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tLayout layout = new Layout();\n\t\t")).add(expression().add(literal("layout.mode(Layout.Mode.")).add(mark("mode")).add(literal(");"))).add(literal("\n\t\tlayout.elementDisplayBuilder(new Layout.ElementDisplayBuilder() {\n\t\t\t@Override\n\t\t\tpublic AlexandriaElementDisplay displayFor(Element element, Object o) {\n\t\t\t\treturn ElementDisplays.displayFor(box, element);\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic Class<? extends AlexandriaElementDisplay> displayTypeFor(Element element, Object o) {\n\t\t\t\treturn ElementDisplays.displayTypeFor(box, element);\n\t\t\t}\n\t\t});\n\t\tbuildOptions(box).forEach(o -> layout.add(o));\n\t\treturn layout;\n\t}\n\n\tprivate static List<ElementOption> buildOptions(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tList<ElementOption> result = new ArrayList<>();\n\t\t")).add(mark("group").multiple("\n")).add(literal("\n\t\treturn result;\n\t}\n}")),
			rule().add((condition("trigger", "group"))).add(literal("result.add(new Group().")).add(expression().add(literal("label(\"")).add(mark("label")).add(literal("\")"))).add(literal(".mode(Group.Mode.")).add(mark("mode")).add(literal(")")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("option").multiple("\n"))).add(literal(";")),
			rule().add((condition("type", "catalog")), (condition("trigger", "option"))).add(literal(".add(new Option().render(new RenderCatalog().catalog((Catalog) ElementDisplays.displayFor(box, \"")).add(mark("catalog")).add(literal("\").element())).label(\"")).add(mark("label")).add(literal("\"))")),
			rule().add((condition("type", "panel")), (condition("trigger", "option"))).add(literal(".add(new Option().render(new RenderPanel().panel((Panel) ElementDisplays.displayFor(box, \"")).add(mark("panel")).add(literal("\").element())).label(\"")).add(mark("label")).add(literal("\")))")),
			rule().add((condition("type", "layout & src"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Display extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Layout {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Display(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t@Override\n\tprotected void init() {\n\t\tsuper.init();\n\t}\n}"))
		);
		return this;
	}
}