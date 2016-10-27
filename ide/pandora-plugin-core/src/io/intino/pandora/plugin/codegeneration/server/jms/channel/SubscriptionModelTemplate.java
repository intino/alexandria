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
			rule().add((condition("type", "subscription"))).add(literal("package ")).add(mark("package")).add(literal(".subscriptions;\n\nimport com.google.gson.Gson;\nimport io.intino.pandora.Box;\nimport io.intino.pandora.jms.Consumer;\n")).add(mark("schemaImport")).add(literal("\n\nimport javax.jms.*;\n\npublic class ")).add(mark("name", "FirstUpperCase")).add(literal("Subscription implements Consumer {\n\n\tprivate Box box;\n\n\tpublic ")).add(mark("name", "FirstUpperCase")).add(literal("Subscription(Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void consume(Message message) {\n\t\tactionFor(message).execute();\n\t}\n\n\tprivate ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action actionFor(Message message) {\n\t\tfinal ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\taction.box = this.box;")).add(expression().add(literal("\n")).add(literal("\t\ttry {")).add(literal("\n")).add(literal("\t\t\t")).add(mark("message", "assign")).add(literal("\n")).add(literal("\t\t} catch (JMSException e) {")).add(literal("\n")).add(literal("\t\t\te.printStackTrace();")).add(literal("\n")).add(literal("\t\t}"))).add(literal("\n\t\treturn action;\n\t}\n}")),
			rule().add((condition("type", "message & fileData")), (condition("trigger", "assign"))).add(literal("action.message = ((BytesMessage) message).getBytes();")),
			rule().add((condition("type", "message")), (condition("trigger", "assign"))).add(literal("action.message = new Gson().fromJson(((TextMessage) message).getText(), ")).add(mark("type")).add(literal(".class);")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}