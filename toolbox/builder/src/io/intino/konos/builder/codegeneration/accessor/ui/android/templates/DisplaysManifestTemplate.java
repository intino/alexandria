package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DisplaysManifestTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("manifest"))).output(literal("package ")).output(mark("package")).output(literal(".mobile.android.displays\n\nimport android.content.Context\nimport ")).output(mark("package")).output(literal(".mobile.android.displays.*\nimport ")).output(mark("package")).output(literal(".mobile.android.displays.components.*\nimport ")).output(mark("package")).output(literal(".mobile.android.displays.templates.*\nimport ")).output(mark("package")).output(literal(".mobile.android.displays.rows.*\nimport ")).output(mark("package")).output(literal(".mobile.android.displays.items.*\nimport ")).output(mark("package")).output(literal(".mobile.displays.notifiers.*\nimport ")).output(mark("package")).output(literal(".mobile.displays.requesters.*\nimport io.intino.alexandria.mobile.displays.DisplayDefinition\n\nobject ")).output(mark("accessible")).output(literal("Displays {\n\n    fun get(definition: DisplayDefinition, context: Context) : PassiveView<*, *> {\n        return when(definition.type()) {\n            ")).output(mark("display").multiple("\n")).output(literal("\n            else -> throw Exception(\"Display not found for type ")).output(mark("type")).output(literal("\")\n        }\n    }\n\n}")),
			rule().condition((allTypes("display","row"))).output(literal("\"")).output(mark("name", "firstUppercase")).output(literal("\" -> ")).output(mark("name", "firstUppercase")).output(literal("(context)")),
			rule().condition((allTypes("display","mold"))).output(literal("\"")).output(mark("name", "firstUppercase")).output(literal("\" -> ")).output(mark("name", "firstUppercase")).output(literal("(context)")),
			rule().condition((allTypes("display","item"))).output(literal("\"")).output(mark("name", "firstUppercase")).output(literal("\" -> ")).output(mark("name", "firstUppercase")).output(literal("(context)")),
			rule().condition((allTypes("display","template"))).output(literal("\"")).output(mark("name", "firstUppercase")).output(literal("\" -> ")).output(mark("name", "firstUppercase")).output(literal("(context)")),
			rule().condition((type("display"))).output(literal("\"")).output(mark("name", "firstUppercase")).output(literal("\" -> ")).output(mark("name", "firstUppercase")).output(literal("<")).output(mark("name", "firstUppercase")).output(literal("Requester, ")).output(mark("name", "firstUppercase")).output(literal("Notifier>(context)"))
		);
	}
}