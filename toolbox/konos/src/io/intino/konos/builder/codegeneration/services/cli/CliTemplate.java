package io.intino.konos.builder.codegeneration.services.cli;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CliTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("cli", "service"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.cli.CliManager;\nimport io.intino.alexandria.cli.schemas.BotTalk;\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("Service {\n\n    private static ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("ServiceAuthenticator authenticator;\n\n    public static void setup(AlexandriaSpark spark, ")).output(mark("box", "FirstUpperCase")).output(literal("Box box, ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal(" cli) {\n    \tauthenticator = new ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("ServiceAuthenticator(box);\n        spark.route(\"/cli/")).output(mark("name", "camelCaseToSnakeCase", "lowerCase")).output(literal("\")\n                .before(manager -> { if (manager.fromHeader(\"Authorization\") == null || !authenticator.isAuthenticated(manager.fromHeader(\"Authorization\").replace(\"Bearer \", \"\"))) throw new io.intino.alexandria.exceptions.Unauthorized(\"Credential not found\");})\n                .post(manager -> manager.write(io.intino.alexandria.rest.ResponseAdapter.adapt(new CliManager(cli, io.intino.alexandria.rest.RequestAdapter.adapt(manager.fromBody(), BotTalk.class)).execute(manager.fromHeader(\"Authorization\").replace(\"Bearer \", \"\")))));\n    }\n}")),
				rule().condition((allTypes("cli", "authenticator"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\npublic class ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("ServiceAuthenticator {\n \tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n \tpublic ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("ServiceAuthenticator(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(String token) {\n\t\treturn false;\n\t}\n}")),
				rule().condition((type("cli"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.cli.Cli;\nimport io.intino.alexandria.cli.command.MessageProperties;\nimport io.intino.alexandria.cli.response.Text;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".cli.commands.*;\n\nimport java.io.IOException;\nimport java.util.Arrays;\nimport java.util.List;\nimport java.util.stream.Collectors;\n\nimport static java.util.Collections.emptyList;\n\npublic class ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal(" extends Cli {\n\n    public ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("(")).output(mark("box", "validname", "FirstUpperCase")).output(literal("Box box) {\n        super();\n        add(\"help\", \"h\", \"Show this help\", emptyList(), emptyList(), \"\", (properties, command, args) -> new Text(help(properties)));\n        ")).output(mark("command").multiple("\n")).output(literal("\n        ")).output(mark("confirmation").multiple("\n")).output(literal("\n    }\n\n    @Override\n    protected String initialState() {\n        return \"")).output(mark("initialState")).output(literal("\";\n    }\n\n    private String help(MessageProperties properties) {\n        return this.commandsInfoByState(contexts().get(properties.token()).state()).keySet().stream()\n                .map((c) -> new StringBuilder().append(formatCommand(c, this.commandsInfoByState(contexts().get(properties.token()).state()).get(c))))\n                .collect(Collectors.joining(\"\\n\"));\n    }\n\n    private static String formatCommand(String command, CommandInfo info) {\n        return \"`\" + command.substring(command.lastIndexOf(\"$\") + 1) + \"|\" + info.abbr() + helpParameters(info.parameters()) + \"` \" + info.description();\n    }\n\n    private static String helpParameters(List<String> parameters) {\n        return parameters.isEmpty() ? \"\" : \" <\" + String.join(\"> <\", parameters) + \">\";\n    }\n}")),
				rule().condition((type("confirmation"))).output(literal("addQuestionOption(\"")).output(mark("option")).output(literal("\", (properties, command, args) -> new ")).output(mark("option", "firstUpperCase")).output(literal("Command(box).execute(properties, command, args));")),
				rule().condition((type("command"))).output(literal("add(\"")).output(mark("command")).output(literal("\", \"")).output(mark("abbreviation")).output(literal("\", \"")).output(mark("description")).output(literal("\", ")).output(mark("parameters")).output(literal(", ")).output(mark("preconditions")).output(literal(", \"")).output(mark("postcondition")).output(literal("\", (properties, command, args) -> new ")).output(mark("name", "firstUpperCase")).output(literal("Command(box).execute(properties, command, args));")),
				rule().condition((allTypes("list", "empty"))).output(literal("emptyList()")),
				rule().condition((type("list"))).output(literal("List.of(\"")).output(mark("item").multiple("\", \"")).output(literal("\")"))
		);
	}
}