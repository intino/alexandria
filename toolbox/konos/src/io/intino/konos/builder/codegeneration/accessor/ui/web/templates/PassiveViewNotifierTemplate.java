package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class PassiveViewNotifierTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("display")).output(placeholder("import")).output(literal("\n\nexport default class ")).output(placeholder("name", "firstUpperCase")).output(placeholder("proxy")).output(literal("Notifier extends ")).output(placeholder("parentType")).output(literal(" {\n\tconstructor(element) {\n\t\tsuper(element);\n\t\tthis.setup();\n\t};\n\n\tsetup() {\n\t\tif (this.element == null || this.pushLinked) return;\n\t\tsuper.setup();\n\t\t")).output(expression().output(placeholder("notification").multiple("\n"))).output(literal("\n\t\t")).output(expression().output(placeholder("event").multiple("\n"))).output(literal("\n\t\tthis.pushLinked = true;\n\t};\n}")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("import"))).output(literal("import ")).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier from \"./")).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier\"")));
		rules.add(rule().condition(all(attribute("component"), trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/src/displays/notifiers/ComponentNotifier\";")));
		rules.add(rule().condition(all(attribute("exposed"), trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/gen/displays/notifiers/ProxyDisplayNotifier\";")));
		rules.add(rule().condition(all(attribute("basetype"), trigger("import"))).output(literal("import Notifier from \"alexandria-ui-elements/gen/displays/notifiers/")).output(placeholder("type", "firstUpperCase")).output(literal("Notifier\";")));
		rules.add(rule().condition(trigger("import")).output(literal("import Notifier from \"./Notifier\";")));
		rules.add(rule().condition(all(attribute("exposed"), trigger("proxy"))).output(literal("Proxy")));
		rules.add(rule().condition(trigger("proxy")));
		rules.add(rule().condition(all(attribute("extensionof"), trigger("parenttype"))).output(placeholder("parent", "firstUpperCase")).output(literal("Notifier")));
		rules.add(rule().condition(trigger("parenttype")).output(literal("Notifier")));
		rules.add(rule().condition(trigger("notification")).output(literal("this.when(\"")).output(placeholder("name")).output(literal("\")")).output(expression().output(placeholder("target"))).output(literal(".execute((")).output(expression().output(placeholder("parameter", "call"))).output(literal(") => this.element.")).output(placeholder("name")).output(literal("(")).output(expression().output(placeholder("parameter", "value"))).output(literal("));")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("call"))).output(literal("parameters")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("value"))).output(literal("parameters.v")));
		rules.add(rule().condition(all(attribute("","Display"), trigger("target"))).output(literal(".toSelf()")));
		rules.add(rule().condition(trigger("target")));
		rules.add(rule().condition(allTypes("event")).output(literal("this.when(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\").toSelf().execute((parameters) => this.element.notifyProxyMessage(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\"));")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}