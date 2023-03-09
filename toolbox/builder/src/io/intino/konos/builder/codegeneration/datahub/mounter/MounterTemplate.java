package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class MounterTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("mounter"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("datamart", "validPackage", "lowercase")).output(literal(".mounters;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".")).output(mark("box", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.event.Event;\n\nimport ")).output(mark("package", "validPackage")).output(literal(".mounters.Mounter;\n\npublic class ")).output(mark("name", "FirstUpperCase")).output(literal(" implements Mounter {\n\tprivate final ")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(mark("name", "FirstUpperCase")).output(literal("(")).output(mark("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t")).output(mark("type", "method").multiple("\n\n")).output(literal("\n\n\tpublic void handle(Event event) {\n\t\t")).output(mark("type", "switch").multiple("\n\n")).output(literal("\n\t}\n}")),
			rule().condition((trigger("switch"))).output(literal("if\t(event.type().equals(\"")).output(mark("name")).output(literal("\")) handle((")).output(mark("fullType")).output(literal(") event);")),
			rule().condition((trigger("method"))).output(literal("public void handle(")).output(mark("fullType")).output(literal(" event) {\n\n}")),
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
			rule().condition((type("schema")), (trigger("typeclass"))).output(mark("package")).output(literal(".schemas.")).output(mark("name", "FirstUpperCase")),
			rule().condition((type("schema")), (trigger("typename"))).output(mark("name", "firstLowerCase")),
			rule().condition((trigger("typename"))).output(literal("message"))
		);
	}
}