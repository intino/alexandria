package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class WidgetsTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("widgets"))).output(mark("widget").multiple("\n")),
				rule().condition((trigger("widget"))).output(literal("<link rel=\"import\" href=\"")).output(mark("value", "camelCaseToSnakeCase")).output(literal(".html\">"))
		);
	}
}