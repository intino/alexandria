package io.intino.konos.builder.codegeneration;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AbstractBoxTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("box")).output(literal("package ")).output(placeholder("package")).output(literal(";\n\nimport java.util.LinkedHashMap;\nimport java.util.Map;\nimport java.util.UUID;\n")).output(expression().output(literal("\n")).output(literal("import java.util.HashMap;")).output(literal("\n")).output(literal("import java.util.Map")).output(placeholder("hasUi", "hideUi"))).output(literal("\n\nimport io.intino.alexandria.logger.Logger;\nimport java.util.logging.ConsoleHandler;\nimport java.util.logging.Level;\n\n")).output(expression().output(placeholder("messagehub", "import"))).output(literal("\n")).output(expression().output(placeholder("terminal", "import"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.http.AlexandriaSpark;")).output(placeholder("hasREST"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.ui.services.push.PushService")).output(placeholder("hasUI", "hideUi"))).output(literal("\n")).output(expression().output(literal("import io.intino.alexandria.ui.Soul")).output(placeholder("hasUi", "hideUi"))).output(literal("\n\npublic abstract class AbstractBox extends ")).output(expression().output(placeholder("hasUi", "uiBox")).next(expression().output(literal("io.intino.alexandria.core.Box")))).output(literal(" {\n\tprotected ")).output(placeholder("name", "PascalCase")).output(literal("Configuration configuration;\n\t")).output(expression().output(placeholder("service", "field").multiple("\n"))).output(literal("\n\t")).output(expression().output(placeholder("connector", "field"))).output(literal("\n\t")).output(expression().output(placeholder("terminal", "field"))).output(literal("\n\t")).output(expression().output(placeholder("sentinel", "field"))).output(literal("\n\t")).output(expression().output(placeholder("workflow", "field"))).output(literal("\n\t")).output(expression().output(literal("protected Map<String, Soul")).output(literal(">")).output(literal(" uiSouls = new java.util.HashMap<")).output(literal(">")).output(literal("()")).output(placeholder("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private java.util.List<io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed")).output(literal(">")).output(literal(" soulsClosedListeners = new java.util.ArrayList<")).output(literal(">")).output(literal("()")).output(placeholder("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private io.intino.alexandria.ui.services.AuthService authService")).output(placeholder("hasUi", "hideUi"))).output(literal("\n\t")).output(expression().output(literal("private PushService pushService")).output(placeholder("hasUi", "hideUi"))).output(literal("\n\n\tpublic AbstractBox(String[] args) {\n\t\tthis(new ")).output(placeholder("name", "PascalCase")).output(literal("Configuration(args));\n\t}\n\n\tpublic AbstractBox(")).output(placeholder("name", "PascalCase")).output(literal("Configuration configuration) {\n\t\t")).output(expression().output(literal("owner = new ")).output(placeholder("parent")).output(literal("Box(configuration);"))).output(literal("\n\t\tthis.configuration = configuration;\n\t\t")).output(expression().output(placeholder("parent", "empty")).next(expression().output(literal("initJavaLogger();")))).output(literal("\n\t\t")).output(expression().output(placeholder("connector", "setup"))).output(literal("\n\t\t")).output(expression().output(placeholder("service", "setup").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("terminal", "setup"))).output(literal("\n\t}\n\n\tpublic ")).output(placeholder("name", "PascalCase")).output(literal("Configuration configuration() {\n\t\treturn configuration;\n\t}\n\n\t@Override\n\tpublic io.intino.alexandria.core.Box put(Object o) {\n\t\t")).output(expression().output(placeholder("hasParent")).output(literal("owner.put(o);"))).output(literal("\n\t\treturn this;\n\t}\n\n\tpublic abstract void beforeStart();\n\n\tpublic io.intino.alexandria.core.Box start() {\n\t\tinitConnector();\n\t\tif (owner != null) owner.beforeStart();\n\t\tbeforeStart();\n\t\tif (owner != null) owner.startServices();\n\t\tstartServices();\n\t\tif (owner != null) owner.afterStart();\n\t\tafterStart();\n\t\treturn this;\n\t}\n\n\tpublic abstract void afterStart();\n\n\tpublic abstract void beforeStop();\n\n\tpublic void stop() {\n\t\tif (owner != null) owner.beforeStop();\n\t\tbeforeStop();\n\t\tif (owner != null) owner.stopServices();\n\t\tstopServices();\n\t\tif (owner != null) owner.afterStop();\n\t\tafterStop();\n\t}\n\n\t@Override\n\tpublic void stopServices() {\n\t\t")).output(expression().output(placeholder("spark").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("service", "stop").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("connector", "stop"))).output(literal("\n\t}\n\n\tpublic abstract void afterStop();\n\n\t@Override\n\tpublic void startServices() {\n\t\tinitUI();\n\t\tinitAgenda();\n\t\tinitRestServices();\n\t\tinitSoapServices();\n\t\tinitJmxServices();\n\t\tinitTerminal();\n\t\tinitMessagingServices();\n\t\tinitSentinels();\n\t\tinitSlackBots();\n\t\tinitWorkflow();\n\t\tinitCli();\n\t}\n\n\t")).output(expression().output(placeholder("hasUi", "pushService"))).output(literal("\n\n\t")).output(expression().output(placeholder("hasUi", "registerSoul"))).output(literal("\n\n\t")).output(expression().output(placeholder("service", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(placeholder("hasUi", "authService"))).output(literal("\n\n\t")).output(expression().output(placeholder("terminal", "getter"))).output(literal("\n\n\t")).output(expression().output(placeholder("sentinel", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(placeholder("workflow", "getter"))).output(literal("\n\n\t")).output(placeholder("service", "setupMethod").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(placeholder("terminal", "datamartSourceSelector"))).output(literal("\n\n\tprivate void initRestServices() {\n\t\t")).output(placeholder("service", "rest").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initSoapServices() {\n\t\t")).output(placeholder("service", "soap").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initMessagingServices() {\n\t\t")).output(placeholder("service", "messaging").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initJmxServices() {\n\t\t")).output(placeholder("service", "jmx").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initSlackBots() {\n\t\t")).output(placeholder("service", "slack").multiple("\n")).output(literal("\n\t}\n\n\tprivate void initUI() {\n\t\t")).output(placeholder("service", "ui").multiple("\n")).output(literal("\n\t}\n\n\t")).output(expression().output(placeholder("hasUi", "translatorServiceInitialization"))).output(literal("\n\n\tprotected void initConnector() {\n\t\t")).output(expression().output(placeholder("connector", "init"))).output(literal("\n\t}\n\n\tprotected void initTerminal() {\n\t\t")).output(expression().output(placeholder("terminal", "init"))).output(literal("\n\t}\n\n\tprotected void initSentinels() {\n\t\t")).output(expression().output(placeholder("sentinel", "init").multiple("\n"))).output(literal("\n\t}\n\n\tprotected void initWorkflow() {\n\t\t")).output(expression().output(placeholder("workflow", "init"))).output(literal("\n\t}\n\n\tprotected void initAgenda() {\n\t\t")).output(placeholder("service", "agenda").multiple("\n")).output(literal("\n\t}\n\n\tprotected void initCli() {\n\t\t")).output(placeholder("service", "cli").multiple("\n")).output(literal("\n\t}\n\n\tprotected void initJavaLogger() {\n\t\tfinal java.util.logging.Logger Logger = java.util.logging.Logger.getGlobal();\n\t\tfinal ConsoleHandler handler = new ConsoleHandler();\n\t\thandler.setLevel(Level.INFO);\n\t\thandler.setFormatter(new io.intino.alexandria.logger.Formatter());\n\t\tLogger.setUseParentHandlers(false);\n\t\tLogger.addHandler(handler);\n\t\t")).output(placeholder("logger")).output(literal("\n\t}\n\n\tpublic static java.net.URL url(String url) {\n        try {\n            return new java.net.URI(url).toURL();\n        } catch (java.net.MalformedURLException | java.net.URISyntaxException | IllegalArgumentException e) {\n            return null;\n        }\n    }\n}")));
		rules.add(rule().condition(trigger("empty")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("logger"), trigger("logger"))).output(literal("io.intino.alexandria.logger4j.Logger.init();")));
		rules.add(rule().condition(all(allTypes("connector"), trigger("import"))).output(literal("import io.intino.alexandria.terminal.Connector;")));
		rules.add(rule().condition(trigger("uibox")).output(literal("io.intino.alexandria.ui.AlexandriaUiBox")));
		rules.add(rule().condition(all(allTypes("service", "ui"), trigger("setupmethod"))).output(literal("protected void beforeSetup")).output(placeholder("name", "firstUpperCase")).output(literal("Ui(io.intino.alexandria.ui.UISpark sparkInstance) {}\npublic void setup")).output(placeholder("name", "firstUpperCase")).output(literal("Ui() {\n\t")).output(expression().output(literal("if(")).output(placeholder("parameter")).output(literal(" == null || ")).output(placeholder("parameter")).output(literal(".isEmpty())"))).output(literal(" return;\n\tthis.authService = ")).output(expression().output(literal("this.authService(")).output(placeholder("authentication")).output(literal(")")).next(expression().output(literal("null")))).output(literal(";\n\t")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(placeholder("parameter")).output(literal("), \"www/\")"))).output(literal(";\n\tio.intino.alexandria.http.AlexandriaSparkBuilder.setUI(true);\n\tio.intino.alexandria.http.AlexandriaSparkBuilder.addParameters(this.authService);\n\tthis.pushService = new io.intino.alexandria.ui.services.push.PushService();\n\tio.intino.alexandria.ui.UISpark sparkInstance = (io.intino.alexandria.ui.UISpark) io.intino.alexandria.http.AlexandriaSparkBuilder.instance();\n\tbeforeSetup")).output(placeholder("name", "firstUpperCase")).output(literal("Ui(sparkInstance);\n\t")).output(placeholder("package", "validPackage")).output(literal(".ui.")).output(placeholder("name", "PascalCase")).output(literal("Service.init(sparkInstance, (")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this, pushService, new ")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher());\n\t")).output(placeholder("use").multiple("\n")).output(literal("\n\tio.intino.alexandria.ui.UiElementsService.initDisplays(sparkInstance, pushService);\n}")));
		rules.add(rule().condition(all(allTypes("service", "cli"), trigger("setupmethod"))).output(literal("public void setupCli() {\n\t")).output(expression().output(literal("if(")).output(placeholder("parameter")).output(literal(" == null || ")).output(placeholder("parameter")).output(literal(".isEmpty())"))).output(literal(" return;\n\t")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(placeholder("parameter")).output(literal("), \"www/\")"))).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("service", "soap"), trigger("setup"))).output(expression().output(literal("if(")).output(placeholder("parameter")).output(literal(" != null && !")).output(placeholder("parameter")).output(literal(".isEmpty()) "))).output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(placeholder("parameter")).output(literal("), \"www/\");")));
		rules.add(rule().condition(all(allTypes("terminal"), trigger("setup"))).output(literal("this.terminal = new ")).output(placeholder("qn")).output(literal("(connector);")));
		rules.add(rule().condition(all(allTypes("connector"), trigger("setup"))).output(literal("this.connector = io.intino.alexandria.terminal.ConnectorFactory.createConnector(new io.intino.alexandria.jms.ConnectionConfig(")).output(placeholder("parameter").multiple(", ")).output(literal("), ")).output(placeholder("additionalParameter").multiple(",")).output(literal(");")));
		rules.add(rule().condition(all(allTypes("service", "jmx"), trigger("jmx"))).output(literal("this.")).output(placeholder("name", "CamelCase")).output(literal(" = new JMX")).output(placeholder("name", "PascalCase")).output(literal("().init(((")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this));\nLogger.info(\"Jmx service ")).output(placeholder("name")).output(literal(": started!\");")));
		rules.add(rule().condition(all(allTypes("service", "slack"), trigger("slack"))).output(literal("if (")).output(placeholder("parameter")).output(literal(" == null || ")).output(placeholder("parameter")).output(literal(".isEmpty()) return;\nthis.")).output(placeholder("name", "CamelCase")).output(literal(" = new ")).output(placeholder("name", "PascalCase")).output(literal("SlackBot((")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this, ")).output(placeholder("parameter")).output(literal(");\nLogger.info(\"Slack service ")).output(placeholder("name")).output(literal(": started!\");")));
		rules.add(rule().condition(all(allTypes("service", "rest"), trigger("rest"))).output(expression().output(literal("if(")).output(placeholder("parameter")).output(literal(" == null || ")).output(placeholder("parameter")).output(literal(".isEmpty()) return;"))).output(literal("\nio.intino.alexandria.http.AlexandriaSparkBuilder.setup(Integer.parseInt(")).output(placeholder("parameter")).output(literal("), \"www/\");\n")).output(expression().output(placeholder("name", "PascalCase")).output(literal("Service.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), (")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this);"))).output(literal("\n")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();"))).output(literal("\nLogger.info(\"Rest service ")).output(placeholder("name")).output(literal(": started at port \" + ")).output(placeholder("parameter")).output(literal(" + \"!\");")));
		rules.add(rule().condition(all(allTypes("service", "soap"), trigger("soap"))).output(expression().output(placeholder("name", "PascalCase")).output(literal("Service.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), (")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this);"))).output(literal("\n")).output(expression().output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();")).output(literal("\n")).output(literal("Logger.info(\"Soap service ")).output(placeholder("name")).output(literal(": started!\");"))));
		rules.add(rule().condition(all(allTypes("service", "ui"), trigger("ui"))).output(literal("setup")).output(placeholder("name", "firstUpperCase")).output(literal("Ui();\n")).output(expression().output(literal("this.initTranslatorService()"))).output(literal(";\nio.intino.alexandria.ui.UISpark spark")).output(placeholder("name", "firstUpperCase")).output(literal("Instance = (io.intino.alexandria.ui.UISpark) io.intino.alexandria.http.AlexandriaSparkBuilder.instance();\nspark")).output(placeholder("name", "firstUpperCase")).output(literal("Instance.start();\nLogger.info(\"UI ")).output(placeholder("name")).output(literal(": started at port \" + ")).output(placeholder("parameter")).output(literal(" + \"!\");")));
		rules.add(rule().condition(trigger("use")).output(expression().output(placeholder("")).output(literal(".initDisplays(sparkInstance, pushService);"))));
		rules.add(rule().condition(all(allTypes("service", "messaging"), trigger("messaging"))).output(literal("this.")).output(placeholder("name", "CamelCase")).output(literal(" = new ")).output(placeholder("name", "PascalCase")).output(literal("Service(this.connector, (")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this);\nLogger.info(\"Messaging service ")).output(placeholder("name")).output(literal(": started!\");")));
		rules.add(rule().condition(all(allTypes("service", "agenda"), trigger("agenda"))).output(literal("if (this.agenda != null) return;\nthis.agenda = new ")).output(placeholder("name", "PascalCase")).output(literal("Service((")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this);\nthis.agenda.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), scheduler);\nio.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();\nLogger.info(\"Agenda service: started!\");")));
		rules.add(rule().condition(all(allTypes("service", "cli"), trigger("cli"))).output(literal("setupCli();\n")).output(placeholder("name", "PascalCase")).output(literal("Service ")).output(placeholder("name", "CamelCase")).output(literal("Service = new ")).output(placeholder("name", "PascalCase")).output(literal("Service();\n")).output(placeholder("name", "CamelCase")).output(literal("Service.setup(io.intino.alexandria.http.AlexandriaSparkBuilder.instance(), (")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this, new ")).output(placeholder("name", "PascalCase")).output(literal("((")).output(placeholder("configuration", "PascalCase")).output(literal("Box)this));\nio.intino.alexandria.http.AlexandriaSparkBuilder.instance().start();\nLogger.info(\"")).output(placeholder("name", "firstUpperCase")).output(literal(" service: started!\");")));
		rules.add(rule().condition(all(allTypes("sentinel"), trigger("init"))).output(literal("Sentinels.init(this.scheduler, this.configuration.home() ")).output(expression().output(placeholder("hasWebhook")).output(literal(" io.intino.alexandria.http.AlexandriaSparkBuilder.instance()"))).output(literal(", (")).output(placeholder("configuration", "PascalCase")).output(literal("Box) this);")));
		rules.add(rule().condition(all(allTypes("workflow"), trigger("init"))).output(literal("this.workflow = new ")).output(placeholder("package")).output(literal(".bpm.Workflow((")).output(placeholder("box", "PascalCase")).output(literal("Box)this")).output(expression().output(literal(", ")).output(placeholder("parameter"))).output(literal(");")));
		rules.add(rule().condition(all(allTypes("connector"), trigger("init"))).output(literal("if (!(this.connector instanceof io.intino.alexandria.terminal.JmsConnector) || ((io.intino.alexandria.terminal.JmsConnector) this.connector).connection() != null) return;\nfinal io.intino.alexandria.terminal.JmsConnector connector = (io.intino.alexandria.terminal.JmsConnector) this.connector;\nif (connector.connection() == null) connector.start();\nif (configuration().get(\"datahub_url\") != null)\n\twhile (connector.connection() == null) try {\n\t\tThread.sleep(1000 * 30);\n\t\tconnector.start();\n\t} catch (InterruptedException e) {\n\t\tLogger.error(e);\n\t}")));
		rules.add(rule().condition(all(allTypes("terminal"), trigger("init"))).output(literal("if (this.terminal != null) {\n\tjava.time.Instant sealStamp = this.terminal.requestLastSeal();\n\tLogger.info(\"Last seal on \" + sealStamp.toString());\n\tjava.util.function.Predicate<java.time.Instant> filter = i -> !i.isBefore(sealStamp);\n\tthis.terminal.initDatamarts(")).output(expression().output(placeholder("datamartsLoad")).output(literal("()"))).output(literal(");\n\t")).output(placeholder("subscriber").multiple("\n")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("datamartsLoad"), trigger("datamartsourceselector"))).output(literal("protected abstract String ")).output(placeholder("datamartsLoad")).output(literal("();")));
		rules.add(rule().condition(all(allTypes("subscriber", "durable", "filtered"), trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(placeholder("terminal")).output(literal(".")).output(placeholder("eventQn", "FirstUpperCase")).output(literal("Consumer) (e, t) -> new ")).output(placeholder("package")).output(literal(".subscribers.")).output(placeholder("name", "FirstUpperCase")).output(literal("((")).output(placeholder("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e, t), \"")).output(placeholder("subscriberId")).output(literal("\", filter, null);")));
		rules.add(rule().condition(all(allTypes("subscriber", "durable"), trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(placeholder("terminal")).output(literal(".")).output(placeholder("eventQn", "FirstUpperCase")).output(literal("Consumer) (e, t) -> new ")).output(placeholder("package")).output(literal(".subscribers.")).output(placeholder("name", "FirstUpperCase")).output(literal("((")).output(placeholder("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e, t), \"")).output(placeholder("subscriberId")).output(literal("\");")));
		rules.add(rule().condition(all(allTypes("subscriber"), trigger("subscriber"))).output(literal("this.terminal.subscribe((")).output(placeholder("terminal")).output(literal(".")).output(placeholder("eventQn", "FirstUpperCase")).output(literal("Consumer) (e, t) -> new ")).output(placeholder("package")).output(literal(".subscribers.")).output(placeholder("name", "FirstUpperCase")).output(literal("((")).output(placeholder("box", "firstUpperCase")).output(literal("Box) AbstractBox.this).accept(e, t));")));
		rules.add(rule().condition(trigger("split")).output(placeholder("type")).output(literal(".Split.")).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("service", "jmx"), trigger("field"))).output(literal("protected io.intino.alexandria.jmx.JMXServer ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("service", "slack"), trigger("field"))).output(literal("private io.intino.alexandria.slack.Bot ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("service", "messaging"), trigger("field"))).output(literal("private ")).output(placeholder("name", "PascalCase")).output(literal("Service ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("sentinel"), trigger("field"))).output(literal("private io.intino.alexandria.scheduler.AlexandriaScheduler scheduler = new io.intino.alexandria.scheduler.AlexandriaScheduler();")));
		rules.add(rule().condition(all(allTypes("connector"), trigger("field"))).output(literal("protected io.intino.alexandria.terminal.Connector connector;")));
		rules.add(rule().condition(all(allTypes("terminal"), trigger("field"))).output(literal("protected ")).output(placeholder("qn")).output(literal(" terminal;")));
		rules.add(rule().condition(all(allTypes("workflow"), trigger("field"))).output(literal("protected ")).output(placeholder("package")).output(literal(".bpm.Workflow workflow;")));
		rules.add(rule().condition(all(allTypes("agenda"), trigger("field"))).output(literal("protected AgendaService agenda;")));
		rules.add(rule().condition(all(allTypes("service", "messaging"), trigger("getter"))).output(literal("public ")).output(placeholder("name", "PascalCase")).output(literal("Service ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn ")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("terminal"), trigger("getter"))).output(literal("public ")).output(placeholder("qn")).output(literal(" terminal() {\n\treturn this.terminal;\n}\n\nprotected io.intino.alexandria.terminal.Connector datahubConnector() {\n\treturn this.connector;\n}")));
		rules.add(rule().condition(all(allTypes("workflow"), trigger("getter"))).output(literal("public ")).output(placeholder("package")).output(literal(".bpm.Workflow workflow() {\n\treturn this.workflow;\n}\n")));
		rules.add(rule().condition(all(allTypes("agenda"), trigger("getter"))).output(literal("public AgendaService agenda() {\n\treturn this.agenda;\n}")));
		rules.add(rule().condition(all(allTypes("service", "slack"), trigger("getter"))).output(literal("public ")).output(placeholder("name", "PascalCase")).output(literal("SlackBot ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn (")).output(placeholder("name", "PascalCase")).output(literal("SlackBot) ")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("service"), trigger("getter"))));
		rules.add(rule().condition(trigger("spark")).output(literal("io.intino.alexandria.http.AlexandriaSparkBuilder.instance().stop();")));
		rules.add(rule().condition(all(allTypes("connector"), trigger("stop"))).output(literal("if (connector instanceof io.intino.alexandria.terminal.JmsConnector) ((io.intino.alexandria.terminal.JmsConnector) connector).stop();")));
		rules.add(rule().condition(all(allTypes("service", "slack"), trigger("stop"))).output(literal("this.")).output(placeholder("name", "CamelCase")).output(literal(".disconnect();")));
		rules.add(rule().condition(all(allTypes("service"), trigger("stop"))));
		rules.add(rule().condition(allTypes("service")));
		rules.add(rule().condition(all(allTypes("sentinel"), trigger("getter"))).output(literal("public io.intino.alexandria.scheduler.AlexandriaScheduler scheduler() {\n\treturn this.scheduler;\n}")));
		rules.add(rule().condition(trigger("authservice")).output(literal("protected abstract io.intino.alexandria.ui.services.AuthService authService(java.net.URL authServiceUrl);")));
		rules.add(rule().condition(trigger("translatorserviceinitialization")).output(literal("private void initTranslatorService() {\n\ttranslatorService = new io.intino.alexandria.ui.services.TranslatorService();\n\t")).output(placeholder("useDictionaries").multiple("\n")).output(literal("\n\ttranslatorService.addAll(")).output(placeholder("package", "validPackage")).output(literal(".I18n.dictionaries());\n\ttranslatorService.addAll(io.intino.alexandria.I18n.dictionaries());\n}")));
		rules.add(rule().condition(trigger("usedictionaries")).output(literal("translatorService.addAll(")).output(placeholder("")).output(literal(");")));
		rules.add(rule().condition(trigger("pushservice")).output(literal("public PushService pushService() {\n\treturn pushService;\n}")));
		rules.add(rule().condition(trigger("registersoul")).output(literal("public java.util.List<Soul> souls() {\n\treturn new java.util.ArrayList<>(uiSouls.values());\n}\n\npublic java.util.Optional<Soul> soul(String clientId) {\n\treturn java.util.Optional.ofNullable(uiSouls.get(clientId));\n}\n\npublic void registerSoul(String clientId, Soul soul) {\n\t")).output(expression().output(literal("if (owner != null) ((")).output(placeholder("parent")).output(literal("Box) owner).registerSoul(clientId, soul);"))).output(literal("\n\tuiSouls.put(clientId, soul);\n}\n\npublic void unRegisterSoul(String clientId) {\n\t")).output(expression().output(literal("if (owner != null) ((")).output(placeholder("parent")).output(literal("Box) owner).unRegisterSoul(clientId);"))).output(literal("\n\tuiSouls.remove(clientId);\n\tif (uiSouls.size() <= 0) notifySoulsClosed();\n}\n\npublic void onSoulsClosed(io.intino.alexandria.ui.AlexandriaUiBox.SoulsClosed listener) {\n\t")).output(expression().output(literal("if (owner != null) ((")).output(placeholder("parent")).output(literal("Box) owner).onSoulsClosed(listener);"))).output(literal("\n\tthis.soulsClosedListeners.add(listener);\n}\n\nprivate void notifySoulsClosed() {\n\tsoulsClosedListeners.forEach(l -> l.accept());\n}\n")));
		rules.add(rule().condition(all(allTypes("conf", "file"), trigger("additionalparameter"))).output(literal("configuration().get(\"")).output(placeholder("value")).output(literal("\") == null ? null : new java.io.File(configuration().get(\"")).output(placeholder("value")).output(literal("\"))")));
		rules.add(rule().condition(all(allTypes("conf"), trigger("additionalparameter"))).output(literal("configuration().get(\"")).output(placeholder("value")).output(literal("\")")));
		rules.add(rule().condition(all(allTypes("conf", "file"), trigger("parameter"))).output(literal("configuration().get(\"")).output(placeholder("value")).output(literal("\") == null ? null : new java.io.File(configuration().get(\"")).output(placeholder("value")).output(literal("\"))")));
		rules.add(rule().condition(all(allTypes("conf"), trigger("parameter"))).output(literal("configuration().get(\"")).output(placeholder("value")).output(literal("\")")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("parameter"))).output(literal("configuration().get(\"")).output(placeholder("value", "customParameter")).output(literal("\")")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("authentication"))).output(literal("url(configuration().get(\"")).output(placeholder("value", "customParameter")).output(literal("\"))")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("edition"))).output(literal("url(configuration().get(\"")).output(placeholder("value", "customParameter")).output(literal("\"))")));
		rules.add(rule().condition(all(allTypes("file"), trigger("parameter"))).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("int"), trigger("parameter"))).output(placeholder("value")));
		rules.add(rule().condition(trigger("parameter")).output(literal("\"")).output(placeholder("value")).output(literal("\"")));
		rules.add(rule().condition(trigger("authentication")).output(literal("url(\"")).output(placeholder("value")).output(literal("\")")));
		rules.add(rule().condition(trigger("edition")).output(literal("url(\"")).output(placeholder("value")).output(literal("\")")));
		rules.add(rule().condition(trigger("parentinit")));
		rules.add(rule().condition(trigger("hide")));
		rules.add(rule().condition(trigger("hideui")).output(literal(";")));
		rules.add(rule().condition(trigger("hideui")).output(literal(";")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}