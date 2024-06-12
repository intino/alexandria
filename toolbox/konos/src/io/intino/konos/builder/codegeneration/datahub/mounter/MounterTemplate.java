package io.intino.konos.builder.codegeneration.datahub.mounter;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class MounterTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("mounter")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("datamart", "validPackage", "lowercase")).output(literal(".mounters;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".")).output(placeholder("box", "firstUpperCase")).output(literal("Box;\nimport io.intino.alexandria.event.Event;\n\nimport ")).output(placeholder("package", "validPackage")).output(literal(".mounters.Mounter;\n\npublic class ")).output(placeholder("name", "FirstUpperCase")).output(literal(" implements Mounter {\n\tprivate final ")).output(placeholder("box", "validName", "firstUpperCase")).output(literal("Box box;\n\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("(")).output(placeholder("box", "validName", "firstUpperCase")).output(literal("Box box) {\n\t\tthis.box = box;\n\t}\n\n\t")).output(placeholder("type", "method").multiple("\n\n")).output(literal("\n\n\tpublic void handle(Event event) {\n\t\t")).output(placeholder("type", "switch").multiple("\nelse ")).output(literal("\n\t}\n}")));
		rules.add(rule().condition(trigger("switch")).output(literal("if (event.type().equals(\"")).output(placeholder("name")).output(literal("\")) handle((")).output(placeholder("fullType")).output(literal(") event);")));
		rules.add(rule().condition(trigger("method")).output(literal("public void handle(")).output(placeholder("fullType")).output(literal(" event) {\n\n}")));
		rules.add(rule().condition(all(allTypes("tank"), trigger("field"))).output(literal("io.intino.alexandria.datalake.Datalake.EventStore.Tank ")).output(placeholder("name", "CamelCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("tank"), trigger("content"))).output(placeholder("name", "CamelCase")).output(literal(".content()")));
		rules.add(rule().condition(all(allTypes("tank"), trigger("constructor"))).output(literal("this.")).output(placeholder("name", "CamelCase")).output(literal(" = box.datalake().eventStore().tank(t -> t.name().equals(\"")).output(placeholder("qn")).output(literal("\")).get();")));
		rules.add(rule().condition(trigger("column")).output(literal("new MappColumnStreamer(box.datalake().setStore().tank(\"")).output(placeholder("fullName")).output(literal("\").on(timetag).index()).add(new SimpleSelector(\"")).output(placeholder("name")).output(literal("\", ColumnStream.Type.")).output(placeholder("type", "FirstUpperCase")).output(literal(", ")).output(placeholder("mounter", "PascalCase")).output(literal("MounterFunctions.")).output(placeholder("name", "firstLowerCase")).output(placeholder("facet")).output(literal("()));")));
		rules.add(rule().condition(all(all(allTypes("column"), attribute("type","Long")), trigger("method"))).output(literal("public static Function<Item, ")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn Item::key;\n}")));
		rules.add(rule().condition(all(all(allTypes("column"), attribute("type","Double")), trigger("method"))).output(literal("public static Function<Item, ")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn i -> ")).output(placeholder("type", "FirstUpperCase")).output(literal(".valueOf(i.value());\n}")));
		rules.add(rule().condition(all(all(allTypes("column"), attribute("type","Integer")), trigger("method"))).output(literal("public static Function<Item, ")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn i -> Double.valueOf(i.value());\n}")));
		rules.add(rule().condition(all(all(allTypes("column"), attribute("type","Timetag")), trigger("method"))).output(literal("public static Function<Item, io.intino.alexandria.Timetag> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn i -> io.intino.alexandria.Timetag.of(i.value());\n}")));
		rules.add(rule().condition(all(all(allTypes("column"), attribute("type","Boolean")), trigger("method"))).output(literal("public static Function<Item, Boolean> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn i -> Boolean.parseBoolean(i.value());\n}")));
		rules.add(rule().condition(all(all(allTypes("column"), attribute("type","Nominal")), trigger("method"))).output(literal("public static Function<Item, String> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn Item::value;\n}")));
		rules.add(rule().condition(all(allTypes("column"), trigger("method"))).output(literal("public static Function<Item, ")).output(placeholder("type", "FirstUpperCase")).output(literal("> ")).output(placeholder("name", "validName", "firstLowerCase")).output(placeholder("facet")).output(literal("() {\n\treturn Item::value;\n}")));
		rules.add(rule().condition(trigger("format")).output(literal(".add(TabbBuilder.Format.")).output(placeholder("", "lowercase")).output(literal(")")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		rules.add(rule().condition(all(allTypes("schema"), trigger("typeclass"))).output(placeholder("package")).output(literal(".schemas.")).output(placeholder("name", "FirstUpperCase")));
		rules.add(rule().condition(all(allTypes("schema"), trigger("typename"))).output(placeholder("name", "firstLowerCase")));
		rules.add(rule().condition(trigger("typename")).output(literal("message")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}