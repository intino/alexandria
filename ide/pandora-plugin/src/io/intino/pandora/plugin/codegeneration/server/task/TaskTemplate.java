package io.intino.pandora.plugin.codegeneration.server.task;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class TaskTemplate extends Template {

	protected TaskTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new TaskTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "scheduled"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(".scheduling;\n\nimport org.quartz.*;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.pandora.scheduling.ScheduledTrigger;\n\npublic class ")).add(mark("name", "firstUpperCase")).add(literal("Task implements ScheduledTrigger {\n\n\tpublic void execute(JobExecutionContext context) throws JobExecutionException {\n\t\t")).add(mark("box", "FirstUpperCase")).add(literal("Box box = (")).add(mark("box", "FirstUpperCase")).add(literal("Box) context.getMergedJobDataMap().get(\"box\");\n\t\t")).add(mark("package", "ValidPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action action = new ")).add(mark("package", "ValidPackage")).add(literal(".actions.")).add(mark("name", "firstUpperCase")).add(literal("Action();\n\t\taction.box = box;\n\t\taction.execute();\n\t}\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "assign"))).add(literal("action.")).add(mark("name")).add(literal(" = ")).add(mark("name")).add(literal(";")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name"))
		);
		return this;
	}
}