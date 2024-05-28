package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class CategoricalAxisTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("axis")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(".analytic.axes;\n\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".analytic.Axis;\n\nimport java.util.*;\nimport java.util.stream.Stream;\nimport java.util.function.Predicate;\nimport java.util.function.Function;\nimport java.util.stream.Collectors;\n\npublic class ")).output(placeholder("name", "PascalCase")).output(literal(" implements Axis {\n\n\tpublic static final String TITLE = \"")).output(placeholder("label")).output(literal("\";\n\n\tpublic static final Component NA = new Component(0, \"NA\"")).output(expression().output(literal(", ")).output(placeholder("include", "defaultValue").multiple(", "))).output(literal(");\n\t")).output(expression().output(placeholder("component").multiple("\n"))).output(literal("\n\n\tprivate static final class Singleton {\n\t\tprivate static final ")).output(placeholder("name", "PascalCase")).output(literal(" INSTANCE = new ")).output(placeholder("name", "PascalCase")).output(literal("();\n\t}\n\n\t// === STATIC METHODS === //\n\n\tpublic static ")).output(placeholder("name", "PascalCase")).output(literal(" get() {\n\t\treturn Singleton.INSTANCE;\n\t}\n\n\tpublic static String title() {\n\t\treturn TITLE;\n\t}\n\n\tpublic static int size() {\n\t\treturn get().getSize();\n\t}\n\n\tpublic static List<Component> components() {\n\t\treturn get().getComponents();\n\t}\n\n\tpublic static Stream<Component> stream() {\n\t\treturn get().toStream();\n\t}\n\n\tpublic static List<Component> components(Predicate<Component> filter) {\n\t\treturn get().getComponents(filter);\n\t}\n\n\tpublic static Component component(int index) {\n\t\treturn get().getComponent(index);\n\t}\n\n\tpublic static Component component(String name) {\n\t\treturn get().getComponent(name);\n\t}\n\n\t")).output(expression().output(placeholder("include", "staticGetter").multiple("\n\n"))).output(literal("\n\n\t// === === //\n\n\tprivate final Component[] components;\n\tprivate final Map<String, Component> componentsByName;\n\n\tprivate ")).output(placeholder("name", "PascalCase")).output(literal("() {\n\t\t")).output(expression().output(placeholder("components", "load"))).output(literal("\n\t\tthis.componentsByName = toStream().collect(Collectors.toMap(Component::id, Function.identity()));\n\t}\n\n\t@Override\n\tpublic String getTitle() {\n\t\treturn TITLE;\n\t}\n\n\t@Override\n\tpublic int getSize() {\n\t\treturn components.length;\n\t}\n\n\t/**\n\t* Get all components of this axis as a list, without the NA component (Starts with the component of index 1).\n\t*/\n\t@Override\n\tpublic List<Component> getComponents() {\n\t\treturn new AbstractList<>() {\n\t\t\t@Override\n\t\t\tpublic Component get(int index) {\n\t\t\t\treturn component(index + 1);\n\t\t\t}\n\t\t\t@Override\n\t\t\tpublic int size() {\n\t\t\t\treturn ")).output(placeholder("name", "PascalCase")).output(literal(".size() - 1;\n\t\t\t}\n\t\t};\n\t}\n\n\t/**\n\t* Get all components of this axis as a stream, without the NA component (Starts with the component of index 1).\n\t*/\n\t@Override\n\tpublic Stream<Component> toStream() {\n\t\treturn Arrays.stream(components).skip(1);\n\t}\n\n\tpublic List<Component> getComponents(Predicate<Component> filter) {\n\t\treturn stream().filter(filter).collect(Collectors.toList());\n\t}\n\n\t@Override\n\tpublic Component getComponent(int index) {\n\t\tif(index < 0 || index >= components.length) return NA;\n\t\treturn components[index];\n\t}\n\n\t@Override\n\tpublic Component getComponent(String name) {\n\t\treturn componentsByName.getOrDefault(name, NA);\n\t}\n\n\t@Override\n\tpublic String toString() {\n\t\treturn \"")).output(placeholder("name", "PascalCase")).output(literal("\";\n\t}\n\n\n\tpublic static class Component implements Axis.Component {\n\n\t\tprivate final int index;\n\t\tprivate final String id;\n\t\t")).output(placeholder("include", "declaration").multiple("\n")).output(literal("\n\n\t\tComponent(int index, String id")).output(expression().output(literal(", ")).output(placeholder("include", "parameter").multiple(", "))).output(literal(") {\n\t\t\tthis.index = index;\n\t\t\tthis.id = id;\n\t\t\t")).output(placeholder("include", "assign").multiple("\n")).output(literal("\n\t\t}\n\n\t\t@Override\n\t\tpublic int index() {\n\t\t\treturn index;\n\t\t}\n\n\t\t@Override\n\t\tpublic String id() {\n\t\t\treturn id;\n\t\t}\n\n\t\t")).output(expression().output(placeholder("include", "getter").multiple("\n\n"))).output(literal("\n\n\t\t@Override\n\t\tpublic Axis axis() {\n\t\t\treturn ")).output(placeholder("name", "PascalCase")).output(literal(".get();\n\t\t}\n\n\t\t@Override\n\t\tpublic boolean equals(Object o) {\n\t\t\tif (this == o) return true;\n\t\t\tif (o == null || getClass() != o.getClass()) return false;\n\t\t\tComponent component = (Component) o;\n\t\t\treturn index == component.index;\n\t\t}\n\n\t\t@Override\n\t\tpublic int hashCode() {\n\t\t\treturn Objects.hash(index, id);\n\t\t}\n\n\t\t@Override\n\t\tpublic String toString() {\n\t\t\treturn id;\n\t\t}\n\t}\n}")));
		rules.add(rule().condition(allTypes("component")).output(literal("public static final Component ")).output(placeholder("name")).output(literal(" = new Component(")).output(placeholder("index")).output(literal(", \"")).output(placeholder("id")).output(literal("\"")).output(expression().output(literal(", \"")).output(placeholder("label")).output(literal("\""))).output(expression().output(literal(", ")).output(placeholder("include", "constructor").multiple(", "))).output(literal(");")));
		rules.add(rule().condition(all(attribute("name", "label"), trigger("declaration"))).output(literal("private final String ")).output(placeholder("name", "firstLowerCase")).output(literal(";")));
		rules.add(rule().condition(all(all(allTypes("include"), attribute("name", "label")), trigger("staticgetter"))).output(literal("public static ")).output(placeholder("axis", "PascalCase")).output(literal(".Component componentByLabel(String label) {\n\treturn stream().filter(c -> c.label().equals(label)).findFirst().orElse(NA);\n}")));
		rules.add(rule().condition(all(all(allTypes("include"), attribute("type", "continuous")), trigger("staticgetter"))).output(literal("public static ")).output(placeholder("axis", "PascalCase")).output(literal(".Component componentBy")).output(placeholder("label", "PascalCase")).output(literal("(")).output(placeholder("name", "PascalCase")).output(literal(".Range range) {\n\treturn stream().filter(c -> c.")).output(placeholder("label", "CamelCase")).output(literal("().equals(range)).findFirst().orElse(NA);\n}")));
		rules.add(rule().condition(all(all(allTypes("include"), attribute("type", "categorical")), trigger("staticgetter"))).output(literal("public static ")).output(placeholder("axis", "PascalCase")).output(literal(".Component componentBy")).output(placeholder("label", "PascalCase")).output(literal("(")).output(placeholder("name", "PascalCase")).output(literal(".Component component) {\n\treturn stream().filter(c -> c.")).output(placeholder("label", "CamelCase")).output(literal("().equals(component)).findFirst().orElse(NA);\n}")));
		rules.add(rule().condition(all(all(allTypes("include"), attribute("type", "continuous")), trigger("declaration"))).output(literal("private final ")).output(placeholder("name", "PascalCase")).output(literal(".Range ")).output(placeholder("label", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(all(allTypes("include"), attribute("type", "categorical")), trigger("declaration"))).output(literal("private final ")).output(placeholder("name", "PascalCase")).output(literal(".Component ")).output(placeholder("label", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(attribute("name", "label"), trigger("getter"))).output(literal("public String ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn ")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(trigger("getter"), attribute("type", "continuous"))).output(literal("public ")).output(placeholder("name", "PascalCase")).output(literal(".Range ")).output(placeholder("label", "CamelCase")).output(literal("() {\n\treturn ")).output(placeholder("label", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(trigger("getter"), attribute("type", "categorical"))).output(literal("public ")).output(placeholder("name", "PascalCase")).output(literal(".Component ")).output(placeholder("label", "CamelCase")).output(literal("() {\n\treturn ")).output(placeholder("label", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(trigger("tostring")).output(literal("sb.append(\", ")).output(placeholder("label", "CamelCase")).output(literal("=\").append(")).output(placeholder("label", "CamelCase")).output(literal(");")));
		rules.add(rule().condition(all(attribute("name", "label"), trigger("defaultvalue"))).output(literal("\"")).output(placeholder("id")).output(literal("\"")));
		rules.add(rule().condition(all(trigger("defaultvalue"), attribute("type", "categorical"))).output(placeholder("name", "PascalCase")).output(literal(".NA")));
		rules.add(rule().condition(all(trigger("defaultvalue"), attribute("type", "continuous"))).output(placeholder("name", "PascalCase")).output(literal(".component(0)")));
		rules.add(rule().condition(all(attribute("name", "label"), trigger("parameter"))).output(literal("String ")).output(placeholder("name", "CamelCase")));
		rules.add(rule().condition(all(attribute("type", "categorical"), trigger("parameter"))).output(placeholder("name", "PascalCase")).output(literal(".Component ")).output(placeholder("label", "CamelCase")));
		rules.add(rule().condition(all(attribute("type", "continuous"), trigger("parameter"))).output(placeholder("name", "PascalCase")).output(literal(".Range ")).output(placeholder("label", "CamelCase")));
		rules.add(rule().condition(all(attribute("name", "label"), trigger("constructorfromtsv"))).output(literal("l.length > ")).output(placeholder("index")).output(literal(" ? l[")).output(placeholder("index")).output(literal("] : \"NA\"")));
		rules.add(rule().condition(all(trigger("constructorfromtsv"), attribute("type", "continuous"))).output(literal("l.length > ")).output(placeholder("index")).output(literal(" ? ")).output(placeholder("name", "PascalCase")).output(literal(".rangeOf(Double.parseDouble(l[")).output(placeholder("index")).output(literal("])) : ")).output(placeholder("name", "PascalCase")).output(literal(".component(0)")));
		rules.add(rule().condition(all(trigger("constructorfromtsv"), attribute("type", "categorical"))).output(literal("l.length > ")).output(placeholder("index")).output(literal(" ? ")).output(placeholder("name", "PascalCase")).output(literal(".component(l[")).output(placeholder("index")).output(literal("]) : ")).output(placeholder("name", "PascalCase")).output(literal(".NA")));
		rules.add(rule().condition(trigger("constructor")).output(placeholder("name", "PascalCase")).output(literal(".component(\"")).output(placeholder("id")).output(literal("\")")));
		rules.add(rule().condition(trigger("assign")).output(literal("this.")).output(placeholder("label", "CamelCase")).output(literal(" = ")).output(placeholder("label", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(trigger("put")).output(literal("components.put(")).output(placeholder("index")).output(literal(", ")).output(placeholder("name", "FirstUpperCase")).output(literal(");\ncomponentsByName.put(\"")).output(placeholder("name")).output(literal("\", ")).output(placeholder("name", "FirstUpperCase")).output(literal(");")));
		rules.add(rule().condition(trigger("field")).output(literal("public static final Component ")).output(placeholder("name", "PascalCase")).output(literal(" = new Component(")).output(placeholder("index")).output(literal(", \"")).output(placeholder("name")).output(literal("\"")).output(expression().output(literal(", \"")).output(placeholder("label")).output(literal("\""))).output(literal(");")));
		rules.add(rule().condition(all(all(allTypes("components"), attribute("embedded", "true")), trigger("load"))).output(literal("this.components = new Component[] {NA")).output(expression().output(literal(", ")).output(placeholder("component").multiple(", "))).output(literal("};")));
		rules.add(rule().condition(all(all(allTypes("components"), attribute("embedded", "false")), trigger("load"))).output(literal("Component[] components = new Component[0];\njava.io.InputStream resource = getClass().getResourceAsStream(\"")).output(placeholder("resource")).output(literal("\");\nif(resource == null) throw new IllegalStateException(\"")).output(placeholder("name", "PascalCase")).output(literal(": Resource \" + \"")).output(placeholder("resource")).output(literal("\" + \" not found\");\ntry (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(resource))) {\n\t\tcomponents = Stream.concat(Stream.of(NA),\n\t\treader.lines().map(l -> l.split(\"\t\")).map(l -> new Component(Integer.parseInt(l[0]),l[1]")).output(expression().output(literal(",")).output(placeholder("include", "constructorFromTsv").multiple(", "))).output(literal("))\n\t\t).toArray(Component[]::new);\n\t} catch (Exception e) {\n\t\tio.intino.alexandria.logger.Logger.error(\"Failed to load components of ")).output(placeholder("name", "PascalCase")).output(literal("\", e);\n\t}\n\tthis.components = components;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}