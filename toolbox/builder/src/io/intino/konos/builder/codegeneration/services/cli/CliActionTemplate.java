package io.intino.konos.builder.codegeneration.services.cli;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CliActionTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("command"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".actions;\n\nimport io.intino.alexandria.cli.command.MessageProperties;\nimport io.intino.alexandria.cli.response.Text;")).output(expression().output(literal("\n")).output(mark("response", "import"))).output(literal("\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "firstUppercase")).output(literal("Action {\n\tpublic ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\tpublic MessageProperties properties;\n\tpublic String state;\n\t")).output(mark("parameter", "declaration")).output(literal("\n\n\tpublic ")).output(mark("response", "type")).output(literal(" execute() {\n\t\treturn null;\n\t}\n\n    ")).output(expression().output(mark("response", "method").multiple("\n"))).output(literal("\n}")),
			rule().condition((type("condition"))).output(literal("public io.intino.alexandria.cli.response.Text when")).output(mark("value", "firstUpperCase")).output(literal("() {\n    return null;\n}")),
			rule().condition((allTypes("parameter","list"))).output(literal("public List<String> ")).output(mark("name")).output(literal(";")),
			rule().condition((type("parameter"))).output(literal("public String ")).output(mark("name")).output(literal(";")),
			rule().condition((allTypes("response","multiline")), (trigger("type"))).output(literal("MultiLineProvider")),
			rule().condition((allTypes("response","attachment")), (trigger("type"))).output(literal("io.intino.alexandria.Resource")),
			rule().condition((allTypes("response","confirmation")), (trigger("type"))).output(literal("io.intino.alexandria.cli.response.QuestionProvider")),
			rule().condition((type("response")), (trigger("type"))).output(literal("String")),
			rule().condition((allTypes("response","multiline")), (trigger("import"))).output(literal("import ")).output(mark("package", "validPackage")).output(literal(".cli.commands.")).output(mark("command", "firstUppercase")).output(literal("Command.MultiLineProvider;")),
			rule().condition((type("response")), (trigger("type"))).output(literal("io.intino.alexandria.cli.response.Text")),
			rule().condition((allTypes("response","confirmation")), (trigger("method"))).output(mark("option").multiple("\n")),
			rule().condition((type("response")), (trigger("method"))),
			rule().condition((type("option"))).output(literal("public io.intino.alexandria.cli.response.Text when")).output(mark("value", "firstUppercase")).output(literal("() {\n    return null;\n}"))
		);
	}
}