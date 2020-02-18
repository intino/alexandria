package io.intino.konos.builder.codegeneration.services.ui.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DisplayBoxTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("box","decorated")), (trigger("extension"))).output(literal("B extends Box")),
			rule().condition((type("box")), (trigger("extension"))),
			rule().condition((allTypes("box","decorated")), (trigger("extensiontagged"))).output(literal("<B extends Box>")),
			rule().condition((type("box")), (trigger("extensiontagged"))),
			rule().condition((allTypes("box","decorated")), (trigger("type"))).output(literal("B")),
			rule().condition((allTypes("box","accessible")), (trigger("type"))).output(literal("Box")),
			rule().condition((type("box")), (trigger("type"))).output(mark("box", "firstUpperCase")).output(literal("Box"))
		);
	}
}