package io.intino.konos.builder.codegeneration.task;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class SchedulerTemplate extends Template {

	protected SchedulerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SchedulerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "scheduler"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport ")).add(mark("package", "ValidPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.alexandria.scheduler.AlexandriaScheduler;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".scheduling.*;\nimport org.quartz.*;\nimport java.util.LinkedHashSet;\nimport java.util.Map;\nimport java.util.Set;\nimport java.time.ZoneId;\nimport java.util.TimeZone;\nimport io.intino.alexandria.logger.Logger;\n\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.CronScheduleBuilder.*;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Tasks {\n\n\tprivate ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Tasks() {\n\n\t}\n\n\tpublic static void init(AlexandriaScheduler tasker, ")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tJobDetail job;\n\t\ttry {\n\t\t\t")).add(mark("task", "init").multiple("\n")).add(literal("\n\t\t\ttasker.startSchedules();\n\t\t} catch (Exception e) {\n\t\t\tLogger.error(e.getMessage());\n\t\t}\n\t}\n\n\tprivate static Set<Trigger> newSet(Trigger... triggers) {\n\t\tLinkedHashSet<Trigger> set = new LinkedHashSet<>();\n\t\tjava.util.Collections.addAll(set, triggers);\n\t\treturn set;\n\t}\n}")),
			rule().add((condition("type", "task")), (condition("type", "DirectorySentinelTask")), (condition("trigger", "init"))).add(literal("tasker.watchDirectory(\"")).add(mark("name")).add(literal("\", new java.io.File(\"")).add(mark("file")).add(literal("\"), (f, e) -> { try {\n\t\t")).add(mark("package")).add(literal(".actions.")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action action = new ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action();\n\t\taction.box = box;\n\t\taction.directory = f.toURI().toURL();\n\t\taction.event = e;\n\t\taction.execute();\n\t} catch(java.io.IOException ignored) {}\n\t}, ")).add(mark("event", "fullPath").multiple(", ")).add(literal(");")),
			rule().add((condition("trigger", "fullPath"))).add(literal("io.intino.alexandria.scheduler.directory.KonosDirectorySentinel.Event.")).add(mark("value")),
			rule().add((condition("type", "task")), (condition("trigger", "init"))).add(literal("job = newJob(")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Task.class).withIdentity(\"")).add(mark("name")).add(literal("\").build();\njob.getJobDataMap().put(\"box\", box);\ntasker.scheduleJob(job, newSet(")).add(mark("job").multiple(", ")).add(literal("), true);\n")),
			rule().add((condition("type", "cronTrigger")), (condition("trigger", "job"))).add(literal("newTrigger().withIdentity(\"")).add(mark("name")).add(literal("\").withSchedule(cronSchedule(\"")).add(mark("pattern")).add(literal("\")")).add(expression().add(literal(".inTimeZone(TimeZone.getTimeZone(ZoneId.of(\"")).add(mark("timeZone")).add(literal("\")))"))).add(literal(").build()")),
			rule().add((condition("type", "onBootTrigger")), (condition("trigger", "job"))).add(literal("newTrigger().startNow().build()"))
		);
		return this;
	}
}