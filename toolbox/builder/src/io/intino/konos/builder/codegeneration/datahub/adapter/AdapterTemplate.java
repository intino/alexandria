package io.intino.konos.builder.codegeneration.datahub.adapter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AdapterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("adapter"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".datahub.feeders;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void exectute() {\n\n\t}\n}"))
		);
	}
}