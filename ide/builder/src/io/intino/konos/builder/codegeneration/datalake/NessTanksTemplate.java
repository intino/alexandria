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
			rule().add((condition("type", "tanks"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".ness;\n\nimport io.intino.konos.jms.TopicConsumer;\nimport io.intino.konos.jms.TopicProducer;\nimport io.intino.konos.datalake.Ness;\nimport io.intino.konos.jms.Consumer;\nimport io.intino.konos.jms.Bus;\nimport org.apache.activemq.ActiveMQConnection;\nimport org.apache.activemq.advisory.DestinationListener;\nimport org.apache.activemq.advisory.DestinationSource;\nimport org.apache.activemq.command.ActiveMQTopic;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Configuration;\n")).add(expression().add(mark("tankImport"))).add(literal("\nimport javax.jms.JMSException;\nimport javax.jms.Message;\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.HashMap;\n\npublic class NessTanks {\n\t")).add(mark("tank", "field").multiple("\n")).add(literal("\n\n\tpublic static void registerTanks(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\t")).add(mark("tank", "assign").multiple("\n")).add(literal("\n\t\tfinal String clientID = box.configuration().nessConfiguration().clientID;\n\t\t")).add(mark("tank", "subscribe").multiple("\n")).add(literal("\n\t}\n\n\tpublic static List<io.intino.konos.datalake.Datalake.Tank> tanks() {\n\t\tList<io.intino.konos.datalake.Datalake.Tank> tanks = new ArrayList<>();\n\t\t")).add(mark("tank", "add").multiple("\n")).add(literal("\n\t\treturn tanks;\n\t}\n\n\t")).add(mark("tank", "getter").multiple("\n\n")).add(literal("\n\n\t")).add(mark("tank", "class").multiple("\n\n")).add(literal("\n\n\tpublic static void unregister() {\n\t\t")).add(mark("tank", "unregister").multiple("\n")).add(literal("\n\t}\n\n\tprivate static boolean isRegisterOnly(Message message) {\n\t\ttry {\n\t\t\treturn message.getBooleanProperty(io.intino.konos.datalake.Datalake.REGISTER_ONLY);\n\t\t} catch (JMSException e) {\n\t\t\treturn false;\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "tank")), (condition("trigger", "unregister"))).add(mark("name", "firstLowerCase")).add(literal(".unregister();")),
			rule().add((condition("type", "tank")), (condition("trigger", "field"))).add(literal("private static io.intino.konos.datalake.Datalake.Tank ")).add(mark("name", "firstLowerCase")).add(literal(";")),
			rule().add((condition("type", "tank")), (condition("trigger", "assign"))).add(mark("name", "firstLowerCase")).add(literal(" = box.datalake().add(\"")).add(mark("messageType")).add(literal("\");\n")).add(mark("name", "firstLowerCase")).add(literal(".handler(new ")).add(mark("name", "FirstUpperCase")).add(literal("Handler(box));")),
			rule().add((condition("type", "tank")), (condition("trigger", "add"))).add(literal("tanks.add(NessTanks.")).add(mark("name", "firstLowerCase")).add(literal(");")),
			rule().add((condition("type", "tank")), (condition("trigger", "getter"))).add(literal("public static io.intino.konos.datalake.Datalake.Tank ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn NessTanks.")).add(mark("name", "firstLowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "tank")), (condition("trigger", "subscribe"))).add(mark("name", "firstLowerCase")).add(literal(".flow(clientID != null ? clientID + \"-")).add(mark("name", "lowerCase")).add(literal("\" : null);")),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", ")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "formatMessage"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom", "customType").multiple(""))),
			rule().add((condition("trigger", "tank")), (condition("trigger", "class"))).add(literal("public static class ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Handler implements io.intino.konos.datalake.MessageHandler {\n\tprivate final ")).add(mark("box", "firstUppercase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("Handler(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void handle(io.intino.ness.inl.Message m) {\n\t\ttry {\n\t\t\t")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("MessageHandler tank = new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("MessageHandler();\n\t\t\ttank.box = box;\n\t\t\ttank.")).add(mark("type", "typeName")).add(literal(" = ")).add(mark("type", "load")).add(literal(";\n\t\t\ttank.execute();\n\t\t} catch(Throwable e) {\n\t\t\torg.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);\n\t\t}\n\t}\n}")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("conf", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "tankImport"))).add(literal("import ")).add(mark("value", "validPackage")).add(literal(".ness.messagehandlers.*;")),
			rule().add((condition("type", "schema")), (condition("trigger", "load"))).add(literal("io.intino.konos.alexandria.Inl.fromMessage(m, ")).add(mark("package")).add(literal(".schemas.")).add(mark("name", "FirstUpperCase")).add(literal(".class)")),
			rule().add((condition("trigger", "load"))).add(literal("m")),
			rule().add((condition("type", "schema")), (condition("trigger", "typeName"))).add(mark("name", "firstLowerCase")),
			rule().add((condition("trigger", "typeName"))).add(literal("message"))
		);
		return this;
	}
}