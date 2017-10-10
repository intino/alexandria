package io.intino.konos.builder.codegeneration;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class AbstractBoxTemplate extends Template {

	protected AbstractBoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractBoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;")).add(expression().add(mark("hasActivity")).add(literal("\n")).add(literal("import java.util.HashMap;")).add(literal("\n")).add(literal("import java.util.Map;")).add(literal("\n"))).add(literal("\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\nimport java.util.logging.ConsoleHandler;\nimport java.util.logging.Level;\nimport static org.slf4j.Logger.ROOT_LOGGER_NAME;\n")).add(expression().add(literal("\n")).add(mark("hasREST")).add(literal("import io.intino.konos.server.KonosSpark;"))).add(expression().add(literal("\n")).add(mark("hasActivity", "hide")).add(literal("import io.intino.konos.server.activity.displays.Soul;"))).add(literal("\n\npublic abstract class AbstractBox extends io.intino.konos.Box {\n\tprivate static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);\n\tprotected ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration;")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("service", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("dataLake", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("task", "field"))).add(expression().add(literal("\n")).add(literal("    ")).add(mark("hasActivity", "hide")).add(literal("protected Map<String, Soul> activitySouls = new java.util.HashMap<>();"))).add(literal("\n\n\tpublic AbstractBox(String[] args) {\n\t\tthis(new ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration(args));\n\t}\n\t\n\tpublic AbstractBox(")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(expression().add(literal("owner = new ")).add(mark("parent")).add(literal("Box(configuration);"))).add(literal("\n\t\tthis.configuration = configuration;")).add(expression().add(literal("\n")).add(literal("\t\tinitLogger();")).add(literal("\n")).add(literal("\t\t")).add(mark("service", "setup").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t\t")).add(mark("activity", "setup").multiple("\n"))).add(literal("\n\t}\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration() {\n\t\treturn (")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration) configuration;\n\t}\n\n\t@Override\n\tpublic io.intino.konos.Box put(Object o) {\n\t\t")).add(expression().add(mark("hasParent")).add(literal("owner.put(o);"))).add(literal("\n\t\treturn this;\n\t}\n\n\tpublic io.intino.konos.Box open() {\n\t\tif(owner != null) owner.open();\n\t\tinitActivities();\n\t\tinitRESTServices();\n\t\tinitJMXServices();\n\t\tinitJMSServices();\n\t\tinitDataLake();\n\t\tinitTasks();\n\t\tinitSlackBots();\n\t\treturn this;\n\t}\n\n\tpublic void close() {\n\t\tif(owner != null) owner.close();\n\t\t")).add(mark("spark").multiple("\n")).add(literal("\n\t\t")).add(mark("service", "quit").multiple("\n")).add(literal("\n\t\t")).add(mark("dataLake", "quit").multiple("\n")).add(literal("\n\t}\n\n\t")).add(expression().add(mark("hasActivity", "registerSoul")).add(literal("\n")).add(literal("\t"))).add(expression().add(mark("service", "getter").multiple("\n\n"))).add(literal("\n\n\t")).add(mark("dataLake", "getter").multiple("\n\n")).add(literal("\n\n\t")).add(mark("task", "getter").multiple("\n\n")).add(literal("\n\n\tprivate void initRESTServices() {\n\t\t")).add(mark("service", "rest").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMSServices() {\n\t\t")).add(expression().add(mark("jms")).add(literal("javax.jms.Connection connection;"))).add(literal("\n\t\t")).add(mark("service", "jms").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMXServices() {\n\t\t")).add(mark("service", "jmx").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initSlackBots() {\n\t\t")).add(mark("service", "slack").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initActivities() {\n\t\t")).add(mark("activity").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initDataLake() {\n\t\t")).add(mark("dataLake", "init").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initTasks() {\n\t\t")).add(mark("task", "init").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initLogger() {\n\t\tfinal java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();\n\t\tfinal ConsoleHandler handler = new ConsoleHandler();\n\t\thandler.setLevel(Level.INFO);\n\t\thandler.setFormatter(new io.intino.konos.LogFormatter(\"log\"));\n\t\tlogger.addHandler(handler);\n\t}\n}")),
			rule().add((condition("type", "activity")), (condition("trigger", "setup"))).add(literal("if (configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration != null)\n\tio.intino.konos.server.activity.ActivityKonosSpark.setup(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.authService);")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "setup"))).add(literal("if (configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration != null)\n\tio.intino.konos.server.activity.ActivityKonosSpark.setup(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.port")).add(expression().add(literal(", configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.webDirectory"))).add(literal(", null);")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "jmx"))).add(literal("this.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new JMX")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("().init(((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this));\nlogger.info(\"JMX service ")).add(mark("name")).add(literal(": started!\");")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "slack"))).add(literal("if (configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration == null) return;\nthis.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("SlackBot((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\nlogger.info(\"Slack service ")).add(mark("name")).add(literal(": started!\");")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "rest"))).add(literal("if (configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration == null) return;\n")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Resources.setup(io.intino.konos.server.activity.ActivityKonosSpark.instance(), (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this).start();\nlogger.info(\"REST service ")).add(mark("name")).add(literal(": started!\");")),
			rule().add((condition("type", "activity")), (condition("trigger", "activity"))).add(literal("if (configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration == null) return;\n")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Activity.init(io.intino.konos.server.activity.ActivityKonosSpark.instance(), (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this).start();\nlogger.info(\"Activity ")).add(mark("name")).add(literal(": started!\");")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "jms"))).add(literal("if (configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration == null) return;\ntry {\n\tconnection = new org.apache.activemq.ActiveMQConnectionFactory(configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.user, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.password, configuration().")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("Configuration.url).createConnection();\n\tjava.lang.Thread thread = new java.lang.Thread(() -> {\n\t\ttry {\n\t\t\tconnection.start();\n\t\t\tthis.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Service(connection, (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\n\t\t\tlogger.info(\"JMS service ")).add(mark("name")).add(literal(": started!\");\n\t\t} catch (javax.jms.JMSException e) {\n\t\t\tlogger.error(e.getMessage());\n\t\t}\n\t}, \"jms init\");\n\tthread.start();\n\tthread.join(10000);\n} catch (javax.jms.JMSException | InterruptedException e) {\n\tlogger.error(e.getMessage());\n}")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "init"))).add(literal("if (configuration().nessConfiguration() == null) return;\nthis.ness = new io.intino.konos.datalake.Ness(configuration().nessConfiguration().url, configuration().nessConfiguration().user, configuration().nessConfiguration().password, configuration().nessConfiguration().clientID);\njava.lang.Thread thread = new java.lang.Thread(()-> {\n\tthis.ness.start();\n\t")).add(mark("package")).add(literal(".ness.NessTanks.registerTanks((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\n\t")).add(mark("package")).add(literal(".ness.NessOperations.init((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\n\tlogger.info(\"Ness datalake: started!\");\n}, \"ness init\");\ntry {\n\tthread.start();\n\tthread.join(10000);\n} catch (InterruptedException e) {\n\tlogger.error(e.getMessage());\n}")),
			rule().add((condition("type", "service & jmx")), (condition("trigger", "field"))).add(literal("private io.intino.konos.jmx.JMXServer ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "field"))).add(literal("private io.intino.konos.slack.Bot ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "field"))).add(literal("private ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Service ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "task")), (condition("trigger", "field"))).add(literal("private io.intino.konos.scheduling.KonosTasker tasker = new io.intino.konos.scheduling.KonosTasker();")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "field"))).add(literal("private io.intino.konos.datalake.Ness ness;")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Service ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "getter"))).add(literal("public io.intino.konos.datalake.Ness datalake() {\n\treturn this.ness;\n}")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("SlackBot ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn (")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("SlackBot) ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "getter"))),
			rule().add((condition("trigger", "spark"))).add(literal("io.intino.konos.server.activity.ActivityKonosSpark.instance().stop();")),
			rule().add((condition("type", "service")), (condition("type", "jms")), (condition("trigger", "quit"))).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(".closeSession();")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "quit"))).add(literal("if (ness != null) ness.stop();")),
			rule().add((condition("type", "service")), (condition("trigger", "quit"))),
			rule().add((condition("type", "service"))),
			rule().add((condition("type", "task")), (condition("trigger", "init"))).add(literal("Tasks.init(this.tasker, (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);")),
			rule().add((condition("type", "task")), (condition("trigger", "getter"))).add(literal("public io.intino.konos.scheduling.KonosTasker tasker() {\n\treturn this.tasker;\n}")),
			rule().add((condition("trigger", "registerSoul"))).add(literal("public void registerSoul(String clientId, Soul soul) {\n\t")).add(expression().add(literal("if(owner != null) ((")).add(mark("parent")).add(literal("Box) owner).registerSoul(clientId, soul);"))).add(literal("\n\tactivitySouls.put(clientId, soul);\n}\n\npublic void unRegisterSoul(String clientId) {\n\t")).add(expression().add(literal("if(owner != null) ((")).add(mark("parent")).add(literal("Box) owner).unRegisterSoul(clientId);"))).add(literal("\n\tactivitySouls.remove(clientId);\n}\n")),
			rule().add((condition("trigger", "import"))),
			rule().add((condition("trigger", "parentInit"))),
			rule().add((condition("trigger", "hide")))
		);
		return this;
	}
}