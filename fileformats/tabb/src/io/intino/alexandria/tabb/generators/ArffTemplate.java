package io.intino.alexandria.tabb.generators;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ArffTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("arff"))).output(literal("@RELATION relation\n\n")).output(mark("attribute").multiple("\n")).output(literal("\n\n@DATA\n")),
			rule().condition((trigger("attribute"))).output(literal("@ATTRIBUTE ")).output(mark("name")).output(literal(" ")).output(mark("type")),
			rule().condition((type("nominal")), (trigger("type"))).output(literal("{")).output(mark("value", "quoted").multiple(",")).output(literal("}")),
			rule().condition((type("date")), (trigger("type"))).output(literal("DATE \"")).output(mark("format")).output(literal("\"")),
			rule().condition((type("numeric")), (trigger("type"))).output(literal("NUMERIC")),
			rule().condition((type("string")), (trigger("type"))).output(literal("string")),
			rule().condition((trigger("quoted"))).output(literal("\"")).output(mark("value")).output(literal("\""))
		);
	}
}