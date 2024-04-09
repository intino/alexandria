package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SchemaAnnotatedTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("root"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport ")).output(mark("root")).output(literal(".schemas.*;\n\n")).output(mark("schema")),
			rule().condition((trigger("schema"))).output(literal("public ")).output(expression().output(mark("inner"))).output(literal(" class ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(expression().output(literal("extends ")).output(mark("parent"))).output(literal(" implements java.io.Serializable {\n\n\t")).output(mark("attribute", "declaration").multiple("\n")).output(literal("\n\n\t")).output(mark("attribute", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(mark("attribute", "setter").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(mark("schema").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((allTypes("word","single")), (trigger("declaration"))).output(literal("private ")).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "FirstLowerCase")).output(literal(";\n\npublic enum ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}\n")),
				rule().condition((allTypes("word", "list")), (trigger("declaration"))).output(literal("private java.util.List<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "FirstLowerCase")).output(literal(" = new java.util.ArrayList<>();\n\npublic enum ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}")),
				rule().condition((allTypes("word", "set")), (trigger("declaration"))).output(literal("private java.util.Set<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "FirstLowerCase")).output(literal(" = new java.util.HashSet<>();\n\npublic enum ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}")),
			rule().condition((allTypes("primitive","single")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name")).output(literal("\")\nprivate ")).output(mark("type")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(expression().output(literal(" = ")).output(mark("defaultValue"))).output(literal(";")),
			rule().condition((type("schema")), (type("single")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name", "firstLowerCase")).output(literal("\")\nprivate ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(expression().output(literal(" = ")).output(mark("defaultValue"))).output(literal(";")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name")).output(literal("\")\nprivate ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(expression().output(literal(" = ")).output(mark("defaultValue"))).output(literal(";")),
			rule().condition((type("map")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name")).output(literal("\")\nprivate java.util.Map<")).output(mark("key", "renderMapType")).output(literal(", ")).output(mark("value", "renderMapType")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new java.util.HashMap<>();")),
				rule().condition((type("list")), (anyTypes("object", "schema")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name", "firstLowerCase")).output(literal("\")\nprivate java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new java.util.ArrayList<>();")),
				rule().condition((type("list")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name")).output(literal("\")\nprivate java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new java.util.ArrayList<>();")),
				rule().condition((type("set")), (trigger("declaration"))).output(literal("@com.google.gson.annotations.SerializedName(\"")).output(mark("name")).output(literal("\")\nprivate java.util.Set<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = new java.util.HashSet<>();")),
			rule().condition((allTypes("word","single")), (trigger("getter"))).output(literal("public ")).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "FirstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "snakeCaseToCamelCase", "FirstLowerCase")).output(literal(";\n}")),
				rule().condition((anyTypes("object", "word")), (type("list")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
				rule().condition((anyTypes("object", "word")), (type("set")), (trigger("getter"))).output(literal("public java.util.Set<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "FirstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "FirstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("primitive","single")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("getter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
				rule().condition((allTypes("schema", "list")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}\n\npublic java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal("List(java.util.function.Predicate<")).output(mark("type", "firstUpperCase")).output(literal("> predicate) {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".stream().filter(predicate).collect(java.util.stream.Collectors.toList());\n}\n\npublic ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(java.util.function.Predicate<")).output(mark("type", "firstUpperCase")).output(literal("> predicate) {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(".stream().filter(predicate).findFirst().orElse(null);\n}")),
				rule().condition((allTypes("list", "object")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
				rule().condition((allTypes("set", "object")), (trigger("getter"))).output(literal("public java.util.Set<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
				rule().condition((type("list")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
				rule().condition((type("set")), (trigger("getter"))).output(literal("public java.util.Set<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((type("map")), (trigger("getter"))).output(literal("public java.util.Map<")).output(mark("key", "renderMapType")).output(literal(", ")).output(mark("value", "renderMapType")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((type("map")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(java.util.Map<")).output(mark("key", "renderMapType")).output(literal(", ")).output(mark("value", "renderMapType")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((type("list")), (trigger("rendermaptype"))).output(literal("java.util.List<")).output(mark("type", "firstUpperCase")).output(literal(">")),
			rule().condition((trigger("rendermaptype"))).output(mark("type", "firstUpperCase")),
			rule().condition((allTypes("word","single")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((allTypes("word", "list")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(java.util.List<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((allTypes("word", "set")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(java.util.Set<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((allTypes("primitive","single")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((allTypes("list", "object")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((allTypes("list", "schema")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List(java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((type("list")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((allTypes("set", "schema")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("Set(java.util.Set<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
				rule().condition((type("set")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(java.util.Set<")).output(mark("type")).output(literal("> ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this;\n}"))
		);
	}
}