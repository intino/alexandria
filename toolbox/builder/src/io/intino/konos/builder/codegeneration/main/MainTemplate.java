package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MainTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("main"))).output(literal("package ")).output(mark("package")).output(literal(";\n\nimport io.intino.alexandria.core.Box;\n\npublic class Main {\n\tpublic static void main(String[] args) {\n\t\t")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box box = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box(args);\n\t\t")).output(expression().output(mark("model"))).output(literal("\n\t\tbox.open();\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(box::close));\n\t}\n}")),
				rule().condition((type("model")), (trigger("model"))).output(literal("io.intino.tara.magritte.Graph graph = new io.intino.tara.magritte.Graph().loadStashes(\"")).output(mark("name")).output(literal("\");\nbox.put(graph).initDatamarts();"))
		);
	}
}