package io.intino.konos.builder.codegeneration.services.ui.display;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplaysTemplate extends Template {

	protected DisplaysTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplaysTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "Displays"))).add(literal("package ")).add(mark("package")).add(literal(".displays;\n\nimport io.intino.konos.alexandria.ui.displays.AlexandriaDisplay;\nimport io.intino.konos.alexandria.ui.displays.*;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class Displays {\n\tprivate static Map<String, ElementDisplayBuilder> displayMap = new HashMap<>();\n\n\tstatic {\n\t\t{\n\t\t\t")).add(mark("display").multiple("\n")).add(literal("\n\t\t\t")).add(mark("dialog").multiple("\n")).add(literal("\n\t\t}\n\t}\n\n\tpublic static AlexandriaElementDisplay displayFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String name) {\n\t\tif (!displayMap.containsKey(name)) return null;\n\t\treturn displayMap.get(name).build(box);\n\t}\n\n\tpublic static AlexandriaElementDisplay displayFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element) {\n\t\tif (!displayMap.containsKey(element.name())) return defaultElement(box, element);\n\t\treturn displayMap.get(element.name()).build(box);\n\t}\n\n\tpublic static <T extends io.intino.konos.alexandria.ui.model.Element> T elementFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, Class<T> tClass, String name) {\n\t\tif (!displayMap.containsKey(name)) return null;\n\t\treturn (T) displayMap.get(name).build(box).element();\n\t}\n\n\tpublic static Class<? extends AlexandriaElementDisplay> displayTypeFor(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element) {\n\t\tif (!displayMap.containsKey(element.name())) return defaultElementType(box, element);\n\t\treturn displayMap.get(element.name()).build(box).getClass();\n\t}\n\n\tprivate static Class<? extends AlexandriaElementDisplay> defaultElementType(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element) {\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.Panel) return AlexandriaPanel.class;\n\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.TemporalCatalog)\n\t\t\treturn ((io.intino.konos.alexandria.ui.model.TemporalCatalog) element).type() == io.intino.konos.alexandria.ui.model.TemporalCatalog.Type.Range ? AlexandriaTemporalRangeCatalog.class : AlexandriaTemporalTimeCatalog.class;\n\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.Catalog)\n\t\t\treturn AlexandriaCatalog.class;\n\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.panel.Desktop)\n\t\t\treturn AlexandriaDesktop.class;\n\n\t\treturn null;\n\t}\n\n\tprivate static AlexandriaElementDisplay defaultElement(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element) {\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.Panel) return new AlexandriaPanel(box);\n\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.TemporalCatalog)\n\t\t\treturn ((io.intino.konos.alexandria.ui.model.TemporalCatalog) element).type() == io.intino.konos.alexandria.ui.model.TemporalCatalog.Type.Range ? new AlexandriaTemporalRangeCatalog(box) : new AlexandriaTemporalTimeCatalog(box);\n\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.Catalog)\n\t\t\treturn new AlexandriaCatalog(box);\n\n\t\tif (element instanceof io.intino.konos.alexandria.ui.model.panel.Desktop)\n\t\t\treturn new AlexandriaDesktop(box);\n\n\t\treturn null;\n\t}\n\n/*\t\tprivate void refreshCatalog(")).add(mark("box", "firstUpperCase")).add(literal("Box box, String catalog, Item... items) {\n\t\tuiSouls.values().stream().map(Soul::getAll).flatMap(Collection::stream)\n\t\t\t\t.filter(d -> d instanceof AlexandriaAbstractCatalog && ((AlexandriaAbstractCatalog) d)\n\t\t\t\t\t\t.isFor(catalog)).forEach(d -> {\n\t\t\tAlexandriaAbstractCatalog display = (AlexandriaAbstractCatalog) d;\n\t\t\tdisplay.dirty(true);\n\t\t\tif (categorizationsHaveChanges || items.length == 0)\n\t\t\t\tdisplay.refresh();\n\t\t\telse\n\t\t\t\tdisplay.refresh(items);\n\t\t});\n\t}\n*/\n\tprivate interface ElementDisplayBuilder {\n\t\tAlexandriaElementDisplay build(")).add(mark("box", "firstUpperCase")).add(literal("Box box);\n\t}\n}")),
			rule().add((condition("trigger", "display"))).add(literal("displayMap.put(\"")).add(mark("name")).add(literal("\", (box) -> new ")).add(mark("name", "FirstUpperCase")).add(literal("(box));")),
			rule().add((condition("trigger", "dialog"))).add(literal("dialogMap.put(\"")).add(mark("name")).add(literal("\", (box) -> new ")).add(mark("name", "FirstUpperCase")).add(literal("(box));"))
		);
		return this;
	}
}