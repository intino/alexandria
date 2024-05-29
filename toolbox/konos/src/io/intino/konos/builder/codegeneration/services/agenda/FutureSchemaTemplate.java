package io.intino.konos.builder.codegeneration.services.agenda;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class FutureSchemaTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("future")).output(literal("package ")).output(placeholder("package", "validPackage")).output(literal(".agenda;\n\n")).output(placeholder("schemaImport")).output(literal("\nimport ")).output(placeholder("package", "validPackage")).output(literal(".AgendaService;\nimport java.time.Instant;\nimport java.time.temporal.ChronoUnit;\n\npublic class ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema {\n\t")).output(expression().output(placeholder("parameter", "field").multiple("\n"))).output(literal("\n\t")).output(expression().output(placeholder("option", "field").multiple("\n"))).output(literal("\n\tTimeout timeout;\n\n\t")).output(expression().output(placeholder("parameter", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(placeholder("parameter", "setter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(placeholder("option", "getter").multiple("\n\n"))).output(literal("\n\n\t")).output(expression().output(placeholder("option", "setter").multiple("\n\n"))).output(literal("\n\n\tpublic Timeout timeout() {\n\t\treturn timeout == null ? timeout = new Timeout(null) : timeout;\n\t}\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("Schema timeout(Timeout timeout) {\n\t\tthis.timeout = timeout;\n\t\treturn this;\n\t}\n\n\t")).output(placeholder("option", "class").multiple("\n\n")).output(literal("\n\n\tpublic static class Timeout extends AgendaService.Option {\n\t\tInstant timeout;\n\n\t\tpublic Timeout(Instant timeout) {\n\t\t\tthis.timeout = timeout == null ? null : timeout.truncatedTo(ChronoUnit.SECONDS);\n\t\t}\n\n\t\tpublic Instant on() {\n\t\t\treturn this.timeout;\n\t\t}\n\t}\n}")));
		rules.add(rule().condition(all(allTypes("option"), trigger("class"))).output(literal("public static class ")).output(placeholder("name", "firstUpperCase")).output(literal(" extends AgendaService.Option {\n\t")).output(expression().output(placeholder("optionParameter", "field").multiple("\n"))).output(literal("\n\n\tpublic ")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("optionParameter", "signature").multiple(", ")).output(literal(") {\n\t\t")).output(expression().output(placeholder("optionParameter", "assign").multiple("\n"))).output(literal("\n\t}\n\n\t")).output(expression().output(placeholder("optionParameter", "getter").multiple("\n\n"))).output(literal("\n}")));
		rules.add(rule().condition(all(allTypes("option"), trigger("getter"))).output(literal("public ")).output(placeholder("future", "firstUpperCase")).output(literal("Schema.")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(placeholder("name", "firstLowerCase")).output(literal(" == null ? ")).output(placeholder("name", "firstLowerCase")).output(literal(" = new ")).output(placeholder("name", "firstUpperCase")).output(literal("(")).output(placeholder("optionParameter", "nullParameter").multiple(", ")).output(literal(") : ")).output(placeholder("name", "firstLowerCase")).output(literal(";\n}")));
		rules.add(rule().condition(trigger("getter")).output(literal("public ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal("() {\n\treturn ")).output(placeholder("name", "firstLowerCase")).output(literal(";\n}")));
		rules.add(rule().condition(all(allTypes("option"), trigger("setter"))).output(literal("public ")).output(placeholder("future", "firstUpperCase")).output(literal("Schema ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("name", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "firstLowerCase")).output(literal(" = ")).output(placeholder("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(trigger("setter")).output(literal("public ")).output(placeholder("owner", "firstUpperCase")).output(literal("Schema ")).output(placeholder("name", "firstLowerCase")).output(literal("(")).output(placeholder("type", "firstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal(") {\n\tthis.")).output(placeholder("name", "firstLowerCase")).output(literal(" = ")).output(placeholder("name", "firstLowerCase")).output(literal(";\n\treturn this;\n}")));
		rules.add(rule().condition(trigger("assign")).output(literal("this.")).output(placeholder("name")).output(literal(" = ")).output(placeholder("name")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("field"))).output(literal("private ")).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal(";")));
		rules.add(rule().condition(all(allTypes("option"), trigger("field"))).output(literal("private ")).output(placeholder("name", "FirstUpperCase")).output(literal(" ")).output(placeholder("name", "firstLowerCase")).output(literal(";")));
		rules.add(rule().condition(trigger("signature")).output(placeholder("type")).output(literal(" ")).output(placeholder("name")));
		rules.add(rule().condition(allTypes("schemaImport")).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		rules.add(rule().condition(trigger("nullparameter")).output(literal("null")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}