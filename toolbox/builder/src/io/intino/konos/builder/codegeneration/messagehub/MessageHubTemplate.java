package io.intino.konos.builder.codegeneration.messagehub;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MessageHubTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("messagehub", "jms"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\npublic class MessageHub extends io.intino.alexandria.messagehub.JmsMessageHub {\n\tpublic MessageHub(String url, String user, String password, String clientId) {\n\t\tsuper(url, user, password, clientId);\n\t}\n}")),
				rule().condition((type("messagehub"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(";\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\npublic class MessageHub {\n\t")).output(mark("tank").multiple("\n")).output(literal("\n\n\tpublic MessageHub(")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\n\t}\n}"))
		);
	}
}