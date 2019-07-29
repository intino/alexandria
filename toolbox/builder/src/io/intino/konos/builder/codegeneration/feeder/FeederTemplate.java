package io.intino.konos.builder.codegeneration.feeder;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class FeederTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((allTypes("feeder", "simple"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".feeders;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprivate ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void exectute() {\n\n\t}\n}")),
				rule().condition((allTypes("feeder", "complex"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".feeders;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\nimport io.intino.alexandria.message.Message;\nimport java.util.Arrays;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tsuper(box);\n\t}\n\n\t")).output(mark("sensor", "class")).output(literal("\n}")),
				rule().condition((type("sensor")), (trigger("class"))).output(literal("public static class ")).output(mark("name", "FirstUpperCase")).output(literal("Sensor extends Abstract")).output(mark("name", "FirstUpperCase")).output(literal("Sensor {\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("Sensor() {\n\t}\n\n\t")).output(mark("parent", "get")).output(literal("\n\n\t")).output(mark("parent", "methods")).output(literal("\n}")),
				rule().condition((type("poll")), (trigger("get"))).output(literal("public Message get(Object... args) {\n\tString option = null; //TODO\n\treturn get(option, args);\n}")),
				rule().condition((trigger("get"))).output(literal("public Message get(Object... args) {\n\treturn null;\n}")),
				rule().condition((type("poll")), (trigger("methods"))).output(mark("eventMethod").multiple("\n")),
				rule().condition((trigger("eventmethod"))).output(literal("protected Object ")).output(mark("value", "firstLowerCase")).output(literal("(java.util.List<Object> objects) {\n\t//return null;\n}"))
		);
	}
}