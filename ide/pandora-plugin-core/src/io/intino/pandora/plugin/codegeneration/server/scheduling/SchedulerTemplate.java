package io.intino.pandora.plugin.codegeneration.server.scheduling;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.CRLF;

public class SchedulerTemplate extends Template {

	protected SchedulerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SchedulerTemplate(Locale.ENGLISH, CRLF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "scheduler"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport tara.magritte.Graph;\nimport org.siani.pandora.scheduling.PandoraScheduler;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".scheduling.*;\nimport org.quartz.*;\nimport java.util.LinkedHashSet;\nimport java.util.Map;\nimport java.util.Set;\n\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.CronScheduleBuilder.*;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Schedules {\n\n\tpublic static void init(PandoraScheduler scheduler, Graph graph) {\n\t\tJobDetail job;\n\t\ttry {\n\t\t\t")).add(mark("schedule", "init").multiple("\n")).add(literal("\n\t\t\tscheduler.start();\n\t\t} catch(Exception e) {\n\t\t\te.printStackTrace();\n\t\t}\n\t}\n\n\tprivate static Set<Trigger> newSet(Trigger... triggers) {\n\t\tLinkedHashSet<Trigger> set = new LinkedHashSet<>();\n\t\tjava.util.Collections.addAll(set, triggers);\n\t\treturn set;\n\t}\n}")),
			rule().add((condition("type", "schedule")), (condition("trigger", "init"))).add(literal("job = newJob(")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Task.class).withIdentity(\"")).add(mark("name")).add(literal("\").build();\njob.getJobDataMap().put(\"graph\", graph);\nscheduler.scheduleJob(job, newSet(")).add(mark("task").multiple(", ")).add(literal("), true);")),
			rule().add((condition("type", "cronTrigger")), (condition("trigger", "task"))).add(literal("newTrigger().withIdentity(\"")).add(mark("name")).add(literal("\").withSchedule(cronSchedule(\"")).add(mark("pattern")).add(literal("\")).build()")),
			rule().add((condition("type", "oneBoot")), (condition("trigger", "task"))).add(literal("newTrigger().startNow().build()"))
		);
		return this;
	}
}