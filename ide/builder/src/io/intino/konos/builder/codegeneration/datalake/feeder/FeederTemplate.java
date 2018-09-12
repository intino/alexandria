package io.intino.konos.builder.codegeneration.datalake.feeder;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class FeederTemplate extends Template {

	protected FeederTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new FeederTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "feeder"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.feeders;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\nimport io.intino.ness.inl.Message;\nimport java.util.Arrays;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal(" extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Feeder {\n\tprivate final ")).add(mark("box", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Feeder(")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t")).add(mark("sensor", "class")).add(literal("\n}")),
			rule().add((condition("type", "sensor")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "FirstUpperCase")).add(literal("Sensor extends Abstract")).add(mark("name", "FirstUpperCase")).add(literal("Sensor {\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Sensor() {\n\t}\n\n\t")).add(mark("parent", "get")).add(literal("\n\n\t")).add(mark("parent", "methods")).add(literal("\n}")),
			rule().add((condition("type", "poll")), (condition("trigger", "get"))).add(literal("public Message get(Object... args) {\n\tString option = null; //TODO\n\treturn get(option, args);\n}")),
			rule().add((condition("trigger", "get"))).add(literal("public Message get(Object... args) {\n\treturn null;\n}")),
			rule().add((condition("type", "poll")), (condition("trigger", "methods"))).add(mark("eventMethod").multiple("\n")),
			rule().add((condition("trigger", "eventMethod"))).add(literal("protected Object ")).add(mark("value", "firstLowerCase")).add(literal("(java.util.List<Object> objects) {\n\t//return null;\n}"))
		);
		return this;
	}
}