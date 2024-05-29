package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class DisplaysManifestTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("manifest")).output(literal("package ")).output(placeholder("package")).output(literal(".mobile.android.displays\n\nimport android.content.Context\nimport ")).output(placeholder("package")).output(literal(".mobile.android.displays.*\nimport ")).output(placeholder("package")).output(literal(".mobile.android.displays.components.*\nimport ")).output(placeholder("package")).output(literal(".mobile.android.displays.templates.*\nimport ")).output(placeholder("package")).output(literal(".mobile.android.displays.rows.*\nimport ")).output(placeholder("package")).output(literal(".mobile.android.displays.items.*\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.notifiers.*\nimport ")).output(placeholder("package")).output(literal(".mobile.displays.requesters.*\nimport io.intino.alexandria.mobile.displays.DisplayDefinition\n\nobject ")).output(placeholder("accessible")).output(literal("Displays {\n\n    fun get(definition: DisplayDefinition, context: Context) : PassiveView<*, *>? {\n        return when(definition.type()) {\n            ")).output(placeholder("display").multiple("\n")).output(literal("\n            //else -> throw Exception(\"Display not found for type \" + definition.type())\n            else -> null\n        }\n    }\n\n}")));
		rules.add(rule().condition(allTypes("display", "row")).output(literal("\"")).output(placeholder("name", "firstUppercase")).output(literal("\" -> ")).output(placeholder("name", "firstUppercase")).output(literal("(context)")));
		rules.add(rule().condition(allTypes("display", "mold")).output(literal("\"")).output(placeholder("name", "firstUppercase")).output(literal("\" -> ")).output(placeholder("name", "firstUppercase")).output(literal("(context)")));
		rules.add(rule().condition(allTypes("display", "item")).output(literal("\"")).output(placeholder("name", "firstUppercase")).output(literal("\" -> ")).output(placeholder("name", "firstUppercase")).output(literal("(context)")));
		rules.add(rule().condition(allTypes("display", "template")).output(literal("\"")).output(placeholder("name", "firstUppercase")).output(literal("\" -> ")).output(placeholder("name", "firstUppercase")).output(literal("(context)")));
		rules.add(rule().condition(allTypes("display")).output(literal("\"")).output(placeholder("name", "firstUppercase")).output(literal("\" -> ")).output(placeholder("name", "firstUppercase")).output(literal("<")).output(placeholder("name", "firstUppercase")).output(literal("Requester, ")).output(placeholder("name", "firstUppercase")).output(literal("Notifier>(context)")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}