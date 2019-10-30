package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class UIServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("ui"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n")).output(expression().output(literal("import ")).output(mark("dialogsImport", "validPackage")).output(literal(".dialogs.*;"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("displaysImport", "validPackage")).output(literal(".displays.*;"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("displaysImport", "validPackage")).output(literal(".displays.notifiers.*;"))).output(literal("\n")).output(expression().output(literal("import ")).output(mark("displaysImport", "validPackage")).output(literal(".displays.requesters.*;"))).output(literal("\nimport ")).output(mark("package", "validPackage")).output(literal(".resources.*;\n\nimport io.intino.alexandria.ui.UIAlexandriaSpark;\nimport io.intino.alexandria.ui.AssetResourceLoader;\nimport io.intino.alexandria.ui.displays.AlexandriaDisplayNotifier;\nimport io.intino.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;\nimport io.intino.alexandria.ui.services.push.PushService;\nimport io.intino.alexandria.ui.spark.resources.AfterDisplayRequest;\nimport io.intino.alexandria.ui.spark.resources.AssetResource;\nimport io.intino.alexandria.ui.spark.resources.AuthenticateCallbackResource;\nimport io.intino.alexandria.ui.spark.resources.BeforeDisplayRequest;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\n\npublic class ")).output(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Service extends io.intino.alexandria.ui.UI {\n\n\tpublic static void init(UIAlexandriaSpark spark, ")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Box box) {\n\t\t")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration configuration = (")).output(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).output(literal("Configuration) box.configuration();\n\t\tbox.routeManager(routeManager(spark));\n\t\tspark.route(\"/push\").push(new PushService());\n\t\tspark.route(\"/authenticate-callback\").get(manager -> new AuthenticateCallbackResource(manager, notifierProvider()).execute());\n\t\tspark.route(\"/asset/:name\").get(manager -> new AssetResource(name -> new AssetResourceLoader(box).load(name), manager, notifierProvider()).execute());\n\t\t")).output(expression().output(mark("userHome"))).output(literal("\n\t\t")).output(expression().output(mark("resource", "path").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("display", "path").multiple("\n"))).output(literal("\n\t\tinitDisplays(spark);\n\t}\n\n\tpublic static void initDisplays(UIAlexandriaSpark spark) {\n\t\t")).output(mark("display").multiple("\n")).output(literal("\n\t\t")).output(mark("dialog").multiple("\n")).output(literal("\n\t\tregisterNotifiers();\n\t}\n\n\tprivate static void registerNotifiers() {\n\t\t")).output(mark("display", "notifier").multiple("\n")).output(literal("\n\t\t")).output(mark("dialog", "notifier").multiple("\n")).output(literal("\n\t}\n}")),
			rule().condition((type("resource")), (trigger("path"))).output(mark("path").multiple("\n")),
			rule().condition((trigger("userhome"))).output(literal("spark.route(\"/alexandria/user\").get(manager -> new ")).output(mark("", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());")),
			rule().condition((allTypes("display","accessible")), (trigger("path"))).output(literal("spark.route(\"/")).output(mark("name", "lowercase")).output(literal("proxy/:displayId/personify\").post(manager -> new ")).output(mark("name", "firstUpperCase")).output(literal("ProxyResource(box, manager, notifierProvider()).execute()); //AAA")),
			rule().condition((type("display")), (trigger("path"))),
			rule().condition((allTypes("path","editor"))).output(literal("spark.route(\"")).output(mark("value")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))).output(literal(").get(manager -> new ")).output(mark("name", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());\nspark.route(\"")).output(mark("value")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))).output(literal(").post(manager -> new ")).output(mark("name", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());")),
			rule().condition((type("path"))).output(literal("spark.route(\"")).output(mark("value")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))).output(literal(").get(manager -> new ")).output(mark("name", "firstUpperCase")).output(literal("Resource(box, manager, notifierProvider()).execute());")),
			rule().condition((allTypes("display","proxy")), (trigger("notifier"))).output(literal("register(")).output(mark("name", "firstUpperCase")).output(literal("ProxyNotifier.class).forDisplay(")).output(mark("package", "validPackage")).output(literal(".displays.")).output(mark("name", "firstUpperCase")).output(literal("Proxy.class);")),
			rule().condition((type("display")), (trigger("notifier"))).output(literal("register(")).output(mark("name", "firstUpperCase")).output(literal("Notifier.class).forDisplay(")).output(mark("package", "validPackage")).output(literal(".displays.")).output(mark("name", "firstUpperCase")).output(literal(".class);\n")).output(expression().output(mark("display", "notifier"))),
			rule().condition((type("dialog")), (trigger("notifier"))).output(literal("register(io.intino.alexandria.ui.displays.AlexandriaDialogNotifier.class).forDisplay(")).output(mark("package", "validPackage")).output(literal(".dialogs.")).output(mark("name", "firstUpperCase")).output(literal(".class);")),
			rule().condition((trigger("custom"))).output(literal(".replace(\"{")).output(mark("")).output(literal("}\", configuration.get(\"")).output(mark("")).output(literal("\"))")),
			rule().condition((allTypes("display","proxy")), (trigger("proxy"))).output(literal("spark.route(\"/")).output(mark("name", "lowercase")).output(literal("proxy/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());//CCC\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("proxy/:displayId\").post(manager -> new ")).output(mark("name", "firstUpperCase")).output(literal("ProxyRequester(manager, notifierProvider()).execute());\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("proxy/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());")),
			rule().condition((type("display"))).output(literal("spark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());//BBB\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").post(manager -> new ")).output(mark("name", "firstUppercase")).output(literal("Requester(manager, notifierProvider()).execute());\n")).output(expression().output(mark("asset"))).output(literal("\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());\n")).output(mark("display", "proxy")),
			rule().condition((type("dialog"))).output(literal("spark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").post(manager -> new io.intino.alexandria.ui.displays.AlexandriaDialogRequester(manager, notifierProvider()).execute());\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").get(manager -> new io.intino.alexandria.ui.displays.AlexandriaDialogRequester(manager, notifierProvider()).execute());\nspark.route(\"/")).output(mark("name", "lowercase")).output(literal("/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());")),
			rule().condition((trigger("asset"))).output(literal("spark.route(\"/")).output(mark("", "lowercase")).output(literal("/:displayId\").get(manager -> new ")).output(mark("", "firstUppercase")).output(literal("Requester(manager, notifierProvider()).execute());")),
			rule().condition((trigger("quoted"))).output(literal("\"")).output(mark("value")).output(literal("\"")),
			rule().condition((type("custom")), (trigger("custom"))).output(literal(".replace(\"{")).output(mark("value")).output(literal("}\", configuration.get(\"")).output(mark("value")).output(literal("\"))"))
		);
	}
}