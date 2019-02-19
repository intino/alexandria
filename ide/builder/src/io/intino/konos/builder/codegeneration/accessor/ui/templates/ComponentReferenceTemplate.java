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
			rule().add((condition("type", "block & reference"))).add(literal("<Ui.")).add(mark("type", "firstUpperCase")).add(mark("input")).add(literal(" id=\"")).add(mark("id")).add(literal("\"")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal(">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "child").multiple("\n")).add(literal("\n"))).add(literal("</Ui.")).add(mark("type", "firstUpperCase")).add(mark("input")).add(literal(">")),
			rule().add((condition("type", "component & reference"))).add(literal("<Ui.")).add(mark("type", "firstUpperCase")).add(mark("input")).add(literal(" id=\"")).add(mark("id")).add(literal("\"")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal("/>")),
			rule().add((condition("type", "input"))).add(literal("Input")),
			rule().add((condition("type", "properties")), (condition("trigger", "common"))).add(expression().add(literal(" label=\"")).add(mark("label")).add(literal("\""))),
			rule().add((condition("type", "properties & block")), (condition("trigger", "specific"))).add(expression().add(literal(" layout=\"")).add(mark("layout").multiple(" ")).add(literal("\""))).add(expression().add(literal(" ")).add(mark("paper"))),
			rule().add((condition("type", "properties & textvalue")), (condition("trigger", "specific"))).add(expression().add(literal(" format=\"")).add(mark("format")).add(literal("\""))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))),
			rule().add((condition("type", "properties")), (condition("trigger", "specific")))
		);
		return this;
	}
}