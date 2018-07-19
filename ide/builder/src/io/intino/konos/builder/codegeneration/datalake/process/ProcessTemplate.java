package io.intino.konos.builder.codegeneration.datalake.process;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ProcessTemplate extends Template {

	protected ProcessTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ProcessTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "process"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".procedures.")).add(mark("procedure", "lowercase")).add(literal(";\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.ness.inl.Message;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Process {\n\n\tpublic ")).add(mark("box", "validName", "firstUpperCase")).add(literal("Box box;\n\tpublic ")).add(mark("type", "typeClass")).add(literal(" ")).add(mark("type", "typeName")).add(literal(";\n\n\tpublic void execute() {\n\t}\n}")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("attribute", "message")), (condition("trigger", "typeClass"))).add(literal("io.intino.ness.inl.Message")),
			rule().add((condition("type", "schema")), (condition("trigger", "typeClass"))).add(mark("package")).add(literal(".schemas.")).add(mark("name", "FirstUpperCase")),
			rule().add((condition("type", "schema")), (condition("trigger", "typeName"))).add(mark("name", "firstLowerCase")),
			rule().add((condition("trigger", "typeName"))).add(literal("message"))
		);
		return this;
	}
}