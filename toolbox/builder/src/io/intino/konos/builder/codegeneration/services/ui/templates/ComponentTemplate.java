package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ComponentTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("reference")), (trigger("declaration"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("reference","item"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").<")).output(mark("name", "firstUpperCase")).output(literal(">item(")).output(mark("owner")).output(literal(".this.item()).owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((type("reference"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((allTypes("component","child","collection")), (trigger("declarations"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";\n")).output(expression().output(mark("heading", "declarations").multiple("\n"))),
			rule().condition((allTypes("component","child","stamp","single")), (trigger("declarations"))).output(literal("public ")).output(mark("template", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("component","child")), (trigger("declarations"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";\n")).output(expression().output(mark("component", "declarations").multiple("\n"))),
			rule().condition((allTypes("component","child","stamp","single")), (trigger("declaration"))).output(literal("public ")).output(mark("template", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("component","child")), (trigger("declaration"))).output(literal("public ")).output(expression().output(mark("ancestors", "firstUpperCase").multiple(".")).output(literal("."))).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("method","multiple"))).output(literal("@Override\npublic ")).output(mark("componentType")).output(literal(" add(")).output(expression().output(mark("objectType")).output(literal(" value"))).output(literal(") {\n\t")).output(mark("componentType")).output(literal(" child = new ")).output(mark("componentType")).output(literal("(box());\n\tchild.id(java.util.UUID.randomUUID().toString());\n\tadd(child, \"")).output(mark("name")).output(literal("\");\n\t")).output(expression().output(literal("child.update(")).output(mark("objectTypeValue")).output(literal(");"))).output(literal("\n\treturn child;\n}\npublic void clear() {\n    super.clear(\"")).output(mark("name")).output(literal("\");\n}")),
			rule().condition((allTypes("method","collection","table"))).output(expression().output(mark("selectionMethod"))).output(literal("\n\npublic ")).output(mark("name", "firstUpperCase")).output(literal("Row create(")).output(expression().output(mark("itemClass")).output(literal(" item"))).output(literal(") {\n\t")).output(mark("name", "firstUpperCase")).output(literal("Row row = new ")).output(mark("name", "firstUpperCase")).output(literal("Row((")).output(mark("box", "firstUpperCase")).output(literal("Box)box());\n\trow.id(java.util.UUID.randomUUID().toString());\n\t")).output(expression().output(literal("row.item(")).output(mark("itemVariable")).output(literal(");"))).output(literal("\n\treturn row;\n}")),
			rule().condition((allTypes("method","collection"))).output(mark("selectionMethod")).output(literal("\n")).output(expression().output(mark("item", "addMethod").multiple("\n\n"))).output(literal("\n\n")).output(expression().output(mark("item", "removeMethod").multiple("\n\n"))),
			rule().condition((allTypes("component","child")), (trigger("method"))),
			rule().condition((allTypes("component","child","stamp","single")), (trigger("class"))),
			rule().condition((allTypes("component","child","collection")), (trigger("class"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("extends")).output(literal(" ")).output(mark("implements")).output(literal(" {\n\t")).output(expression().output(mark("reference", "declaration").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("heading", "declaration").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\t")).output(expression().output(mark("properties", "common"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "specific"))).output(literal("\n\t}\n\n\t@Override\n\tpublic void ")).output(mark("methodName")).output(literal("() {\n\t\tsuper.")).output(mark("methodName")).output(literal("();\n\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("heading", "child").multiple("\n"))).output(literal("\n\t}\n\t")).output(mark("methods")).output(literal("\n\t")).output(expression().output(mark("component", "class").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("component", "method").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((attribute("notifyready"))).output(literal("notifyReady();")),
			rule().condition((allTypes("component","child","block","conditional")), (trigger("class"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("extends")).output(literal(" ")).output(mark("implements")).output(literal(" {\n\t")).output(expression().output(mark("reference", "declaration").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("component", "declaration").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\t")).output(expression().output(mark("properties", "common"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "specific"))).output(literal("\n\t}\n\n\t@Override\n\tpublic void ")).output(mark("methodName")).output(literal("() {\n\t\tsuper.init();\n\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("component", "child").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("component", "conditionalReferences").multiple("\n"))).output(literal("\n\t}\n\t")).output(mark("methods")).output(literal("\n\t")).output(expression().output(mark("component", "class").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("component", "method").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((allTypes("component","child")), (trigger("class"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal(" extends ")).output(mark("extends")).output(literal(" ")).output(mark("implements")).output(literal(" {\n\t")).output(expression().output(mark("reference", "declaration").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("component", "declaration").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("abstractBox", "type")).output(literal(" box) {\n\t\tsuper(box);\n\t\t")).output(expression().output(mark("properties", "common"))).output(literal("\n\t\t")).output(expression().output(mark("properties", "specific"))).output(literal("\n\t}\n\n\t@Override\n\tpublic void ")).output(mark("methodName")).output(literal("() {\n\t\tsuper.init();\n\t\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("component", "child").multiple("\n"))).output(literal("\n\t}\n\t")).output(mark("methods")).output(literal("\n\t")).output(expression().output(mark("component", "class").multiple("\n\n"))).output(literal("\n\t")).output(expression().output(mark("component", "method").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((allTypes("extends","multiple"))).output(literal("io.intino.alexandria.ui.displays.components.Multiple<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("componentType")).output(literal(", ")).output(mark("objectType")).output(literal(">")),
			rule().condition((allTypes("extends","collection","table"))).output(literal("io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", io.intino.alexandria.ui.displays.components.Row, ")).output(mark("itemClass")).output(literal(">")),
			rule().condition((allTypes("extends","collection"))).output(literal("io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("componentType")).output(literal(", ")).output(mark("itemClass")).output(literal(">")),
			rule().condition((allTypes("extends","item"))).output(literal("io.intino.alexandria.ui.displays.components.Item<")).output(mark("abstractBox", "type")).output(literal(", ")).output(mark("itemClass")).output(literal(">")),
			rule().condition((allTypes("extends","stamp"))).output(mark("type", "firstUpperCase")),
			rule().condition((type("extends"))).output(literal("io.intino.alexandria.ui.displays.components.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("<io.intino.alexandria.ui.displays.notifiers.")).output(mark("type", "firstUpperCase")).output(mark("facet").multiple("")).output(literal("Notifier, ")).output(mark("abstractBox", "type")).output(literal(">")),
			rule().condition((allTypes("component","child","multipleblock")), (trigger("rootchildreferences"))),
			rule().condition((allTypes("component","child","collection")), (trigger("rootchildreferences"))).output(expression().output(literal("if (")).output(mark("parent")).output(literal(" != null)"))).output(literal(" ")).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestorsNotMe").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";\n")).output(expression().output(mark("heading", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("rootchildreferences"))).output(expression().output(literal("if (")).output(mark("parent")).output(literal(" != null)"))).output(literal(" ")).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestorsNotMe").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";\n")).output(expression().output(mark("component", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","child","collection")), (trigger("childreferences"))).output(expression().output(literal("if (")).output(mark("parent")).output(literal(" != null)"))).output(literal(" ")).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestors").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";\n")).output(expression().output(mark("heading", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("childreferences"))).output(expression().output(literal("if (")).output(mark("parent")).output(literal(" != null)"))).output(literal(" ")).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestors").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";\n")).output(expression().output(mark("component", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","child","collection")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));\n")).output(expression().output(mark("heading", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","child","stamp","multiple")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((allTypes("component","child","stamp","single")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("template", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).id(\"")).output(mark("id")).output(literal("\"));")),
			rule().condition((allTypes("component","child")), (trigger("rootreferences"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));\n")).output(expression().output(mark("component", "rootChildReferences").multiple("\n"))),
			rule().condition((allTypes("component","child","collection")), (trigger("references"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));\n")).output(expression().output(mark("heading", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("references"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));\n")).output(expression().output(mark("component", "childReferences").multiple("\n"))),
			rule().condition((allTypes("component","child","block")), (trigger("conditionalreferences"))).output(expression().output(mark("component", "conditionalReferences").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("conditionalreferences"))).output(mark("name")).output(literal(" = ")).output(expression().output(mark("ancestorsNotMe").multiple(".")).output(literal("."))).output(mark("name")).output(literal(";\n")).output(expression().output(mark("binding"))),
			rule().condition((allTypes("component","child","collection")), (trigger("initializations"))).output(expression().output(mark("binding"))).output(literal("\n")).output(expression().output(mark("heading", "initializations").multiple("\n"))),
			rule().condition((allTypes("component","child")), (trigger("initializations"))).output(expression().output(mark("binding"))).output(literal("\n")).output(expression().output(mark("component", "initializations").multiple("\n"))),
			rule().condition((allTypes("component","child","stamp","multiple"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((allTypes("component","child","stamp","single"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("template", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box()).id(\"")).output(mark("id")).output(literal("\"));")),
			rule().condition((allTypes("component","child"))).output(literal("if (")).output(mark("name")).output(literal(" == null) ")).output(mark("name")).output(literal(" = register(new ")).output(mark("name", "firstUpperCase")).output(literal("(box()).<")).output(mark("name", "firstUpperCase")).output(literal(">id(\"")).output(mark("id")).output(literal("\").owner(")).output(mark("owner")).output(literal(".this));")),
			rule().condition((type("facet"))).output(mark("name", "firstUpperCase")),
			rule().condition((allTypes("binding","toolbar"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("collection")).output(literal(");")),
			rule().condition((allTypes("binding","grouping"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("collection").multiple(",")).output(literal(");")),
			rule().condition((allTypes("binding","sorting"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("collection").multiple(",")).output(literal(");")),
			rule().condition((allTypes("binding","searchbox"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("collection").multiple(",")).output(literal(");")),
			rule().condition((allTypes("binding","temporalslider"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("collection").multiple(",")).output(literal(");")),
			rule().condition((allTypes("binding","openblock"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("block")).output(literal(");")),
			rule().condition((allTypes("binding","opendialog"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("dialog")).output(literal(");")),
			rule().condition((allTypes("binding","closedialog"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("dialog")).output(literal(");")),
			rule().condition((allTypes("binding","decisiondialog"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("selector")).output(literal(");")),
			rule().condition((allTypes("binding","collectiondialog"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("collection")).output(literal(");")),
			rule().condition((type("binding"))).output(literal("if (")).output(mark("name")).output(literal(" != null) ")).output(mark("name")).output(literal(".bindTo(")).output(mark("selector")).output(literal(", \"")).output(mark("option")).output(literal("\");")),
			rule().condition((allTypes("implements","selectablecollection"))).output(literal("implements io.intino.alexandria.ui.displays.components.collection.Selectable")),
			rule().condition((allTypes("implements","option"))).output(literal("implements io.intino.alexandria.ui.displays.components.selector.SelectorOption")),
			rule().condition((allTypes("implements","dynamicloadedcomponent"))).output(literal("implements io.intino.alexandria.ui.displays.components.DynamicLoaded")),
			rule().condition((type("implements"))).output(literal("implements --undefined--")),
			rule().condition((allTypes("properties","operation")), (trigger("common"))).output(expression().output(literal("title(\"")).output(mark("title")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("color(\"")).output(mark("color")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("disabled(")).output(mark("disabled")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("mode(io.intino.alexandria.ui.displays.components.Operation.Mode.valueOf(\"")).output(mark("mode", "firstUpperCase")).output(literal("\"));"))).output(literal("\n")).output(expression().output(mark("operationMode"))),
			rule().condition((type("properties")), (trigger("common"))).output(expression().output(literal("name(\"")).output(mark("name")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("label(\"")).output(mark("label")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("color(\"")).output(mark("color")).output(literal("\");"))),
			rule().condition((allTypes("properties","selector")), (trigger("specific"))).output(expression().output(literal("multipleSelection(")).output(mark("multipleSelection")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("readonly(")).output(mark("readonly")).output(literal(");"))),
			rule().condition((allTypes("properties","image","avatar")), (trigger("specific"))).output(expression().output(literal("text(\"")).output(mark("text")).output(literal("\");"))),
			rule().condition((allTypes("properties","block")), (trigger("specific"))).output(expression().output(mark("background", "resourceMethod"))).output(literal("\n")).output(expression().output(mark("badge"))),
			rule().condition((allTypes("properties","image")), (trigger("specific"))).output(expression().output(mark("value", "resourceMethod"))).output(literal("\n")).output(expression().output(mark("defaultValue", "resourceMethod"))),
			rule().condition((allTypes("properties","file")), (trigger("specific"))).output(expression().output(mark("value", "resourceMethod"))),
			rule().condition((allTypes("properties","openpage")), (trigger("specific"))).output(expression().output(literal("path(\"")).output(mark("path")).output(literal("\");"))),
			rule().condition((allTypes("properties","switches")), (trigger("specific"))).output(expression().output(literal("state(io.intino.alexandria.ui.displays.components.switches.State.valueOf(\"")).output(mark("state")).output(literal("\"));"))),
			rule().condition((allTypes("properties","export")), (trigger("specific"))).output(expression().output(literal("from(")).output(mark("from")).output(literal("L);"))).output(literal("\n")).output(expression().output(literal("to(")).output(mark("to")).output(literal("L);"))).output(literal("\n")).output(expression().output(literal("min(")).output(mark("min")).output(literal("L);"))).output(literal("\n")).output(expression().output(literal("max(")).output(mark("max")).output(literal("L);"))).output(literal("\n")).output(expression().output(literal("range(")).output(mark("rangeMin")).output(literal(",")).output(mark("rangeMax")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("options(java.util.Arrays.asList(\"")).output(mark("option").multiple("\",\"")).output(literal("\"));"))),
			rule().condition((allTypes("properties","download")), (trigger("specific"))).output(expression().output(literal("options(java.util.Arrays.asList(\"")).output(mark("option").multiple("\",\"")).output(literal("\"));"))),
			rule().condition((allTypes("properties","downloadselection")), (trigger("specific"))).output(expression().output(literal("options(java.util.Arrays.asList(\"")).output(mark("option").multiple("\",\"")).output(literal("\"));"))),
			rule().condition((allTypes("properties","chart")), (trigger("specific"))).output(expression().output(literal("query(\"")).output(mark("query")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("serverUrl(\"")).output(mark("serverUrl")).output(literal("\");"))).output(literal("\n")).output(expression().output(mark("input", "inputMethod"))).output(literal("\n")).output(expression().output(literal("output(\"")).output(mark("output")).output(literal("\");"))),
			rule().condition((allTypes("properties","dashboard")), (trigger("specific"))).output(expression().output(mark("serverScript", "resourceMethod"))).output(literal("\n")).output(expression().output(mark("uiScript", "resourceMethod"))).output(literal("\n")).output(expression().output(mark("resource", "resourceMethod").multiple("\n"))).output(literal("\n")).output(expression().output(mark("parameter").multiple("\n"))),
			rule().condition((allTypes("properties","number")), (trigger("specific"))).output(expression().output(literal("value(")).output(mark("value")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("min(")).output(mark("min")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("max(")).output(mark("max")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("readonly(")).output(mark("readonly")).output(literal(");"))),
			rule().condition((allTypes("properties","date")), (trigger("specific"))).output(expression().output(literal("value(java.time.Instant.ofEpochMilli(")).output(mark("value")).output(literal("L));"))).output(literal("\n")).output(expression().output(literal("min(java.time.Instant.ofEpochMilli(")).output(mark("min")).output(literal("L));"))).output(literal("\n")).output(expression().output(literal("max(java.time.Instant.ofEpochMilli(")).output(mark("max")).output(literal("L));"))),
			rule().condition((allTypes("properties","text")), (trigger("specific"))).output(expression().output(literal("value(\"")).output(mark("defaultValue")).output(literal("\");"))).output(literal("\n")).output(expression().output(literal("readonly(")).output(mark("readonly")).output(literal(");"))),
			rule().condition((allTypes("properties","location")), (trigger("specific"))).output(expression().output(mark("icon", "resourceMethod"))).output(literal("\n")).output(expression().output(literal("value(\"")).output(mark("value")).output(literal("\");"))),
			rule().condition((allTypes("properties","collection","map")), (trigger("specific"))).output(expression().output(literal("type(io.intino.alexandria.ui.displays.components.Map.Type.valueOf(\"")).output(mark("type")).output(literal("\"));"))).output(literal("\n")).output(expression().output(mark("icon", "resourceMethod"))).output(literal("\n")).output(expression().output(mark("layer", "resourceMethod"))),
			rule().condition((allTypes("properties","collection")), (trigger("specific"))).output(expression().output(literal("source(new io.intino.alexandria.ui.sources.")).output(mark("sourceClass", "firstUpperCase")).output(literal("());"))).output(literal("\n")).output(expression().output(literal("pageSize(")).output(mark("pageSize")).output(literal(");"))),
			rule().condition((allTypes("properties","slider")), (trigger("specific"))).output(expression().output(literal("value(")).output(mark("value")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("range(")).output(mark("min")).output(literal(",")).output(mark("max")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("animation(")).output(mark("interval")).output(literal(",")).output(mark("loop")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("readonly(")).output(mark("readonly")).output(literal(");"))).output(literal("\n")).output(expression().output(mark("ordinal", "ordinalMethod").multiple("\n"))),
			rule().condition((allTypes("properties","temporalslider")), (trigger("specific"))).output(expression().output(literal("value(")).output(mark("value")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("range(java.time.Instant.ofEpochMilli(")).output(mark("min")).output(literal("L),java.time.Instant.ofEpochMilli(")).output(mark("max")).output(literal("L));"))).output(literal("\n")).output(expression().output(literal("animation(")).output(mark("interval")).output(literal(",")).output(mark("loop")).output(literal(");"))).output(literal("\n")).output(expression().output(literal("readonly(")).output(mark("readonly")).output(literal(");"))).output(literal("\n")).output(expression().output(mark("ordinal", "ordinalMethod").multiple("\n"))),
			rule().condition((allTypes("properties","collectiondialog")), (trigger("specific"))).output(expression().output(literal("allowSearch(")).output(mark("allowSearch")).output(literal(");"))),
			rule().condition((type("properties")), (trigger("specific"))),
			rule().condition((allTypes("inputMethod","csv")), (trigger("inputmethod"))).output(literal("input(new io.intino.alexandria.ui.displays.components.chart.datasources.CSVDataSource(")).output(mark("owner")).output(literal(".class.getResource(\"")).output(mark("value")).output(literal("\")));")),
			rule().condition((allTypes("parameter","dashboard"))).output(literal("add(\"")).output(mark("name")).output(literal("\", \"")).output(mark("value")).output(literal("\");")),
			rule().condition((allTypes("inputMethod","source")), (trigger("inputmethod"))).output(literal("input(new ")).output(mark("value")).output(literal("());")),
			rule().condition((trigger("resourcemethod"))).output(mark("name")).output(literal("(")).output(mark("owner")).output(literal(".class.getResource(\"")).output(mark("value")).output(literal("\"));")),
			rule().condition((trigger("ordinalmethod"))).output(literal("add(new io.intino.alexandria.ui.displays.components.slider.ordinals.")).output(mark("name", "firstUpperCase")).output(literal("Ordinal());")),
			rule().condition((allTypes("operationMode","iconbutton"))).output(literal("icon(\"")).output(mark("icon")).output(literal("\");")),
			rule().condition((allTypes("operationMode","materialiconbutton"))).output(literal("icon(\"")).output(mark("icon")).output(literal("\");")),
			rule().condition((type("badge"))).output(expression().output(literal("value(")).output(mark("value")).output(literal(");"))),
			rule().condition((type("selectionMethod"))).output(literal("public void onSelect(io.intino.alexandria.ui.displays.events.SelectionListener listener) {\n\tsuper.addSelectionListener(listener);\n}")),
			rule().condition((type("item")), (trigger("adddeclaration"))).output(mark("name")).output(literal("(add")).output(mark("name", "firstUpperCase")).output(literal("(new ")).output(mark("name", "firstUpperCase")).output(literal("(box())")).output(expression().output(literal(", ")).output(mark("itemVariable"))).output(literal("))")),
			rule().condition((type("item")), (trigger("type"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("item")), (trigger("addrow"))).output(mark("name")).output(literal(" = register(add")).output(mark("name", "firstUpperCase")).output(literal("(box()));")),
			rule().condition((type("item")), (trigger("property"))).output(literal("public ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name")).output(literal(";")),
			rule().condition((type("item")), (trigger("addmethod"))).output(mark("methodAccessibility")).output(literal(" ")).output(mark("name", "firstUpperCase")).output(literal(" create")).output(mark("methodName", "firstUpperCase")).output(literal("(")).output(expression().output(mark("itemClass")).output(literal(" element"))).output(literal(") {\n\t")).output(mark("name", "firstUpperCase")).output(literal(" result = new ")).output(mark("name", "firstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box)box());\n\tresult.id(java.util.UUID.randomUUID().toString());\n\t")).output(expression().output(literal("result.item(")).output(mark("itemVariable")).output(literal(");"))).output(literal("\n\treturn result;\n}")),
			rule().condition((allTypes("itemClass","map"))).output(literal("io.intino.alexandria.ui.model.PlaceMark<")).output(mark("value")).output(literal(">")),
			rule().condition((type("itemClass"))).output(mark("value")),
			rule().condition((allTypes("itemVariable","map"))).output(literal("element.item()")),
			rule().condition((type("itemVariable"))).output(literal("element"))
		);
	}
}