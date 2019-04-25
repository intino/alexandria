package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetRequesterTemplate extends Template {

	protected WidgetRequesterTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetRequesterTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widget & accessible"))).add(literal("var ")).add(mark("name", "FirstUpperCase")).add(literal("ProxyBehaviors = ")).add(mark("name", "FirstUpperCase")).add(literal("ProxyBehaviors || {};\n\n")).add(mark("name", "FirstUpperCase")).add(literal("ProxyBehaviors.Requester = {\n\n\tregisterPersonifiedDisplay : function(value) {\n\t\tthis.carry(\"registerPersonifiedDisplay\", { \"value\" : value });\n\t}\n\n};")),
			rule().add((condition("type", "widget"))).add(literal("var ")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors = ")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors || {};\n\n")).add(mark("name", "FirstUpperCase")).add(literal("Behaviors.Requester = {\n\n    ")).add(mark("request").multiple(",\n")).add(literal("\n\n};")),
			rule().add((condition("type", "parameter & addressable")), (condition("trigger", "request"))).add(literal("route")).add(mark("name", "firstUpperCase")).add(literal(" : function(fromDisplay, value) {\n\tif (this.display == null || fromDisplay == null || fromDisplay.id != this.display.id) return;\n\tthis.carry(\"")).add(mark("name")).add(literal("\", { \"value\" : value });\n},\n")).add(mark("name")).add(literal(" : function(value) {\n\tvar pathsElement = this.querySelector(\"")).add(mark("widget", "camelCaseToSnakeCase")).add(literal("-routes\");\n\tif (pathsElement == null) pathsElement = this.parentElement.querySelector(\"")).add(mark("widget", "camelCaseToSnakeCase")).add(literal("-routes\")\n\tpathsElement.")).add(mark("name")).add(literal("(value);\n}")),
			rule().add((condition("type", "addressable")), not(condition("type", "parameter")), (condition("trigger", "request"))).add(literal("route")).add(mark("name", "firstUpperCase")).add(literal(" : function(fromDisplay) {\n\tif (this.display == null || fromDisplay == null || fromDisplay.id != this.display.id) return;\n\tthis.carry(\"")).add(mark("name")).add(literal("\");\n},\n")).add(mark("name")).add(literal(" : function() {\n\tvar pathsElement = this.querySelector(\"")).add(mark("widget", "camelCaseToSnakeCase")).add(literal("-routes\");\n\tif (pathsElement == null) pathsElement = this.parentElement.querySelector(\"")).add(mark("widget", "camelCaseToSnakeCase")).add(literal("-routes\")\n\tpathsElement.")).add(mark("name")).add(literal("();\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "request"))).add(mark("name")).add(literal(" : function(value) {\n\tthis.")).add(mark("method")).add(literal("(\"")).add(mark("name")).add(literal("\", { \"value\" : value });\n}")),
			rule().add((condition("trigger", "request"))).add(mark("name")).add(literal(" : function(")).add(expression().add(mark("parameterSignature"))).add(literal(") {\n\tthis.")).add(mark("method")).add(literal("(\"")).add(mark("name")).add(literal("\"")).add(expression().add(literal(", ")).add(mark("parameter"))).add(literal(");\n}")),
			rule().add((condition("trigger", "parameter"))),
			rule().add((condition("attribute", "Asset")), (condition("trigger", "method"))).add(literal("download")),
			rule().add((condition("trigger", "method"))).add(literal("carry"))
		);
		return this;
	}
}