package io.intino.konos.builder.codegeneration.datalake.feeder;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class FeederTemplate extends Template {

	protected FeederTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new FeederTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "feeder"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.feeders;\n\nimport io.intino.ness.inl.Message;\nimport java.util.Arrays;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Feeder extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder {\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Feeder(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\t")).add(mark("sensor", "class")).add(literal("\n}")),
			rule().add((condition("type", "sensor")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal("Sensor extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Sensor {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Sensor() {\n\t}\n\n\tpublic Message get(Object... args) {\n\t\treturn null;\n\t}\n}"))
		);
		return this;
	}
}