package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ArtifactTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("artifact","legio"))).output(literal("dsl Legio\n\nArtifact(groupId = \"")).output(mark("groupID")).output(literal("\", version = \"1.0.0\") ")).output(mark("artifactID")).output(literal("UI\n\tWebImports\n\t\tWebArtifact(\"io.intino.alexandria\", \"ui-elements\", \"LATEST\") alexandria-ui-elements\n\n\t\tResolution(\"jquery\", \"3.1.0\")\n")).output(mark("repository")),
			rule().condition((type("repository"))).output(literal("Repository(\"")).output(mark("id")).output(literal("\")\n\t")).output(mark("url").multiple("\n")),
			rule().condition((trigger("url"))).output(literal("Release(\"")).output(mark("")).output(literal("\")"))
		);
	}
}