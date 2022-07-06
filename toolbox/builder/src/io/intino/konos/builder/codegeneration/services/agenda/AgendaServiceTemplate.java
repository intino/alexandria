package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AgendaServiceTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("service"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box;\nimport ")).output(mark("package", "validPackage")).output(literal(".agenda.*;\nimport io.intino.alexandria.http.AlexandriaSpark;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.scheduler.AlexandriaScheduler;\nimport org.apache.commons.io.FileUtils;\nimport io.intino.alexandria.Json;\n\n")).output(mark("schemaImport")).output(literal("\nimport org.quartz.*;\n\nimport java.io.File;\nimport java.io.IOException;\nimport java.nio.file.Files;\nimport java.time.Instant;\nimport java.time.temporal.ChronoUnit;\nimport java.util.*;\nimport java.util.function.Predicate;\nimport java.util.stream.Collectors;\n\nimport static io.intino.alexandria.Json.fromString;\nimport static org.quartz.CronScheduleBuilder.cronSchedule;\nimport static org.quartz.JobBuilder.newJob;\nimport static org.quartz.TriggerBuilder.newTrigger;\n\n@SuppressWarnings(\"unchecked\")\npublic class AgendaService {\n\tpublic static final String BaseUri = \"")).output(mark("baseUri")).output(literal("/\";\n\tprivate static final Map<String, File> sources = new HashMap<>();\n\tprivate static final Map<String, Instant> timeouts = new HashMap<>();\n\tprivate final ")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box;\n\tprivate final File root;\n\n\tpublic AgendaService(")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\tthis.root = ")).output(mark("rootPath", "customizeDirectory")).output(literal(";\n\t}\n\n\t")).output(mark("future", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(mark("future", "class").multiple("\n\n")).output(literal("\n\n\t")).output(mark("future", "execute").multiple("\n\n")).output(literal("\n\n\tpublic AlexandriaSpark<?> setup(AlexandriaSpark<?> server, AlexandriaScheduler scheduler) {\n\t\tloadFutures();\n\t\tstartTimers(scheduler);\n\t\tstartService(server);\n\t\treturn server;\n\t}\n\n\tpublic Create create() {\n\t\treturn new Create();\n\t}\n\n\tpublic Remove remove() {\n\t\treturn new Remove();\n\t}\n\n\tprivate void startTimers(AlexandriaScheduler scheduler) {\n\t\ttry {\n\t\t\tJobDetail job = newJob(AgendaServiceTrigger.class).withIdentity(\"AgendaServiceTrigger\").build();\n\t\t\tjob.getJobDataMap().put(\"box\", box);\n\t\t\tscheduler.scheduleJob(job, Set.of(newTrigger().withIdentity(\"AgendaServiceTrigger\").withSchedule(cronSchedule(\"* * * 1/1 * ? *\")).build()), true);\n\t\t} catch (SchedulerException e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n\n\tprivate void startService(AlexandriaSpark<?> server) {\n\t\t")).output(mark("future", "route").multiple("\n")).output(literal("\n\t}\n\n\t")).output(mark("future", "register").multiple("\n")).output(literal("\n\n\tprivate void loadFutures() {\n\t\t")).output(mark("future", "load").multiple("\n")).output(literal("\n\t}\n\n\t")).output(mark("future", "private").multiple("\n")).output(literal("\n\n\tprivate static String read(File file) {\n\t\ttry {\n\t\t\treturn Files.readString(file.toPath());\n\t\t} catch (IOException e) {\n\t\t\tLogger.error(e);\n\t\t\treturn \"\";\n\t\t}\n\t}\n\n\tprivate void write(Object schema, File file) {\n\t\ttry {\n\t\t\tFiles.writeString(file.toPath(), Json.toString(schema));\n\t\t} catch (IOException e) {\n\t\t\tLogger.error(e);\n\t\t}\n\t}\n\n\tpublic class Create {\n\t\t")).output(mark("future", "createMethod").multiple("\n\n")).output(literal("\n\n\t\t")).output(mark("future", "createClass").multiple("\n\n")).output(literal("\n\t}\n\n\tpublic class Remove {\n\t\t")).output(mark("future", "removeMethod").multiple("\n\n")).output(literal("\n\t}\n\n\tpublic static class Option {\n\t\tpublic String id = UUID.randomUUID().toString();\n\t}\n\n\tpublic static class AgendaServiceTrigger implements Job {\n\t\tpublic void execute(JobExecutionContext context) throws JobExecutionException {\n\t\t\t")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box box = (")).output(mark("box", "snakecaseToCamelCase", "firstUpperCase")).output(literal("Box) context.getMergedJobDataMap().get(\"box\");\n\t\t\tfinal Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);\n\t\t\ttimeouts.forEach((k, v) -> {\n\t\t\t\tif (instant.equals(v)) {\n\t\t\t\t\t")).output(mark("future", "timeout").multiple("\n")).output(literal("\n\t\t\t\t}\n\t\t\t});\n\t\t}\n\t}\n}")),
			rule().condition((type("future")), (trigger("class"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal("Futures {\n\tpublic List<")).output(mark("name", "firstUpperCase")).output(literal("> all(")).output(mark("parameter", "predicateSignature").multiple(", ")).output(literal(") {\n\t\treturn ")).output(mark("name", "firstLowerCase")).output(literal("Files().stream()\n\t\t\t\t.filter(f -> ")).output(mark("parameter", "test").multiple(" && ")).output(literal(")\n\t\t\t\t.map(f -> load")).output(mark("name", "firstUpperCase")).output(literal("(f)).collect(Collectors.toList());\n\t}\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\t\treturn load")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("name", "firstLowerCase")).output(literal("File(")).output(mark("parameter", "names").multiple(", ")).output(literal("));\n\t}\n\n\t")).output(expression().output(mark("option", "execute").multiple("\n"))).output(literal("\n}")),
			rule().condition((trigger("register"))).output(literal("private void register")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("name", "firstUpperCase")).output(literal("Schema schema) {\n\tFile file = ")).output(mark("name", "firstLowerCase")).output(literal("File(")).output(mark("parameter", "schemaParameter").multiple(", ")).output(literal(");\n\tregister(schema, file);\n\twrite(schema, file);\n}\n\nprivate static void register(")).output(mark("name", "firstUpperCase")).output(literal("Schema schema, File file) {\n\t")).output(mark("option", "putOption").multiple("\n")).output(literal("\n\ttimeouts.put(\"")).output(mark("name", "firstUpperCase")).output(literal("#\" + schema.timeout().id, schema.timeout().on());\n}")),
			rule().condition((trigger("load"))).output(mark("name", "firstLowerCase")).output(literal("Files().forEach(file -> register(fromString(read(file), ")).output(mark("name", "firstUpperCase")).output(literal("Schema.class), file));")),
			rule().condition((trigger("route"))).output(literal("server.route(BaseUri + Abstract")).output(mark("name", "firstUpperCase")).output(literal(".URI.Path + \":id\").post(manager -> execute")).output(mark("name", "firstUpperCase")).output(literal("(manager.fromPath(\"id\")));")),
			rule().condition((trigger("ifoption"))).output(literal("if (\"")).output(mark("name")).output(literal("\".equalsIgnoreCase(option)) {\n\tfuture.schema().")).output(mark("name", "firstLowerCase")).output(literal("(new ")).output(mark("future", "firstUpperCase")).output(literal("Schema.")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("optionParameter", "argIndex").multiple(", ")).output(literal("));\n\tfuture.")).output(mark("name", "firstLowerCase")).output(literal("();\n}")),
			rule().condition((trigger("putoption"))).output(literal("sources.put(schema.")).output(mark("name", "firstLowerCase")).output(literal("().id, file);")),
			rule().condition((trigger("private"))).output(literal("private void clean(")).output(mark("name", "firstUpperCase")).output(literal(" future) {\n\tfuture.uri().ids().forEach(i -> {\n\t\tfinal File file = sources.remove(i);\n\t\tif (file.exists()) file.delete();\n\t});\n}\n\nprivate Collection<File> ")).output(mark("name", "firstLowerCase")).output(literal("Files() {\n\tFile directory = new File(root, \"")).output(mark("name", "camelCaseToSnakeCase")).output(literal("/\");\n\tdirectory.mkdirs();\n\treturn FileUtils.listFiles(directory, new String[]{\"json\"}, true);\n}\n\nprivate File ")).output(mark("name", "firstLowerCase")).output(literal("File(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\treturn new File(root, \"")).output(mark("name", "camelCaseToSnakeCase")).output(literal("/\" + ")).output(mark("parameter", "names").multiple(" + \".\" + ")).output(literal(" + \".json\");\n}\n\nprivate ")).output(mark("name", "firstUpperCase")).output(literal(" load")).output(mark("name", "firstUpperCase")).output(literal("(File file) {\n\tif (!file.exists()) return null;\n\tfinal ")).output(mark("name", "firstUpperCase")).output(literal(" future = new ")).output(mark("name", "firstUpperCase")).output(literal("(box);\n\tfuture.schema(fromString(read(file), ")).output(mark("name", "firstUpperCase")).output(literal("Schema.class));\n\treturn future;\n}")),
			rule().condition((type("option")), (trigger("execute"))).output(literal("public void ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("future", "FirstUpperCase")).output(literal(" ")).output(mark("future", "FirstLowerCase")).output(expression().output(literal(", ")).output(mark("optionParameter", "signature").multiple(", "))).output(literal(") {\n\texecute(")).output(mark("future", "FirstLowerCase")).output(literal(", \"")).output(mark("name", "firstLowerCase")).output(literal("\"")).output(expression().output(literal(", ")).output(mark("optionParameter", "names").multiple(", "))).output(literal(");\n}")),
			rule().condition((type("future")), (trigger("execute"))).output(literal("private void execute")).output(mark("name", "firstUpperCase")).output(literal("(String id) {\n\tfinal File file = sources.get(id);\n\tif (file == null) return;\n\tfinal ")).output(mark("name", "firstUpperCase")).output(literal(" future = load")).output(mark("name", "firstUpperCase")).output(literal("(file);\n\tfinal String option = future.uri().option(id);\n\tif (option != null) execute(future, option);\n}\n\nprivate void execute(")).output(mark("name", "firstUpperCase")).output(literal(" future, String option, String... params) {\n\t")).output(mark("option", "ifOption").multiple("\nelse ")).output(literal("\n\tclean(future);\n}")),
			rule().condition((trigger("getter"))).output(literal("public ")).output(mark("name", "firstUpperCase")).output(literal("Futures ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn new ")).output(mark("name", "firstUpperCase")).output(literal("Futures();\n}")),
			rule().condition((trigger("removemethod"))).output(literal("public boolean ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\tfinal File file = ")).output(mark("name", "firstLowerCase")).output(literal("File(")).output(mark("parameter", "names").multiple(", ")).output(literal(");\n\tfinal ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal(" = AgendaService.this.load")).output(mark("name", "firstUpperCase")).output(literal("(file);\n\tif (")).output(mark("name", "firstLowerCase")).output(literal(" != null) timeouts.remove(\"")).output(mark("name", "firstUpperCase")).output(literal("#\" + ")).output(mark("name", "firstLowerCase")).output(literal(".schema().timeout().id);\n\tfor (String key : new ArrayList<>(sources.keySet()))\n\t\tif (sources.get(key).equals(file)) sources.remove(key);\n\tif (file.exists()) return file.delete();\n\treturn false;\n}")),
			rule().condition((trigger("createmethod"))).output(literal("public ")).output(mark("name", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\treturn new ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("parameter", "names").multiple(", ")).output(literal(");\n}")),
			rule().condition((trigger("timeout"))).output(literal("if (k.startsWith(\"")).output(mark("name", "FirstUpperCase")).output(literal("#\")) box.agenda().execute")).output(mark("name", "FirstUpperCase")).output(literal("(k.split(\"#\")[1]);")),
			rule().condition((trigger("predicatesignature"))).output(literal("Predicate<")).output(mark("type")).output(literal("> ")).output(mark("name")),
			rule().condition((trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name")),
			rule().condition((trigger("names"))).output(mark("name")),
			rule().condition((trigger("argindex"))).output(literal("params[")).output(mark("index")).output(literal("]")),
			rule().condition((trigger("test"))).output(mark("name")).output(literal(".test(f.getName().split(\"\\\\.\")[")).output(mark("index")).output(literal("])")),
			rule().condition((trigger("schemaparameter"))).output(literal("schema.")).output(mark("name")).output(literal("()")),
			rule().condition((trigger("fluid"))).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("name", "firstLowerCase")).output(literal(")")),
			rule().condition((trigger("optioncreate"))).output(literal("public ")).output(mark("future", "firstUpperCase")).output(literal(" ")).output(mark("name", "firstLowerCase")).output(literal("(")).output(mark("optionParameter", "signature").multiple(", ")).output(literal(") {\n\tschema.")).output(mark("name", "firstLowerCase")).output(literal("(new ")).output(mark("future", "firstUpperCase")).output(literal("Schema.")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("optionParameter", "names").multiple(", ")).output(literal("));\n\treturn this;\n}")),
			rule().condition((trigger("createclass"))).output(literal("public class ")).output(mark("name", "firstUpperCase")).output(literal(" {\n\tprivate final ")).output(mark("name", "firstUpperCase")).output(literal("Schema schema;\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\t\tschema = new ")).output(mark("name", "firstUpperCase")).output(literal("Schema()")).output(expression().output(literal(".")).output(mark("parameter", "fluid").multiple("."))).output(literal(";\n\t}\n\n\t")).output(mark("option", "optionCreate").multiple("\n")).output(literal("\n\n\tpublic ")).output(mark("name", "firstUpperCase")).output(literal(" timeout(Instant timeout) {\n\t\tschema.timeout(new ")).output(mark("name", "firstUpperCase")).output(literal("Schema.Timeout(timeout.truncatedTo(ChronoUnit.SECONDS)));\n\t\treturn this;\n\t}\n\n\tpublic void save() {\n\t\tregister")).output(mark("name", "firstUpperCase")).output(literal("(schema);\n\t}\n}")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((type("archetype")), (trigger("customizedirectory"))).output(literal("new ")).output(mark("package")).output(literal(".Archetype(box.configuration().home()).")).output(mark("path")).output(literal(".root()")),
			rule().condition((trigger("customizedirectory"))).output(literal("new java.io.File(\"")).output(mark("path")).output(literal("\")"))
		);
	}
}