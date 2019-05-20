package io.intino.konos.builder.codegeneration.accessor.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class PassiveViewNotifierTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("accessible","display"))).output(literal("import Notifier from \"./Notifier\";\n\nexport default class ")).output(mark("name", "firstUpperCase")).output(literal("Notifier extends Notifier {\n\n\tconstructor(element) {\n\t\tsuper(element);\n\t\tthis.setup();\n\t};\n\n\tsetup() {\n\t\tif (this.element == null || this.pushLinked) return;\n\t\tsuper.setup();\n\t\tthis.when(\"refreshBaseUrl\").toSelf().execute((parameters) => this.element.refreshBaseUrl(parameters.v));\n\t\tthis.when(\"refreshError\").toSelf().execute((parameters) => this.element.refreshError(parameters.v));\n\t\tthis.pushLinked = true;\n\t};\n}")),
			rule().condition((type("display"))).output(mark("import")).output(literal("\n\nexport default class ")).output(mark("name", "firstUpperCase")).output(literal("Notifier extends ")).output(mark("parentType")).output(literal(" {\n\tconstructor(element) {\n\t\tsuper(element);\n\t\tthis.setup();\n\t};\n\n\tsetup() {\n\t\tif (this.element == null || this.pushLinked) return;\n\t\tsuper.setup();\n\t\t")).output(expression().output(mark("notification").multiple("\n"))).output(literal("\n\t\tthis.pushLinked = true;\n\t};\n}")),
			rule().condition((attribute("extensionof")), (trigger("import"))).output(literal("import ")).output(mark("parent", "firstUpperCase")).output(literal("Notifier from \"./")).output(mark("parent", "firstUpperCase")).output(literal("Notifier\"")),
			rule().condition((trigger("import"))).output(literal("import Notifier from \"./Notifier\";")),
			rule().condition((attribute("extensionof")), (trigger("parenttype"))).output(mark("parent", "firstUpperCase")).output(literal("Notifier")),
			rule().condition((trigger("parenttype"))).output(literal("Notifier")),
			rule().condition((trigger("notification"))).output(literal("this.when(\"")).output(mark("name")).output(literal("\")")).output(expression().output(mark("target"))).output(literal(".execute((")).output(expression().output(mark("parameter", "call"))).output(literal(") => this.element.")).output(mark("name")).output(literal("(")).output(expression().output(mark("parameter", "value"))).output(literal("));")),
			rule().condition((type("parameter")), (trigger("call"))).output(literal("parameters")),
			rule().condition((type("parameter")), (trigger("value"))).output(literal("parameters.v")),
			rule().condition((attribute("", "Display")), (trigger("target"))).output(literal(".toSelf()")),
			rule().condition((trigger("target")))
		);
	}
}