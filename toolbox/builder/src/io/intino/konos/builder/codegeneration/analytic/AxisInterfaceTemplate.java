package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AxisInterfaceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("interface"))).output(literal("package ")).output(mark("package")).output(literal(".analytic;\n\nimport java.util.List;\nimport java.util.stream.Stream;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\n\npublic interface Axis {\n\n\tString label();\n\n\tint size();\n\n\tList<? extends Component> components();\n\n\tComponent component(int index);\n\n\tComponent component(String id);\n\n\tdefault boolean isDynamic() {\n\t\treturn false;\n\t};\n\n\tdefault Stream<? extends Component> stream() {\n\t    return components().stream();\n\t}\n\n\tdefault Stream<? extends Component> parallelStream() {\n\t    return stream().parallel();\n\t}\n\n\n\tinterface Component {\n\n\t\tint index();\n\n\t\tString id();\n\n\t\tdefault String label() {\n\t\t\treturn \"\";\n\t\t}\n\n\t\tAxis axis();\n\t}\n}"))
		);
	}
}