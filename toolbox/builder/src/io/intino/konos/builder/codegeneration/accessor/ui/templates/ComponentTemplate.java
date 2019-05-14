package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ComponentTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("reference"))).output(literal("<Displays.")).output(mark("name", "firstUppercase")).output(literal(" context={this._context.bind(this)} owner={this._owner.bind(this)} id=\"")).output(mark("id")).output(literal("\"")).output(mark("properties", "common")).output(mark("properties", "specific")).output(literal("></Displays.")).output(mark("name", "firstUppercase")).output(literal(">")),
			rule().condition((type("child")), (trigger("declaration"))),
			rule().condition((allTypes("block","child")), (trigger("child"))).output(mark("")),
			rule().condition((type("child")), (trigger("child"))).output(mark("")),
			rule().condition((type("child")), (trigger("add"))),
			rule().condition((allTypes("component","item","child"))),
			rule().condition((allTypes("component","child"))).output(literal("<")).output(mark("extends")).output(literal(" context={this._context.bind(this)} owner={this._owner.bind(this)} id=\"")).output(mark("id")).output(literal("\"")).output(mark("properties", "common")).output(mark("properties", "specific")).output(expression().output(literal(" ")).output(mark("code"))).output(literal(">\n\t")).output(expression().output(mark("reference").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("methods"))).output(literal("\n\t")).output(expression().output(mark("component", "child").multiple("\n"))).output(literal("\n")).output(expression().output(literal("</")).output(mark("extends")).output(literal(">"))),
			rule().condition((allTypes("extends","multiple"))).output(literal("Ui.Multiple")),
			rule().condition((allTypes("extends","stamp"))).output(literal("Displays.")).output(mark("type", "firstUpperCase")),
			rule().condition((type("extends"))).output(literal("Ui.")).output(mark("type", "firstUpperCase")).output(mark("facet")),
			rule().condition((type("facet"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("method"))),
			rule().condition((allTypes("multiple","properties")), (trigger("common"))).output(expression().output(literal(" label=\"")).output(mark("label")).output(literal("\""))).output(expression().output(literal(" format=\"")).output(mark("format").multiple(" ")).output(literal("\""))),
			rule().condition((allTypes("operation","properties")), (trigger("common"))).output(expression().output(literal(" title=\"")).output(mark("title")).output(literal("\""))).output(expression().output(literal(" target=\"")).output(mark("target")).output(literal("\""))).output(expression().output(literal(" mode=\"")).output(mark("operationMode")).output(literal("\""))).output(expression().output(literal(" icon=\"")).output(mark("icon")).output(literal("\""))).output(expression().output(literal(" confirm=\"")).output(mark("confirm")).output(literal("\""))).output(expression().output(literal(" disabled={")).output(mark("disabled")).output(literal("}"))).output(expression().output(literal(" size=\"")).output(mark("size")).output(literal("\""))),
			rule().condition((type("properties")), (trigger("common"))).output(expression().output(literal(" label=\"")).output(mark("label")).output(literal("\""))).output(expression().output(literal(" format=\"")).output(mark("format").multiple(" ")).output(literal("\""))).output(expression().output(literal(" multiple={{instances:\"")).output(mark("instances")).output(literal("\",arrangement:\"")).output(mark("multipleArrangement")).output(literal("\",noItemsMessage:\"")).output(mark("multipleNoItemsMessage")).output(literal("\"}}"))).output(expression().output(literal(" color=\"")).output(mark("color")).output(literal("\""))),
			rule().condition((allTypes("template","properties")), (trigger("specific"))).output(expression().output(literal(" layout=\"")).output(mark("layout").multiple(" ")).output(literal("\""))).output(expression().output(literal(" width=\"")).output(mark("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(mark("height")).output(literal("\""))).output(expression().output(literal(" spacing=\"")).output(mark("spacing")).output(literal("\""))),
			rule().condition((allTypes("image","properties")), (trigger("specific"))).output(expression().output(literal(" width=\"")).output(mark("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(mark("height")).output(literal("\""))),
			rule().condition((allTypes("file","properties")), (trigger("specific"))).output(expression().output(literal(" width=\"")).output(mark("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(mark("height")).output(literal("\""))),
			rule().condition((allTypes("chart","properties")), (trigger("specific"))).output(expression().output(literal(" width=\"")).output(mark("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(mark("height")).output(literal("\""))),
			rule().condition((allTypes("block","properties")), (trigger("specific"))).output(expression().output(literal(" layout=\"")).output(mark("layout").multiple(" ")).output(literal("\""))).output(expression().output(literal(" width=\"")).output(mark("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(mark("height")).output(literal("\""))).output(expression().output(literal(" spacing=\"")).output(mark("spacing")).output(literal("\""))).output(expression().output(literal(" collapsible=\"")).output(mark("collapsible")).output(literal("\""))).output(expression().output(literal(" ")).output(mark("paper"))).output(expression().output(literal(" ")).output(mark("badge"))),
			rule().condition((allTypes("date","properties")), (trigger("specific"))).output(expression().output(literal(" pattern=\"")).output(mark("pattern")).output(literal("\""))).output(expression().output(literal(" mode=\"")).output(mark("mode")).output(literal("\""))).output(expression().output(literal(" value={new Date(")).output(mark("value")).output(literal(")}"))).output(expression().output(literal(" timePicker={")).output(mark("timePicker")).output(literal("}"))).output(expression().output(literal(" mask=\"")).output(mark("mask")).output(literal("\""))),
			rule().condition((allTypes("number","properties")), (trigger("specific"))).output(expression().output(literal(" value={")).output(mark("value")).output(literal("}"))).output(expression().output(literal(" prefix=\"")).output(mark("prefix")).output(literal("\""))).output(expression().output(literal(" suffix=\"")).output(mark("suffix")).output(literal("\""))).output(expression().output(literal(" min={")).output(mark("min")).output(literal("}"))).output(expression().output(literal(" max={")).output(mark("max")).output(literal("}"))).output(expression().output(literal(" step={")).output(mark("step")).output(literal("}"))),
			rule().condition((allTypes("header","properties")), (trigger("specific"))).output(expression().output(literal(" position=\"")).output(mark("position")).output(literal("\""))),
			rule().condition((allTypes("codetext","properties")), (trigger("specific"))).output(expression().output(literal(" mode=\"")).output(mark("mode")).output(literal("\""))).output(expression().output(literal(" language=\"")).output(mark("language")).output(literal("\""))).output(expression().output(literal(" highlighted=")).output(mark("highlighted"))),
			rule().condition((allTypes("text","properties")), (trigger("specific"))).output(expression().output(literal(" mode=\"")).output(mark("mode")).output(literal("\""))).output(expression().output(literal(" prefix=\"")).output(mark("prefix")).output(literal("\""))).output(expression().output(literal(" suffix=\"")).output(mark("suffix")).output(literal("\""))).output(expression().output(literal(" value=\"")).output(mark("defaultValue")).output(literal("\""))).output(expression().output(literal(" highlighted={")).output(mark("highlighted")).output(literal("}"))),
			rule().condition((allTypes("collection","properties")), (trigger("specific"))).output(expression().output(literal(" noItemsMessage=\"")).output(mark("noItemsMessage")).output(literal("\""))).output(expression().output(literal(" pageSize={")).output(mark("pageSize")).output(literal("}"))).output(expression().output(literal(" itemHeight={")).output(mark("itemHeight")).output(literal("}"))).output(expression().output(literal(" scrollingMark={")).output(mark("scrollingMark")).output(literal("}"))).output(expression().output(literal(" navigable=\"")).output(mark("navigable")).output(literal("\""))).output(expression().output(literal(" selection=\"")).output(mark("selection")).output(literal("\""))),
			rule().condition((allTypes("heading","properties")), (trigger("specific"))).output(literal(" style={{width:\"calc(")).output(mark("width")).output(literal("% - ")).output(mark("marginSize")).output(literal("px)\",marginRight:\"10px\"")).output(expression().output(literal(",")).output(mark("marginLeft")).output(literal(":\"10px\""))).output(literal("}}")),
			rule().condition((allTypes("item","properties")), (trigger("specific"))).output(literal(" style={{width:\"calc(")).output(mark("width")).output(literal("% - ")).output(mark("marginSize")).output(literal("px)\",marginRight:\"10px\"")).output(expression().output(literal(",")).output(mark("marginLeft")).output(literal(":\"10px\""))).output(literal("}}")),
			rule().condition((allTypes("properties","spinner")), (trigger("specific"))).output(expression().output(literal(" mode=\"")).output(mark("mode")).output(literal("\""))),
			rule().condition((allTypes("export","properties")), (trigger("specific"))).output(expression().output(literal(" from={")).output(mark("from")).output(literal("}"))).output(expression().output(literal(" to={")).output(mark("to")).output(literal("}"))).output(expression().output(literal(" min={")).output(mark("min")).output(literal("}"))).output(expression().output(literal(" max={")).output(mark("max")).output(literal("}"))).output(expression().output(literal(" range={{min:")).output(mark("rangeMin")).output(literal(",max:")).output(mark("rangeMax")).output(literal("}}"))).output(expression().output(literal(" options={")).output(literal("[")).output(literal("\"")).output(mark("option").multiple("\",\"")).output(literal("\"")).output(literal("]")).output(literal("}"))),
			rule().condition((allTypes("download","properties")), (trigger("specific"))).output(expression().output(literal(" options={")).output(literal("[")).output(literal("\"")).output(mark("option").multiple("\",\"")).output(literal("\"")).output(literal("]")).output(literal("}"))),
			rule().condition((allTypes("downloadselection","properties")), (trigger("specific"))).output(expression().output(literal(" options={")).output(literal("[")).output(literal("\"")).output(mark("option").multiple("\",\"")).output(literal("\"")).output(literal("]")).output(literal("}"))),
			rule().condition((type("properties")), (trigger("specific"))),
			rule().condition((type("badge"))).output(literal("mode=\"")).output(mark("mode")).output(literal("\"")).output(expression().output(literal(" value={")).output(mark("value")).output(literal("}"))).output(expression().output(literal(" max={")).output(mark("max")).output(literal("}"))).output(expression().output(literal(" showZero={")).output(mark("showZero")).output(literal("}"))),
			rule().condition((type("codetext"))).output(literal("value=\"")).output(mark("value")).output(literal("\"")),
			rule().condition((type("highlighted"))).output(literal("{text:\"")).output(mark("text")).output(literal("\",background:\"")).output(mark("background")).output(literal("\"}")),
			rule().condition((type("operationmode"))).output(mark("mode")),
			rule().condition((type("histogram"))).output(literal("{alwaysVisible:")).output(mark("alwaysVisible")).output(literal(",type:\"")).output(mark("type")).output(literal("\"}"))
		);
	}
}