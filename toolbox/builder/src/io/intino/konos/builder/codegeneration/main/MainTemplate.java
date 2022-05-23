package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MainTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("main"))).output(literal("package ")).output(mark("package")).output(literal(";\n\npublic class Main {\n\tpublic static void main(String[] args) {\n\t\t")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box box = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Box(args);\n\t\t")).output(expression().output(mark("model"))).output(literal("\n\t\tbox.start();\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(box::stop));\n\t}\n}")),
			rule().condition((type("model")), (trigger("model"))).output(literal("io.intino.magritte.magritte.Graph graph = new io.intino.magritte.magritte.Graph().loadStashes(\"")).output(mark("name")).output(literal("\");\nbox.put(graph).initDatamarts();"))
		);
	}
}