package io.intino.pandora.plugin.codegeneration.accessor.ui;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class WidgetTemplate extends Template {

	protected WidgetTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new WidgetTemplate(, ).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "widget"))).add(literal("<script src=\"")).add(mark("name", "lowercase")).add(literal("widget/requester.js\"></script>\n<script src=\"")).add(mark("name", "lowercase")).add(literal("widget/notifier-listener.js\"></script>\n\n<dom-module id=\"")).add(mark("name")).add(literal("-widget\">  //TODO PONER ")).add(mark("NAME")).add(literal(" EN SNAKE CASE\n\n    <template>\n        ")).add(mark("innerDisplay").multiple("\n")).add(literal("\n    </template>\n\n    <style>\n    </style>\n\n    <script>\n\n        const ")).add(mark("name", "firstuppercase")).add(literal("WidgetDictionary = {\n            es: {\n            },\n            en: {\n            }\n        };\n\n        Polymer({\n            is: '")).add(mark("name")).add(literal("-widget', //TODO PONER ")).add(mark("NAME")).add(literal(" EN SNAKE CASE\n\n            behaviors: [ CottonBehaviors.CarrierBehavior,\n                         CottonBehaviors.PushBehavior,\n                         CottonBehaviors.TranslatorBehavior,\n                         ")).add(mark("name", "firstuppercase")).add(literal("WidgetBehaviors.Requester,\n                         ")).add(mark("name", "firstuppercase")).add(literal("WidgetBehaviors.NotifierListener ],\n\n            properties : {\n            },\n\n            attached : function() {\n                this.translate(")).add(mark("name", "firstuppercase")).add(literal("WidgetDictionary);\n                this.listenToDisplay();\n            }\n\n        });\n    </script>\n</dom-module>")),
			rule().add((condition("trigger", "innerDisplay"))).add(literal("<div><cotton-zombie display=\"")).add(mark("value")).add(literal("Display\" widget=\"")).add(mark("value")).add(literal("Widget\"></cotton-zombie></div>"))
		);
		return this;
	}
}