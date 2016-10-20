package io.intino.pandora.plugin.codegeneration.server.jms.service;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMSNotifierTemplate extends Template {

	protected JMSNotifierTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMSNotifierTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "notifier"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport io.intino.pandora.jms.QueueProducer;\nimport javax.jms.JMSException;\nimport javax.jms.Session;\nimport javax.jms.Message;\nimport javax.jms.BytesMessage;\nimport javax.jms.TextMessage;\n\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Notifier {\n\n\tprivate final Session session;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Notifier(Session session) {\n\t\tthis.session = session;\n\t}\n\n\t")).add(mark("notification").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "notification"))).add(literal("public void notify")).add(mark("name", "firstUpperCase")).add(literal("(")).add(expression().add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\tfinal ")).add(mark("returnMessageType")).add(literal("Message message = session.create")).add(mark("returnMessageType")).add(literal("Message();\n\tfill")).add(mark("name", "firstUpperCase")).add(literal("Message(message")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(");\n\tnew QueueProducer(session, \"")).add(mark("queue")).add(literal("\").produce(message);\n}\n\nprivate void fill")).add(mark("name", "firstUpperCase")).add(literal("Message(")).add(mark("returnMessageType")).add(literal("Message message")).add(expression().add(literal(", ")).add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter & fileData")), (condition("trigger", "assign"))).add(literal("try {\n\tmessage.writeBytes(java.nio.file.Files.readAllBytes(")).add(mark("name")).add(literal(".toPath()));\n} catch (java.io.IOException e) {\n\tthrow new JMSException(\"file cannot be read\");\n}\n")),
			rule().add((condition("type", "parameter & objectData")), (condition("trigger", "assign"))).add(literal("message.setText(new Gson().toJson(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("message.set")).add(mark("type", "formatted")).add(literal("Property(\"")).add(mark("name")).add(literal("\", ")).add(mark("name")).add(literal(");")),
			rule().add((condition("attribute", "Integer")), (condition("trigger", "formatted"))).add(literal("Int")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}