package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AnalyticBuilderTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("builder"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".analytic;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".analytic.cubes.schemas.*;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.Fingerprint;\nimport io.intino.alexandria.led.*;\nimport io.intino.alexandria.led.util.LedSessionSorter;\n\nimport java.util.function.Consumer;\nimport java.util.function.Function;\nimport java.io.File;\nimport java.util.Objects;\n\nimport org.apache.commons.io.FileUtils;\nimport java.util.List;\n\npublic class ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal(" {\n\n\tprivate static final int DEFAULT_BUFFER_SIZE = 1_000_000;\n\n\tprivate final File stage;\n\tprivate Session session;\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(File stage) {\n\t\tthis(stage, DEFAULT_BUFFER_SIZE);\n\t}\n\n\tpublic ")).output(mark("name", "snakeCaseToCamelCase", "firstUpperCase")).output(literal("(File stage, int factBufferSize) {\n\t\tthis.stage = stage;\n\t\tthis.session = new Session(stage, factBufferSize);\n\t}\n\n\t")).output(mark("cube", "put").multiple("\n\n")).output(literal("\n\n\tpublic void flush() {\n\t\tsession.flush();\n\t}\n\n\tpublic void close() {\n\t\tsession.close();\n\t}\n\n\tprivate static class Session implements AutoCloseable {\n\n\t\tprivate final File root;\n\t\tprivate final java.util.Map<Fingerprint, UnsortedLedStreamBuilder<? extends Schema>> builders;\n\t\tprivate final int ledBufferSize;\n\n\t\tpublic Session(File root, int ledBufferSize) {\n\t\t\tthis.root = root;\n\t\t\tthis.ledBufferSize = ledBufferSize;\n\t\t\tthis.builders = new java.util.HashMap<>();\n\t\t}\n\n\t\tpublic <T extends Schema> void put(String tank, Timetag timetag, Class<T> schemaClass, java.util.stream.Stream<Consumer<T>> stream) {\n\t\t\tLedStream.Builder<T> builder = this.builder(Fingerprint.of(tank, timetag), schemaClass);\n\t\t\tObjects.requireNonNull(builder);\n\t\t\tstream.forEach(builder::append);\n\t\t}\n\n\t\tpublic <T extends Schema> void put(String tank, Timetag timetag, Class<T> schemaClass, Consumer<T> transaction) {\n\t\t\tLedStream.Builder<T> builder = this.builder(Fingerprint.of(tank, timetag), schemaClass);\n\t\t\tbuilder.append(transaction);\n\t\t}\n\n\t\tpublic void flush() {\n\t\t\tthis.builders.forEach((f, b) -> b.flush());\n\t\t}\n\n\t\t@Override\n\t\tpublic void close() {\n\t\t\tthis.builders.forEach((f, b) -> b.close());\n\t\t}\n\n\t\tprivate <T extends Schema> LedStream.Builder<T> builder(Fingerprint fingerprint, Class<T> schemaClass) {\n\t\t\tif (!this.builders.containsKey(fingerprint)) {\n\t\t\t\tthis.builders.put(fingerprint, new UnsortedLedStreamBuilder(schemaClass, Schema.factoryOf(schemaClass),\n\t\t\t\t\tthis.ledBufferSize, fileOf(fingerprint)));\n\t\t\t}\n\t\t\treturn (io.intino.alexandria.led.LedStream.Builder) this.builders.get(fingerprint);\n\t\t}\n\n\t\tprivate java.io.File fileOf(Fingerprint fingerprint) {\n\t\t\t return new java.io.File(root, fingerprint.name() + \".led.session\");\n\t\t}\n\t}\n\n\tpublic static class Sealer {\n\n\t\tpublic static void seal(File destination, File stage) {\n\t\t\tFile tempFolder = tempFolder(stage);\n\t\t\tFile[] files = FileUtils.listFiles(stage, new String[]{\"session\"}, true).toArray(new File[0]);\n\t\t\tLedSessionSorter.sort(files, (Function<File, File>) f -> datamartFile(destination, cleanedNameOf(f)), tempFolder);\n\t\t}\n\n\t\tprivate static File datamartFile(File destination, String name) {\n\t\t\tFile ledFile = new File(destination, name + \".led\");\n\t\t\tledFile.getParentFile().mkdirs();\n\t\t\treturn ledFile;\n\t\t}\n\n\t\tprivate static File tempFolder(File stage) {\n\t\t\tFile temp = new File(stage, \"temp\");\n\t\t\ttemp.mkdir();\n\t\t\treturn temp;\n\t\t}\n\n\t\tprivate static String cleanedNameOf(File file) {\n\t\t\tfinal String name = file.getName();\n\t\t\tint to = file.getName().indexOf(\"#\");\n\t\t\tto = to < 0 ? name.length() : to;\n\t\t\treturn name.substring(0, to).replace(\"-\", \"/\").replace(\".led.session\", \"\");\n\t\t}\n\t}\n}")),
			rule().condition((trigger("put"))).output(literal("public void put")).output(mark("name", "FirstUpperCase")).output(literal("(String tank, Timetag timetag, java.util.function.Consumer<")).output(mark("name", "FirstUpperCase")).output(literal("Schema> fact) {\n\tsession.put(tank, timetag, ")).output(mark("name", "FirstUpperCase")).output(literal("Schema.class, fact);\n}"))
		);
	}
}