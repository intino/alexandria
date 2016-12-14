package io.intino.pandora.plugin.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class BoxTemplate extends Template {

	protected BoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new BoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.logging.Logger;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(mark("hasREST")).add(literal("import io.intino.pandora.server.PandoraSpark;"))).add(literal("\n")).add(expression().add(literal("import tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box extends ")).add(expression().add(mark("parentPackage")).add(literal(".pandora.")).add(mark("parent", "FirstUpperCase")).add(literal("Box")).or(expression().add(literal("io.intino.pandora.Box")))).add(literal(" {\n\tprivate static Logger LOG = Logger.getGlobal();\n\t")).add(expression().add(mark("hasParent")).or(expression().add(literal("protected ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration;")))).add(literal("\n\t")).add(expression().add(mark("hasParent")).or(expression().add(literal("protected Map<String, Object> map = new LinkedHashMap<>();")))).add(literal("\n\t")).add(expression().add(mark("service", "field").multiple("\n"))).add(literal("\n\t")).add(expression().add(mark("channel", "field").multiple("\n"))).add(literal("\n\t")).add(expression().add(mark("process", "field").multiple("\n"))).add(literal("\n\n\tprivate String graphID;\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box(")).add(mark("name", "SnakeCaseToCamelCase", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(expression().add(mark("hasParent")).add(literal("super(configuration);")).add(literal("\n")).add(literal("\t\t"))).add(literal("this.configuration = configuration;")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("service", "setup").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("activity", "setup").multiple("\n"))).add(literal("\n\t}\n\n\n\tpublic ")).add(mark("tara", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box(Graph graph, ")).add(mark("tara", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(expression().add(mark("hasParent")).add(literal("super(configuration);")).add(literal("\n")).add(literal("\t\t"))).add(literal("map.put(graphID = UUID.randomUUID().toString(), graph);\n\t\tthis.configuration = configuration;")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("service", "setup").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("activity", "setup").multiple("\n"))).add(literal("\n\t}\n\n\tpublic Graph graph() {\n\t\treturn (Graph) map().get(graphID);\n\t}\n\n\tprotected Map<String, Object> map() {\n\t\treturn map;\n\t}\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration() {\n\t\treturn (")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration) configuration;\n\t}\n\n\t")).add(mark("service", "getter").multiple("\n")).add(literal("\n\n\tpublic <T> T get(Class<T> tClass) {\n\t\treturn (T) map().values().stream().filter(tClass::isInstance).findFirst().orElse(null);\n\t}\n\n\n\tpublic <T> T get(String object, Class<T> tClass) {\n\t\treturn (T) map().get(object);\n\t}\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Box put(String name, Object object) {\n\t\tmap().put(name, object);\n\t\treturn this;\n\t}\n\n\tpublic void init() {")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parent", "parentInit")).add(literal("super.init();")).or(expression().add(literal(" ")))).add(literal("\n\t\tinitActivities();\n\t\tinitRESTServices();\n\t\tinitJMXServices();\n\t\tinitJMSServices();\n\t\tinitChannels();\n\t\tinitProcesses();\n\t}\n\n\tprivate void initRESTServices() {\n\t\t")).add(mark("service", "rest").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMSServices() {\n\t\t")).add(expression().add(literal("javax.jms.Connection connection;")).add(literal("\n")).add(literal("\t\t")).add(mark("service", "jms").multiple("\n"))).add(literal("\n\t}\n\n\tprivate void initJMXServices() {\n\t\t")).add(mark("service", "jmx").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initActivities() {\n\t\t")).add(mark("activity").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initChannels() {\n\t\t")).add(expression().add(literal("try {")).add(literal("\n")).add(literal("\t\t\tjavax.jms.Connection connection;")).add(literal("\n")).add(literal("\t\t\t")).add(mark("channel", "init").multiple("\n")).add(literal("\n")).add(literal("\t\t} catch (javax.jms.JMSException e) {")).add(literal("\n")).add(literal("\t\t\tLOG.severe(e.getMessage());")).add(literal("\n")).add(literal("\t\t}"))).add(literal("\n\t}\n\n\tprivate void initProcesses() {\n\t\t")).add(mark("process", "process").multiple("\n")).add(literal("\n\t}\n\n\tpublic void stopJMSServices() {\n\n\t}\n\n}")),
			rule().add((condition("type", "activity")), (condition("trigger", "setup"))).add(literal("io.intino.pandora.server.activity.ActivityPandoraSpark.setup(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.authService);")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "setup"))).add(literal("io.intino.pandora.server.activity.ActivityPandoraSpark.setup(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(", null);")),
			rule().add((condition("type", "channel")), (condition("trigger", "init"))).add(literal("connection = new org.apache.activemq.ActiveMQConnectionFactory(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.user, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.password, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.url).createConnection();\n")).add(expression().add(mark("durable")).add(literal("\n"))).add(literal("connection.start();\n")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Channel.init(connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE), this);")),
			rule().add((condition("trigger", "durable"))).add(literal("connection.setClientID(configuration().")).add(mark("channel", "firstLowerCase")).add(literal("Configuration.clientID")).add(expression().add(mark("custom", "replace").multiple(""))).add(literal(");")),
			rule().add((condition("trigger", "replace"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration().")).add(mark("channel", "firstLowerCase")).add(literal("Configuration.")).add(mark("value", "validname", "firstLowerCase")).add(literal(")")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "jmx"))).add(literal("this.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new JMX")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("().init(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port, this);")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "rest"))).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Resources.setup(io.intino.pandora.server.activity.ActivityPandoraSpark.instance(), this);")),
			rule().add((condition("type", "activity")), (condition("trigger", "activity"))).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Activity.init(io.intino.pandora.server.activity.ActivityPandoraSpark.instance(), this);")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "jms"))).add(literal("try {\n\tconnection = new org.apache.activemq.ActiveMQConnectionFactory(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.user, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.password, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.url).createConnection();\n\tconnection.start();\n\tthis.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("JMSService(connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE), this);\n} catch (javax.jms.JMSException e) {\n\tLOG.severe(e.getMessage());\n}")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "field"))).add(literal("private io.intino.pandora.jmx.JMXServer ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "field"))).add(literal("private ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("JMSService ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("JMSService ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "getter"))),
			rule().add((condition("type", "service"))),
			rule().add((condition("type", "process")), (condition("trigger", "init"))).add(literal("//TODO")),
			rule().add((condition("trigger", "import"))),
			rule().add((condition("trigger", "parentInit")))
		);
		return this;
	}
}