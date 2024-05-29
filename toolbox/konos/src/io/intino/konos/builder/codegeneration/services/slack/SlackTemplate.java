package io.intino.konos.builder.codegeneration.services.slack;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class SlackTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("slack", "gen")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".slack.*;\n\nimport io.intino.alexandria.slack.Bot;\nimport java.io.IOException;\nimport java.util.Arrays;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("SlackBot extends Bot {\n\tprivate ")).output(placeholder("name", "PascalCase")).output(literal("Slack ")).output(placeholder("name", "CamelCase")).output(literal("Slack;\n\t")).output(placeholder("level", "field").multiple("\n")).output(literal("\n\n\tpublic ")).output(placeholder("name", "pascalCase")).output(literal("SlackBot(")).output(placeholder("box", "validname", "FirstUpperCase")).output(literal("Box box, String token) {\n\t\tsuper(token);\n\t\t")).output(placeholder("name", "CamelCase")).output(literal("Slack = new ")).output(placeholder("name", "PascalCase")).output(literal("Slack(box);\n\t\t")).output(placeholder("level", "constructor").multiple("\n")).output(literal("\n\n\t\tadd(\"help\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Show this help\", (properties, args) -> {\n\t\t\tfinal java.util.Map<String, CommandInfo> context = this.commandsInfoByContext(contexts().get(properties.username()).command());\n\t\t\tStringBuilder builder = new StringBuilder();\n\t\t\tcontext.keySet().forEach((c) -> builder.append(formatCommand(c, context.get(c))).append(\"\\n\"));\n\t\t\treturn builder.toString();\n\t\t});\n\t\tadd(\"exit\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Exit from current level\", (properties, args) -> {\n\t\t\tfinal Context context = this.contexts().get(properties.username());\n\t\t\tif (context != null) {\n\t\t\t\tString command = context.command();\n\t\t\t\tfinal String message = command.isEmpty() ? \"\" : \"Exited from \" + (command.contains(\"|\") ? command.substring(command.lastIndexOf(\"|\") + 1) : command) + \" \" + String.join(\" \", Arrays.asList(context.getObjects()));\n\t\t\t\tcontext.command(command.contains(\"|\") ? command.substring(0, command.lastIndexOf(\"|\")) : \"\");\n\t\t\t\tcontext.objects(context.getObjects().length > 1 ? Arrays.copyOfRange(context.getObjects(), 0, context.getObjects().length - 1) : new String[0]);\n\t\t\t\treturn message;\n\t\t\t}\n\t\t\treturn \"\";\n\t\t});\n\t\tadd(\"where\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Shows the current level\", (properties, args) -> {\n\t\t\tfinal Context context = this.contexts().get(properties.username());\n\t\t\treturn context != null ? context : \"root\";\n\t\t});\n\t\t")).output(placeholder("request", "add").multiple("\n")).output(literal("\n\t\ttry {\n\t\t\tconnect();\n\t\t\tthis.")).output(placeholder("name", "CamelCase")).output(literal("Slack.init(session(), users());\n\t\t\t")).output(placeholder("level", "init").multiple("\n")).output(literal("\n\t\t} catch (IOException | javax.websocket.DeploymentException e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e);\n\t\t}\n\t}\n\n\tprivate static String formatCommand(String command, CommandInfo info) {\n\t\treturn \"`\" + command.substring(command.lastIndexOf(\"$\") + 1) + helpParameters(info.parameters()) + \"` \" + info.description() + \"\\n\";\n\t}\n\n\tprivate static String helpParameters(java.util.List<String> parameters) {\n\t\treturn parameters.isEmpty() ? \"\" : \" <\" + String.join(\"> <\", parameters) + \">\";\n\t}\n}")));
		rules.add(rule().condition(allTypes("slack", "actions")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(".slack;\n\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".")).output(placeholder("box", "validname", "FirstUpperCase")).output(literal("Box;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".slack.*;\nimport io.intino.alexandria.slack.Bot;\nimport io.intino.alexandria.slack.Bot.MessageProperties;\n\nimport java.util.Map;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("Slack {\n\n\tprivate ")).output(placeholder("box", "validname", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("name", "pascalCase")).output(literal("Slack(")).output(placeholder("box", "validname", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void init(com.ullink.slack.simpleslackapi.SlackSession session) {\n\n\t}\n\n\t")).output(placeholder("request", "method").multiple("\n\n")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("level"), trigger("field"))).output(literal("private ")).output(placeholder("name", "slashToCamelCase", "firstUpperCase")).output(literal("Slack ")).output(placeholder("name", "slashToCamelCase", "firstLowerCase")).output(literal("Slack;")));
		rules.add(rule().condition(all(allTypes("level"), trigger("constructor"))).output(placeholder("name", "slashToCamelCase", "firstLowerCase")).output(literal("Slack = new ")).output(placeholder("name", "slashToCamelCase", "firstUpperCase")).output(literal("Slack(box);")));
		rules.add(rule().condition(all(allTypes("level"), trigger("init"))).output(placeholder("name", "slashToCamelCase", "firstLowerCase")).output(literal("Slack.init(session(), users());")));
		rules.add(rule().condition(all(allTypes("request"), trigger("add"))).output(literal("add(\"")).output(placeholder("name", "lowerCase")).output(literal("\", \"")).output(placeholder("context", "lowercase")).output(literal("\", java.util.Arrays.asList(")).output(placeholder("parameter", "name").multiple(", ")).output(literal("), java.util.Arrays.asList(")).output(placeholder("component").multiple(", ")).output(literal("), \"")).output(placeholder("description")).output(literal("\", (properties, args) -> ")).output(placeholder("type", "slashToCamelCase", "firstLowerCase")).output(literal("Slack.")).output(placeholder("name", "CamelCase")).output(literal("(properties")).output(expression().output(literal(", ")).output(placeholder("parameter", "cast").multiple(", "))).output(literal("));")));
		rules.add(rule().condition(allTypes("request", "newMethod")).output(literal("public String ")).output(placeholder("name", "CamelCase")).output(literal("(MessageProperties properties")).output(expression().output(literal(", ")).output(placeholder("parameter").multiple(", "))).output(literal(") {\n\treturn \"\";\n}")));
		rules.add(rule().condition(all(allTypes("request"), trigger("method"))).output(literal("public ")).output(placeholder("responseType")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(MessageProperties properties")).output(expression().output(literal(", ")).output(placeholder("parameter").multiple(", "))).output(literal(") {\n\treturn \"\";\n}")));
		rules.add(rule().condition(trigger("component")).output(literal("\"")).output(placeholder("value")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("name"))).output(literal("\"")).output(placeholder("name")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("parameter", "Boolean"), trigger("cast"))).output(literal("args.length > ")).output(placeholder("pos")).output(literal(" ? Boolean.parseBoolean(args[")).output(placeholder("pos")).output(literal("]) : false")));
		rules.add(rule().condition(all(allTypes("parameter", "Double"), trigger("cast"))).output(literal("args.length > ")).output(placeholder("pos")).output(literal(" ? Double.parseDouble(args[")).output(placeholder("pos")).output(literal("]) : 0")));
		rules.add(rule().condition(all(allTypes("parameter", "Integer"), trigger("cast"))).output(literal("args.length > ")).output(placeholder("pos")).output(literal(" ? Integer.parseInt(args[")).output(placeholder("pos")).output(literal("]) : 0")));
		rules.add(rule().condition(all(allTypes("parameter", "String", "multiple"), trigger("cast"))).output(literal("args")));
		rules.add(rule().condition(all(allTypes("parameter", "String"), trigger("cast"))).output(literal("args.length > ")).output(placeholder("pos")).output(literal(" ? args[")).output(placeholder("pos")).output(literal("] : \"\"")));
		rules.add(rule().condition(allTypes("parameter", "multiple")).output(placeholder("type")).output(literal("[] ")).output(placeholder("name")));
		rules.add(rule().condition(allTypes("parameter")).output(placeholder("type")).output(literal(" ")).output(placeholder("name")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}