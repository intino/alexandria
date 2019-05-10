package io.intino.konos.builder.codegeneration.datalake.feeder;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractFeederTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("feeder"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".datalake.feeders;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".datalake.Datalake;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.core.Feeder;\nimport io.intino.alexandria.core.Sensor;\nimport io.intino.alexandria.inl.Message;\n\nimport java.util.Arrays;\nimport java.util.List;\nimport java.util.Collections;\n\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" extends io.intino.alexandria.core.Feeder {\n\tprotected List<Sensor> sensors;\n\tprotected final ")).output(mark("box", "FirstUpperCase")).output(literal("Box box;\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.sensors = Arrays.asList(")).output(mark("sensor", "new").multiple(", ")).output(literal(");\n\t}\n\n\tpublic List<String> eventTypes() {\n\t\treturn Arrays.asList(")).output(mark("eventType", "quoted").multiple(", ")).output(literal(");\n\t}\n\n\n\tpublic void feed(Message event) {\n\t\t//box.nessAccesor().push(); TODO\n\n\t}\n\n\tpublic List<Sensor> sensors() {\n\t\treturn sensors;\n\t}\n\n\t")).output(mark("sensor", "class").multiple("\n")).output(literal("\n}")),
				rule().condition((type("sensor")), (trigger("new"))).output(literal("new ")).output(mark("feeder", "FirstUpperCase")).output(literal(".")).output(mark("name", "FirstUpperCase")).output(literal("Sensor()")),
				rule().condition((type("sensor")), (trigger("class"))).output(literal("static abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal("Sensor")).output(expression().output(literal(" extends io.intino.alexandria.core.sensors.")).output(mark("type"))).output(literal(" {\n\t")).output(mark("parent", "field")).output(literal("\n\n\tpublic Abstract")).output(mark("name", "FirstUpperCase")).output(literal("Sensor() {\n\t\t")).output(mark("parent", "super")).output(literal("\n\t\t")).output(expression().output(mark("width"))).output(literal("\n\t\t")).output(expression().output(mark("height"))).output(literal("\n\t}\n\n\t")).output(mark("parent", "methods")).output(literal("\n}")),
				rule().condition((type("width"))).output(literal("width(")).output(mark("value")).output(literal(");")),
				rule().condition((type("height"))).output(literal("height(")).output(mark("value")).output(literal(");")),
				rule().condition((type("documentedition")), (trigger("super"))).output(literal("super(\"")).output(mark("mode")).output(literal("\");")),
				rule().condition((type("documentsignature")), (trigger("super"))).output(literal("super(\"")).output(mark("signType")).output(literal("\", \"")).output(mark("signFormat")).output(literal("\");")),
				rule().condition((type("formedition")), (trigger("super"))).output(literal("super(\"")).output(mark("path")).output(literal("\");")),
				rule().condition((type("poll")), (trigger("field"))).output(literal("private java.util.Map<String, java.util.function.Function<List<Object>, Object>> messageBuilders = new java.util.HashMap<>();")),
				rule().condition((type("poll")), (trigger("super"))).output(literal("super(\"")).output(mark("defaultOption")).output(literal("\", Arrays.asList(")).output(mark("option").multiple(", ")).output(literal("));\nregisterMessageBuilders();")),
				rule().condition((type("poll")), (trigger("methods"))).output(literal("private void registerMessageBuilders() {\n\t")).output(mark("option", "build").multiple("\n")).output(literal("\n}\n\npublic Message get(String option, Object... args) {\n\treturn io.intino.alexandria.inl.Inl.toMessage(messageBuilders.get(option).apply(Arrays.asList(args)));\n}\n\n")).output(mark("eventMethod").multiple("\n\n")),
				rule().condition((trigger("build"))).output(literal("messageBuilders.put(\"")).output(mark("value")).output(literal("\", this::")).output(mark("event", "firstLowerCase")).output(literal(");")),
				rule().condition((trigger("eventmethod"))).output(literal("protected abstract Object ")).output(mark("value", "firstLowerCase")).output(literal("(List<Object> objects);")),
				rule().condition((trigger("option"))).output(literal("new Option(\"")).output(mark("value")).output(literal("\"")).output(expression().output(literal(", Arrays.asList(")).output(mark("option").multiple(",")).output(literal(")"))).output(literal(")")),
				rule().condition((trigger("quoted"))).output(literal("\"")).output(mark("value")).output(literal("\""))
		);
	}
}