package io.intino.alexandria.tabb.generators;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class ArffTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("arff")).output(literal("@RELATION relation\n\n")).output(placeholder("attribute").multiple("\n")).output(literal("\n\n@DATA\n")));
		rules.add(rule().condition(trigger("attribute")).output(literal("@ATTRIBUTE ")).output(placeholder("name")).output(literal(" ")).output(placeholder("type")));
		rules.add(rule().condition(all(allTypes("Nominal"), trigger("type"))).output(literal("{")).output(placeholder("value", "quoted").multiple(",")).output(literal("}")));
		rules.add(rule().condition(all(allTypes("Date"), trigger("type"))).output(literal("DATE \"")).output(placeholder("format")).output(literal("\"")));
		rules.add(rule().condition(all(allTypes("Numeric"), trigger("type"))).output(literal("NUMERIC")));
		rules.add(rule().condition(all(allTypes("String"), trigger("type"))).output(literal("string")));
		rules.add(rule().condition(trigger("quoted")).output(literal("\"")).output(placeholder("")).output(literal("\"")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}