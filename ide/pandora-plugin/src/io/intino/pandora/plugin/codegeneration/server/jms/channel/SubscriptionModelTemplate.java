package io.intino.pandora.plugin.codegeneration.server.jms.channel;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class SubscriptionModelTemplate extends Template {

	protected SubscriptionModelTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SubscriptionModelTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "subscription"))).add(literal("package ")).add(mark("package")).add(literal(".subscriptions;\n\nimport com.google.gson.Gson;\nimport ")).add(mark("package", "validname")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport io.intino.pandora.jms.Consumer;\n")).add(mark("schemaImport")).add(literal("\n\nimport javax.jms.*;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Subscription implements Consumer {\n\n\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Subscription(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void consume(Message message) {\n\t\tactionFor(message).execute();\n\t}\n\n\tprivate ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action actionFor(Message message) {\n\t\tfinal ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\taction.box = this.box;\n\t\taction.message = textFrom(message);\n\t\treturn action;\n\t}\n}")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}