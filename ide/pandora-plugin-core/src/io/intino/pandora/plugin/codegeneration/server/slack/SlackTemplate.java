package io.intino.pandora.plugin.codegeneration.server.slack;

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
			rule().add((condition("type", "slack"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport io.intino.pandora.slack.Bot;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("SlackBot extends Bot {\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("SlackBot(Graph graph) {\n\t\tsuper(\"")).add(mark("token")).add(literal("\");\n\t\t")).add(mark("request", "add").multiple("\n")).add(literal("\n\t}\n\t")).add(expression().add(literal("\n")).add(literal("\tpublic void notify(String message) {")).add(literal("\n")).add(literal("\t\tsend(\"")).add(mark("channel")).add(literal("\", message);")).add(literal("\n")).add(literal("\t}"))).add(literal("\n\n\t")).add(mark("request").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "add"))).add(literal("add(\"")).add(mark("name")).add(literal("\", \"")).add(mark("description")).add(literal("\", args -> ")).add(mark("name")).add(literal("(graph")).add(expression().add(literal(", ")).add(mark("parameter", "cast").multiple(", "))).add(literal("));")),
			rule().add((condition("type", "request"))).add(literal("private String ")).add(mark("name")).add(literal("(")).add(mark("parameter").multiple(", ")).add(literal(") {\n\treturn \"\";\n}")),
			rule().add((condition("type", "parameter & Boolean")), (condition("trigger", "cast"))).add(literal("Boolean.parseBoolean(args[")).add(mark("pos")).add(literal("])")),
			rule().add((condition("type", "parameter & Double")), (condition("trigger", "cast"))).add(literal("Double.parseDouble(args[")).add(mark("pos")).add(literal("])")),
			rule().add((condition("type", "parameter & Integer")), (condition("trigger", "cast"))).add(literal("Integer.parseInt(args[")).add(mark("pos")).add(literal("])")),
			rule().add((condition("type", "parameter & String")), (condition("trigger", "cast"))).add(literal("args[")).add(mark("pos")).add(literal("]")),
			rule().add((condition("type", "parameter"))).add(mark("type")).add(literal(" ")).add(mark("name"))
		);
		return this;
	}
}