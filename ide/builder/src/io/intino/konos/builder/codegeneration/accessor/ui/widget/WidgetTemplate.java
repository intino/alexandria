package io.intino.konos.builder.codegeneration.accessor.ui.widget;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetTemplate extends Template {

	protected WidgetTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widget"))).add(literal("<link rel=\"import\" href=\"../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../lib/cotton-push/cotton-push-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-zombie/cotton-zombie.html\">\n<link rel=\"import\" href=\"../../lib/cotton-translator/cotton-translator-behavior.html\">\n")).add(expression().add(literal("<link rel=\"import\" href=\"../../lib/alexandria-activity-elements/alexandria-")).add(mark("type", "camelCaseToSnakeCase")).add(literal(".html\">"))).add(literal("\n\n")).add(expression().add(mark("parent", "import")).add(literal("\n"))).add(literal("\n")).add(expression().add(mark("imports")).add(literal("\n"))).add(expression().add(literal("\n")).add(mark("path", "pahtimport")).add(literal("\n"))).add(literal("\n<script src=\"")).add(mark("name", "lowercase")).add(literal("/requester.js\"></script>\n<script src=\"")).add(mark("name", "lowercase")).add(literal("/notifier-listener.js\"></script>\n\n<dom-module id=\"")).add(mark("name", "camelCaseToSnakeCase")).add(literal("\">\n\n    <template>\n    \t")).add(mark("path")).add(literal("\n    \t")).add(mark("parent")).add(literal("\n        ")).add(mark("innerDisplay").multiple("\n")).add(literal("\n        ")).add(mark("attached", "prototype")).add(literal("\n    </template>\n\n    <style>\n    </style>\n\n    <script>\n\n        const ")).add(mark("name", "firstuppercase")).add(literal("Dictionary = {\n            es: {\n            },\n            en: {\n            }\n        };\n\n        Polymer({\n            is: '")).add(mark("name", "camelCaseToSnakeCase")).add(literal("',\n\n            behaviors: [ CottonBehaviors.CarrierBehavior,\n                         CottonBehaviors.PushBehavior,\n                         CottonBehaviors.TranslatorBehavior")).add(expression().add(literal(",")).add(literal("\n")).add(literal("                         ")).add(mark("includes"))).add(literal("\n                         ],\n\n            properties : {\n            },\n\t\t\t")).add(mark("attached")).add(literal("\n\n        });\n    </script>\n</dom-module>")),
			rule().add((condition("trigger", "pathImport"))).add(literal("<link rel=\"import\" href=\"")).add(mark("name", "lowercase")).add(literal("/")).add(mark("name", "camelCaseToSnakeCase-paths")).add(literal(".html\">")),
			rule().add((condition("trigger", "path"))).add(literal("<")).add(mark("name", "camelCaseToSnakeCase-paths")).add(literal("></")).add(mark("name", "camelCaseToSnakeCase-paths")).add(literal(">")),
			rule().add((condition("trigger", "imports"))).add(literal("<script src=\"../../lib/alexandria-activity-elements/alexandria")).add(mark("type", "lowercase")).add(literal("/requester.js\"></script>\n<script src=\"../../lib/alexandria-activity-elements/alexandria")).add(mark("type", "lowercase")).add(literal("/notifier-listener.js\"></script>")),
			rule().add((condition("trigger", "includes"))).add(mark("widget", "firstuppercase")).add(literal("Behaviors.Requester,\n")).add(mark("widget", "firstuppercase")).add(literal("Behaviors.NotifierListener")),
			rule().add((condition("type", "prototype")), (condition("trigger", "prototype"))).add(literal("<alexandria-")).add(mark("type", "camelCaseToSnakeCase")).add(literal(" id=\"alexandria-")).add(mark("type", "camelCaseToSnakeCase")).add(literal("\"></alexandria-")).add(mark("type", "camelCaseToSnakeCase")).add(literal(">")),
			rule().add((condition("type", "prototype")), (condition("trigger", "attached"))).add(literal("attached : function() {\n\tthis.translate(")).add(mark("widget", "firstuppercase")).add(literal("Dictionary);\n\tthis._linkWithAlexandriaElement();\n},\n\n_linkWithAlexandriaElement : function() {\n\tvar element = document.getElementById(\"alexandria-")).add(mark("type", "camelCaseToSnakeCase")).add(literal("\");\n\telement.display = this.display;\n\telement.behaviors.push(")).add(mark("widget", "firstuppercase")).add(literal("Behaviors.Requester);\n\telement.behaviors.push(")).add(mark("widget", "firstuppercase")).add(literal("Behaviors.NotifierListener);\n\telement.behaviors.push(Alexandria")).add(mark("type", "firstuppercase")).add(literal("Behaviors.Requester);\n\telement.behaviors.push(Alexandria")).add(mark("type", "firstuppercase")).add(literal("Behaviors.NotifierListener);\n}")),
			rule().add((condition("trigger", "attached"))).add(literal("attached : function() {\n\tthis.translate(")).add(mark("widget", "firstuppercase")).add(literal("Dictionary);\n\tthis.listenToDisplay();\n}")),
			rule().add((condition("trigger", "import"))).add(literal("<link rel=\"import\" href=\"../../lib/")).add(mark("dsl", "lowercase")).add(literal("/")).add(mark("value", "lowercase")).add(literal(".html\">")),
			rule().add((condition("trigger", "innerDisplay"))).add(literal("<div><cotton-zombie display=\"")).add(mark("value")).add(literal("\" widget=\"")).add(mark("value")).add(literal("\"></cotton-zombie></div>")),
			rule().add((condition("trigger", "parent"))).add(literal("<div><cotton-zombie display=\"")).add(mark("value")).add(literal("\" widget=\"")).add(mark("value")).add(literal("\"></cotton-zombie></div>"))
		);
		return this;
	}
}