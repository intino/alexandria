package io.intino.konos.builder.codegeneration.accessor.jmx;

import io.intino.itrules.RuleSet;
import io.intino.itrules.Template;

public class JMXAccessorTemplate extends Template {

	public RuleSet ruleSet() {
		return new RuleSet().add(
				rule().condition((type("accessor"))).output(literal("package ")).output(mark("package", "ValidPackage")).output(literal(";\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.alexandria.exceptions.*;\nimport io.intino.alexandria.jmx.JMXClient;\nimport ")).output(mark("package", "ValidPackage")).output(literal(".jmx.")).output(mark("name", "validName", "firstUpperCase")).output(literal("MBean;\n\n")).output(mark("schemaImport")).output(literal("\n\npublic class ")).output(mark("name", "validName", "firstUpperCase")).output(literal("JMXAccessor {\n\n\tprivate final JMXClient.JMXConnection connection;\n\tprivate ")).output(mark("name", "validName", "firstUpperCase")).output(literal("MBean bean;\n\n\tpublic ")).output(mark("name", "validName", "firstUpperCase")).output(literal("JMXAccessor(String url, int port) throws java.io.IOException {\n\t\tJMXClient server = new JMXClient(url, port);\n\t\tconnection = server.connect();\n\t\tbean = connection.mBean(")).output(mark("name", "validName", "firstUpperCase")).output(literal("MBean.class);\n\t}\n\n\tpublic void closeJMXConnection() {\n\t\tconnection.close();\n\t}\n\n\t")).output(mark("operation").multiple("\n\n")).output(literal("\n}")),
				rule().condition((type("operation"))).output(literal("public ")).output(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).output(literal(" ")).output(mark("name", "validName", "firstLowerCase")).output(literal("(")).output(mark("parameter", "signature").multiple(", ")).output(literal(") {\n\treturn bean != null ? bean.")).output(mark("name", "validName", "firstLowerCase")).output(literal("(")).output(mark("parameter", "name").multiple(", ")).output(literal(") : null;\n}")),
				rule().condition((type("parameter")), (trigger("signature"))).output(mark("type")).output(literal(" ")).output(mark("name", "validName", "firstLowerCase")),
				rule().condition((type("parameter")), (trigger("name"))).output(mark("name", "validName", "firstLowerCase")),
				rule().condition((type("schemaimport")), (trigger("schemaimport"))).output(literal("import ")).output(mark("package")).output(literal(".schemas.*;"))
		);
	}
}