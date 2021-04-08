package io.intino.konos.builder.codegeneration.facts;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ColumnsTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("column")), (type("id")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedLong(0);\n}")),
			rule().condition((type("column")), (allTypes("long","unsigned")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("ULong")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (type("long")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (allTypes("int","unsigned")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("UInteger")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (type("int")), (trigger("getter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Integer")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (allTypes("short","unsigned")), (trigger("getter"))).output(literal("public int ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("UShort")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (type("short")), (trigger("getter"))).output(literal("public short ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (allTypes("byte","unsigned")), (trigger("getter"))).output(literal("public short ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("UByte")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((type("column")), (type("byte")), (trigger("getter"))).output(literal("public byte ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Byte")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(");\n}")),
			rule().condition((allTypes("column","boolean")), (trigger("getter"))).output(literal("public boolean ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getBoolean(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","float")), (trigger("getter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Real")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","double")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.get")).output(mark("aligned")).output(literal("Real")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","datetime")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn java.time.Instant.ofEpochMilli(bitBuffer.get")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal("));\n}")),
			rule().condition((allTypes("column","date")), (trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn java.time.LocalDate.ofEpochDay(Short.toUnsignedInt(bitBuffer.get")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(")));\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("getter"))).output(literal("public ")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("type", "firstUpperCase")).output(literal(".component(bitBuffer.getIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal("));\n}")),
			rule().condition((allTypes("column","real")), (attribute("size", "32")), (trigger("getter"))).output(literal("public float ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("column","real")), (trigger("getter"))).output(literal("public double ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(");\n}")),
			rule().condition((allTypes("virtualColumn","primitive")), (trigger("abstract"))).output(literal("public abstract ")).output(mark("type", "firstLowerCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("();")),
			rule().condition((type("virtualColumn")), (trigger("abstract"))).output(literal("public abstract ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("();")),
			rule().condition((allTypes("virtualColumn","primitive")), (trigger("implementation"))).output(literal("@Override\npublic ")).output(mark("type", "firstLowerCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n    // TODO: compute and return ")).output(mark("name", "firstLowerCase")).output(literal(".\n    return ")).output(mark("defaultValue")).output(literal(";\n}")),
			rule().condition((type("virtualColumn")), (trigger("implementation"))).output(literal("@Override\npublic ")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n    // TODO: compute and return ")).output(mark("name", "firstLowerCase")).output(literal(".\n    return ")).output(mark("defaultValue")).output(literal(";\n}")),
			rule().condition((type("column")), (allTypes("int","unsigned")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(int ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("UInteger")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","int")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(int ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Integer")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","byte")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(byte ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Byte")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","short")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(short ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","byte","unsigned")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(byte ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("UByte")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","short","unsigned")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(short ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("UShort")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","boolean")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(boolean ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setBoolean(")).output(mark("offset")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (type("id")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setAlignedLong(0, ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (allTypes("long","unsigned")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("ULong")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (type("long")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","datetime")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(".toEpochMilli());\n\treturn this;\n}")),
			rule().condition((allTypes("column","date")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", (short) ")).output(mark("name", "firstLowerCase")).output(literal(".toEpochDay());\n\treturn this;\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(String ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? (int) NULL : ")).output(mark("type", "firstUpperCase")).output(literal(".component(")).output(mark("name", "firstLowerCase")).output(literal(").index());\n\treturn this;\n}\n\npublic ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? (int) NULL : ")).output(mark("name", "firstLowerCase")).output(literal(".index());\n\treturn this;\n}")),
			rule().condition((allTypes("column","integer")), (type("unsigned")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(int ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("UInteger")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","integer")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(int ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Integer")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","boolean")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(boolean ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setBoolean(")).output(mark("offset")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","float")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(float ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Real")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","double")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Real")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (allTypes("longInteger","unsigned")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("ULong")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (type("longInteger")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (type("id")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setAlignedLong(0, ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","datetime")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(".toEpochMilli());\n\treturn this;\n}")),
			rule().condition((allTypes("column","date")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", (short) ")).output(mark("name", "firstLowerCase")).output(literal(".toEpochDay());\n\treturn this;\n}")),
			rule().condition((allTypes("column","id")), (trigger("getter"))).output(literal("public long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedLong(0);\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(String ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? (int) NULL : ")).output(mark("type", "firstUpperCase")).output(literal(".component(")).output(mark("name", "firstLowerCase")).output(literal(").index());\n\treturn this;\n}\n\npublic ")).output(mark("owner", "firstUpperCase")).output(literal("Builder ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? (int) NULL : ")).output(mark("name", "firstLowerCase")).output(literal(".index());\n\treturn this;\n}"))
		);
	}
}