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
			rule().add((condition("type", "mold"))).add(literal("<link rel=\"import\" href=\"../../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../../lib/cotton-translator/cotton-translator-behavior.html\">\n\n<link rel=\"import\" href=\"../../../lib/alexandria-ui-elements/_common/alexandria-breadcrumbs-behavior.html\">\n<link rel=\"import\" href=\"../../../lib/alexandria-ui-elements/_common/mold/alexandria-mold-layout-behavior.html\">\n<link rel=\"import\" href=\"../../../lib/alexandria-ui-elements/_common/mold/alexandria-mold-block.html\">\n<link rel=\"import\" href=\"../../../lib/alexandria-ui-elements/_common/mold/alexandria-mold-stamps.html\">\n\n<dom-module id=\"")).add(mark("name", "CamelCaseToSnakeCase")).add(literal("-layout\">\n\n\t<template>\n\t\t")).add(mark("block").multiple("\n")).add(literal("\n\t\t")).add(mark("expandedBlocks")).add(literal("\n\t\t<div id=\"highlight\"></div>\n\t</template>\n\n\t<style>\n\t\t:host {\n\t\t\tdisplay:block;\n\t\t\theight:100%;\n\t\t\toverflow:auto;\n\t\t\tposition:relative;\n\t\t}")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("block", "style").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("stamp", "style").multiple("\n"))).add(literal("\n\t\t:host[mode=\"list\"], :host[mode=\"grid\"] {\n\t\t\twidth: 100%;\n\t\t}\n\t\t:host[mode=\"magazine\"] alexandria-mold-block {\n\t\t\theight: 100%;\n\t\t}\n\t\t:host alexandria-mold-block {\n\t\t\tmargin-left: 2px;\n\t\t\tmargin-right: 2px;\n\t\t}\n\t\t:host .expanded-block {\n\t\t\tmargin: 10px 0;\n\t\t}\n\t\t:host #highlight {\n\t\t\tbackground: var(--accent-color);\n\t\t\tposition: absolute;\n\t\t\ttop: 0;\n\t\t\theight: 100%;\n\t\t\twidth: 100%;\n\t\t\tvisibility: hidden;\n\t\t\topacity: 0;\n\t\t\ttransition: visibility 0s 0.5s, opacity 0.5s linear;\n\t\t\tborder-radius: 10px;\n\t\t}\n\t\t:host #highlight.visible {\n\t\t\tvisibility: visible;\n\t\t\topacity: 0.3;\n\t\t\ttransition: opacity 0.5s linear;\n\t\t}\n\t</style>\n\n\t<script>\n\t\tPolymer({\n\t\t\tis: '")).add(mark("name", "CamelCaseToSnakeCase")).add(literal("-layout',\n\n\t\t\tbehaviors: [ CottonBehaviors.TranslatorBehavior, AlexandriaBehaviors.MoldLayoutBehavior, AlexandriaBehaviors.BreadcrumbsBehavior ],\n\n\t\t\tblocks : function() {\n\t\t\t\tvar blocks = [];")).add(expression().add(literal("\n")).add(literal("\t\t\t\t")).add(mark("block", "js").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t\t\t")).add(mark("expandedblocks", "js")).add(literal("\n")).add(literal("\t\t\t\t"))).add(literal("return blocks;\n\t\t\t},\n\n\t\t\tstamps : function() {\n\t\t\t\tvar stamps = [];\n\t\t\t\t")).add(expression().add(mark("stamp", "js").multiple("\n")).add(literal("\n")).add(literal("\t\t\t\t"))).add(literal("return stamps;\n\t\t\t}\n\t\t});\n\t</script>\n\n</dom-module>")),
			rule().add((condition("type", "block")), (condition("trigger", "js"))).add(literal("blocks.push({")).add(expression().add(literal("name:\"")).add(mark("name")).add(literal("\""))).add(expression().add(literal(",layout:\"")).add(mark("layout").multiple(" ")).add(literal("\""))).add(expression().add(literal(",expanded:")).add(mark("expanded"))).add(expression().add(literal(",style:\"")).add(mark("style")).add(literal("\""))).add(expression().add(literal(",width:\"")).add(mark("width")).add(literal("\""))).add(expression().add(literal(",height:\"")).add(mark("height")).add(literal("\""))).add(expression().add(literal(",hiddenIfMobile:")).add(mark("hiddenIfMobile"))).add(literal("});")).add(expression().add(literal("\n")).add(mark("block", "js").multiple("\n"))),
			rule().add((condition("type", "stamp")), (condition("trigger", "js"))).add(literal("stamps.push({name:\"")).add(mark("name")).add(literal("\",type:\"")).add(mark("type")).add(literal("\"")).add(expression().add(literal(",")).add(mark("attribute").multiple(","))).add(expression().add(literal(",properties:{")).add(mark("property").multiple(",")).add(literal("}"))).add(literal("});")),
			rule().add((condition("type", "stamp & hasCustomClass")), (condition("trigger", "style"))).add(literal(":host .")).add(mark("name")).add(literal(" { @apply(--")).add(mark("name")).add(literal("); }")),
			rule().add((condition("type", "stamp")), (condition("trigger", "style"))),
			rule().add((condition("type", "block & hasCustomClass")), (condition("trigger", "style"))).add(literal(":host .")).add(mark("name")).add(literal(" { @apply(--")).add(mark("name")).add(literal("); }")).add(expression().add(literal("\n")).add(mark("block", "style").multiple("\n"))),
			rule().add((condition("type", "block")), (condition("trigger", "style"))),
			rule().add((condition("type", "boolean | number")), (condition("trigger", "attribute"))).add(mark("name")).add(literal(":")).add(mark("value")),
			rule().add((condition("trigger", "attribute"))).add(mark("name")).add(literal(":\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "boolean | number")), (condition("trigger", "property"))).add(mark("name")).add(literal(":")).add(mark("value")),
			rule().add((condition("trigger", "property"))).add(mark("name")).add(literal(":\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "block")), (condition("trigger", "block"))).add(literal("<alexandria-mold-block class=\"")).add(mark("name")).add(literal("\" block=\"[[")).add(mark("name")).add(literal("]]\">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("block").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("stamp").multiple("\n"))).add(literal("\n</alexandria-mold-block>")),
			rule().add((condition("type", "stamp"))).add(literal("<alexandria-stamp-")).add(mark("type")).add(literal(" class=\"")).add(mark("name")).add(literal("\" stamp=\"[[")).add(mark("name")).add(literal("]]\"></alexandria-stamp-")).add(mark("type")).add(literal(">")),
			rule().add((condition("type", "expandedBlocks")), (condition("trigger", "js"))).add(mark("block", "js").multiple("\n")),
			rule().add((condition("trigger", "expandedBlocks"))).add(literal("<div class=\"expanded-block\" hidden$=\"[[!hasExpandedBlocks]]\">")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("block").multiple("\n")).add(literal("\n"))).add(literal("</div>"))
		);
		return this;
	}
}