package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class MessageHandlerTemplate extends Template {

	protected MessageHandlerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new MessageHandlerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "messageHandler"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.messagehandlers;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("MessageHandler {\n\n\tpublic ")).add(mark("box", "validName", "firstUpperCase")).add(literal("Box box;\n\tpublic ")).add(mark("type", "typeClass")).add(literal(" ")).add(mark("type", "typeName")).add(literal(";\n\n\tpublic void execute() {\n\n\t}\n}")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;")),
			rule().add((condition("attribute", "message")), (condition("trigger", "typeClass"))).add(literal("io.intino.ness.inl.Message")),
			rule().add((condition("type", "schema")), (condition("trigger", "typeClass"))).add(mark("package")).add(literal(".schemas.")).add(mark("name", "FirstUpperCase")),
			rule().add((condition("type", "schema")), (condition("trigger", "typeName"))).add(mark("name", "firstLowerCase")),
			rule().add((condition("trigger", "typeName"))).add(literal("message"))
		);
		return this;
	}
}