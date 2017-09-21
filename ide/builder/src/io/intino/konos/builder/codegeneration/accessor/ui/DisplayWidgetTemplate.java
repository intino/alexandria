package io.intino.konos.builder.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class DisplayWidgetTemplate extends Template {

	protected DisplayWidgetTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new DisplayWidgetTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widget"))).add(literal("<link rel=\"import\" href=\"../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../lib/cotton-push/cotton-push-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-zombie/cotton-zombie.html\">\n<link rel=\"import\" href=\"../../lib/cotton-translator/cotton-translator-behavior.html\">\n")).add(expression().add(mark("parent", "import")).add(literal("\n"))).add(literal("\n<script src=\"")).add(mark("name", "lowercase")).add(literal("widget/requester.js\"></script>\n<script src=\"")).add(mark("name", "lowercase")).add(literal("widget/notifier-listener.js\"></script>\n\n<dom-module id=\"")).add(mark("name", "camelCaseToSnakeCase")).add(literal("-widget\">\n\n    <template>\n    \t")).add(mark("parent")).add(literal("\n        ")).add(mark("innerDisplay").multiple("\n")).add(literal("\n    </template>\n\n    <style>\n    </style>\n\n    <script>\n\n        const ")).add(mark("name", "firstuppercase")).add(literal("WidgetDictionary = {\n            es: {\n            },\n            en: {\n            }\n        };\n\n        Polymer({\n            is: '")).add(mark("name", "camelCaseToSnakeCase")).add(literal("-widget',\n\n            behaviors: [ CottonBehaviors.CarrierBehavior,\n                         CottonBehaviors.PushBehavior,\n                         CottonBehaviors.TranslatorBehavior,\n                         ")).add(mark("name", "firstuppercase")).add(literal("WidgetBehaviors.Requester,\n                         ")).add(mark("name", "firstuppercase")).add(literal("WidgetBehaviors.NotifierListener ],\n\n            properties : {\n            },\n\n            attached : function() {\n                this.translate(")).add(mark("name", "firstuppercase")).add(literal("WidgetDictionary);\n                this.listenToDisplay();\n            }\n\n        });\n    </script>\n</dom-module>")),
			rule().add((condition("trigger", "import"))).add(literal("<link rel=\"import\" href=\"../../lib/")).add(mark("dsl", "lowercase")).add(literal("/")).add(mark("value", "lowercase")).add(literal("-widget.html\">")),
			rule().add((condition("trigger", "innerDisplay"))).add(literal("<div><cotton-zombie display=\"")).add(mark("value")).add(literal("Display\" widget=\"")).add(mark("value")).add(literal("Widget\"></cotton-zombie></div>")),
			rule().add((condition("trigger", "parent"))).add(literal("<div><cotton-zombie display=\"")).add(mark("value")).add(literal("Display\" widget=\"")).add(mark("value")).add(literal("Widget\"></cotton-zombie></div>"))
		);
		return this;
	}
}