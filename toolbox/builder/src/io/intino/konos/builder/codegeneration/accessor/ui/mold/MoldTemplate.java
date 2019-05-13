package io.intino.konos.builder.codegeneration.accessor.ui.mold;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MoldTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("mold"))).output(literal("\n<link rel=\"import\" href=\"../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"../../lib/alexandria-ui-elements/_common/mold/alexandria-mold-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-carrier/cotton-carrier-behavior.html\">\n<link rel=\"import\" href=\"../../lib/cotton-translator/cotton-translator-behavior.html\">\n<link rel=\"import\" href=\"")).output(mark("name", "lowercase")).output(literal("/")).output(mark("name", "camelCaseToSnakeCase")).output(literal("-layout.html\">\n\n<dom-module id=\"")).output(mark("name", "CamelCaseToSnakeCase")).output(literal("\">\n\n\t<template>\n\t\t<")).output(mark("name", "camelCaseToSnakeCase")).output(literal("-layout id=\"layout\" item=\"[[item]]\" mode=\"[[mode]]\"></")).output(mark("name", "camelCaseToSnakeCase")).output(literal("-layout>\n\t</template>\n\n\t<script>\n\t\tPolymer({\n\t\t\tis: '")).output(mark("name", "camelCaseToSnakeCase")).output(literal("',\n\t\t\tbehaviors: [ AlexandriaBehaviors.MoldBehavior, CottonBehaviors.CarrierBehavior, CottonBehaviors.PushBehavior ],\n\t\t\tproperties : {\n\t\t\t\titem : Object,\n\t\t\t\tmode : String\n\t\t\t},\n\t\t\tready : function() {\n\t\t\t\tthis.scopeSubtree(this.$.layout, true);\n\t\t\t}\n\t\t});\n\t</script>\n</dom-module>"))
		);
	}
}