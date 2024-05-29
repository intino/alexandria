package io.intino.konos.builder.codegeneration.main;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class MainTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("main")).output(literal("package ")).output(placeholder("package")).output(literal(";\n\npublic class Main {\n\tpublic static void main(String[] args) {\n\t\t")).output(placeholder("name", "PascalCase")).output(literal("Box box = new ")).output(placeholder("name", "PascalCase")).output(literal("Box(args);\n\t\t")).output(expression().output(placeholder("model"))).output(literal("\n\t\tbox.start();\n\t\tRuntime.getRuntime().addShutdownHook(new Thread(box::stop));\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("model"), trigger("model"))).output(literal("io.intino.magritte.framework.Graph graph = new io.intino.magritte.framework.Graph().loadStashes(\"")).output(placeholder("name")).output(literal("\");")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}