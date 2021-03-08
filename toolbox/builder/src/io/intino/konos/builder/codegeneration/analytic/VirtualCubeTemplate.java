package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class VirtualCubeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("cube","src"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport ")).output(mark("package")).output(literal(".analytic.axes.*;\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(java.util.List<Loader> loaders) {\n\t\tsuper(loaders);\n\t}\n\n\tpublic static long ")).output(mark("name", "firstLowerCase")).output(literal("Id(")).output(mark("name", "FirstUpperCase")).output(literal(".Fact fact) {\n\t\treturn 0L;//TODO\n\t}\n\n\t")).output(expression().output(mark("customDimension", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customIndicator", "staticMethod").multiple("\n\n"))).output(literal("\n\n\tpublic static long idOf(")).output(mark("mainCube", "idOf")).output(literal(") {\n\t\treturn 0L; //TODO\n\t}\n\n\tpublic static class Loader extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Loader {\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tsuper(datasource")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(");\n\t\t}\n\n\t\t// TODO write here a cache if necessary\n\t}\n}")),
			rule().condition((trigger("idof"))).output(mark("", "FirstUpperCase")).output(literal(".Fact ")).output(mark("", "firstLowerCase")),
			rule().condition((type("cube"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.led.LedReader;\nimport io.intino.alexandria.led.LedStream;\nimport io.intino.alexandria.led.Schema;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\nimport io.intino.alexandria.led.util.iterators.MergedIterator;\nimport io.intino.alexandria.led.util.iterators.StatefulIterator;\n\nimport ")).output(mark("package")).output(literal(".analytic.axes.*;\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\n\nimport java.io.File;\nimport java.util.*;\nimport java.util.function.Function;\nimport java.util.function.Predicate;\nimport java.util.stream.Stream;\nimport java.util.stream.StreamSupport;\n\nimport static java.util.Comparator.comparingLong;\nimport static java.util.Spliterators.spliteratorUnknownSize;\n\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic static final Fact NULL_FACT = new NullFact();\n\n\tprivate final List<? extends Loader> loaders;\n\tprivate final List<Predicate<Fact>> filterList = new ArrayList<>();\n\tprivate final List<Axis> axes = new ArrayList<>();\n\tprivate final Map<Axis, Set<Axis.Component>> components = new HashMap<>();\n\tprivate final List<Function<Fact, Axis.Component>> groupByList = new ArrayList<>();\n\tprivate Aggregation[] result;\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("(List<? extends Loader> loaders) {\n\t\tthis.loaders = loaders;\n\t}\n\n\t")).output(expression().output(mark("dimension", "method").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "method").multiple("\n\n"))).output(literal("\n\n\tpublic Abstract")).output(mark("name", "firstUpperCase")).output(literal(" execute() {\n\t\tresult = new Aggregation[resultsSize()];\n\t\tresults().forEach(this::append);\n\t\tfillCategories();\n\t\tcalculateTotals();\n\n\t\treturn this;\n\t}\n\n\tpublic Iterator<Fact> detail() {\n\t\tMergedIterator<Fact> iterator = new MergedIterator<>(loaders.stream().map(Iterable::iterator), comparingLong(Fact::id));\n\t\treturn StreamSupport.stream(spliteratorUnknownSize(iterator, Spliterator.SORTED), false).filter(this::check).iterator();\n\t}\n\n\tpublic Aggregation aggregation(List<Axis.Component> components) {\n\t\treturn result[indexOf(components.toArray(new Axis.Component[0]))];\n\t}\n\n\tprivate void fillCategories() {\n\t\tfor (Aggregation aggregation : result) {\n\t\t\tif (aggregation == null) continue;\n\t\t\tfor (int i = 0; i < aggregation.components.length; i++)\n\t\t\t\tcomponents.get(axes.get(i)).add(aggregation.components[i]);\n\t\t}\n\t}\n\n\tprivate void calculateTotals() {\n\t\tfor (Loader loader : loaders) {\n\t\t\tif(loader.axes().size() != axes.size()) return;\n\t\t\tfor (int i = 0; i < loader.totals().length; i++) {\n\t\t\t\tif (result[i] == null) continue;\n\t\t\t\tresult[i].append(loader.totals()[i]);\n\t\t\t}\n\t\t}\n\t}\n\n\tprivate int resultsSize() {\n\t\tint accumulator = 1;\n\t\tfor (Axis axis : axes) accumulator *= axis.size() + 1;\n\t\treturn accumulator;\n\t}\n\n\tprivate Stream<Aggregation[]> results() {\n\t\treturn loaders.parallelStream().map(this::results);\n\t}\n\n\tprivate Aggregation[] results(Iterable<Fact> facts) {\n\t\tAggregation[] result = new Aggregation[resultsSize()];\n\t\tfor (Fact fact : facts) {\n\t\t\tif (!check(fact)) continue;\n\t\t\tAxis.Component[] components = componentsOf(fact);\n\t\t\tint index = indexOf(components);\n\t\t\tif (result[index] == null) result[index] = new Aggregation(components);\n\t\t\tresult[index].append(fact);\n\t\t}\n\t\treturn result;\n\t}\n\n\tprivate synchronized void append(Aggregation[] results) {\n\t\tfor (int i = 0; i < results.length; i++) {\n\t\t\tif (results[i] == null) continue;\n\t\t\tif (result[i] == null) result[i] = results[i];\n\t\t\telse result[i].append(results[i]);\n\t\t}\n\t}\n\n\tprivate int indexOf(Axis.Component[] components) {\n\t\tint index = 0;\n\t\tfor (int i = 0; i < components.length; i++) {\n\t\t\tindex *= axes.get(i).size();\n\t\t\tindex += components[i].index();\n\t\t}\n\t\treturn index;\n\t}\n\n\tprivate boolean check(Fact item) {\n\t\tfor (Predicate<Fact> filter : filterList) if (!filter.test(item)) return false;\n\t\treturn true;\n\t}\n\n\tprivate Axis.Component[] componentsOf(Fact item) {\n\t\tAxis.Component[] components = new Axis.Component[groupByList.size()];\n\t\tfor (int i = 0; i < components.length; i++) components[i] = groupByList.get(i).apply(item);\n\t\treturn components;\n\t}\n\n\tpublic static class Fact {\n\n\t\t")).output(expression().output(mark("cube", "fieldFact").multiple("\n"))).output(literal("\n\n\t\tpublic Fact(")).output(mark("cube", "factParameter").multiple(", ")).output(literal(") {\n\t\t\t")).output(mark("cube", "assignFact").multiple("\n")).output(literal("\n\t\t}\n\n\t\tpublic long id() {\n\t\t\treturn ")).output(mark("mainCube", "firstLowerCase")).output(literal(".id();\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "factGetter").multiple("\n\n"))).output(literal("\n\n\t\tpublic int size() {\n\t\t\treturn ")).output(mark("cube", "size").multiple(" + ")).output(literal(";\n\t\t}\n\n\t    @Override\n\t    public boolean equals(Object o) {\n\t    \tif(o == null || o.getClass() != getClass()) return false;\n\t    \tfinal Fact other = (Fact) o;\n\t    \treturn Objects.equals(")).output(mark("mainCube", "firstLowerCase")).output(literal(", other.")).output(mark("mainCube", "firstLowerCase")).output(literal(");\n\t    }\n\n\t    @Override\n\t    public int hashCode() {\n\t    \treturn ")).output(mark("mainCube", "firstLowerCase")).output(literal(".hashCode();\n\t    }\n\n\t    @Override\n\t    public String toString() {\n\t    \treturn \"")).output(mark("name")).output(literal("Fact{\" +\n\t    \t        \"")).output(mark("mainCube")).output(literal("=\" + ")).output(mark("mainCube", "firstLowerCase")).output(literal(" +\n\t    \t        \", ")).output(mark("joinCube")).output(literal("=\" + ")).output(mark("joinCube", "firstLowerCase")).output(literal(" +\n\t    \t        \"}\";\n\t    }\n\t}\n\n\tpublic static class NullFact extends Fact {\n\n\t\tprivate NullFact() {\n\t\t\tsuper(")).output(mark("cube", "nullFactParameter").multiple(", ")).output(literal(");\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "nullgetter").multiple("\n\n"))).output(literal("\n\n\t\t@Override\n\t\tpublic long id() {\n\t\t\treturn 0;\n\t\t}\n\t}\n\n\tpublic static class Aggregation {\n\n\t\tprivate final Axis.Component[] components;\n\t\tprivate long aggregationCount = 0;\n\t\t")).output(expression().output(mark("index", "field"))).output(literal("\n\t\t")).output(expression().output(mark("indicator", "field").multiple("\n"))).output(literal("\n\t\tprivate long total")).output(mark("name", "FirstUpperCase")).output(literal(";\n\n\t\tpublic Aggregation(Axis.Component[] components) {\n\t\t\tthis.components = components;\n\t\t}\n\n\t\tpublic void append(Fact fact) {\n\t\t\t")).output(expression().output(mark("indicator", "sum").multiple("\n"))).output(literal("\n\t\t\t")).output(expression().output(mark("index", "append"))).output(literal("\n\t\t\taggregationCount++;\n\t\t}\n\n\t\tpublic void append(Aggregation aggregation) {\n\t\t\t")).output(expression().output(mark("indicator", "sumAggregation").multiple("\n"))).output(literal("\n\t\t\t")).output(expression().output(mark("index", "append2"))).output(literal("\n\t\t}\n\n\t\tpublic Aggregation append(long ids) {\n\t\t\tthis.total")).output(mark("name", "FirstUpperCase")).output(literal(" += ids;\n\t\t\treturn this;\n\t\t}\n\n\t\tpublic long total")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\t\t\treturn total")).output(mark("name", "FirstUpperCase")).output(literal(";\n\t\t}\n\n\t\t")).output(mark("index", "getter")).output(literal("\n\n\t\tpublic Axis.Component[] components() {\n\t\t\treturn components;\n\t\t}\n\n\t\t")).output(expression().output(mark("indicator", "getter").multiple("\n\n"))).output(literal("\n\t}\n\n\tpublic static class Loader implements Iterable<Fact> {\n\n\t\tprivate final StatefulIterator<")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact> ids;\n\t\tprivate final List<Axis> axes = new ArrayList<>();\n\t\tprivate long[] totals;\n\t\tprivate final List<Predicate<")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact>> filters = new ArrayList<>();\n\t\tprivate final List<Function<")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact, Axis.Component>> groupsBy = new ArrayList<>();\n\t\tprotected final Datasource datasource;\n\t\t")).output(expression().output(mark("split", "field"))).output(literal("\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tthis.datasource = datasource;\n\t\t\t")).output(expression().output(mark("split", "assign"))).output(literal("\n\t\t\tthis.ids = StatefulIterator.of(new ")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Loader(new ")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Loader.Datasource(this.datasource.root, this.datasource.from, this.datasource.to)")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(").iterator());\n\t\t}\n\n\t\t@Override\n\t\tpublic Iterator<Fact> iterator() {\n\t\t\tStatefulIterator<")).output(mark("mainCube", "FirstUpperCase")).output(literal(".Fact> facts = StatefulIterator.of(new ")).output(mark("mainCube", "FirstUpperCase")).output(literal(".Loader(new ")).output(mark("mainCube", "FirstUpperCase")).output(literal(".Loader.Datasource(this.datasource.root, this.datasource.from, this.datasource.to)")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(").iterator());\n\t\t\tprocess(ids.current());\n\t\t\tids.next();\n\t\t\treturn StreamSupport.stream(Spliterators.spliteratorUnknownSize(facts, Spliterator.SORTED), false)\n\t\t\t\t\t.map(f -> {\n\t\t\t\t\t\tlong id = ")).output(mark("name", "FirstUpperCase")).output(literal(".idOf(facts.current());\n\t\t\t\t\t\twhile (ids.current() != null && ids.current().id() < id) {\n\t\t\t\t\t\t\tids.next();\n\t\t\t\t\t\t\tprocess(ids.current());\n\t\t\t\t\t\t}\n\t\t\t\t\t\tif (!facts.hasNext()) end();\n\t\t\t\t\t\treturn new Fact(f, ids.current() != null && ids.current().id() == id ?\n\t\t\t\t\t\t\t\tids.current() : Abstract")).output(mark("joinCube", "FirstUpperCase")).output(literal(".NULL_FACT);\n\t\t\t\t\t})\n\t\t\t\t\t.iterator();\n\t\t}\n\n\t\tprivate void end() {\n\t\t\twhile (ids.hasNext()) {\n\t\t\t\tids.next();\n\t\t\t\tprocess(ids.current());\n\t\t\t}\n\t\t}\n\n\t\tprivate int totalsSize() {\n\t\t\tint accumulator = 1;\n\t\t\tfor (Axis axis : axes) accumulator *= axis.size();\n\t\t\treturn accumulator;\n\t\t}\n\n\t\tprivate void process(")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact fact) {\n\t\t\tif (fact == null) return;\n\t\t\tif (totals == null) totals = new long[totalsSize()];\n\t\t\tfor (Predicate<")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact> filter : filters) if (!filter.test(fact)) return;\n\t\t\tAxis.Component[] components = componentsOf(fact);\n\t\t\ttotals[indexOf(components)] += 1;\n\t\t}\n\n\t\tprivate int indexOf(Axis.Component[] components) {\n\t\t\tint index = 0;\n\t\t\tfor (int i = 0; i < components.length; i++) {\n\t\t\t\ti *= axes.get(i).size();\n\t\t\t\ti += components[i].index();\n\t\t\t}\n\t\t\treturn index;\n\t\t}\n\n\t\tprivate Axis.Component[] componentsOf(")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact item) {\n\t\t\tAxis.Component[] components = new Axis.Component[axes.size()];\n\t\t\tfor (int i = 0; i < components.length; i++) components[i] = groupsBy.get(i).apply(item);\n\t\t\treturn components;\n\t\t}\n\n\t\tprivate long totalOf(List<Axis.Component> categories) {\n\t\t\treturn totals[indexOf(categories.toArray(new Axis.Component[0]))];\n\t\t}\n\n\t\tprivate void filter(Predicate<")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact> filter) {\n\t\t\tif (filter == null) return;\n\t\t\tfilters.add(filter);\n\t\t}\n\n\t\tprivate void by(Axis axis, Function<")).output(mark("joinCube", "FirstUpperCase")).output(literal(".Fact, Axis.Component> dimension) {\n\t\t\taxes.add(axis);\n\t\t\tif (dimension == null) return;\n\t\t\tgroupsBy.add(dimension);\n\t\t}\n\n\t\tpublic List<Axis> axes() {\n\t\t\treturn axes;\n\t\t}\n\n\t\tpublic long[] totals() {\n\t\t\treturn totals;\n\t\t}\n\n\t\tpublic static class Datasource {\n\t\t\tprivate final File root;\n\t\t\tprivate final Timetag from;\n\t\t\tprivate final Timetag to;\n\n\t\t\tpublic Datasource(File root, Timetag from, Timetag to) {\n\t\t\t\tthis.root = root;\n\t\t\t\tthis.from = from;\n\t\t\t\tthis.to = to;\n\t\t\t}\n\n\t\t\tpublic Timetag from() {\n\t\t\t\treturn from;\n\t\t\t}\n\n\t\t\tpublic Timetag to() {\n\t\t\t\treturn to;\n\t\t\t}\n\t\t}\n\t}\n}")),
			rule().condition((allTypes("index","total")), (trigger("field"))).output(literal("private long aggregationTotal = 0;\nprivate long last = 0;")),
			rule().condition((allTypes("index","total")), (trigger("append"))).output(literal("if (last != fact.id()) {\n\taggregationTotal++;\n\tlast = fact.id();\n}")),
			rule().condition((allTypes("index","total")), (trigger("append2"))).output(literal("aggregationTotal += aggregation.aggregationTotal;")),
			rule().condition((allTypes("index","total")), (trigger("getter"))).output(literal("public long aggregationTotal() {\n\treturn aggregationTotal;\n}")),
			rule().condition((type("index")), (trigger("field"))).output(literal("private final List<Long> ids = new java.util.ArrayList<>();")),
			rule().condition((type("index")), (trigger("append"))).output(literal("if (ids.isEmpty() || ids.get(ids.size() - 1) != fact.id()) ids.add(fact.id());")),
			rule().condition((type("index")), (trigger("append2"))).output(literal("ids.addAll(aggregation.ids);")),
			rule().condition((type("index")), (trigger("getter"))).output(literal("public List<Long> ids() {\n\treturn ids;\n}")),
			rule().condition((trigger("assignfact"))).output(literal("this.")).output(mark("", "FirstLowerCase")).output(literal(" = ")).output(mark("", "FirstLowerCase")).output(literal(";")),
			rule().condition((trigger("fieldfact"))).output(literal("private final ")).output(mark("", "FirstUpperCase")).output(literal(".Fact ")).output(mark("", "FirstLowerCase")).output(literal(";")),
			rule().condition((trigger("factparameter"))).output(mark("", "FirstUpperCase")).output(literal(".Fact ")).output(mark("", "FirstLowerCase")),
			rule().condition((trigger("nullfactparameter"))).output(literal("Abstract")).output(mark("", "FirstUpperCase")).output(literal(".NULL_FACT")),
			rule().condition((trigger("size"))).output(mark("", "FirstLowerCase")).output(literal(".size()")),
			rule().condition((trigger("nbits"))).output(literal("NBits")),
			rule().condition((type("customFilter")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("() {\n\tfilterList.add(v -> ")).output(mark("cube", "firstUpperCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("Filter(v));\n\treturn this;\n}")),
			rule().condition((type("customFilter")), (trigger("staticmethod"))).output(literal("public static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact) {\n\treturn true;\n}")),
			rule().condition((type("dimension")), (trigger("staticmethod"))).output(literal("public static Predicate<Fact> ")).output(mark("name", "firstLowerCase")).output(literal("Predicate(Set<")).output(mark("name", "FirstUpperCase")).output(literal(".Component> ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\treturn v -> false; //TODO\n}\n\npublic static Function<Fact, Axis.Component> ")).output(mark("name", "firstLowerCase")).output(literal("Function() {\n\treturn v -> null; //TODO\n}")),
			rule().condition((allTypes("dimension","categorical")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "firstLowerCase")).output(literal("Function(fact));\n}\n\npublic static Axis.Component ")).output(mark("name", "firstLowerCase")).output(literal("Function(Fact fact) {\n\treturn fact.")).output(mark("source", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("()")).output(expression().output(literal(".")).output(mark("child", "snakeCaseToCamelCase", "firstLowerCase"))).output(literal(";\n}")),
			rule().condition((allTypes("dimension","continuous")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "firstLowerCase")).output(literal("Function(fact));\n}\n\nprivate static Axis.Component ")).output(mark("name", "firstLowerCase")).output(literal("Function(Fact fact) {\n\treturn ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".rangeOf(fact.")).output(mark("source", "firstLowerCase")).output(literal("());\n}")),
			rule().condition((type("indicator")), (trigger("sum"))).output(mark("name", "firstLowerCase")).output(literal(" += fact.")).output(mark("source", "firstLowerCase")).output(literal("();")),
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
			rule().condition((type("split")), (trigger("setparameter"))).output(literal("java.util.Set<String> ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("assign"))).output(literal("this.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("name"))).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("nameupper"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("split")), (trigger("field"))).output(literal("protected final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("method"))).output(literal("private static Set<String> all")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\treturn java.util.Set.of(")).output(mark("value", "quoted").multiple(", ")).output(literal(");\n}")),
			rule().condition((trigger("dimension"))).output(literal("public static Predicate<")).output(mark("cube", "FirstUpperCase")).output(literal(".Fact> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List) {\n\treturn r -> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List.contains(r.")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("());\n}\n\npublic static Function<")).output(mark("cube", "FirstUpperCase")).output(literal(".Fact, String> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn r -> r.")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("().id();\n}")),
			rule().condition((allTypes("column","int","unsigned")), (trigger("factgetter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","int")), (trigger("factgetter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","float")), (trigger("factgetter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","double")), (trigger("factgetter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (anyTypes("id","long")), (trigger("factgetter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (allTypes("byte","unsigned")), (trigger("factgetter"))).output(literal("public short ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (type("byte")), (trigger("factgetter"))).output(literal("public byte ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (allTypes("short","unsigned")), (trigger("factgetter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (type("short")), (trigger("factgetter"))).output(literal("public short ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (type("boolean")), (trigger("factgetter"))).output(literal("public boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("factgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (trigger("factgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","virtual")), (trigger("factgetter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube", "firstLowerCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("nullgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("type", "firstUpperCase")).output(literal(".NA;\n}")),
			rule().condition((allTypes("column","int","unsigned")), (trigger("nullgetter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0;\n}")),
			rule().condition((allTypes("column","int")), (trigger("nullgetter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0;\n}")),
			rule().condition((type("column")), (anyTypes("id","long")), (trigger("nullgetter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0L;\n}")),
			rule().condition((allTypes("column","short","unsigned")), (trigger("nullgetter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0;\n}")),
			rule().condition((allTypes("column","short")), (trigger("nullgetter"))).output(literal("public short ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0;\n}")),
			rule().condition((allTypes("column","byte","unsigned")), (trigger("nullgetter"))).output(literal("public short ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0;\n}")),
			rule().condition((allTypes("column","byte")), (trigger("nullgetter"))).output(literal("public byte ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0;\n}")),
			rule().condition((allTypes("column","float")), (trigger("nullgetter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0.0f;\n}")),
			rule().condition((allTypes("column","double")), (trigger("nullgetter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn 0.0f;\n}")),
			rule().condition((allTypes("column","boolean")), (trigger("nullgetter"))).output(literal("public boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn false;\n}")),
			rule().condition((allTypes("column","boolean")), (trigger("nullgetter"))).output(literal("public boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn false;\n}")),
			rule().condition((allTypes("column","boolean")), (trigger("nullgetter"))).output(literal("public boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn false;\n}"))
		);
	}
}