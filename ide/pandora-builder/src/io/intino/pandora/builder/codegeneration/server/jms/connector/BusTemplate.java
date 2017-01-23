package io.intino.pandora.builder.codegeneration.server.jms.connector;

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
			rule().add((condition("type", "bus"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".bus;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "firstUpperCase")).add(literal("Configuration;\n\nimport javax.jms.JMSException;\nimport java.util.logging.Level;\nimport java.util.logging.Logger;\n\npublic class ")).add(mark("name", "firstUppercase")).add(literal("Bus {\n\tprivate static Logger logger = Logger.getGlobal();\n\n\tprivate javax.jms.Session session;\n\n\tpublic ")).add(mark("name", "firstUppercase")).add(literal("Bus(")).add(mark("box")).add(literal("Box box) {\n\t\t")).add(mark("box", "firstUpperCase")).add(literal("Configuration.")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration = box.configuration().")).add(mark("name", "firstLowerCase")).add(literal("Configuration();\n\t\ttry {\n\t\t\tjavax.jms.Connection connection = new org.apache.activemq.ActiveMQConnectionFactory(configuration.user, configuration.password, configuration.url).createConnection();\n\t\t\tif (configuration.clientID != null) connection.setClientID(configuration.clientID);\n\t\t\tconnection.start();\n\t\t\tthis.session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);\n        \t} catch (JMSException e) {\n        \t\tlogger.log(Level.SEVERE, e.getMessage(), e);\n\t\t\treturn;\n\t\t}\n\t\t")).add(mark("channel", "channelinit").multiple("\n")).add(literal("\n\t}\n\n\tpublic javax.jms.Session session() {\n\t\treturn session;\n\t}\n\n\tpublic void closeSession() {\n    \t\ttry {\n    \t\t\tsession.close();\n    \t\t} catch (JMSException e) {\n\t\t\tlogger.log(Level.SEVERE, e.getMessage(), e);\n    \t\t}\n    \t}\n}")),
			rule().add((condition("trigger", "channelinit"))).add(mark("value", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Channel.init(session, box);")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("channel", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")"))
		);
		return this;
	}
}