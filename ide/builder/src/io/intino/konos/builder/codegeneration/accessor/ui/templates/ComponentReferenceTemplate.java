package io.intino.konos.builder.codegeneration.accessor.ui.templates;

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
			rule().add((condition("type", "reference")), (condition("trigger", "declaration"))),
			rule().add((condition("type", "block & reference")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "reference")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "reference")), (condition("trigger", "add"))),
			rule().add((condition("type", "block & reference"))).add(literal("<Ui.")).add(mark("type", "firstUpperCase")).add(literal(" id=\"")).add(mark("name")).add(literal("\">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "child").multiple("\n")).add(literal("\n"))).add(literal("</Ui.")).add(mark("type", "firstUpperCase")).add(literal(">")),
			rule().add((condition("type", "component & reference"))).add(literal("<Ui.")).add(mark("type", "firstUpperCase")).add(literal(" id=\"")).add(mark("name")).add(literal("\"/>"))
		);
		return this;
	}
}