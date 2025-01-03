package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class ServiceTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("ui", "accessibleAccessor")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui;\n\nimport io.intino.alexandria.ui.AlexandriaUiServer;\nimport io.intino.alexandria.ui.services.push.PushService;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Service extends io.intino.alexandria.ui.UI {\n\n\tpublic static void initDisplays(AlexandriaUiServer server, PushService pushService) {\n\t}\n\n}")));
		rules.add(rule().condition(allTypes("ui")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".ui;\n")).output(expression().output(literal("import ")).output(placeholder("displaysImport", "validPackage")).output(literal(".ui.displays.*;"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("notifiersImport", "validPackage")).output(literal(".ui.displays.notifiers.*;"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("requestersImport", "validPackage")).output(literal(".ui.displays.requesters.*;"))).output(literal("\nimport ")).output(placeholder("package", "validPackage")).output(literal(".ui.resources.*;\n")).output(expression().output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;"))).output(literal("\n")).output(expression().output(literal("import ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Configuration;"))).output(literal("\n\nimport io.intino.alexandria.ui.AlexandriaUiServer;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\nimport io.intino.alexandria.ui.displays.DisplayRouteDispatcher;\nimport io.intino.alexandria.ui.resources.AssetResourceLoader;\nimport io.intino.alexandria.ui.services.push.PushService;\nimport io.intino.alexandria.ui.server.resources.AfterDisplayRequest;\nimport io.intino.alexandria.ui.server.resources.AssetResource;\nimport io.intino.alexandria.ui.server.resources.AuthenticateCallbackResource;\nimport io.intino.alexandria.ui.server.resources.BeforeDisplayRequest;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal("Service extends io.intino.alexandria.ui.UI {\n\n\tpublic static void init(AlexandriaUiServer server, ")).output(placeholder("box", "PascalCase")).output(literal("Box box, PushService pushService, DisplayRouteDispatcher routeDispatcher) {\n\t\t")).output(placeholder("box", "PascalCase")).output(literal("Configuration configuration = (")).output(placeholder("box", "PascalCase")).output(literal("Configuration) box.configuration();\n\t\tbox.routeManager(routeManager(server, routeDispatcher));\n        server.route(\"/_alexandria/push\").register(pushService);\n        server.route(\"/authenticate-callback\").get(manager -> new AuthenticateCallbackResource(manager, notifierProvider()).execute());\n        server.route(\"/authenticate-callback/\").get(manager -> new AuthenticateCallbackResource(manager, notifierProvider()).execute());\n        server.route(\"/asset/:name\").get(manager -> new AssetResource(name -> new AssetResourceLoader(box).load(name), manager, notifierProvider()).execute());\n\t\t")).output(expression().output(placeholder("userHome"))).output(literal("\n\t\t")).output(expression().output(placeholder("resource", "path").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("display", "path").multiple("\n"))).output(literal("\n\t\tinitDisplays(server, pushService);\n\t}\n\n\tpublic static void initDisplays(AlexandriaUiServer server, PushService pushService) {\n\t\t")).output(placeholder("display", "init").multiple("\n")).output(literal("\n\t\tregisterNotifiers();\n\t}\n\n\tprivate static void registerNotifiers() {\n\t\t")).output(placeholder("display", "notifier").multiple("\n")).output(literal("\n\t}\n\n\t")).output(placeholder("display", "method").multiple("\n")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("resource"), trigger("path"))).output(placeholder("path").multiple("\n")));
		rules.add(rule().condition(trigger("userhome")).output(literal("server.route(\"/alexandria/user\").get(manager -> new ")).output(placeholder("", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());")));
		rules.add(rule().condition(all(allTypes("display", "accessible"), trigger("path"))).output(literal("server.route(\"/")).output(placeholder("name", "lowercase")).output(literal("proxy/:displayId/personify\").post(manager -> new ")).output(placeholder("name", "firstUpperCase")).output(literal("ProxyResource(box, manager, notifierProvider()).execute());")));
		rules.add(rule().condition(all(allTypes("display"), trigger("path"))));
		rules.add(rule().condition(allTypes("path", "editor")).output(literal("server.route(\"")).output(placeholder("value")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))).output(literal(").get(manager -> new ")).output(placeholder("name", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());\nserver.route(\"")).output(placeholder("value")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))).output(literal(").post(manager -> new ")).output(placeholder("name", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());")));
		rules.add(rule().condition(allTypes("path")).output(literal("server.route(\"")).output(placeholder("value")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))).output(literal(").get(manager -> new ")).output(placeholder("name", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());")));
		rules.add(rule().condition(all(allTypes("display", "proxy"), trigger("notifier"))).output(literal("register(")).output(placeholder("name", "firstUpperCase")).output(literal("ProxyNotifier.class).forDisplay(")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("type")).output(literal("s."))).output(placeholder("name", "firstUpperCase")).output(literal("Proxy.class);")));
		rules.add(rule().condition(all(allTypes("display", "genericNotifier"), trigger("notifier"))).output(literal("register(io.intino.alexandria.ui.displays.notifiers.")).output(placeholder("generic", "firstUpperCase")).output(literal("Notifier.class).forDisplay(")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("type")).output(literal("s."))).output(placeholder("name", "firstUpperCase")).output(literal(".class);\n")).output(expression().output(placeholder("display", "notifier"))));
		rules.add(rule().condition(all(allTypes("display"), trigger("notifier"))).output(literal("register(")).output(placeholder("name", "firstUpperCase")).output(literal("Notifier.class).forDisplay(")).output(placeholder("package", "validPackage")).output(literal(".ui.displays.")).output(expression().output(placeholder("type")).output(literal("s."))).output(placeholder("name", "firstUpperCase")).output(literal(".class);\n")).output(expression().output(placeholder("display", "notifier"))));
		rules.add(rule().condition(trigger("custom")).output(literal(".replace(\"{")).output(placeholder("")).output(literal("}\", configuration.get(\"")).output(placeholder("")).output(literal("\"))")));
		rules.add(rule().condition(all(allTypes("display", "proxy"), trigger("proxy"))).output(literal("server.route(\"/")).output(placeholder("name", "lowercase")).output(literal("proxy/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());\nserver.route(\"/")).output(placeholder("name", "lowercase")).output(literal("proxy/:displayId\").post(manager -> new ")).output(placeholder("name", "firstUpperCase")).output(literal("ProxyRequester(manager, notifierProvider()).execute());\nserver.route(\"/")).output(placeholder("name", "lowercase")).output(literal("proxy/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());")));
		rules.add(rule().condition(all(allTypes("display"), trigger("init"))).output(literal("init")).output(placeholder("name", "firstUpperCase")).output(literal("(server, pushService);")));
		rules.add(rule().condition(all(allTypes("display"), trigger("method"))).output(literal("private static void init")).output(placeholder("name", "firstUpperCase")).output(literal("(AlexandriaUiServer server, PushService pushService) {\n\tserver.route(\"/")).output(placeholder("name", "lowercase")).output(literal("/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());\n\tserver.route(\"/")).output(placeholder("name", "lowercase")).output(literal("/:displayId\").post(manager -> new ")).output(placeholder("requesterType")).output(literal("Requester(manager, notifierProvider()).execute());\n\t")).output(expression().output(placeholder("asset"))).output(literal("\n\tserver.route(\"/")).output(placeholder("name", "lowercase")).output(literal("/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());\n\tpushService.register(\"")).output(placeholder("name", "lowercase")).output(literal("\", new ")).output(placeholder("requesterType")).output(literal("PushRequester());\n\t")).output(placeholder("display", "proxy")).output(literal("\n}")));
		rules.add(rule().condition(allTypes("display")));
		rules.add(rule().condition(trigger("asset")).output(literal("server.route(\"/")).output(placeholder("", "lowercase")).output(literal("/:displayId\").get(manager -> new ")).output(placeholder("", "firstUppercase")).output(literal("Requester(manager, notifierProvider()).execute());")));
		rules.add(rule().condition(trigger("quoted")).output(literal("\"")).output(placeholder("")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("custom"))).output(literal(".replace(\"{")).output(placeholder("")).output(literal("}\", configuration.get(\"")).output(placeholder("")).output(literal("\"))")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}