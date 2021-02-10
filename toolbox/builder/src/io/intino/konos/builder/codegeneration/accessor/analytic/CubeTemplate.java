package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CubeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("cube"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.led.Schema;\nimport ")).output(mark("package", "validPackage")).output(literal(".analytic.axes.*;\n\n@Schema.Definition(name = \"")).output(mark("name", "firstUpperCase")).output(literal("\", size = ")).output(mark("size")).output(literal(")\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" extends Schema {\n\n\t")).output(expression().output(mark("split"))).output(literal("\n\n\tpublic static final int SIZE = ")).output(mark("size")).output(literal(";\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("() {\n\t\tsuper(defaultByteStore());\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(io.intino.alexandria.led.buffers.store.ByteStore store) {\n\t\tsuper(store);\n    }\n\n\tpublic int size() {\n\t\treturn SIZE;\n\t}\n\n\tprotected long id() {\n\t\treturn ")).output(mark("id")).output(literal("();\n\t}\n\n\t")).output(expression().output(mark("column", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("column", "setter").multiple("\n\n"))).output(literal("\n\n\tprivate static io.intino.alexandria.led.buffers.store.ByteStore defaultByteStore() {\n\t\tjava.nio.ByteBuffer buffer = io.intino.alexandria.led.util.memory.MemoryUtils.allocBuffer((long) SIZE);\n\t\tio.intino.alexandria.led.util.memory.MemoryAddress address = io.intino.alexandria.led.util.memory.MemoryAddress.of(buffer);\n\t\treturn new io.intino.alexandria.led.buffers.store.ByteBufferStore(buffer, address, 0, buffer.capacity());\n\t}\n}")),
			rule().condition((trigger("axisimport"))).output(literal("import ")).output(mark("")).output(literal(".axis.*;")),
			rule().condition((trigger("split"))).output(literal("public enum Split {\n\t")).output(mark("enum", "asEnum").multiple(", ")).output(literal(";\n\n\tpublic abstract String qn();\n\n\tpublic static Split splitByQn(String qn) {\n\t\treturn java.util.Arrays.stream(values()).filter(c -> c.qn().equals(qn)).findFirst().orElse(null);\n\t}\n}")),
			rule().condition((trigger("asenum"))).output(mark("value", "snakeCaseToCamelCase")).output(literal(" {\n\tpublic String qn() {\n\t\treturn \"")).output(mark("qn")).output(literal("\";\n\t}\n}")),
			rule().condition((trigger("nbits"))).output(literal("NBits")),
			rule().condition((allTypes("column","integer")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(int ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Integer")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","bool")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(boolean ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setByteNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", (byte) (")).output(mark("name", "firstLowerCase")).output(literal(" ? 2 : 1));\n\treturn this;\n}")),
			rule().condition((allTypes("column","real")), (attribute("size", "32")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(float ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","real")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setAlignedReal")).output(mark("size")).output(literal("Bits(")).output(mark("offset")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (type("longInteger")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((type("column")), (type("id")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(long ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setAlignedLong(0, ")).output(mark("name", "firstLowerCase")).output(literal(");\n\treturn this;\n}")),
			rule().condition((allTypes("column","datetime")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Long")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(".toEpochMilli());\n\treturn this;\n}")),
			rule().condition((allTypes("column","date")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.set")).output(mark("aligned")).output(literal("Short")).output(expression().output(mark("bits", "nbits"))).output(literal("(")).output(mark("offset")).output(expression().output(literal(", ")).output(mark("bits"))).output(literal(", (short) ")).output(mark("name", "firstLowerCase")).output(literal(".toEpochDay());\n\treturn this;\n}")),
			rule().condition((allTypes("column","id")), (trigger("getter"))).output(literal("@Id\n@Attribute(name = \"")).output(mark("name")).output(literal("\", type = Schema.DataType.LONG, index = 0, size = 64)\npublic long ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn bitBuffer.getAlignedLong(0);\n}")),
			rule().condition((allTypes("column","categorical")), (trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(String ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? (int) NULL : ")).output(mark("type", "firstUpperCase")).output(literal(".instance().component(")).output(mark("name", "firstLowerCase")).output(literal(").index);\n\treturn this;\n}\n\npublic ")).output(mark("owner", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type", "firstUpperCase")).output(literal(".Component ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tbitBuffer.setIntegerNBits(")).output(mark("offset")).output(literal(", ")).output(mark("bits")).output(literal(", ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? (int) NULL : ")).output(mark("name", "firstLowerCase")).output(literal(".index);\n\treturn this;\n}"))
		);
	}
}