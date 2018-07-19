package io.intino.konos.builder.codegeneration.schema;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class SchemaTemplate extends Template {

	protected SchemaTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SchemaTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "root"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport ")).add(mark("root")).add(literal(".schemas.*;\n\n")).add(mark("schema")),
			rule().add((condition("trigger", "schema"))).add(literal("public ")).add(mark("inner")).add(literal(" class ")).add(mark("name", "firstUpperCase")).add(literal(" ")).add(expression().add(literal("extends ")).add(mark("parent")).add(literal(" "))).add(literal("implements java.io.Serializable {\n\n\t")).add(mark("attribute", "declaration").multiple("\n")).add(literal("\n\n\t")).add(mark("attribute", "getter").multiple("\n\n")).add(literal("\n\n\t")).add(mark("attribute", "setter").multiple("\n\n")).add(literal("\n\t")).add(expression().add(literal("\n")).add(literal("\t")).add(mark("schema").multiple("\n"))).add(literal("\n}")),
			rule().add((condition("type", "primitive & single")), (condition("trigger", "declaration"))).add(literal("private ")).add(mark("type")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(expression().add(literal(" = ")).add(mark("defaultValue"))).add(literal(";")),
			rule().add(not(condition("type", "primitive")), (condition("type", "single")), (condition("trigger", "declaration"))).add(literal("private ")).add(mark("type", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(expression().add(literal(" = ")).add(mark("defaultValue"))).add(literal(";")),
			rule().add((condition("type", "multiple & member")), (condition("trigger", "declaration"))).add(literal("private java.util.List<")).add(mark("type", "firstUpperCase")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal(" = new java.util.ArrayList<>();")),
			rule().add((condition("type", "multiple")), (condition("trigger", "declaration"))).add(literal("private java.util.List<")).add(mark("type", "FirstUpperCase")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal(" = new java.util.ArrayList<>();")),
			rule().add((condition("type", "attributeMap")), (condition("trigger", "declaration"))).add(literal("private java.util.Map<String, String> ")).add(mark("name")).add(literal(" = new java.util.HashMap<>();")),
			rule().add((condition("type", "primitive & single")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("type")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal(";\n}")),
			rule().add(not(condition("type", "primitive")), (condition("type", "single")), (condition("trigger", "getter"))).add(literal("public ")).add(mark("type", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "multiple & member")), (condition("trigger", "getter"))).add(literal("public java.util.List<")).add(mark("type", "firstUpperCase")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal("() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal(";\n}")),
			rule().add((condition("type", "multiple")), (condition("trigger", "getter"))).add(literal("public java.util.List<")).add(mark("type", "FirstUpperCase")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal("() {\n\treturn this.")).add(mark("name", "firstLowerCase")).add(literal(";\n}")),
			rule().add((condition("type", "attributeMap")), (condition("trigger", "getter"))).add(literal("public java.util.List<String> attributeNames() {\n\treturn new java.util.ArrayList<>(this.")).add(mark("name")).add(literal(".keySet());\n}\n\npublic String attributeValue(String key) {\n\treturn this.")).add(mark("name")).add(literal(".get(key);\n}")),
			rule().add((condition("type", "primitive & single")), (condition("trigger", "setter"))).add(literal("public ")).add(mark("element", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("(")).add(mark("type")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal(" = ")).add(mark("name", "firstLowerCase")).add(literal(";\n\treturn this;\n}")),
			rule().add(not(condition("type", "primitive")), (condition("type", "single")), (condition("trigger", "setter"))).add(literal("public ")).add(mark("element", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("(")).add(mark("type", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal(" = ")).add(mark("name", "firstLowerCase")).add(literal(";\n\treturn this;\n}")),
			rule().add((condition("type", "multiple & member")), (condition("trigger", "setter"))).add(literal("public ")).add(mark("element", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal("(java.util.List<")).add(mark("type", "firstUpperCase")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal(" = ")).add(mark("name", "firstLowerCase")).add(mark("list")).add(literal(";\n\treturn this;\n}")),
			rule().add((condition("type", "multiple")), (condition("trigger", "setter"))).add(literal("public ")).add(mark("element", "firstUpperCase")).add(literal(" ")).add(mark("name", "firstLowerCase")).add(literal("(java.util.List<")).add(mark("type", "FirstUpperCase")).add(literal("> ")).add(mark("name", "firstLowerCase")).add(literal(") {\n\tthis.")).add(mark("name", "firstLowerCase")).add(literal(" = ")).add(mark("name", "firstLowerCase")).add(literal(";\n\treturn this;\n}")),
			rule().add((condition("type", "attributeMap")), (condition("trigger", "setter"))).add(literal("public ")).add(mark("element", "firstUpperCase")).add(literal(" addAttribute(String key, String value) {\n\tthis.")).add(mark("name")).add(literal(".put(key, value);\n\treturn this;\n}"))
		);
		return this;
	}
}