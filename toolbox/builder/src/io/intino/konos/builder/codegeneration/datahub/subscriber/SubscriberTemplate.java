package io.intino.konos.builder.codegeneration.datahub.subscriber;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SubscriberTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("subscriber"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".subscribers;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" implements java.util.function.Consumer<")).output(mark("type")).output(literal("> {\n\tprivate final ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void accept(")).output(mark("type")).output(literal(" event) {\n\n\t}\n}"))
		);
	}
}