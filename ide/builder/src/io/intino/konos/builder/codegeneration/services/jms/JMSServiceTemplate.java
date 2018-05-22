package io.intino.konos.builder.codegeneration.services.jms;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class JMSServiceTemplate extends Template {

	protected JMSServiceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMSServiceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "jms"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport ")).add(mark("package")).add(literal(".requests.*;\nimport io.intino.konos.alexandria.Box;\nimport io.intino.konos.jms.")).add(mark("model")).add(literal("Consumer;\nimport io.intino.konos.jms.")).add(mark("model")).add(literal("Producer;\n\nimport javax.jms.BytesMessage;\nimport javax.jms.JMSException;\nimport javax.jms.Session;\nimport javax.jms.Connection;\nimport javax.jms.TextMessage;\nimport java.io.ByteArrayOutputStream;\nimport java.io.IOException;\nimport java.io.InputStream;\nimport java.util.logging.Logger;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Service extends io.intino.konos.jms.Bus {\n\n\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Configuration configuration;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Service(Connection connection, ")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\tthis.configuration = box.configuration();\n\t\tthis.connection = connection;\n\t\tthis.session = createSession(connection);\n\t\tif (session == null) return;\n\t\t")).add(mark("request").multiple("\n")).add(literal("\n\t}\n\n\t")).add(mark("notification").multiple("\n\n")).add(literal("\n\t\n\tprivate Session createSession(Connection connection) {\n\t\ttry {\n\t\t\treturn connection.createSession(false, Session.AUTO_ACKNOWLEDGE);\n\t\t} catch (JMSException e) {\n\t\t\tLogger.getGlobal().severe(e.getMessage());\n\t\t\treturn null;\n\t\t}\n\t}\n\n\tprivate byte[] toByteArray(InputStream stream) {\n\t\ttry {\n\t\t\tByteArrayOutputStream buffer = new ByteArrayOutputStream();\n\t\t\tint nRead;\n\t\t\tbyte[] data = new byte[16384];\n\t\t\twhile ((nRead = stream.read(data, 0, data.length)) != -1) {\n\t\t\t\tbuffer.write(data, 0, nRead);\n\t\t\t}\n\t\t\tbuffer.flush();\n\t\t\treturn buffer.toByteArray();\n\t\t} catch (IOException e) {\n\t\t\tLogger.getGlobal().severe(e.getMessage());\n\t\t}\n\t\treturn new byte[0];\n\t}\n}")),
			rule().add((condition("type", "request"))).add(literal("new ")).add(mark("model")).add(literal("Consumer(session, ")).add(mark("queue", "format")).add(literal(").listen(new ")).add(mark("name", "firstUpperCase")).add(literal("Request(box));")),
			rule().add((condition("type", "notification"))).add(literal("public void notify")).add(mark("name", "firstUpperCase")).add(literal("(")).add(expression().add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\tfinal ")).add(mark("returnMessageType")).add(literal("Message message = session.create")).add(mark("returnMessageType")).add(literal("Message();\n\tfill")).add(mark("name", "firstUpperCase")).add(literal("Message(message")).add(expression().add(literal(", ")).add(mark("parameter", "name").multiple(", "))).add(literal(");\n\tnew ")).add(mark("model")).add(literal("Producer(session, ")).add(mark("queue", "format")).add(literal(").produce(message);\n}\n\nprivate void fill")).add(mark("name", "firstUpperCase")).add(literal("Message(")).add(mark("returnMessageType")).add(literal("Message message")).add(expression().add(literal(", ")).add(mark("parameter", "signature").multiple(", "))).add(literal(") throws JMSException {\n\t")).add(mark("parameter", "assign").multiple("\n")).add(literal("\n}")),
			rule().add((condition("type", "queue")), (condition("trigger", "format"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration.get(\"")).add(mark("value")).add(literal("\"))")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().add((condition("type", "parameter & fileData")), (condition("trigger", "assign"))).add(literal("message.writeBytes(toByteArray(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter & objectData")), (condition("trigger", "assign"))).add(literal("message.setText(new Gson().toJson(")).add(mark("name")).add(literal("));")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("message.set")).add(mark("type", "formatted")).add(literal("Property(\"")).add(mark("name")).add(literal("\", ")).add(mark("name")).add(literal(");")),
			rule().add((condition("attribute", "Integer")), (condition("trigger", "formatted"))).add(literal("Int"))
		);
		return this;
	}
}