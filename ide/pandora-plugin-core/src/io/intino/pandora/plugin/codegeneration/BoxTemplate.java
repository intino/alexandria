package io.intino.pandora.plugin.codegeneration;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class BoxTemplate extends Template {

	protected BoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport org.apache.activemq.ActiveMQConnectionFactory;\n\nimport javax.jms.Connection;\nimport javax.jms.Session;\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(mark("hasREST")).add(literal("import io.intino.pandora.server.PandoraSpark;"))).add(literal("\n")).add(expression().add(literal("import tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\npublic class ")).add(mark("name")).add(literal("Box extends ")).add(expression().add(mark("parent")).add(literal("Box")).or(expression().add(literal("io.intino.pandora.Box")))).add(literal(" {\n\n\tprivate ")).add(mark("name")).add(literal("Configuration configuration;\n\t")).add(expression().add(mark("hasREST")).add(literal("private io.intino.pandora.server.PandoraSpark restServer;"))).add(literal("\n\tprivate javax.jms.Session jmsSession;\n\tprivate Map<String, Object> map = new LinkedHashMap<>();\n\t")).add(expression().add(mark("service", "field").multiple("\n"))).add(literal("\n\t")).add(expression().add(mark("channel", "field").multiple("\n"))).add(literal("\n\t")).add(expression().add(mark("process", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\n")).add(literal("\tprivate String graphID;")).add(literal("\n")).add(literal("\n")).add(literal("\tpublic ")).add(mark("tara")).add(literal("Box(Graph graph, ")).add(mark("tara")).add(literal("Configuration configuration) {")).add(literal("\n")).add(literal("\t\tmap.put(graphID = UUID.randomUUID().toString(), graph);")).add(literal("\n")).add(literal("\t\tthis.configuration = configuration;")).add(literal("\n")).add(literal("\t}")).add(literal("\n")).add(literal("\n")).add(literal("\tpublic Graph graph() {")).add(literal("\n")).add(literal("\t\treturn (Graph) map.get(graphID);")).add(literal("\n")).add(literal("\t}")).add(literal("\n")).add(literal("\t"))).add(literal("\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Box(")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\tthis.configuration = configuration;\n\t}\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Configuration configuration() {\n\t\treturn configuration;\n\t}\n\n\t")).add(mark("service", "getter").multiple("\n")).add(literal("\n\n\tpublic <T> T get(Class<T> tClass) {\n\t\treturn (T) map.values().stream().filter(tClass::isInstance).findFirst().orElse(null);\n\t}\n\n\n\tpublic <T> T get(String object, Class<T> tClass) {\n\t\treturn (T) map.get(object);\n\t}\n\n\tpublic ")).add(mark("name")).add(literal("Box put(String name, Object object) {\n\t\tmap.put(name, object);\n\t\treturn this;\n\t}\n\n\tpublic void init() {")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parent", "parentInit")).add(literal("super.init();")).or(expression().add(literal(" ")))).add(literal("\n\t\tinitRESTServices();\n\t\tinitJMSServices();\n\t\tinitChannels();\n\t\tinitProcesses();\n\t}\n\n\tprivate void initRESTServices() {\n\t\t")).add(mark("service", "rest").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMSServices() {\n\t\t")).add(mark("service", "jms").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initChannels() {\n\t\t")).add(mark("channel", "channels").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initProcesses() {\n\t\t")).add(mark("process", "process").multiple("\n")).add(literal("\n\t}\n\n\tpublic void stopJMSServices() {\n\n\t}\n\n}")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "rest"))).add(literal("if (restServer == null) restServer = new PandoraSpark(configuration.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(");\n")).add(mark("name", "FirstUpperCase")).add(literal("Resources.setup(restServer, this);")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "jms"))).add(literal("if (jmsSession == null) {\n\ttry {\n\t\tConnection connection = new ActiveMQConnectionFactory(configuration.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.user, configuration.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.password, configuration.")).add(mark("name", "firstLowerCase")).add(literal("Configuration.url).createConnection();\n\t\tconnection.start();\n\t\tthis.jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);\n\t} catch (javax.jms.JMSException e) {\n\t\te.printStackTrace();\n\t}\n}\nthis.")).add(mark("name", "firstLowerCase")).add(literal(" = new ")).add(mark("name", "firstUpperCase")).add(literal("JMSService(jmsSession, this);")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "field"))).add(literal("private ")).add(mark("name", "firstUpperCase")).add(literal("JMSService ")).add(mark("name", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "firstUpperCase")).add(literal("JMSService ")).add(mark("name", "firstlowerCase")).add(literal("() {\n\treturn ")).add(mark("name", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "getter"))),
			rule().add((condition("type", "service"))),
			rule().add((condition("type", "channel")), (condition("trigger", "init"))),
			rule().add((condition("type", "process")), (condition("trigger", "init"))),
			rule().add((condition("trigger", "import"))),
			rule().add((condition("trigger", "parentInit")))
		);
		return this;
	}
}