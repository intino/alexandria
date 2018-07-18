package io.intino.konos.builder.codegeneration.datalake.feeder;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractFeederTemplate extends Template {

	protected AbstractFeederTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractFeederTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "feeder & Poll"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.feeders;\n\nimport java.util.Arrays;\n\npublic class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder")).add(expression().add(literal(" extends io.intino.konos.datalake.feeders.PollFeeder"))).add(literal(" {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder() {\n\t\tsuper(\"")).add(mark("defaultOption")).add(literal("\", Arrays.asList(")).add(mark("option", "quoted").multiple(", ")).add(literal("));\n\t}\n\n\tpublic boolean fits(List<String> eventTypes) {\n\t\treturn false;//TODO\n\t}\n}")),
			rule().add((condition("type", "feeder"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.feeders;\n\nimport java.util.Arrays;\nimport java.util.Collections;\n\npublic class Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder")).add(expression().add(literal(" extends io.intino.konos.datalake.Feeder"))).add(literal(" {\n\n\tpublic Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder() {\n\t\tsuper(\"")).add(mark("defaultOption")).add(literal("\", Arrays.asList(")).add(mark("option").multiple(", ")).add(literal("));\n\t}\n\n}")),
			rule().add((condition("trigger", "option"))).add(literal("new Option(\"")).add(mark("value")).add(literal("\", ")).add(expression().add(mark("option")).or(expression().add(literal("Collections.emptyList()")))).add(literal(")")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\""))
		);
		return this;
	}
}