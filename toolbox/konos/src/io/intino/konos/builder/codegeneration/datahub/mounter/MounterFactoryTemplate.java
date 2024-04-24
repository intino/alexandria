package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MounterFactoryTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("factory"))).output(literal("package ")).output(mark("package")).output(literal(".mounters;\n\nimport io.intino.alexandria.event.Event;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.stream.Stream;\n\npublic class MounterFactory {\n\tprivate Map<Class<? extends Event>, List<Mounter>> mountersByClass = new HashMap<>();\n\tprivate Map<String, List<Mounter>> mountersByType = new HashMap<>();\n\n\tpublic MounterFactory(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\t")).output(mark("event", "put").multiple("\n")).output(literal("\n\t}\n\n\tpublic List<Mounter> mountersOf(Event event) {\n\t\tList<Mounter> mounters = mountersByClass.getOrDefault(event.getClass(), List.of());\n\t\tif (mounters.isEmpty()) mounters = mountersByType.getOrDefault(event.type(), List.of());\n\t\treturn mounters;\n\t}\n\n\tpublic void handle(Event... events) {\n\t\tStream.of(events).forEach(event -> {\n\t\t\tmountersOf(event).forEach(m -> m.handle(event));\n\t\t});\n\t}\n}")),
				rule().condition((trigger("put"))).output(literal("mountersByClass.put(")).output(mark("eventType")).output(literal(".class, java.util.List.of(")).output(mark("mounter").multiple(", ")).output(literal("));\nmountersByType.put(\"")).output(mark("type")).output(literal("\", java.util.List.of(")).output(mark("mounter").multiple(", ")).output(literal("));")),
				rule().condition((trigger("mounter"))).output(literal("new ")).output(mark("package")).output(literal(".")).output(mark("datamart", "lowercase")).output(literal(".mounters.")).output(mark("name", "FirstUpperCase")).output(literal("(box)"))
		);
	}
}