package io.intino.konos.builder.codegeneration.accessor.ui.android.templates;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SchemaTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("root"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport ")).output(mark("root")).output(literal(".*;\nimport kotlinx.serialization.Serializable\n\n@Serializable\n")).output(mark("schema")),
			rule().condition((trigger("schema"))).output(expression().output(mark("inner"))).output(literal("\nopen class ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(expression().output(literal(": ")).output(mark("parent")).output(literal("()"))).output(literal(" {\n\n\t")).output(mark("attribute", "declaration").multiple("\n")).output(literal("\n\n\t")).output(mark("attribute", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(mark("attribute", "setter").multiple("\n\n")).output(literal("\n\n\t")).output(expression().output(mark("schema").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((allTypes("word","single")), (trigger("declaration"))).output(literal("private lateinit var ")).output(mark("name", "FirstLowerCase")).output(literal(" : ")).output(mark("type", "FirstUpperCase")).output(literal("\n\nenum class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}\n")),
			rule().condition((allTypes("word","multiple")), (trigger("declaration"))).output(literal("private var ")).output(mark("name", "FirstLowerCase")).output(literal(" : List<")).output(mark("type", "FirstUpperCase")).output(literal("> = arrayListOf()\n\nenum class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\t")).output(mark("words").multiple(", ")).output(literal("\n}\n")),
			rule().condition((allTypes("primitive","single")), (trigger("declaration"))).output(literal("private")).output(expression().output(literal(" ")).output(mark("lateInit")).output(literal("lateinit"))).output(literal(" var ")).output(mark("name", "firstLowerCase")).output(literal(" : ")).output(mark("typeFrame")).output(expression().output(literal(" = ")).output(mark("defaultValue"))),
			rule().condition(not(type("primitive")), (type("single")), (trigger("declaration"))).output(literal("private")).output(expression().output(literal(" ")).output(mark("lateInit")).output(literal("lateinit"))).output(literal(" var ")).output(mark("name", "firstLowerCase")).output(literal(" : ")).output(mark("type", "firstUpperCase")).output(expression().output(literal(" = ")).output(mark("defaultValue"))),
			rule().condition((type("map")), (trigger("declaration"))).output(literal("private var ")).output(mark("name", "firstLowerCase")).output(literal(" : Map<")).output(mark("key", "renderMapType")).output(literal(", ")).output(mark("value", "renderMapType")).output(literal("> = mapOf()")),
			rule().condition((type("multiple")), (anyTypes("object","schema")), (trigger("declaration"))).output(literal("private var ")).output(mark("name", "firstLowerCase")).output(literal(" : List<")).output(mark("type", "firstUpperCase")).output(literal("> = arrayListOf()")),
			rule().condition((type("multiple")), (trigger("declaration"))).output(literal("private var ")).output(mark("name", "firstLowerCase")).output(literal(" : List<")).output(mark("type")).output(literal("> = arrayListOf()")),
			rule().condition((allTypes("word","single")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "FirstLowerCase")).output(literal("() : ")).output(mark("type", "FirstUpperCase")).output(literal(" {\n\treturn ")).output(mark("name", "FirstLowerCase")).output(literal("\n}")),
			rule().condition((allTypes("word","multiple")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "FirstLowerCase")).output(literal("() : List<")).output(mark("type", "FirstUpperCase")).output(literal("> {\n\treturn ")).output(mark("name", "FirstLowerCase")).output(literal("\n}")),
			rule().condition((allTypes("primitive","single")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() : ")).output(mark("typeFrame")).output(literal(" {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal("\n}")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() : ")).output(mark("type", "firstUpperCase")).output(literal(" {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal("\n}")),
			rule().condition((allTypes("multiple","schema")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List() : List<")).output(mark("type", "firstUpperCase")).output(literal("> {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal("\n}\n\nfun ")).output(mark("name", "firstLowerCase")).output(literal("List(predicate : java.util.function.Predicate<")).output(mark("type", "firstUpperCase")).output(literal(">) : List<")).output(mark("type", "firstUpperCase")).output(literal("> {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(".stream().filter(predicate).toList()\n}\n\nfun ")).output(mark("name", "firstLowerCase")).output(literal("(predicate : java.util.function.Predicate<")).output(mark("type", "firstUpperCase")).output(literal(">) : ")).output(mark("type", "firstUpperCase")).output(literal(" {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal(".stream().filter(predicate).findFirst().orElse(null)\n}")),
			rule().condition((allTypes("multiple","object")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() : List<")).output(mark("type", "firstUpperCase")).output(literal("> {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal("\n}")),
			rule().condition((type("multiple")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("() : List<")).output(mark("type")).output(literal("> {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal("\n}")),
			rule().condition((type("map")), (trigger("getter"))).output(literal("fun ")).output(mark("name", "firstLowerCase")).output(literal("() : Map<")).output(mark("key", "renderMapType")).output(literal(", ")).output(mark("value", "renderMapType")).output(literal("> {\n\treturn this.")).output(mark("name", "firstLowerCase")).output(literal("\n}")),
			rule().condition((type("map")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("name", "firstLowerCase")).output(literal(" : Map<")).output(mark("key", "renderMapType")).output(literal(", ")).output(mark("value", "renderMapType")).output(literal(">) : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition((type("list")), (trigger("rendermaptype"))).output(literal("List<")).output(mark("type", "firstUpperCase")).output(literal(">")),
			rule().condition((trigger("rendermaptype"))).output(mark("type", "firstUpperCase")),
			rule().condition((allTypes("word","single")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : ")).output(mark("type", "FirstUpperCase")).output(literal(") : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition((allTypes("word","multiple")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : List<")).output(mark("type", "FirstUpperCase")).output(literal(">) : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition((allTypes("primitive","single")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : ")).output(mark("typeFrame")).output(literal(") : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition(not(type("primitive")), (type("single")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : ")).output(mark("type", "firstUpperCase")).output(literal(") : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(";\n\treturn this\n}")),
			rule().condition((allTypes("multiple","object")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : List<")).output(mark("type", "firstUpperCase")).output(literal(">) : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition((allTypes("multiple","schema")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("List(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : List<")).output(mark("type", "firstUpperCase")).output(literal(">) : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition((type("multiple")), (trigger("setter"))).output(literal("fun ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("(")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal(" : List<")).output(mark("type")).output(literal(">) : ")).output(mark("element", "firstUpperCase")).output(literal(" {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "snakeCaseToCamelCase", "firstLowerCase")).output(literal("\n\treturn this\n}")),
			rule().condition((allTypes("typeFrame","datetime"))).output(literal("kotlinx.datetime.Instant")),
			rule().condition((allTypes("typeFrame","date"))).output(literal("kotlinx.datetime.Instant")),
			rule().condition((allTypes("typeFrame","real"))).output(literal("Double")),
			rule().condition((allTypes("typeFrame","integer"))).output(literal("Int")),
			rule().condition((allTypes("typeFrame","longinteger"))).output(literal("Long")),
			rule().condition((type("typeFrame"))).output(mark("value")),
			rule().condition((trigger("inner"))).output(literal("@kotlinx.serialization.Serializable"))
		);
	}
}