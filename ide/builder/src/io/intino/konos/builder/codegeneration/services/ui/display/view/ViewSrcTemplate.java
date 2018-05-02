package io.intino.konos.builder.codegeneration.services.ui.display.view;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ViewSrcTemplate extends Template {

	protected ViewSrcTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ViewSrcTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "view & container & display"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\t")).add(expression().add(mark("catalogScope")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tpublic static AlexandriaDisplay ")).add(mark("display", "firstLowerCase")).add(literal("(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element context, Consumer<Boolean> loadingListener, Consumer<io.intino.konos.alexandria.ui.displays.CatalogInstantBlock> instantListener, UISession session) {\n\t\treturn null;//TODO\n\t}\n\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "view & container & mold & hasMethods"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "view & container & catalog & hasMethods"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\t")).add(mark("filter")).add(literal("\n\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "view & container & panel & hasMethods"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "view & container & set"))).add(literal("public static class ")).add(mark("name", "firstUpperCase")).add(literal(" {\n\t")).add(mark("item").multiple("\n")).add(literal("\n\n\t")).add(mark("hidden")).add(literal("\n}")),
			rule().add((condition("type", "group"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("item").multiple("\n")).add(literal("\n\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("hidden")).add(literal("\n"))).add(literal("\n}")),
			rule().add((condition("type", "items"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\n\tpublic static java.util.List<io.intino.konos.alexandria.ui.model.Item> items(")).add(mark("box", "firstUpperCase")).add(literal("Box box, UISession session) {\n\t\treturn java.util.Collections.emptyList();\n\t}\n\n\tpublic static String label(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element, ")).add(mark("itemClass")).add(literal(" ")).add(mark("itemClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn null;\n\t}\n\n\tpublic static String icon(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element, ")).add(mark("itemClass")).add(literal(" ")).add(mark("itemClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn null;\n\t}\n\n\tpublic static Integer bubble(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Element element, ")).add(mark("itemClass")).add(literal(" ")).add(mark("itemClass", "shortType", "firstLowerCase")).add(literal(") {\n\t\treturn null;\n\t}\n\n\t")).add(mark("view")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("hidden")).add(literal("\n"))).add(literal("\n}")),
			rule().add((condition("type", "item"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal(" {\n\t")).add(mark("view")).add(literal("\n\t")).add(mark("hidden")).add(expression().add(literal("\n"))).add(literal("\n}")),
			rule().add((condition("type", "hidden"))).add(literal("public static boolean hidden(")).add(mark("box")).add(literal("Box box, Object object, UISession session) {\n\treturn false;//TODO\n}")),
			rule().add((condition("type", "filter"))).add(literal("public static boolean filter(")).add(mark("box")).add(literal("Box box, io.intino.konos.alexandria.ui.model.Catalog catalog, io.intino.konos.alexandria.ui.model.Element context, Object target, ")).add(mark("itemClass")).add(literal(" object, UISession session) {\n\treturn true;//TODO\n}\ndef\n\ndef type(catalogScope)\npublic static void ")).add(mark("display", "firstLowerCase")).add(literal("Scope(")).add(mark("box", "firstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.ui.displays.AlexandriaDisplay display, io.intino.konos.alexandria.ui.model.catalog.Scope scope) {\n}"))
		);
		return this;
	}
}