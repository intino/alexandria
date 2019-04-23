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
			rule().add((condition("type", "reference"))).add(literal("<Displays.")).add(mark("name", "firstUppercase")).add(literal(" context={this._context.bind(this)} owner={this._owner.bind(this)} id=\"")).add(mark("id")).add(literal("\"")).add(mark("properties", "common")).add(mark("properties", "specific")).add(literal("></Displays.")).add(mark("name", "firstUppercase")).add(literal(">")),
			rule().add((condition("type", "child")), (condition("trigger", "declaration"))),
			rule().add((condition("type", "block & child")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "child")), (condition("trigger", "child"))).add(mark("value")),
			rule().add((condition("type", "child")), (condition("trigger", "add"))),
			rule().add((condition("type", "component & child & item"))),
			rule().add((condition("type", "component & child"))).add(literal("<")).add(mark("extends")).add(literal(" context={this._context.bind(this)} owner={this._owner.bind(this)} id=\"")).add(mark("id")).add(literal("\"")).add(mark("properties", "common")).add(mark("properties", "specific")).add(expression().add(literal(" ")).add(mark("code"))).add(literal(">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("reference").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("methods"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("component", "child").multiple("\n"))).add(expression().add(literal("\n"))).add(literal("</")).add(mark("extends")).add(literal(">")),
			rule().add((condition("type", "extends & multiple"))).add(literal("Ui.Multiple")),
			rule().add((condition("type", "extends & stamp"))).add(literal("Displays.")).add(mark("type", "firstUpperCase")),
			rule().add((condition("type", "extends"))).add(literal("Ui.")).add(mark("type", "firstUpperCase")).add(mark("facet")),
			rule().add((condition("type", "facet"))).add(mark("name", "firstUpperCase")),
			rule().add((condition("type", "method"))),
			rule().add((condition("type", "properties & multiple")), (condition("trigger", "common"))).add(expression().add(literal(" label=\"")).add(mark("label")).add(literal("\""))).add(expression().add(literal(" format=\"")).add(mark("format").multiple(" ")).add(literal("\""))),
			rule().add((condition("type", "properties & operation")), (condition("trigger", "common"))).add(expression().add(literal(" label=\"")).add(mark("label")).add(literal("\""))).add(expression().add(literal(" format=\"")).add(mark("format").multiple(" ")).add(literal("\""))),
			rule().add((condition("type", "properties")), (condition("trigger", "common"))).add(expression().add(literal(" label=\"")).add(mark("label")).add(literal("\""))).add(expression().add(literal(" format=\"")).add(mark("format").multiple(" ")).add(literal("\""))).add(expression().add(literal(" multiple={{instances:\"")).add(mark("instances")).add(literal("\",arrangement:\"")).add(mark("multipleArrangement")).add(literal("\",noItemsMessage:\"")).add(mark("multipleNoItemsMessage")).add(literal("\"}}"))).add(expression().add(literal(" color=\"")).add(mark("color")).add(literal("\""))),
			rule().add((condition("type", "properties & template")), (condition("trigger", "specific"))).add(expression().add(literal(" layout=\"")).add(mark("layout").multiple(" ")).add(literal("\""))).add(expression().add(literal(" width=\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(" height=\"")).add(mark("height")).add(literal("\""))).add(expression().add(literal(" spacing=\"")).add(mark("spacing")).add(literal("\""))),
			rule().add((condition("type", "properties & image")), (condition("trigger", "specific"))).add(expression().add(literal(" width=\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(" height=\"")).add(mark("height")).add(literal("\""))),
			rule().add((condition("type", "properties & file")), (condition("trigger", "specific"))).add(expression().add(literal(" width=\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(" height=\"")).add(mark("height")).add(literal("\""))),
			rule().add((condition("type", "properties & chart")), (condition("trigger", "specific"))).add(expression().add(literal(" width=\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(" height=\"")).add(mark("height")).add(literal("\""))),
			rule().add((condition("type", "properties & block")), (condition("trigger", "specific"))).add(expression().add(literal(" layout=\"")).add(mark("layout").multiple(" ")).add(literal("\""))).add(expression().add(literal(" width=\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(" height=\"")).add(mark("height")).add(literal("\""))).add(expression().add(literal(" spacing=\"")).add(mark("spacing")).add(literal("\""))).add(expression().add(literal(" collapsible=\"")).add(mark("collapsible")).add(literal("\""))).add(expression().add(literal(" ")).add(mark("paper"))).add(expression().add(literal(" ")).add(mark("badge"))),
			rule().add((condition("type", "properties & date")), (condition("trigger", "specific"))).add(expression().add(literal(" pattern=\"")).add(mark("pattern")).add(literal("\""))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))).add(expression().add(literal(" value={new Date(")).add(mark("value")).add(literal(")}"))).add(expression().add(literal(" timePicker={")).add(mark("timePicker")).add(literal("}"))).add(expression().add(literal(" mask=\"")).add(mark("mask")).add(literal("\""))),
			rule().add((condition("type", "properties & number")), (condition("trigger", "specific"))).add(expression().add(literal(" value={")).add(mark("value")).add(literal("}"))).add(expression().add(literal(" prefix=\"")).add(mark("prefix")).add(literal("\""))).add(expression().add(literal(" suffix=\"")).add(mark("suffix")).add(literal("\""))).add(expression().add(literal(" min={")).add(mark("min")).add(literal("}"))).add(expression().add(literal(" max={")).add(mark("max")).add(literal("}"))).add(expression().add(literal(" step={")).add(mark("step")).add(literal("}"))),
			rule().add((condition("type", "properties & header")), (condition("trigger", "specific"))).add(expression().add(literal(" position=\"")).add(mark("position")).add(literal("\""))),
			rule().add((condition("type", "properties & codeText")), (condition("trigger", "specific"))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))).add(expression().add(literal(" language=\"")).add(mark("language")).add(literal("\""))).add(expression().add(literal(" highlighted=")).add(mark("highlighted"))),
			rule().add((condition("type", "properties & text")), (condition("trigger", "specific"))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))).add(expression().add(literal(" prefix=\"")).add(mark("prefix")).add(literal("\""))).add(expression().add(literal(" suffix=\"")).add(mark("suffix")).add(literal("\""))).add(expression().add(literal(" value=\"")).add(mark("defaultValue")).add(literal("\""))).add(expression().add(literal(" highlighted={")).add(mark("highlighted")).add(literal("}"))),
			rule().add((condition("type", "properties & openpage")), (condition("trigger", "specific"))).add(expression().add(literal(" title=\"")).add(mark("title")).add(literal("\""))).add(expression().add(literal(" target=\"")).add(mark("target")).add(literal("\""))),
			rule().add((condition("type", "properties & collection")), (condition("trigger", "specific"))).add(expression().add(literal(" noItemsMessage=\"")).add(mark("noItemsMessage")).add(literal("\""))).add(expression().add(literal(" pageSize={")).add(mark("pageSize")).add(literal("}"))).add(expression().add(literal(" itemHeight={")).add(mark("itemHeight")).add(literal("}"))).add(expression().add(literal(" itemsWidth={")).add(literal("[")).add(mark("itemWidth").multiple(",")).add(literal("]")).add(literal("}"))).add(expression().add(literal(" scrollingMark={")).add(mark("scrollingMark")).add(literal("}"))),
			rule().add((condition("type", "properties & spinner")), (condition("trigger", "specific"))).add(expression().add(literal(" mode=\"")).add(mark("mode")).add(literal("\""))),
			rule().add((condition("type", "properties")), (condition("trigger", "specific"))),
			rule().add((condition("type", "badge"))).add(literal("mode=\"")).add(mark("mode")).add(literal("\"")).add(expression().add(literal(" value={")).add(mark("value")).add(literal("}"))).add(expression().add(literal(" max={")).add(mark("max")).add(literal("}"))).add(expression().add(literal(" showZero={")).add(mark("showZero")).add(literal("}"))),
			rule().add((condition("type", "codetext"))).add(literal("value=\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "highlighted"))).add(literal("{text:\"")).add(mark("text")).add(literal("\",background:\"")).add(mark("background")).add(literal("\"}"))
		);
		return this;
	}
}