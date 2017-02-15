package io.intino.konos.builder.codegeneration.process.task;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class TaskerTemplate extends Template {

	protected TaskerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new TaskerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "tasker"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport ")).add(mark("package", "ValidPackage")).add(literal(".")).add(mark("box", "FirstUpperCase")).add(literal("Box;\nimport io.intino.konos.scheduling.KonosTasker;\nimport io.intino.konos.scheduling.directory.KonosDirectorySentinel;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".scheduling.*;\nimport org.quartz.*;\nimport java.util.LinkedHashSet;\nimport java.util.Map;\nimport java.util.Set;\nimport java.util.logging.Logger;\n\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.CronScheduleBuilder.*;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Tasks {\n\tprivate static final Logger logger = Logger.getGlobal();\n\n\tprivate ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Tasks() {\n\n\t}\n\n\tpublic static void init(KonosTasker tasker, ")).add(mark("box", "FirstUpperCase")).add(literal("Box box) {\n\t\tJobDetail job;\n\t\ttry {\n\t\t\t")).add(mark("task", "init").multiple("\n")).add(literal("\n\t\t\ttasker.startSchedules();\n\t\t} catch (Exception e) {\n\t\t\tlogger.severe(e.getMessage());\n\t\t}\n\t}\n\n\tprivate static Set<Trigger> newSet(Trigger... triggers) {\n\t\tLinkedHashSet<Trigger> set = new LinkedHashSet<>();\n\t\tjava.util.Collections.addAll(set, triggers);\n\t\treturn set;\n\t}\n}")),
			rule().add((condition("type", "task")), (condition("type", "DirectorySentinelTask")), (condition("trigger", "init"))).add(literal("tasker.watchDirectory(\"")).add(mark("name")).add(literal("\", new java.io.File(\"")).add(mark("file")).add(literal("\"), (f, e) -> { try {\n\t\t")).add(mark("package")).add(literal(".actions.")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action action = new ")).add(mark("package")).add(literal(".actions.")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action();\n\t\taction.box = box;\n\t\taction.directory = f.toURI().toURL();\n\t\taction.event = e;\n\t\taction.execute();\n\t} catch(java.io.IOException ignored) {}\n\t}, ")).add(mark("event", "fullPath").multiple(", ")).add(literal(");")),
			rule().add((condition("trigger", "fullPath"))).add(literal("KonosDirectorySentinel.Event.")).add(mark("value")),
			rule().add((condition("type", "task")), (condition("trigger", "init"))).add(literal("job = newJob(")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Task.class).withIdentity(\"")).add(mark("name")).add(literal("\").build();\njob.getJobDataMap().put(\"box\", box);\ntasker.scheduleJob(job, newSet(")).add(mark("job").multiple(", ")).add(literal("), true);")),
			rule().add((condition("type", "cronTrigger")), (condition("trigger", "job"))).add(literal("newTrigger().withIdentity(\"")).add(mark("name")).add(literal("\").withSchedule(cronSchedule(\"")).add(mark("pattern")).add(literal("\")).build()")),
			rule().add((condition("type", "onBootTrigger")), (condition("trigger", "job"))).add(literal("newTrigger().startNow().build()"))
		);
		return this;
	}
}