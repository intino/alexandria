package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CategoricalAxisTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("interface"))).output(literal("package ")).output(mark("package")).output(literal(".analytic;\n\nimport java.util.Collection;\n\n\tpublic interface Axis {\n\t\tString label();\n\n\t\tint size();\n\n\t\tCollection<? extends Component> components();\n\n\t\tComponent component(int index);\n\n\t\tComponent component(String id);\n\n\t\tdefault boolean isDynamic() {\n\t\t\treturn false;\n\t\t};\n\n\n\tinterface Component {\n\t\tint index();\n\n\t\tString id();\n\n\t\tdefault String label() {\n\t\t\treturn \"\";\n\t\t}\n\n\t\tAxis axis();\n\t}\n}")),
			rule().condition((type("axis"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".analytic.axes;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".analytic.Axis;\n\nimport java.util.ArrayList;\nimport java.util.List;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\nimport java.util.concurrent.atomic.AtomicInteger;\n\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" implements Axis {\n\tprivate static ")).output(mark("name", "FirstUpperCase")).output(literal(" instance;\n\tpublic final Component NA = new Component(0, \"NA\"")).output(expression().output(literal(",")).output(mark("include", "defaultValue").multiple(", "))).output(literal(");\n\tprivate final java.util.Map<Integer, Component> components;\n\tprivate final java.util.Map<String, Component> componentsByName;\n\tprivate final int size;\n\n\tpublic static ")).output(mark("name", "FirstUpperCase")).output(literal(" instance() {\n\t\treturn instance == null ? (instance = new ")).output(mark("name", "FirstUpperCase")).output(literal("()): instance;\n\t}\n\n\tprivate ")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\t\tcomponents = new java.util.HashMap<>();\n\t\tcomponentsByName = new java.util.HashMap<>();\n\t\tAtomicInteger size = new AtomicInteger();\n\t\ttry (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(")).output(mark("name", "FirstUpperCase")).output(literal(".class.getResourceAsStream(\"/analytic/axes/")).output(mark("name")).output(literal(".tsv\")))) {\n\t\t\treader.lines().map(l -> l.split(\"\\t\", -1)).\n\t\t\tmap(l -> new Component(Integer.parseInt(l[0]), l[1]")).output(expression().output(literal(",")).output(mark("include", "constructor").multiple(", "))).output(literal(")).\n\t\t\tforEach(c -> { components.put(c.index, c); componentsByName.put(c.id, c);  size.getAndIncrement(); });\n\t\t} catch (java.io.IOException e) {\n\t\t\tio.intino.alexandria.logger.Logger.error(e);\n\t\t}\n\t\tthis.size = size.get();\n\t}\n\n\tpublic String label() {\n\t\treturn \"")).output(mark("label")).output(literal("\";\n\t}\n\n\tpublic int size() {\n\t\treturn this.size;\n\t}\n\n\tpublic List<Component> components() {\n\t\treturn new ArrayList<>(components.values());\n\t}\n\n\tpublic List<Component> components(Predicate<Component> filter) {\n\t\treturn components.values().stream().filter(filter).collect(Collectors.toList());\n\t}\n\n\tpublic Component component(int index) {\n\t\treturn components.getOrDefault(index, NA);\n\t}\n\n\tpublic Component component(String name) {\n\t\treturn componentsByName.getOrDefault(name, NA);\n\t}\n\n\tpublic Component component(Predicate<Component> filter) {\n\t\treturn components.values().stream().filter(filter).findFirst().orElse(NA);\n\t}\n\n\tpublic class Component implements Axis.Component {\n\t\tpublic final int index;\n\t\tpublic final String id;\n\t\t")).output(mark("include", "declaration").multiple("\n")).output(literal("\n\n\t\tComponent(int index, String id")).output(expression().output(literal(", ")).output(mark("include", "parameter").multiple(", "))).output(literal(") {\n\t\t\tthis.index = index;\n\t\t\tthis.id = id;\n\t\t\t")).output(mark("include", "assign").multiple("\n")).output(literal("\n\t\t}\n\n\t\tpublic int index() {\n\t\t\treturn index;\n\t\t}\n\n\t\tpublic String id() {\n\t\t\treturn id;\n\t\t}\n\n\t\tpublic Axis axis() {\n\t\t\treturn ")).output(mark("name", "FirstUpperCase")).output(literal(".this;\n\t\t}\n\n\t\tpublic boolean equals(Component component) {\n\t\t\treturn this.index == component.index;\n\t\t}\n\t}\n}")),
			rule().condition((attribute("name", "label")), (trigger("declaration"))).output(literal("public final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((trigger("declaration"))).output(literal("public final ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((attribute("name", "label")), (trigger("defaultvalue"))).output(literal("\"\"")),
			rule().condition((trigger("defaultvalue"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance().NA")),
			rule().condition((attribute("name", "label")), (trigger("parameter"))).output(literal("String ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((trigger("parameter"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((attribute("name", "label")), (trigger("constructor"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? l[")).output(mark("index")).output(literal("] : \"NA\"")),
			rule().condition((trigger("constructor"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance().component(l[")).output(mark("index")).output(literal("]) : ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance().NA")),
			rule().condition((trigger("assign"))).output(literal("this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((trigger("put"))).output(literal("components.put(")).output(mark("index")).output(literal(", ")).output(mark("name", "FirstUpperCase")).output(literal(");\ncomponentsByName.put(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "FirstUpperCase")).output(literal(");")),
			rule().condition((trigger("field"))).output(literal("public static final Component ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" = new Component(")).output(mark("index")).output(literal(", \"")).output(mark("name")).output(literal("\"")).output(expression().output(literal(", \"")).output(mark("label")).output(literal("\""))).output(literal(");"))
		);
	}
}