package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WidgetRequesterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("accessible","widget"))).output(literal("var ")).output(mark("name", "FirstUpperCase")).output(literal("ProxyBehaviors = ")).output(mark("name", "FirstUpperCase")).output(literal("ProxyBehaviors || {};\n\n")).output(mark("name", "FirstUpperCase")).output(literal("ProxyBehaviors.Requester = {\n\n\tregisterPersonifiedDisplay : function(value) {\n\t\tthis.carry(\"registerPersonifiedDisplay\", { \"value\" : value });\n\t}\n\n};")),
			rule().condition((type("widget"))).output(literal("var ")).output(mark("name", "FirstUpperCase")).output(literal("Behaviors = ")).output(mark("name", "FirstUpperCase")).output(literal("Behaviors || {};\n\n")).output(mark("name", "FirstUpperCase")).output(literal("Behaviors.Requester = {\n\n    ")).output(mark("request").multiple(",\n")).output(literal("\n\n};")),
			rule().condition((allTypes("addressable","parameter")), (trigger("request"))).output(literal("route")).output(mark("name", "firstUpperCase")).output(literal(" : function(fromDisplay, value) {\n\tif (this.display == null || fromDisplay == null || fromDisplay.id != this.display.id) return;\n\tthis.carry(\"")).output(mark("name")).output(literal("\", { \"value\" : value });\n},\n")).output(mark("name")).output(literal(" : function(value) {\n\tvar pathsElement = this.querySelector(\"")).output(mark("widget", "camelCaseToSnakeCase")).output(literal("-routes\");\n\tif (pathsElement == null) pathsElement = this.parentElement.querySelector(\"")).output(mark("widget", "camelCaseToSnakeCase")).output(literal("-routes\")\n\tpathsElement.")).output(mark("name")).output(literal("(value);\n}")),
			rule().condition((type("addressable")), not(type("parameter")), (trigger("request"))).output(literal("route")).output(mark("name", "firstUpperCase")).output(literal(" : function(fromDisplay) {\n\tif (this.display == null || fromDisplay == null || fromDisplay.id != this.display.id) return;\n\tthis.carry(\"")).output(mark("name")).output(literal("\");\n},\n")).output(mark("name")).output(literal(" : function() {\n\tvar pathsElement = this.querySelector(\"")).output(mark("widget", "camelCaseToSnakeCase")).output(literal("-routes\");\n\tif (pathsElement == null) pathsElement = this.parentElement.querySelector(\"")).output(mark("widget", "camelCaseToSnakeCase")).output(literal("-routes\")\n\tpathsElement.")).output(mark("name")).output(literal("();\n}")),
			rule().condition((type("parameter")), (trigger("request"))).output(mark("name")).output(literal(" : function(value) {\n\tthis.")).output(mark("method")).output(literal("(\"")).output(mark("name")).output(literal("\", { \"value\" : value });\n}")),
			rule().condition((trigger("request"))).output(mark("name")).output(literal(" : function(")).output(expression().output(mark("parameterSignature"))).output(literal(") {\n\tthis.")).output(mark("method")).output(literal("(\"")).output(mark("name")).output(literal("\"")).output(expression().output(literal(", ")).output(mark("parameter"))).output(literal(");\n}")),
			rule().condition((trigger("parameter"))),
			rule().condition((attribute("asset")), (trigger("method"))).output(literal("download")),
			rule().condition((trigger("method"))).output(literal("carry"))
		);
	}
}