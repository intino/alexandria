package io.intino.konos.builder.codegeneration.datalake;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class NessEventsTemplate extends Template {

	protected NessEventsTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new NessEventsTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "events"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(";\n\nimport io.intino.konos.jms.TopicConsumer;\nimport io.intino.konos.jms.TopicProducer;\nimport io.intino.konos.jms.Consumer;\nimport io.intino.konos.jms.Bus;\nimport org.apache.activemq.ActiveMQConnection;\nimport org.apache.activemq.advisory.DestinationListener;\nimport org.apache.activemq.advisory.DestinationSource;\nimport org.apache.activemq.command.ActiveMQTopic;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Configuration;\n")).add(expression().add(mark("eventHandlerImport"))).add(literal("\nimport javax.jms.JMSException;\nimport javax.jms.Message;\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n\npublic class NessEvents {\n\tprivate static Logger logger = Logger.getGlobal();\n\n\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Configuration configuration;\n\tprivate ")).add(mark("box", "firstUppercase")).add(literal("Box box;\n\n\tpublic static void addHandlers(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\t")).add(mark("eventHandler", "subscribe").multiple("\n")).add(literal("\n\t}\n\n\tprivate static class Subscriptor implements Consumer {\n\n\t\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n\t\tSubscriptor(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\t\tthis.box = box;\n\t\t}\n\n\t\tpublic void consume(Message message) {\n\t\t\tString text = textFrom(message);\n\t\t\tString type = typeOf(text);\n\t\t\t")).add(mark("eventHandler", "select").multiple("\n")).add(literal("\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "eventHandler")), (condition("trigger", "field"))).add(literal("public static final String ")).add(mark("name")).add(literal(" = ")).add(mark("messageType", "format")).add(literal(";")),
			rule().add((condition("type", "eventHandler")), (condition("trigger", "subscribe"))).add(literal("new TopicConsumer(box.datalake().session(), ")).add(mark("messageType", "format")).add(literal(").listen(new Subscriptor(box)")).add(expression().add(literal(", ")).add(mark("durable"))).add(literal(");")),
			rule().add((condition("type", "durable")), (condition("trigger", "durable"))).add(literal("box.configuration.")).add(mark("conf")).add(literal("Configuration().clientID")).add(expression().add(mark("custom", "replace").multiple(""))),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", ")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "formatMessage"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom", "customType").multiple(""))),
			rule().add((condition("trigger", "customType"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", box.configuration.")).add(mark("conf")).add(literal("Configuration().")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "format"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", box.configuration.")).add(mark("conf")).add(literal("Configuration().")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "eventHandler")), (condition("trigger", "select"))).add(literal("if (")).add(mark("messageType", "formatMessage")).add(literal(".equalsIgnoreCase(type) || \"")).add(mark("simpleMessageType", "shortPath")).add(literal("\".equalsIgnoreCase(type)) {\n\t")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("EventHandler eventHandler = new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("EventHandler();\n\teventHandler.box = box;\n\teventHandler.message = io.intino.ness.inl.Inl.messageOf(text);\n\teventHandler.execute();\n}")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("conf", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "eventHandlerImport"))).add(literal("import ")).add(mark("value", "validPackage")).add(literal(".eventhandlers.*;"))
		);
		return this;
	}
}