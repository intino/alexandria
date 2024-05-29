package io.intino.konos.builder.codegeneration.sentinel;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class SentinelsTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("sentinels")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".")).output(placeholder("box", "FirstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.scheduler.AlexandriaScheduler;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".actions.*;\nimport org.quartz.*;\nimport java.util.LinkedHashSet;\nimport java.util.Map;\nimport java.util.Set;\nimport java.util.TimeZone;\nimport java.time.ZoneId;\nimport java.io.File;\nimport io.intino.alexandria.logger.Logger;\n\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.CronScheduleBuilder.*;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\npublic class ")).output(placeholder("name", "pascalCase")).output(literal("Sentinels {\n\n\tprivate ")).output(placeholder("name", "pascalCase")).output(literal("Sentinels() {\n\n\t}\n\n\tpublic static void init(AlexandriaScheduler scheduler, java.io.File home")).output(expression().output(placeholder("hasWebhook")).output(literal(" io.intino.alexandria.http.AlexandriaSpark webServer"))).output(literal(", ")).output(placeholder("box", "FirstUpperCase")).output(literal("Box box) {\n\t\tJobDetail job;\n\t\ttry {\n\t\t\t")).output(placeholder("sentinel", "init").multiple("\n")).output(literal("\n\t\t\tscheduler.startSchedules();\n\t\t} catch (Exception e) {\n\t\t\tLogger.error(e.getMessage());\n\t\t}\n\t}\n\n\t")).output(placeholder("sentinel", "actionCall").multiple("\n")).output(literal("\n\n\tprivate static Set<Trigger> newSet(Trigger... triggers) {\n\t\tLinkedHashSet<Trigger> set = new LinkedHashSet<>();\n\t\tjava.util.Collections.addAll(set, triggers);\n\t\treturn set;\n\t}\n}")));
		rules.add(rule().condition(all(all(allTypes("sentinel"), allTypes("FileListener")), trigger("init"))).output(literal("File directory = ")).output(placeholder("file", "customizeDirectory")).output(literal(";\nif (directory != null && directory.exists()) scheduler.watchDirectory(\"")).output(placeholder("name")).output(literal("\", directory, (file, event) -> {\n\t\t\t")).output(placeholder("package", "ValidPackage")).output(literal(".actions.")).output(placeholder("name", "CamelCase")).output(literal("Action action = new ")).output(placeholder("package", "ValidPackage")).output(literal(".actions.")).output(placeholder("name", "CamelCase")).output(literal("Action();\n\t\t\taction.box = box;\n\t\t\taction.file = file;\n\t\t\taction.event = event;\n\t\t\taction.execute();\n\t\t}, ")).output(placeholder("event", "fullEvent").multiple(", ")).output(literal(");\nelse if (directory != null) Logger.warn(\"Directory \" + directory + \" not found\");")));
		rules.add(rule().condition(all(allTypes("archetype"), trigger("customizedirectory"))).output(literal("new ")).output(placeholder("package")).output(literal(".Archetype(home).")).output(placeholder("path")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("customizedirectory"))).output(literal("box.configuration().get(\"")).output(placeholder("path")).output(literal("\") == null ? null : new java.io.File(box.configuration().get(\"")).output(placeholder("path")).output(literal("\"))")));
		rules.add(rule().condition(trigger("customizedirectory")).output(literal("new java.io.File(\"")).output(placeholder("path")).output(literal("\")")));
		rules.add(rule().condition(all(all(allTypes("sentinel"), allTypes("webhook")), trigger("init"))).output(literal("webServer.route(")).output(placeholder("path", "format")).output(literal(").get(manager -> ")).output(placeholder("name", "CamelCase")).output(literal("Action(manager).execute());")));
		rules.add(rule().condition(all(all(allTypes("sentinel"), allTypes("webhook")), trigger("actioncall"))).output(literal("private static ")).output(placeholder("name", "PascalCase")).output(literal("Action ")).output(placeholder("name", "CamelCase")).output(literal("Action(io.intino.alexandria.http.spark.SparkManager manager) {\n\t")).output(placeholder("name", "PascalCase")).output(literal("Action action = new ")).output(placeholder("name", "PascalCase")).output(literal("Action();\n\t")).output(placeholder("parameter", "assign").multiple("\n")).output(literal("\n\treturn action;\n}")));
		rules.add(rule().condition(all(allTypes("sentinel"), trigger("init"))).output(literal("job = newJob(")).output(placeholder("package", "ValidPackage")).output(literal(".scheduling.")).output(placeholder("name", "PascalCase")).output(literal("Listener.class).withIdentity(\"")).output(placeholder("name")).output(literal("\").build();\njob.getJobDataMap().put(\"box\", box);\nscheduler.scheduleJob(job, newSet(")).output(placeholder("job").multiple(", ")).output(literal("), true);")));
		rules.add(rule().condition(trigger("assign")).output(literal("action.")).output(placeholder("name", "CamelCase")).output(literal(" = manager.from")).output(placeholder("in", "firstUpperCase")).output(literal("(\"")).output(placeholder("name")).output(literal("\");")));
		rules.add(rule().condition(all(allTypes("path"), trigger("format"))).output(literal("\"")).output(placeholder("name")).output(literal("\"")).output(expression().output(placeholder("custom").multiple(""))));
		rules.add(rule().condition(trigger("fullevent")).output(literal("io.intino.alexandria.scheduler.directory.DirectorySentinel.Event.")).output(placeholder("")));
		rules.add(rule().condition(all(allTypes("cronTrigger"), trigger("job"))).output(literal("newTrigger().withIdentity(\"")).output(placeholder("name")).output(literal("\").withSchedule(cronSchedule(\"")).output(placeholder("pattern")).output(literal("\")")).output(expression().output(literal(".inTimeZone(TimeZone.getTimeZone(ZoneId.of(\"")).output(placeholder("timeZone")).output(literal("\")))"))).output(literal(").build()")));
		rules.add(rule().condition(all(all(allTypes("onBootTrigger"), allTypes("delay")), trigger("job"))).output(literal("newTrigger().startAt(java.util.Date.from(java.time.Instant.now().plus(")).output(placeholder("delay")).output(literal(", java.time.temporal.ChronoUnit.MILLIS))).build()")));
		rules.add(rule().condition(all(allTypes("onBootTrigger"), trigger("job"))).output(literal("newTrigger().startNow().build()")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}