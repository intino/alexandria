package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ComponentTemplate extends Template {

	protected ComponentTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ComponentTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "reference")), (condition("trigger", "declaration"))).add(literal("public ")).add(expression().add(mark("ancestors", "firstUpperCase").multiple(".")).add(literal("."))).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "reference"))).add(mark("name")).add(literal(" = register(new ")).add(mark("name", "firstUpperCase")).add(literal("((")).add(mark("box", "firstUpperCase")).add(literal("Box)box()).<")).add(mark("name", "firstUpperCase")).add(literal(">id(\"")).add(mark("id")).add(literal("\").owner(")).add(mark("owner")).add(literal(".this));")),
			rule().add((condition("type", "component & child & moldableblock & multipleblock")), (condition("trigger", "declarations"))),
			rule().add((condition("type", "component & child & moldableblock")), (condition("trigger", "declarations"))).add(literal("public ")).add(mark("mold", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "component & child")), (condition("trigger", "declarations"))).add(literal("public ")).add(expression().add(mark("ancestors", "firstUpperCase").multiple(".")).add(literal("."))).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")).add(expression().add(literal("\n")).add(mark("component", "declarations").multiple("\n"))),
			rule().add((condition("type", "component & child & moldableblock & multipleblock")), (condition("trigger", "declaration"))),
			rule().add((condition("type", "component & child & moldableblock")), (condition("trigger", "declaration"))).add(literal("public ")).add(mark("mold", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "component & child")), (condition("trigger", "declaration"))).add(literal("public ")).add(expression().add(mark("ancestors", "firstUpperCase").multiple(".")).add(literal("."))).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "component & child & moldableblock & multipleblock")), (condition("trigger", "method"))).add(literal("public void add")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("mold", "firstUpperCase")).add(literal(" instance) {\n\tinstance.id(java.util.UUID.randomUUID().toString());\n\t")).add(mark("owner")).add(literal(".this.add(instance, \"")).add(mark("name")).add(literal("\");\n}")),
			rule().add((condition("type", "component & child")), (condition("trigger", "method"))),
			rule().add((condition("type", "component & child & moldableblock & multipleblock")), (condition("trigger", "class"))),
			rule().add((condition("type", "component & child & moldableblock")), (condition("trigger", "class"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "class"))).add(literal("public class ")).add(mark("name", "firstUpperCase")).add(literal(" extends ")).add(mark("extends")).add(expression().add(literal(" ")).add(mark("implements"))).add(literal(" {\n\t")).add(expression().add(mark("reference", "declaration").multiple("\n")).add(literal("\n")).add(literal("\t"))).add(expression().add(mark("component", "declaration").multiple("\n"))).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n\t\tsuper(box);\n\t}\n\n\t@Override\n\tpublic void init() {\n\t\tsuper.init();")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("reference").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "child").multiple("\n"))).add(literal("\n\t}\n\t")).add(expression().add(mark("component", "class").multiple("\n"))).add(literal("\n\t")).add(expression().add(mark("component", "method").multiple("\n"))).add(literal("\n}")),
			rule().add((condition("type", "extends & moldableblock"))).add(mark("type", "firstUpperCase")),
			rule().add((condition("type", "extends"))).add(literal("io.intino.alexandria.ui.displays.components.")).add(mark("type", "firstUpperCase")).add(mark("facet")).add(literal("<")).add(mark("abstractBox", "type")).add(literal(">")),
			rule().add((condition("type", "component & child & multipleblock")), (condition("trigger", "moldChildReferences"))),
			rule().add((condition("type", "component & child & multipleblock")), (condition("trigger", "blockChildReferences"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "moldChildReferences"))).add(mark("name")).add(literal(" = ")).add(expression().add(mark("ancestorsNotMe").multiple(".")).add(literal("."))).add(mark("name")).add(literal(";")).add(expression().add(literal("\n")).add(mark("component", "moldChildReferences").multiple("\n"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "blockChildReferences"))).add(mark("name")).add(literal(" = ")).add(expression().add(mark("ancestorsNotMe").multiple(".")).add(literal("."))).add(mark("name")).add(literal(";")).add(expression().add(literal("\n")).add(mark("component", "blockChildReferences").multiple("\n"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "childReferences"))).add(mark("name")).add(literal(" = ")).add(expression().add(mark("ancestors").multiple(".")).add(literal("."))).add(mark("name")).add(literal(";")).add(expression().add(literal("\n")).add(mark("component", "childReferences").multiple("\n"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "moldReferences"))).add(mark("name")).add(literal(" = register(new ")).add(mark("name", "firstUpperCase")).add(literal("(box()).<")).add(mark("name", "firstUpperCase")).add(literal(">id(\"")).add(mark("id")).add(literal("\").owner(")).add(mark("owner")).add(literal(".this));")).add(expression().add(literal("\n")).add(mark("component", "moldChildReferences").multiple("\n"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "blockReferences"))).add(mark("name")).add(literal(" = register(new ")).add(mark("name", "firstUpperCase")).add(literal("(box()).<")).add(mark("name", "firstUpperCase")).add(literal(">id(\"")).add(mark("id")).add(literal("\").owner(")).add(mark("owner")).add(literal(".this));")).add(expression().add(literal("\n")).add(mark("component", "blockChildReferences").multiple("\n"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "references"))).add(mark("name")).add(literal(" = register(new ")).add(mark("name", "firstUpperCase")).add(literal("(box()).<")).add(mark("name", "firstUpperCase")).add(literal(">id(\"")).add(mark("id")).add(literal("\").owner(")).add(mark("owner")).add(literal(".this));")).add(expression().add(literal("\n")).add(mark("component", "childReferences").multiple("\n"))),
			rule().add((condition("type", "component & child")), (condition("trigger", "initializations"))).add(mark("binding")).add(expression().add(literal("\n")).add(mark("component", "initializations").multiple("\n"))),
			rule().add((condition("type", "component & child & moldableblock & multipleblock"))),
			rule().add((condition("type", "component & child & moldableblock"))).add(mark("name")).add(literal(" = register(new ")).add(mark("mold", "firstUpperCase")).add(literal("((")).add(mark("box", "firstUpperCase")).add(literal("Box)box())")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal(".id(\"")).add(mark("id")).add(literal("\"));")),
			rule().add((condition("type", "component & child"))).add(mark("name")).add(literal(" = register(new ")).add(mark("name", "firstUpperCase")).add(literal("(box())")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal(".<")).add(mark("name", "firstUpperCase")).add(literal(">id(\"")).add(mark("id")).add(literal("\").owner(")).add(mark("owner")).add(literal(".this));")),
			rule().add((condition("type", "facet"))).add(mark("name", "firstUpperCase")),
			rule().add((condition("type", "binding"))).add(mark("name")).add(literal(".bindTo(")).add(mark("selector")).add(literal(", \"")).add(mark("option")).add(literal("\");")),
			rule().add((condition("type", "implements & option"))).add(literal("implements io.intino.alexandria.ui.displays.components.selector.SelectorOption")),
			rule().add((condition("type", "implements"))).add(literal("implements --undefined--")),
			rule().add((condition("type", "properties & block")), (condition("trigger", "common"))).add(expression().add(literal(".label(\"")).add(mark("label")).add(literal("\")"))),
			rule().add((condition("type", "properties")), (condition("trigger", "common"))),
			rule().add((condition("type", "properties")), (condition("trigger", "specific")))
		);
		return this;
	}
}