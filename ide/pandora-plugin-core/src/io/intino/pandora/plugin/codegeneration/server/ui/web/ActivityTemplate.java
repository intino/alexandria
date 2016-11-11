package io.intino.pandora.plugin.codegeneration.server.ui.web;

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
			rule().add((condition("type", "activity"))).add(literal("package ")).add(mark("package")).add(literal(";\n\nimport ")).add(mark("package")).add(literal(".displays.notifiers.*;\nimport ")).add(mark("package")).add(literal(".displays.requesters.*;\nimport ")).add(mark("package")).add(literal(".resources.*;\n\nimport io.intino.pandora.server.activity.ActivityPandoraSpark;\nimport io.intino.pandora.server.activity.displays.DisplayNotifier;\nimport io.intino.pandora.server.activity.displays.DisplayNotifierProvider;\nimport io.intino.pandora.server.activity.services.push.PushService;\nimport io.intino.pandora.server.activity.spark.resources.AfterDisplayRequest;\nimport io.intino.pandora.server.activity.spark.resources.AssetResource;\nimport io.intino.pandora.server.activity.spark.resources.AuthenticateCallbackResource;\nimport io.intino.pandora.server.activity.spark.resources.BeforeDisplayRequest;\n\nimport java.net.MalformedURLException;\nimport java.net.URL;\n\npublic class UIActivity {\n\n    public static void init(ActivityPandoraSpark spark, ")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n        spark.route(\"/push\").push(new PushService());\n        spark.route(\"/authenticate-callback\").get(manager -> new AuthenticateCallbackResource(manager, notifierProvider()).execute());\n        spark.route(\"/asset/:name\").get(manager -> new AssetResource(name -> box.graph().loadResource(name), manager, notifierProvider()).execute());\n\n\t\t")).add(mark("resource").multiple("\n")).add(literal("\n\n\t\t")).add(mark("display").multiple("\n")).add(literal("\n\n    }\n\n    public static URL authServiceUrl() {\n        try {\n            return new URL(\"")).add(mark("auth")).add(literal("\");\n        } catch (MalformedURLException e) {\n            return null;\n        }\n    }\n\n    private static DisplayNotifierProvider notifierProvider() {\n        return (display, messageCarrier) -> {\n            ")).add(mark("display").multiple("\n")).add(literal("\n            return new DisplayNotifier(display, messageCarrier);\n        };\n    }\n}")),
			rule().add((condition("type", "abstractPage")), (condition("trigger", "resource"))).add(mark("path").multiple("\n")),
			rule().add((condition("trigger", "path"))).add(literal("spark.route(\"")).add(mark("value")).add(literal("\").get(manager -> new ")).add(mark("name", "firstUpperCase")).add(literal("Resource(box, manager, notifierProvider()).execute());")),
			rule().add((condition("type", "display"))).add(literal("spark.route(\"/")).add(mark("name", "lowecase")).add(literal("/:displayId\").before(manager -> new BeforeDisplayRequest(manager).execute());\nspark.route(\"/")).add(mark("name", "lowecase")).add(literal("/:displayId\").post(manager -> new ")).add(mark("name", "firstUppercase")).add(literal("DisplayRequester(manager, notifierProvider()).execute());\n")).add(mark("asset")).add(literal("\nspark.route(\"/")).add(mark("name", "lowecase")).add(literal("/:displayId\").after(manager -> new AfterDisplayRequest(manager).execute());")),
			rule().add((condition("trigger", "asset"))).add(literal("spark.route(\"/")).add(mark("value", "lowecase")).add(literal("/:displayId\").get(manager -> new ")).add(mark("value", "firstUppercase")).add(literal("DisplayRequester(manager, notifierProvider()).execute());")),
			rule().add((condition("type", "display"))).add(literal("if (display instanceof ")).add(mark("name", "firstUpperCase")).add(literal("Display) return new ")).add(mark("name", "firstUpperCase")).add(literal("DisplayNotifier(display, messageCarrier);"))
		);
		return this;
	}
}