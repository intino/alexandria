package io.intino.konos.builder.codegeneration.accessor.ui.mold;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MoldLayoutTemplate extends Template {

	protected MoldLayoutTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MoldLayoutTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "mold"))).add(literal("<link rel=\"import\" href=\"../../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../../lib/cotton-translator/cotton-translator-behavior.html\">\n\n<link rel=\"import\" href=\"../../../lib/alexandria-activity-elements/_common/mold/alexandria-mold-layout-behavior.html\">\n<link rel=\"import\" href=\"../../../lib/alexandria-activity-elements/_common/mold/alexandria-mold-block.html\">\n<link rel=\"import\" href=\"../../../lib/alexandria-activity-elements/_common/mold/alexandria-mold-stamps.html\">\n\n<dom-module id=\"")).add(mark("name", "CamelCaseToSnakeCase")).add(literal("-layout\">\n\n    <template>\n    \t")).add(mark("block").multiple("\n")).add(literal("\n\t\t")).add(mark("expandedBlocks")).add(literal("\n\t\t<div id=\"highlight\"></div>\n    </template>\n\n    <style>\n        :host[mode=\"list\"], :host[mode=\"grid\"] {\n            width: 100%;\n        }\n        :host[mode=\"magazine\"] alexandria-mold-block {\n            height: 100%;\n        }\n        :host alexandria-mold-block {\n            margin-left: 2px;\n            margin-right: 2px;\n        }\n        :host .expanded-block {\n            margin: 10px 0;\n        }\n        :host #highlight {\n            background: var(--accent-color);\n            position: absolute;\n            top: 0;\n            height: 100%;\n            width: 100%;\n            visibility: hidden;\n            opacity: 0;\n            transition: visibility 0s 0.5s, opacity 0.5s linear;\n            border-radius: 10px;\n        }\n        :host #highlight.visible {\n            visibility: visible;\n            opacity: 0.3;\n            transition: opacity 0.5s linear;\n        }\n    </style>\n\n    <script>\n        Polymer({\n            is: '")).add(mark("name", "CamelCaseToSnakeCase")).add(literal("-layout',\n\n            behaviors: [ CottonBehaviors.TranslatorBehavior, AlexandriaBehaviors.MoldLayoutBehavior ],\n\n            blocks : function() {\n                var blocks = [];")).add(expression().add(literal("\n")).add(literal("                ")).add(mark("block", "js").multiple("\n"))).add(expression().add(literal("\n")).add(literal("                ")).add(mark("expandedblocks", "js")).add(literal("\n")).add(literal("                "))).add(literal("return blocks;\n            },\n\n            stamps : function() {\n                var stamps = [];\n\t\t\t\t")).add(expression().add(mark("stamp", "js").multiple("\n")).add(literal("\n")).add(literal("                "))).add(literal("return stamps;\n            }\n        });\n    </script>\n\n</dom-module>")),
			rule().add((condition("type", "block")), (condition("trigger", "js"))).add(literal("blocks.push({")).add(expression().add(literal("name:\"")).add(mark("name")).add(literal("\""))).add(expression().add(literal(",layout:\"")).add(mark("layout")).add(literal("\""))).add(expression().add(literal(",expanded:")).add(mark("expanded"))).add(expression().add(literal(",style:\"")).add(mark("style")).add(literal("\""))).add(expression().add(literal(",width:\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(",height:\"")).add(mark("height")).add(literal("\""))).add(expression().add(literal(",hiddenIfMobile:")).add(mark("hiddenIfMobile"))).add(literal("});")).add(expression().add(literal("\n")).add(mark("block", "js").multiple("\n"))),
			rule().add((condition("type", "stamp")), (condition("trigger", "js"))).add(literal("stamps.push({name:\"")).add(mark("name")).add(literal("\",type:\"")).add(mark("type")).add(literal("\"")).add(expression().add(literal(",")).add(mark("attribute").multiple(","))).add(expression().add(literal(",properties:{")).add(mark("property").multiple(",")).add(literal("}"))).add(literal("});")),
			rule().add((condition("type", "boolean | number")), (condition("trigger", "attribute"))).add(mark("name")).add(literal(":")).add(mark("value")),
			rule().add((condition("trigger", "attribute"))).add(mark("name")).add(literal(":\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "boolean | number")), (condition("trigger", "property"))).add(mark("name")).add(literal(":")).add(mark("value")),
			rule().add((condition("trigger", "property"))).add(mark("name")).add(literal(":\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "block")), (condition("trigger", "block"))).add(literal("<alexandria-mold-block block=\"[[")).add(mark("name")).add(literal("]]\">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("block").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("stamp").multiple("\n"))).add(literal("\n</alexandria-mold-block>")),
			rule().add((condition("type", "stamp"))).add(literal("<alexandria-stamp-")).add(mark("type")).add(literal(" stamp=\"[[")).add(mark("name")).add(literal("]]\"></alexandria-stamp-")).add(mark("type")).add(literal(">")),
			rule().add((condition("type", "expandedBlocks")), (condition("trigger", "js"))).add(mark("block", "js").multiple("\n")),
			rule().add((condition("trigger", "expandedBlocks"))).add(literal("<div class=\"expanded-block\" hidden$=\"[[!hasExpandedBlocks]]\">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("block").multiple("\n")).add(literal("\n"))).add(literal("</div>"))
		);
		return this;
	}
}