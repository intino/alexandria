package io.intino.konos.builder.codegeneration;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class AbstractBoxTemplate extends Template {

	protected AbstractBoxTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new AbstractBoxTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "box"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;")).add(expression().add(mark("hasUi")).add(literal("\n")).add(literal("import java.util.HashMap;")).add(literal("\n")).add(literal("import java.util.Map;")).add(literal("\n"))).add(literal("\nimport io.intino.alexandria.logger.Logger;\nimport java.util.logging.ConsoleHandler;\nimport java.util.logging.Level;\n")).add(expression().add(literal("\n")).add(mark("hasREST")).add(literal("import io.intino.alexandria.rest.AlexandriaSpark;"))).add(expression().add(literal("\n")).add(mark("hasUi", "hide")).add(literal("import io.intino.alexandria.ui.displays.Soul;"))).add(literal("\n\npublic abstract class AbstractBox extends ")).add(expression().add(mark("hasUi", "uiBox")).or(expression().add(literal("io.intino.alexandria.core.Box")))).add(literal(" {\n\tprotected ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration;")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("service", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("dataLake", "field").multiple("\n"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("task", "field"))).add(expression().add(literal("\n")).add(literal("    ")).add(mark("hasUi", "hide")).add(literal("protected Map<String, Soul> uiSouls = new java.util.HashMap<>();"))).add(expression().add(literal("\n")).add(literal("    ")).add(mark("hasUi", "hide")).add(literal("private java.util.List<io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed> soulsClosedListeners = new java.util.ArrayList<>();"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("hasUi", "hide")).add(literal("private io.intino.alexandria.ui.services.AuthService authService;"))).add(expression().add(literal("\n")).add(literal("\t")).add(mark("hasUi", "hide")).add(literal("private io.intino.alexandria.ui.services.EditorService editorService;"))).add(literal("\n\n\tpublic AbstractBox(String[] args) {\n\t\tthis(new ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration(args));\n\t}\n\t\n\tpublic AbstractBox(")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration configuration) {\n\t\t")).add(expression().add(literal("owner = new ")).add(mark("parent")).add(literal("Box(configuration);"))).add(literal("\n\t\tthis.configuration = configuration;")).add(expression().add(literal("\n")).add(literal("\t\tinitJavaLogger();")).add(literal("\n")).add(literal("\t\t")).add(mark("service", "setup").multiple("\n"))).add(literal("\n\t\t")).add(mark("datalake", "setup")).add(literal("\n\t}\n\n\tpublic ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Configuration configuration() {\n\t\treturn configuration;\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\t")).add(expression().add(mark("hasParent")).add(literal("owner.put(o);"))).add(literal("\n\t\treturn this;\n\t}\n\n\tpublic io.intino.alexandria.core.Box open() {\n\t\tif (owner != null) owner.open();\n\t\tinitUI();\n\t\tinitRESTServices();\n\t\tinitJMXServices();\n\t\tinitJMSServices();\n\t\tinitDatalake();\n\t\tinitTasks();\n\t\tinitSlackBots();\n\t\treturn this;\n\t}\n\n\tpublic void close() {\n\t\tif(owner != null) owner.close();\n\t\t")).add(mark("spark").multiple("\n")).add(literal("\n\t\t")).add(mark("service", "quit").multiple("\n")).add(literal("\n\t\t")).add(mark("dataLake", "quit").multiple("\n")).add(literal("\n\t}\n\n\t")).add(expression().add(mark("hasUi", "registerSoul")).add(literal("\n")).add(literal("\t"))).add(expression().add(mark("service", "getter").multiple("\n\n"))).add(literal("\n\t")).add(expression().add(mark("hasUi", "authService"))).add(literal("\n\t")).add(expression().add(mark("hasUi", "editorService"))).add(literal("\n\t")).add(mark("dataLake", "getter").multiple("\n\n")).add(literal("\n\t")).add(mark("task", "getter").multiple("\n\n")).add(literal("\n\n\tprivate void initRESTServices() {\n\t\t")).add(mark("service", "rest").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMSServices() {\n\t\t")).add(expression().add(mark("jms")).add(literal("javax.jms.Connection connection;"))).add(literal("\n\t\t")).add(mark("service", "jms").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJMXServices() {\n\t\t")).add(mark("service", "jmx").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initSlackBots() {\n\t\t")).add(mark("service", "slack").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initUI() {\n\t\t")).add(mark("service", "ui").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initDatalake() {\n\t\t")).add(mark("dataLake", "init").multiple("\n")).add(literal("\n\t}\n\t")).add(expression().add(mark("datalake", "hide")).add(literal("\n")).add(literal("\tprivate io.intino.ness.core.Datalake datalakeFrom(String nessUrl, String nessUser, String nessPassword, String clientId) {")).add(literal("\n")).add(literal("\t\tif (nessUrl.startsWith(\"file://\")) return new io.intino.alexandria.nessaccessor.local.LocalDatalake(new java.io.File(nessUrl.replace(\"file://\", \"\")));")).add(literal("\n")).add(literal("\t\treturn new io.intino.alexandria.nessaccessor.tcp.TCPDatalake(nessUrl, nessUser, nessPassword, clientId);")).add(literal("\n")).add(literal("\t}")).add(literal("\n")).add(literal("\t"))).add(literal("\n\tprivate void initTasks() {\n\t\t")).add(mark("task", "init").multiple("\n")).add(literal("\n\t}\n\n\tprivate void initJavaLogger() {\n\t\tfinal java.util.logging.Logger Logger = java.util.logging.Logger.getGlobal();\n\t\tfinal ConsoleHandler handler = new ConsoleHandler();\n\t\thandler.setLevel(Level.INFO);\n\t\thandler.setFormatter(new io.intino.alexandria.logger.Formatter());\n\t\tLogger.setUseParentHandlers(false);\n\t\tLogger.addHandler(handler);\n\t}\n\n\tprivate static java.net.URL url(String url) {\n\t\ttry {\n\t\treturn new java.net.URL(url);\n\t\t} catch (java.net.MalformedURLException e) {\n\t\t\treturn null;\n\t\t}\n\t}\n}")),
			rule().add((condition("trigger", "uiBox"))).add(literal("io.intino.alexandria.ui.AlexandriaUiBox")),
			rule().add((condition("type", "service & ui")), (condition("trigger", "setup"))).add(literal("this.authService = ")).add(expression().add(literal("this.authService(")).add(mark("authentication")).add(literal(")")).or(expression().add(literal("null")))).add(literal(";\nthis.editorService = ")).add(expression().add(literal("this.editorService(")).add(mark("edition")).add(literal(")")).or(expression().add(literal("null")))).add(literal(";\n")).add(expression().add(literal("if(")).add(mark("parameter")).add(literal(" != null && !")).add(mark("parameter")).add(literal(".isEmpty())"))).add(expression().add(literal("io.intino.alexandria.rest.AlexandriaSparkBuilder.setup(Integer.parseInt(")).add(mark("parameter")).add(literal("), \"www/\")"))).add(literal(";\nio.intino.alexandria.rest.AlexandriaSparkBuilder.setUI(true);\nio.intino.alexandria.rest.AlexandriaSparkBuilder.addParameters(this.authService, this.editorService);")),
			rule().add((condition("type", "service & rest")), (condition("trigger", "setup"))).add(literal("io.intino.alexandria.rest.AlexandriaSparkBuilder.setup(Integer.parseInt(")).add(mark("parameter")).add(literal("), \"www/\");")),
				rule().add((condition("type", "datalake")), (condition("trigger", "setup"))).add(literal("this.nessAccessor = new io.intino.alexandria.nessaccessor.NessAccessor(datalakeFrom(")).add(mark("parameter").multiple(", ")).add(literal("));")),
				rule().add((condition("type", "service & jmx")), (condition("trigger", "jmx"))).add(literal("this.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new JMX")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("().init(((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this));\nLogger.info(\"JMX service ")).add(mark("name")).add(literal(": started!\");")),
				rule().add((condition("type", "service & slack")), (condition("trigger", "slack"))).add(literal("if (")).add(mark("parameter")).add(literal(" == null || ")).add(mark("parameter")).add(literal(".isEmpty()) return;\nthis.")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("SlackBot((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this, ")).add(mark("parameter")).add(literal(");\nLogger.info(\"Slack service ")).add(mark("name")).add(literal(": started!\");")),
				rule().add((condition("type", "service & rest")), (condition("trigger", "rest"))).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Service.setup(io.intino.alexandria.rest.AlexandriaSparkBuilder.instance(), (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this).start();\nLogger.info(\"REST service ")).add(mark("name")).add(literal(": started!\");")),
				rule().add((condition("type", "service & ui")), (condition("trigger", "ui"))).add(expression().add(literal("if (")).add(mark("parameter")).add(literal(" == null || !io.intino.alexandria.rest.AlexandriaSparkBuilder.isUI()) return;"))).add(literal("\nio.intino.alexandria.ui.UIAlexandriaSpark sparkInstance = (io.intino.alexandria.ui.UIAlexandriaSpark) io.intino.alexandria.rest.AlexandriaSparkBuilder.instance();\n")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Service.init(sparkInstance, (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\nio.intino.alexandria.ui.UiElementsService.initDisplays(sparkInstance);\n")).add(mark("use").multiple("\n")).add(literal("\nsparkInstance.start();\nLogger.info(\"UI ")).add(mark("name")).add(literal(": started!\");")),
			rule().add((condition("trigger", "use"))).add(mark("value")).add(literal(".initDisplays(sparkInstance);")),
				rule().add((condition("type", "service & jms")), (condition("trigger", "jms"))).add(literal("try {\n\tconnection = new org.apache.activemq.ActiveMQConnectionFactory(")).add(mark("parameter").multiple(", ")).add(literal(").createConnection();\n\tjava.lang.Thread thread = new java.lang.Thread(() -> {\n\t\ttry {\n\t\t\tconnection.start();\n\t\t\tthis.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = new ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Service(connection, (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\n\t\t\tLogger.info(\"JMS service ")).add(mark("name")).add(literal(": started!\");\n\t\t} catch (javax.jms.JMSException e) {\n\t\t\tLogger.error(e.getMessage());\n\t\t}\n\t}, \"jms init\");\n\tthread.start();\n\tthread.join(10000);\n} catch (javax.jms.JMSException | InterruptedException e) {\n\tLogger.error(e.getMessage());\n}")),
				rule().add((condition("type", "dataLake & requireConnection")), (condition("trigger", "init"))).add(literal("this.nessAccessor.connection().connect(\"")).add(mark("mode")).add(literal("\");\n")).add(mark("package", "validPackage")).add(literal(".datalake.Datalake.registerTanks((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\nregisterFeeders();\n")).add(mark("nessOperations")).add(literal("\nif (nessAccessor.connection() instanceof io.intino.alexandria.nessaccessor.tcp.TCPDatalake.Connection && ((io.intino.alexandria.nessaccessor.tcp.TCPDatalake.Connection) nessAccessor.connection()).session() != null)\n\tLogger.info(\"Ness connection: started!\");")),
				rule().add((condition("type", "dataLake")), (condition("trigger", "init"))).add(literal("java.lang.Thread thread = new java.lang.Thread(() -> {\n\tthis.nessAccessor.connection().connect(\"")).add(mark("mode")).add(literal("\");\n\t")).add(mark("package", "validPackage")).add(literal(".datalake.Datalake.registerTanks((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);\n\tregisterFeeders();\n\t")).add(mark("nessOperations")).add(literal("\n\tif (nessAccessor.connection() instanceof io.intino.alexandria.nessaccessor.tcp.TCPDatalake.Connection && ((io.intino.alexandria.nessaccessor.tcp.TCPDatalake.Connection) nessAccessor.connection()).session() != null)\n\t\tLogger.info(\"Ness connection: started!\");\n}, \"ness accessor init\");\ntry {\n\tthread.start();\n\tthread.join(10000);\n} catch (InterruptedException e) {\n\tLogger.error(e.getMessage());\n}")),
			rule().add((condition("trigger", "nessOperations"))).add(mark("package", "validPackage")).add(literal(".datalake.NessOperations.init((")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);")),
			rule().add((condition("type", "custom")), (condition("trigger", "parameter"))).add(literal("configuration().get(\"")).add(mark("value", "customParameter")).add(literal("\")")),
			rule().add((condition("type", "custom")), (condition("trigger", "authentication"))).add(literal("url(configuration().get(\"")).add(mark("value", "customParameter")).add(literal("\"))")),
			rule().add((condition("type", "custom")), (condition("trigger", "edition"))).add(literal("url(configuration().get(\"")).add(mark("value", "customParameter")).add(literal("\"))")),
			rule().add((condition("trigger", "parameter"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("trigger", "authentication"))).add(literal("url(\"")).add(mark("value")).add(literal("\")")),
			rule().add((condition("trigger", "edition"))).add(literal("url(\"")).add(mark("value")).add(literal("\")")),
			rule().add((condition("attribute", "Transacted")), (condition("trigger", "mode"))).add(literal("Transacted")),
			rule().add((condition("trigger", "mode"))),
				rule().add((condition("type", "service & jmx")), (condition("trigger", "field"))).add(literal("private io.intino.alexandria.jmx.JMXServer ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
				rule().add((condition("type", "service & slack")), (condition("trigger", "field"))).add(literal("private io.intino.alexandria.slack.Bot ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "field"))).add(literal("private ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Service ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";")),
				rule().add((condition("type", "task")), (condition("trigger", "field"))).add(literal("private io.intino.alexandria.scheduler.AlexandriaScheduler scheduler = new io.intino.alexandria.scheduler.AlexandriaScheduler();")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "field"))).add(literal("private io.intino.alexandria.nessaccessor.NessAccessor nessAccessor;")),
			rule().add((condition("type", "service & jms")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("Service ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "getter"))).add(literal("public io.intino.alexandria.nessaccessor.NessAccessor nessAccessor() {\n\treturn this.nessAccessor;\n}\n\npublic io.intino.alexandria.core.Feeders feeders() {\n\treturn io.intino.alexandria.core.Feeders.get();\n}\n\npublic void registerFeeders() {\n\t")).add(mark("feeder").multiple("\n")).add(literal("\n}")),
			rule().add((condition("trigger", "feeder"))).add(literal("io.intino.alexandria.core.Feeders.get().register(new ")).add(mark("package", "validPackage")).add(literal(".datalake.feeders.")).add(mark("name", "FirstUpperCase")).add(literal("((")).add(mark("box", "FirstUpperCase")).add(literal("Box)this));")),
			rule().add((condition("type", "service & slack")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("SlackBot ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal("() {\n\treturn (")).add(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).add(literal("SlackBot) ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "service")), (condition("trigger", "getter"))),
			rule().add((condition("trigger", "spark"))).add(literal("io.intino.alexandria.rest.AlexandriaSparkBuilder.instance().stop();")),
			rule().add((condition("type", "service")), (condition("type", "jms")), (condition("trigger", "quit"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(" != null) ")).add(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).add(literal(".closeSession();")),
			rule().add((condition("type", "dataLake")), (condition("trigger", "quit"))).add(literal("if (nessAccessor != null) nessAccessor.connection().disconnect();")),
			rule().add((condition("type", "service")), (condition("trigger", "quit"))),
			rule().add((condition("type", "service"))),
				rule().add((condition("type", "task")), (condition("trigger", "init"))).add(literal("Tasks.init(this.scheduler, (")).add(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box) this);")),
				rule().add((condition("type", "task")), (condition("trigger", "getter"))).add(literal("public io.intino.alexandria.scheduler.AlexandriaScheduler scheduler() {\n\treturn this.scheduler;\n}")),
			rule().add((condition("trigger", "authservice"))).add(literal("protected abstract io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl);")),
			rule().add((condition("trigger", "editorService"))).add(literal("protected abstract io.intino.alexandria.ui.services.EditorService editorService(java.net.URL editorServiceUrl);\n\npublic io.intino.alexandria.ui.services.EditorService editorService() {\n\treturn this.editorService;\n}")),
			rule().add((condition("trigger", "registerSoul"))).add(literal("public java.util.List<Soul> souls() {\n\treturn new java.util.ArrayList<>(uiSouls.values());\n}\n\n\tpublic java.util.Optional<Soul> soul(String clientId) {\n\t\treturn java.util.Optional.ofNullable(uiSouls.get(clientId));\n\t}\n\n\tpublic void registerSoul(String clientId, Soul soul) {\n\t\t")).add(expression().add(literal("if (owner != null) ((")).add(mark("parent")).add(literal("Box) owner).registerSoul(clientId, soul);"))).add(literal("\n\t\tuiSouls.put(clientId, soul);\n\t}\n\n\tpublic void unRegisterSoul(String clientId) {\n\t\t")).add(expression().add(literal("if (owner != null) ((")).add(mark("parent")).add(literal("Box) owner).unRegisterSoul(clientId);"))).add(literal("\n\t\tuiSouls.remove(clientId);\n\t\tif (uiSouls.size() <= 0) notifySoulsClosed();\n\t}\n\n\tpublic void onSoulsClosed(io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed listener) {\n\t\t")).add(expression().add(literal("if (owner != null) ((")).add(mark("parent")).add(literal("Box) owner).onSoulsClosed(listener);"))).add(literal("\n\t\tthis.soulsClosedListeners.add(listener);\n\t}\n\n\tprivate void notifySoulsClosed() {\n\t\tsoulsClosedListeners.forEach(l -> l.accept());\n\t}")),
			rule().add((condition("trigger", "import"))),
			rule().add((condition("trigger", "parentInit"))),
			rule().add((condition("trigger", "hide")))
		);
		return this;
	}
}