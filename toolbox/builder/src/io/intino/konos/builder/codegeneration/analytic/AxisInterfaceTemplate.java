package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AxisInterfaceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("interface"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".analytic;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".analytic.axes.*;\n\nimport java.util.List;\nimport java.util.stream.Stream;\n\npublic interface Axis {\n\n\tString getLabel();\n\n\tint getSize();\n\n\tList<? extends Component> getComponents();\n\n\tComponent getComponent(int index);\n\n\tComponent getComponent(String id);\n\n\tdefault boolean isDynamic() {\n\t\treturn false;\n\t};\n\n\tdefault Stream<? extends Component> toStream() {\n\t    return getComponents().stream();\n\t}\n\n\tdefault Stream<? extends Component> toParallelStream() {\n\t    return toStream().parallel();\n\t}\n\n\n\tinterface Component {\n\n\t\tint index();\n\n\t\tString id();\n\n\t\tdefault String label() {\n\t\t\treturn \"\";\n\t\t}\n\n\t\tAxis axis();\n\t}\n\n\tstatic Axis byName(String name) {\n\t    if(name == null) throw new NullPointerException(\"Name cannot be null\");\n\t    switch(name) {\n\t        ")).output(expression().output(mark("axisNameCase").multiple("\n"))).output(literal("\n\t    }\n\t    throw new IllegalArgumentException(\"Unknown axis \" + name);\n\t}\n\n\tstatic Axis byLabel(String label) {\n        if(label == null) throw new NullPointerException(\"Label cannot be null\");\n        switch(label) {\n            ")).output(expression().output(mark("axisLabelCase").multiple("\n"))).output(literal("\n        }\n        throw new IllegalArgumentException(\"Unknown axis \" + label);\n    }\n\n    static List<Axis> getAll() {\n        return List.of(\n            ")).output(expression().output(mark("axisListElement").multiple(",\n"))).output(literal("\n        );\n    }\n}")),
			rule().condition((type("axisNameCase"))).output(literal("case \"")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("\": return ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get();")),
			rule().condition((type("axisLabelCase"))).output(literal("case ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".AXIS_LABEL: return ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".AXIS_LABEL;")),
			rule().condition((type("axisListElement"))).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get(),"))
		);
	}
}