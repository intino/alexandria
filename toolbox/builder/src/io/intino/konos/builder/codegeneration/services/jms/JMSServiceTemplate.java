package io.intino.konos.builder.codegeneration.services.jms;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class JMSServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("jms"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport ")).output(mark("package")).output(literal(".requests.*;\nimport io.intino.alexandria.core.Box;\nimport io.intino.alexandria.jms.")).output(mark("model")).output(literal("Consumer;\nimport io.intino.alexandria.jms.")).output(mark("model")).output(literal("Producer;\n\nimport javax.jms.BytesMessage;\nimport javax.jms.JMSException;\nimport javax.jms.Session;\nimport javax.jms.Connection;\nimport javax.jms.TextMessage;\nimport java.io.ByteArrayOutputStream;\nimport java.io.IOException;\nimport java.io.InputStream;\n\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Service extends io.intino.alexandria.jms.Bus {\n\n\tprivate ")).output(mark("box", "firstUpperCase")).output(literal("Configuration configuration;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Service(Connection connection, ")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.configuration = box.configuration();\n\t\tthis.connection = connection;\n\t\tthis.session = createSession(connection);\n\t\tif (session == null) return;\n\t\t")).output(mark("request").multiple("\n")).output(literal("\n\t}\n\n\t")).output(mark("notification").multiple("\n\n")).output(literal("\n\t\n\tprivate Session createSession(Connection connection) {\n\t\ttry {\n\t\t\treturn connection.createSession(false, Session.AUTO_ACKNOWLEDGE);\n\t\t} catch (JMSException e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);\n\t\t\treturn null;\n\t\t}\n\t}\n\n\tprivate byte[] toByteArray(InputStream stream) {\n\t\ttry {\n\t\t\tByteArrayOutputStream buffer = new ByteArrayOutputStream();\n\t\t\tint nRead;\n\t\t\tbyte[] data = new byte[16384];\n\t\t\twhile ((nRead = stream.read(data, 0, data.length)) != -1) {\n\t\t\t\tbuffer.write(data, 0, nRead);\n\t\t\t}\n\t\t\tbuffer.flush();\n\t\t\treturn buffer.toByteArray();\n\t\t} catch (IOException e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e.getMessage(), e);\n\t\t}\n\t\treturn new byte[0];\n\t}\n}")),
			rule().condition((type("request"))).output(literal("new ")).output(mark("model")).output(literal("Consumer(session, ")).output(mark("queue", "format")).output(literal(").listen(new ")).output(mark("name", "firstUpperCase")).output(literal("Request(box));")),
			rule().condition((type("notification"))).output(literal("public void notify")).output(mark("name", "firstUpperCase")).output(literal("(")).output(expression().output(mark("parameter", "signature").multiple(", "))).output(literal(") throws JMSException {\n\tfinal ")).output(mark("returnMessageType")).output(literal("Message message = session.create")).output(mark("returnMessageType")).output(literal("Message();\n\tfill")).output(mark("name", "firstUpperCase")).output(literal("Message(message")).output(expression().output(literal(", ")).output(mark("parameter", "name").multiple(", "))).output(literal(");\n\tnew ")).output(mark("model")).output(literal("Producer(session, ")).output(mark("queue", "format")).output(literal(").produce(message);\n}\n\nprivate void fill")).output(mark("name", "firstUpperCase")).output(literal("Message(")).output(mark("returnMessageType")).output(literal("Message message")).output(expression().output(literal(", ")).output(mark("parameter", "signature").multiple(", "))).output(literal(") throws JMSException {\n\t")).output(mark("parameter", "assign").multiple("\n")).output(literal("\n}")),
			rule().condition((type("queue")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", configuration.get(\"")).output(mark("value")).output(literal("\"))")),
			rule().condition((type("parameter")), (trigger("name"))).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((type("parameter")), (trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((allTypes("parameter","filedata")), (trigger("assign"))).output(literal("message.writeBytes(toByteArray(")).output(mark("name")).output(literal("));")),
			rule().condition((allTypes("objectdata","parameter")), (trigger("assign"))).output(literal("message.setText(new Gson().toJson(")).output(mark("name")).output(literal("));")),
			rule().condition((type("parameter")), (trigger("assign"))).output(literal("message.set")).output(mark("type", "formatted")).output(literal("Property(\"")).output(mark("name")).output(literal("\", ")).output(mark("name")).output(literal(");")),
			rule().condition((attribute("integer")), (trigger("formatted"))).output(literal("Int"))
		);
	}
}