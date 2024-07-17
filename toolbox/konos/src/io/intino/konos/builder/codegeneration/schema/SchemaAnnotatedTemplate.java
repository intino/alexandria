package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class SchemaAnnotatedTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("root")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport ")).output(placeholder("root")).output(literal(".schemas.*;\n\n")).output(placeholder("schema")));
		rules.add(rule().condition(trigger("schema")).output(literal("public ")).output(expression().output(placeholder("inner"))).output(literal(" class ")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(expression().output(literal("extends ")).output(placeholder("parent"))).output(literal(" implements java.io.Serializable {\n\n\t")).output(placeholder("attribute", "declaration").multiple("\n")).output(literal("\n\n\t")).output(placeholder("attribute", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(placeholder("attribute", "setter").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(placeholder("schema").multiple("\n\n"))).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("word", "single"), trigger("declaration"))).output(literal("private ")).output(placeholder("type", "FirstUpperCase")).output(literal(" ")).output(placeholder("name", "FirstLowerCase")).output(literal(";\n\npublic enum ")).output(placeholder("name", "FirstUpperCase")).output(literal(" {\n\t")).output(placeholder("words").multiple(", ")).output(literal("\n}\n")));
		rules.add(rule().condition(all(allTypes("word", "list"), trigger("declaration"))).output(literal("private java.util.List<")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "FirstLowerCase")).output(literal(" = new java.util.ArrayList<>();\n\npublic enum ")).output(placeholder("name", "FirstUpperCase")).output(literal(" {\n\t")).output(placeholder("words").multiple(", ")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("word", "set"), trigger("declaration"))).output(literal("private java.util.Set<")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "FirstLowerCase")).output(literal(" = new java.util.HashSet<>();\n\npublic enum ")).output(placeholder("name", "FirstUpperCase")).output(literal(" {\n\t")).output(placeholder("words").multiple(", ")).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("primitive", "single"), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name")).output(literal("\")\nprivate ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(expression().output(literal(" = ")).output(placeholder("defaultValue"))).output(literal(";")));
		rules.add(rule().condition(all(all(allTypes("schema"), allTypes("single")), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\")\nprivate ")).output(placeholder("type", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(expression().output(literal(" = ")).output(placeholder("defaultValue"))).output(literal(";")));
		rules.add(rule().condition(all(all(not(allTypes("primitive")), allTypes("single")), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name")).output(literal("\")\nprivate ")).output(placeholder("type", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(expression().output(literal(" = ")).output(placeholder("defaultValue"))).output(literal(";")));
		rules.add(rule().condition(all(allTypes("map"), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name")).output(literal("\")\nprivate java.util.Map<")).output(placeholder("key", "renderMapType")).output(literal(", ")).output(placeholder("value", "renderMapType")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(" = new java.util.HashMap<>();")));
		rules.add(rule().condition(all(all(allTypes("list"), any(allTypes("object"), allTypes("schema"))), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name", "firstLowerCase")).output(literal("\")\nprivate java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(" = new java.util.ArrayList<>();")));
		rules.add(rule().condition(all(allTypes("list"), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name")).output(literal("\")\nprivate java.util.List<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(" = new java.util.ArrayList<>();\n")));
		rules.add(rule().condition(all(allTypes("set"), trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(placeholder("name")).output(literal("\")\nprivate java.util.Set<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(" = new java.util.HashSet<>();")));
		rules.add(rule().condition(all(allTypes("word", "single"), trigger("getter"))).output(literal("public ")).output(placeholder("type", "FirstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn ")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(all(any(allTypes("object"), allTypes("word")), allTypes("list")), trigger("getter"))).output(literal("public java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "firstLowerCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(all(any(allTypes("object"), allTypes("word")), allTypes("set")), trigger("getter"))).output(literal("public java.util.Set<")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn ")).output(placeholder("name", "FirstLowerCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("primitive", "single"), trigger("getter"))).output(literal("public ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(all(not(allTypes("primitive")), allTypes("single")), trigger("getter"))).output(literal("public ")).output(placeholder("type", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("schema", "list"), trigger("getter"))).output(literal("public java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("List() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}\n\npublic java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "firstLowerCase")).output(literal("List(java.util.function.Predicate<")).output(placeholder("type", "firstUpperCase")).output(literal("> predicate) {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(".stream().filter(predicate).collect(java.util.stream.Collectors.toList());\n}\n\npublic ")).output(placeholder("type", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("(java.util.function.Predicate<")).output(placeholder("type", "firstUpperCase")).output(literal("> predicate) {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(".stream().filter(predicate).findFirst().orElse(null);\n}")));
		rules.add(rule().condition(all(allTypes("list", "object"), trigger("getter"))).output(literal("public java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("set", "object"), trigger("getter"))).output(literal("public java.util.Set<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("list"), trigger("getter"))).output(literal("public java.util.List<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("set"), trigger("getter"))).output(literal("public java.util.Set<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "firstLowerCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("map"), trigger("getter"))).output(literal("public java.util.Map<")).output(placeholder("key", "renderMapType")).output(literal(", ")).output(placeholder("value", "renderMapType")).output(literal("> ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(placeholder("name", "CamelCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("map"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("(java.util.Map<")).output(placeholder("key", "renderMapType")).output(literal(", ")).output(placeholder("value", "renderMapType")).output(literal("> ")).output(placeholder("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("list"), trigger("rendermaptype"))).output(literal("java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal(">")));
		rules.add(rule().condition(trigger("rendermaptype")).output(placeholder("type", "firstUpperCase")));
		rules.add(rule().condition(all(allTypes("word", "single"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(")).output(placeholder("type", "FirstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("word", "list"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(java.util.List<")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("word", "set"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(java.util.Set<")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("primitive", "single"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(all(not(allTypes("primitive")), allTypes("single")), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(")).output(placeholder("type", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("list", "object"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("(java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("list", "schema"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("List(java.util.List<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("list"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(java.util.List<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("set", "schema"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("Set(java.util.Set<")).output(placeholder("type", "firstUpperCase")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(all(allTypes("set"), trigger("setter"))).output(literal("public ")).output(placeholder("element", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "CamelCase")).output(literal("(java.util.Set<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name", "CamelCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "CamelCase")).output(literal(" = ")).output(placeholder("name", "CamelCase")).output(literal(";\n\treturn this;\n}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}