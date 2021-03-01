package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class CubeTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("cube"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".analytic.cubes;\n\nimport io.intino.alexandria.led.Schema;\nimport io.intino.alexandria.led.allocators.SchemaFactory;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\nimport io.intino.alexandria.led.buffers.store.ByteBufferStore;\nimport java.util.UUID;\nimport ")).output(mark("package", "validPackage")).output(literal(".analytic.axes.*;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Builder extends Schema {\n\n\t")).output(expression().output(mark("split"))).output(literal("\n\n\tpublic static final int SIZE = ")).output(mark("size")).output(literal("; // Bytes\n    public static final UUID SERIAL_UUID = UUID.fromString(\"")).output(mark("serialUUID")).output(literal("\");\n    public static final SchemaFactory<")).output(mark("name", "firstUpperCase")).output(literal("Builder> FACTORY = new SchemaFactory<>(")).output(mark("name", "firstUpperCase")).output(literal("Builder.class) {\n        @Override\n        public ")).output(mark("name", "firstUpperCase")).output(literal("Builder newInstance(ByteStore store) {\n            return new ")).output(mark("name", "firstUpperCase")).output(literal("Builder(store);\n        }\n    };\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Builder() {\n\t\tsuper(new ByteBufferStore(SIZE));\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Builder(io.intino.alexandria.led.buffers.store.ByteStore store) {\n\t\tsuper(store);\n    }\n\n    @Override\n\tpublic long id() {\n\t\treturn bitBuffer.getAlignedLong(0);\n\t}\n\n    @Override\n\tpublic int size() {\n\t\treturn SIZE;\n\t}\n\n    @Override\n\tpublic UUID serialUUID() {\n\t    return SERIAL_UUID;\n\t}\n\n\t")).output(expression().output(mark("column", "setter").multiple("\n\n"))).output(literal("\n\n}")),
			rule().condition((trigger("axisimport"))).output(literal("import ")).output(mark("")).output(literal(".axis.*;")),
			rule().condition((trigger("split"))).output(literal("public enum Split {\n\t")).output(mark("enum", "asEnum").multiple(", ")).output(literal(";\n\n\tpublic abstract String qn();\n\n\tpublic static Split splitByQn(String qn) {\n\t\treturn java.util.Arrays.stream(values()).filter(c -> c.qn().equals(qn)).findFirst().orElse(null);\n\t}\n}")),
			rule().condition((trigger("asenum"))).output(mark("value", "snakeCaseToCamelCase")).output(literal(" {\n\tpublic String qn() {\n\t\treturn \"")).output(mark("qn")).output(literal("\";\n\t}\n}")),
			rule().condition((trigger("nbits"))).output(literal("NBits"))
		);
	}
}