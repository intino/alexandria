package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class DistributionTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("distribution"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.distributions;\n\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\nimport java.util.*;\nimport java.util.stream.Collectors;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" implements Axis {\n\tprivate static final ")).output(mark("name", "FirstUpperCase")).output(literal(" instance = new ")).output(mark("name", "FirstUpperCase")).output(literal("();\n\n\tprivate static Range[] ranges = new Range[")).output(mark("rangeSize")).output(literal("];\n\tprivate static double[] limits = new double[")).output(mark("rangeSize")).output(literal("];\n\n\tprivate ")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\t\t")).output(mark("range", "init").multiple("\n")).output(literal("\n\t\tfor (int i = 0; i < ranges.length; i++) limits[i] = ranges[i].max;\n\t}\n\n\tpublic static ")).output(mark("name", "FirstUpperCase")).output(literal(" instance() {\n\t\treturn instance;\n\t}\n\n\tpublic String label() {\n\t\treturn \"")).output(mark("label")).output(literal("\";\n\t}\n\n\tpublic List<Range> ranges() {\n\t\treturn new AbstractList<>() {\n\t\t\t@Override\n\t\t\tpublic Range get(int index) {\n\t\t\t\treturn ranges[index];\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic int size() {\n\t\t\t\treturn ranges.length;\n\t\t\t}\n\t\t};\n\t}\n\n\tpublic static Range rangeOf(double value) {\n\t\tint index = Arrays.binarySearch(limits, value);\n\t\tindex = index < 0 ? (index + 1) * -1 : index;\n\t\treturn ranges[index >= ranges.length ? ranges.length - 1 : index];\n\t}\n\n\t@Override\n\tpublic Component byIndex(int index) {\n\t\treturn ranges().get(index);\n\t}\n\n\t@Override\n\tpublic int size() {\n\t\treturn ranges().size();\n\t}\n\n\t@Override\n\tpublic Collection<? extends Component> components() {\n\t\treturn null;\n\t}\n\n\tpublic class Range implements Component {\n\t\tprivate final double min;\n\t\tprivate final double max;\n\t\tprivate final String label;\n\n\t\tpublic Range(double min, double max, String label) {\n\t\t\tthis.min = min;\n\t\t\tthis.max = max;\n\t\t\tthis.label = label;\n\t\t}\n\n\t\tpublic double min() {\n\t\t\treturn min;\n\t\t}\n\n\t\tpublic double max() {\n\t\t\treturn max;\n\t\t}\n\n\t\t@Override\n\t\tpublic int index() {\n\t\t\treturn 0;\n\t\t}\n\n\t\t@Override\n\t\tpublic String id() {\n\t\t\treturn label;\n\t\t}\n\n\t\tpublic String label() {\n\t\t\treturn label;\n\t\t}\n\n\t\t@Override\n\t\tpublic Axis axis() {\n\t\t\treturn ")).output(mark("name", "FirstUpperCase")).output(literal(".this;\n\t\t}\n\t}\n}")),
			rule().condition((allTypes("range","lower")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(Double.MIN_VALUE, ")).output(mark("bound")).output(literal(", \"< ")).output(mark("bound")).output(literal("\");")),
			rule().condition((allTypes("range","upper")), (trigger("init"))).output(literal("\tranges[2] = new Range(")).output(mark("bound")).output(literal(", Double.MAX_VALUE, \"> ")).output(mark("bound")).output(literal("\"); // TODO Discutir con JJ si son intervalos abiertos o cerrados...")),
			rule().condition((type("range")), (trigger("init"))).output(literal("ranges[1] = new Range(")).output(mark("lower")).output(literal(", ")).output(mark("upper")).output(literal(", \"")).output(mark("lower")).output(literal(" - ")).output(mark("upper")).output(literal("\");"))
		);
	}
}