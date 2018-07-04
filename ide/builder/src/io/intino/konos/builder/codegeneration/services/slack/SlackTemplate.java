package io.intino.konos.builder.codegeneration.services.slack;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class SlackTemplate extends Template {

	protected SlackTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SlackTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "slack & gen"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport ")).add(mark("package", "ValidPackage")).add(literal(".slack.*;\n\nimport io.intino.konos.slack.Bot;\nimport com.ullink.slack.simpleslackapi.SlackAttachment;\nimport java.io.IOException;\nimport java.util.Arrays;\n\npublic class ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBot extends Bot {\n\tprivate ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Slack ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Slack;\n\t")).add(mark("level", "field").multiple("\n")).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBot(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box, String token) {\n\t\tsuper(token);\n\t\t")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Slack = new ")).add(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).add(literal("Slack(box);\n\t\t")).add(mark("level", "constructor").multiple("\n")).add(literal("\n\n\t\tadd(\"help\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Show this help\", (properties, args) -> {\n\t\t\tfinal java.util.Map<String, CommandInfo> context = this.commandsInfoByContext(contexts().get(properties.username()).command());\n\t\t\tStringBuilder builder = new StringBuilder();\n\t\t\tcontext.keySet().forEach((c) -> builder.append(formatCommand(c, context.get(c))).append(\"\\n\"));\n\t\t\treturn builder.toString();\n\t\t});\n\t\tadd(\"exit\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Exit from current level\", (properties, args) -> {\n\t\t\tfinal Context context = this.contexts().get(properties.username());\n\t\t\tif (context != null) {\n\t\t\t\tString command = context.command();\n\t\t\t\tfinal String message = command.isEmpty() ? \"\" : \"Exited from \" + (command.contains(\"|\") ? command.substring(command.lastIndexOf(\"|\") + 1) : command) + \" \" + String.join(\" \", Arrays.asList(context.getObjects()));\n\t\t\t\tcontext.command(command.contains(\"|\") ? command.substring(0, command.lastIndexOf(\"|\")) : \"\");\n\t\t\t\tcontext.objects(context.getObjects().length > 1 ? Arrays.copyOfRange(context.getObjects(), 0, context.getObjects().length - 1) : new String[0]);\n\t\t\t\treturn message;\n\t\t\t}\n\t\t\treturn \"\";\n\t\t});\n\t\tadd(\"where\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Shows the current level\", (properties, args) -> {\n\t\t\tfinal Context context = this.contexts().get(properties.username());\n\t\t\treturn context != null ? context : \"root\";\n\t\t});\n\t\t")).add(mark("request", "add").multiple("\n")).add(literal("\n\t\ttry {\n\t\t\texecute();\n\t\t\tthis.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Slack.init(session());\n\t\t\t")).add(mark("level", "init").multiple("\n")).add(literal("\n\t\t} catch (IOException e) {\n\t\t\torg.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);\n\t\t}\n\t}\n\n\tprivate static String formatCommand(String command, CommandInfo info) {\n\t\treturn \"`\" + command.substring(command.lastIndexOf(\"$\") + 1) + helpParameters(info.parameters()) + \"` \" + info.description() + \"\\n\";\n\t}\n\n\tprivate static String helpParameters(java.util.List<String> parameters) {\n\t\treturn parameters.isEmpty() ? \"\" : \" <\" + String.join(\"> <\", parameters) + \">\";\n\t}\n}")),
			rule().add((condition("type", "slack & actions"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".slack;\n\nimport ")).add(mark("package", "ValidPackage")).add(literal(".")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".slack.*;\nimport io.intino.konos.slack.Bot;\nimport io.intino.konos.slack.Bot.MessageProperties;\n\nimport java.util.Map;\n\npublic class ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("Slack {\n\n\tprivate ")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("Slack(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void init(com.ullink.slack.simpleslackapi.SlackSession session) {\n\n\t}\n\n\t")).add(mark("request", "method").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "level")), (condition("trigger", "field"))).add(literal("private ")).add(mark("name", "slashToCamelCase", "firstUpperCase")).add(literal("Slack ")).add(mark("name", "slashToCamelCase", "firstLowerCase")).add(literal("Slack;")),
			rule().add((condition("type", "level")), (condition("trigger", "constructor"))).add(mark("name", "slashToCamelCase", "firstLowerCase")).add(literal("Slack = new ")).add(mark("name", "slashToCamelCase", "firstUpperCase")).add(literal("Slack(box);")),
			rule().add((condition("type", "level")), (condition("trigger", "init"))).add(mark("name", "slashToCamelCase", "firstLowerCase")).add(literal("Slack.init(session());")),
			rule().add((condition("type", "request")), (condition("trigger", "add"))).add(literal("add(\"")).add(mark("name", "toLowerCase")).add(literal("\", \"")).add(mark("context", "lowercase")).add(literal("\", java.util.Arrays.asList(")).add(mark("parameter", "name").multiple(", ")).add(literal("), java.util.Arrays.asList(")).add(mark("component").multiple(", ")).add(literal("), \"")).add(mark("description")).add(literal("\", (properties, args) -> ")).add(mark("type", "slashToCamelCase", "firstLowerCase")).add(literal("Slack.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("(properties")).add(expression().add(literal(", ")).add(mark("parameter", "cast").multiple(", "))).add(literal("));")),
			rule().add((condition("type", "request & newMethod"))).add(literal("public String ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("(MessageProperties properties")).add(expression().add(literal(", ")).add(mark("parameter").multiple(", "))).add(literal(") {\n\treturn \"\";\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "method"))).add(literal("public ")).add(mark("responseType")).add(literal(" ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("(MessageProperties properties")).add(expression().add(literal(", ")).add(mark("parameter").multiple(", "))).add(literal(") {\n\treturn \"\";\n}")),
			rule().add((condition("trigger", "component"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(literal("\"")).add(mark("name")).add(literal("\"")),
			rule().add((condition("type", "parameter & Boolean")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? Boolean.parseBoolean(args[")).add(mark("pos")).add(literal("]) : false")),
			rule().add((condition("type", "parameter & Double")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? Double.parseDouble(args[")).add(mark("pos")).add(literal("]) : 0")),
			rule().add((condition("type", "parameter & Integer")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? Integer.parseInt(args[")).add(mark("pos")).add(literal("]) : 0")),
			rule().add((condition("type", "parameter & String & multiple")), (condition("trigger", "cast"))).add(literal("args")),
			rule().add((condition("type", "parameter & String")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? args[")).add(mark("pos")).add(literal("] : \"\"")),
			rule().add((condition("type", "parameter & multiple"))).add(mark("type")).add(literal("[] ")).add(mark("name")),
			rule().add((condition("type", "parameter"))).add(mark("type")).add(literal(" ")).add(mark("name"))
		);
		return this;
	}
}