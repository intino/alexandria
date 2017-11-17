package io.intino.konos.builder.codegeneration.services.activity.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ElementDisplaysTemplate extends Template {

	protected ElementDisplaysTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ElementDisplaysTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", ""))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.activity.displays.*;\nimport io.intino.konos.alexandria.activity.model.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class ElementDisplays {\n\tprivate static Map<String, ComponentCreator> elementMap = new HashMap<>();\n\n\tstatic {\n\t\t{\n\t\t\t")).add(mark("display").multiple("\n")).add(literal("\n\t\t}\n\t}\n\n\tpublic static AlexandriaElementDisplay displayFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String name) {\n\t\tif (!elementMap.containsKey(name)) return null;\n\t\treturn elementMap.get(name).build(box);\n\t}\n\n\tpublic static AlexandriaElementDisplay displayFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Element element) {\n\t\tif (!elementMap.containsKey(element.name())) return defaultElementDisplay(box, element);\n\t\treturn elementMap.get(element.name()).build(box);\n\t}\n\n\tpublic static Class<? extends AlexandriaElementDisplay> displayTypeFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Element element) {\n\t\tif (!elementMap.containsKey(element.name())) return defaultElementType(box, element);\n\t\treturn elementMap.get(element.name()).build(box).getClass();\n\t}\n\n\tprivate static Class<? extends AlexandriaElementDisplay> defaultElementType(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Element element) {\n\t\tif (element instanceof Panel) return AlexandriaPanelDisplay.class;\n\n\t\tif (element instanceof TemporalCatalog)\n\t\t\treturn ((TemporalCatalog) element).type() == TemporalCatalog.Type.Range ? AlexandriaTemporalRangeCatalogDisplay.class : AlexandriaTemporalTimeCatalogDisplay.class;\n\n\t\tif (element instanceof Catalog)\n\t\t\treturn AlexandriaCatalogDisplay.class;\n\n\t\tif (element instanceof Layout)\n\t\t\treturn ((Layout) element).mode() == Layout.Mode.Menu ? AlexandriaMenuLayoutDisplay.class : AlexandriaTabLayoutDisplay.class;\n\n\t\tif (element instanceof Desktop)\n\t\t\treturn AlexandriaDesktopDisplay.class;\n\n\t\treturn null;\n\t}\n\n\tprivate static AlexandriaElementDisplay defaultElementDisplay(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Element element) {\n\t\tif (element instanceof Panel) return new AlexandriaPanelDisplay(box);\n\n\t\tif (element instanceof TemporalCatalog)\n\t\t\treturn ((TemporalCatalog) element).type() == TemporalCatalog.Type.Range ? new AlexandriaTemporalRangeCatalogDisplay(box) : new AlexandriaTemporalTimeCatalogDisplay(box);\n\n\t\tif (element instanceof Catalog)\n\t\t\treturn new AlexandriaCatalogDisplay(box);\n\n\t\tif (element instanceof Layout)\n\t\t\treturn ((Layout) element).mode() == Layout.Mode.Menu ? new AlexandriaMenuLayoutDisplay(box) : new AlexandriaTabLayoutDisplay(box);\n\n\t\tif (element instanceof Desktop)\n\t\t\treturn new AlexandriaDesktopDisplay(box);\n\n\t\treturn null;\n\t}\n\n\tprivate interface ComponentCreator {\n\t\tAlexandriaElementDisplay build(")).add(mark("box", "firstUpperCase")).add(literal("Box box);\n\t}\n}")),
			rule().add((condition("trigger", "display"))).add(literal("elementMap.put(\"")).add(mark("name")).add(literal("\", (box) -> new ")).add(mark("name", "FirstUpperCase")).add(mark("type", "FirstUpperCase")).add(literal("(box));"))
		);
		return this;
	}
}