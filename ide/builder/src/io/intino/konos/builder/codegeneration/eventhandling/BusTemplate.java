package io.intino.konos.builder.codegeneration.eventhandling;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BusTemplate extends Template {

	protected BusTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BusTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "bus"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(";\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Configuration;\nimport io.intino.konos.jms.TopicConsumer;\nimport io.intino.konos.jms.TopicProducer;\nimport io.intino.konos.jms.Consumer;\nimport ")).add(mark("package", "validPackage")).add(literal(".eventhandlers.*;\nimport javax.jms.JMSException;\nimport javax.jms.Message;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n\npublic class ")).add(mark("name", "firstUppercase")).add(literal("Bus {\n\tprivate static Logger logger = Logger.getGlobal();\n\n\tprivate javax.jms.Session session;\n\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Configuration configuration;\n\tprivate ")).add(mark("box", "firstUppercase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "firstUppercase")).add(literal("Bus(")).add(mark("box", "firstUppercase")).add(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.configuration = box.configuration();\n\t\t")).add(mark("box", "firstUpperCase")).add(literal("Configuration.")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = this.configuration.")).add(mark("name", "firstLowerCase")).add(literal("Configuration();\n\t\ttry {\n\t\t\tjavax.jms.Connection connection = new org.apache.activemq.ActiveMQConnectionFactory(configuration.user, configuration.password, configuration.url).createConnection();\n\t\t\tif (configuration.clientID != null) connection.setClientID(configuration.clientID);\n\t\t\tconnection.start();\n\t\t\tthis.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);\n\t\t\t\t")).add(mark("eventHandler", "subscribe").multiple("\n")).add(literal("\n\t\t} catch (JMSException e) {\n\t\t\tlogger.log(Level.SEVERE, e.getMessage(), e);\n\t\t}\n\t}\n\n\tpublic javax.jms.Session session() {\n\t\treturn session;\n\t}\n\n\tpublic TopicProducer newTopicProducer(String path) {\n\t\ttry {\n\t\t\treturn new TopicProducer(session(), path);\n\t\t} catch (JMSException e) {\n\t\t\tlogger.severe(e.getMessage());\n\t\t\treturn null;\n\t\t}\n\t}\n\n\tpublic void closeSession() {\n\t\ttry {\n\t\t\tsession.close();\n\t\t} catch (JMSException e) {\n\t\t\tlogger.log(Level.SEVERE, e.getMessage(), e);\n\t\t}\n\t}\n\n\tprivate static class Subscriptor implements Consumer {\n\n\t\tprivate ")).add(mark("box", "firstUpperCase")).add(literal("Box box;\n\n\t\tSubscriptor(")).add(mark("box", "firstUpperCase")).add(literal("Box box) {\n\t\t\tthis.box = box;\n\t\t}\n\n\t\tpublic void consume(Message message) {\n\t\t\tString text = textFrom(message);\n\t\t\t")).add(mark("eventHandler", "select").multiple("\n")).add(literal("\n\t\t}\n\t}\n}")),
			rule().add((condition("type", "eventHandler")), (condition("trigger", "field"))).add(literal("public static final String ")).add(mark("name")).add(literal(" = ")).add(mark("messageType", "format")).add(literal(";")),
			rule().add((condition("type", "eventHandler")), (condition("trigger", "subscribe"))).add(literal("new TopicConsumer(session, ")).add(mark("messageType", "format")).add(literal(").listen(new Subscriptor(box)")).add(expression().add(literal(", ")).add(mark("durable"))).add(literal(");")),
			rule().add((condition("type", "durable")), (condition("trigger", "durable"))).add(literal("configuration.")).add(mark("conf")).add(literal("Configuration().clientID")).add(expression().add(mark("custom", "replace").multiple(""))),
			rule().add((condition("type", "custom")), (condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", ")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "formatMessage"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom", "customType").multiple(""))),
			rule().add((condition("trigger", "customType"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", box.configuration.")).add(mark("conf")).add(literal("Configuration().")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "format"))).add(literal("\"")).add(mark("name")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", this.configuration.")).add(mark("conf")).add(literal("Configuration().")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("trigger", "eventHandler")), (condition("trigger", "select"))).add(literal("if (")).add(mark("messageType", "formatMessage")).add(literal(".equals(typeOf(message))) {\n\t")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("EventHandler eventHandler = new ")).add(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).add(literal("EventHandler();\n\teventHandler.box = box;\n\teventHandler.message = text;\n\teventHandler.execute();\n}")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("conf", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")"))
		);
		return this;
	}
}