package io.intino.konos.builder.codegeneration.services.activity;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class ActivityTemplate extends Template {

	protected ActivityTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new ActivityTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "activity"))).add(literal("package ")).add(mark("package", "validPackage")).add(literal(";\n")).add(expression().add(literal("import ")).add(mark("dialogsImport", "validPackage")).add(literal(".dialogs.*;"))).add(literal("\n")).add(expression().add(literal("import ")).add(mark("displaysImport", "validPackage")).add(literal(".displays.*;"))).add(literal("\n")).add(expression().add(literal("import ")).add(mark("displaysImport", "validPackage")).add(literal(".displays.notifiers.*;"))).add(literal("\n")).add(expression().add(literal("import ")).add(mark("displaysImport", "validPackage")).add(literal(".displays.requesters.*;"))).add(literal("\nimport ")).add(mark("package", "validPackage")).add(literal(".resources.*;\n\nimport io.intino.konos.alexandria.activity.ActivityAlexandriaSpark;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier;\nimport io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;\nimport io.intino.konos.alexandria.activity.services.push.PushService;\nimport io.intino.konos.alexandria.activity.spark.resources.AfterDisplayRequest;\nimport io.intino.konos.alexandria.activity.spark.resources.AssetResource;\nimport io.intino.konos.alexandria.activity.spark.resources.AuthenticateCallbackResource;\nimport io.intino.konos.alexandria.activity.spark.resources.BeforeDisplayRequest;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\n\npublic class ")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Activity extends io.intino.konos.alexandria.activity.Activity {\n\n\tpublic static void init(ActivityAlexandriaSpark spark, ")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Box box) {\n\t\t")).add(mark("box", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("Configuration.")).add(mark("name", "SnakeCaseToCamelCase", "FirstUpperCase")).add(literal("ActivityConfiguration configuration = box.configuration().")).add(mark("name", "SnakeCaseToCamelCase", "FirstLowerCase")).add(literal("Configuration;\n\t\tspark.route(\"/push\").push(new PushService());\n\t\tspark.route(\"/authenticate-callback\").get(manager -> new AuthenticateCallbackResource(manager, notifierProvider()).execute());\n\t\tspark.route(\"/asset/:name\").get(manager -> new AssetResource(name -> new AssetResourceLoader(box).load(name), manager, notifierProvider()).execute());\n\t\t")).add(mark("userHome")).add(literal("\n\t\t")).add(mark("resource").multiple("\n")).add(literal("\n\t\tinitDisplays(spark);\n\t}\n\n\tpublic static void initDisplays(ActivityAlexandriaSpark spark) {\n\t\t")).add(mark("display").multiple("\n")).add(literal("\n\t\t")).add(mark("dialog").multiple("\n")).add(literal("\n\t\tregisterNotifiers();\n\t}\n\n\tprivate static void registerNotifiers() {\n\t\t")).add(mark("display", "notifier").multiple("\n")).add(literal("\n\t\t")).add(mark("dialog", "notifier").multiple("\n")).add(literal("\n\t}\n}")),
			rule().add((condition("type", "abstractPage")), (condition("trigger", "resource"))).add(mark("path").multiple("\n")),
			rule().add((condition("trigger", "userHome"))).add(literal("spark.route(\"/alexandria/user\").get(manager -> new ")).add(mark("value", "firstUpperCase")).add(literal("Resource(box, manager, notifierProvider()).execute());")),
			rule().add((condition("trigger", "path"))).add(literal("spark.route(\"")).add(mark("value")).add(literal("\"")).add(expression().add(mark("custom").multiple(""))).add(literal(").get(manager -> new ")).add(mark("name", "firstUpperCase")).add(literal("Resource(box, manager, notifierProvider()).execute());")),
			rule().add((condition("type", "display")), (condition("trigger", "notifier"))).add(literal("register(")).add(mark("name", "firstUpperCase")).add(literal("Notifier.class).forDisplay(")).add(mark("name", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("type", "dialog")), (condition("trigger", "notifier"))).add(literal("register(io.intino.konos.alexandria.activity.displays.AlexandriaDialogNotifier.class).forDisplay(")).add(mark("name", "firstUpperCase")).add(literal(".class);")),
			rule().add((condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration.")).add(mark("value")).add(literal(")")),
			rule().add((condition("type", "display"))).add(literal("spark.route(\"/")).add(mark("name", "lowercase")).add(literal("/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());\nspark.route(\"/")).add(mark("name", "lowercase")).add(literal("/:displayId\").post(manager -> new ")).add(mark("name", "firstUppercase")).add(literal("Requester(manager, notifierProvider()).execute());\n")).add(mark("asset")).add(literal("\nspark.route(\"/")).add(mark("name", "lowercase")).add(literal("/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());")),
			rule().add((condition("type", "dialog"))).add(literal("spark.route(\"/")).add(mark("name", "lowercase")).add(literal("dialog/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());\nspark.route(\"/")).add(mark("name", "lowercase")).add(literal("dialog/:displayId\").post(manager -> new io.intino.konos.alexandria.activity.displays.AlexandriaDialogRequester(manager, notifierProvider()).execute());\nspark.route(\"/")).add(mark("name", "lowercase")).add(literal("dialog/:displayId\").get(manager -> new io.intino.konos.alexandria.activity.displays.AlexandriaDialogRequester(manager, notifierProvider()).execute());\nspark.route(\"/")).add(mark("name", "lowercase")).add(literal("dialog/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());")),
			rule().add((condition("trigger", "asset"))).add(literal("spark.route(\"/")).add(mark("value", "lowercase")).add(literal("/:displayId\").get(manager -> new ")).add(mark("value", "firstUppercase")).add(literal("Requester(manager, notifierProvider()).execute());")),
			rule().add((condition("trigger", "quoted"))).add(literal("\"")).add(mark("value")).add(literal("\"")),
			rule().add((condition("type", "custom")), (condition("trigger", "custom"))).add(literal(".replace(\"{")).add(mark("value")).add(literal("}\", configuration.")).add(mark("value")).add(literal(")"))
		);
		return this;
	}
}