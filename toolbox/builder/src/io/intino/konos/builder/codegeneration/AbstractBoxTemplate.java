package io.intino.konos.builder.codegeneration;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractBoxTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("box"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).output(expression().output(literal("\n")).output(literal("import java.util.HashMap;")).output(literal("\n")).output(literal("import java.util.Map")).output(mark("hasUi", "hideUi"))).output(literal("\n\nimport io.intino.alexandria.logger.Logger;\nimport java.util.logging.ConsoleHandler;\nimport java.util.logging.Level;\n\n")).output(expression().output(mark("messagehub", "import"))).output(literal("\n")).output(expression().output(mark("terminal", "import"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.http.AlexandriaSpark;")).output(mark("hasREST"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.ui.services.push.PushService")).output(mark("hasUI", "hideUi"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.ui.Soul")).output(mark("hasUi", "hideUi"))).output(literal("\n\npublic abstract class AbstractBox extends ")).output(expression().output(mark("hasUi", "uiBox")).next(expression().output(literal("io.intino.alexandria.core.Box")))).output(literal(" {\n\tprotected ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration configuration;\n\t")).output(expression().output(mark("service", "field").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("connector", "field"))).output(literal("\n\t")).output(expression().output(mark("terminal", "field"))).output(literal("\n\t")).output(expression().output(mark("sentinel", "field"))).output(literal("\n\t")).output(expression().output(mark("workflow", "field"))).output(literal("\n\t")).output(expression().output(literal("protected Map<String, Soul> uiSouls = new java.util.HashMap<>()")).output(mark("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private java.util.List<io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed> soulsClosedListeners = new java.util.ArrayList<>()")).output(mark("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private io.intino.alexandria.ui.services.AuthService authService")).output(mark("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private PushService pushService")).output(mark("hasUi", "hideUi"))).output(literal("\n\n\tpublic AbstractBox(String[] args) {\n\t\tthis(new ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration(args));\n\t}\n\n\tpublic AbstractBox(")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration configuration) {\n\t\t")).output(expression().output(literal("owner = new ")).output(mark("parent")).output(literal("Box(configuration);"))).output(literal("\n\t\tthis.configuration = configuration;\n\t\t")).output(expression().output(mark("parent", "empty")).next(expression().output(literal("initJavaLogger();")))).output(literal("\n\t\t")).output(expression().output(mark("connector", "setup"))).output(literal("\n\t\t")).output(expression().output(mark("service", "setup").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("terminal", "setup"))).output(literal("\n\t}\n\n\tpublic ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Configuration configuration() {\n\t\treturn configuration;\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\t")).output(expression().output(mark("hasParent")).output(literal("owner.put(o);"))).output(literal("\n\t\treturn this;\n\t}\n\n\tpublic abstract void beforeStart();\n\n\tpublic io.intino.alexandria.core.Box start() {\n\t\tinitConnector();\n\t\tif (owner != null) owner.beforeStart();\n\t\tbeforeStart();\n\t\tif (owner != null) owner.startServices();\n\t\tstartServices();\n\t\tif (owner != null) owner.afterStart();\n\t\tafterStart();\n\t\treturn this;\n\t}\n\n\tpublic abstract void afterStart();\n\n\tpublic abstract void beforeStop();\n\n\tpublic void stop() {\n\t\tif (owner != null) owner.beforeStop();\n\t\tbeforeStop();\n\t\tif (owner != null) owner.stopServices();\n\t\tstopServices();\n\t\tif (owner != null) owner.afterStop();\n\t\tafterStop();\n\t}\n\n\t@Override\n\tpublic void stopServices() {\n\t\t")).output(expression().output(mark("spark").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("service", "stop").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("connector", "stop"))).output(literal("\n\t}\n\n\tpublic abstract void afterStop();\n\n\t@Override\n\tpublic void startServices() {\n\t\tinitUI();\n\t\tinitAgenda();\n\t\tinitRestServices();\n\t\tinitSoapServices();\n\t\tinitJmxServices();\n\t\tinitTerminal();\n\t\tinitMessagingServices();\n\t\tinitSentinels();\n\t\tinitSlackBots();\n\t\tinitWorkflow();\n\t\tinitCli();\n\t}\n\n\t")).output(expression().output(mark("hasUi", "pushService"))).output(literal("\n\n\t")).output(expression().output(mark("hasUi", "registerSoul"))).output(literal("\n\n\t")).output(expression().output(mark("service", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("hasUi", "authService"))).output(literal("\n\n\t")).output(expression().output(mark("terminal", "getter"))).output(literal("\n\n\t")).output(expression().output(mark("sentinel", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("workflow", "getter"))).output(literal("\n\n\t")).output(mark("service", "setupMethod").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(mark("terminal", "datamartSourceSelector"))).output(literal("\n\n\tprivate void initRestServices() {\n\t\t")).output(mark("service", "rest").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initSoapServices() {\n\t\t")).output(mark("service", "soap").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initMessagingServices() {\n\t\t")).output(mark("service", "messaging").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initJmxServices() {\n\t\t")).output(mark("service", "jmx").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initSlackBots() {\n\t\t")).output(mark("service", "slack").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initUI() {\n\t\t")).output(mark("service", "ui").multiple("\n")).output(literal("\n\t}\n\n\t")).output(expression().output(mark("hasUi", "translatorServiceInitialization"))).output(literal("\n\n\tprotected void initConnector() {\n\t\t")).output(expression().output(mark("connector", "init"))).output(literal("\n\t}\n\n\tprotected void initTerminal() {\n\t\t")).output(expression().output(mark("terminal", "init"))).output(literal("\n\t}\n\n\tprotected void initSentinels() {\n\t\t")).output(expression().output(mark("sentinel", "init").multiple("\n"))).output(literal("\n\t}\n\n\tprotected void initWorkflow() {\n\t\t")).output(expression().output(mark("workflow", "init"))).output(literal("\n\t}\n\n\tprotected void initAgenda() {\n\t\t")).output(mark("service", "agenda").multiple("\n")).output(literal("\n\t}\n\n\tprotected void initCli() {\n\t\t")).output(mark("service", "cli").multiple("\n")).output(literal("\n\t}\n\n\tprotected void initJavaLogger() {\n\t\tfinal java.util.logging.Logger Logger = java.util.logging.Logger.getGlobal();\n\t\tfinal ConsoleHandler handler = new ConsoleHandler();\n\t\thandler.setLevel(Level.INFO);\n\t\thandler.setFormatter(new io.intino.alexandria.logger.Formatter());\n\t\tLogger.setUseParentHandlers(false);\n\t\tLogger.addHandler(handler);\n\t\t")).output(mark("logger")).output(literal("\n\t}\n\n\tprotected java.net.URL url(String url) {\n\t\ttry {\n\t\t\treturn new java.net.URL(url);\n\t\t} catch (java.net.MalformedURLException e) {\n\t\t\treturn null;\n\t\t}\n\t}\n}")),
			rule().condition((trigger("empty"))).output(literal(";")),
			rule().condition((type("logger")), (trigger("logger"))).output(literal("io.intino.alexandria.logger4j.Logger.init();")),
			rule().condition((type("connector")), (trigger("import"))).output(literal("import io.intino.alexandria.terminal.Connector;")),
			rule().condition((trigger("uibox"))).output(literal("io.intino.alexandria.ui.AlexandriaUiBox")),
			rule().condition((allTypes("service","ui")), (trigger("setupmethod"))).output(literal("public void setupUi() {\n\t")).output(expression().output(literal("if(")).output(mark("parameter")).output(literal(" == null || ")).output(mark("parameter")).output(literal(".isEmpty())"))).output(literal(" return;\n\tthis.authService = ")).output(expression().output(literal("this.authService(")).output(mark("authentication")).output(literal(")")).next(expression().output(literal("null")))).output(literal(";\n\t")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(mark("parameter")).output(literal("), \"www/\")"))).output(literal(";\n\tio.intino.alexandria.http.AlexandriaSparkBuilder.setUI(true);\n\tio.intino.alexandria.http.AlexandriaSparkBuilder.addParameters(this.authService);\n\tthis.pushService = new io.intino.alexandria.ui.services.push.PushService();\n\tio.intino.alexandria.ui.UISpark sparkInstance = (io.intino.alexandria.ui.UISpark) io.intino.alexandria.http.AlexandriaSparkBuilder.instance();\n\t")).output(mark("package", "validPackage")).output(literal(".ui.")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Service.init(sparkInstance, (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this, pushService, new ")).output(mark("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher());\n\t")).output(mark("use").multiple("\n")).output(literal("\n\tio.intino.alexandria.ui.UiElementsService.initDisplays(sparkInstance, pushService);\n}")),
			rule().condition((allTypes("service","cli")), (trigger("setupmethod"))).output(literal("public void setupCli() {\n\t")).output(expression().output(literal("if(")).output(mark("parameter")).output(literal(" == null || ")).output(mark("parameter")).output(literal(".isEmpty())"))).output(literal(" return;\n\t")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(mark("parameter")).output(literal("), \"www/\")"))).output(literal(";\n}")),
			rule().condition((allTypes("service","soap")), (trigger("setup"))).output(expression().output(literal("if(")).output(mark("parameter")).output(literal(" != null && !")).output(mark("parameter")).output(literal(".isEmpty()) "))).output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(mark("parameter")).output(literal("), \"www/\");")),
			rule().condition((type("terminal")), (trigger("setup"))).output(literal("this.terminal = new ")).output(mark("qn")).output(literal("(connector);")),
			rule().condition((type("connector")), (trigger("setup"))).output(literal("this.connector = io.intino.alexandria.terminal.ConnectorFactory.createConnector(new io.intino.alexandria.jms.ConnectionConfig(")).output(mark("parameter").multiple(", ")).output(literal("), ")).output(mark("additionalParameter").multiple(",")).output(literal(");")),
			rule().condition((allTypes("service","jmx")), (trigger("jmx"))).output(literal("this.")).output(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).output(literal(" = new JMX")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("().init(((")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this));\nLogger.info(\"Jmx service ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((allTypes("service","slack")), (trigger("slack"))).output(literal("if (")).output(mark("parameter")).output(literal(" == null || ")).output(mark("parameter")).output(literal(".isEmpty()) return;\nthis.")).output(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).output(literal(" = new ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("SlackBot((")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this, ")).output(mark("parameter")).output(literal(");\nLogger.info(\"Slack service ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((allTypes("service","rest")), (trigger("rest"))).output(expression().output(literal("if(")).output(mark("parameter")).output(literal(" == null || ")).output(mark("parameter")).output(literal(".isEmpty()) return;"))).output(literal("\nio.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(mark("parameter")).output(literal("), \"www/\");\n")).output(expression().output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Service.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);"))).output(literal("\n")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();"))).output(literal("\nLogger.info(\"Rest service ")).output(mark("name")).output(literal(": started at port \" + ")).output(mark("parameter")).output(literal(" + \"!\");")),
			rule().condition((allTypes("service","soap")), (trigger("soap"))).output(expression().output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Service.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);"))).output(literal("\n")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();")).output(literal("\n")).output(literal("Logger.info(\"Soap service ")).output(mark("name")).output(literal(": started!\");"))),
			rule().condition((allTypes("service","ui")), (trigger("ui"))).output(literal("setupUi();\n")).output(expression().output(literal("this.initTranslatorService()"))).output(literal(";\nio.intino.alexandria.ui.UISpark sparkInstance = (io.intino.alexandria.ui.UISpark) io.intino.alexandria.http.AlexandriaSparkBuilder.instance();\nsparkInstance.start();\nLogger.info(\"UI ")).output(mark("name")).output(literal(": started at port \" + ")).output(mark("parameter")).output(literal(" + \"!\");")),
			rule().condition((trigger("use"))).output(expression().output(mark("")).output(literal(".initDisplays(sparkInstance, pushService);"))),
			rule().condition((allTypes("service","messaging")), (trigger("messaging"))).output(literal("this.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service(this.connector, (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);\nLogger.info(\"Messaging service ")).output(mark("name")).output(literal(": started!\");")),
				rule().condition((allTypes("service", "agenda")), (trigger("agenda"))).output(literal("if (this.agenda != null) return;\nthis.agenda = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service((")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);\nthis.agenda.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), scheduler);\nio.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();\nLogger.info(\"Agenda service: started!\");")),
			rule().condition((allTypes("service","cli")), (trigger("cli"))).output(literal("setupCli();\n")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal("Service = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service();\n")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal("Service.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this, new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("((")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box)this));\nio.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();\nLogger.info(\"")).output(mark("name", "firstUpperCase")).output(literal(" service: started!\");")),
			rule().condition((type("sentinel")), (trigger("init"))).output(literal("Sentinels.init(this.scheduler, this.configuration.home() ")).output(expression().output(mark("hasWebhook")).output(literal(" io.intino.alexandria.http.AlexandriaSparkBuilder.instance()"))).output(literal(", (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);")),
			rule().condition((type("workflow")), (trigger("init"))).output(literal("this.workflow = new ")).output(mark("package")).output(literal(".bpm.Workflow((")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box)this")).output(expression().output(literal(", ")).output(mark("parameter"))).output(literal(");")),
			rule().condition((type("connector")), (trigger("init"))).output(literal("if (!(this.connector instanceof io.intino.alexandria.terminal.JmsConnector) || ((io.intino.alexandria.terminal.JmsConnector) this.connector).connection() != null) return;\nfinal io.intino.alexandria.terminal.JmsConnector connector = (io.intino.alexandria.terminal.JmsConnector) this.connector;\nif (connector.connection() == null) connector.start();\nif (configuration().get(\"datahub_url\") != null)\n\twhile (connector.connection() == null) try {\n\t\tThread.sleep(1000 * 30);\n\t\tconnector.start();\n\t} catch (InterruptedException e) {\n\t\tLogger.error(e);\n\t}")),
			rule().condition((type("terminal")), (trigger("init"))).output(literal("if (this.terminal != null) {\n\tjava.time.Instant sealStamp = this.terminal.requestLastSeal();\n\tLogger.info(\"Last seal on \" + sealStamp.toString());\n\tjava.util.function.Predicate<java.time.Instant> filter = i -> !i.isBefore(sealStamp);\n\tthis.terminal.initDatamarts(")).output(expression().output(mark("datamartsLoad")).output(literal("()"))).output(literal(");\n\t")).output(mark("subscriber").multiple("\n")).output(literal("\n}")),
			rule().condition((type("datamartsLoad")), (trigger("datamartsourceselector"))).output(literal("protected abstract String ")).output(mark("datamartsLoad")).output(literal("();")),
			rule().condition((allTypes("subscriber","durable","filtered")), (trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(mark("terminal")).output(literal(".")).output(mark("eventQn", "FirstUpperCase")).output(literal("Consumer) (e, t) -> new ")).output(mark("package")).output(literal(".subscribers.")).output(mark("name", "FirstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e, t), \"")).output(mark("subscriberId")).output(literal("\", filter, null);")),
			rule().condition((allTypes("subscriber","durable")), (trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(mark("terminal")).output(literal(".")).output(mark("eventQn", "FirstUpperCase")).output(literal("Consumer) (e, t) -> new ")).output(mark("package")).output(literal(".subscribers.")).output(mark("name", "FirstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e, t), \"")).output(mark("subscriberId")).output(literal("\");")),
			rule().condition((type("subscriber")), (trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(mark("terminal")).output(literal(".")).output(mark("eventQn", "FirstUpperCase")).output(literal("Consumer) (e, t) -> new ")).output(mark("package")).output(literal(".subscribers.")).output(mark("name", "FirstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e, t));")),
			rule().condition((trigger("split"))).output(mark("type")).output(literal(".Split.")).output(mark("value")),
			rule().condition((allTypes("service","jmx")), (trigger("field"))).output(literal("protected io.intino.alexandria.jmx.JMXServer ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";")),
			rule().condition((allTypes("service","slack")), (trigger("field"))).output(literal("private io.intino.alexandria.slack.Bot ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";")),
			rule().condition((allTypes("service","messaging")), (trigger("field"))).output(literal("private ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";")),
			rule().condition((type("sentinel")), (trigger("field"))).output(literal("private io.intino.alexandria.scheduler.AlexandriaScheduler scheduler = new io.intino.alexandria.scheduler.AlexandriaScheduler();")),
			rule().condition((type("connector")), (trigger("field"))).output(literal("protected io.intino.alexandria.terminal.Connector connector;")),
			rule().condition((type("terminal")), (trigger("field"))).output(literal("protected ")).output(mark("qn")).output(literal(" terminal;")),
			rule().condition((type("workflow")), (trigger("field"))).output(literal("protected ")).output(mark("package")).output(literal(".bpm.Workflow workflow;")),
			rule().condition((type("agenda")), (trigger("field"))).output(literal("protected AgendaService agenda;")),
			rule().condition((allTypes("service","messaging")), (trigger("getter"))).output(literal("public ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";\n}")),
			rule().condition((type("terminal")), (trigger("getter"))).output(literal("public ")).output(mark("qn")).output(literal(" terminal() {\n\treturn this.terminal;\n}\n\nprotected io.intino.alexandria.terminal.Connector datahubConnector() {\n\treturn this.connector;\n}")),
			rule().condition((type("workflow")), (trigger("getter"))).output(literal("public ")).output(mark("package")).output(literal(".bpm.Workflow workflow() {\n\treturn this.workflow;\n}")),
			rule().condition((type("agenda")), (trigger("getter"))).output(literal("public AgendaService agenda() {\n\treturn this.agenda;\n}")),
			rule().condition((allTypes("service","slack")), (trigger("getter"))).output(literal("public ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("SlackBot ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal("() {\n\treturn (")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("SlackBot) ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";\n}")),
			rule().condition((type("service")), (trigger("getter"))),
			rule().condition((trigger("spark"))).output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.instance().stop();")),
			rule().condition((type("connector")), (trigger("stop"))).output(literal("if (connector instanceof io.intino.alexandria.terminal.JmsConnector) ((io.intino.alexandria.terminal.JmsConnector) connector).stop();")),
			rule().condition((allTypes("service","slack")), (trigger("stop"))).output(literal("this.")).output(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).output(literal(".disconnect();")),
			rule().condition((type("service")), (trigger("stop"))),
			rule().condition((type("service"))),
			rule().condition((type("sentinel")), (trigger("getter"))).output(literal("public io.intino.alexandria.scheduler.AlexandriaScheduler scheduler() {\n\treturn this.scheduler;\n}")),
			rule().condition((trigger("authservice"))).output(literal("protected abstract io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl);")),
			rule().condition((trigger("translatorserviceinitialization"))).output(literal("private void initTranslatorService() {\n\ttranslatorService = new io.intino.alexandria.ui.services.TranslatorService();\n\t")).output(mark("useDictionaries").multiple("\n")).output(literal("\n\ttranslatorService.addAll(")).output(mark("package", "validPackage")).output(literal(".I18n.dictionaries());\n\ttranslatorService.addAll(io.intino.alexandria.I18n.dictionaries());\n}")),
			rule().condition((trigger("usedictionaries"))).output(literal("translatorService.addAll(")).output(mark("")).output(literal(");")),
			rule().condition((trigger("pushservice"))).output(literal("public PushService pushService() {\n\treturn pushService;\n}")),
			rule().condition((trigger("registersoul"))).output(literal("public java.util.List<Soul> souls() {\n\treturn new java.util.ArrayList<>(uiSouls.values());\n}\n\npublic java.util.Optional<Soul> soul(String clientId) {\n\treturn java.util.Optional.ofNullable(uiSouls.get(clientId));\n}\n\npublic void registerSoul(String clientId, Soul soul) {\n\t")).output(expression().output(literal("if (owner != null) ((")).output(mark("parent")).output(literal("Box) owner).registerSoul(clientId, soul);"))).output(literal("\n\tuiSouls.put(clientId, soul);\n}\n\npublic void unRegisterSoul(String clientId) {\n\t")).output(expression().output(literal("if (owner != null) ((")).output(mark("parent")).output(literal("Box) owner).unRegisterSoul(clientId);"))).output(literal("\n\tuiSouls.remove(clientId);\n\tif (uiSouls.size() <= 0) notifySoulsClosed();\n}\n\npublic void onSoulsClosed(io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed listener) {\n\t")).output(expression().output(literal("if (owner != null) ((")).output(mark("parent")).output(literal("Box) owner).onSoulsClosed(listener);"))).output(literal("\n\tthis.soulsClosedListeners.add(listener);\n}\n\nprivate void notifySoulsClosed() {\n\tsoulsClosedListeners.forEach(l -> l.accept());\n}")),
			rule().condition((allTypes("conf","file")), (trigger("additionalparameter"))).output(literal("configuration().get(\"")).output(mark("value")).output(literal("\") == null ? null : new java.io.File(configuration().get(\"")).output(mark("value")).output(literal("\"))")),
			rule().condition((type("conf")), (trigger("additionalparameter"))).output(literal("configuration().get(\"")).output(mark("value")).output(literal("\")")),
			rule().condition((allTypes("conf","file")), (trigger("parameter"))).output(literal("configuration().get(\"")).output(mark("value")).output(literal("\") == null ? null : new java.io.File(configuration().get(\"")).output(mark("value")).output(literal("\"))")),
			rule().condition((type("conf")), (trigger("parameter"))).output(literal("configuration().get(\"")).output(mark("value")).output(literal("\")")),
			rule().condition((type("custom")), (trigger("parameter"))).output(literal("configuration().get(\"")).output(mark("value", "customParameter")).output(literal("\")")),
			rule().condition((type("custom")), (trigger("authentication"))).output(literal("url(configuration().get(\"")).output(mark("value", "customParameter")).output(literal("\"))")),
			rule().condition((type("custom")), (trigger("edition"))).output(literal("url(configuration().get(\"")).output(mark("value", "customParameter")).output(literal("\"))")),
			rule().condition((type("file")), (trigger("parameter"))).output(mark("value")),
			rule().condition((type("int")), (trigger("parameter"))).output(mark("value")),
			rule().condition((trigger("parameter"))).output(literal("\"")).output(mark("value")).output(literal("\"")),
			rule().condition((trigger("authentication"))).output(literal("url(\"")).output(mark("value")).output(literal("\")")),
			rule().condition((trigger("edition"))).output(literal("url(\"")).output(mark("value")).output(literal("\")")),
			rule().condition((trigger("parentinit"))),
			rule().condition((trigger("hide"))),
			rule().condition((trigger("hideui"))).output(literal(";")),
				rule().condition((trigger("hideui"))).output(literal(";"))
		);
	}
}