package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewNotifierTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.displays.notifiers\n\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.")).output(expression().output(placeholder("packageType")).output(literal("s."))).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal("\nimport kotlinx.serialization.json.JsonArray\nimport kotlinx.serialization.json.JsonObject\nimport kotlinx.serialization.json.JsonPrimitive\n\nopen class ")).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal("Notifier(private var element: ")).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal(") : ")).output(placeholder("parentType")).output(literal("(element) {\n\n    init {\n        setup();\n    }\n\n    override fun setup() {\n        if (this.element == null || this.element.name() == null || this.pushLinked) return;\n        super.setup();\n        ")).output(expression().output(placeholder("notification").multiple("\n"))).output(literal("\n        ")).output(expression().output(placeholder("event").multiple("\n"))).output(literal("\n\t\tthis.pushLinked = true;\n    }\n\n}")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("import"))).output(literal("import ")).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier from \"./")).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier\"")));
		rules.add(rule().condition(all(attribute("component"), trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/src/displays/notifiers/ComponentNotifier\";")));
		rules.add(rule().condition(all(attribute("exposed"), trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/gen/displays/notifiers/ProxyDisplayNotifier\";")));
		rules.add(rule().condition(all(attribute("basetype"), trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/gen/displays/notifiers/")).output(placeholder("type", "firstUpperCase")).output(literal("Notifier\";")));
		rules.add(rule().condition(trigger("import")).output(literal("import Notifier from \"./Notifier\";")));
		rules.add(rule().condition(all(attribute("exposed"), trigger("proxy"))).output(literal("Proxy")));
		rules.add(rule().condition(trigger("proxy")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("parenttype"))).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier")));
		rules.add(rule().condition(trigger("parenttype")).output(literal("Notifier")));
		rules.add(rule().condition(trigger("notification")).output(literal("onMessage(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\")")).output(expression().output(placeholder("target"))).output(literal(".execute { whenReady { element.")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(expression().output(placeholder("parameter", "value"))).output(literal(") } }")));
		rules.add(rule().condition(allTypes("parameter", "datetime")).output(literal("kotlinx.datetime.Instant.fromEpochMilliseconds(((it[\"v\"]!! as JsonPrimitive).content).toLong())")));
		rules.add(rule().condition(allTypes("parameter", "date")).output(literal("kotlinx.datetime.Instant.fromEpochMilliseconds(((it[\"v\"]!! as JsonPrimitive).content).toLong())")));
		rules.add(rule().condition(allTypes("parameter", "real")).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toDouble()")));
		rules.add(rule().condition(allTypes("parameter", "longinteger")).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toLong()")));
		rules.add(rule().condition(allTypes("parameter", "integer")).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toInt()")));
		rules.add(rule().condition(allTypes("parameter", "boolean")).output(literal("((it[\"v\"]!! as JsonPrimitive).content).toBoolean()")));
		rules.add(rule().condition(allTypes("parameter", "object", "list")).output(literal("if (it[\"v\"] != null) io.intino.alexandria.mobile.util.Json.list(it[\"v\"] as JsonArray, ")).output(placeholder("package")).output(literal(".mobile.schemas.")).output(placeholder("value", "firstUpperCase")).output(literal(".empty()) else emptyList()")));
		rules.add(rule().condition(allTypes("parameter", "object")).output(literal("if (it[\"v\"] != null && !(it[\"v\"] as JsonObject).isEmpty()) io.intino.alexandria.mobile.util.Json.parse((it[\"v\"] as JsonObject).toString()) as ")).output(placeholder("package")).output(literal(".mobile.schemas.")).output(placeholder("value", "firstUpperCase")).output(literal(" else ")).output(placeholder("package")).output(literal(".mobile.schemas.")).output(placeholder("value", "firstUpperCase")).output(literal(".empty()")));
		rules.add(rule().condition(allTypes("parameter", "text", "list")).output(literal("if (it.containsKey(\"v\")) io.intino.alexandria.mobile.util.Json.list(it[\"v\"] as JsonArray) else emptyList()")));
		rules.add(rule().condition(allTypes("parameter", "list")).output(literal("if (it.containsKey(\"v\")) io.intino.alexandria.mobile.util.Json.list(it[\"v\"] as JsonArray, ")).output(placeholder("value")).output(literal("()) else emptyList()")));
		rules.add(rule().condition(allTypes("parameter")).output(literal("if (it.containsKey(\"v\")) (it[\"v\"] as JsonPrimitive).content else \"\"")));
		rules.add(rule().condition(all(attribute("","Display"), trigger("target"))).output(literal(".toSelf()")));
		rules.add(rule().condition(trigger("target")));
		rules.add(rule().condition(allTypes("event")).output(literal("onMessage(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\").toSelf().execute { whenReady { element.")).output(placeholder("name", "firstLowerCase")).output(literal("(if (it.containsKey(\"v\")) (it[\"v\"] as JsonPrimitive).content else \"\") } }")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}