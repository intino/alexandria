package io.intino.konos.builder.codegeneration.services.taskinbox;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class InboxTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("inbox"))).output(literal("package ")).output(mark("package")).output(literal(".taskinbox;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal(" implements ")).output(mark("taskInbox", "firstUpperCase")).output(literal("Service.InboxDispatcher<")).output(mark("input", "resolve")).output(literal(", ")).output(mark("output")).output(literal("> {\n\t")).output(mark("box", "firstUpperCase")).output(literal("Box box;\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void onRequest(")).output(mark("input", "resolve")).output(literal(" input) {\n\t\t//TODO save input\n\t}\n\n\tpublic ")).output(mark("output")).output(literal(" onResponse() {\n\t\treturn null; //TODO\n\t}\n}")),
				rule().condition((type("list")), (trigger("resolve"))).output(literal("java.util.List<")).output(mark("value")).output(literal(">")),
				rule().condition((trigger("resolve"))).output(mark("value"))
		);
	}
}