package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CategoricalAxisTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("axis"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".analytic.axes;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".analytic.Axis;\n\nimport java.util.*;\nimport java.util.stream.Stream;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\nimport java.util.concurrent.atomic.AtomicInteger;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" implements Axis {\n\n    public static final Component NA = new Component(0, \"NA\"")).output(expression().output(literal(", ")).output(mark("include", "defaultValue").multiple(", "))).output(literal(");\n    ")).output(expression().output(mark("component").multiple("\n"))).output(literal("\n\n    private static final class Singleton {\n    \tprivate static final ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" INSTANCE = new ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("();\n    }\n\n    public static ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" instance() {\n    \treturn Singleton.INSTANCE;\n    }\n\n\n\tprivate final Component[] components;\n\tprivate final Map<String, Component> componentsByName;\n\n\tprivate ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("() {\n\t\tthis.componentsByName = new WeakHashMap<>();\n\t    ")).output(expression().output(mark("components", "load"))).output(literal("\n\t}\n\n    @Override\n\tpublic String label() {\n\t\treturn \"")).output(mark("label")).output(literal("\";\n\t}\n\n    @Override\n\tpublic int size() {\n\t\treturn components.length - 1;\n\t}\n\n    @Override\n    public List<Component> components() {\n    \treturn new AbstractList<>() {\n    \t\t@Override\n    \t\tpublic Component get(int index) {\n    \t\t\treturn component(index + 1);\n    \t\t}\n    \t\t@Override\n    \t\tpublic int size() {\n    \t\t\treturn ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(".this.size();\n    \t\t}\n    \t};\n    }\n\n    @Override\n    public Stream<Component> stream() {\n    \treturn Arrays.stream(components).skip(1);\n    }\n\n    public List<Component> components(Predicate<Component> filter) {\n    \treturn stream().filter(filter).collect(Collectors.toList());\n    }\n\n    @Override\n    public Component component(int index) {\n    \treturn (index <= 0 || index > size()) ? NA : components[index];\n    }\n\n    @Override\n    public Component component(String name) {\n    \tif(!componentsByName.containsKey(name))\n    \t\tcomponentsByName.put(name, stream().filter(c -> c.label().equals(name)).findAny().orElse(NA));\n    \treturn componentsByName.getOrDefault(name, NA);\n    }\n\n\n\tpublic static class Component implements Axis.Component {\n\n\t\tpublic final int index;\n\t\tpublic final String id;\n\t\t")).output(mark("include", "declaration").multiple("\n")).output(literal("\n\n\t\tComponent(int index, String id")).output(expression().output(literal(", ")).output(mark("include", "parameter").multiple(", "))).output(literal(") {\n\t\t\tthis.index = index;\n\t\t\tthis.id = id;\n\t\t\t")).output(mark("include", "assign").multiple("\n")).output(literal("\n\t\t}\n\n        @Override\n\t\tpublic int index() {\n\t\t\treturn index;\n\t\t}\n\n        @Override\n\t\tpublic String id() {\n\t\t\treturn id;\n\t\t}\n\n\t\t")).output(expression().output(mark("include", "getter").multiple("\n\n"))).output(literal("\n\n        @Override\n\t\tpublic Axis axis() {\n\t\t\treturn ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(".instance();\n\t\t}\n\n\t    @Override\n\t    public boolean equals(Object o) {\n\t    \tif (this == o) return true;\n\t    \tif (o == null || getClass() != o.getClass()) return false;\n\t    \tComponent component = (Component) o;\n\t    \treturn index == component.index;\n\t    }\n\n\t    @Override\n\t    public int hashCode() {\n\t    \treturn Objects.hash(index, id);\n\t    }\n\n\t    @Override\n\t    public String toString() {\n\t        StringBuilder sb = new StringBuilder(axis().getClass().getSimpleName());\n\t        sb.append(\".Component {\");\n\t        sb.append(\"index=\").append(index);\n\t        sb.append(\", id=\").append(id);\n\t        ")).output(expression().output(mark("include", "toString").multiple("\n"))).output(literal("\n\t        return sb.append('}').toString();\n\t    }\n\t}\n}")),
			rule().condition((type("component"))).output(literal("public static final Component ")).output(mark("name")).output(literal(" = new Component(")).output(mark("index")).output(literal(", \"")).output(mark("id")).output(literal("\"")).output(expression().output(literal(", \"")).output(mark("label")).output(literal("\""))).output(expression().output(literal(", ")).output(mark("include").multiple(", "))).output(literal(");")),
			rule().condition((attribute("name", "label")), (trigger("declaration"))).output(literal("public final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((trigger("declaration"))).output(literal("public final ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((attribute("name", "label")), (trigger("getter"))).output(literal("public String ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n    return ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((trigger("getter"))).output(literal("public ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n    return ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((trigger("tostring"))).output(literal("sb.append(\", ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = \").append(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(");")),
			rule().condition((attribute("name", "label")), (trigger("defaultvalue"))).output(literal("\"\"")),
			rule().condition((trigger("defaultvalue"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".NA")),
			rule().condition((attribute("name", "label")), (trigger("parameter"))).output(literal("String ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((trigger("parameter"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((attribute("name", "label")), (trigger("constructor"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? l[")).output(mark("index")).output(literal("] : \"NA\"")),
			rule().condition((trigger("constructor"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".instance().component(l[")).output(mark("index")).output(literal("]) : ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".NA")),
			rule().condition((trigger("assign"))).output(literal("this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((trigger("put"))).output(literal("components.put(")).output(mark("index")).output(literal(", ")).output(mark("name", "FirstUpperCase")).output(literal(");\ncomponentsByName.put(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "FirstUpperCase")).output(literal(");")),
			rule().condition((trigger("field"))).output(literal("public static final Component ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" = new Component(")).output(mark("index")).output(literal(", \"")).output(mark("name")).output(literal("\"")).output(expression().output(literal(", \"")).output(mark("label")).output(literal("\""))).output(literal(");")),
			rule().condition((type("components")), (attribute("embedded", "true")), (trigger("load"))).output(literal("this.components = new Component[] {NA")).output(expression().output(literal(", ")).output(mark("component").multiple(", "))).output(literal("};")),
			rule().condition((type("components")), (attribute("embedded", "false")), (trigger("load"))).output(literal("Component[] components = new Component[0];\ntry (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(\n    getClass().getResourceAsStream(\"")).output(mark("resource")).output(literal("\")))) {\n\t\tcomponents = Stream.concat(Stream.of(NA),\n\t\treader.lines().map(l -> l.split(\"\t\")).map(l -> new Component(Integer.parseInt(l[0]),l[1]")).output(expression().output(literal(",")).output(mark("include", "constructor").multiple(", "))).output(literal("))\n\t\t).toArray(Component[]::new);\n\t} catch (java.io.IOException e) {\n\t\tio.intino.alexandria.logger.Logger.error(\"Failed to load components of ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("\", e);\n\t}\n\tthis.components = components;"))
		);
	}
}