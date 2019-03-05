package io.intino.konos.builder.codegeneration.accessor.ui.templates;

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
			rule().add((condition("type", "reference"))).add(literal("<Displays.")).add(mark("name", "firstUppercase")).add(literal(" id=\"")).add(mark("id")).add(literal("\"")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal("></Displays.")).add(mark("name", "firstUppercase")).add(literal(">")),
			rule().add((condition("type", "child")), (condition("trigger", "declaration"))),
			rule().add((condition("type", "block & child")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "child")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "child")), (condition("trigger", "add"))),
			rule().add((condition("type", "component & child"))).add(literal("<Ui.")).add(mark("type", "firstUpperCase")).add(mark("facet")).add(literal(" id=\"")).add(mark("id")).add(literal("\"")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal(">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("reference").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "child").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("code")).add(literal("\n"))).add(literal("</Ui.")).add(mark("type", "firstUpperCase")).add(mark("facet")).add(literal(">")),
			rule().add((condition("type", "facet"))).add(mark("name", "firstUpperCase")),
			rule().add((condition("type", "properties")), (condition("trigger", "common"))).add(expression().add(literal(" label=\"")).add(mark("label")).add(literal("\""))).add(expression().add(literal(" styleName=\"")).add(mark("style")).add(literal("\""))),
			rule().add((condition("type", "properties & block")), (condition("trigger", "specific"))).add(expression().add(literal(" layout=\"")).add(mark("layout").multiple(" ")).add(literal("\""))).add(expression().add(literal(" width=\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(" height=\"")).add(mark("height")).add(literal("\""))).add(expression().add(literal(" spacing=\"")).add(mark("spacing")).add(literal("\""))).add(expression().add(literal(" collapsible=\"")).add(mark("collapsible")).add(literal("\""))).add(expression().add(literal(" ")).add(mark("paper"))),
			rule().add((condition("type", "properties & date")), (condition("trigger", "specific"))).add(expression().add(literal(" format=\"")).add(mark("format")).add(literal("\""))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))),
			rule().add((condition("type", "properties & header")), (condition("trigger", "specific"))).add(expression().add(literal(" color=\"")).add(mark("color")).add(literal("\""))).add(expression().add(literal(" position=\"")).add(mark("position")).add(literal("\""))),
			rule().add((condition("type", "properties & text")), (condition("trigger", "specific"))).add(expression().add(literal(" format=\"")).add(mark("format")).add(literal("\""))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))).add(expression().add(literal(" value=\"")).add(mark("defaultValue")).add(literal("\""))),
			rule().add((condition("type", "properties")), (condition("trigger", "specific"))),
			rule().add((condition("type", "codetext"))).add(mark("value"))
		);
		return this;
	}
}