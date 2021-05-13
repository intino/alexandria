package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CategoricalAxisTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("axis"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".analytic.axes;\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".analytic.Axis;\n\nimport java.util.*;\nimport java.util.stream.Stream;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" implements Axis {\n\n    public static final String TITLE = \"")).output(mark("label")).output(literal("\";\n\n    public static final Component NA = new Component(0, \"NA\"")).output(expression().output(literal(", ")).output(mark("include", "defaultValue").multiple(", "))).output(literal(");\n    ")).output(expression().output(mark("component").multiple("\n"))).output(literal("\n\n    private static final class Singleton {\n    \tprivate static final ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" INSTANCE = new ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("();\n    }\n\n    // === STATIC METHODS === //\n\n    public static ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" get() {\n    \treturn Singleton.INSTANCE;\n    }\n\n\tpublic static String title() {\n\t\treturn TITLE;\n\t}\n\n\tpublic static int size() {\n\t\treturn get().getSize();\n\t}\n\n    public static List<Component> components() {\n    \treturn get().getComponents();\n    }\n\n    public static Stream<Component> stream() {\n    \treturn get().toStream();\n    }\n\n    public static List<Component> components(Predicate<Component> filter) {\n    \treturn get().getComponents(filter);\n    }\n\n    public static Component component(int index) {\n    \treturn get().getComponent(index);\n    }\n\n    public static Component component(String name) {\n    \treturn get().getComponent(name);\n    }\n\n    ")).output(expression().output(mark("include", "staticGetter").multiple("\n\n"))).output(literal("\n\n    // === === //\n\n\tprivate final Component[] components;\n\tprivate final Map<String, Component> componentsByName;\n\n\tprivate ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("() {\n\t\tthis.componentsByName = new HashMap<>();\n\t    ")).output(expression().output(mark("components", "load"))).output(literal("\n\t}\n\n    @Override\n\tpublic String getTitle() {\n\t\treturn TITLE;\n\t}\n\n    @Override\n\tpublic int getSize() {\n\t\treturn components.length;\n\t}\n\n    /**\n    * Get all components of this axis as a list, without the NA component (Starts with the component of index 1).\n    */\n    @Override\n    public List<Component> getComponents() {\n    \treturn new AbstractList<>() {\n    \t\t@Override\n    \t\tpublic Component get(int index) {\n    \t\t\treturn component(index + 1);\n    \t\t}\n    \t\t@Override\n    \t\tpublic int size() {\n    \t\t\treturn ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(".size() - 1;\n    \t\t}\n    \t};\n    }\n\n    /**\n    * Get all components of this axis as a stream, without the NA component (Starts with the component of index 1).\n    */\n    @Override\n    public Stream<Component> toStream() {\n    \treturn Arrays.stream(components).skip(1);\n    }\n\n    public List<Component> getComponents(Predicate<Component> filter) {\n    \treturn stream().filter(filter).collect(Collectors.toList());\n    }\n\n    @Override\n    public Component getComponent(int index) {\n        if(index < 0 || index >= components.length) return NA;\n    \treturn components[index];\n    }\n\n    @Override\n    public Component getComponent(String name) {\n    \tif(!componentsByName.containsKey(name))\n    \t\tcomponentsByName.put(name, stream().filter(c -> c.id().equals(name)).findAny().orElse(NA));\n    \treturn componentsByName.getOrDefault(name, NA);\n    }\n\n    @Override\n    public String toString() {\n        return \"")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("\";\n    }\n\n\n\tpublic static class Component implements Axis.Component {\n\n\t\tprivate final int index;\n\t\tprivate final String id;\n\t\t")).output(mark("include", "declaration").multiple("\n")).output(literal("\n\n\t\tComponent(int index, String id")).output(expression().output(literal(", ")).output(mark("include", "parameter").multiple(", "))).output(literal(") {\n\t\t\tthis.index = index;\n\t\t\tthis.id = id;\n\t\t\t")).output(mark("include", "assign").multiple("\n")).output(literal("\n\t\t}\n\n        @Override\n\t\tpublic int index() {\n\t\t\treturn index;\n\t\t}\n\n        @Override\n\t\tpublic String id() {\n\t\t\treturn id;\n\t\t}\n\n\t\t")).output(expression().output(mark("include", "getter").multiple("\n\n"))).output(literal("\n\n        @Override\n\t\tpublic Axis axis() {\n\t\t\treturn ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(".get();\n\t\t}\n\n\t    @Override\n\t    public boolean equals(Object o) {\n\t    \tif (this == o) return true;\n\t    \tif (o == null || getClass() != o.getClass()) return false;\n\t    \tComponent component = (Component) o;\n\t    \treturn index == component.index;\n\t    }\n\n\t    @Override\n\t    public int hashCode() {\n\t    \treturn Objects.hash(index, id);\n\t    }\n\n\t    @Override\n\t    public String toString() {\n\t        return id;\n\t    }\n\t}\n}")),
			rule().condition((type("component"))).output(literal("public static final Component ")).output(mark("name")).output(literal(" = new Component(")).output(mark("index")).output(literal(", \"")).output(mark("id")).output(literal("\"")).output(expression().output(literal(", \"")).output(mark("label")).output(literal("\""))).output(expression().output(literal(", ")).output(mark("include", "constructor").multiple(", "))).output(literal(");")),
			rule().condition((attribute("name", "label")), (trigger("declaration"))).output(literal("private final String ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("include")), (attribute("name", "label")), (trigger("staticgetter"))).output(literal("public static ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component componentByLabel(String label) {\n    return stream().filter(c -> c.label().equals(label)).findFirst().orElse(NA);\n}")),
			rule().condition((type("include")), (attribute("type", "continuous")), (trigger("staticgetter"))).output(literal("public static ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component componentBy")).output(mark("label", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range range) {\n    return stream().filter(c -> c.")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("().equals(range)).findFirst().orElse(NA);\n}")),
			rule().condition((type("include")), (attribute("type", "categorical")), (trigger("staticgetter"))).output(literal("public static ")).output(mark("axis", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component componentBy")).output(mark("label", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component component) {\n    return stream().filter(c -> c.")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("().equals(component)).findFirst().orElse(NA);\n}")),
			rule().condition((type("include")), (attribute("type", "continuous")), (trigger("declaration"))).output(literal("private final ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((type("include")), (attribute("type", "categorical")), (trigger("declaration"))).output(literal("private final ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((attribute("name", "label")), (trigger("getter"))).output(literal("public String ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n    return ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((trigger("getter")), (attribute("type", "continuous"))).output(literal("public ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n    return ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((trigger("getter")), (attribute("type", "categorical"))).output(literal("public ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n    return ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((trigger("tostring"))).output(literal("sb.append(\", ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("=\").append(")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(");")),
			rule().condition((attribute("name", "label")), (trigger("defaultvalue"))).output(literal("\"")).output(mark("id")).output(literal("\"")),
			rule().condition((trigger("defaultvalue")), (attribute("type", "categorical"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".NA")),
			rule().condition((trigger("defaultvalue")), (attribute("type", "continuous"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".component(0)")),
			rule().condition((attribute("name", "label")), (trigger("parameter"))).output(literal("String ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((attribute("type", "categorical")), (trigger("parameter"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Component ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((attribute("type", "continuous")), (trigger("parameter"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".Range ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")),
			rule().condition((attribute("name", "label")), (trigger("constructorfromtsv"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? l[")).output(mark("index")).output(literal("] : \"NA\"")),
			rule().condition((trigger("constructorfromtsv")), (attribute("type", "continuous"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".rangeOf(Double.parseDouble(l[")).output(mark("index")).output(literal("])) : ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".component(0)")),
			rule().condition((trigger("constructorfromtsv")), (attribute("type", "categorical"))).output(literal("l.length > ")).output(mark("index")).output(literal(" ? ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".component(l[")).output(mark("index")).output(literal("]) : ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".NA")),
			rule().condition((trigger("constructor"))).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(".component(\"")).output(mark("id")).output(literal("\")")),
			rule().condition((trigger("assign"))).output(literal("this.")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("label", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((trigger("put"))).output(literal("components.put(")).output(mark("index")).output(literal(", ")).output(mark("name", "FirstUpperCase")).output(literal(");\ncomponentsByName.put(\"")).output(mark("name")).output(literal("\", ")).output(mark("name", "FirstUpperCase")).output(literal(");")),
			rule().condition((trigger("field"))).output(literal("public static final Component ")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal(" = new Component(")).output(mark("index")).output(literal(", \"")).output(mark("name")).output(literal("\"")).output(expression().output(literal(", \"")).output(mark("label")).output(literal("\""))).output(literal(");")),
			rule().condition((type("components")), (attribute("embedded", "true")), (trigger("load"))).output(literal("this.components = new Component[] {NA")).output(expression().output(literal(", ")).output(mark("component").multiple(", "))).output(literal("};")),
			rule().condition((type("components")), (attribute("embedded", "false")), (trigger("load"))).output(literal("Component[] components = new Component[0];\njava.io.InputStream resource = getClass().getResourceAsStream(\"")).output(mark("resource")).output(literal("\");\nif(resource == null) throw new IllegalStateException(\"")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(": Resource \" + \"")).output(mark("resource")).output(literal("\" + \" not found\");\ntry (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(resource))) {\n\t\tcomponents = Stream.concat(Stream.of(NA),\n\t\treader.lines().map(l -> l.split(\"\t\")).map(l -> new Component(Integer.parseInt(l[0]),l[1]")).output(expression().output(literal(",")).output(mark("include", "constructorFromTsv").multiple(", "))).output(literal("))\n\t\t).toArray(Component[]::new);\n\t} catch (Exception e) {\n\t\tio.intino.alexandria.logger.Logger.error(\"Failed to load components of ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("\", e);\n\t}\n\tthis.components = components;"))
		);
	}
}