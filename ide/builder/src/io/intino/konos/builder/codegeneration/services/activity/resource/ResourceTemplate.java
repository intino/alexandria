package io.intino.konos.builder.codegeneration.services.activity.resource;

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
			rule().add((condition("type", "page"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(".resources;\n\nimport ")).add(mark("package", "validPackage")).add(literal(".")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box;\nimport ")).add(mark("package", "validPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action;\nimport io.intino.konos.alexandria.exceptions.AlexandriaException;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;\n\nimport java.util.Base64;\nimport java.util.UUID;\nimport java.util.function.Consumer;\nimport java.util.function.Function;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Resource extends io.intino.konos.alexandria.activity.spark.resources.Resource {\n\tprivate final ")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box;\n\n\tpublic ")).add(mark("name", "firstUpperCase")).add(literal("Resource(")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box, io.intino.konos.alexandria.activity.spark.ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {\n\t\tsuper(manager, notifierProvider);\n\t\tthis.box = box;\n\t}\n\n\t@Override\n\tpublic void execute() throws AlexandriaException {\n\t\tsuper.execute();\n\t\t")).add(expression().add(mark("restrict")).add(literal("\n")).add(literal("\t\tif (isLogged()) render();")).add(literal("\n")).add(literal("\t\telse authenticate();")).or(expression().add(literal("render();")))).add(literal("\n\t}\n\n\tprivate void render() {\n\t\tString clientId = UUID.randomUUID().toString();\n\t\t")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\taction.session = manager.currentSession();\n\t\taction.session.whenLogin(new Function<String, String>() {\n\t\t\t@Override\n\t\t\tpublic String apply(String baseUrl) {\n\t\t\t\treturn ")).add(mark("name", "firstUpperCase")).add(literal("Resource.this.authenticate(baseUrl);\n\t\t\t}\n\t\t});\n\t\taction.session.whenLogout(b -> logout());\n\t\taction.box = box;\n\t\taction.clientId = clientId;\n\t\taction.googleApiKey = \"")).add(mark("googleApiKey")).add(literal("\";\n\t\t")).add(mark("parameter").multiple("\n")).add(literal("\n\t\tmanager.pushService().onOpen(client -> {\n\t\t\tif (!client.id().equals(action.clientId))\n\t\t\t\treturn false;\n\n\t\t\tif (client.soul() != null)\n\t\t\t\treturn false;\n\n\t\t\tio.intino.konos.alexandria.activity.displays.Soul soul = action.prepareSoul(client);\n\t\t\tsoul.addRegisterDisplayListener(display -> {\n\t\t\t\tdisplay.inject(notifier(action.session, client, display));\n\t\t\t\tdisplay.inject(action.session);\n\t\t\t\tdisplay.inject(soul);\n\t\t\t\tdisplay.inject(() -> soul);\n\t\t\t});\n\t\t\tclient.soul(soul);\n\t\t\tbox.registerSoul(clientId, soul);\n\n\t\t\treturn true;\n\t\t});\n\n\t\tmanager.pushService().onClose(clientId).execute(new Consumer<io.intino.konos.alexandria.activity.services.push.ActivityClient>() {\n\t\t\t@Override\n\t\t\tpublic void accept(io.intino.konos.alexandria.activity.services.push.ActivityClient client) {\n\t\t\t\tbox.soul(client.id()).ifPresent(s -> s.destroy());\n\t\t\t\tbox.unRegisterSoul(client.id());\n\t\t\t\tmanager.unRegister(client);\n\t\t\t}\n\t\t});\n\n\t\tmanager.write(action.execute());\n\t}\n\n}")),
			rule().add((condition("type", "parameter"))).add(literal("action.")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal(" = parameterValue(\"")).add(mark("name")).add(literal("\");"))
		);
		return this;
	}
}