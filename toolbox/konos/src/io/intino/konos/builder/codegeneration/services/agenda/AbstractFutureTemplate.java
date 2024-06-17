package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.allTypes;
import static io.intino.itrules.template.condition.predicates.Predicates.trigger;
import static io.intino.itrules.template.outputs.Outputs.*;

public class AbstractFutureTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("future")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".agenda;\n\n")).output(placeholder("schemaImport")).output(literal("\nimport io.intino.alexandria.logger.Logger;\n\nimport java.util.List;\nimport java.util.Map;\n\nimport static  ")).output(placeholder("package", "validPackage")).output(literal(".AgendaService.BaseUri;\n\npublic abstract class Abstract")).output(placeholder("name", "FirstUpperCase")).output(literal(" {\n\tprotected ")).output(placeholder("name", "FirstUpperCase")).output(literal("Schema schema;\n\n\tpublic void schema(")).output(placeholder("name", "FirstUpperCase")).output(literal("Schema schema) {\n\t\tthis.schema = schema;\n\t}\n\n\tpublic ")).output(placeholder("name", "FirstUpperCase")).output(literal("Schema schema() {\n\t\treturn this.schema;\n\t}\n\n\t")).output(placeholder("option", "methods").multiple("\n\n")).output(literal("\n\n\tpublic void timeout() {\n\t\tonTimeout(")).output(placeholder("parameter", "call").multiple(",")).output(literal(");\n\t}\n\n\tprotected abstract void onTimeout(")).output(placeholder("parameter", "signature").multiple(",")).output(literal(");\n\n\t")).output(placeholder("parameter", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(placeholder("option", "optionGetter").multiple("\n\n")).output(literal("\n\n\tpublic URI uri() {\n\t\tif (schema == null) Logger.error(\"Future is not loaded\");\n\t\treturn new URI();\n\t}\n\n\tpublic class URI {\n\t\tpublic static final String Path = \"")).output(placeholder("name", "camelCaseToKebabCase")).output(literal("/\";\n\n\t\tpublic List<String> ids() {\n\t\t\treturn List.of(")).output(placeholder("option", "id").multiple(", ")).output(literal(");\n\t\t}\n\n\t\tpublic String option(String id) {\n\t\t\t")).output(placeholder("option", "getterURI").multiple("\n")).output(literal("\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic Map<String, String> options() {\n\t\t\treturn Map.of(")).output(placeholder("option", "map").multiple(",")).output(literal(");\n\t\t}\n\n\t\t")).output(placeholder("option", "optionGetterURI").multiple("\n")).output(literal("\n\t}\n}")));
		rules.add(rule().condition(trigger("methods")).output(literal("public void ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\ton")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("parameter", "call").multiple(", ")).output(expression().output(literal(", ")).output(placeholder("optionParameter", "optionCall").multiple(", "))).output(literal(");\n}\n\nprotected abstract void on")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(expression().output(literal(", ")).output(placeholder("optionParameter", "signature").multiple(", "))).output(literal(");")));
		rules.add(rule().condition(trigger("getter")).output(literal("protected ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name")).output(literal("() {\n\treturn schema.")).output(placeholder("name")).output(literal("();\n}")));
		rules.add(rule().condition(trigger("optiongetter")).output(placeholder("optionParameter", "optionParameterGetter").multiple("\n\n")));
		rules.add(rule().condition(trigger("optionparametergetter")).output(literal("protected ")).output(placeholder("type")).output(literal(" ")).output(placeholder("owner", "firstLowerCase")).output(placeholder("name", "FirstUpperCase")).output(literal("() {\n\treturn schema.")).output(placeholder("owner", "firstLowerCase")).output(literal("().")).output(placeholder("name", "FirstLowerCase")).output(literal("();\n}")));
		rules.add(rule().condition(trigger("call")).output(placeholder("name", "firstLowerCase")).output(literal("()")));
		rules.add(rule().condition(trigger("optioncall")).output(placeholder("owner", "firstLowerCase")).output(placeholder("name", "firstUpperCase")).output(literal("()")));
		rules.add(rule().condition(trigger("signature")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "firstLowerCase")));
		rules.add(rule().condition(trigger("id")).output(literal("schema.")).output(placeholder("name", "firstLowerCase")).output(literal("().id")));
		rules.add(rule().condition(trigger("getteruri")).output(literal("if (schema.")).output(placeholder("name", "firstLowerCase")).output(literal("().id.equals(id)) return \"")).output(placeholder("name", "firstLowerCase")).output(literal("\";")));
		rules.add(rule().condition(trigger("optiongetteruri")).output(literal("public String ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\treturn BaseUri + Path + schema.")).output(placeholder("name", "firstLowerCase")).output(literal("().id;\n}")));
		rules.add(rule().condition(trigger("map")).output(literal("\"")).output(placeholder("name", "firstLowerCase")).output(literal("\", schema.")).output(placeholder("name", "firstLowerCase")).output(literal("().id")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}