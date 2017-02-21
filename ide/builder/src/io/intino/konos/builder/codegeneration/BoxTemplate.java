package io.intino.konos.builder.codegeneration;

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
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.logging.Logger;\nimport java.util.Map;\nimport java.util.UUID;\n")).add(expression().add(mark("hasREST")).add(literal("import io.intino.konos.server.KonosSpark;"))).add(literal("\n")).add(expression().add(literal("import io.intino.tara.magritte.Graph;")).add(mark("tara", "import"))).add(literal("\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box extends ")).add(expression().add(mark("parent")).add(literal("Box")).or(expression().add(literal("io.intino.konos.Box")))).add(literal(" {\n\tprivate static Logger LOG = Logger.getGlobal();\n\t")).add(expression().add(mark("hasParent")).or(expression().add(literal("protected ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration;")))).add(literal("\n\t")).add(expression().add(mark("hasParent")).or(expression().add(literal("protected Map<String, Object> map = new LinkedHashMap<>();")))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("service", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("bus", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("task", "field"))).add(literal("\n\n\t")).add(expression().add(mark("tara", "hide")).add(literal("private String graphID;"))).add(literal("\n\n\tpublic ")).add(expression().add(mark("tara", "SnakeCaseToCamelCase", "FirstUpperCase")).or(expression().add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")))).add(literal("Box(")).add(expression().add(mark("tara", "hide")).add(literal("io.intino.tara.magritte.Graph graph, "))).add(expression().add(mark("tara", "SnakeCaseToCamelCase", "FirstUpperCase")).or(expression().add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")))).add(literal("Configuration configuration) {\n\t\t")).add(expression().add(mark("hasParent")).add(literal("super(graph, configuration);")).add(literal("\n")).add(literal("\t\t"))).add(expression().add(mark("tara", "hide")).add(literal("box.put(graphID = UUID.randomUUID().toString(), graph);"))).add(literal("\n\t\tconfiguration.args().entrySet().forEach((e) -> box.put(e.getKey(), e.getValue()));\n\t\tthis.configuration = configuration;")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("service", "setup").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("activity", "setup").multiple("\n"))).add(literal("\n\t}\n\t")).add(expression().add(mark("tara", "hide")).add(literal("\n")).add(literal("\tpublic io.intino.tara.magritte.Graph graph() {")).add(literal("\n")).add(literal("\t\treturn (io.intino.tara.magritte.Graph) box().get(graphID);")).add(literal("\n")).add(literal("\t}")).add(literal("\n")).add(literal("\n")).add(literal("\tpublic void graph(io.intino.tara.magritte.Graph graph) {")).add(literal("\n")).add(literal("\t\tbox().put(graphID, graph);")).add(literal("\n")).add(literal("\t}"))).add(literal("\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration() {\n\t\treturn (")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration) configuration;\n\t}\n\t\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("service", "getter").multiple("\n\n"))).add(literal("\n\n\t")).add(mark("bus", "getter").multiple("\n\n")).add(literal("\n\n\t")).add(mark("task", "getter").multiple("\n\n")).add(literal("\n\n\tpublic void init() {")).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("parent", "parentInit")).add(literal("super.init();")).or(expression().add(literal(" ")))).add(literal("\n\t\tinitActivities();\n\t\tinitRESTServices();\n\t\tinitJMXServices();\n\t\tinitJMSServices();\n\t\tinitBuses();\n\t\tinitTasks();\n\t\tinitSlackBots();\n\t}\n\n\tprivate void initRESTServices() {\n\t\t")).add(mark("service", "rest").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMSServices() {\n\t\t")).add(expression().add(mark("jms")).add(literal("javax.jms.Connection connection;"))).add(literal("\n\t\t")).add(mark("service", "jms").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMXServices() {\n\t\t")).add(mark("service", "jmx").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initSlackBots() {\n\t\t")).add(mark("service", "slack").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initActivities() {\n\t\t")).add(mark("activity").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initBuses() {\n\t\t")).add(mark("bus", "init").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initTasks() {\n\t\t")).add(mark("task", "init").multiple("\n")).add(literal("\n\t}\n\n\tpublic void stopJMSServices() {\n\n\t}\n\n}")),
			rule().add((condition("type", "activity")), (condition("trigger", "setup"))).add(literal("io.intino.konos.server.activity.ActivityKonosSpark.setup(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.authService);")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "setup"))).add(literal("io.intino.konos.server.activity.ActivityKonosSpark.setup(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(", null);")),
			rule().add((condition("type", "bus")), (condition("trigger", "init"))).add(literal("this.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Bus = new ")).add(mark("package", "lowercase")).add(literal(".bus.")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Bus(this);")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "jmx"))).add(literal("this.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new JMX")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("().init(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.localhostIP, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port, this);")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "slack"))).add(literal("this.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("SlackBot(this);")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "rest"))).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Resources.setup(io.intino.konos.server.activity.ActivityKonosSpark.instance(), this);")),
			rule().add((condition("type", "activity")), (condition("trigger", "activity"))).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Activity.init(io.intino.konos.server.activity.ActivityKonosSpark.instance(), this);")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "jms"))).add(literal("try {\n\tconnection = new org.apache.activemq.ActiveMQConnectionFactory(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.user, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.password, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.url).createConnection();\n\tconnection.start();\n\tthis.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("JMSService(connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE), this);\n} catch (javax.jms.JMSException e) {\n\tLOG.severe(e.getMessage());\n}")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "field"))).add(literal("private io.intino.konos.jmx.JMXServer ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "field"))).add(literal("private io.intino.konos.slack.Bot ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "field"))).add(literal("private ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("JMSService ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "task")), (condition("trigger", "field"))).add(literal("private io.intino.konos.scheduling.KonosTasker tasker = new io.intino.konos.scheduling.KonosTasker();")),
			rule().add((condition("type", "bus")), (condition("trigger", "field"))).add(literal("private ")).add(mark("package")).add(literal(".bus.")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Bus ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Bus;")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("JMSService ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "bus")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("package")).add(literal(".bus.")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Bus ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("Bus() {\n\treturn ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("Bus;\n}")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("SlackBot ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn (")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("SlackBot) ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "getter"))),
			rule().add((condition("type", "service"))),
			rule().add((condition("type", "task")), (condition("trigger", "init"))).add(literal("Tasks.init(this.tasker, this);")),
			rule().add((condition("type", "task")), (condition("trigger", "getter"))).add(literal("public io.intino.konos.scheduling.KonosTasker tasker() {\n\treturn this.tasker;\n}")),
			rule().add((condition("trigger", "import"))),
			rule().add((condition("trigger", "parentInit"))),
			rule().add((condition("trigger", "hide")))
		);
		return this;
	}
}