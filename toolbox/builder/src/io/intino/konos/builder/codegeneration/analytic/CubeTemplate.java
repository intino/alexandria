package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CubeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("cube","src"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\n\nimport io.provista.cosmos.box.analytic.Axis;\nimport io.provista.cosmos.box.analytic.axes.*;\n\nimport java.io.File;\nimport java.util.*;\nimport java.util.stream.Collectors;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(List<Loader> loaders) {\n\t\tsuper(loaders);\n\t}\n\n\t")).output(expression().output(mark("customDimension", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customIndicator", "staticMethod").multiple("\n\n"))).output(literal("\n\n\t@Override\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal(" execute() {\n\t    super.execute();\n\t    return this;\n\t}\n\n\tpublic static List<Axis> dimensions() {\n        return List.of(")).output(expression().output(mark("dimension", "getInstance").multiple(", "))).output(literal(");\n    }\n\n\tpublic static class Loader extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Loader {\n\n\t    public static List<Loader> create(File root, Timetag from, Timetag to, Collection<String> divisions) {\n            return divisions.stream().map(d -> new Loader(new Loader.Datasource(root, from, to), d)).collect(Collectors.toList());\n        }\n\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tsuper(datasource")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(");\n\t\t}\n\n\t\t// TODO write here a cache if necessary\n\t}\n\n\tpublic static class Fact extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".AbstractFact {\n\n\t    public Fact(ByteStore store) {\n\t        super(store);\n\t    }\n\n\t    ")).output(expression().output(mark("virtualColumn", "implementation").multiple("\n\n"))).output(literal("\n\t}\n}")),
			rule().condition((type("cube"))).output(literal("package ")).output(mark("package")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.led.LedReader;\nimport io.intino.alexandria.led.LedStream;\nimport io.intino.alexandria.led.Schema;\nimport io.intino.alexandria.led.allocators.SchemaFactory;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\nimport io.intino.alexandria.led.util.iterators.MergedIterator;\nimport io.intino.alexandria.led.util.collections.SparseLongList;\n\nimport ")).output(mark("package")).output(literal(".analytic.axes.*;\nimport ")).output(mark("package")).output(literal(".analytic.Axis;\n\nimport java.io.File;\nimport java.util.*;\nimport java.util.stream.Collectors;\nimport java.util.function.Function;\nimport java.util.function.Predicate;\nimport java.util.stream.Stream;\nimport java.util.stream.StreamSupport;\n\nimport static java.util.Comparator.comparingLong;\nimport static java.util.Spliterators.spliteratorUnknownSize;\n\nimport ")).output(mark("package")).output(literal(".analytic.cubes.")).output(mark("name", "FirstUpperCase")).output(literal(".Fact;\n\n@SuppressWarnings(\"unused\")\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" implements Iterable<Abstract")).output(mark("name", "FirstUpperCase")).output(literal(".Aggregation> {\n\n\tpublic static final Fact NULL_FACT = new NullFact();\n\n\tprivate final List<? extends Loader> loaders;\n\tprivate Predicate<Fact> filter = fact -> true;\n\tprivate final List<Axis> axes = new ArrayList<>();\n\tprivate final Map<Axis, Set<Axis.Component>> components = new HashMap<>();\n\tprivate final List<Function<Fact, ? extends Axis.Component>> groupByList = new ArrayList<>();\n\tprivate Aggregation[] result;\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("(List<? extends Loader> loaders) {\n\t\tthis.loaders = java.util.Objects.requireNonNull(loaders);\n\t}\n\n\t")).output(expression().output(mark("dimension", "method").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("customFilter", "method").multiple("\n\n"))).output(literal("\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" groupBy(Axis axis) {\n\t    if(axis == null) throw new NullPointerException(\"Axis cannot be null\");\n\t    switch(axis.getLabel()) {\n\t        ")).output(expression().output(mark("dimension", "switchCaseGroupBy").multiple("\n"))).output(literal("\n\t    }\n        return this;\n\t}\n\n    @SuppressWarnings(\"unchecked\")\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" filter(Axis axis, Set<? extends Axis.Component> components) {\n\t    if(axis == null) throw new NullPointerException(\"Axis cannot be null\");\n\t    switch(axis.getLabel()) {\n\t        ")).output(expression().output(mark("dimension", "switchCaseFilter").multiple("\n"))).output(literal("\n\t    }\n        return this;\n    }\n\n\tpublic Abstract")).output(mark("name", "firstUpperCase")).output(literal(" execute() {\n\t\tresult = new Aggregation[resultsSize()];\n\t\tresults().forEach(this::append);\n\t\tfillCategories();\n\t\treturn this;\n\t}\n\n\tpublic Aggregation[] result() {\n        if(result == null) return new Aggregation[0];\n        return Arrays.stream(result).filter(java.util.Objects::nonNull).toArray(Aggregation[]::new);\n    }\n\n\t@Override\n\tpublic Iterator<Aggregation> iterator() {\n\t    return result == null\n            ? Stream.<Aggregation>empty().iterator()\n            : Arrays.stream(result).filter(java.util.Objects::nonNull).iterator();\n\t}\n\n\tpublic static List<Axis> dimensions() {\n\t    return List.of(")).output(expression().output(mark("dimension", "getInstance").multiple(", "))).output(literal(");\n\t}\n\n\tpublic boolean contains(Axis axis, Axis.Component component) {\n\t    if(!components.containsKey(axis)) return false;\n\t    return components.get(axis).contains(component);\n\t}\n\n\tpublic Iterator<Fact> detail() {\n\t\tMergedIterator<Fact> iterator = new MergedIterator<>(loaders.stream().map(Iterable::iterator), comparingLong(Fact::id));\n\t\treturn StreamSupport.stream(spliteratorUnknownSize(iterator, Spliterator.SORTED), false).filter(this::check).iterator();\n\t}\n\n\tpublic Aggregation aggregation(List<Axis.Component> components) {\n\t    final int index = indexOf(components);\n\t\treturn result[index];\n\t}\n\n    public Aggregation aggregation(Axis.Component... components) {\n        final int index = indexOf(components);\n    \treturn result[index];\n    }\n\n\tprivate void fillCategories() {\n\t\tfor (Aggregation aggregation : result) {\n\t\t\tif (aggregation == null) continue;\n\t\t\tfor (int i = 0; i < aggregation.components.length; i++)\n\t\t\t\tcomponents.get(axes.get(i)).add(aggregation.components[i]);\n\t\t}\n\t}\n\n\tprivate int resultsSize() {\n\t\tint accumulator = 1;\n\t\tfor (Axis axis : axes) accumulator *= axis.getSize() + 1;\n\t\treturn accumulator;\n\t}\n\n\tprivate Stream<Aggregation[]> results() {\n\t\treturn loaders.parallelStream().map(this::results);\n\t}\n\n\tprivate Aggregation[] results(Iterable<Fact> facts) {\n\t\tAggregation[] result = new Aggregation[resultsSize()];\n\t\tfor (Fact fact : facts) {\n\t\t    fact.setCube(this);\n\t\t\tif (!check(fact)) continue;\n\t\t\tAxis.Component[] components = componentsOf(fact);\n\t\t\tint index = indexOf(components);\n\t\t\tif (result[index] == null) result[index] = new Aggregation(components);\n\t\t\tresult[index].append(fact);\n\t\t}\n\t\treturn result;\n\t}\n\n\tprivate synchronized void append(Aggregation[] results) {\n\t\tfor (int i = 0; i < results.length; i++) {\n\t\t    final Aggregation aggregation = results[i];\n\t\t\tif (aggregation == null) continue;\n\t\t\tif (result[i] == null)\n                result[i] = aggregation;\n            else\n                result[i].append(aggregation);\n\t\t}\n\t}\n\n\tprivate int indexOf(Axis.Component[] components) {\n\t\tint index = 0;\n\t\tfor (int i = 0; i < components.length; i++) {\n\t\t\tindex *= axes.get(i).getSize();\n\t\t\tindex += components[i].index();\n\t\t}\n\t\treturn index;\n\t}\n\n\tprivate int indexOf(Collection<Axis.Component> components) {\n    \tint index = 0;\n    \tint i = 0;\n    \tfor (Axis.Component component : components) {\n    \t\tindex *= axes.get(i++).getSize();\n    \t\tindex += component.index();\n    \t}\n    \treturn index;\n    }\n\n\tprivate boolean check(Fact item) {\n\t    return filter.test(item);\n\t}\n\n\tprivate Axis.Component[] componentsOf(Fact item) {\n\t\tAxis.Component[] components = new Axis.Component[groupByList.size()];\n\t\tfor (int i = 0; i < components.length; i++) components[i] = groupByList.get(i).apply(item);\n\t\treturn components;\n\t}\n\n\tpublic static abstract class AbstractFact extends Schema {\n\n        public static final int SIZE = ")).output(mark("size")).output(literal("; // Bytes\n        public static final UUID SERIAL_UUID = UUID.fromString(\"")).output(mark("serialUUID")).output(literal("\");\n        public static final SchemaFactory<Fact> FACTORY = new SchemaFactory<>(Fact.class) {\n            @Override\n            public Fact newInstance(ByteStore store) {\n                return new Fact(store);\n            }\n        };\n\n        private Timetag timetag;\n        private Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" cube;\n\n        public AbstractFact(ByteStore store) {\n        \tsuper(store);\n        }\n\n        ")).output(expression().output(mark("virtualColumn", "abstract").multiple("\n\n"))).output(literal("\n\n        ")).output(expression().output(mark("column", "getter").multiple("\n\n"))).output(literal("\n\n        @Override\n        public long id() {\n        \treturn ")).output(mark("id")).output(literal("();\n        }\n\n        public final Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" cube() {\n            return cube;\n        }\n\n        void setCube(Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" cube) {\n            this.cube = cube;\n        }\n\n        public final Timetag timetag() {\n            return timetag;\n        }\n\n        void setTimetag(Timetag timetag) {\n            this.timetag = timetag;\n        }\n\n        @Override\n        public int size() {\n        \treturn SIZE;\n        }\n\n        @Override\n        public UUID serialUUID() {\n            return SERIAL_UUID;\n        }\n\n         @Override\n         public String toString() {\n             return \"")).output(mark("name", "FirstUpperCase")).output(literal(".Fact{\"\n                     + \"id=\" + id()\n                     + \", timetag=\" + timetag()\n                     ")).output(expression().output(mark("column", "toString").multiple("\n"))).output(literal("\n                     + '}';\n         }\n    }\n\n\tpublic static class NullFact extends Fact {\n\n\t\tpublic static final int SIZE = Fact.SIZE; // Bytes\n\t\tpublic static final UUID SERIAL_UUID = Fact.SERIAL_UUID;\n        public static final SchemaFactory<NullFact> FACTORY = new SchemaFactory<>(NullFact.class) {\n            @Override\n        \tpublic NullFact newInstance(ByteStore store) {\n        \t    return new NullFact();\n        \t}\n        };\n\n\t\tprivate NullFact() {\n\t\t\tsuper(ByteStore.empty());\n\t\t}\n\n\t\t@Override\n\t\tpublic long id() {\n\t\t\treturn ")).output(mark("id")).output(literal("();\n\t\t}\n\n\t\t@Override\n\t\tpublic int size() {\n\t\t\treturn SIZE;\n\t\t}\n\n\t\t@Override\n\t\tpublic UUID serialUUID() {\n\t\t    return SERIAL_UUID;\n\t\t}\n\t}\n\n\tpublic enum Indicator {\n\n\t    Total(\"Total\", \"\", Mode.Sum),\n\t    Distincts(\"Distincts\", \"\", Mode.Sum),\n\t    ")).output(expression().output(mark("indicator", "enum").multiple(",\n"))).output(literal(";\n\n\n\t    public static Indicator byName(String name) {\n\t        return Arrays.stream(values()).filter(i -> i.name().equalsIgnoreCase(name)).findFirst().orElse(null);\n\t    }\n\n\t    public final String title;\n\t    public final String unit;\n\t    public final Mode mode;\n\n\t    Indicator(String title, String unit, Mode mode) {\n\t        this.title = title;\n\t        this.unit = unit;\n\t        this.mode = mode;\n\t    }\n\n\t    public enum Mode {\n\t        Sum, Average\n\t    }\n\t}\n\n\tpublic static class Aggregation {\n\n\t\tprivate final Axis.Component[] components;\n\t\tprivate long aggregationTotal = 0L;\n\t\tprivate long aggregationDistincts = 0L;\n\t\tprivate long lastID = Long.MIN_VALUE;\n\t\t")).output(expression().output(mark("index", "field"))).output(literal("\n\t\t")).output(mark("indicator", "field").multiple("\n")).output(literal("\n\n\t\tpublic Aggregation(Axis.Component[] components) {\n\t\t\tthis.components = components;\n\t\t}\n\n\t\tpublic void append(Fact fact) {\n\t\t\t")).output(expression().output(mark("indicator", "sum").multiple("\n"))).output(literal("\n\t\t\tif (lastID != fact.id()) {\n\t\t\t    ")).output(expression().output(mark("index", "append"))).output(literal("\n                ++aggregationDistincts;\n                lastID = fact.id();\n            }\n            ++aggregationTotal;\n\t\t}\n\n\t\tpublic void append(Aggregation aggregation) {\n\t\t\t")).output(expression().output(mark("indicator", "sumAggregation").multiple("\n"))).output(literal("\n\t\t\t")).output(expression().output(mark("index", "append2"))).output(literal("\n\t\t\taggregationDistincts = Math.max(aggregationDistincts(), aggregation.aggregationDistincts());\n\t\t\taggregationTotal += aggregation.aggregationTotal();\n\t\t}\n\n\t\tpublic long aggregationTotal() {\n\t\t\treturn aggregationTotal;\n\t\t}\n\n\t\tpublic long aggregationDistincts() {\n\t\t    return aggregationDistincts;\n\t\t}\n\n\t\t")).output(expression().output(mark("index", "getter"))).output(literal("\n\n\t\tpublic Axis.Component[] components() {\n\t\t\treturn components;\n\t\t}\n\n\t\t")).output(expression().output(mark("indicator", "getter").multiple("\n\n"))).output(literal("\n\n\t\tpublic Number indicator(Indicator indicator) {\n            switch(indicator) {\n                ")).output(expression().output(mark("indicator", "switchCase").multiple("\n"))).output(literal("\n                case Total: return aggregationTotal;\n                case Distincts: return aggregationDistincts;\n            }\n            return 0L;\n        }\n\n\t\t@Override\n\t\tpublic String toString() {\n\t\t    return \"")).output(mark("name", "FirstUpperCase")).output(literal(".Aggregation{\"\n\t\t        + \"aggregationTotal=\" + aggregationTotal\n\t\t        + \"aggregationDistincts=\" + aggregationDistincts\n\t\t        ")).output(expression().output(mark("index", "toString").multiple("\n"))).output(literal("\n\t\t        ")).output(expression().output(mark("indicator", "toString").multiple("\n"))).output(literal("\n\t\t        + \"}\";\n\t\t}\n\t}\n\n\tpublic static class Loader implements Iterable<Fact> {\n\n\t\tprotected final Datasource datasource;\n\t\t")).output(expression().output(mark("split", "field"))).output(literal("\n\n\t\tpublic Loader(Datasource datasource")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\tthis.datasource = datasource;\n\t\t\t")).output(expression().output(mark("split", "assign"))).output(literal("\n\t\t}\n\n\t\t@Override\n\t\tpublic Iterator<Fact> iterator() {\n\t\t    return datasource.leds(")).output(mark("split", "name")).output(literal(").asJavaStream().iterator();\n\t\t}\n\n\t\t")).output(expression().output(mark("split", "method"))).output(literal("\n\n\t\tpublic static class Datasource {\n\n\t\t\tprivate final File root;\n\t\t\tprivate final Timetag from;\n\t\t\tprivate final Timetag to;\n\n\t\t\tpublic Datasource(File root, Timetag from, Timetag to) {\n\t\t\t\tthis.root = root;\n\t\t\t\tthis.from = from;\n\t\t\t\tthis.to = to;\n\t\t\t}\n\n\t\t\tpublic Timetag from() {\n\t\t\t\treturn from;\n\t\t\t}\n\n\t\t\tpublic Timetag to() {\n\t\t\t\treturn to;\n\t\t\t}\n\n\t\t\tprivate LedStream<Fact> leds(")).output(mark("split", "parameter")).output(literal(") {\n\t\t\t\treturn LedStream.merged(StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(t -> on(t")).output(expression().output(literal(", ")).output(mark("split", "name"))).output(literal(")));\n\t\t\t}\n\n\t\t\tprivate LedStream<Fact> on(Timetag timetag")).output(expression().output(literal(", ")).output(mark("split", "parameter"))).output(literal(") {\n\t\t\t\tFile file = new File(root + \"/")).output(mark("name", "FirstUpperCase")).output(literal("\" ")).output(expression().output(literal("+ \".\" + ")).output(mark("split", "name"))).output(literal(", timetag.value() + \".led\");\n\t\t\t\tLedStream<Fact> facts = file.exists() ? new LedReader(file).read(Fact.class) : LedStream.empty(Fact.class);\n\t\t\t\treturn facts.peek(fact -> fact.setTimetag(timetag));\n\t\t\t}\n\t\t}\n\t}\n}")),
			rule().condition((type("indicator")), (trigger("enum"))).output(mark("fieldName", "CamelCase", "FirstUpperCase")).output(literal("(\"")).output(mark("label")).output(literal("\", \"")).output(mark("unit")).output(literal("\", Mode.")).output(mark("mode")).output(literal(")")),
			rule().condition((type("indicator")), (trigger("switchcase"))).output(literal("case ")).output(mark("fieldName", "CamelCase", "FirstUpperCase")).output(literal(": return ")).output(mark("name", "snakeCaseToCamelCase", "FirstLowerCase")).output(literal(";")),
			rule().condition((type("index")), (trigger("field"))).output(literal("private final SparseLongList ids = new SparseLongList();")),
			rule().condition((type("index")), (trigger("append"))).output(literal("ids.add(fact.id());")),
			rule().condition((type("index")), (trigger("append2"))).output(literal("ids.addAll(aggregation.ids);")),
			rule().condition((type("index")), (trigger("getter"))).output(literal("public List<Long> ids() {\n\treturn ids.asList();\n}")),
			rule().condition((trigger("nbits"))).output(literal("NBits")),
			rule().condition((type("customFilter")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("() {\n    filter = filter.and(")).output(mark("cube", "FirstUpperCase")).output(literal("::")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Filter);\n\treturn this;\n}")),
			rule().condition((type("customFilter")), (trigger("staticmethod"))).output(literal("public static boolean ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Filter(Fact fact) {\n\t// TODO: write here your code\n\treturn true;\n}")),
			rule().condition((type("customDimension")), (trigger("staticmethod"))).output(literal("public static ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(expression().output(mark("isDistribution")).output(literal("Range"))).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function() {\n    // TODO: write here your code\n\treturn v -> null;\n}")),
			rule().condition((allTypes("dimension","categorical")), (trigger("switchcasegroupby"))).output(literal("case ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".AXIS_LABEL: return groupBy")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("();")),
			rule().condition((allTypes("dimension","categorical")), (trigger("switchcasefilter"))).output(literal("case ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".AXIS_LABEL: return filter")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("((Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component>) components);")),
			rule().condition((type("dimension")), (trigger("getinstance"))).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get()")),
			rule().condition((allTypes("dimension","categorical")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tfilter = filter.and(v -> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get());\n\tcomponents.put(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get(), new HashSet<>());\n\tgroupByList.add(Abstract")).output(mark("cube", "FirstUpperCase")).output(literal("::")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function);\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function(fact));\n}\n\npublic static ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function(Fact fact) {\n\treturn fact.")).output(mark("source", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("()")).output(expression().output(literal(".")).output(mark("child", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("()"))).output(literal(";\n}")),
			rule().condition((allTypes("dimension","continuous")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "firstUpperCase")).output(literal(" filter")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tfilter = filter.and(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" groupBy")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get());\n\tcomponents.put(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get(), new HashSet<>());\n\tgroupByList.add(Abstract")).output(mark("cube", "FirstUpperCase")).output(literal("::")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function);\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\treturn ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".contains(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function(fact));\n}\n\npublic static ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function(Fact fact) {\n\treturn ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".rangeOf(fact.")).output(mark("source", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("());\n}")),
			rule().condition((allTypes("customDimension","continuous")), (trigger("method"))).output(literal("public Abstract")).output(mark("cube", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(" filter")).output(mark("name", "firstUpperCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase")).output(literal(".Range> ranges) {//TODO\n\tfilter = filter.and(v -> ")).output(mark("name", "firstLowerCase")).output(literal("Filter(v, ranges));\n\treturn this;\n}\n\npublic Abstract")).output(mark("cube", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(" groupBy")).output(mark("name", "firstUpperCase")).output(literal("() {\n\taxes.add(")).output(mark("axis", "snakeCaseToCamelCase")).output(literal(".get);\n\tcomponents.put(")).output(mark("axis", "snakeCaseToCamelCase")).output(literal(".get, new HashSet<>());\n\tgroupByList.add(Abstract")).output(mark("cube", "firstUpperCase")).output(literal("::")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function);\n\treturn this;\n}\n\npublic static boolean ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Filter(Fact fact, Set<")).output(mark("axis", "snakeCaseToCamelCase")).output(literal(".Range> rangos) {\n\treturn rangos.contains(")).output(mark("cube", "firstUpperCase")).output(literal(".")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Function(fact));\n}")),
			rule().condition((type("indicator")), (trigger("tostring"))).output(literal(" + \", ")).output(mark("name", "firstLowerCase")).output(literal("=\" + ")).output(mark("name", "firstLowerCase")).output(literal("()")),
			rule().condition((type("indicator")), (trigger("sum"))).output(mark("name", "firstLowerCase")).output(literal(" += fact.")).output(mark("source", "firstLowerCase")).output(literal("();")),
			rule().condition((type("indicator")), (trigger("sumaggregation"))).output(mark("name", "firstLowerCase")).output(literal(" += aggregation.")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","average")), (trigger("field"))).output(literal("double ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","sum")), (trigger("field"))).output(literal("long ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((allTypes("indicator","average")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(" / (double) aggregationDistincts;\n}")),
			rule().condition((type("index")), (trigger("index"))).output(literal("ids.size()")),
			rule().condition((allTypes("indicator","sum")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("indicator","average")), (trigger("staticmethod"))).output(literal("public static double ")).output(mark("name")).output(literal("(Fact fact) {\n\treturn 0.;\n}")),
			rule().condition((allTypes("indicator","sum")), (trigger("staticmethod"))).output(literal("public static long ")).output(mark("name")).output(literal("(Fact fact) {\n\treturn 0;\n}")),
			rule().condition((type("split")), (trigger("parameter"))).output(literal("String ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("setparameter"))).output(literal("Set<String> ")).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("assign"))).output(literal("this.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("name"))).output(mark("name", "firstLowerCase")),
			rule().condition((type("split")), (trigger("nameupper"))).output(mark("name", "firstUpperCase")),
			rule().condition((type("split")), (trigger("field"))).output(literal("protected final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("split")), (trigger("method"))).output(literal("private static Set<String> all")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\treturn java.util.Set.of(")).output(mark("value", "quoted").multiple(", ")).output(literal(");\n}")),
			rule().condition((type("dimension")), (trigger("ifgroupby"))).output(literal("if(axis.equals(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get())) return groupBy")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("();")),
			rule().condition((type("dimension")), (trigger("iffilterby"))).output(literal("if(axis.equals(")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".get())) return filter")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("();")),
			rule().condition((trigger("dimension"))).output(literal("public static Predicate<")).output(mark("cube", "FirstUpperCase")).output(literal(".Fact> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(Set<")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List) {\n\treturn r -> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List.contains(r.")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("());\n}\n\npublic static Function<")).output(mark("cube", "FirstUpperCase")).output(literal(".Fact, String> ")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn r -> r.")).output(mark("axis", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("().id();\n}")),
			rule().condition((type("column")), (trigger("tostring"))).output(literal("+ \", ")).output(mark("name")).output(literal("=\" + ")).output(mark("name")).output(literal("()"))
		);
	}
}