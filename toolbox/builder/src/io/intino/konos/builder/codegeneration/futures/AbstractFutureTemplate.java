package io.intino.konos.builder.codegeneration.futures;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class AbstractFutureTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
			rule().condition((type("future"))).output(literal("package ")).output(mark("package", "validPackage")).output(literal(".agenda;\n\n")).output(mark("schemaImport")).output(literal("\nimport io.intino.alexandria.logger.Logger;\n\nimport java.util.List;\nimport java.util.Map;\n\nimport static  ")).output(mark("package", "validPackage")).output(literal(".AgendaService.BaseUri;\n\npublic abstract class Abstract")).output(mark("name", "FirstUpperCase")).output(literal(" {\n\tprotected ")).output(mark("name", "FirstUpperCase")).output(literal("Schema schema;\n\n\tpublic void schema(")).output(mark("name", "FirstUpperCase")).output(literal("Schema schema) {\n\t\tthis.schema = schema;\n\t}\n\n\t")).output(mark("option", "methods").multiple("\n\n")).output(literal("\n\n\tpublic void timeout() {\n\t\tonTimeout(")).output(mark("parameter", "call").multiple(",")).output(literal(");\n\t}\n\n\tprotected abstract void onTimeout(")).output(mark("parameter", "signature").multiple(",")).output(literal(");\n\n\t")).output(mark("parameter", "getter").multiple("\n\n")).output(literal("\n\n\t")).output(mark("option", "optionGetter").multiple("\n\n")).output(literal("\n\n\tpublic URI uri() {\n\t\tif (schema == null) Logger.error(\"Future is not loaded\");\n\t\treturn new URI();\n\t}\n\n\tpublic class URI {\n\t\tpublic static final String Path = \"")).output(mark("name", "camelCaseToSnakeCase")).output(literal("/\";\n\n\t\tpublic List<String> ids() {\n\t\t\treturn List.of(")).output(mark("option", "id").multiple(", ")).output(literal(");\n\t\t}\n\n\t\tpublic String option(String id) {\n\t\t\t")).output(mark("option", "getterURI").multiple("\n")).output(literal("\n\t\t\treturn null;\n\t\t}\n\n\t\tpublic Map<String, String> options() {\n\t\t\treturn Map.of(")).output(mark("option", "map").multiple(",")).output(literal(");\n\t\t}\n\n\t\t")).output(mark("option", "optionGetterURI").multiple("\n")).output(literal("\n\t}\n}")),
			rule().condition((trigger("methods"))).output(literal("public void ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\ton")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("parameter", "call").multiple(", ")).output(expression().output(literal(", ")).output(mark("optionParameter", "optionCall").multiple(", "))).output(literal(");\n}\n\nprotected abstract void on")).output(mark("name", "firstUpperCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(expression().output(literal(", ")).output(mark("optionParameter", "signature").multiple(", "))).output(literal(");")),
			rule().condition((trigger("getter"))).output(literal("protected String ")).output(mark("name")).output(literal("() {\n\treturn schema.")).output(mark("name")).output(literal("();\n}")),
			rule().condition((trigger("optiongetter"))).output(mark("optionParameter", "optionParameterGetter").multiple("\n\n")),
			rule().condition((trigger("optionparametergetter"))).output(literal("protected ")).output(mark("type")).output(literal(" ")).output(mark("owner", "firstLowerCase")).output(mark("name", "FirstUpperCase")).output(literal("() {\n\treturn schema.")).output(mark("owner", "firstLowerCase")).output(literal("().")).output(mark("name", "FirstLowerCase")).output(literal("();\n}")),
			rule().condition((trigger("call"))).output(mark("name", "firstLowerCase")).output(literal("()")),
			rule().condition((trigger("optioncall"))).output(mark("owner", "firstLowerCase")).output(mark("name", "firstUpperCase")).output(literal("()")),
			rule().condition((trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name", "firstLowerCase")),
			rule().condition((trigger("id"))).output(literal("schema.")).output(mark("name", "firstLowerCase")).output(literal("().id")),
			rule().condition((trigger("getteruri"))).output(literal("if (schema.")).output(mark("name", "firstLowerCase")).output(literal("().id.equals(id)) return \"")).output(mark("name", "firstLowerCase")).output(literal("\";")),
			rule().condition((trigger("optiongetteruri"))).output(literal("public String ")).output(mark("name", "firstLowerCase")).output(literal("() {\n\treturn BaseUri + Path + schema.")).output(mark("name", "firstLowerCase")).output(literal("().id;\n}")),
			rule().condition((trigger("map"))).output(literal("\"")).output(mark("name", "firstLowerCase")).output(literal("\", schema.")).output(mark("name", "firstLowerCase")).output(literal("().id")),
			rule().condition((type("schemaImport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}