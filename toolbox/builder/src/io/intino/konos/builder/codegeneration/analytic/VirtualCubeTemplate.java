package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class VirtualCubeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("cube","src"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport ")).output(mark("package")).output(literal(".analytic.axes.*;\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(java.util.List<Loader> loaders) {\n\t\tsuper(loaders);\n\t}\n\n\tpublic static long ")).output(mark("name", "firstLowerCase")).output(literal("Id(Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Fact fact) {\n\t\treturn 0L;//TODO\n\t}\n\n\t")).output(expression().output(mark("customDimension", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customIndicator", "staticMethod").multiple("\n\n"))).output(literal("\n\n\tpublic static class Loader extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Loader {\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tsuper(datasource")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(");\n\t\t}\n\n\t\t// TODO write here a cache if necessary\n\t}\n}")),
			rule().condition((type("cube"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.led.LedReader;\nimport io.intino.alexandria.led.LedStream;\nimport io.intino.alexandria.led.Schema;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\nimport io.intino.alexandria.led.util.iterators.MergedIterator;\nimport io.intino.alexandria.led.util.iterators.StatefulIterator;\n\nimport ")).output(mark("package")).output(literal(".analytic.axes.*;\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\n\nimport java.io.File;\nimport java.util.*;\nimport java.util.function.Function;\nimport java.util.function.Predicate;\nimport java.util.stream.Stream;\nimport java.util.stream.StreamSupport;\n\nimport static java.util.Comparator.comparingLong;\nimport static java.util.Spliterators.spliteratorUnknownSize;\n\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprivate static final String ID_SEP = \" - \";\n\tprivate static Fact nullFact = new NullFact();\n\tprivate final List<? extends Loader> loaders;\n\tprivate final List<Predicate<View>> filterList = new ArrayList<>();\n\tprivate final List<Axis> axes = new ArrayList<>();\n\tprivate final Map<Axis, Set<Axis.Component>> components = new HashMap<>();\n\tprivate final List<Function<View, Axis.Component>> groupByList = new ArrayList<>();\n\tprivate Aggregation[] result;\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("(List<? extends Loader> loaders) {\n\t\tthis.loaders = loaders;\n\t}\n\n\tpublic static Fact nullFact() {\n\t\treturn nullFact;\n\t}\n\n\t")).output(expression().output(mark("dimension", "method").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "method").multiple("\n\n"))).output(literal("\n\n\tpublic Abstract")).output(mark("name", "firstUpperCase")).output(literal(" execute() {\n\t\tresult = new Aggregation[resultsSize()];\n\t\tresults().forEach(this::append);\n\t\tfillCategories();\n\t\tcalculateTotals();\n\n\t\treturn this;\n\t}\n\n\tpublic Iterator<View> detail() {\n\t\tMergedIterator<View> iterator = new MergedIterator<>(loaders.stream().map(Iterable::iterator), comparingLong(View::id));\n\t\treturn StreamSupport.stream(spliteratorUnknownSize(iterator, Spliterator.SORTED), false).filter(this::check).iterator();\n\t}\n\n\tprivate void fillCategories() {\n\t\tfor (Aggregation aggregation : result)\n\t\t\tfor (int i = 0; i < aggregation.components.length; i++)\n\t\t\t\tcomponents.get(axes.get(i)).add(aggregation.components[i]);\n\t}\n\n\tprivate void calculateTotals() {\n\t\tfor (Loader loader : loaders) {\n\t\t\tif(loader.axes().size() != axes.size()) return;\n\t\t\tfor (int i = 0; i < loader.totals().length; i++) {\n\t\t\t\tif(result[i] == null) continue;\n\t\t\t\tresult[i].append(loader.totals()[i]);\n\t\t\t}\n\t\t}\n\t}\n\n\tprivate int resultsSize() {\n\t\tint accumulator = 1;\n\t\tfor (Axis axis : axes) accumulator *= axis.size();\n\t\treturn accumulator;\n\t}\n\n\tprivate Stream<Aggregation[]> results() {\n\t\treturn loaders.parallelStream().map(this::results);\n\t}\n\n\tprivate Aggregation[] results(Iterable<View> ledger) {\n\t\tAggregation[] result = new Aggregation[resultsSize()];\n\t\tfor (View view : ledger) {\n\t\t\tif (!check(view)) continue;\n\t\t\tAxis.Component[] components = componentsOf(view);\n\t\t\tint index = indexOf(components);\n\t\t\tif (result[index] == null) result[index] = new Aggregation(components);\n\t\t\tresult[index].append(view);\n\t\t}\n\t\treturn result;\n\t}\n\n\tprivate synchronized void append(Aggregation[] results) {\n\t\tfor (int i = 0; i < results.length; i++) {\n\t\t\tif (result[i] == null) result[i] = results[i];\n\t\t\telse result[i].append(results[i]);\n\t\t}\n\t}\n\n\tprivate int indexOf(Axis.Component[] components) {\n\t\tint index = 0;\n\t\tfor (int i = 0; i < components.length; i++) {\n\t\t\ti *= axes.get(i).size();\n\t\t\ti += components[i].index();\n\t\t}\n\t\treturn index;\n\t}\n\n\tprivate boolean check(View item) {\n\t\tfor (Predicate<View> filter : filterList) if (!filter.test(item)) return false;\n\t\treturn true;\n\t}\n\n\tprivate Axis.Component[] componentsOf(View item) {\n\t\tAxis.Component[] components = new Axis.Component[groupByList.size()];\n\t\tfor (int i = 0; i < components.length; i++) components[i] = groupByList.get(i).apply(item);\n\t\treturn components;\n\t}\n\n\tpublic static class View {\n\t\t")).output(mark("cube", "fieldView").multiple("\n")).output(literal("\n\n\t\tpublic View(Fact fact, Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Fact ")).output(mark("name", "firstLowerCase")).output(literal("Fact) {\n\t\t\t")).output(mark("cube", "assingView").multiple("\n")).output(literal("\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "viewGetter").multiple("\n"))).output(literal("\n\t}\n\n\tpublic static class Fact extends Schema {\n\t\tpublic static final int SIZE = ")).output(mark("size").multiple("+")).output(literal(";\n\n\t\tpublic Fact(ByteStore store) {\n\t\t\tsuper(store);\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "getter").multiple("\n"))).output(literal("\n\n\t\t@Override\n\t\tprotected long id() {\n\t\t\treturn ")).output(mark("id")).output(literal("();\n\t\t}\n\n\t\t@Override\n\t\tpublic int size() {\n\t\t\treturn SIZE;\n\t\t}\n\t}\n\n\tpublic static class NullFact extends Fact {\n\t\tpublic static final int SIZE = ")).output(mark("size")).output(literal(";\n\n\t\tprivate NullFact() {\n\t\t\tsuper(null);\n\t\t}\n\n\t\t")).output(expression().output(mark("column", "nullgetter").multiple("\n"))).output(literal("\n\n\t\t@Override\n\t\tprotected long id() {\n\t\t\treturn -1;\n\t\t}\n\n\t\t@Override\n\t\tpublic int size() {\n\t\t\treturn SIZE;\n\t\t}\n\t}\n\n\tprivate class Aggregation {\n\t\tprivate final Axis.Component[] components;\n\t\tprivate final List<Long> ids = new ArrayList<>();\n\t\t")).output(mark("indicator", "field").multiple("\n")).output(literal("\n\t\tprivate long total")).output(mark("name", "FirstUpperCase")).output(literal(";\n\n\n\t\tpublic Aggregation(Axis.Component[] components) {\n\t\t\tthis.components = components;\n\t\t}\n\n\t\tpublic void append(View view) {\n\t\t\t")).output(mark("indicator", "sum").multiple("\n")).output(literal("\n\t\t\tif (ids.isEmpty() || ids.get(ids.size() - 1) != view.id()) ids.add(view.id());\n\t\t}\n\n\t\tpublic void append(Aggregation aggregation) {\n\t\t\t")).output(mark("indicator", "sumAggregation").multiple("\n")).output(literal("\n\t\t\tids.addAll(aggregation.ids);\n\t\t}\n\n\t\tpublic Aggregation append(long ids) {\n\t\t\tthis.total")).output(mark("name", "FirstUpperCase")).output(literal(" += ids;\n\t\t\treturn this;\n\t\t}\n\n\t\tpublic long total")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\t\t\treturn total")).output(mark("name", "FirstUpperCase")).output(literal(";\n\t\t}\n\n\n\t\tpublic Axis.Component[] components() {\n\t\t\treturn components;\n\t\t}\n\n\t\t")).output(expression().output(mark("indicator", "getter").multiple("\n\n"))).output(literal("\n\t}\n\n\tpublic static class Loader implements Iterable<View> {\n\t\tprivate final StatefulIterator<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View> ids;\n\t\tprivate final List<Axis> axes = new ArrayList<>();\n\t\tprivate long[] totals;\n\t\tprivate final List<Predicate<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View>> filters = new ArrayList<>();\n\t\tprivate final List<Function<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View, Axis.Component>> groupsBy = new ArrayList<>();\n\t\tprivate final Datasource datasource;\n\t\t")).output(expression().output(mark("split", "field"))).output(literal("\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tthis.datasource = datasource;\n\t\t\t")).output(expression().output(mark("split", "assign"))).output(literal("\n\t\t\tthis.ids = StatefulIterator.of(new Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Loader(new Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Loader.Datasource(this.datasource.root, this.datasource.from, this.datasource.to)")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(").iterator());\n\t\t}\n\n\t\t@Override\n\t\tpublic Iterator<View> iterator() {\n\t\t\tStatefulIterator<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Fact> facts = StatefulIterator.of(datasource.leds(")).output(expression().output(mark("split", "name"))).output(literal("));\n\t\t\tprocess(ids.current());\n\t\t\treturn StreamSupport.stream(Spliterators.spliteratorUnknownSize(facts, Spliterator.SORTED), false)\n\t\t\t\t\t.map(v -> {\n\t\t\t\t\t\twhile (ids.current() != null && ")).output(mark("name", "FirstUpperCase")).output(literal(".matches(ids.current(), ventas.current())) {\n\t\t\t\t\t\t\tids.next();\n\t\t\t\t\t\t\tprocess(ids.current());\n\t\t\t\t\t\t}\n\t\t\t\t\t\tif (!facts.hasNext()) end();\n\t\t\t\t\t\treturn new View(v, ids.current() != null && ids.current().id() == id ?\n\t\t\t\t\t\t\t\tids.current().fact() : Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".nullFact());\n\t\t\t\t\t})\n\t\t\t\t\t.iterator();\n\t\t}\n\n\t\tprivate void end() {\n\t\t\twhile (ids.hasNext()) {\n\t\t\t\tids.next();\n\t\t\t\tprocess(ids.current());\n\t\t\t}\n\t\t}\n\n\t\tprivate int totalsSize() {\n\t\t\tint accumulator = 1;\n\t\t\tfor (Axis axis : axes) accumulator *= axis.size();\n\t\t\treturn accumulator;\n\t\t}\n\n\t\tprivate void process(Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View view) {\n\t\t\tif (view == null) return;\n\t\t\tif (totals == null) totals = new long[totalsSize()];\n\t\t\tfor (Predicate<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View> filter : filters) if (!filter.test(view)) return;\n\t\t\tAxis.Component[] components = componentsOf(view);\n\t\t\ttotals[indexOf(components)] += 1;\n\t\t}\n\n\t\tprivate int indexOf(Axis.Component[] components) {\n\t\t\tint index = 0;\n\t\t\tfor (int i = 0; i < components.length; i++) {\n\t\t\t\ti *= axes.get(i).size();\n\t\t\t\ti += components[i].index();\n\t\t\t}\n\t\t\treturn index;\n\t\t}\n\n\t\tprivate Axis.Component[] componentsOf(Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View item) {\n\t\t\tAxis.Component[] components = new Axis.Component[axes.size()];\n\t\t\tfor (int i = 0; i < components.length; i++) components[i] = groupsBy.get(i).apply(item);\n\t\t\treturn components;\n\t\t}\n\n\t\tprivate long totalOf(List<Axis.Component> categories) {\n\t\t\treturn totals[indexOf(categories.toArray(new Axis.Component[0]))];\n\t\t}\n\n\t\tprivate void filter(Predicate<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View> filter) {\n\t\t\tif (filter == null) return;\n\t\t\tfilters.add(filter);\n\t\t}\n\n\t\tprivate void by(Axis axis, Function<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".View, Axis.Component> dimension) {\n\t\t\taxes.add(axis);\n\t\t\tif (dimension == null) return;\n\t\t\tgroupsBy.add(dimension);\n\t\t}\n\n\t\tpublic List<Axis> axes() {\n\t\t\treturn axes;\n\t\t}\n\n\t\tpublic long[] totals() {\n\t\t\treturn totals;\n\t\t}\n\n\t\tpublic static class Datasource {\n\t\t\t// TODO cache\n\t\t\tprivate final File root;\n\t\t\tprivate final Timetag from;\n\t\t\tprivate final Timetag to;\n\n\t\t\tpublic Datasource(File root, Timetag from, Timetag to) {\n\t\t\t\tthis.root = root;\n\t\t\t\tthis.from = from;\n\t\t\t\tthis.to = to;\n\t\t\t}\n\n\t\t\tprivate LedStream<Fact> leds(")).output(mark("split", "parameter")).output(literal(") {\n\t\t\t\treturn LedStream.merged(StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(t -> on(t")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(")));\n\t\t\t}\n\n\t\t\tprivate LedStream<Fact> on(Timetag timetag")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\t\tFile file = new File(root + \"/\" ")).output(expression().output(literal("+ ")).output(mark("split", "name")).output(literal(" + \".\""))).output(literal(" + \"")).output(mark("name", "FirstUpperCase")).output(literal("/\", timetag.value() + \".led\");\n\t\t\t\treturn file.exists() ? new LedReader(file).read(Fact::new) : LedStream.empty();\n\t\t\t}\n\t\t}\n\t}\n}")),
			rule().condition((trigger("nbits"))).output(literal("NBits")),
			rule().condition((type("customFilter")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("() {\n\tfilterList.add(v -> ")).output(mark("cube", "firstUpperCase")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("Filter(v));\n\treturn this;\n}")),
			rule().condition((type("customFilter")), (trigger("staticmethod"))).output(literal("public static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(View view) {\n\treturn true;\n}")),
			rule().condition((type("dimension")), (trigger("staticmethod"))).output(literal("public static Predicate<View> ")).output(mark("name", "firstLowerCase")).output(literal("Predicate(Set<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Component> ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\treturn v -> false; //TODO\n}\n\npublic static Function<View, Axis.Component> ")).output(mark("name", "firstLowerCase")).output(literal("Function() {\n\treturn v -> null; //TODO\n}")),
			rule().condition((allTypes("dimension","categorical")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "firstUpperCase")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis", "firstUpperCase")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(View view, Set<Abstract")).output(mark("axis", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "firstLowerCase")).output(literal("Function(view));\n}\n\npublic static Axis.Component ")).output(mark("name", "firstLowerCase")).output(literal("Function(View view) {\n\treturn view.")).output(mark("source")).output(literal("()")).output(expression().output(literal(".")).output(mark("child")).output(literal("()"))).output(literal(";\n}")),
			rule().condition((allTypes("dimension","distribution")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\tfilterList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "firstUpperCase")).output(literal(".instance());\n\tcomponents.put(")).output(mark("axis", "firstUpperCase")).output(literal(".instance(), new HashSet<>());\n\tgroupByList.add(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Function(v));\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "firstLowerCase")).output(literal("Filter(View view, Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "firstLowerCase")).output(literal("Function(view));\n}\n\nprivate static Axis.Component ")).output(mark("name", "firstLowerCase")).output(literal("Function(View view) {\n\treturn ")).output(mark("axis", "firstUpperCase")).output(literal(".instance().rangeOf(view.")).output(mark("source", "firstLowerCase")).output(literal("());\n}")),
			rule().condition((type("indicator")), (trigger("sum"))).output(mark("name", "firstLowerCase")).output(literal(" += view.")).output(mark("name", "firstLowerCase")).output(literal("();")),
			rule().condition((type("indicator")), (trigger("sumaggregation"))).output(mark("name", "firstLowerCase")).output(literal(" += aggregation.")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","average")), (trigger("field"))).output(literal("double ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","sum")), (trigger("field"))).output(literal("long ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","average")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(" / (double) ids.size();\n}")),
			rule().condition((allTypes("indicator","sum")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("indicator","average")), (trigger("staticmethod"))).output(literal("public static double ")).output(mark("name")).output(literal("(View view) {\n\treturn 0.;\n}")),
			rule().condition((allTypes("indicator","sum")), (trigger("staticmethod"))).output(literal("public static long ")).output(mark("name")).output(literal("(View view) {\n\treturn 0;\n}")),
			rule().condition((type("split")), (trigger("parameter"))).output(literal("String ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("setparameter"))).output(literal("java.util.Set<String> ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("assign"))).output(literal("this.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("name"))).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("nameupper"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("split")), (trigger("field"))).output(literal("private final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("method"))).output(literal("private static Set<String> all")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\treturn java.util.Set.of(")).output(mark("value", "quoted").multiple(", ")).output(literal(");\n}")),
			rule().condition((trigger("dimension"))).output(literal("public static Predicate<")).output(mark("cube", "FirstUpperCase")).output(literal(".View> ")).output(mark("axis", "firstLowerCase")).output(literal("(Set<")).output(mark("axis", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "firstLowerCase")).output(literal("List) {\n\treturn r -> ")).output(mark("axis", "firstLowerCase")).output(literal("List.contains(r.")).output(mark("axis", "firstLowerCase")).output(literal("());\n}\n\npublic static Function<")).output(mark("cube", "FirstUpperCase")).output(literal(".View, String> ")).output(mark("axis", "firstLowerCase")).output(literal("() {\n\treturn r -> r.")).output(mark("axis", "firstLowerCase")).output(literal("().id();\n}")),
			rule().condition((type("column")), (anyTypes("id","longInteger")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((allTypes("column","datetime")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn java.time.Instant.ofEpochMilli(bitBuffer.get")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal("));\n}")),
			rule().condition((allTypes("column","date")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn java.time.LocalDate.ofEpochDay(Short.toUnsignedInt(bitBuffer.get")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(")));\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("getter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("type", "firstUpperCase")).output(literal(".instance().component(bitBuffer.getIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal("));\n}")),
			rule().condition((allTypes("column","integer")), (trigger("getter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Integer")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((allTypes("column","bool")), (trigger("getter"))).output(literal("public Boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\tbyte value = bitBuffer.getByteNBits(")).output(mark("offset")).output(literal(", 2);\n\treturn value == NULL ? null : value == 2;\n}")),
			rule().condition((allTypes("column","real")), (attribute("size", "32")), (trigger("getter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","real")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("dimension","categorical")), (trigger("viewgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("dimension","distribution")), (trigger("viewgetter"))).output(literal("public ")).output(mark("distribution")).output(literal(".Range ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("categorical")), (trigger("viewgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","integer")), (trigger("viewgetter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","real")), (attribute("size", "32")), (trigger("viewgetter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((allTypes("column","real")), (trigger("viewgetter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((type("column")), (anyTypes("id","longInteger")), (trigger("viewgetter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((trigger("viewgetter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("cube")).output(literal(".")).output(mark("name", "firstLowerCase")).output(literal("();\n}"))
		);
	}
}