package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class NessTanksTemplate extends Template {

	protected NessTanksTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new NessTanksTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "tanks"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness;\n\nimport io.intino.konos.jms.TopicConsumer;\nimport io.intino.konos.jms.TopicProducer;\nimport io.intino.konos.jms.Consumer;\nimport io.intino.konos.jms.Bus;\nimport org.apache.activemq.ActiveMQConnection;\nimport org.apache.activemq.advisory.DestinationListener;\nimport org.apache.activemq.advisory.DestinationSource;\nimport org.apache.activemq.command.ActiveMQTopic;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Configuration;\n")).add(expression().add(mark("tankImport"))).add(literal("\nimport javax.jms.JMSException;\nimport javax.jms.Message;\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic class NessTanks {\n\t")).add(mark("tank", "field").multiple("\n")).add(literal("\n\n\tpublic static void registerTanks(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\t")).add(mark("tank", "assign").multiple("\n")).add(literal("\n\t\t")).add(mark("tank", "subscribe").multiple("\n")).add(literal("\n\t}\n\n\t")).add(mark("tank", "getter").multiple("\n\n")).add(literal("\n\n\t")).add(mark("tank", "class").multiple("\n\n")).add(literal("\n\n\tpublic static void unregister() {\n\t\t")).add(mark("tank", "unregister").multiple("\n")).add(literal("\n\t}\n\n\tprivate static boolean isRegisterOnly(Message message) {\n\t\ttry {\n\t\t\treturn message.getBooleanProperty(io.intino.konos.datalake.Ness.REGISTER_ONLY);\n\t\t} catch (JMSException e) {\n\t\t\treturn false;\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "tank")), (condition("trigger", "unregister"))).add(mark("name")).add(literal(".unregister();")),
			rule().add((condition("type", "tank")), (condition("trigger", "field"))).add(literal("private static io.intino.konos.datalake.Ness.Tank ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "tank")), (condition("trigger", "assign"))).add(mark("name")).add(literal(" = box.datalake().tank(")).add(mark("messageType", "format")).add(literal(");")),
			rule().add((condition("type", "tank")), (condition("trigger", "getter"))).add(literal("public static io.intino.konos.datalake.Ness.Tank ")).add(mark("name")).add(literal("() {\n\treturn NessTanks.")).add(mark("name")).add(literal(";\n}")),
			rule().add((condition("type", "tank")), (condition("trigger", "subscribe"))).add(mark("name")).add(literal(".flow(new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Subscriptor(box), box.configuration().nessConfiguration().clientID + \"-")).add(mark("name")).add(literal("\");")),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", ")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "formatMessage"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom", "customType").multiple(""))),
			rule().add((condition("trigger", "customType"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", box.configuration().")).add(mark("conf")).add(literal("Configuration().")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "format"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", box.configuration().")).add(mark("conf")).add(literal("Configuration().")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "tank")), (condition("trigger", "class"))).add(literal("private static class ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Subscriptor implements io.intino.konos.datalake.Ness.TankFlow {\n\tprivate final ")).add(mark("box", "firstUppercase")).add(literal("Box box;\n\n\t")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Subscriptor(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void consume(Message message) {\n\t\tbox.datalake().lastMessage(java.time.Instant.now());\n\t\tif (isRegisterOnly(message)) return;\n\t\tString text = Consumer.textFrom(message);\n\t\t")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("MessageHandler tank = new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("MessageHandler();\n\t\ttank.box = box;\n\t\ttank.message = io.intino.ness.Inl.load(text).get(0);\n\t\ttank.execute();\n\t}\n}")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("conf", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "tankImport"))).add(literal("import ")).add(mark("value", "validPackage")).add(literal(".ness.messagehandlers.*;"))
		);
		return this;
	}
}