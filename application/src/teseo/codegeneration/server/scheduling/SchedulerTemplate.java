package teseo.codegeneration.server.scheduling;

import org.siani.itrules.*;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.*;

public class SchedulerTemplate extends Template {

	protected SchedulerTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new SchedulerTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
			rule().add((condition("type", "scheduler"))).add(literal("package ")).add(mark("package", "validname")).add(literal(";\n\nimport TeseoScheduler;\nimport ")).add(mark("package", "validname")).add(literal(".scheduling.*;\nimport org.quartz.*;\nimport tara.magritte.Graph;\nimport java.util.Date;\nimport java.util.LinkedHashSet;\nimport java.util.Map;\nimport java.util.Set;\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.SimpleScheduleBuilder.*;\nimport static org.quartz.CronScheduleBuilder.*;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\npublic class ")).add(mark("name", "firstUpperCase", "SnakeCaseToCamelCase")).add(literal("Schedules {\n\n\tpublic static void init(TeseoScheduler scheduler, Graph graph) {\n\t\tJobDetail job;\n\t\ttry {\n\t\t\t")).add(mark("schedule", "init").multiple("\n")).add(literal("\n\t\t\tscheduler.start();\n\t\t} catch(Exception e) {\n\t\t\te.printStackTrace();\n\t\t}\n\t}\n\n\t")).add(mark("schedule", "create").multiple("\n")).add(literal("\n\n\tpublic static void create(Class actionClass, TeseoScheduler scheduler, Map<String, Object> parameters) {\n\t\t")).add(mark("schedule", "switch")).add(literal("\n\t}\n\n\tprivate static Set<Trigger> newSet(Trigger... triggers) {\n\t\tLinkedHashSet<Trigger> set = new LinkedHashSet<>();\n\t\tjava.util.Collections.addAll(set, triggers);\n\t\treturn set;\n\t}\n}")),
			rule().add((condition("type", "schedule")), not(condition("type", "onStart")), (condition("trigger", "create"))).add(literal("public static void ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("(TeseoScheduler scheduler, Map<String, Object> parameters) {\n\ttry {\n\t\tJobDetail job = newJob(")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action.class).withIdentity(\"")).add(mark("name")).add(literal("\").build();\n\t\tfor (Map.Entry<String, Object> entry : parameters.entrySet())\n\t\t\tjob.getJobDataMap().put(entry.getKey(), entry.getValue());\n\t\tscheduler.scheduleJob(job, newSet(")).add(mark("trigger").multiple(", ")).add(literal("), true);\n\t} catch (SchedulerException e) {\n\t\te.printStackTrace();\n\t}\n}")),
			rule().add((condition("type", "schedule")), not(condition("type", "onStart")), (condition("trigger", "switch"))).add(literal("if (")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action.class.equals(actionClass)) ")).add(mark("name", "SnakeCaseToCamelCase", "firstLowerCase")).add(literal("(scheduler, parameters);")),
			rule().add((condition("type", "schedule & onStart")), (condition("trigger", "init"))).add(literal("job = newJob(")).add(mark("name", "SnakeCaseToCamelCase")).add(literal("Action.class).withIdentity(\"")).add(mark("name")).add(literal("\").build();\njob.getJobDataMap().put(\"graph\", graph);\nscheduler.scheduleJob(job, newSet(")).add(mark("trigger").multiple(", ")).add(literal("), true);")),
			rule().add((condition("type", "cron")), (condition("trigger", "trigger"))).add(literal("newTrigger().withIdentity(\"")).add(mark("name")).add(literal("\").withSchedule(cronSchedule(\"")).add(mark("pattern")).add(literal("\")).build()")),
			rule().add((condition("type", "oneTime")), (condition("type", "trigger")), (condition("trigger", "trigger"))).add(literal("newTrigger().startNow().build()")),
			rule().add(not(condition("type", "cron")), (condition("type", "trigger")), (condition("trigger", "trigger"))).add(literal("newTrigger().withIdentity(\"")).add(mark("name")).add(literal("\").startAt(new Date(")).add(mark("start")).add(literal("L)).withSchedule(simpleSchedule().withIntervalInSeconds(")).add(mark("interval")).add(literal(")).build()"))
		);
		return this;
	}
}