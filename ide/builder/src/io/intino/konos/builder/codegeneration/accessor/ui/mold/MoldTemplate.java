package io.intino.konos.builder.codegeneration.accessor.ui.mold;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MoldTemplate extends Template {

	protected MoldTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MoldTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("trigger", "mold"))).add(literal("\n<link rel=\"import\" href=\"../../lib/polymer/polymer.html\">\n<link rel=\"import\" href=\"")).add(mark("name", "lowercase")).add(literal("/")).add(mark("name", "camelCaseToSnakeCase-layout")).add(literal(".html\">\n\n<dom-module id=\"")).add(mark("name", "camelCaseToSnakeCase")).add(literal("\">\n\n\t<template>\n\t\t<")).add(mark("name", "camelCaseToSnakeCase-layout")).add(literal(" item=\"[[item]]\" mode=\"[[mode]]\"></")).add(mark("name", "camelCaseToSnakeCase-layout")).add(literal(">\n\t</template>\n\n\t<script>\n\t\tPolymer({\n\t\t\tis: '")).add(mark("name", "camelCaseToSnakeCase")).add(literal("',\n\n\t\t\tproperties : {\n\t\t\t\titem : Object,\n\t\t\t\tmode : String\n\t\t\t}\n\t\t});\n\t</script>\n</dom-module>\n"))
		);
		return this;
	}
}