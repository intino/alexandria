package io.intino.konos.builder.codegeneration.services.ui.display.view;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ViewTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("view", "display"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" {\n\t")).output(expression().output(mark("catalogScope")).output(literal("\n")).output(literal("\t"))).output(literal("\n\tpublic static AlexandriaDisplay display(")).output(mark("box", "firstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.model.Element context, Consumer<Boolean> loadingListener, Consumer<io.intino.alexandria.ui.displays.CatalogInstantBlock> instantListener, UISession session) {\n\t\treturn null;//TODO\n\t}\n\n\t")).output(mark("hidden")).output(literal("\n}")),
				rule().condition((allTypes("view", "hasmethods", "mold"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" {\n\t")).output(mark("hidden")).output(literal("\n}")),
				rule().condition((allTypes("view", "catalog", "hasmethods"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" {\n\t")).output(mark("filter")).output(literal("\n\n\t")).output(mark("hidden")).output(literal("\n}")),
				rule().condition((allTypes("view", "hasmethods", "panel"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" {\n\t")).output(mark("hidden")).output(literal("\n}")),
				rule().condition((allTypes("view", "set"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" {\n\t")).output(mark("item").multiple("\n")).output(literal("\n\n\t")).output(mark("hidden")).output(literal("\n}")),
				rule().condition((type("group"))).output(literal("public static class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("item").multiple("\n")).output(literal("\n\n\t")).output(expression().output(mark("hidden"))).output(literal("\n}")),
				rule().condition((type("items"))).output(literal("public static class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic static java.util.List<io.intino.alexandria.ui.model.Item> items(")).output(mark("box", "firstUpperCase")).output(literal("Box box, UISession session) {\n\t\treturn java.util.Collections.emptyList();\n\t}\n\n\tpublic static String label(")).output(mark("box", "firstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.model.Element element, ")).output(mark("itemClass")).output(literal(" ")).output(mark("itemClass", "shortType", "firstLowerCase")).output(literal(") {\n\t\treturn null;\n\t}\n\n\tpublic static String icon(")).output(mark("box", "firstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.model.Element element, ")).output(mark("itemClass")).output(literal(" ")).output(mark("itemClass", "shortType", "firstLowerCase")).output(literal(") {\n\t\treturn null;\n\t}\n\n\tpublic static Integer bubble(")).output(mark("box", "firstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.model.Element element, ")).output(mark("itemClass")).output(literal(" ")).output(mark("itemClass", "shortType", "firstLowerCase")).output(literal(") {\n\t\treturn null;\n\t}\n\n\t")).output(mark("view")).output(literal("\n\t")).output(expression().output(mark("hidden"))).output(literal("\n}")),
				rule().condition((type("item"))).output(literal("public static class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("view")).output(literal("\n\t")).output(expression().output(mark("hidden"))).output(literal("\n}")),
				rule().condition((type("hidden"))).output(literal("public static boolean hidden(")).output(mark("box")).output(literal("Box box, Object object, UISession session) {\n\treturn false;//TODO\n}")),
				rule().condition((type("filter"))).output(literal("public static boolean filter(")).output(mark("box")).output(literal("Box box, io.intino.alexandria.ui.model.Catalog catalog, io.intino.alexandria.ui.model.Element context, Object target, ")).output(mark("itemClass")).output(literal(" object, UISession session) {\n\treturn true;//TODO\n}\ndef\n\ndef type(catalogScope)\npublic static void scope(")).output(mark("box", "firstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.displays.AlexandriaDisplay display, io.intino.alexandria.ui.model.catalog.Scope scope) {\n}"))
		);
	}
}