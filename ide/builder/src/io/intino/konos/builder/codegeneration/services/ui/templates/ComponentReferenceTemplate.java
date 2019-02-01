package io.intino.konos.builder.codegeneration.services.ui.templates;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ComponentReferenceTemplate extends Template {

	protected ComponentReferenceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ComponentReferenceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "component & reference")), (condition("trigger", "declaration"))).add(literal("public ")).add(mark("name", "firstUpperCase")).add(literal(" ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "component & reference")), (condition("trigger", "class"))).add(literal("public class ")).add(mark("name", "firstUpperCase")).add(literal(" extends io.intino.alexandria.ui.displays.components.")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("abstractBox", "type")).add(literal("> {\n\t")).add(expression().add(mark("component", "declaration").multiple("\n")).add(literal("\n")).add(literal("\n")).add(literal("\t"))).add(literal("public ")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("abstractBox", "type")).add(literal(" box) {\n\t\tsuper(box);")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("component", "reference").multiple("\n"))).add(literal("\n\t}")).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\t")).add(mark("component", "class").multiple("\n"))).add(literal("\n}")),
			rule().add((condition("type", "component & reference"))).add(mark("name")).add(literal(" = new ")).add(mark("name", "firstUpperCase")).add(literal("(box());"))
		);
		return this;
	}
}