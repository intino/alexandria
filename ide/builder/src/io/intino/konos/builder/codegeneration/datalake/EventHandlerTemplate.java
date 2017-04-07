package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class EventHandlerTemplate extends Template {

	protected EventHandlerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new EventHandlerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "eventHandler"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".eventhandlers;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\n\npublic class ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("EventHandler {\n\n\tpublic ")).add(mark("box", "validName", "firstUpperCase")).add(literal("Box box;\n\tpublic String message;\n\n\tpublic void execute() {\n\n\t}\n\n}"))
		);
		return this;
	}
}