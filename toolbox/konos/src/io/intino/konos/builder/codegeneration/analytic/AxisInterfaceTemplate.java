package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AxisInterfaceTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("interface")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(".analytic;\n\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".analytic.axes.*;\n\nimport java.util.List;\nimport java.util.stream.Stream;\n\npublic interface Axis {\n\n\tString getTitle();\n\n\tint getSize();\n\n\tList<? extends Component> getComponents();\n\n\tComponent getComponent(int index);\n\n\tComponent getComponent(String id);\n\n\tdefault boolean isDynamic() {\n\t\treturn false;\n\t};\n\n\tdefault Stream<? extends Component> toStream() {\n\t\treturn getComponents().stream();\n\t}\n\n\tdefault Stream<? extends Component> toParallelStream() {\n\t\treturn toStream().parallel();\n\t}\n\n\n\tinterface Component {\n\n\t\tint index();\n\n\t\tString id();\n\n\t\tdefault String label() {\n\t\t\treturn id();\n\t\t}\n\n\t\tAxis axis();\n\t}\n\n\tstatic Axis byName(String name) {\n\t\tswitch(name) {\n\t\t\t")).output(expression().output(placeholder("axis", "NameCase").multiple("\n"))).output(literal("\n\t\t}\n\t\treturn null;\n\t}\n\n\tstatic Axis byLabel(String label) {\n\t\tswitch(label) {\n\t\t\t")).output(expression().output(placeholder("axis", "LabelCase").multiple("\n"))).output(literal("\n\t\t}\n\t\treturn null;\n\t}\n\n\tstatic List<Axis> all() {\n\t\treturn List.of(\n\t\t\t")).output(expression().output(placeholder("axis", "ListElement").multiple(",\n"))).output(literal("\n\t\t);\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("axis"), trigger("namecase"))).output(literal("case \"")).output(placeholder("axis", "PascalCase")).output(literal("\": return ")).output(placeholder("axis", "PascalCase")).output(literal(".get();")));
		rules.add(rule().condition(all(allTypes("axis"), trigger("labelcase"))).output(literal("case ")).output(placeholder("axis", "PascalCase")).output(literal(".TITLE: return ")).output(placeholder("axis", "PascalCase")).output(literal(".get();")));
		rules.add(rule().condition(all(allTypes("axis"), trigger("listelement"))).output(placeholder("axis", "PascalCase")).output(literal(".get()")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}