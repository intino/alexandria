package io.intino.konos.builder.codegeneration.schema;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SchemaTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("root"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport ")).output(mark("root")).output(literal(".schemas.*;\n\n")).output(mark("schema")),
			rule().condition((trigger("schema"))).output(literal("public ")).output(expression().output(mark("inner"))).output(literal(" class ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(expression().output(literal("extends ")).output(mark("parent"))).output(literal(" implements java.io.Serializable {\n\n\t")).output(mark("attribute", "declaration").multiple("\n")).output(literal("\n\n\t")).output(mark("attribute", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(mark("attribute", "setter").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(mark("schema").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((allTypes("single","word")), (trigger("declaration"))).output(literal("private ")).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "FirstLowerCase")).output(literal(";\n\npublic enum ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}\n")),
			rule().condition((allTypes("multiple","word")), (trigger("declaration"))).output(literal("private java.util.List<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "FirstLowerCase")).output(literal(" = new java.util.ArrayList<>();\n\npublic enum ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}\n")),
			rule().condition((allTypes("single","primitive")), (trigger("declaration"))).output(literal("private ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(expression().output(literal(" = ")).output(mark("defaultValue"))).output(literal(";")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("declaration"))).output(literal("private ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(expression().output(literal(" = ")).output(mark("defaultValue"))).output(literal(";")),
			rule().condition((allTypes("multiple","member")), (trigger("declaration"))).output(literal("private java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal(" = new java.util.ArrayList<>();")),
			rule().condition((type("multiple")), (trigger("declaration"))).output(literal("private java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal(" = new java.util.ArrayList<>();")),
			rule().condition((type("attributemap")), (trigger("declaration"))).output(literal("private java.util.Map<String, String> ")).output(mark("name")).output(literal(" = new java.util.HashMap<>();")),
			rule().condition((allTypes("single","word")), (trigger("getter"))).output(literal("public ")).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "FirstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "FirstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("multiple","word")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "FirstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "FirstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("single","primitive")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("getter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((allTypes("multiple","member")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((type("multiple")), (trigger("getter"))).output(literal("public java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((type("attributemap")), (trigger("getter"))).output(literal("public java.util.List<String> attributeNames() {\n\treturn new java.util.ArrayList<>(this.")).output(mark("name")).output(literal(".keySet());\n}\n\npublic String attributeValue(String key) {\n\treturn this.")).output(mark("name")).output(literal(".get(key);\n}")),
			rule().condition((allTypes("single","word")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type", "FirstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((allTypes("multiple","word")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(java.util.List<")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((allTypes("single","primitive")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((allTypes("multiple","member")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(java.util.List<")).output(mark("type", "firstUpperCase")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((type("multiple")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(java.util.List<")).output(mark("type")).output(literal("> ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((type("attributemap")), (trigger("setter"))).output(literal("public ")).output(mark("element", "firstUpperCase")).output(literal(" addAttribute(String key, String value) {\n\tthis.")).output(mark("name")).output(literal(".put(key, value);\n\treturn this;\n}"))
		);
	}
}