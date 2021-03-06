package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class ListenerTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("listener"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".scheduling;\n\nimport org.quartz.JobExecutionContext;\nimport org.quartz.JobExecutionException;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.scheduler.ScheduledTrigger;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Listener implements ScheduledTrigger {\n\n\tpublic void execute(JobExecutionContext context) throws JobExecutionException {\n\t\t")).output(mark("box", "FirstUpperCase")).output(literal("Box box = (")).output(mark("box", "FirstUpperCase")).output(literal("Box) context.getMergedJobDataMap().get(\"box\");\n\t\t")).output(mark("target").multiple("\n")).output(literal("\n\t}\n}")),
			rule().condition((trigger("target"))).output(mark("package", "ValidPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action action = new ")).output(mark("package", "ValidPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action();\naction.box = box;\naction.execute();"))
		);
	}
}