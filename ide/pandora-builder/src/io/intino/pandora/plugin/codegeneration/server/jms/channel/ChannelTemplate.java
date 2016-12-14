package io.intino.pandora.plugin.codegeneration.server.jms.channel;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ChannelTemplate extends Template {

	protected ChannelTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ChannelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "channel"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(";\n\nimport ")).add(mark("package", "validPackage")).add(literal(".subscriptions.*;\nimport io.intino.pandora.Box;\nimport io.intino.pandora.jms.*;\n")).add(mark("schemaImport")).add(literal("\n\nimport javax.jms.Session;\nimport javax.jms.JMSException;\n\npublic class ")).add(mark("name", "firstUppercase")).add(literal("Channel {\n\n\tpublic static void init(Session session, ")).add(mark("box")).add(literal("Box box) {\n\t\t")).add(mark("box", "firstUpperCase")).add(literal("Configuration.")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = box.configuration().")).add(mark("name", "firstLowerCase")).add(literal("Configuration;\n\t\t")).add(mark("subscription").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "subscription"))).add(literal("new ")).add(mark("type")).add(literal("Consumer(session, ")).add(mark("path", "format")).add(literal(").listen(new ")).add(mark("name", "firstUpperCase")).add(literal("Subscription(box)")).add(expression().add(literal(", ")).add(mark("durable"))).add(literal(");")),
			rule().add((condition("type", "durable")), (condition("trigger", "durable"))).add(literal("configuration.clientID")).add(expression().add(mark("custom", "replace").multiple(""))),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", ")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("type", "path")), (condition("trigger", "format"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}