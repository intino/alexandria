package io.intino.konos.builder.codegeneration.futures;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class FutureActionTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("action"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.future.Future;\nimport java.time.*;\nimport java.util.*;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Future extends Future {\n\tprivate ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Future(")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box, String id) {\n\t\tsuper(box, id);\n\t}\n\n\tpublic void execute(")).output(mark("parameter").multiple(", ")).output(literal(") {\n\n\t}\n}"))
		);
	}
}