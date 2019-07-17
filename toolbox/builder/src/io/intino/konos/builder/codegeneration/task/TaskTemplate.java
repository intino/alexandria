package io.intino.konos.builder.codegeneration.task;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class TaskTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("task"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(".scheduling;\n\nimport org.quartz.JobExecutionContext;\nimport org.quartz.JobExecutionException;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.scheduler.ScheduledTrigger;\n\npublic class ")).output(mark("name", "firstUpperCase")).output(literal("Task implements ScheduledTrigger {\n\n\tpublic void execute(JobExecutionContext context) throws JobExecutionException {\n\t\t")).output(mark("box", "FirstUpperCase")).output(literal("Box box = (")).output(mark("box", "FirstUpperCase")).output(literal("Box) context.getMergedJobDataMap().get(\"box\");\n\t\t")).output(mark("target").multiple("\n")).output(literal("\n\t}\n}")),
				rule().condition((type("mounter")), (trigger("target"))).output(mark("package", "ValidPackage")).output(literal(".datahub.mounters.")).output(mark("name", "firstUpperCase")).output(literal("Mounter ")).output(mark("name")).output(literal("Mounter = new ")).output(mark("package", "ValidPackage")).output(literal(".datahub.mounters.")).output(mark("name", "firstUpperCase")).output(literal("Mounter();\n")).output(mark("name")).output(literal("Mounter.box = box;\n")).output(mark("name")).output(literal("Mounter.execute();")),
				rule().condition((type("feeder")), (trigger("target"))).output(literal("new ")).output(mark("package", "ValidPackage")).output(literal(".datahub.feeders.")).output(mark("name", "firstUpperCase")).output(literal("Feeder(box).execute();")),
				rule().condition((trigger("target"))).output(mark("package", "ValidPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action action = new ")).output(mark("package", "ValidPackage")).output(literal(".actions.")).output(mark("name", "firstUpperCase")).output(literal("Action();\naction.box = box;\naction.execute();")),
				rule().condition((type("parameter")), (trigger("assign"))).output(literal("action.")).output(mark("name")).output(literal(" = ")).output(mark("name")).output(literal(";")),
				rule().condition((type("parameter")), (trigger("name"))).output(mark("name"))
		);
	}
}