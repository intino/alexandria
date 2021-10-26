package io.intino.konos.builder.codegeneration.futures;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class FutureSchemaTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("future"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".agenda;\n\n")).output(mark("schemaImport")).output(literal("\nimport ")).output(mark("package", "validPackage")).output(literal(".FuturesService;\nimport java.time.Instant;\nimport java.time.temporal.ChronoUnit;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Schema {\n\t")).output(expression().output(mark("parameter", "field").multiple("\n"))).output(literal("\n\t")).output(expression().output(mark("option", "field").multiple("\n"))).output(literal("\n\tTimeout timeout;\n\n\t")).output(expression().output(mark("parameter", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("parameter", "setter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("option", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(mark("option", "setter").multiple("\n\n"))).output(literal("\n\n\tpublic Timeout timeout() {\n\t\treturn timeout == null ? timeout = new Timeout(null) : timeout;\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("Schema timeout(Timeout timeout) {\n\t\tthis.timeout = timeout;\n\t\treturn this;\n\t}\n\n\t")).output(mark("option", "class").multiple("\n\n")).output(literal("\n\n\tpublic static class Timeout extends FuturesService.Option {\n\t\tInstant timeout;\n\n\t\tpublic Timeout(Instant timeout) {\n\t\t\tthis.timeout = timeout == null ? null : timeout.truncatedTo(ChronoUnit.SECONDS);\n\t\t}\n\n\t\tpublic Instant on() {\n\t\t\treturn this.timeout;\n\t\t}\n\t}\n}")),
			rule().condition((type("option")), (trigger("class"))).output(literal("public static class ")).output(mark("name", "firstUpperCase")).output(literal(" extends FuturesService.Option {\n\t")).output(expression().output(mark("optionParameter", "field").multiple("\n"))).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("optionParameter", "signature").multiple(", ")).output(literal(") {\n\t\t")).output(expression().output(mark("optionParameter", "assign").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(expression().output(mark("optionParameter", "getter").multiple("\n\n"))).output(literal("\n}")),
			rule().condition((type("option")), (trigger("getter"))).output(literal("public ")).output(mark("future", "firstUpperCase")).output(literal("Schema.")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(" == null ? ")).output(mark("name", "firstLowerCase")).output(literal(" = new ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("optionParameter", "nullParameter").multiple(", ")).output(literal(") : ")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((trigger("getter"))).output(literal("public ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(mark("name", "firstLowerCase")).output(literal(";\n}")),
			rule().condition((type("option")), (trigger("setter"))).output(literal("public ")).output(mark("future", "firstUpperCase")).output(literal("Schema ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((trigger("setter"))).output(literal("public ")).output(mark("owner", "firstUpperCase")).output(literal("Schema ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("type", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(mark("name", "firstLowerCase")).output(literal(" = ")).output(mark("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")),
			rule().condition((trigger("assign"))).output(literal("this.")).output(mark("name")).output(literal(" = ")).output(mark("name")).output(literal(";")),
			rule().condition((type("parameter")), (trigger("field"))).output(literal("private ")).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((type("option")), (trigger("field"))).output(literal("private ")).output(mark("name", "FirstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(";")),
			rule().condition((trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((trigger("nullparameter"))).output(literal("null"))
		);
	}
}