package io.intino.konos.builder.codegeneration.services.rocketchat;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class RocketchatTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("rocketchat", "gen"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".rocketchat.*;\n\nimport io.intino.alexandria.rocketchat.Bot;\nimport java.io.IOException;\nimport java.util.Arrays;\n\npublic class ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("RocketChatBot extends Bot {\n\tprivate ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("RocketChat ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("RocketChat;\n\t")).output(mark("level", "field").multiple("\n")).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("RocketChatBot(")).output(mark("box", "validname", "FirstUpperCase")).output(literal("Box box, String token) {\n\t\tsuper(token);\n\t\t")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("RocketChat = new ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("RocketChat(box);\n\t\t")).output(mark("level", "constructor").multiple("\n")).output(literal("\n\n\t\tadd(\"help\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Show this help\", (properties, args) -> {\n\t\t\tfinal java.util.Map<String, CommandInfo> context = this.commandsInfoByContext(contexts().get(properties.username()).command());\n\t\t\tStringBuilder builder = new StringBuilder();\n\t\t\tcontext.keySet().forEach((c) -> builder.append(formatCommand(c, context.get(c))).append(\"\\n\"));\n\t\t\treturn builder.toString();\n\t\t});\n\t\tadd(\"exit\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Exit from current level\", (properties, args) -> {\n\t\t\tfinal Context context = this.contexts().get(properties.username());\n\t\t\tif (context != null) {\n\t\t\t\tString command = context.command();\n\t\t\t\tfinal String message = command.isEmpty() ? \"\" : \"Exited from \" + (command.contains(\"|\") ? command.substring(command.lastIndexOf(\"|\") + 1) : command) + \" \" + String.join(\" \", Arrays.asList(context.getObjects()));\n\t\t\t\tcontext.command(command.contains(\"|\") ? command.substring(0, command.lastIndexOf(\"|\")) : \"\");\n\t\t\t\tcontext.objects(context.getObjects().length > 1 ? Arrays.copyOfRange(context.getObjects(), 0, context.getObjects().length - 1) : new String[0]);\n\t\t\t\treturn message;\n\t\t\t}\n\t\t\treturn \"\";\n\t\t});\n\t\tadd(\"where\", java.util.Collections.emptyList(), java.util.Collections.emptyList(), \"Shows the current level\", (properties, args) -> {\n\t\t\tfinal Context context = this.contexts().get(properties.username());\n\t\t\treturn context != null ? context : \"root\";\n\t\t});\n\t\t")).output(mark("request", "add").multiple("\n")).output(literal("\n\t\ttry {\n\t\t\tconnect();\n\t\t\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("RocketChat.init(session(), users());\n\t\t\t")).output(mark("level", "init").multiple("\n")).output(literal("\n\t\t} catch (IOException | javax.websocket.DeploymentException e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e);\n\t\t}\n\t}\n\n\tprivate static String formatCommand(String command, CommandInfo info) {\n\t\treturn \"`\" + command.substring(command.lastIndexOf(\"$\") + 1) + helpParameters(info.parameters()) + \"` \" + info.description() + \"\\n\";\n\t}\n\n\tprivate static String helpParameters(java.util.List<String> parameters) {\n\t\treturn parameters.isEmpty() ? \"\" : \" <\" + String.join(\"> <\", parameters) + \">\";\n\t}\n}")),
				rule().condition((allTypes("slack", "actions"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".slack;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".")).output(mark("box", "validname", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".slack.*;\nimport io.intino.alexandria.slack.Bot;\nimport io.intino.alexandria.slack.Bot.MessageProperties;\n\nimport java.util.Map;\n\npublic class ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("RocketChat {\n\n\tprivate ")).output(mark("box", "validname", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "firstUpperCase", "snakeCaseToCamelCase")).output(literal("RocketChat(")).output(mark("box", "validname", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void init(com.ullink.slack.simpleslackapi.RocketChatSession session) {\n\n\t}\n\n\t")).output(mark("request", "method").multiple("\n\n")).output(literal("\n}")),
				rule().condition((type("level")), (trigger("field"))).output(literal("private ")).output(mark("name", "slashToCamelCase", "firstUpperCase")).output(literal("RocketChat ")).output(mark("name", "slashToCamelCase", "firstLowerCase")).output(literal("RocketChat;")),
				rule().condition((type("level")), (trigger("constructor"))).output(mark("name", "slashToCamelCase", "firstLowerCase")).output(literal("RocketChat = new ")).output(mark("name", "slashToCamelCase", "firstUpperCase")).output(literal("RocketChat(box);")),
				rule().condition((type("level")), (trigger("init"))).output(mark("name", "slashToCamelCase", "firstLowerCase")).output(literal("RocketChat.init(session(), users());")),
				rule().condition((type("request")), (trigger("add"))).output(literal("add(\"")).output(mark("name", "lowerCase")).output(literal("\", \"")).output(mark("context", "lowercase")).output(literal("\", java.util.Arrays.asList(")).output(mark("parameter", "name").multiple(", ")).output(literal("), java.util.Arrays.asList(")).output(mark("component").multiple(", ")).output(literal("), \"")).output(mark("description")).output(literal("\", (properties, args) -> ")).output(mark("type", "slashToCamelCase", "firstLowerCase")).output(literal("RocketChat.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(properties")).output(expression().output(literal(", ")).output(mark("parameter", "cast").multiple(", "))).output(literal("));")),
				rule().condition((allTypes("request", "newMethod"))).output(literal("public String ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(MessageProperties properties")).output(expression().output(literal(", ")).output(mark("parameter").multiple(", "))).output(literal(") {\n\treturn \"\";\n}")),
				rule().condition((type("request")), (trigger("method"))).output(literal("public ")).output(mark("responseType")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(MessageProperties properties")).output(expression().output(literal(", ")).output(mark("parameter").multiple(", "))).output(literal(") {\n\treturn \"\";\n}")),
				rule().condition((trigger("component"))).output(literal("\"")).output(mark("value")).output(literal("\"")),
				rule().condition((type("parameter")), (trigger("name"))).output(literal("\"")).output(mark("name")).output(literal("\"")),
				rule().condition((allTypes("parameter", "Boolean")), (trigger("cast"))).output(literal("args.length > ")).output(mark("pos")).output(literal(" ? Boolean.parseBoolean(args[")).output(mark("pos")).output(literal("]) : false")),
				rule().condition((allTypes("parameter", "Double")), (trigger("cast"))).output(literal("args.length > ")).output(mark("pos")).output(literal(" ? Double.parseDouble(args[")).output(mark("pos")).output(literal("]) : 0")),
				rule().condition((allTypes("parameter", "Integer")), (trigger("cast"))).output(literal("args.length > ")).output(mark("pos")).output(literal(" ? Integer.parseInt(args[")).output(mark("pos")).output(literal("]) : 0")),
				rule().condition((allTypes("parameter", "String", "multiple")), (trigger("cast"))).output(literal("args")),
				rule().condition((allTypes("parameter", "String")), (trigger("cast"))).output(literal("args.length > ")).output(mark("pos")).output(literal(" ? args[")).output(mark("pos")).output(literal("] : \"\"")),
				rule().condition((allTypes("parameter", "multiple"))).output(mark("type")).output(literal("[] ")).output(mark("name")),
				rule().condition((type("parameter"))).output(mark("type")).output(literal(" ")).output(mark("name"))
		);
	}
}