package io.intino.pandora.plugin.codegeneration.server.jms.channel;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class ChannelTemplate extends Template {

	protected ChannelTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ChannelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "channel"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport ")).add(mark("package")).add(literal(".subscriptions.*;\nimport tara.magritte.Graph;\nimport org.siani.pandora.jms.*;\n")).add(mark("schemaImport")).add(literal("\n\nimport javax.jms.Session;\nimport javax.jms.JMSException;\n\npublic class ")).add(mark("name", "firstUppercase")).add(literal("Channel {\n\n\tpublic static void init(Session session, Graph graph) {\n\t\t")).add(mark("subscription").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "subscription"))).add(literal("new ")).add(mark("type")).add(literal("Consumer(session, \"")).add(mark("path")).add(literal("\").listen(new ")).add(mark("name", "firstUpperCase")).add(literal("Subscription(graph));")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}