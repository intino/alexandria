package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ContinuousAxisTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("continuous"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".analytic.axes;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".analytic.Axis;\nimport java.util.*;\nimport java.util.stream.Collectors;\nimport java.util.stream.Stream;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" implements Axis {\n\n\tpublic static final String TITLE = \"")).output(mark("label")).output(literal("\";\n\n\tprivate static final class Singleton {\n\t\tprivate static final ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" INSTANCE = new ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("();\n\t}\n\n\t// === STATIC METHODS === //\n\n\tpublic static ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" get() {\n\t\treturn Singleton.INSTANCE;\n\t}\n\n\tpublic static String title() {\n\t\treturn TITLE;\n\t}\n\n\tpublic static int size() {\n\t\treturn get().getSize();\n\t}\n\n\tpublic static Range component(String id) {\n\t\treturn get().getComponent(id);\n\t}\n\n\tpublic static Range component(int index) {\n\t\treturn get().getComponent(index);\n\t}\n\n\tpublic static List<Range> ranges() {\n\t\treturn get().getRanges();\n\t}\n\n\tpublic static Range rangeOf(final double value) {\n\t\tfinal Range[] ranges = Singleton.INSTANCE.ranges;\n\t\tint index = binarySearch(ranges, value);\n\t\tindex = index < 0 ? (index + 1) * -1 : index;\n\t\treturn ranges[index >= ranges.length ? ranges.length - 1 : index];\n\t}\n\n\tprivate static int binarySearch(final Range[] ranges, final double value) {\n\t\tint low = 0;\n\t\tint high = ranges.length - 1;\n\n\t\twhile (low <= high) {\n\t\t\tfinal int mid = (low + high) >>> 1;\n\t\t\tfinal double midVal = ranges[mid].max;\n\t\t\tfinal int comparison = Double.compare(midVal, value);\n\n\t\t\tif (comparison < 0)\n\t\t\t\tlow = mid + 1;\n\t\t\telse if (comparison > 0)\n\t\t\t\thigh = mid - 1;\n\t\t\telse\n\t\t\t\treturn mid;\n\t\t}\n\t\treturn -(low + 1);\n\t}\n\n\t// === === //\n\n\tprivate final Range[] ranges = new Range[")).output(mark("rangeSize")).output(literal("];\n\n\tprivate ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("() {\n\t\t")).output(mark("range", "init").multiple("\n")).output(literal("\n\t}\n\n\t@Override\n\tpublic String getTitle() {\n\t\treturn TITLE;\n\t}\n\n\t@Override\n\tpublic Range getComponent(String id) {\n\t\treturn ranges[Integer.parseInt(id)];\n\t}\n\n\t@Override\n\tpublic Range getComponent(int index) {\n\t\treturn ranges[index];\n\t}\n\n\tpublic List<Range> getRanges() {\n\t\treturn new AbstractList<>() {\n\t\t\t@Override\n\t\t\tpublic Range get(int index) {\n\t\t\t\treturn ranges[index];\n\t\t\t}\n\n\t\t\t@Override\n\t\t\tpublic int size() {\n\t\t\t\treturn ranges.length;\n\t\t\t}\n\t\t};\n\t}\n\n\t@Override\n\tpublic int getSize() {\n\t\treturn ranges().size();\n\t}\n\n\t@Override\n\tpublic List<Range> getComponents() {\n\t\treturn ranges();\n\t}\n\n\t@Override\n\tpublic Stream<Range> toStream() {\n\t\treturn Arrays.stream(ranges);\n\t}\n\n\t@Override\n\tpublic String toString() {\n\t\treturn \"")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("\";\n\t}\n\n\tpublic class Range implements Component {\n\n\t\tprivate final int index;\n\t\tprivate final double min;\n\t\tprivate final double max;\n\t\tprivate final String label;\n\n\t\tpublic Range(int index, double min, double max, String label) {\n\t\t\tthis.index = index;\n\t\t\tthis.min = min;\n\t\t\tthis.max = max;\n\t\t\tthis.label = label;\n\t\t}\n\n\t\tpublic double min() {\n\t\t\treturn min;\n\t\t}\n\n\t\tpublic double max() {\n\t\t\treturn max;\n\t\t}\n\n\t\t@Override\n\t\tpublic int index() {\n\t\t\treturn index;\n\t\t}\n\n\t\t@Override\n\t\tpublic String id() {\n\t\t\treturn label;\n\t\t}\n\n\t\t@Override\n\t\tpublic String label() {\n\t\t\treturn label;\n\t\t}\n\n\t\t@Override\n\t\tpublic Axis axis() {\n\t\t\treturn ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(".this;\n\t\t}\n\n\t\t@Override\n\t\tpublic boolean equals(Object o) {\n\t\t\tif (this == o) return true;\n\t\t\tif (o == null || getClass() != o.getClass()) return false;\n\t\t\tRange range = (Range) o;\n\t\t\treturn index == range.index;\n\t\t}\n\n\t\t@Override\n\t\tpublic int hashCode() {\n\t\t\treturn index;\n\t\t}\n\n\t\t@Override\n\t\tpublic String toString() {\n\t\t\treturn label;\n\t\t}\n\t}\n}")),
			rule().condition((allTypes("range","lower")), (attribute("label")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(")).output(mark("index")).output(literal(", Double.MIN_VALUE, ")).output(mark("bound")).output(literal(", \"")).output(mark("label")).output(literal("\");")),
			rule().condition((allTypes("range","upper")), (attribute("label")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(")).output(mark("index")).output(literal(", ")).output(mark("bound")).output(literal(", Double.MAX_VALUE, \"")).output(mark("label")).output(literal("\");")),
			rule().condition((type("range")), (attribute("label")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(")).output(mark("index")).output(literal(", ")).output(mark("lower")).output(literal(", ")).output(mark("upper")).output(literal(", \"")).output(mark("label")).output(literal("\");")),
			rule().condition((allTypes("range","lower")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(")).output(mark("index")).output(literal(", Double.MIN_VALUE, ")).output(mark("bound")).output(literal(", \"< ")).output(mark("bound")).output(literal("\");")),
			rule().condition((allTypes("range","upper")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(")).output(mark("index")).output(literal(", ")).output(mark("bound")).output(literal(", Double.MAX_VALUE, \"> ")).output(mark("bound")).output(literal("\");")),
			rule().condition((type("range")), (trigger("init"))).output(literal("ranges[")).output(mark("index")).output(literal("] = new Range(")).output(mark("index")).output(literal(", ")).output(mark("lower")).output(literal(", ")).output(mark("upper")).output(literal(", \"")).output(mark("lower")).output(literal(" - ")).output(mark("upper")).output(literal("\");"))
		);
	}
}