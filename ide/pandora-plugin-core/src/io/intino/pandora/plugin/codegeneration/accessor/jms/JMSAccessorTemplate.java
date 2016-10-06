package io.intino.pandora.plugin.codegeneration.accessor.jms;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.CRLF;

public class JMSAccessorTemplate extends Template {

	protected JMSAccessorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMSAccessorTemplate(Locale.ENGLISH, CRLF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "accessor"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport org.siani.pandora.exceptions.*;\nimport org.siani.pandora.jms.QueueProducer;\nimport com.google.gson.Gson;\n\nimport javax.jms.*;\n")).add(mark("formatImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("JMSAccessor {\n\n\tprivate final Session session;\n\n\tpublic ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("JMSAccessor(Session session) {\n\t\tthis.session = session;\n\t}\n\n\t")).add(mark("resource").multiple("\n\n")).add(literal("\n\n\t")).add(mark("resource", "interface").multiple("\n\n")).add(literal("\n\n\tprivate static String createRandomString() {\n\t\tjava.util.Random random = new java.util.Random(System.currentTimeMillis());\n\t\tlong randomLong = random.nextLong();\n\t\treturn Long.toHexString(randomLong);\n\t}\n}")),
			rule().add((condition("type", "resource & reply")), (condition("trigger", "interface"))).add(literal("public interface ")).add(mark("name", "firstUpperCase")).add(literal("Response extends MessageListener {\n\n\tvoid callback(")).add(mark("reply", "return")).add(literal(" value);\n\n\tdefault void onMessage(Message message) {\n\t\ttry {\n\t\t\tcallback(")).add(mark("reply")).add(literal(");\n\t\t} catch (JMSException e) {\n\t\t\te.printStackTrace();\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "resource")), (condition("trigger", "interface"))),
			rule().add((condition("type", "resource & reply"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter", "signature").multiple(", ")).add(literal(", "))).add(mark("name", "firstUpperCase")).add(literal("Response callback) throws JMSException {\n\tDestination temporaryQueue = session.createTemporaryQueue();\n\tsession.createConsumer(temporaryQueue).setMessageListener(callback);\n\tfinal ")).add(mark("messageType")).add(literal("Message message = session.create")).add(mark("messageType")).add(literal("Message();\n\tmessage.setJMSReplyTo(temporaryQueue);\n\tmessage.setJMSCorrelationID(createRandomString());\n\tfill")).add(mark("name", "firstUpperCase")).add(literal("(message")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(");\n\tnew QueueProducer(session, \"")).add(mark("queue")).add(literal("\").produce(message);\n}\n\nprivate void fill")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("messageType")).add(literal("Message message")).add(expression().add(literal(", ")).add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "resource"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\tfinal TextMessage message = session.createTextMessage();\n\tfill")).add(mark("name", "firstUpperCase")).add(literal("(message")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(");\n\tnew QueueProducer(session, \"")).add(mark("queue")).add(literal("\").produce(message);\n}\n\nprivate void fill")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("messageType")).add(literal("Message message")).add(expression().add(literal(", ")).add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter & objectData")), (condition("trigger", "assign"))).add(literal("message.setText(new Gson().toJson(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("message.setStringProperty(\"")).add(mark("name")).add(literal("\", ")).add(mark("name")).add(literal(");")),
			rule().add((condition("type", "reply")), (condition("trigger", "type"))).add(literal("Text")),
			rule().add((condition("type", "reply & fileData")), (condition("trigger", "type"))).add(literal("Bytes")),
			rule().add((condition("type", "reply")), (condition("trigger", "return"))).add(mark("value")),
			rule().add((condition("type", "reply & fileData")), (condition("trigger", "reply"))).add(literal("((ByteMessage) message).getBytes()")),
			rule().add((condition("type", "reply")), (condition("trigger", "reply"))).add(literal("new Gson().fromJson(((TextMessage) message).getText(), ")).add(mark("value")).add(literal(".class)")),
			rule().add((condition("type", "formatImport"))).add(literal("import ")).add(mark("package")).add(literal(".formats.*;"))
		);
		return this;
	}
}