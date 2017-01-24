package io.intino.konos.builder.codegeneration.process;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class CommandTemplate extends Template {

	protected CommandTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new CommandTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "command"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".commands;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("module", "firstUpperCase")).add(literal("Konos;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Command {\n\n\tpublic ")).add(mark("box", "validName", "firstUpperCase")).add(literal("Box box;\n\tpublic String message;\n\n\tpublic void execute() {\n\n\t}\n\n}"))
		);
		return this;
	}
}