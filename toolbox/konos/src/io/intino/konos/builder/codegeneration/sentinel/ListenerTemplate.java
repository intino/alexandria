package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class ListenerTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("listener")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(".scheduling;\n\nimport org.quartz.JobExecutionContext;\nimport org.quartz.JobExecutionException;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.scheduler.ScheduledTrigger;\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Listener implements ScheduledTrigger {\n\n\tpublic void execute(JobExecutionContext context) throws JobExecutionException {\n\t\t")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box = (")).output(placeholder("box", "FirstUpperCase")).output(literal("Box) context.getMergedJobDataMap().get(\"box\");\n\t\t")).output(placeholder("target").multiple("\n")).output(literal("\n\t}\n}")));
		rules.add(rule().condition(trigger("target")).output(placeholder("package", "ValidPackage")).output(literal(".actions.")).output(placeholder("name", "firstUpperCase")).output(literal("Action action = new ")).output(placeholder("package", "ValidPackage")).output(literal(".actions.")).output(placeholder("name", "firstUpperCase")).output(literal("Action();\naction.box = box;\naction.execute();")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}