package io.intino.konos.builder.codegeneration.accessor.jms;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMSAccessorTemplate extends Template {

	protected JMSAccessorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMSAccessorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "accessor"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(";\n\nimport io.intino.konos.exceptions.*;\nimport io.intino.konos.jms.QueueProducer;\nimport com.google.gson.Gson;\n\nimport java.util.List;\nimport javax.jms.*;\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Accessor {\n\n\tprivate final Session session;\n\t")).add(mark("custom", "field").multiple("\n")).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Accessor(Session session, ")).add(mark("custom", "signature").multiple(", ")).add(literal(") {\n\t\tthis.session = session;\n\t\t")).add(mark("custom", "assign").multiple("\n")).add(literal("\n\t}\n\n\t")).add(mark("request").multiple("\n\n")).add(literal("\n\n\t")).add(mark("request", "interface").multiple("\n\n")).add(literal("\n\n\tprivate static String createRandomString() {\n\t\tjava.util.Random random = new java.util.Random(System.currentTimeMillis());\n\t\tlong randomLong = random.nextLong();\n\t\treturn Long.toHexString(randomLong);\n\t}\n\n\tprivate byte[] toByteArray(java.io.InputStream stream) {\n\t\ttry {\n\t\t\tjava.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();\n\t\t\tint nRead;\n\t\t\tbyte[] data = new byte[16384];\n\t\t\twhile ((nRead = stream.read(data, 0, data.length)) != -1) {\n\t\t\t\tbuffer.write(data, 0, nRead);\n\t\t\t}\n\t\t\tbuffer.flush();\n\t\t\treturn buffer.toByteArray();\n\t\t} catch (java.io.IOException e) {\n\t\t\te.printStackTrace();\n\t\t}\n\t\treturn new byte[0];\n\t}\n}")),
			rule().add((condition("type", "request & reply")), (condition("trigger", "interface"))).add(literal("public interface ")).add(mark("name", "firstUpperCase")).add(literal("Response extends MessageListener {\n\n\tvoid callback(")).add(mark("reply", "return")).add(literal(" value);\n\n\tdefault void onMessage(Message message) {\n\t\ttry {\n\t\t\t")).add(mark("reply")).add(literal("\n\n\t\t} catch (JMSException e) {\n\t\t\te.printStackTrace();\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "request")), (condition("trigger", "interface"))),
			rule().add((condition("type", "request & reply"))).add(literal("public javax.jms.MessageConsumer ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter", "signature").multiple(", ")).add(literal(", "))).add(mark("name", "firstUpperCase")).add(literal("Response callback) throws JMSException {\n\tDestination temporaryQueue = session.createTemporaryQueue();\n\tjavax.jms.MessageConsumer consumer = session.createConsumer(temporaryQueue);\n\tconsumer.setMessageListener(callback);\n\tfinal ")).add(mark("messageType")).add(literal("Message message = session.create")).add(mark("messageType")).add(literal("Message();\n\tmessage.setJMSReplyTo(temporaryQueue);\n\tmessage.setJMSCorrelationID(createRandomString());\n\tfill")).add(mark("name", "firstUpperCase")).add(literal("(message")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(");\n\tnew QueueProducer(session, \"")).add(mark("queue")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))).add(literal(").produce(message);\n\treturn consumer;\n}\n\nprivate void fill")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("messageType")).add(literal("Message message")).add(expression().add(literal(", ")).add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n}")),
			rule().add(not(condition("type", "parameter")), (condition("trigger", "field"))).add(literal("private String ")).add(mark("value", "validname")).add(literal(";")),
			rule().add(not(condition("type", "parameter")), (condition("trigger", "signature"))).add(literal("String ")).add(mark("value", "validname")),
			rule().add(not(condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("this.")).add(mark("value", "validname")).add(literal(" = ")).add(mark("value", "validname")).add(literal(";")),
			rule().add(not(condition("type", "parameter")), (condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", this.")).add(mark("value", "validname")).add(literal(")")),
			rule().add((condition("type", "request"))).add(literal("public void ")).add(mark("name")).add(literal("(")).add(expression().add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\tfinal TextMessage message = session.createTextMessage();\n\tfill")).add(mark("name", "firstUpperCase")).add(literal("(message")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(");\n\tnew QueueProducer(session, \"")).add(mark("queue")).add(literal("\").produce(message);\n}\n\nprivate void fill")).add(mark("name", "firstUpperCase")).add(literal("(")).add(mark("messageType")).add(literal("Message message")).add(expression().add(literal(", ")).add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "parameter & list")), (condition("trigger", "signature"))).add(literal("java.util.List<")).add(mark("type")).add(literal("> ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter & objectData")), (condition("trigger", "assign"))).add(literal("message.setText(new Gson().toJson(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter & fileData")), (condition("trigger", "assign"))).add(literal("message.writeBytes(toByteArray(")).add(mark("name")).add(literal(".content()));")),
			rule().add((condition("type", "parameter & list")), (condition("trigger", "assign"))).add(literal("message.setStringProperty(\"")).add(mark("name")).add(literal("\", new Gson().toJson(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("message.set")).add(mark("type", "format")).add(literal("Property(\"")).add(mark("name")).add(literal("\", ")).add(mark("name")).add(literal(");")),
			rule().add((condition("type", "reply")), (condition("trigger", "type"))).add(literal("Text")),
			rule().add((condition("type", "reply & fileData")), (condition("trigger", "type"))).add(literal("Bytes")),
			rule().add((condition("type", "reply & list")), (condition("trigger", "return"))).add(literal("List<")).add(mark("value")).add(literal(">")),
			rule().add((condition("type", "reply")), (condition("trigger", "return"))).add(mark("value")),
			rule().add((condition("attribute", "Integer")), (condition("trigger", "format"))).add(literal("Int")),
			rule().add((condition("type", "reply & fileData")), (condition("trigger", "reply"))).add(literal("byte[] bytes = new byte[(int) ((BytesMessage) message).getBodyLength()];\n((BytesMessage) message).readBytes(bytes);\ncallback(new io.intino.konos.Resource(\"file\",\"\", new java.io.ByteArrayInputStream(bytes)));")),
			rule().add((condition("type", "reply & list")), (condition("trigger", "reply"))).add(literal("java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<")).add(mark("value")).add(literal(">>(){}.getType();\ncallback(new Gson().fromJson(((TextMessage) message).getText(), listType));")),
			rule().add((condition("type", "reply")), (condition("trigger", "reply"))).add(literal("callback(new Gson().fromJson(((TextMessage) message).getText(), ")).add(mark("value")).add(literal(".class));")),
			rule().add((condition("type", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}