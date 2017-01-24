package io.intino.pandora.builder.codegeneration.process;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class CommandDispatcherTemplate extends Template {

	protected CommandDispatcherTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new CommandDispatcherTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "dispatcher"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\n\npublic class PandoraProcessDispatcher {\n\n    Map<String, Command> commands = new LinkedHashMap<>();\n\n    public void process(String message) {\n        // TODO save message\n        ")).add(mark("commands").multiple("else ")).add(literal("\n    }\n}")),
			rule().add((condition("trigger", "commands"))).add(literal("if (\"")).add(mark("messageType")).add(literal("\".equals(message)) {\n    ")).add(mark("className")).add(literal(" command = new ")).add(mark("className")).add(literal("();\n    command.box = this;\n    command.message = message;\n    command.execute();\n}"))
		);
		return this;
	}
}