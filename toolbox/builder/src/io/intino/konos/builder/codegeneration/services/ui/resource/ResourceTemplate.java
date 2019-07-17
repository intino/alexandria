package io.intino.konos.builder.codegeneration.services.ui.resource;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ResourceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("accessibledisplay", "resource"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".resources;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("ProxyAction;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.exceptions.Unauthorized;\nimport io.intino.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;\nimport io.intino.alexandria.ui.displays.Soul;\nimport io.intino.alexandria.ui.services.auth.exceptions.CouldNotObtainInfo;\nimport io.intino.alexandria.ui.services.push.UIClient;\nimport io.intino.alexandria.ui.services.push.UISession;\n\nimport java.util.function.Function;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource extends io.intino.alexandria.ui.spark.resources.ProxyResource {\n\tprivate final ")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.spark.UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tfillBrowser(manager, session());\n\t\tfillDeviceParameter();\n\t\ttry {\n\t\t\tif (!isLogged(accessToken()))\n\t\t\t\tthrow new Unauthorized(\"user is not logged\");\n\n\t\t\tauthenticate(session(), accessToken());\n\t\t\trender();\n\t\t} catch (CouldNotObtainInfo couldNotObtainInfo) {\n\t\t\tthrow new Unauthorized(\"user is not logged\");\n\t\t} catch (Throwable error) {\n\t\t\tthrow new Unauthorized(\"could not render component\");\n\t\t}\n\t}\n\n\tprivate void render() {\n\t\tUIClient client = client();\n\n\t\t")).output(mark("name", "firstUpperCase")).output(literal("ProxyAction action = new ")).output(mark("name", "firstUpperCase")).output(literal("ProxyAction();\n\t\taction.session = session();\n\t\taction.session.browser().requestUrl(manager.requestUrl());\n\t\taction.session.whenLogin(new Function<String, String>() {\n\t\t\t@Override\n\t\t\tpublic String apply(String baseUrl) {\n\t\t\t\treturn ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource.this.authenticate(baseUrl);\n\t\t\t}\n\t\t});\n\t\taction.session.whenLogout(b -> logout());\n\t\taction.box = box;\n\t\taction.clientId = client.id();\n\t\taction.googleApiKey = \"\";\n\t\taction.device = parameterValue(\"device\");\n\t\t")).output(mark("parameter").multiple("\n")).output(literal("\n\n\t\tSoul soul = soul();\n\t\tif (soul != null) {\n\t\t\taction.soul = soul;\n\t\t\taction.execute();\n\t\t\treturn;\n\t\t}\n\n\t\tsoul = new Soul(action.session) {\n\t\t\t@Override\n\t\t\tpublic void personify() {\n\t\t\t\tSoul soul = this;\n\t\t\t\taddRegisterDisplayListener(display -> {\n\t\t\t\t\tdisplay.inject(notifier(action.session, client, display));\n\t\t\t\t\tdisplay.inject(action.session);\n\t\t\t\t\tdisplay.inject(soul);\n\t\t\t\t\tdisplay.inject(() -> soul);\n\t\t\t\t});\n\t\t\t}\n\t\t};\n\t\tclient.soul(soul);\n\t\taction.soul = soul;\n\t\taction.execute();\n\t\tbox.registerSoul(client.id(), soul);\n\n\t\tmanager.write(\"OK\");\n\t}\n}")),
				rule().condition((type("resource"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".resources;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action;\nimport io.intino.alexandria.exceptions.AlexandriaException;\nimport io.intino.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;\n\nimport java.util.Base64;\nimport java.util.UUID;\nimport java.util.function.Consumer;\nimport java.util.function.Function;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Resource extends io.intino.alexandria.ui.spark.resources.")).output(expression().output(mark("editor"))).output(literal("Resource {\n\tprivate final ")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Resource(")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box, io.intino.alexandria.ui.spark.UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tsuper.execute();\n\t\tfillDeviceParameter();\n\t\t")).output(expression().output(mark("confidential")).output(literal("\n")).output(literal("if (isLogged()) render();")).output(literal("\n")).output(literal("else authenticate();")).next(expression().output(literal("render();")))).output(literal("\n\t}\n\n\tprivate void render() {\n\t\tString clientId = UUID.randomUUID().toString();\n\t\t")).output(mark("name", "firstUpperCase")).output(literal("Action action = new ")).output(mark("name", "firstUpperCase")).output(literal("Action();\n\t\taction.session = manager.currentSession();\n\t\taction.session.browser().requestUrl(manager.requestUrl());\n\t\taction.session.whenLogin(")).output(mark("name", "firstUpperCase")).output(literal("Resource.this::authenticate);\n\t\taction.session.whenLogout(b -> logout());\n\t\taction.box = box;\n\t\taction.clientId = clientId;\n\t\t")).output(expression().output(literal("action.googleApiKey = ")).output(mark("googleApiKey", "format")).output(literal(";"))).output(literal("\n\t\taction.device = parameterValue(\"device\");\n\t\taction.token = parameterValue(\"token\");\n\t\t")).output(expression().output(mark("editor", "parameters"))).output(literal("\n\t\t")).output(mark("parameter").multiple("\n")).output(literal("\n\t\tmanager.pushService().onOpen(client -> {\n\t\t\tif (!client.id().equals(action.clientId))\n\t\t\t\treturn false;\n\n\t\t\tif (client.soul() != null)\n\t\t\t\treturn false;\n\n\t\t\tio.intino.alexandria.ui.displays.Soul soul = action.prepareSoul(client);\n\t\t\tsoul.onRedirect((location) -> manager.redirect(location));\n\t\t\tsoul.addRegisterDisplayListener(display -> {\n\t\t\t\tdisplay.inject(notifier(action.session, client, display));\n\t\t\t\tdisplay.inject(action.session);\n\t\t\t\tdisplay.inject(soul);\n\t\t\t\tdisplay.inject(() -> soul);\n\t\t\t});\n\t\t\tclient.soul(soul);\n\n\t\t\tbox.registerSoul(clientId, soul);\n\n\t\t\treturn true;\n\t\t});\n\n\t\tmanager.pushService().onClose(clientId).execute(new Consumer<io.intino.alexandria.ui.services.push.UIClient>() {\n\t\t\t@Override\n\t\t\tpublic void accept(io.intino.alexandria.ui.services.push.UIClient client) {\n\t\t\t\tbox.soul(client.id()).ifPresent(s -> s.destroy());\n\t\t\t\tbox.unRegisterSoul(client.id());\n\t\t\t\tmanager.unRegister(client);\n\t\t\t}\n\t\t});\n\n\t\tmanager.write(action.execute());\n\t}\n\n}")),
				rule().condition((trigger("parameters"))).output(literal("action.document = loadDocument();\naction.permission = loadPermission();")),
				rule().condition((type("parameter"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = parameterValue(\"")).output(mark("name")).output(literal("\");")),
				rule().condition((type("googleapikey")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
				rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", box.configuration().get(\"")).output(mark("value")).output(literal("\"))"))
		);
	}
}