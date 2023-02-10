package io.intino.konos.builder.codegeneration.services.cli;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CliCommandTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("command","confirmation"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".cli.commands;\n\nimport io.intino.alexandria.cli.Cli;\nimport io.intino.alexandria.cli.Context;\nimport io.intino.alexandria.cli.Response;\nimport io.intino.alexandria.cli.command.MessageProperties;\nimport io.intino.alexandria.cli.response.Text;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".cli.commands.*;\n\npublic class ")).output(mark("option", "firstUpperCase")).output(literal("Command {\n    private TrooperBox box;\n\n    public ")).output(mark("option", "firstUpperCase")).output(literal("Command(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n        this.box = box;\n    }\n\n    public Response execute(MessageProperties properties, String command, String... args) {\n        Context context = properties.context();\n        Cli.CommandInfo lastCommand = context.lastCommand();\n        if (lastCommand == null) return new Text(\"Command not found\");\n        ")).output(mark("condition").multiple("\n")).output(literal("\n        return new Text(\"Command not found\");\n    }\n\n}")),
			rule().condition((type("command"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".cli.commands;\n\nimport io.intino.alexandria.cli.Command;\nimport io.intino.alexandria.cli.Response;\nimport io.intino.alexandria.cli.command.MessageProperties;\nimport io.intino.alexandria.cli.response.Line;\nimport io.intino.alexandria.cli.response.MessageData;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUppercase")).output(literal("Action;\n\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic class ")).output(mark("name", "firstUppercase")).output(literal("Command implements Command {\n    private ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n    public ")).output(mark("name", "firstUppercase")).output(literal("Command(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n        this.box = box;\n    }\n\n    ")).output(mark("response", "provider")).output(literal("\n    ")).output(mark("response", "definition").multiple("\n")).output(literal("\n\n    @Override\n    public ")).output(mark("response", "type")).output(literal(" execute(MessageProperties properties, String command, String... args) {\n        return ")).output(mark("execute")).output(literal(";\n    }\n\n    ")).output(mark("response", "method").multiple("\n")).output(literal("\n\n    private ")).output(mark("name", "firstUppercase")).output(literal("Action fill(")).output(mark("name", "firstUppercase")).output(literal("Action action, MessageProperties properties, String... args) {\n        action.box = this.box;\n        action.state = properties.context().state();\n        action.properties = properties;\n        ")).output(mark("parameter").multiple("\n")).output(literal("\n        return action;\n    }\n\n}")),
			rule().condition((allTypes("execute","multiline"))).output(literal("new ")).output(mark("response", "type")).output(literal("(")).output(mark("response", "definitionDeclaration")).output(literal(", fill(new ")).output(mark("name", "firstUppercase")).output(literal("Action(), properties, args).execute())")),
			rule().condition((allTypes("execute","confirmation"))).output(literal("new ")).output(mark("response", "type")).output(literal("(\"")).output(mark("question")).output(literal("\", List.of(\"")).output(mark("option").multiple("\", \"")).output(literal("\"), fill(new ")).output(mark("name", "firstUppercase")).output(literal("Action(), properties, args).execute())")),
			rule().condition((type("execute"))).output(literal("new ")).output(mark("response", "type")).output(literal("(fill(new ")).output(mark("name", "firstUppercase")).output(literal("Action(), properties, args).execute())")),
			rule().condition((allTypes("parameter","list"))).output(literal("action.")).output(mark("name")).output(literal(" = java.util.Arrays.asList(args);")),
			rule().condition((type("parameter"))).output(literal("action.")).output(mark("name")).output(literal(" = args[")).output(mark("index")).output(literal("];")),
			rule().condition((allTypes("response","multiline")), (trigger("type"))).output(literal("io.intino.alexandria.cli.response.MultiLine")),
			rule().condition((allTypes("response","attachment")), (trigger("type"))).output(literal("io.intino.alexandria.cli.response.Attachment")),
			rule().condition((allTypes("response","confirmation")), (trigger("type"))).output(literal("io.intino.alexandria.cli.response.Question")),
			rule().condition((type("response")), (trigger("type"))).output(literal("io.intino.alexandria.cli.response.Text")),
			rule().condition((allTypes("response","confirmation")), (trigger("method"))).output(mark("option", "method").multiple("\n")),
			rule().condition((type("response")), (trigger("method"))),
			rule().condition((allTypes("response","multiline")), (trigger("provider"))).output(literal("public static class MultiLineProvider extends io.intino.alexandria.cli.response.MultiLineProvider {\n    ")).output(mark("line", "declaration").multiple("\n")).output(literal("\n\n    ")).output(mark("line", "method").multiple("\n")).output(literal("\n    ")).output(mark("line", "class").multiple("\n")).output(literal("\n\n    @Override\n    protected MessageData data(String line) {\n        ")).output(mark("line", "data").multiple("\n")).output(literal("\n        return null;\n    }\n\n    @Override\n    protected List<MessageData> dataList(String line) {\n        ")).output(mark("line", "dataList").multiple("\n")).output(literal("\n        return null;\n    }\n}")),
			rule().condition((allTypes("response","multiline")), (trigger("definition"))).output(mark("line", "definition").multiple("\n")),
			rule().condition((allTypes("response","multiline")), (trigger("definitiondeclaration"))).output(literal("List.of(")).output(mark("line", "definitionDeclaration").multiple(", ")).output(literal(")")),
			rule().condition((allTypes("line","multiple")), (trigger("declaration"))).output(literal("private List<MessageData> ")).output(mark("name", "firstLowerCase")).output(literal(" = new ArrayList<>();")),
			rule().condition((type("line")), (trigger("declaration"))).output(literal("private MessageData ")).output(mark("name", "firstLowerCase")).output(literal(" = new MessageData();")),
			rule().condition((allTypes("line","multiple")), (trigger("method"))).output(literal("public MultiLineProvider add")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("name", "firstUpperCase")).output(literal(" value) {\n    this.")).output(mark("name", "firstLowerCase")).output(literal(".add(value);\n    return this;\n}\n\npublic MultiLineProvider addAll")).output(mark("name", "firstUpperCase")).output(literal("(List<")).output(mark("name", "firstUpperCase")).output(literal("> values) {\n    this.")).output(mark("name", "firstLowerCase")).output(literal(".addAll(values);\n    return this;\n}")),
			rule().condition((type("line")), (trigger("method"))).output(literal("public MultiLineProvider ")).output(mark("name", "firstLowerCase")).output(literal("Variable(String variable, String value) {\n    this.")).output(mark("name", "firstLowerCase")).output(literal(".add(variable, value);\n    return this;\n}")),
			rule().condition((allTypes("line","multiple")), (trigger("class"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" extends MessageData {\n}")),
			rule().condition((type("line")), (trigger("class"))),
			rule().condition((allTypes("line","multiple")), (trigger("data"))),
			rule().condition((type("line")), (trigger("data"))).output(literal("if (line.equals(\"")).output(mark("name")).output(literal("\")) return ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("line","multiple")), (trigger("datalist"))).output(literal("if (line.equals(\"")).output(mark("name")).output(literal("\")) return ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("line")), (trigger("datalist"))),
			rule().condition((type("line")), (trigger("definition"))).output(literal("private static final Line ")).output(mark("name", "firstUpperCase")).output(literal("Line = new Line(\"")).output(mark("name")).output(literal("\", \"")).output(mark("content")).output(literal("\", ")).output(mark("multiple")).output(expression().output(literal(", \"")).output(mark("dependant")).output(literal("\""))).output(literal(");")),
			rule().condition((type("line")), (trigger("definitiondeclaration"))).output(mark("name", "firstUpperCase")).output(literal("Line")),
			rule().condition((type("condition"))).output(literal("if (lastCommand.name().equals(\"")).output(mark("command")).output(literal("\")) return new ")).output(mark("commandName", "firstUpperCase")).output(literal("Command(box).when")).output(mark("option", "firstUpperCase")).output(literal("(properties, lastCommand.name(), lastCommand.parameters().toArray(new String[0]));")),
			rule().condition((type("option"))).output(literal("public io.intino.alexandria.cli.response.Text when")).output(mark("value", "firstUppercase")).output(literal("(MessageProperties properties, String command, String... args) {\n    return fill(new ")).output(mark("commandName", "firstUppercase")).output(literal("Action(), properties, args).when")).output(mark("value", "firstUppercase")).output(literal("();\n}"))
		);
	}
}