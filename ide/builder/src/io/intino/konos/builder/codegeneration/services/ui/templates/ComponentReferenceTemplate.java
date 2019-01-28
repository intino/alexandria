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
			rule().add((condition("type", "reference")), (condition("trigger", "declaration"))).add(literal("protected Alexandria")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("abstractBox", "type")).add(literal("> ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "block & reference")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "reference")), (condition("trigger", "child"))).add(literal("Alexandria")).add(mark("type", "firstUpperCase")).add(literal("<")).add(mark("abstractBox", "type")).add(literal("> ")).add(mark("value")),
			rule().add((condition("type", "reference")), (condition("trigger", "add"))).add(mark("parent")).add(literal(".add(")).add(mark("name")).add(literal(");")),
			rule().add((condition("type", "block & reference"))).add(mark("component", "child").multiple("\n")).add(literal("\n")).add(expression().add(literal("Alexandria")).add(mark("addType", "firstUpperCase")).add(literal(" "))).add(mark("name")).add(literal(" = new Alexandria")).add(mark("type", "firstUpperCase")).add(literal("<>(box());\n")).add(mark("component", "add").multiple("\n")),
			rule().add((condition("type", "component & reference"))).add(mark("name")).add(literal(" = new Alexandria")).add(mark("type", "firstUpperCase")).add(literal("<>(box());"))
		);
		return this;
	}
}