package io.intino.konos.builder.codegeneration.accessor.jmx;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.literal;
import static io.intino.itrules.template.outputs.Outputs.placeholder;

public class JMXAccessorTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("accessor")).output(literal("package ")).output(placeholder("package", "ValidPackage")).output(literal(";\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.jmx.JMXClient;\nimport ")).output(placeholder("package", "ValidPackage")).output(literal(".jmx.")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("MBean;\n\n")).output(placeholder("schemaImport")).output(literal("\n\npublic class ")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("JMXAccessor {\n\n\tprivate final JMXClient.JMXConnection connection;\n\tprivate ")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("MBean bean;\n\n\tpublic ")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("JMXAccessor(String url, int port) throws java.io.IOException {\n\t\tJMXClient server = new JMXClient(url, port);\n\t\tconnection = server.connect();\n\t\tbean = connection.mBean(")).output(placeholder("name", "validName", "firstUpperCase")).output(literal("MBean.class);\n\t}\n\n\tpublic void closeJMXConnection() {\n\t\tconnection.close();\n\t}\n\n\t")).output(placeholder("operation").multiple("\n\n")).output(literal("\n}")));
		rules.add(rule().condition(allTypes("operation")).output(literal("public ")).output(placeholder("returnType", "firstUpperCase", "ReturnTypeFormatter")).output(literal(" ")).output(placeholder("name", "validName", "firstLowerCase")).output(literal("(")).output(placeholder("parameter", "signature").multiple(", ")).output(literal(") {\n\treturn bean != null ? bean.")).output(placeholder("name", "validName", "firstLowerCase")).output(literal("(")).output(placeholder("parameter", "name").multiple(", ")).output(literal(") : null;\n}")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("signature"))).output(placeholder("type")).output(literal(" ")).output(placeholder("name", "validName", "firstLowerCase")));
		rules.add(rule().condition(all(allTypes("parameter"), trigger("name"))).output(placeholder("name", "validName", "firstLowerCase")));
		rules.add(rule().condition(all(allTypes("schemaImport"), trigger("schemaimport"))).output(literal("import ")).output(placeholder("package")).output(literal(".schemas.*;")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}