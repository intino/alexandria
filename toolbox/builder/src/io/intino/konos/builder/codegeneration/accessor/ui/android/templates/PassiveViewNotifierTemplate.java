package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewNotifierTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("display"))).output(literal("package ")).output(mark("package")).output(literal(".mobile.displays.notifiers\n\nimport ")).output(mark("package")).output(literal(".mobile.displays.")).output(expression().output(mark("packageType")).output(literal("s."))).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal("\nimport kotlinx.serialization.json.JsonPrimitive\n\nopen class ")).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal("Notifier(private var element: ")).output(mark("name", "firstUpperCase")).output(mark("proxy")).output(literal(") : ")).output(mark("parentType")).output(literal("(element) {\n\n    init {\n\t\t")).output(expression().output(mark("notification").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(mark("event").multiple("\n"))).output(literal("\n    }\n\n}")),
			rule().condition((attribute("extensionof")), (trigger("import"))).output(literal("import ")).output(mark("parent", "firstUpperCase")).output(literal("Notifier from \"./")).output(mark("parent", "firstUpperCase")).output(literal("Notifier\"")),
			rule().condition((attribute("component")), (trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/src/displays/notifiers/ComponentNotifier\";")),
			rule().condition((attribute("accessible")), (trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/gen/displays/notifiers/ProxyDisplayNotifier\";")),
			rule().condition((attribute("basetype")), (trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/gen/displays/notifiers/")).output(mark("type", "firstUpperCase")).output(literal("Notifier\";")),
			rule().condition((trigger("import"))).output(literal("import Notifier from \"./Notifier\";")),
			rule().condition((attribute("accessible")), (trigger("proxy"))).output(literal("Proxy")),
			rule().condition((trigger("proxy"))),
			rule().condition((attribute("extensionof")), (trigger("parenttype"))).output(mark("parent", "firstUpperCase")).output(literal("Notifier")),
			rule().condition((trigger("parenttype"))).output(literal("Notifier")),
			rule().condition((trigger("notification"))).output(literal("onMessage(\"")).output(mark("name", "firstLowerCase")).output(literal("\")")).output(expression().output(mark("target"))).output(literal(".execute { element.")).output(mark("name", "firstLowerCase")).output(literal("(")).output(expression().output(mark("parameter", "value"))).output(literal(") }")),
			rule().condition((allTypes("parameter","datetime"))).output(literal("kotlinx.datetime.Instant.fromEpochMilliseconds(((it[\"v\"]!! as JsonPrimitive).content).toLong())")),
			rule().condition((allTypes("parameter","date"))).output(literal("kotlinx.datetime.Instant.fromEpochMilliseconds(((it[\"v\"]!! as JsonPrimitive).content).toLong())")),
			rule().condition((allTypes("parameter","real"))).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toDouble()")),
			rule().condition((allTypes("parameter","longinteger"))).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toLong()")),
			rule().condition((allTypes("parameter","integer"))).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toInt()")),
			rule().condition((allTypes("parameter","boolean"))).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toBoolean()")),
			rule().condition((allTypes("parameter","object"))).output(literal("io.intino.alexandria.mobile.util.Json.parse((it[\"v\"]!! as JsonPrimitive).content) as ")).output(mark("package")).output(literal(".mobile.schemas.")).output(mark("value", "firstUpperCase")),
			rule().condition((type("parameter"))).output(literal("(it[\"v\"]!! as JsonPrimitive).content")),
			rule().condition((attribute("", "Display")), (trigger("target"))).output(literal(".toSelf()")),
			rule().condition((trigger("target"))),
			rule().condition((type("event"))).output(literal("onMessage(\"")).output(mark("name", "firstLowerCase")).output(literal("\").toSelf().execute { element.")).output(mark("name", "firstLowerCase")).output(literal("((it[\"v\"]!! as JsonPrimitive).content) }"))
		);
	}
}