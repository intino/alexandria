package io.intino.konos.builder.codegeneration.services.cli;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class CliActionTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("command")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".actions;\n\nimport io.intino.alexandria.cli.command.MessageProperties;\nimport io.intino.alexandria.cli.response.Text;")).output(expression().output(literal("\n")).output(placeholder("response", "import"))).output(literal("\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(placeholder("name", "firstUppercase")).output(literal("Action {\n\tpublic ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\tpublic MessageProperties properties;\n\tpublic String state;\n\t")).output(placeholder("parameter", "declaration")).output(literal("\n\n\tpublic ")).output(placeholder("response", "type")).output(literal(" execute() {\n\t\treturn null;\n\t}\n\n    ")).output(expression().output(placeholder("response", "method").multiple("\n"))).output(literal("\n}")));
		rules.add(rule().condition(allTypes("condition")).output(literal("public io.intino.alexandria.cli.response.Text when")).output(placeholder("value", "firstUpperCase")).output(literal("() {\n    return null;\n}")));
		rules.add(rule().condition(allTypes("parameter", "list")).output(literal("public List<String> ")).output(placeholder("name")).output(literal(";")));
		rules.add(rule().condition(allTypes("parameter")).output(literal("public String ")).output(placeholder("name")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("response", "multiline"), trigger("type"))).output(literal("MultiLineProvider")));
		rules.add(rule().condition(all(allTypes("response", "attachment"), trigger("type"))).output(literal("io.intino.alexandria.Resource")));
		rules.add(rule().condition(all(allTypes("response", "confirmation"), trigger("type"))).output(literal("io.intino.alexandria.cli.response.QuestionProvider")));
		rules.add(rule().condition(all(allTypes("response"), trigger("type"))).output(literal("String")));
		rules.add(rule().condition(all(allTypes("response", "multiline"), trigger("import"))).output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".cli.commands.")).output(placeholder("command", "firstUppercase")).output(literal("Command.MultiLineProvider;")));
		rules.add(rule().condition(all(allTypes("response"), trigger("type"))).output(literal("io.intino.alexandria.cli.response.Text")));
		rules.add(rule().condition(all(allTypes("response", "confirmation"), trigger("method"))).output(placeholder("option").multiple("\n")));
		rules.add(rule().condition(all(allTypes("response"), trigger("method"))));
		rules.add(rule().condition(allTypes("option")).output(literal("public io.intino.alexandria.cli.response.Text when")).output(placeholder("value", "firstUppercase")).output(literal("() {\n    return null;\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}