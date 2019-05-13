package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ComponentTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("reference")), (trigger("declaration"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("reference","item"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").<")).output(mark("name", "firstUpperCase")).output(literal(">item(")).output(mark("owner")).output(literal(".this.item()).owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((type("reference"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((allTypes("component","collection","child")), (trigger("declarations"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")).output(expression().output(literal("\n")).output(mark("heading", "declarations").multiple("\n"))),
			rule().condition((allTypes("single","component","stamp","child")), (trigger("declarations"))).output(literal("public ")).output(mark("template", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("component","child")), (trigger("declarations"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")).output(expression().output(literal("\n")).output(mark("component", "declarations").multiple("\n"))),
			rule().condition((allTypes("single","component","stamp","child")), (trigger("declaration"))).output(literal("public ")).output(mark("template", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("component","child")), (trigger("declaration"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("method","multiple"))).output(literal("@Override\npublic ")).output(mark("componentType")).output(literal(" add(")).output(expression().output(mark("objectType")).output(literal(" value"))).output(literal(") {\n\t")).output(mark("componentType")).output(literal(" child = new ")).output(mark("componentType")).output(literal("(box());\n\tchild.id(java.util.UUID.randomUUID().toString());\n\tadd(child, \"")).output(mark("name")).output(literal("\");")).output(expression().output(literal("\n")).output(literal("\tchild.update(")).output(mark("objectTypeValue")).output(literal(");"))).output(literal("\n\treturn child;\n}")),
			rule().condition((allTypes("method","collection","table"))).output(mark("selectionMethod")).output(literal("\n\npublic ")).output(mark("name", "firstUpperCase")).output(literal("Row add(")).output(expression().output(mark("itemClass")).output(literal(" item"))).output(literal(") {\n\t")).output(mark("name", "firstUpperCase")).output(literal("Row row = new ")).output(mark("name", "firstUpperCase")).output(literal("Row((")).output(mark("box", "firstUpperCase")).output(literal("Box)box());")).output(expression().output(literal("\n")).output(literal("\trow.id(java.util.UUID.randomUUID().toString());")).output(literal("\n")).output(literal("\trow.item(")).output(mark("itemVariable")).output(literal(");"))).output(literal("\n\taddPromise(row, \"rows\");\n\treturn row;\n}\n\npublic ")).output(mark("name", "firstUpperCase")).output(literal("Row insert(")).output(expression().output(mark("itemClass")).output(literal(" item, "))).output(literal("int index) {\n\t")).output(mark("name", "firstUpperCase")).output(literal("Row row = new ")).output(mark("name", "firstUpperCase")).output(literal("Row((")).output(mark("box", "firstUpperCase")).output(literal("Box)box());")).output(expression().output(literal("\n")).output(literal("\trow.id(java.util.UUID.randomUUID().toString());")).output(literal("\n")).output(literal("\trow.item(")).output(mark("itemVariable")).output(literal(");"))).output(literal("\n\tinsertPromise(row, index, \"rows\");\n\treturn row;\n}\n\n@Override\npublic void clear() {\n\tclear(\"rows\");\n}")),
			rule().condition((allTypes("method","collection"))).output(mark("selectionMethod")).output(literal("\n")).output(expression().output(mark("item", "addMethod").multiple("\n"))).output(literal("\n")).output(expression().output(mark("item", "removeMethod").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("method"))),
			rule().condition((allTypes("single","component","stamp","child")), (trigger("class"))),
			rule().condition((allTypes("component","collection","child")), (trigger("class"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("extends")).output(expression().output(literal(" ")).output(mark("implements"))).output(literal(" {\n\t")).output(expression().output(mark("reference", "declaration").multiple("\n")).output(literal("\n")).output(literal("\t"))).output(expression().output(mark("heading", "declaration").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\t")).output(mark("properties", "common")).output(literal("\n\t\t")).output(mark("properties", "specific")).output(literal("\n\t}\n\n\t@Override\n\tpublic void ")).output(mark("methodName")).output(literal("() {\n\t\tsuper.")).output(mark("methodName")).output(literal("();")).output(expression().output(literal("\n")).output(literal("\t\t")).output(mark("reference").multiple("\n"))).output(expression().output(literal("\n")).output(literal("\t\t")).output(mark("heading", "child").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(mark("methods")).output(literal("\n\n\t")).output(expression().output(mark("component", "class").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("component", "method").multiple("\n"))).output(literal("\n}")),
			rule().condition((allTypes("component","child")), (trigger("class"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("extends")).output(expression().output(literal(" ")).output(mark("implements"))).output(literal(" {\n\t")).output(expression().output(mark("reference", "declaration").multiple("\n")).output(literal("\n")).output(literal("\t"))).output(expression().output(mark("component", "declaration").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\t")).output(mark("properties", "common")).output(literal("\n\t\t")).output(mark("properties", "specific")).output(literal("\n\t}\n\n\t@Override\n\tpublic void ")).output(mark("methodName")).output(literal("() {\n\t\tsuper.")).output(mark("methodName")).output(literal("();")).output(expression().output(literal("\n")).output(literal("\t\t")).output(mark("reference").multiple("\n"))).output(expression().output(literal("\n")).output(literal("\t\t")).output(mark("component", "child").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(mark("methods")).output(literal("\n\n\t")).output(expression().output(mark("component", "class").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("component", "method").multiple("\n"))).output(literal("\n}")),
			rule().condition((allTypes("extends","multiple"))).output(literal("io.intino.alexandria.ui.displays.components.Multiple<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("componentType")).output(literal(", ")).output(mark("objectType")).output(literal(">")),
			rule().condition((allTypes("extends","collection","table"))).output(literal("io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", io.intino.alexandria.ui.displays.components.Row, ")).output(mark("itemClass")).output(literal(">")),
			rule().condition((allTypes("extends","collection"))).output(literal("io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("componentType")).output(literal(", ")).output(mark("itemClass")).output(literal(">")),
			rule().condition((allTypes("item","extends"))).output(literal("io.intino.alexandria.ui.displays.components.Item<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("itemClass")).output(literal(">")),
			rule().condition((allTypes("extends","stamp"))).output(mark("type", "firstUpperCase")),
			rule().condition((type("extends"))).output(literal("io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet")).output(literal("<io.intino.alexandria.ui.displays.notifiers.")).output(mark("type", "firstUpperCase")).output(mark("facet")).output(literal("Notifier, ")).output(mark("abstractBox", "type")).output(literal(">")),
			rule().condition((allTypes("component","multipleblock","child")), (trigger("rootchildreferences"))),
			rule().condition((allTypes("component","collection","child")), (trigger("rootchildreferences"))).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestorsNotMe").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";")).output(expression().output(literal("\n")).output(mark("heading", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("rootchildreferences"))).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestorsNotMe").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";")).output(expression().output(literal("\n")).output(mark("component", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","collection","child")), (trigger("childreferences"))).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestors").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";")).output(expression().output(literal("\n")).output(mark("heading", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("childreferences"))).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestors").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";")).output(expression().output(literal("\n")).output(mark("component", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","collection","child")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")).output(expression().output(literal("\n")).output(mark("heading", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","multiple","stamp","child")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((allTypes("single","component","stamp","child")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("template", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).id(\"")).output(mark("id")).output(literal("\"));")),
			rule().condition((allTypes("component","child")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")).output(expression().output(literal("\n")).output(mark("component", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","collection","child")), (trigger("references"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")).output(expression().output(literal("\n")).output(mark("heading", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("references"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")).output(expression().output(literal("\n")).output(mark("component", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","collection","child")), (trigger("initializations"))).output(mark("binding")).output(expression().output(literal("\n")).output(mark("heading", "initializations").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("initializations"))).output(mark("binding")).output(expression().output(literal("\n")).output(mark("component", "initializations").multiple("\n"))),
			rule().condition((allTypes("component","multiple","stamp","child"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((allTypes("single","component","stamp","child"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("template", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).id(\"")).output(mark("id")).output(literal("\"));")),
			rule().condition((allTypes("component","child"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((type("facet"))).output(mark("name", "firstUpperCase")),
			rule().condition((allTypes("toolbar","binding"))).output(mark("name")).output(literal(".bindTo(")).output(mark("collection")).output(literal(");")),
			rule().condition((allTypes("binding","grouping"))).output(mark("name")).output(literal(".bindTo(")).output(mark("collection")).output(literal(");")),
			rule().condition((type("binding"))).output(mark("name")).output(literal(".bindTo(")).output(mark("selector")).output(literal(", \"")).output(mark("option")).output(literal("\");")),
			rule().condition((allTypes("implements","selectablecollection"))).output(literal("implements io.intino.alexandria.ui.displays.components.collection.Selectable")),
			rule().condition((allTypes("implements","option"))).output(literal("implements io.intino.alexandria.ui.displays.components.selector.SelectorOption")),
			rule().condition((allTypes("implements","dynamicloadedcomponent"))).output(literal("implements io.intino.alexandria.ui.displays.components.DynamicLoaded")),
			rule().condition((type("implements"))).output(literal("implements --undefined--")),
			rule().condition((allTypes("operation","properties")), (trigger("common"))).output(expression().output(literal("title(\"")).output(mark("title")).output(literal("\");"))).output(expression().output(literal("\n")).output(literal("color(\"")).output(mark("color")).output(literal("\");"))).output(expression().output(literal("\n")).output(literal("disabled(")).output(mark("disabled")).output(literal(");"))).output(literal("\nmode(io.intino.alexandria.ui.displays.components.Operation.Mode.valueOf(\"")).output(mark("mode", "firstUpperCase")).output(literal("\"));\n")).output(mark("operationMode")),
			rule().condition((type("properties")), (trigger("common"))).output(expression().output(literal("label(\"")).output(mark("label")).output(literal("\");"))).output(expression().output(literal("\n")).output(literal("color(\"")).output(mark("color")).output(literal("\");"))),
			rule().condition((allTypes("image","avatar","properties")), (trigger("specific"))).output(expression().output(literal("text(\"")).output(mark("text")).output(literal("\");"))),
			rule().condition((allTypes("block","properties")), (trigger("specific"))).output(expression().output(mark("background", "resourceMethod"))).output(expression().output(literal("\n")).output(mark("badge"))),
			rule().condition((allTypes("image","properties")), (trigger("specific"))).output(expression().output(mark("value", "resourceMethod"))).output(expression().output(literal("\n")).output(mark("defaultValue", "resourceMethod"))),
			rule().condition((allTypes("file","properties")), (trigger("specific"))).output(expression().output(mark("value", "resourceMethod"))),
			rule().condition((allTypes("openpage","properties")), (trigger("specific"))).output(expression().output(literal("path(\"")).output(mark("path")).output(literal("\");"))),
			rule().condition((allTypes("export","properties")), (trigger("specific"))).output(expression().output(literal("from(")).output(mark("from")).output(literal("L);"))).output(expression().output(literal("\n")).output(literal("to(")).output(mark("to")).output(literal("L);"))).output(expression().output(literal("\n")).output(literal("min(")).output(mark("min")).output(literal("L);"))).output(expression().output(literal("\n")).output(literal("max(")).output(mark("max")).output(literal("L);"))).output(expression().output(literal("\n")).output(literal("range(")).output(mark("rangeMin")).output(literal(",")).output(mark("rangeMax")).output(literal(");"))).output(expression().output(literal("\n")).output(literal("options(java.util.Arrays.asList(\"")).output(mark("option").multiple("\",\"")).output(literal("\"));"))),
			rule().condition((allTypes("download","properties")), (trigger("specific"))).output(expression().output(literal("options(java.util.Arrays.asList(\"")).output(mark("option").multiple("\",\"")).output(literal("\"));"))),
			rule().condition((allTypes("downloadselection","properties")), (trigger("specific"))).output(expression().output(literal("options(java.util.Arrays.asList(\"")).output(mark("option").multiple("\",\"")).output(literal("\"));"))),
			rule().condition((allTypes("chart","properties")), (trigger("specific"))).output(expression().output(literal("query(\"")).output(mark("query")).output(literal("\");"))).output(expression().output(literal("\n")).output(mark("input", "inputMethod"))).output(expression().output(literal("\n")).output(literal("output(\"")).output(mark("output")).output(literal("\");"))),
			rule().condition((allTypes("number","properties")), (trigger("specific"))).output(expression().output(literal("value(")).output(mark("value")).output(literal(");"))).output(expression().output(literal("\n")).output(literal("min(")).output(mark("min")).output(literal(");"))).output(expression().output(literal("\n")).output(literal("max(")).output(mark("max")).output(literal(");"))),
			rule().condition((allTypes("date","properties")), (trigger("specific"))).output(expression().output(literal("value(java.time.Instant.ofEpochMilli(")).output(mark("value")).output(literal("L));"))).output(expression().output(literal("\n")).output(literal("min(java.time.Instant.ofEpochMilli(")).output(mark("min")).output(literal("L));"))).output(expression().output(literal("\n")).output(literal("max(java.time.Instant.ofEpochMilli(")).output(mark("max")).output(literal("L));"))),
			rule().condition((allTypes("text","properties")), (trigger("specific"))).output(expression().output(literal("value(\"")).output(mark("defaultValue")).output(literal("\");"))),
			rule().condition((allTypes("collection","properties")), (trigger("specific"))).output(expression().output(literal("source(new io.intino.alexandria.ui.sources.")).output(mark("sourceClass", "firstUpperCase")).output(literal("());"))).output(literal("\npageSize(")).output(mark("pageSize")).output(literal(");")),
			rule().condition((type("properties")), (trigger("specific"))),
			rule().condition((allTypes("inputmethod","csv")), (trigger("inputmethod"))).output(literal("input(new io.intino.alexandria.ui.displays.components.chart.datasources.CSVDataSource(")).output(mark("owner")).output(literal(".class.getResource(\"")).output(mark("value")).output(literal("\")));")),
			rule().condition((allTypes("inputmethod","source")), (trigger("inputmethod"))).output(literal("input(new ")).output(mark("value")).output(literal("());")),
			rule().condition((trigger("resourcemethod"))).output(mark("name")).output(literal("(")).output(mark("owner")).output(literal(".class.getResource(\"")).output(mark("value")).output(literal("\"));")),
			rule().condition((allTypes("iconbutton","operationmode"))).output(literal("icon(\"")).output(mark("icon")).output(literal("\");")),
			rule().condition((allTypes("materialiconbutton","operationmode"))).output(literal("icon(\"")).output(mark("icon")).output(literal("\");")),
			rule().condition((type("badge"))).output(expression().output(literal("value(")).output(mark("value")).output(literal(");"))),
			rule().condition((type("selectionmethod"))).output(literal("public void onSelect(io.intino.alexandria.ui.displays.events.SelectionListener listener) {\n\tsuper.addSelectionListener(listener);\n}")),
			rule().condition((type("item")), (trigger("adddeclaration"))).output(mark("name")).output(literal("(add")).output(mark("name", "firstUpperCase")).output(literal("(new ")).output(mark("name", "firstUpperCase")).output(literal("(box())")).output(expression().output(literal(", ")).output(mark("itemVariable"))).output(literal("))")),
			rule().condition((type("item")), (trigger("type"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("item")), (trigger("addrow"))).output(mark("name")).output(literal(" = register(add")).output(mark("name", "firstUpperCase")).output(literal("(box()));")),
			rule().condition((type("item")), (trigger("property"))).output(literal("public ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((type("item")), (trigger("addmethod"))).output(mark("methodAccessibility")).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" add")).output(mark("methodName", "firstUpperCase")).output(literal("(")).output(expression().output(mark("itemClass")).output(literal(" item"))).output(literal(") {\n\t")).output(mark("name", "firstUpperCase")).output(literal(" result = new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box());\n\tresult.id(java.util.UUID.randomUUID().toString());")).output(expression().output(literal("\n")).output(literal("\tresult.item(")).output(mark("itemVariable")).output(literal(");"))).output(expression().output(literal("\n")).output(literal("\t")).output(mark("addPromise")).output(literal("(result, \"rows\");"))).output(literal("\n\treturn result;\n}\n")).output(mark("methodAccessibility")).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" insert")).output(mark("methodName", "firstUpperCase")).output(literal("(")).output(expression().output(mark("itemClass")).output(literal(" item, "))).output(literal("int index) {\n\t")).output(mark("name", "firstUpperCase")).output(literal(" result = new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box());\n\tresult.id(java.util.UUID.randomUUID().toString());")).output(expression().output(literal("\n")).output(literal("\tresult.item(")).output(mark("itemVariable")).output(literal(");"))).output(expression().output(literal("\n")).output(literal("\t")).output(mark("insertPromise")).output(literal("(result, index, \"rows\");"))).output(literal("\n\treturn result;\n}")),
			rule().condition((type("item")), (trigger("removemethod"))).output(literal("@Override\npublic void clear() {\n\tclear(\"rows\");\n}"))
		);
	}
}