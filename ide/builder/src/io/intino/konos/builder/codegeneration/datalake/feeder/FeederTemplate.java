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
			rule().add((condition("type", "feeder"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.feeders;\n\nimport io.intino.ness.inl.Message;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport java.io.InputStream;\nimport java.util.List;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Feeder extends Abstract")).add(mark("name", "firstUpperCase")).add(literal("Feeder {\n\tpublic ")).add(mark("box")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Feeder(AmidasBox box) {\n\t\tthis.box = box;\n\t}\n\n\n\tpublic Message event(InputStream document) {\n\t\treturn null;//TODO\n\t}\n\n\t")).add(mark("feed")).add(literal("\n}")),
			rule().add((condition("trigger", "sensor"))).add(literal("public Message ")).add(mark("name", "FirstUpperCase")).add(literal("(InputStream document) {\n\treturn null;//TODO\n}"))
		);
		return this;
	}
}