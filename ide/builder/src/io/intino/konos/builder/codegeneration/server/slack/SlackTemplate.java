package io.intino.konos.builder.codegeneration.server.slack;

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
			rule().add((condition("type", "slack & gen"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport io.intino.konos.slack.Bot;\n\nimport java.io.IOException;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n\npublic class ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBot extends Bot {\n\tprivate static Logger LOG = Logger.getGlobal();\n\n\n\tpublic ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBot(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box) {\n\t\tsuper(box.configuration().")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration().token);\n\t\tadd(\"help\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Show this help\", (properties, args) -> {\n            final String help = ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBotActions.help(box, properties, this.getCommandsInfo());\n            return help == null || help.isEmpty() ? help() : help;\n        });\n\t\t")).add(mark("request", "add").multiple("\n")).add(literal("\n\t\ttry {\n\t\t\texecute();\n\t\t\t")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBotActions.init(box, session());\n\t\t} catch (IOException e) {\n\t\t\tLOG.log(Level.SEVERE, e.getMessage(), e);\n\t\t}\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\tpublic void notify(String message) {")).add(literal("\n")).add(literal("\t\tsend(\"")).add(mark("channel")).add(literal("\", message);")).add(literal("\n")).add(literal("\t}"))).add(literal("\n}")),
			rule().add((condition("type", "slack & actions"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport io.intino.konos.Box;\nimport io.intino.konos.slack.Bot.MessageProperties;\n\nimport java.util.Map;\n\npublic class ")).add(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBotActions {\n\n    static String help(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box, MessageProperties properties, Map<String, Bot.CommandInfo> info) {\n        return \"\";\n    }\n\n    public static void init(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box, com.ullink.slack.simpleslackapi.SlackSession session) {\n\n    }\n\n\t")).add(mark("request", "method").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "add"))).add(literal("add(\"")).add(mark("name")).add(literal("\".toLowerCase(), java.util.Arrays.asList(")).add(mark("parameter", "name").multiple(", ")).add(literal("), java.util.Arrays.asList(")).add(mark("component").multiple(", ")).add(literal("), \"")).add(mark("description")).add(literal("\", (properties, args) -> ")).add(mark("bot", "firstUpperCase", "snakeCaseToCamelCase")).add(literal("SlackBotActions.")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("(box, properties")).add(expression().add(literal(", ")).add(mark("parameter", "cast").multiple(", "))).add(literal("));")),
			rule().add((condition("type", "request & newMethod"))).add(literal("static String ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box, MessageProperties properties")).add(expression().add(literal(", ")).add(mark("parameter").multiple(", "))).add(literal(") {\n\treturn \"\";\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "method"))).add(literal("static String ")).add(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).add(literal("(")).add(mark("box", "validname", "FirstUpperCase")).add(literal("Box box, MessageProperties properties")).add(expression().add(literal(", ")).add(mark("parameter").multiple(", "))).add(literal(") {\n\treturn \"\";\n}")),
			rule().add((condition("trigger", "component"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(literal("\"")).add(mark("name")).add(literal("\"")),
			rule().add((condition("type", "parameter & Boolean")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? Boolean.parseBoolean(args[")).add(mark("pos")).add(literal("]) : false")),
			rule().add((condition("type", "parameter & Double")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? Double.parseDouble(args[")).add(mark("pos")).add(literal("]) : 0")),
			rule().add((condition("type", "parameter & Integer")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? Integer.parseInt(args[")).add(mark("pos")).add(literal("]) : 0")),
			rule().add((condition("type", "parameter & String & multiple")), (condition("trigger", "cast"))).add(literal("args")),
			rule().add((condition("type", "parameter & String")), (condition("trigger", "cast"))).add(literal("args.length > ")).add(mark("pos")).add(literal(" ? args[")).add(mark("pos")).add(literal("] : \"\"")),
			rule().add((condition("type", "parameter"))).add(mark("type")).add(literal(" ")).add(mark("name"))
		);
		return this;
	}
}