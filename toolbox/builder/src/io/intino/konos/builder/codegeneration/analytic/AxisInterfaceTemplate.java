package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AxisInterfaceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("interface"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".analytic;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".analytic.axes.*;\n\nimport java.util.List;\nimport java.util.stream.Stream;\n\npublic interface Axis {\n\n\tString getTitle();\n\n\tint getSize();\n\n\tList<? extends Component> getComponents();\n\n\tComponent getComponent(int index);\n\n\tComponent getComponent(String id);\n\n\tdefault boolean isDynamic() {\n\t\treturn false;\n\t};\n\n\tdefault Stream<? extends Component> toStream() {\n\t    return getComponents().stream();\n\t}\n\n\tdefault Stream<? extends Component> toParallelStream() {\n\t    return toStream().parallel();\n\t}\n\n\n\tinterface Component {\n\n\t\tint index();\n\n\t\tString id();\n\n\t\tdefault String label() {\n\t\t\treturn id();\n\t\t}\n\n\t\tAxis axis();\n\t}\n\n\tstatic Axis byName(String name) {\n\t    switch(name) {\n\t        ")).output(expression().output(mark("axis", "NameCase").multiple("\n"))).output(literal("\n\t    }\n\t    return null;\n\t}\n\n\tstatic Axis byLabel(String label) {\n        switch(label) {\n            ")).output(expression().output(mark("axis", "LabelCase").multiple("\n"))).output(literal("\n        }\n        return null;\n    }\n\n    static List<Axis> all() {\n        return List.of(\n            ")).output(expression().output(mark("axis", "ListElement").multiple(",\n"))).output(literal("\n        );\n    }\n}")),
			rule().condition((type("axis")), (trigger("namecase"))).output(literal("case \"")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("\": return ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get();")),
			rule().condition((type("axis")), (trigger("labelcase"))).output(literal("case ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".TITLE: return ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get();")),
			rule().condition((type("axis")), (trigger("listelement"))).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get()"))
		);
	}
}