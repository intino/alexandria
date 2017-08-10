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
			rule().add((condition("type", "messageHandler"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness.messagehandlers;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\npublic class ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("MessageHandler {\n\n\tpublic ")).add(mark("box", "validName", "firstUpperCase")).add(literal("Box box;\n\tpublic io.intino.ness.inl.Message message;\n\n\tpublic void execute() {\n\n\t}\n\n}"))
		);
		return this;
	}
}