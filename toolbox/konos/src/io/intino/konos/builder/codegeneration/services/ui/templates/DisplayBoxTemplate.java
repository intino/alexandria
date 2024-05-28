package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class DisplayBoxTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(all(allTypes("box", "decorated"), trigger("extension"))).output(literal("B extends Box")));
		rules.add(rule().condition(all(allTypes("box"), trigger("extension"))));
		rules.add(rule().condition(all(allTypes("box", "decorated"), trigger("extensiontagged"))).output(literal("<B extends Box>")));
		rules.add(rule().condition(all(allTypes("box"), trigger("extensiontagged"))));
		rules.add(rule().condition(all(allTypes("box", "decorated"), trigger("type"))).output(literal("B")));
		rules.add(rule().condition(all(allTypes("box", "accessible"), trigger("type"))).output(literal("Box")));
		rules.add(rule().condition(all(allTypes("box"), trigger("type"))).output(placeholder("box", "firstUpperCase")).output(literal("Box")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}