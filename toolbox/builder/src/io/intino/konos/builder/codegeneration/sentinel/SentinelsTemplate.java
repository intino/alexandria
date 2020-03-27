package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class SentinelsTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("sentinels"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport ")).output(mark("package", "ValidPackage")).output(literal(".")).output(mark("box", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.scheduler.AlexandriaScheduler;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".actions.*;\nimport org.quartz.*;\nimport java.util.LinkedHashSet;\nimport java.util.Map;\nimport java.util.Set;\nimport java.time.ZoneId;\nimport java.util.TimeZone;\nimport io.intino.alexandria.logger.Logger;\n\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.CronScheduleBuilder.*;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\npublic class ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Sentinels {\n\n\tprivate ")).output(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).output(literal("Sentinels() {\n\n\t}\n\n\tpublic static void init(AlexandriaScheduler scheduler, io.intino.alexandria.http.AlexandriaSpark webServer, ")).output(mark("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tJobDetail job;\n\t\ttry {\n\t\t\t")).output(mark("sentinel", "init").multiple("\n")).output(literal("\n\t\t\tscheduler.startSchedules();\n\t\t} catch (Exception e) {\n\t\t\tLogger.error(e.getMessage());\n\t\t}\n\t}\n\n\t")).output(mark("sentinel", "actionCall").multiple("\n")).output(literal("\n\n\tprivate static Set<Trigger> newSet(Trigger... triggers) {\n\t\tLinkedHashSet<Trigger> set = new LinkedHashSet<>();\n\t\tjava.util.Collections.addAll(set, triggers);\n\t\treturn set;\n\t}\n}")),
			rule().condition((type("sentinel")), (type("DirectoryListener")), (trigger("init"))).output(literal("scheduler.watchDirectory(\"")).output(mark("name")).output(literal("\", new java.io.File(\"")).output(mark("file")).output(literal("\"), (file, event) -> {\n\t\t")).output(mark("package", "ValidPackage")).output(literal(".actions.")).output(mark("name", "SnakeCaseToCamelCase")).output(literal("Action action = new ")).output(mark("package", "ValidPackage")).output(literal(".actions.")).output(mark("name", "SnakeCaseToCamelCase")).output(literal("Action();\n\t\taction.box = box;\n\t\taction.directory = file;\n\t\taction.event = event;\n\t\taction.execute();\n\t}, ")).output(mark("event", "fullPath").multiple(", ")).output(literal(");")),
			rule().condition((type("sentinel")), (type("WebHook")), (trigger("init"))).output(literal("webServer.route(")).output(mark("path", "format")).output(literal(").get(manager -> ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal("Action(manager).execute());")),
			rule().condition((type("sentinel")), (type("WebHook")), (trigger("actioncall"))).output(literal("private static ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Action ")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal("Action(io.intino.alexandria.http.spark.SparkManager manager) {\n\t")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Action action = new ")).output(mark("name", "SnakeCaseToCamelCase", "firstUpperCase")).output(literal("Action();\n\t")).output(mark("parameter", "assign").multiple("\n")).output(literal("\n\treturn action;\n}")),
			rule().condition((trigger("assign"))).output(literal("action.")).output(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).output(literal(" = manager.from")).output(mark("in", "firstUpperCase")).output(literal("(\"")).output(mark("name")).output(literal("\");")),
			rule().condition((type("path")), (trigger("format"))).output(literal("\"")).output(mark("name")).output(literal("\"")).output(expression().output(mark("custom").multiple(""))),
			rule().condition((trigger("fullpath"))).output(literal("io.intino.alexandria.scheduler.directory.DirectorySentinel.Event.")).output(mark("")),
			rule().condition((type("sentinel")), (trigger("init"))).output(literal("job = newJob(")).output(mark("package", "ValidPackage")).output(literal(".scheduling.")).output(mark("name", "SnakeCaseToCamelCase")).output(literal("Listener.class).withIdentity(\"")).output(mark("name")).output(literal("\").build();\njob.getJobDataMap().put(\"box\", box);\nscheduler.scheduleJob(job, newSet(")).output(mark("job").multiple(", ")).output(literal("), true);\n")),
			rule().condition((type("cronTrigger")), (trigger("job"))).output(literal("newTrigger().withIdentity(\"")).output(mark("name")).output(literal("\").withSchedule(cronSchedule(\"")).output(mark("pattern")).output(literal("\")")).output(expression().output(literal(".inTimeZone(TimeZone.getTimeZone(ZoneId.of(\"")).output(mark("timeZone")).output(literal("\")))"))).output(literal(").build()")),
			rule().condition((type("onBootTrigger")), (trigger("job"))).output(literal("newTrigger().startNow().build()"))
		);
	}
}