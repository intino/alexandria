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
			rule().add((condition("type", "component & reference")), (condition("trigger", "declarations"))).add(literal("public ")).add(expression().add(mark("ancestors", "firstUpperCase").multiple(".")).add(literal("."))).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")).add(expression().add(literal("\n")).add(mark("component", "declarations").multiple("\n"))),
			rule().add((condition("type", "component & reference")), (condition("trigger", "declaration"))).add(literal("public ")).add(expression().add(mark("ancestors", "firstUpperCase").multiple(".")).add(literal("."))).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "component & reference")), (condition("trigger", "class"))).add(literal("public class ")).add(mark("name", "firstUpperCase")).add(literal(" extends io.intino.alexandria.ui.displays.components.")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("abstractBox", "type")).add(literal("> {\n\t")).add(expression().add(mark("component", "declaration").multiple("\n")).add(literal("\n")).add(literal("\n")).add(literal("\t"))).add(literal("public ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n\t\tsuper(box);\n\t}\n\n\t@Override\n\tpublic void init() {\n\t\tsuper.init();")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "reference").multiple("\n"))).add(literal("\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("editable"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("component", "class").multiple("\n"))).add(literal("\n}")),
			rule().add((condition("type", "component & reference")), (condition("trigger", "childReferences"))).add(mark("name")).add(literal(" = ")).add(expression().add(mark("ancestors").multiple(".")).add(literal("."))).add(mark("name")).add(literal(";")).add(expression().add(literal("\n")).add(mark("component", "childReferences").multiple("\n"))),
			rule().add((condition("type", "component & reference")), (condition("trigger", "references"))).add(mark("name")).add(literal(" = addAndPersonify(new ")).add(mark("name", "firstUpperCase")).add(literal("(box()).id(\"")).add(mark("id")).add(literal("\"));")).add(expression().add(literal("\n")).add(mark("component", "childReferences").multiple("\n"))),
			rule().add((condition("type", "component & reference"))).add(mark("name")).add(literal(" = addAndPersonify(new ")).add(mark("name", "firstUpperCase")).add(literal("(box()).id(\"")).add(mark("id")).add(literal("\"));")),
			rule().add((condition("type", "editable & filevalue"))).add(literal("public void onChange(io.intino.alexandria.ui.displays.events.ChangeListener listener) { this.changeListener = listener; }")),
			rule().add((condition("type", "editable"))).add(literal("public void onChange(io.intino.alexandria.ui.displays.events.ChangeListener listener) { this.changeListener = listener; }\npublic void onKeyPress(io.intino.alexandria.ui.displays.events.KeyPressListener listener) { this.keyPressListener = listener; }"))
		);
		return this;
	}
}