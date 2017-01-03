package io.intino.pandora.plugin.codegeneration.accessor.jmx;

import org.siani.itrules.LineSeparator;
import org.siani.itrules.Template;

import java.util.Locale;

import static org.siani.itrules.LineSeparator.LF;

public class JMXAccessorTemplate extends Template {

	protected JMXAccessorTemplate(Locale locale, LineSeparator separator) {
		super(locale, separator);
	}

	public static Template create() {
		return new JMXAccessorTemplate(Locale.ENGLISH, LF).define();
	}

	public Template define() {
		add(
				rule().add((condition("type", "accessor"))).add(literal("package ")).add(mark("package", "ValidPackage")).add(literal(";\n\nimport java.util.List;\nimport java.util.ArrayList;\nimport io.intino.pandora.exceptions.*;\nimport ")).add(mark("package", "ValidPackage")).add(literal(".jmx.")).add(mark("name", "validName", "firstUpperCase")).add(literal("MBean;\nimport io.intino.pandora.jmx.JMXClient;\n\n")).add(mark("schemaImport")).add(literal("\n\npublic class ")).add(mark("name", "validName", "firstUpperCase")).add(literal("JMXAccessor {\n\n\tprivate ")).add(mark("name", "validName", "firstUpperCase")).add(literal("MBean bean;\n\n\tpublic ")).add(mark("name", "validName", "firstUpperCase")).add(literal("JMXAccessor(String url, int port) throws java.io.IOException {\n\t\tJMXClient server = new JMXClient(url, port);\n\t\tJMXClient.JMXConnection connection = server.connect();\n\t\tbean = connection.mBean(")).add(mark("name", "validName", "firstUpperCase")).add(literal("MBean.class);\n\t}\n\n\t")).add(mark("operation").multiple("\n\n")).add(literal("\n}")),
			rule().add((condition("type", "operation"))).add(literal("public ")).add(mark("returnType", "firstUpperCase", "ReturnTypeFormatter")).add(literal(" ")).add(mark("name", "validName", "firstLowerCase")).add(literal("(")).add(mark("parameter", "signature").multiple("\n")).add(literal(") {\n\treturn bean != null ? bean.")).add(mark("name", "validName", "firstLowerCase")).add(literal("(")).add(mark("parameter", "name").multiple("\n")).add(literal(") : null;\n}")),
			rule().add((condition("type", "parameter")), (condition("trigger", "signature"))).add(mark("type")).add(literal(" ")).add(mark("name", "validName", "firstLowerCase")),
			rule().add((condition("type", "parameter")), (condition("trigger", "name"))).add(mark("name", "validName", "firstLowerCase")),
			rule().add((condition("type", "schemaImport")), (condition("trigger", "schemaImport"))).add(literal("import ")).add(mark("package")).add(literal(".schemas.*;"))
		);
		return this;
	}
}