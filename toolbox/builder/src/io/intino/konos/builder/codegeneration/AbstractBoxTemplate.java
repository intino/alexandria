package io.intino.konos.builder.codegeneration;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractBoxTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("box"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).output(expression().output(literal("\n")).output(literal("import java.util.HashMap;")).output(literal("\n")).output(literal("import java.util.Map")).output(mark("hasUi", "hideUi"))).output(literal("\n\nimport io.intino.alexandria.logger.Logger;\nimport java.util.logging.ConsoleHandler;\nimport java.util.logging.Level;\n\n")).output(expression().output(mark("messagehub", "import"))).output(literal("\n")).output(expression().output(mark("terminal", "import"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.rest.AlexandriaSpark;")).output(mark("hasREST"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.ui.Soul")).output(mark("hasUi", "hideUi"))).output(literal("\n\npublic abstract class AbstractBox extends ")).output(expression().output(mark("hasUi", "uiBox")).next(expression().output(literal("io.intino.alexandria.core.Box")))).output(literal(" {\n\tprotected ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration configuration;\n\t")).output(expression().output(mark("service", "field").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("messagehub", "field"))).output(literal("\n\t")).output(expression().output(mark("terminal", "field"))).output(literal("\n\t")).output(expression().output(mark("datalake", "field"))).output(literal("\n\t")).output(expression().output(mark("sentinel", "field"))).output(literal("\n\t")).output(expression().output(mark("workflow", "field"))).output(literal("\n    ")).output(expression().output(literal("protected Map<String, Soul> uiSouls = new java.util.HashMap<>()")).output(mark("hasUi", "hideUi"))).output(literal("\n    ")).output(expression().output(literal("private java.util.List<io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed> soulsClosedListeners = new java.util.ArrayList<>()")).output(mark("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private io.intino.alexandria.ui.services.AuthService authService")).output(mark("hasUi", "hideUi"))).output(literal("\n\n\tpublic AbstractBox(String[] args) {\n\t\tthis(new ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration(args));\n\t}\n\n\tpublic AbstractBox(")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration configuration) {\n\t\t")).output(expression().output(literal("owner = new ")).output(mark("parent")).output(literal("Box(configuration);"))).output(literal("\n\t\tthis.configuration = configuration;\n\t\tinitJavaLogger();\n\t\t")).output(expression().output(mark("service", "setup").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("datalake", "setup"))).output(literal("\n\t\t")).output(expression().output(mark("messagehub", "setup"))).output(literal("\n\t\t")).output(expression().output(mark("terminal", "setup"))).output(literal("\n\t}\n\n\tpublic ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Configuration configuration() {\n\t\treturn configuration;\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\t")).output(expression().output(mark("hasParent")).output(literal("owner.put(o);"))).output(literal("\n\t\treturn this;\n\t}\n\n\tprotected abstract void beforeStart();\n\n\tprotected abstract void afterStart();\n\n\tpublic io.intino.alexandria.core.Box start() {\n\t\tbeforeStart();\n\t\tif (owner != null) owner.start();\n\t\tinitUI();\n\t\tinitRESTServices();\n\t\tinitJMXServices();\n\t\tinitDatalake();\n\t\tinitMessageHub();\n\t\tinitTerminal();\n\t\tinitMessagingServices();\n\t\tinitSentinels();\n\t\tinitSlackBots();\n\t\tinitWorkflow();\n\t\tafterStart();\n\t\treturn this;\n\t}\n\n\tpublic void stop() {\n\t\tbeforeStop();\n\t\tif (owner != null) owner.stop();\n\t\t")).output(expression().output(mark("spark").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("service", "close").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("messagehub", "close"))).output(literal("\n\t\t")).output(expression().output(mark("terminal", "close"))).output(literal("\n\t\tafterStop();\n\t}\n\tprotected abstract void beforeStop();\n\n\tprotected abstract void afterStop();\n\n\t")).output(expression().output(mark("hasUi", "registerSoul"))).output(literal("\n\n\t")).output(expression().output(mark("service", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("hasUi", "authService"))).output(literal("\n\n\t")).output(expression().output(mark("messagehub", "getter"))).output(literal("\n\n\t")).output(expression().output(mark("terminal", "getter"))).output(literal("\n\n\t")).output(expression().output(mark("datalake", "getter"))).output(literal("\n\n\t")).output(expression().output(mark("sentinel", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("workflow", "getter"))).output(literal("\n\n\tprivate void initRESTServices() {\n\t\t")).output(mark("service", "rest").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initMessagingServices() {\n\t\t")).output(mark("service", "messaging").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initJMXServices() {\n\t\t")).output(mark("service", "jmx").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initSlackBots() {\n\t\t")).output(mark("service", "slack").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initUI() {\n\t\t")).output(mark("service", "ui").multiple("\n")).output(literal("\n\t}\n\n\t")).output(expression().output(mark("hasUi", "translatorServiceInitialization"))).output(literal("\n\n\tprivate void initDatalake() {\n\t\t")).output(expression().output(mark("datalake", "init"))).output(literal("\n\t}\n\n\tprivate void initMessageHub() {\n\t\t")).output(expression().output(mark("messagehub", "init"))).output(literal("\n\t}\n\n\n\tprivate void initTerminal() {\n\t\t")).output(expression().output(mark("terminal", "init"))).output(literal("\n\t}\n\n\tprivate void initSentinels() {\n\t\t")).output(expression().output(mark("sentinel", "init").multiple("\n"))).output(literal("\n\t}\n\n\tprivate void initWorkflow() {\n\t\t")).output(expression().output(mark("workflow", "init"))).output(literal("\n\t}\n\n\t")).output(expression().output(mark("authenticationValidator"))).output(literal("\n\n\tprivate void initJavaLogger() {\n\t\tfinal java.util.logging.Logger Logger = java.util.logging.Logger.getGlobal();\n\t\tfinal ConsoleHandler handler = new ConsoleHandler();\n\t\thandler.setLevel(Level.INFO);\n\t\thandler.setFormatter(new io.intino.alexandria.logger.Formatter());\n\t\tLogger.setUseParentHandlers(false);\n\t\tLogger.addHandler(handler);\n\t}\n\n\tprivate java.net.URL url(String url) {\n\t\ttry {\n\t\t\treturn new java.net.URL(url);\n\t\t} catch (java.net.MalformedURLException e) {\n\t\t\treturn null;\n\t\t}\n\t}\n}")),
			rule().condition((type("datalake")), (trigger("import"))).output(literal("import io.intino.alexandria.datalake.Datalake;")),
			rule().condition((type("messageHub")), (trigger("import"))).output(literal("import io.intino.alexandria.event.EventHub;")),
			rule().condition((trigger("uibox"))).output(literal("io.intino.alexandria.ui.AlexandriaUiBox")),
			rule().condition((allTypes("service","ui")), (trigger("setup"))).output(literal("this.authService = ")).output(expression().output(literal("this.authService(")).output(mark("authentication")).output(literal(")")).next(expression().output(literal("null")))).output(literal(";\n")).output(expression().output(literal("if(")).output(mark("parameter")).output(literal(" != null && !")).output(mark("parameter")).output(literal(".isEmpty())"))).output(expression().output(literal("io.intino.alexandria.rest.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(mark("parameter")).output(literal("), \"www/\")"))).output(literal(";\nio.intino.alexandria.rest.AlexandriaSparkBuilder.setUI(true);\nio.intino.alexandria.rest.AlexandriaSparkBuilder.addParameters(this.authService);")),
			rule().condition((allTypes("service","rest")), (trigger("setup"))).output(literal("io.intino.alexandria.rest.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(mark("parameter")).output(literal("), \"www/\");")),
			rule().condition((allTypes("datalake","mirror")), (trigger("setup"))).output(literal("this.datalake = new Datalake(new java.io.File(")).output(mark("path", "parameter")).output(literal("), ")).output(mark("parameter").multiple(", ")).output(literal(");")),
			rule().condition((type("datalake")), (trigger("setup"))).output(literal("this.datalake = new io.intino.alexandria.datalake.file.FileDatalake(new java.io.File(")).output(mark("path", "parameter")).output(literal("));")),
			rule().condition((type("terminal")), (trigger("setup"))).output(literal("this.eventHub = new io.intino.alexandria.event.JmsEventHub(")).output(mark("parameter").multiple(", ")).output(literal(");\nthis.terminal = new ")).output(mark("qn")).output(literal("(eventHub);")),
			rule().condition((allTypes("jms","messageHub")), (trigger("setup"))).output(literal("this.messageHub = new ")).output(mark("package")).output(literal(".MessageHub(")).output(mark("parameter").multiple(", ")).output(literal(");")),
			rule().condition((type("messageHub")), (trigger("setup"))).output(literal("this.messageHub = new ")).output(mark("package")).output(literal(".MessageHub((")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);")),
			rule().condition((allTypes("service","jmx")), (trigger("jmx"))).output(literal("this.")).output(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).output(literal(" = new JMX")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("().init(((")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this));\nLogger.info(\"JMX service ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((allTypes("service","slack")), (trigger("slack"))).output(literal("if (")).output(mark("parameter")).output(literal(" == null || ")).output(mark("parameter")).output(literal(".isEmpty()) return;\nthis.")).output(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).output(literal(" = new ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("SlackBot((")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this, ")).output(mark("parameter")).output(literal(");\nLogger.info(\"Slack service ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((allTypes("service","rest")), (trigger("rest"))).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Service.setup(io.intino.alexandria.rest.AlexandriaSparkBuilder.instance(), (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this).start();\nLogger.info(\"REST service ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((allTypes("service","ui")), (trigger("ui"))).output(expression().output(literal("if (")).output(mark("parameter")).output(literal(" == null || !io.intino.alexandria.rest.AlexandriaSparkBuilder.isUI()) return;"))).output(literal("\n")).output(expression().output(literal("this.initTranslatorService()"))).output(literal(";\nio.intino.alexandria.ui.UISpark sparkInstance = (io.intino.alexandria.ui.UISpark) io.intino.alexandria.rest.AlexandriaSparkBuilder.instance();\nio.intino.alexandria.ui.services.push.PushService pushService = new io.intino.alexandria.ui.services.push.PushService();\n")).output(mark("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher routeDispatcher = new ")).output(mark("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher();\n")).output(mark("package", "validPackage")).output(literal(".ui.")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Service.init(sparkInstance, (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this, pushService, routeDispatcher);\n")).output(mark("use")).output(literal("\nio.intino.alexandria.ui.UiElementsService.initDisplays(sparkInstance, pushService);\nsparkInstance.start();\nLogger.info(\"UI ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((trigger("use"))).output(mark("")).output(literal(".initDisplays(sparkInstance, pushService);")),
			rule().condition((allTypes("service","messaging")), (trigger("messaging"))).output(literal("this.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service(this.messageHub, (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);\nLogger.info(\"Messaging service ")).output(mark("name")).output(literal(": started!\");")),
			rule().condition((type("sentinel")), (trigger("init"))).output(literal("Sentinels.init(this.scheduler, (")).output(mark("configuration", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box) this);")),
			rule().condition((type("workflow")), (trigger("init"))).output(literal("this.workflow = new ")).output(mark("package")).output(literal(".bpm.Workflow((")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box)this, this.configuration().workspace());")),
			rule().condition((allTypes("datalake","mirror")), (trigger("init"))).output(literal("this.datalake.init();")),
			rule().condition((type("terminal")), (trigger("init"))).output(expression().output(literal("if(this.terminal != null) {")).output(literal("\n")).output(literal("\t")).output(mark("subscriber").multiple("\n")).output(literal("\n")).output(literal("}")).output(literal("\n"))),
			rule().condition((type("messageHub")), (trigger("init"))).output(expression().output(literal("if(this.messageHub != null) {")).output(literal("\n")).output(literal("\t")).output(mark("subscriber").multiple("\n")).output(literal("\n")).output(literal("}")).output(literal("\n"))).output(literal("\n")).output(expression().output(literal("registerFeeders();")).output(mark("feeder", "hide"))).output(literal("\nLogger.info(\"Message Hub connected!\");")),
			rule().condition((type("terminal")), (trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(mark("terminal")).output(literal(".")).output(mark("tank")).output(literal("Consumer) e -> new ")).output(mark("package")).output(literal(".subscribers.")).output(mark("name", "FirstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e)")).output(expression().output(literal(", \"")).output(mark("subscriberId")).output(literal("\""))).output(expression().output(literal(", ")).output(mark("context"))).output(literal(");")),
			rule().condition((trigger("context"))).output(mark("type")).output(literal(".Context.")).output(mark("value")),
			rule().condition((type("messageHub")), (trigger("subscriber"))).output(literal("this.messageHub.attachListener(\"")).output(mark("source")).output(literal("\", \"")).output(mark("subscriberId")).output(literal("\", m -> new ")).output(mark("package")).output(literal(".subscribers.")).output(mark("name", "FirstUpperCase")).output(literal("((")).output(mark("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).handle(m));")),
			rule().condition((allTypes("service","jmx")), (trigger("field"))).output(literal("private io.intino.alexandria.jmx.JMXServer ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";")),
			rule().condition((allTypes("service","slack")), (trigger("field"))).output(literal("private io.intino.alexandria.slack.Bot ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";")),
			rule().condition((allTypes("service","messaging")), (trigger("field"))).output(literal("private ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";")),
			rule().condition((type("sentinel")), (trigger("field"))).output(literal("private io.intino.alexandria.scheduler.AlexandriaScheduler scheduler = new io.intino.alexandria.scheduler.AlexandriaScheduler();")),
			rule().condition((allTypes("datalake","mirror")), (trigger("field"))).output(literal("private ")).output(mark("package")).output(literal(".Datalake datalake;")),
			rule().condition((type("datalake")), (trigger("field"))).output(literal("private io.intino.alexandria.datalake.Datalake datalake;")),
			rule().condition((type("messageHub")), (trigger("field"))).output(literal("private ")).output(mark("package")).output(literal(".MessageHub messageHub;")),
			rule().condition((type("terminal")), (trigger("field"))).output(literal("private io.intino.alexandria.event.JmsEventHub eventHub;\nprivate ")).output(mark("qn")).output(literal(" terminal;")),
			rule().condition((type("workflow")), (trigger("field"))).output(literal("private ")).output(mark("package")).output(literal(".bpm.Workflow workflow;")),
			rule().condition((allTypes("service","messaging")), (trigger("getter"))).output(literal("public ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Service ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";\n}")),
			rule().condition((type("datalake")), (trigger("getter"))).output(literal("public io.intino.alexandria.datalake.Datalake datalake() {\n\treturn this.datalake;\n}")),
			rule().condition((type("terminal")), (trigger("getter"))).output(literal("public ")).output(mark("qn")).output(literal(" terminal() {\n\treturn this.terminal;\n}\n\npublic io.intino.alexandria.event.JmsEventHub eventHub() {\n\treturn this.eventHub;\n}")),
			rule().condition((type("messagehub")), (trigger("getter"))).output(literal("public io.intino.alexandria.message.MessageHub messageHub() {\n\treturn this.messageHub;\n}\n\n")).output(expression().output(literal("public io.intino.alexandria.core.Feeders feeders() {")).output(literal("\n")).output(literal("\treturn io.intino.alexandria.core.Feeders.get();")).output(literal("\n")).output(literal("}")).output(literal("\n")).output(literal("\n")).output(literal("private void registerFeeders() {")).output(literal("\n")).output(literal("\t")).output(mark("feeder").multiple("\n")).output(literal("\n")).output(literal("}"))),
			rule().condition((type("workflow")), (trigger("getter"))).output(literal("public ")).output(mark("package")).output(literal(".bpm.Workflow workflow() {\n\treturn this.workflow;\n}")),
			rule().condition((trigger("feeder"))).output(literal("io.intino.alexandria.core.Feeders.get().register(new ")).output(mark("package", "validPackage")).output(literal(".datalake.feeders.")).output(mark("name", "FirstUpperCase")).output(literal("((")).output(mark("box", "FirstUpperCase")).output(literal("Box) this));")),
			rule().condition((allTypes("service","slack")), (trigger("getter"))).output(literal("public ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("SlackBot ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal("() {\n\treturn (")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("SlackBot) ")).output(mark("name", "SnakeCaseToCamelCase", "firstlowerCase")).output(literal(";\n}")),
			rule().condition((type("service")), (trigger("getter"))),
			rule().condition((trigger("spark"))).output(literal("io.intino.alexandria.rest.AlexandriaSparkBuilder.instance().stop();")),
			rule().condition((type("terminal")), (trigger("close"))).output(literal("if (terminal != null) terminal.stop();")),
			rule().condition((type("messageHub")), (trigger("close"))).output(literal("if (messageHub != null) messageHub.stop();")),
			rule().condition((type("service")), (trigger("close"))),
			rule().condition((type("service"))),
			rule().condition((type("sentinel")), (trigger("getter"))).output(literal("public io.intino.alexandria.scheduler.AlexandriaScheduler scheduler() {\n\treturn this.scheduler;\n}")),
			rule().condition((trigger("authservice"))).output(literal("protected abstract io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl);")),
			rule().condition((trigger("translatorserviceinitialization"))).output(literal("private void initTranslatorService() {\n    translatorService = new io.intino.alexandria.ui.services.TranslatorService();\n\t")).output(mark("useDictionaries").multiple("\n")).output(literal("\n    translatorService.addAll(")).output(mark("package", "validPackage")).output(literal(".I18n.dictionaries());\n    translatorService.addAll(io.intino.alexandria.I18n.dictionaries());\n}")),
			rule().condition((trigger("usedictionaries"))).output(literal("translatorService.addAll(")).output(mark("")).output(literal(");")),
			rule().condition((trigger("registersoul"))).output(literal("public java.util.List<Soul> souls() {\n\treturn new java.util.ArrayList<>(uiSouls.values());\n}\n\n\tpublic java.util.Optional<Soul> soul(String clientId) {\n\t\treturn java.util.Optional.ofNullable(uiSouls.get(clientId));\n\t}\n\n\tpublic void registerSoul(String clientId, Soul soul) {\n\t\t")).output(expression().output(literal("if (owner != null) ((")).output(mark("parent")).output(literal("Box) owner).registerSoul(clientId, soul);"))).output(literal("\n\t\tuiSouls.put(clientId, soul);\n\t}\n\n\tpublic void unRegisterSoul(String clientId) {\n\t\t")).output(expression().output(literal("if (owner != null) ((")).output(mark("parent")).output(literal("Box) owner).unRegisterSoul(clientId);"))).output(literal("\n\t\tuiSouls.remove(clientId);\n\t\tif (uiSouls.size() <= 0) notifySoulsClosed();\n\t}\n\n\tpublic void onSoulsClosed(io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed listener) {\n\t\t")).output(expression().output(literal("if (owner != null) ((")).output(mark("parent")).output(literal("Box) owner).onSoulsClosed(listener);"))).output(literal("\n\t\tthis.soulsClosedListeners.add(listener);\n\t}\n\n\tprivate void notifySoulsClosed() {\n\t\tsoulsClosedListeners.forEach(l -> l.accept());\n\t}")),
			rule().condition((trigger("authenticationvalidator"))).output(literal("public abstract io.intino.alexandria.rest.security.")).output(mark("type", "FirstUpperCase")).output(literal("AuthenticationValidator authenticationValidator();")),
			rule().condition((allTypes("conf","file")), (trigger("parameter"))).output(literal("new java.io.File(configuration().get(\"")).output(mark("value")).output(literal("\"))")),
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
			rule().condition((trigger("hideui"))).output(literal(";")),
			rule().condition((trigger("hidedatalake"))).output(literal(";"))
		);
	}
}