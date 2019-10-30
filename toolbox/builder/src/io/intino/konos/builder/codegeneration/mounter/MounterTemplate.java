package io.intino.konos.builder.codegeneration.mounter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MounterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((allTypes("mounter","population","src"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".mounters;\n\nimport io.intino.alexandria.mapp.MappStream.Item;\n\nimport java.util.function.Function;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal("MounterFunctions {\n\n\t")).output(mark("column", "method").multiple("\n\n")).output(literal("\n}")),
			rule().condition((allTypes("mounter","population"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".mounters;\n\nimport io.intino.alexandria.Timetag;\nimport io.intino.alexandria.logger.Logger;\nimport io.intino.alexandria.tabb.ColumnStream;\nimport io.intino.alexandria.tabb.TabbBuilder;\nimport io.intino.alexandria.tabb.streamers.MappColumnStreamer;\nimport io.intino.alexandria.tabb.streamers.MappColumnStreamer.SimpleSelector;\nimport ")).output(mark("package", "validPackage")).output(literal(".mounter.")).output(mark("name", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("MounterFunctions;\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\n\nimport java.util.function.Predicate;\nimport java.io.IOException;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tpublic ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\n\tpublic void pump(Timetag from, Timetag to, Predicate<Timetag> filter) {\n\t\tfrom.iterateTo(to).forEach(timetag -> {\n\t\t\tTabbBuilder tabbBuilder = new TabbBuilder();\n\t\t\t")).output(mark("column").multiple("\n")).output(literal("\n\t\t\ttry {\n\t\t\t\ttabbBuilder")).output(expression().output(mark("format"))).output(literal(".save(new java.io.File(box.")).output(mark("datamart", "firstLowerCase")).output(literal("datamart(), \"")).output(mark("name", "lowercase")).output(literal("-\" + timetag.value() + \".tabb\"));\n\t\t\t} catch (IOException e) {\n\t\t\t\tLogger.error(e);\n\t\t\t}\n\t\t});\n\t}\n}")),
			rule().condition((allTypes("mounter","realtime"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".mounters;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.message.Message;\nimport io.intino.alexandria.message.MessageCast;\nimport io.intino.alexandria.logger.Logger;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprivate ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\tpublic void handle(Message message) {\n\t\t")).output(expression().output(literal("try {")).output(literal("\n")).output(literal("\t")).output(mark("type", "typeClass")).output(literal(" object = MessageCast.cast(message).as(")).output(mark("type", "typeClass")).output(literal(".class);")).output(literal("\n")).output(literal("} catch (IllegalAccessException e) {")).output(literal("\n")).output(literal("\tLogger.error(e);")).output(literal("\n")).output(literal("}"))).output(literal("\n\t}\n}")),
			rule().condition((allTypes("mounter","batch"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".mounters;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.message.Message;\nimport io.intino.alexandria.zim.ZimStream;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\n\tprivate ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\t")).output(mark("tank", "field").multiple("\n")).output(literal("\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t\t")).output(mark("tank", "constructor").multiple("\n")).output(literal("\n\n\t}\n\n\tpublic void execute() {\n        ZimStream.Merge stream = new ZimStream.Merge(")).output(mark("tank", "content").multiple(", ")).output(literal(");\n        while(stream.hasNext()) {\n            process(stream.next());\n        }\n    }\n\n\tprivate void process(Message message) {\n\n    }\n}")),
			rule().condition((type("tank")), (trigger("field"))).output(literal("io.intino.alexandria.datalake.Datalake.EventStore.Tank ")).output(mark("name", "camelCase", "firstLowerCase")).output(literal(";")),
			rule().condition((type("tank")), (trigger("content"))).output(mark("name", "camelCase", "firstLowerCase")).output(literal(".content()")),
			rule().condition((type("tank")), (trigger("constructor"))).output(literal("this.")).output(mark("name", "camelCase", "firstLowerCase")).output(literal(" = box.datalake().eventStore().tank(t -> t.name().equals(\"")).output(mark("qn")).output(literal("\")).get();")),
			rule().condition((trigger("column"))).output(literal("new MappColumnStreamer(box.datalake().setStore().tank(\"")).output(mark("fullName")).output(literal("\").on(timetag).index()).add(new SimpleSelector(\"")).output(mark("name")).output(literal("\", ColumnStream.Type.")).output(mark("type", "FirstUpperCase")).output(literal(", ")).output(mark("mounter", "snakeCaseToCamelCase", "FirstUpperCase")).output(literal("MounterFunctions.")).output(mark("name", "firstLowerCase")).output(mark("facet")).output(literal("()));")),
			rule().condition((type("column")), (attribute("type", "Long")), (trigger("method"))).output(literal("public static Function<Item, ")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn Item::key;\n}")),
			rule().condition((type("column")), (attribute("type", "Double")), (trigger("method"))).output(literal("public static Function<Item, ")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn i -> ")).output(mark("type", "FirstUpperCase")).output(literal(".valueOf(i.value());\n}")),
			rule().condition((type("column")), (attribute("type", "Integer")), (trigger("method"))).output(literal("public static Function<Item, ")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn i -> Double.valueOf(i.value());\n}")),
			rule().condition((type("column")), (attribute("type", "Timetag")), (trigger("method"))).output(literal("public static Function<Item, io.intino.alexandria.Timetag> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn i -> io.intino.alexandria.Timetag.of(i.value());\n}")),
			rule().condition((type("column")), (attribute("type", "Boolean")), (trigger("method"))).output(literal("public static Function<Item, Boolean> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn i -> Boolean.parseBoolean(i.value());\n}")),
			rule().condition((type("column")), (attribute("type", "Nominal")), (trigger("method"))).output(literal("public static Function<Item, String> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn Item::value;\n}")),
			rule().condition((type("column")), (trigger("method"))).output(literal("public static Function<Item, ")).output(mark("type", "FirstUpperCase")).output(literal("> ")).output(mark("name", "validName", "firstLowerCase")).output(mark("facet")).output(literal("() {\n\treturn Item::value;\n}")),
			rule().condition((trigger("format"))).output(literal(".add(TabbBuilder.Format.")).output(mark("", "lowercase")).output(literal(")")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;")),
			rule().condition((attribute("message")), (trigger("typeclass"))).output(literal("io.intino.alexandria.message.Message")),
			rule().condition((type("schema")), (trigger("typeclass"))).output(mark("package")).output(literal(".schemas.")).output(mark("name", "FirstUpperCase")),
			rule().condition((type("schema")), (trigger("typename"))).output(mark("name", "firstLowerCase")),
			rule().condition((trigger("typename"))).output(literal("message"))
		);
	}
}