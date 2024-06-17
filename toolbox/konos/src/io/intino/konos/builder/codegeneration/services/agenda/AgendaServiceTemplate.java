package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AgendaServiceTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("service")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "PascalCase")).output(literal("Box;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".agenda.*;\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.scheduler.AlexandriaScheduler;\nimport org.apache.commons.io.FileUtils;\nimport io.intino.alexandria.Json;\n\n")).output(placeholder("schemaImport")).output(literal("\nimport org.quartz.*;\n\nimport java.io.File;\nimport java.io.IOException;\nimport java.nio.file.Files;\nimport java.time.Instant;\nimport java.time.temporal.ChronoUnit;\nimport java.util.*;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\n\nimport static io.intino.alexandria.Json.fromString;\nimport static org.quartz.CronScheduleBuilder.cronSchedule;\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\n@SuppressWarnings(\"unchecked\")\npublic class AgendaService {\n\tpublic static final String BaseUri = \"")).output(placeholder("baseUri")).output(literal("/\";\n\tprivate static final Map<String, File> sources = new HashMap<>();\n\tprivate static final Map<String, Instant> timeouts = new HashMap<>();\n\tprivate final ")).output(placeholder("box", "PascalCase")).output(literal("Box box;\n\tprivate final File root;\n\n\tpublic AgendaService(")).output(placeholder("box", "PascalCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.root = ")).output(placeholder("rootPath", "customizeDirectory")).output(literal(";\n\t}\n\n\t")).output(placeholder("future", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(placeholder("future", "class").multiple("\n\n")).output(literal("\n\n\t")).output(placeholder("future", "execute").multiple("\n\n")).output(literal("\n\n\tpublic AlexandriaSpark<?> setup(AlexandriaSpark<?> server, AlexandriaScheduler scheduler) {\n\t\tloadFutures();\n\t\tstartTimers(scheduler);\n\t\tstartService(server);\n\t\treturn server;\n\t}\n\n\tpublic Create create() {\n\t\treturn new Create();\n\t}\n\n\tpublic Remove remove() {\n\t\treturn new Remove();\n\t}\n\n\tprivate void startTimers(AlexandriaScheduler scheduler) {\n\t\ttry {\n\t\t\tJobDetail job = newJob(AgendaServiceTrigger.class).withIdentity(\"AgendaServiceTrigger\").build();\n\t\t\tjob.getJobDataMap().put(\"box\", box);\n\t\t\tscheduler.scheduleJob(job, Set.of(newTrigger().withIdentity(\"AgendaServiceTrigger\").withSchedule(cronSchedule(\"0 0/1 * 1/1 * ? *\")).build()), true);\n\t\t} catch (SchedulerException e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n\n\tprivate void startService(AlexandriaSpark<?> server) {\n\t\t")).output(placeholder("future", "route").multiple("\n")).output(literal("\n\t}\n\n\t")).output(placeholder("future", "register").multiple("\n")).output(literal("\n\n\tprivate void loadFutures() {\n\t\t")).output(placeholder("future", "load").multiple("\n")).output(literal("\n\t}\n\n\t")).output(placeholder("future", "private").multiple("\n")).output(literal("\n\n\tprivate static String read(File file) {\n\t\ttry {\n\t\t\treturn Files.readString(file.toPath());\n\t\t} catch (IOException e) {\n\t\t\tLogger.error(e);\n\t\t\treturn \"\";\n\t\t}\n\t}\n\n\tprivate void write(Object schema, File file) {\n\t\ttry {\n\t\t\tFiles.writeString(file.toPath(), Json.toString(schema));\n\t\t} catch (IOException e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n\n\tpublic class Create {\n\t\t")).output(placeholder("future", "createMethod").multiple("\n\n")).output(literal("\n\n\t\t")).output(placeholder("future", "createClass").multiple("\n\n")).output(literal("\n\t}\n\n\tpublic class Remove {\n\t\t")).output(placeholder("future", "removeMethod").multiple("\n\n")).output(literal("\n\t}\n\n\tpublic static class Option {\n\t\tpublic String id = UUID.randomUUID().toString();\n\t}\n\n\tpublic static class AgendaServiceTrigger implements Job {\n\t\tpublic void execute(JobExecutionContext context) throws JobExecutionException {\n\t\t\t")).output(placeholder("box", "PascalCase")).output(literal("Box box = (")).output(placeholder("box", "PascalCase")).output(literal("Box) context.getMergedJobDataMap().get(\"box\");\n\t\t\tfinal Instant instant = Instant.now().truncatedTo(ChronoUnit.MINUTES);\n\t\t\ttimeouts.forEach((k, v) -> {\n\t\t\t\tif (instant.equals(v)) {\n\t\t\t\t\t")).output(placeholder("future", "timeout").multiple("\n")).output(literal("\n\t\t\t\t}\n\t\t\t});\n\t\t}\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("future"), trigger("class"))).output(literal("public class ")).output(placeholder("name", "firstUpperCase")).output(literal("Futures {\n\tpublic List<")).output(placeholder("name", "firstUpperCase")).output(literal("> all(")).output(placeholder("parameter", "predicateSignature").multiple(", ")).output(literal(") {\n\t\treturn ")).output(placeholder("name", "firstLowerCase")).output(literal("Files().stream()\n\t\t\t\t.filter(f -> ")).output(placeholder("parameter", "test").multiple(" && ")).output(literal(")\n\t\t\t\t.map(f -> load")).output(placeholder("name", "firstUpperCase")).output(literal("(f)).collect(Collectors.toList());\n\t}\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\t\treturn load")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("name", "firstLowerCase")).output(literal("File(")).output(placeholder("parameter", "names").multiple(", ")).output(literal("));\n\t}\n\n\t")).output(expression().output(placeholder("option", "execute").multiple("\n"))).output(literal("\n}")));
		rules.add(rule().condition(trigger("register")).output(literal("private void register")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("name", "firstUpperCase")).output(literal("Schema schema) {\n\tFile file = ")).output(placeholder("name", "firstLowerCase")).output(literal("File(")).output(placeholder("parameter", "schemaParameter").multiple(", ")).output(literal(");\n\tregister(schema, file);\n\twrite(schema, file);\n}\n\nprivate static void register(")).output(placeholder("name", "firstUpperCase")).output(literal("Schema schema, File file) {\n\t")).output(expression().output(placeholder("option", "putOption").multiple("\n"))).output(literal("\n\tsources.put(schema.timeout().id, file);\n\ttimeouts.put(\"")).output(placeholder("name", "firstUpperCase")).output(literal("#\" + schema.timeout().id, schema.timeout().on());\n}")));
		rules.add(rule().condition(trigger("load")).output(placeholder("name", "firstLowerCase")).output(literal("Files().forEach(file -> register(fromString(read(file), ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema.class), file));")));
		rules.add(rule().condition(trigger("route")).output(literal("server.route(BaseUri + Abstract")).output(placeholder("name", "firstUpperCase")).output(literal(".URI.Path + \":id\").post(manager -> execute")).output(placeholder("name", "firstUpperCase")).output(literal("(manager.fromPath(\"id\")));")));
		rules.add(rule().condition(trigger("ifoption")).output(literal("if (\"")).output(placeholder("name")).output(literal("\".equalsIgnoreCase(option)) {\n\tfuture.schema().")).output(placeholder("name", "firstLowerCase")).output(literal("(new ")).output(placeholder("future", "firstUpperCase")).output(literal("Schema.")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("optionParameter", "argIndex").multiple(", ")).output(literal("));\n\tfuture.")).output(placeholder("name", "firstLowerCase")).output(literal("();\n}")));
		rules.add(rule().condition(trigger("putoption")).output(literal("sources.put(schema.")).output(placeholder("name", "firstLowerCase")).output(literal("().id, file);")));
		rules.add(rule().condition(trigger("private")).output(literal("private void clean(")).output(placeholder("name", "firstUpperCase")).output(literal(" future) {\n\tfuture.uri().ids().forEach(i -> {\n\t\tfinal File file = sources.remove(i);\n\t\tif (file != null && file.exists()) file.delete();\n\t});\n\tString id = future.schema().timeout().id;\n\tfinal File file = sources.remove(id);\n\tif (file != null && file.exists()) file.delete();\n}\n\nprivate Collection<File> ")).output(placeholder("name", "firstLowerCase")).output(literal("Files() {\n\tFile directory = new File(root, \"")).output(placeholder("name", "camelCaseToKebabCase")).output(literal("/\");\n\tdirectory.mkdirs();\n\treturn FileUtils.listFiles(directory, new String[]{\"json\"}, true);\n}\n\nprivate File ")).output(placeholder("name", "firstLowerCase")).output(literal("File(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\tFile directory = new File(root, \"")).output(placeholder("name", "camelCaseToKebabCase")).output(literal("/\");\n\tdirectory.mkdirs();\n\treturn new File(directory, ")).output(placeholder("parameter", "names").multiple(" + \"##\" + ")).output(literal(" + \".json\");\n}\n\nprivate ")).output(placeholder("name", "firstUpperCase")).output(literal(" load")).output(placeholder("name", "firstUpperCase")).output(literal("(File file) {\n\tif (!file.exists()) return null;\n\tfinal ")).output(placeholder("name", "firstUpperCase")).output(literal(" future = new ")).output(placeholder("name", "firstUpperCase")).output(literal("(box);\n\tfuture.schema(fromString(read(file), ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema.class));\n\treturn future;\n}")));
		rules.add(rule().condition(all(allTypes("option"), trigger("execute"))).output(literal("public void ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("future", "FirstUpperCase")).output(literal(" ")).output(placeholder("future", "FirstLowerCase")).output(expression().output(literal(", ")).output(placeholder("optionParameter", "signature").multiple(", "))).output(literal(") {\n\texecute(")).output(placeholder("future", "FirstLowerCase")).output(literal(", \"")).output(placeholder("name", "firstLowerCase")).output(literal("\"")).output(expression().output(literal(", ")).output(placeholder("optionParameter", "names").multiple(", "))).output(literal(");\n}")));
		rules.add(rule().condition(all(allTypes("future"), trigger("execute"))).output(literal("private void execute")).output(placeholder("name", "firstUpperCase")).output(literal("(String id) {\n\tfinal File file = sources.get(id);\n\tif (file == null) return;\n\tfinal ")).output(placeholder("name", "firstUpperCase")).output(literal(" future = load")).output(placeholder("name", "firstUpperCase")).output(literal("(file);\n\tfinal String option = future.uri().option(id);\n\texecute(future, option);\n}\n\nprivate void execute(")).output(placeholder("name", "firstUpperCase")).output(literal(" future, String option, String... params) {\n\t")).output(expression().output(placeholder("option", "ifOption").multiple("\nelse "))).output(literal("\n\t")).output(expression().output(placeholder("hasOption", "else")).output(literal(" "))).output(literal("\n\tfuture.timeout();\n\tclean(future);\n}")));
		rules.add(rule().condition(trigger("else")).output(literal("else")));
		rules.add(rule().condition(trigger("getter")).output(literal("public ")).output(placeholder("name", "firstUpperCase")).output(literal("Futures ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\treturn new ")).output(placeholder("name", "firstUpperCase")).output(literal("Futures();\n}")));
		rules.add(rule().condition(trigger("removemethod")).output(literal("public boolean ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\tfinal File file = ")).output(placeholder("name", "firstLowerCase")).output(literal("File(")).output(placeholder("parameter", "names").multiple(", ")).output(literal(");\n\tfinal ")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal(" = AgendaService.this.load")).output(placeholder("name", "firstUpperCase")).output(literal("(file);\n\tif (")).output(placeholder("name", "firstLowerCase")).output(literal(" != null) timeouts.remove(\"")).output(placeholder("name", "firstUpperCase")).output(literal("#\" + ")).output(placeholder("name", "firstLowerCase")).output(literal(".schema().timeout().id);\n\tfor (String key : new ArrayList<>(sources.keySet()))\n\t\tif (sources.get(key).equals(file)) sources.remove(key);\n\tif (file.exists()) return file.delete();\n\treturn false;\n}")));
		rules.add(rule().condition(trigger("createmethod")).output(literal("public ")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\treturn new ")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("parameter", "names").multiple(", ")).output(literal(");\n}")));
		rules.add(rule().condition(trigger("timeout")).output(literal("if (k.startsWith(\"")).output(placeholder("name", "FirstUpperCase")).output(literal("#\")) box.agenda().execute")).output(placeholder("name", "FirstUpperCase")).output(literal("(k.split(\"#\")[1]);")));
		rules.add(rule().condition(trigger("predicatesignature")).output(literal("Predicate<")).output(placeholder("type")).output(literal("> ")).output(placeholder("name")));
		rules.add(rule().condition(trigger("signature")).output(placeholder("type")).output(literal(" ")).output(placeholder("name")));
		rules.add(rule().condition(trigger("names")).output(placeholder("name")));
		rules.add(rule().condition(trigger("argindex")).output(literal("params[")).output(placeholder("index")).output(literal("]")));
		rules.add(rule().condition(all(allTypes("Boolean"), trigger("test"))).output(placeholder("name")).output(literal(".test(Boolean.valueOf(f.getName().split(\"##\")[")).output(placeholder("index")).output(literal("]))")));
		rules.add(rule().condition(all(allTypes("Double"), trigger("test"))).output(placeholder("name")).output(literal(".test(Double.parseDouble(f.getName().split(\"##\")[")).output(placeholder("index")).output(literal("]))")));
		rules.add(rule().condition(all(allTypes("Integer"), trigger("test"))).output(placeholder("name")).output(literal(".test(Integer.parseInt(f.getName().split(\"##\")[")).output(placeholder("index")).output(literal("]))")));
		rules.add(rule().condition(trigger("test")).output(placeholder("name")).output(literal(".test(f.getName().split(\"##\")[")).output(placeholder("index")).output(literal("])")));
		rules.add(rule().condition(trigger("schemaparameter")).output(literal("schema.")).output(placeholder("name")).output(literal("()")));
		rules.add(rule().condition(trigger("fluid")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("name", "firstLowerCase")).output(literal(")")));
		rules.add(rule().condition(trigger("optioncreate")).output(literal("public ")).output(placeholder("future", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("optionParameter", "signature").multiple(", ")).output(literal(") {\n\tschema.")).output(placeholder("name", "firstLowerCase")).output(literal("(new ")).output(placeholder("future", "firstUpperCase")).output(literal("Schema.")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("optionParameter", "names").multiple(", ")).output(literal("));\n\treturn this;\n}")));
		rules.add(rule().condition(trigger("createclass")).output(literal("public class ")).output(placeholder("name", "firstUpperCase")).output(literal(" {\n\tprivate final ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema schema;\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\t\tschema = new ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema()")).output(expression().output(literal(".")).output(placeholder("parameter", "fluid").multiple("."))).output(literal(";\n\t}\n\n\t")).output(placeholder("option", "optionCreate").multiple("\n")).output(literal("\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal(" timeout(Instant timeout) {\n\t\tschema.timeout(new ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema.Timeout(timeout.truncatedTo(ChronoUnit.MINUTES)));\n\t\treturn this;\n\t}\n\n\tpublic void save() {\n\t\tregister")).output(placeholder("name", "firstUpperCase")).output(literal("(schema);\n\t}\n}")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		rules.add(rule().condition(all(allTypes("archetype"), trigger("customizedirectory"))).output(literal("new ")).output(placeholder("package")).output(literal(".Archetype(box.configuration().home()).")).output(placeholder("path")).output(literal(".root()")));
		rules.add(rule().condition(all(allTypes("custom"), trigger("customizedirectory"))).output(literal("new File(box.configuration().get(\"")).output(placeholder("path", "customParameter")).output(literal("\"))")));
		rules.add(rule().condition(trigger("customizedirectory")).output(literal("new java.io.File(\"")).output(placeholder("path", "customParameter")).output(literal("\")")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}