package io.intino.konos.builder.codegeneration.server.activity.web;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ResourceTemplate extends Template {

	protected ResourceTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ResourceTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "page"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".resources;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action;\nimport io.intino.konos.exceptions.KonosException;\nimport io.intino.konos.server.activity.displays.DisplayNotifierProvider;\n\nimport java.util.UUID;\nimport java.util.function.Consumer;\nimport java.util.function.Function;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Resource extends io.intino.konos.server.activity.spark.resources.Resource {\n\tprivate final ")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Resource(")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box, io.intino.konos.server.activity.spark.ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void execute() throws KonosException {\n\t\tsuper.execute();\n\t\t")).add(expression().add(mark("restrict")).add(literal("\n")).add(literal("\t\tif (isLogged()) render();")).add(literal("\n")).add(literal("\t\telse authenticate();")).or(expression().add(literal("render();")))).add(literal("\n\t}\n\n\tprivate void render() {\n\t\tString clientId = UUID.randomUUID().toString();\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Action ")).add(mark("name", "firstLowercase")).add(literal("Action = new ")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\t")).add(mark("name", "firstLowercase")).add(literal("Action.session = manager.currentSession();\n\t\t")).add(mark("name", "firstLowercase")).add(literal("Action.session.whenLogin(new Function<String, String>() {\n\t\t\t@Override\n\t\t\tpublic String apply(String baseUrl) {\n\t\t\t\treturn ")).add(mark("name", "firstUpperCase")).add(literal("Resource.this.authenticate(baseUrl);\n\t\t\t}\n\t\t});\n\t\t")).add(mark("name", "firstLowercase")).add(literal("Action.session.whenLogout(b -> logout());\n\t\t")).add(mark("name", "firstLowercase")).add(literal("Action.box = box;\n\t\t")).add(mark("name", "firstLowercase")).add(literal("Action.clientId = clientId;\n\t\tmanager.pushService().onOpen(client -> {\n\t\t\tif (client.soul() != null)\n\t\t\t\treturn;\n\n\t\t\tio.intino.konos.server.activity.displays.Soul soul = ")).add(mark("name", "firstLowerCase")).add(literal("Action.prepareSoul(client);\n\t\t\tsoul.addRegisterDisplayListener(display -> {\n\t\t\t\tdisplay.inject(notifier(")).add(mark("name", "firstLowerCase")).add(literal("Action.session, client, display));\n\t\t\t\tdisplay.inject(")).add(mark("name", "firstLowerCase")).add(literal("Action.session);\n\t\t\t\tdisplay.inject(soul);\n\t\t\t\tdisplay.inject(() -> soul);\n\t\t\t});\n\t\t\tclient.soul(soul);\n\t\t});\n\n\t\tmanager.pushService().onClose(clientId).execute(new Consumer<io.intino.konos.server.activity.services.push.ActivityClient>() {\n\t\t\t@Override\n\t\t\tpublic void accept(io.intino.konos.server.activity.services.push.ActivityClient client) {\n\t\t\t\tmanager.unRegister(client);\n\t\t\t}\n\t\t});\n\n\t\tmanager.write(")).add(mark("name", "firstLowerCase")).add(literal("Action.execute());\n\t}\n\n}"))
		);
		return this;
	}
}