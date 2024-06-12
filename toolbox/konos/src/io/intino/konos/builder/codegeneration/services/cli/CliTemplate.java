package io.intino.konos.builder.codegeneration.services.cli;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class CliTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("cli", "service")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.cli.CliManager;\nimport io.intino.alexandria.cli.schemas.BotTalk;\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("Service {\n\n    private static ")).output(placeholder("name", "pascalCase")).output(literal("ServiceAuthenticator authenticator;\n\n    public static void setup(AlexandriaSpark spark, ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box, ")).output(placeholder("name", "pascalCase")).output(literal(" cli) {\n    \tauthenticator = new ")).output(placeholder("name", "pascalCase")).output(literal("ServiceAuthenticator(box);\n        spark.route(\"/cli/")).output(placeholder("name", "camelCaseToKebabCase", "lowerCase")).output(literal("\")\n                .before(manager -> { if (manager.fromHeader(\"Authorization\") == null || !authenticator.isAuthenticated(manager.fromHeader(\"Authorization\").replace(\"Bearer \", \"\"))) throw new io.intino.alexandria.exceptions.Unauthorized(\"Credential not found\");})\n                .post(manager -> manager.write(io.intino.alexandria.rest.ResponseAdapter.adapt(new CliManager(cli, io.intino.alexandria.rest.RequestAdapter.adapt(manager.fromBody(), BotTalk.class)).execute(manager.fromHeader(\"Authorization\").replace(\"Bearer \", \"\")))));\n    }\n}")));
		rules.add(rule().condition(allTypes("cli", "authenticator")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("ServiceAuthenticator {\n \tprivate ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box;\n\n \tpublic ")).output(placeholder("name", "pascalCase")).output(literal("ServiceAuthenticator(")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic boolean isAuthenticated(String token) {\n\t\treturn false;\n\t}\n}")));
		rules.add(rule().condition(allTypes("cli")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport io.intino.alexandria.cli.Cli;\nimport io.intino.alexandria.cli.command.MessageProperties;\nimport io.intino.alexandria.cli.response.Text;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".cli.commands.*;\n\nimport java.io.IOException;\nimport java.util.Arrays;\nimport java.util.List;\nimport java.util.stream.Collectors;\n\nimport static java.util.Collections.emptyList;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal(" extends Cli {\n\n    public ")).output(placeholder("name", "pascalCase")).output(literal("(")).output(placeholder("box", "validname", "FirstUpperCase")).output(literal("Box box) {\n        super();\n        add(\"help\", \"h\", \"Show this help\", emptyList(), emptyList(), \"\", (properties, command, args) -> new Text(help(properties)));\n        ")).output(placeholder("command").multiple("\n")).output(literal("\n        ")).output(placeholder("confirmation").multiple("\n")).output(literal("\n    }\n\n    @Override\n    protected String initialState() {\n        return \"")).output(placeholder("initialState")).output(literal("\";\n    }\n\n    private String help(MessageProperties properties) {\n        return this.commandsInfoByState(contexts().get(properties.token()).state()).keySet().stream()\n                .map((c) -> new StringBuilder().append(formatCommand(c, this.commandsInfoByState(contexts().get(properties.token()).state()).get(c))))\n                .collect(Collectors.joining(\"\\n\"));\n    }\n\n    private static String formatCommand(String command, CommandInfo info) {\n        return \"`\" + command.substring(command.lastIndexOf(\"$\") + 1) + \"|\" + info.abbr() + helpParameters(info.parameters()) + \"` \" + info.description();\n    }\n\n    private static String helpParameters(List<String> parameters) {\n        return parameters.isEmpty() ? \"\" : \" <\" + String.join(\"> <\", parameters) + \">\";\n    }\n}")));
		rules.add(rule().condition(allTypes("confirmation")).output(literal("addQuestionOption(\"")).output(placeholder("option")).output(literal("\", (properties, command, args) -> new ")).output(placeholder("option", "firstUpperCase")).output(literal("Command(box).execute(properties, command, args));")));
		rules.add(rule().condition(allTypes("command")).output(literal("add(\"")).output(placeholder("command")).output(literal("\", \"")).output(placeholder("abbreviation")).output(literal("\", \"")).output(placeholder("description")).output(literal("\", ")).output(placeholder("parameters")).output(literal(", ")).output(placeholder("preconditions")).output(literal(", \"")).output(placeholder("postcondition")).output(literal("\", (properties, command, args) -> new ")).output(placeholder("name", "firstUpperCase")).output(literal("Command(box).execute(properties, command, args));")));
		rules.add(rule().condition(allTypes("list", "empty")).output(literal("emptyList()")));
		rules.add(rule().condition(allTypes("list")).output(literal("List.of(\"")).output(placeholder("item").multiple("\", \"")).output(literal("\")")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}