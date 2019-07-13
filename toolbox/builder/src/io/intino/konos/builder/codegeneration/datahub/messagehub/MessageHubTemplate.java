package io.intino.konos.builder.codegeneration.datahub.messagehub;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessageHubTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("messagehub"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".datahub;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\npublic class MessageHub ")).output(expression().output(literal("extends ")).output(mark("type", "class"))).output(literal(" {\n\t")).output(mark("tank").multiple("\n")).output(literal("\n\n\tpublic MessageHub(")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\t")).output(mark("type", "super")).output(literal("\n\t}\n}")),
			rule().condition((type("jms")), (trigger("class"))).output(literal("io.intino.alexandria.messagehub.JmsMessageHub")),
			rule().condition((type("rabbit")), (trigger("class"))).output(literal("io.intino.alexandria.messagehub.RabbitMessageHub")),
			rule().condition((type("jms")), (trigger("super"))).output(literal("super(")).output(mark("url")).output(literal(", ")).output(mark("user")).output(literal(", ")).output(mark("password")).output(literal(");")),
			rule().condition((type("rabbit")), (trigger("super"))).output(literal("io.intino.alexandria.messagehub.RabbitMessageHub")),
			rule().condition((trigger("tank"))).output(literal("public static final String ")).output(mark("name")).output(literal(" = \"")).output(mark("qn")).output(literal("\";")),
			rule().condition((type("custom")), (trigger("parameter"))).output(literal("box.configuration().get(\"")).output(mark("value", "customParameter")).output(literal("\")")),
			rule().condition((trigger("parameter"))).output(literal("\"")).output(mark("value")).output(literal("\""))
		);
	}
}