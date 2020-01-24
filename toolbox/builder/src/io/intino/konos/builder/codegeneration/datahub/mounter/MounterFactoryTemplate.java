package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MounterFactoryTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("factory"))).output(literal("package ")).output(mark("package")).output(literal(".mounters;\n​\nimport io.intino.alexandria.event.Event;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n​\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.stream.Stream;\n​\npublic class MounterFactory {\n\tprivate Map<String, List<Mounter>> mounters = new HashMap<>();\n​\n\tpublic MounterFactory(")).output(mark("box", "firstUpperCase")).output(literal("Box box) {\n\t\t")).output(mark("event", "put").multiple("\n")).output(literal("\n\t}\n​\n\tpublic List<Mounter> mountersOf(Class<? extends Event> clazz) {\n\t\treturn mounters.get(clazz.getCanonicalName());\n\t}\n​\n\tpublic List<Mounter> mountersOf(Event event) {\n\t\treturn mountersOf(event.getClass());\n\t}\n​\n\tpublic void handle(Event... events) {\n\t\tStream.of(events).forEach(event -> {\n\t\t\tList<Mounter> mounters = mountersOf(event);\n\t\t\tif (mounters != null) mounters.forEach(m -> m.handle(event));\n\t\t});\n\t}\n}")),
			rule().condition((trigger("put"))).output(literal("mounters.put(")).output(mark("name", "FirstUpperCase")).output(literal(".class.getCanonicalName(), java.util.List.of(")).output(mark("mounter").multiple(", ")).output(literal(");")),
			rule().condition((trigger("mounter"))).output(literal("new ")).output(mark("package")).output(literal(".")).output(mark("name", "FirstUpperCase")).output(literal("(box)"))
		);
	}
}