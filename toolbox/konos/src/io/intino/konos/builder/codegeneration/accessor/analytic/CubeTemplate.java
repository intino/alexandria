package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.*;

public class CubeTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("cube")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".analytic.cubes.schemas;\n\nimport io.intino.alexandria.led.HeapLedStreamBuilder;\nimport io.intino.alexandria.led.Schema;\nimport io.intino.alexandria.led.LedReader;\nimport io.intino.alexandria.led.LedWriter;\nimport io.intino.alexandria.led.LedStream;\nimport io.intino.alexandria.led.Led;\nimport io.intino.alexandria.led.allocators.SchemaFactory;\nimport io.intino.alexandria.led.buffers.store.ByteStore;\nimport io.intino.alexandria.led.buffers.store.ByteBufferStore;\nimport io.intino.alexandria.Timetag;\n\nimport java.util.stream.*;\nimport java.util.List;\nimport java.util.Arrays;\nimport java.util.Comparator;\nimport java.io.File;\nimport java.util.UUID;\nimport ")).output(placeholder("package", "validPackage")).output(literal(".analytic.axes.*;\n\npublic class ")).output(placeholder("name")).output(literal(" extends Schema {\n\n\tpublic static final int SIZE = ")).output(placeholder("size")).output(literal("; // Bytes\n\tpublic static final UUID SERIAL_UUID = UUID.fromString(\"")).output(placeholder("serialUUID")).output(literal("\");\n\tpublic static final SchemaFactory<")).output(placeholder("name")).output(literal("> FACTORY = new SchemaFactory<>(")).output(placeholder("name")).output(literal(".class) {\n\t\t@Override\n\t\tpublic ")).output(placeholder("name")).output(literal(" newInstance(ByteStore store) {\n\t\t\treturn new ")).output(placeholder("name")).output(literal("(store);\n\t\t}\n\t};\n\n\tpublic ")).output(placeholder("name")).output(literal("() {\n\t\tsuper(new ByteBufferStore(SIZE));\n\t}\n\n\tpublic ")).output(placeholder("name")).output(literal("(ByteStore store) {\n\t\tsuper(store);\n\t}\n\n\t@Override\n\tpublic long id() {\n\t\treturn bitBuffer.getAlignedLong(0);\n\t}\n\n\t@Override\n\tpublic int size() {\n\t\treturn SIZE;\n\t}\n\n\t@Override\n\tpublic UUID serialUUID() {\n\t\treturn SERIAL_UUID;\n\t}\n\n\t")).output(expression().output(placeholder("column", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(placeholder("column", "setter").multiple("\n\n"))).output(literal("\n\n\t@Override\n\tpublic String toString() {\n\t\treturn \"")).output(placeholder("name", "FirstUpperCase")).output(literal("{\"\n\t\t\t\t+ \"id=\" + id()\n\t\t\t\t")).output(expression().output(placeholder("column", "toString").multiple("\n"))).output(literal("\n\t\t\t\t+ '}';\n\t}\n\n\t")).output(expression().output(placeholder("split"))).output(literal("\n\n\n\tpublic static class Builder extends HeapLedStreamBuilder<")).output(placeholder("name")).output(literal("> {\n\n\t\tpublic Builder() {\n\t\t\tthis(1_000_000);\n\t\t}\n\n\t\tpublic Builder(File tempDirectory) {\n\t\t\tthis(1_000_000, tempDirectory);\n\t\t}\n\n\t\tpublic Builder(int numSchemasPerBlock) {\n\t\t\tthis(numSchemasPerBlock, new File(System.getProperty(\"java.io.tmpdir\")));\n\t\t}\n\n\t\tpublic Builder(int numSchemasPerBlock, File tempDirectory) {\n\t\t\tsuper(")).output(placeholder("name")).output(literal(".class, numSchemasPerBlock, tempDirectory);\n\t\t}\n\t}\n\n\tpublic static class Tank {\n\n\t\tpublic static File get(File root) {\n\t\t\treturn new File(root, \"")).output(placeholder("cube")).output(literal("\");\n\t\t}\n\n\t\tpublic static File get(File root, Divisiones.Component division) {\n\t\t\treturn new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id());\n\t\t}\n\n\t\tpublic static File on(File root, Timetag timetag) {\n\t\t\treturn new File(root, \"")).output(placeholder("cube")).output(literal("/\" + timetag + \".led\");\n\t\t}\n\n\t\tpublic static File on(File root, Divisiones.Component division, Timetag timetag) {\n\t\t\treturn new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id() + \"/\" + timetag + \".led\");\n\t\t}\n\t}\n\n\tpublic static class Reader {\n\n\t\tpublic static List<LedStream<")).output(placeholder("name")).output(literal(">> readAll(File root, Divisiones.Component division) {\n\t\t\tFile[] leds = getLedFiles(new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id()));\n\t\t\treturn Arrays.stream(leds).map(Reader::openLedStream).collect(Collectors.toList());\n\t\t}\n\n\t\tpublic static List<LedStream<")).output(placeholder("name")).output(literal(">> read(File root, Divisiones.Component division, Timetag from, Timetag to) {\n\t\t\tFile[] leds = getLedFiles(new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id()));\n\t\t\treturn Arrays.stream(leds)\n\t\t\t\t.filter(led -> {\n\t\t\t\t\tfinal String name = led.getName();\n\t\t\t\t\tfinal Timetag timetag = Timetag.of(name.substring(0, name.indexOf('.')));\n\t\t\t\t\treturn (timetag.equals(from) || timetag.isAfter(from)) && (timetag.equals(to) || timetag.isBefore(to));\n\t\t\t\t})\n\t\t\t\t.map(Reader::openLedStream)\n\t\t\t\t.collect(Collectors.toList());\n\t\t}\n\n\t\tpublic static LedStream<")).output(placeholder("name")).output(literal("> read(File root, Divisiones.Component division, Timetag timetag) {\n\t\t\tFile[] leds = getLedFiles(new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id()));\n\t\t\treturn Arrays.stream(leds)\n\t\t\t\t.filter(led -> {\n\t\t\t\t\tfinal String name = led.getName();\n\t\t\t\t\tfinal Timetag t = Timetag.of(name.substring(0, name.indexOf('.')));\n\t\t\t\t\treturn t.equals(timetag);\n\t\t\t\t})\n\t\t\t\t.map(Reader::openLedStream)\n\t\t\t\t.findFirst().orElse(null);\n\t\t}\n\n\t\tpublic static LedStream<")).output(placeholder("name")).output(literal("> readFirst(File root, Divisiones.Component division) {\n\t\t\tFile[] leds = getLedFiles(new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id()));\n\t\t\tif(leds.length == 0) return LedStream.empty(")).output(placeholder("name")).output(literal(".class);\n\t\t\treturn openLedStream(leds[0]);\n\t\t}\n\n\t\tpublic static LedStream<")).output(placeholder("name")).output(literal("> readLast(File root, Divisiones.Component division) {\n\t\t\tFile[] leds = getLedFiles(new File(root, \"")).output(placeholder("cube")).output(literal(".\" + division.id()));\n\t\t\tif(leds.length == 0) return LedStream.empty(")).output(placeholder("name")).output(literal(".class);\n\t\t\treturn openLedStream(leds[leds.length - 1]);\n\t\t}\n\n\t\tpublic static LedStream<")).output(placeholder("name")).output(literal("> readLedStream(File ledFile) {\n\t\t\treturn new LedReader(ledFile).read(")).output(placeholder("name")).output(literal(".class);\n\t\t}\n\n\t\tpublic static Led<")).output(placeholder("name")).output(literal("> readLed(File ledFile) {\n\t\t\treturn new LedReader(ledFile).readAll(")).output(placeholder("name")).output(literal(".class);\n\t\t}\n\n\t\tprivate static LedStream<")).output(placeholder("name")).output(literal("> openLedStream(File led) {\n\t\t\tif(led == null || !led.exists()) return LedStream.empty(")).output(placeholder("name")).output(literal(".class);\n\t\t\treturn new LedReader(led).read(")).output(placeholder("name")).output(literal(".class);\n\t\t}\n\n\t\tprivate static File[] getLedFiles(File dir) {\n\t\t\tFile[] leds = dir.listFiles(f -> f.getName().endsWith(\".led\"));\n\t\t\tif(leds == null || leds.length == 0) return new File[0];\n\t\t\tArrays.sort(leds, Comparator.comparing(File::getName));\n\t\t\treturn leds;\n\t\t}\n\t}\n\n\tpublic static class Writer {\n\n\t\tpublic static void write(File file, LedStream.Builder<")).output(placeholder("name")).output(literal("> builder) {\n\t\t\twrite(file, builder.build());\n\t\t}\n\n\t\tpublic static void write(File file, LedStream<")).output(placeholder("name")).output(literal("> ledStream) {\n\t\t\tnew LedWriter(file).write(ledStream);\n\t\t}\n\n\t\tpublic static void write(File file, Led<")).output(placeholder("name")).output(literal("> led) {\n\t\t\tnew LedWriter(file).write(led);\n\t\t}\n\t}\n}")));
		rules.add(rule().condition(trigger("axisimport")).output(literal("import ")).output(placeholder("")).output(literal(".axis.*;")));
		rules.add(rule().condition(trigger("split")).output(literal("public enum Split {\n\t")).output(placeholder("enum", "asEnum").multiple(", ")).output(literal(";\n\n\tpublic abstract String qn();\n\n\tpublic static Split splitByQn(String qn) {\n\t\tfor(Split split : values()) {\n\t\t\tif(split.qn().equals(qn)) return split;\n\t\t}\n\t\treturn null;\n\t}\n}")));
		rules.add(rule().condition(trigger("asenum")).output(placeholder("value", "CamelCase")).output(literal(" {\n\tpublic String qn() {\n\t\treturn \"")).output(placeholder("qn")).output(literal("\";\n\t}\n}")));
		rules.add(rule().condition(trigger("nbits")).output(literal("NBits")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}