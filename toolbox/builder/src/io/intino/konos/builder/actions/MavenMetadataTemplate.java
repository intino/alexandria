package io.intino.konos.builder.actions;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MavenMetadataTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("metadata"))).output(literal("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<metadata>\n  <groupId>")).output(mark("group", "lowercase")).output(literal("</groupId>\n  <artifactId>")).output(mark("artifact", "lowercase")).output(literal("</artifactId>\n  <version>")).output(mark("version")).output(literal("</version>\n  <versioning>\n    <latest>")).output(mark("version")).output(literal("</latest>\n    <release>")).output(mark("version")).output(literal("</release>\n    <versions>\n      <version>")).output(mark("version")).output(literal("</version>\n      ")).output(mark("oldversion").multiple("\n")).output(literal("\n    </versions>\n    <lastUpdated>")).output(mark("time")).output(literal("</lastUpdated>\n  </versioning>\n</metadata>")),
				rule().condition((trigger("oldversion"))).output(literal("<version>")).output(mark("value")).output(literal("</version>"))
		);
	}
}