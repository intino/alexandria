package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class MounterFactoryTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("factory")).output(literal("package ")).output(placeholder("package")).output(literal(".mounters;\n\nimport io.intino.alexandria.event.Event;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "firstUpperCase")).output(literal("Box;\n\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\nimport java.util.stream.Stream;\n\npublic class MounterFactory {\n\tprivate Map<Class<? extends Event>, List<Mounter>> mountersByClass = new HashMap<>();\n\tprivate Map<String, List<Mounter>> mountersByType = new HashMap<>();\n\n\tpublic MounterFactory(")).output(placeholder("box", "firstUpperCase")).output(literal("Box box) {\n\t\t")).output(placeholder("event", "put").multiple("\n")).output(literal("\n\t}\n\n\tpublic List<Mounter> mountersOf(Event event) {\n\t\tList<Mounter> mounters = mountersByClass.getOrDefault(event.getClass(), List.of());\n\t\tif (mounters.isEmpty()) mounters = mountersByType.getOrDefault(event.type(), List.of());\n\t\treturn mounters;\n\t}\n\n\tpublic void handle(Event... events) {\n\t\tStream.of(events).forEach(event -> {\n\t\t\tmountersOf(event).forEach(m -> m.handle(event));\n\t\t});\n\t}\n}")));
		rules.add(rule().condition(trigger("put")).output(literal("mountersByClass.put(")).output(placeholder("eventType")).output(literal(".class, java.util.List.of(")).output(placeholder("mounter").multiple(", ")).output(literal("));\nmountersByType.put(\"")).output(placeholder("type")).output(literal("\", java.util.List.of(")).output(placeholder("mounter").multiple(", ")).output(literal("));")));
		rules.add(rule().condition(trigger("mounter")).output(literal("new ")).output(placeholder("package")).output(literal(".")).output(placeholder("datamart", "lowercase")).output(literal(".mounters.")).output(placeholder("name", "FirstUpperCase")).output(literal("(box)")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}