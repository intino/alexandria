package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AxisInterfaceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("interface"))).output(literal("package ")).output(mark("package")).output(literal(".analytic;\n\nimport java.util.List;\nimport java.util.stream.Stream;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\n\npublic interface Axis {\n\n\tString getLabel();\n\n\tint getSize();\n\n\tList<? extends Component> getComponents();\n\n\tComponent getComponent(int index);\n\n\tComponent getComponent(String id);\n\n\tdefault boolean isDynamic() {\n\t\treturn false;\n\t};\n\n\tdefault Stream<? extends Component> toStream() {\n\t    return getComponents().stream();\n\t}\n\n\tdefault Stream<? extends Component> toParallelStream() {\n\t    return toStream().parallel();\n\t}\n\n\n\tinterface Component {\n\n\t\tint index();\n\n\t\tString id();\n\n\t\tdefault String label() {\n\t\t\treturn \"\";\n\t\t}\n\n\t\tAxis axis();\n\t}\n}"))
		);
	}
}