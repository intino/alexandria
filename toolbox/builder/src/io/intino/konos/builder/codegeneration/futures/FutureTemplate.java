package io.intino.konos.builder.codegeneration.futures;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class FutureTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("future"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".actions;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprivate final ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t")).output(mark("option").multiple("\n\n")).output(literal("\n\n\t@Override\n\tprotected void onTimeout(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\n\t}\n}")),
			rule().condition((trigger("option"))).output(literal("@Override\nprotected void on")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(expression().output(literal(", ")).output(mark("optionParameter", "signature").multiple(", "))).output(literal(") {\n\n}")),
			rule().condition((trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name")),
			rule().condition((trigger("name"))).output(mark("name")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}