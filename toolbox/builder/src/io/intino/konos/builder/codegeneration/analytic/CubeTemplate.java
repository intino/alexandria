package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CubeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("cube","src"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(java.util.List<Loader> loaders) {\n\t\tsuper(loaders);\n\t}\n\n\t")).output(expression().output(mark("customDimension", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customIndicator", "staticMethod").multiple("\n\n"))).output(literal("\n\n\tpublic static class Loader extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Loader {\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tsuper(datasource")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(");\n\t\t}\n\n\t\t// TODO write here a cache if necessary\n\t}\n}")),
			rule().condition((type("cube"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.led.LedReader;\nimport io.intino.alexandria.led.LedStream;\nimport io.intino.alexandria.led.Schema;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\nimport io.intino.alexandria.led.util.iterators.MergedIterator;\n\nimport ")).output(mark("package")).output(literal(".analytic.axes.*;\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\n\nimport java.io.File;\nimport java.util.*;\nimport java.util.function.Function;\nimport java.util.function.Predicate;\nimport java.util.stream.Stream;\nimport java.util.stream.StreamSupport;\n\nimport static java.util.Comparator.comparingLong;\nimport static java.util.Spliterators.spliteratorUnknownSize;\n\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprivate static final String ID_SEP = \" - \";\n\tprivate static Fact nullFact = new NullFact();\n\tprivate final List<? extends Loader> loaders;\n\tprivate final List<Predicate<Fact>> filterList = new ArrayList<>();\n\tprivate final List<Axis> axes = new ArrayList<>();\n\tprivate final Map<Axis, Set<Axis.Component>> components = new HashMap<>();\n\tprivate final List<Function<Fact, Axis.Component>> groupByList = new ArrayList<>();\n\tprivate Aggregation[] result;\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("(List<? extends Loader> loaders) {\n\t\tthis.loaders = loaders;\n\t}\n\n\tpublic static Fact nullFact() {\n\t\treturn nullFact;\n\t}\n\n\t")).output(expression().output(mark("dimension", "method").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "method").multiple("\n\n"))).output(literal("\n\n\tpublic Abstract")).output(mark("name", "firstUpperCase")).output(literal(" execute() {\n\t\tresult = new Aggregation[resultsSize()];\n\t\tresults().forEach(this::append);\n\t\tfillCategories();\n\t\treturn this;\n\t}\n\n\tpublic Iterator<Fact> detail() {\n\t\tMergedIterator<Fact> iterator = new MergedIterator<>(loaders.stream().map(Iterable::iterator), comparingLong(Fact::id));\n\t\treturn StreamSupport.stream(spliteratorUnknownSize(iterator, Spliterator.SORTED), false).filter(this::check).iterator();\n\t}\n\n\tpublic Aggregation aggregation(List<Axis.Component> components) {\n\t\treturn result[indexOf(components.toArray(new Axis.Component[0]))];\n\t}\n\n\tprivate void fillCategories() {\n\t\tfor (Aggregation aggregation : result)\n\t\t\tfor (int i = 0; i < aggregation.components.length; i++)\n\t\t\t\tcomponents.get(axes.get(i)).add(aggregation.components[i]);\n\t}\n\n\tprivate int resultsSize() {\n\t\tint accumulator = 1;\n\t\tfor (Axis axis : axes) accumulator *= axis.size();\n\t\treturn accumulator;\n\t}\n\n\tprivate Stream<Aggregation[]> results() {\n\t\treturn loaders.parallelStream().map(this::results);\n\t}\n\n\tprivate Aggregation[] results(Iterable<Fact> facts) {\n\t\tAggregation[] result = new Aggregation[resultsSize()];\n\t\tfor (Fact fact : facts) {\n\t\t\tif (!check(fact)) continue;\n\t\t\tAxis.Component[] components = componentsOf(fact);\n\t\t\tint index = indexOf(components);\n\t\t\tif (result[index] == null) result[index] = new Aggregation(components);\n\t\t\tresult[index].append(fact);\n\t\t}\n\t\treturn result;\n\t}\n\n\tprivate synchronized void append(Aggregation[] results) {\n\t\tfor (int i = 0; i < results.length; i++) {\n\t\t\tif (result[i] == null) result[i] = results[i];\n\t\t\telse result[i].append(results[i]);\n\t\t}\n\t}\n\n\tprivate int indexOf(Axis.Component[] components) {\n\t\tint index = 0;\n\t\tfor (int i = 0; i < components.length; i++) {\n\t\t\ti *= axes.get(i).size();\n\t\t\ti += components[i].index();\n\t\t}\n\t\treturn index;\n\t}\n\n\tprivate boolean check(Fact item) {\n\t\tfor (Predicate<Fact> filter : filterList) if (!filter.test(item)) return false;\n\t\treturn true;\n\t}\n\n\tprivate Axis.Component[] componentsOf(Fact item) {\n\t\tAxis.Component[] components = new Axis.Component[groupByList.size()];\n\t\tfor (int i = 0; i < components.length; i++) components[i] = groupByList.get(i).apply(item);\n\t\treturn components;\n\t}\n\n\n\tpublic static class Fact extends Schema {\n\t\tpublic static final int SIZE = ")).output(mark("size")).output(literal(";\n\n\t\tpublic Fact(ByteStore store) {\n\t\t\tsuper(store);\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "getter").multiple("\n\n"))).output(literal("\n\n\t\t@Override\n\t\tpublic long id() {\n\t\t\treturn ")).output(mark("id")).output(literal("();\n\t\t}\n\n\t\t@Override\n\t\tpublic int size() {\n\t\t\treturn SIZE;\n\t\t}\n\t}\n\n\tpublic static class NullFact extends Fact {\n\t\tpublic static final int SIZE = ")).output(mark("size")).output(literal(";\n\n\t\tprivate NullFact() {\n\t\t\tsuper(null);\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "nullgetter").multiple("\n\n"))).output(literal("\n\n\t\t@Override\n\t\tpublic long id() {\n\t\t\treturn -1;\n\t\t}\n\n\t\t@Override\n\t\tpublic int size() {\n\t\t\treturn SIZE;\n\t\t}\n\t}\n\n\tprivate class Aggregation {\n\t\tprivate final Axis.Component[] components;\n\t\tprivate long aggregationCount = 0;\n\t\t")).output(mark("index", "field")).output(literal("\n\t\t")).output(mark("indicator", "field").multiple("\n")).output(literal("\n\n\t\tpublic Aggregation(Axis.Component[] components) {\n\t\t\tthis.components = components;\n\t\t}\n\n\t\tpublic void append(Fact fact) {\n\t\t\t")).output(mark("indicator", "sum").multiple("\n")).output(literal("\n\t\t\t")).output(mark("index", "append")).output(literal("\n\t\t\taggregationCount++;\n\t\t}\n\n\t\tpublic void append(Aggregation aggregation) {\n\t\t\t")).output(mark("indicator", "sumAggregation").multiple("\n")).output(literal("\n\t\t\t")).output(mark("index", "append2")).output(literal("\n\t\t\taggregationCount += aggregation.aggregationCount;\n\t\t}\n\n\t\tpublic long aggregationCount() {\n\t\t\treturn aggregationCount;\n\t\t}\n\n\t\t")).output(mark("index", "getter")).output(literal("\n\n\t\tpublic Axis.Component[] components() {\n\t\t\treturn components;\n\t\t}\n\n\t\t")).output(mark("indicator", "getter").multiple("\n\n")).output(literal("\n\t}\n\n\tpublic static class Loader implements Iterable<Fact> {\n\t\tprivate final Datasource datasource;\n\t\t")).output(expression().output(mark("split", "field"))).output(literal("\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tthis.datasource = datasource;\n\t\t\t")).output(expression().output(mark("split", "assign"))).output(literal("\n\t\t}\n\n\t\t@Override\n\t\tpublic Iterator<Fact> iterator() {\n\t\t\treturn StreamSupport.stream(spliteratorUnknownSize(datasource.leds(")).output(mark("split", "name")).output(literal("), Spliterator.SORTED), false).iterator();\n\t\t}\n\n\t\tpublic static List<Loader> facts(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "SetParameter"))).output(literal(") {\n\t\t\t")).output(mark("split", "name")).output(literal(" = ")).output(mark("split", "name")).output(literal(".isEmpty() ? all")).output(mark("split", "nameUpper")).output(literal("() : ")).output(mark("split", "name")).output(literal(";\n\t\t\treturn ")).output(mark("split", "name")).output(literal(".stream().map(d -> new Loader(datasource, d)).collect(java.util.stream.Collectors.toList());\n\t\t}\n\n\t\t")).output(expression().output(mark("split", "method"))).output(literal("\n\n\t\tpublic static class Datasource {\n\t\t\tprivate final File root;\n\t\t\tprivate final Timetag from;\n\t\t\tprivate final Timetag to;\n\n\t\t\tpublic Datasource(File root, Timetag from, Timetag to) {\n\t\t\t\tthis.root = root;\n\t\t\t\tthis.from = from;\n\t\t\t\tthis.to = to;\n\t\t\t}\n\n\t\t\tprivate LedStream<Fact> leds(")).output(mark("split", "parameter")).output(literal(") {\n\t\t\t\treturn LedStream.merged(StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(t -> on(t")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(")));\n\t\t\t}\n\n\t\t\tprivate LedStream<Fact> on(Timetag timetag")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\t\tFile file = new File(root + \"/\" ")).output(expression().output(literal("+ ")).output(mark("split", "name")).output(literal(" + \".\""))).output(literal(" + \"")).output(mark("name", "FirstUpperCase")).output(literal("/\", timetag.value() + \".led\");\n\t\t\t\treturn file.exists() ? new LedReader(file).read(Fact::new) : LedStream.empty();\n\t\t\t}\n\t\t}\n\t}\n}")),
			rule().condition((allTypes("index","total")), (trigger("field"))).output(literal("private long aggregationTotal = 0;\nprivate long last = 0;")),
			rule().condition((allTypes("index","total")), (trigger("append"))).output(literal("if (last != fact.id()) {\n\taggregationTotal++;\n\tlast = fact.id();\n}")),
			rule().condition((allTypes("index","total")), (trigger("append2"))).output(literal("aggregationTotal += aggregation.aggregationTotal;")),
			rule().condition((allTypes("index","total")), (trigger("getter"))).output(literal("public long aggregationTotal() {\n\treturn aggregationTotal;\n}")),
			rule().condition((type("index")), (trigger("field"))).output(literal("private final List<Long> ids = new java.util.ArrayList<>();")),
			rule().condition((type("index")), (trigger("append"))).output(literal("if (ids.isEmpty() || ids.get(ids.size() - 1) != fact.id()) ids.add(fact.id());")),
			rule().condition((type("index")), (trigger("append2"))).output(literal("ids.addAll(aggregation.ids);")),
			rule().condition((type("index")), (trigger("getter"))).output(literal("public List<Long> ids() {\n\treturn ids;\n}")),
			rule().condition((trigger("nbits"))).output(literal("NBits")),
			rule().condition((type("customFilter")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("() {\n\tfilterList.add(v -> ")).output(mark("cube", "firstUpperCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("Filter(v));\n\treturn this;\n}")),
			rule().condition((type("customFilter")), (trigger("staticmethod"))).output(literal("public static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact) {\n\t//TODO\n\treturn true;\n}")),
			rule().condition((type("customDimension")), (trigger("staticmethod"))).output(literal("public static ")).output(mark("name", "firstLowerCase")).output(expression().output(mark("isDistribution")).output(literal("Range"))).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("Function() {\n\treturn v -> null; //TODO\n}")),
			rule().condition((allTypes("dimension","categorical")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "firstUpperCase")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis", "firstUpperCase")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "firstLowerCase")).output(literal("Function(fact));\n}\n\npublic static Axis.Component ")).output(mark("name", "firstLowerCase")).output(literal("Function(Fact fact) {\n\treturn fact.")).output(mark("source")).output(literal("()")).output(expression().output(literal(".")).output(mark("child")).output(literal("()"))).output(literal(";\n}")),
			rule().condition((allTypes("dimension","distribution")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "firstUpperCase")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis", "firstUpperCase")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "firstLowerCase")).output(literal("Function(fact));\n}\n\nprivate static Axis.Component ")).output(mark("name", "firstLowerCase")).output(literal("Function(Fact fact) {\n\treturn ")).output(mark("axis", "firstUpperCase")).output(literal(".instance().rangeOf(fact.")).output(mark("source", "firstLowerCase")).output(literal("());\n}")),
			rule().condition((allTypes("customDimension","continuous")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis")).output(literal(".Range> ranges) {//TODO\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ranges));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("cube", "firstUpperCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis")).output(literal(".Range> rangos) {\n\treturn rangos.contains(")).output(mark("cube", "firstUpperCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("Function(fact));\n}")),
			rule().condition((type("indicator")), (trigger("sum"))).output(mark("name", "firstLowerCase")).output(literal(" += fact.")).output(mark("name", "firstLowerCase")).output(literal("();")),
			rule().condition((type("indicator")), (trigger("sumaggregation"))).output(mark("name", "firstLowerCase")).output(literal(" += aggregation.")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","average")), (trigger("field"))).output(literal("double ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","sum")), (trigger("field"))).output(literal("long ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","average")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(" / (double) ")).output(mark("index")).output(literal(";\n}")),
			rule().condition((type("total")), (trigger("index"))).output(literal("aggregationTotal")),
			rule().condition((type("index")), (trigger("index"))).output(literal("ids.size()")),
			rule().condition((allTypes("indicator","sum")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("indicator","average")), (trigger("staticmethod"))).output(literal("public static double ")).output(mark("name")).output(literal("(Fact fact) {\n\treturn 0.;\n}")),
			rule().condition((allTypes("indicator","sum")), (trigger("staticmethod"))).output(literal("public static long ")).output(mark("name")).output(literal("(Fact fact) {\n\treturn 0;\n}")),
			rule().condition((type("split")), (trigger("parameter"))).output(literal("String ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("setparameter"))).output(literal("Set<String> ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("assign"))).output(literal("this.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("name"))).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("nameupper"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("split")), (trigger("field"))).output(literal("private final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("method"))).output(literal("private static Set<String> all")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\treturn java.util.Set.of(")).output(mark("value", "quoted").multiple(", ")).output(literal(");\n}")),
			rule().condition((trigger("dimension"))).output(literal("public static Predicate<")).output(mark("cube", "FirstUpperCase")).output(literal(".Fact> ")).output(mark("axis", "firstLowerCase")).output(literal("(Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "firstLowerCase")).output(literal("List) {\n\treturn r -> ")).output(mark("axis", "firstLowerCase")).output(literal("List.contains(r.")).output(mark("axis", "firstLowerCase")).output(literal("());\n}\n\npublic static Function<")).output(mark("cube", "FirstUpperCase")).output(literal(".Fact, String> ")).output(mark("axis", "firstLowerCase")).output(literal("() {\n\treturn r -> r.")).output(mark("axis", "firstLowerCase")).output(literal("().id();\n}")),
			rule().condition((type("column")), (anyTypes("id","longInteger")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((allTypes("column","datetime")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn java.time.Instant.ofEpochMilli(bitBuffer.get")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal("));\n}")),
			rule().condition((allTypes("column","date")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn java.time.LocalDate.ofEpochDay(Short.toUnsignedInt(bitBuffer.get")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(")));\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("getter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("type", "firstUpperCase")).output(literal(".instance().component(bitBuffer.getIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal("));\n}")),
			rule().condition((allTypes("column","integer")), (trigger("getter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Integer")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((allTypes("column","bool")), (trigger("getter"))).output(literal("public Boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\tbyte value = bitBuffer.getByteNBits(")).output(mark("offset")).output(literal(", 2);\n\treturn value == NULL ? null : value == 2;\n}")),
			rule().condition((allTypes("column","real")), (attribute("size", "32")), (trigger("getter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","real")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("nullgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("type", "firstUpperCase")).output(literal(".instance().NA;\n}")),
			rule().condition((allTypes("column","integer")), (trigger("nullgetter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn -1;\n}")),
			rule().condition((allTypes("column","real")), (attribute("size", "32")), (trigger("nullgetter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn -1f;\n}")),
			rule().condition((allTypes("column","real")), (trigger("nullgetter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn -1;\n}")),
			rule().condition((allTypes("column","bool")), (trigger("nullgetter"))).output(literal("public Boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn null;\n}"))
		);
	}
}