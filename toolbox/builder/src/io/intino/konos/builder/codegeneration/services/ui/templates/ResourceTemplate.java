package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ResourceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("resource","accessibleDisplay"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.resources;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".ui.pages.")).output(mark("name", "firstUpperCase")).output(literal("ProxyPage;\nimport io.intino.alexandria.Json;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.exceptions.Unauthorized;\nimport io.intino.alexandria.ui.Soul;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\nimport io.intino.alexandria.ui.services.auth.exceptions.CouldNotObtainInfo;\nimport io.intino.alexandria.ui.services.push.UIClient;\nimport io.intino.alexandria.ui.services.push.UISession;\n\nimport java.util.function.Consumer;\nimport java.util.function.Function;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource extends io.intino.alexandria.ui.spark.resources.ProxyResource {\n\tprivate final ")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.spark.UISparkManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tfillBrowser(manager, session());\n\t\tfillDeviceParameter();\n\t\t")).output(mark("render")).output(literal("\n\t}\n\n\tprivate void render() {\n\t\tUIClient client = client();\n\n\t\t")).output(mark("name", "firstUpperCase")).output(literal("ProxyPage page = new ")).output(mark("name", "firstUpperCase")).output(literal("ProxyPage();\n\t\tpage.session = session();\n\t\tpage.session.browser().onRedirect(location -> manager.redirect(location));\n\t\tpage.session.browser().requestUrl(manager.requestUrl());\n\t\tpage.session.whenLogin(new Function<String, String>() {\n\t\t\t@Override\n\t\t\tpublic String apply(String baseUrl) {\n\t\t\t\treturn ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource.this.authenticate(page.session, baseUrl);\n\t\t\t}\n\t\t});\n\t\tpage.session.whenLogout(b -> logout(page.session));\n\t\tpage.box = box;\n\t\tpage.clientId = client.id();\n\t\tpage.googleApiKey = \"\";\n\t\tpage.device = parameterValue(\"device\");\n\t\t")).output(mark("parameter").multiple("\n")).output(literal("\n        if (!page.hasPermissions()) {\n            manager.redirect(page.redirectUrl());\n            return;\n        }\n\n\t\tSoul soul = soul();\n\t\tif (soul != null) {\n\t\t\tpage.soul = soul;\n\t\t\tpage.execute();\n\t\t\treturn;\n\t\t}\n\n\t\tsoul = new Soul(page.session) {\n\t\t\t@Override\n\t\t\tpublic void personify() {\n\t\t\t\tSoul soul = this;\n\t\t\t\taddRegisterDisplayListener(display -> {\n\t\t\t\t\tdisplay.inject(notifier(page.session, client, display));\n\t\t\t\t\tdisplay.inject(page.session);\n\t\t\t\t\tdisplay.inject(soul);\n\t\t\t\t\tdisplay.inject(() -> soul);\n\t\t\t\t});\n\t\t\t}\n\t\t};\n\t\tclient.soul(soul);\n\t\tclient.cookies(manager.cookies());\n\t\tpage.soul = soul;\n\t\tpage.execute();\n\t\tbox.registerSoul(client.id(), soul);\n\t\tsoul.register(new io.intino.alexandria.ui.displays.DisplayRouter(box).id(\"__router__\"));\n\t\t//((")).output(mark("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher)box.routeManager().routeDispatcher()).dispatch")).output(mark("name", "firstUpperCase")).output(literal("(soul")).output(expression().output(literal(", ")).output(mark("parameter", "dispatch").multiple(", "))).output(literal(");\n\n        manager.pushService().onClose(page.clientId).execute(new Consumer<UIClient>() {\n            @Override\n            public void accept(io.intino.alexandria.ui.services.push.UIClient client) {\n                box.soul(client.id()).ifPresent(s -> s.destroy());\n                box.unRegisterSoul(client.id());\n                manager.unRegister(client);\n            }\n        });\n\n\t\tmanager.write(\"OK\");\n\t}\n}")),
			rule().condition((allTypes("render","confidential"))).output(literal("try {\n    if (!isLogged(accessToken()))\n        throw new Unauthorized(\"user is not logged\");\n\n    authenticate(session(), accessToken());\n    render();\n} catch (CouldNotObtainInfo couldNotObtainInfo) {\n    throw new Unauthorized(\"user is not logged\");\n} catch (Throwable error) {\n    throw new Unauthorized(\"could not render component\");\n}")),
			rule().condition((type("render"))).output(literal("render();")),
			rule().condition((type("resource"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".ui.resources;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".ui.pages.")).output(mark("name", "firstUpperCase")).output(literal("Page;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;\n\nimport java.util.Base64;\nimport java.util.UUID;\nimport java.util.function.Consumer;\nimport java.util.function.Function;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Resource extends io.intino.alexandria.ui.spark.resources.")).output(expression().output(mark("editor"))).output(literal("Resource {\n\tprivate final ")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Resource(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.spark.UISparkManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tsuper.execute();\n\t\tfillDeviceParameter();\n\t\t")).output(expression().output(mark("confidential")).output(literal("\n")).output(literal("if (isLogged()) render();")).output(literal("\n")).output(literal("else authenticate();")).next(expression().output(literal("render();")))).output(literal("\n\t}\n\n\tprivate void render() {\n\t\tString clientId = UUID.randomUUID().toString();\n\t\t")).output(mark("name", "firstUpperCase")).output(literal("Page page = new ")).output(mark("name", "firstUpperCase")).output(literal("Page();\n\t\tpage.session = manager.currentSession();\n\t\tpage.session.browser().onRedirect(location -> manager.redirect(location));\n\t\tpage.session.browser().requestUrl(manager.requestUrl());\n\t\tpage.session.whenLogin(new Function<String, String>() {\n\t\t\t@Override\n\t\t\tpublic String apply(String baseUrl) {\n\t\t\t\treturn ")).output(mark("name", "firstUpperCase")).output(literal("Resource.this.authenticate(page.session, baseUrl);\n\t\t\t}\n\t\t});\n\t\tpage.session.whenLogout(b -> logout(page.session));\n\t\tpage.box = box;\n\t\tpage.clientId = clientId;\n\t\t")).output(expression().output(literal("page.googleApiKey = ")).output(mark("googleApiKey", "format")).output(literal(";"))).output(literal("\n\t\tpage.device = parameterValue(\"device\");\n\t\tpage.token = parameterValue(\"token\");")).output(expression().output(literal("\n")).output(literal("")).output(mark("editor", "parameters"))).output(literal("\n\t\t")).output(mark("parameter").multiple("\n")).output(literal("\n        if (!page.hasPermissions()) {\n            manager.redirect(page.redirectUrl());\n            return;\n        }\n\n\t\tmanager.pushService().onOpen(client -> {\n\t\t\tif (!client.id().equals(page.clientId))\n\t\t\t\treturn false;\n\n\t\t\tif (client.soul() != null) {\n\t\t\t\t((")).output(mark("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher)box.routeManager().routeDispatcher()).dispatch")).output(mark("name", "firstUpperCase")).output(literal("(client.soul()")).output(expression().output(literal(", ")).output(mark("parameter", "dispatch").multiple(", "))).output(literal(");\n\t\t\t\treturn false;\n\t\t\t}\n\n\t\t\tio.intino.alexandria.ui.Soul soul = page.prepareSoul(client);\n\t\t\tsoul.onRedirect((location) -> manager.redirect(location));\n\t\t\tsoul.addRegisterDisplayListener(display -> {\n\t\t\t\tdisplay.inject(notifier(page.session, client, display));\n\t\t\t\tdisplay.inject(page.session);\n\t\t\t\tdisplay.inject(soul);\n\t\t\t\tdisplay.inject(() -> soul);\n\t\t\t});\n\t\t\tclient.soul(soul);\n\t\t\tclient.cookies(manager.cookies());\n\n\t\t\tbox.registerSoul(clientId, soul);\n\t\t\tsoul.register(new io.intino.alexandria.ui.displays.DisplayRouter(box).id(\"__router__\"));\n\t\t\t((")).output(mark("package", "validPackage")).output(literal(".ui.displays.RouteDispatcher)box.routeManager().routeDispatcher()).dispatch")).output(mark("name", "firstUpperCase")).output(literal("(soul")).output(expression().output(literal(", ")).output(mark("parameter", "dispatch").multiple(", "))).output(literal(");\n\n\t\t\treturn true;\n\t\t});\n\n\t\tmanager.pushService().onClose(clientId).execute(new Consumer<io.intino.alexandria.ui.services.push.UIClient>() {\n\t\t\t@Override\n\t\t\tpublic void accept(io.intino.alexandria.ui.services.push.UIClient client) {\n\t\t\t\tbox.soul(client.id()).ifPresent(s -> s.destroy());\n\t\t\t\tbox.unRegisterSoul(client.id());\n\t\t\t\tmanager.unRegister(client);\n\t\t\t}\n\t\t});\n\n\t\tmanager.write(page.execute());\n\t}\n\n}")),
			rule().condition((trigger("parameters"))).output(literal("page.document = loadDocument();\npage.permission = loadPermission();")),
			rule().condition((type("parameter")), (trigger("dispatch"))).output(literal("page.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((type("parameter"))).output(literal("page.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = parameterValue(\"")).output(mark("name")).output(literal("\");")),
			rule().condition((type("googleApiKey")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("")).output(literal("}\", box.configuration().get(\"")).output(mark("")).output(literal("\"))"))
		);
	}
}